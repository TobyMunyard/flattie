// === GLOBAL VARIABLES ===
let isDirty = false;
let initialDelegations = [];
let flatmates = []; // Will store flatmate objects with id, username, etc.
let rentValues = [];
let numPeople = 1; // Default number of people since you must be signed in AND in a flat to view the page- will be updated on load 
let personNames = [];
let expenseId = null;
let sliderLocked = [];
let totalRent = 0;

// === SIDEBAR TOGGLE ===
function openNav() {
    const sidebar = document.getElementById("mySidebar");
    const main = document.getElementById("main");
    const openButton = document.querySelector(".openbtn");

    if (sidebar.style.width === "250px") {
        sidebar.style.width = "0";
        main.style.marginLeft = "0";
    } else {
        sidebar.style.width = "250px";
        main.style.marginLeft = "250px";
    }
}

function closeNav() {
    document.getElementById("mySidebar").style.width = "0";
    document.getElementById("main").style.marginLeft = "0";
}

// === STATE UPDATE FUNCTIONS ===
function markDirty() {
    isDirty = true;
    document.getElementById('delegation-controls').style.display = 'block';
}

function clearDirty() {
    isDirty = false;
    document.getElementById('delegation-controls').style.display = 'none';
}

// === DELEGATION PAYLOAD FUNCTION ===
// This function collects the delegation payload for saving to the server.
function collectDelegationPayload() {
    return rentValues.map((value, i) => {
        return {
            flatmate: { id: flatmates[i].id },
            amount: parseFloat((totalRent * value / 100).toFixed(2))
        };
    });
}


// === RENT CALCULATOR ===
// This script handles the dynamic rent calculator functionality, including slider creation, value adjustment, and event handling.
document.addEventListener('DOMContentLoaded', function () {
    const totalRentElement = document.getElementById('totalRent');
    const slidersContainer = document.getElementById('sliders');

    rentValues = Array(numPeople).fill(100 / numPeople); // Initial equal distribution

    // Fetching initial data from the server - this is to prevent race condiditons and stale values
    let rentPromise = fetch('/api/flat/rent').then(res => res.json());
    let flatmatePromise = fetch('/api/flat/flatmates').then(res => res.json());
    let expenseIdPromise = fetch('/api/flat/rent-expense-id').then(res => res.json());


    // Fetch the rent value and flatmates from the server
    Promise.all([rentPromise, flatmatePromise, expenseIdPromise])
        .then(([rentData, flatmateData, id]) => {
            totalRent = parseFloat(rentData);
            expenseId = id;
            totalRentElement.textContent = totalRent.toFixed(2);
            flatmates = flatmateData;
            numPeople = flatmates.length;
            createSliders();

            restoreDelegationsFromServer(); // Restore delegations from server
        })
        .catch(error => {
            console.error("Failed to fetch rent or flatmates:", error);
            // Fallback setup
            totalRent = 404;
            expenseId = 0;
            flatmates = [];
            numPeople = 0;
            personNames = [];
            createSliders();
        });

    function createSliders() {
        console.log("🔄 createSliders called. Flatmates:", flatmates);
        slidersContainer.innerHTML = ''; // Clear existing sliders
        // Reset rentValues and personNames if they are not set or have incorrect length
        if (!rentValues || rentValues.length !== numPeople) {
            rentValues = Array(numPeople).fill(100 / numPeople);
        }
        personNames = flatmates.map(m => m.username);
        // Reset sliderLocked if it is not set or has incorrect length
        if (!sliderLocked || sliderLocked.length !== numPeople) {
            sliderLocked = Array(numPeople).fill(false);
        }

        for (let i = 0; i < numPeople; i++) {
            const sliderContainer = document.createElement('div');
            sliderContainer.classList.add('slider-container');

            const profilePicture = document.createElement('img');
            // This is a placeholder for the profile picture
            // In a real application, you would fetch the actual profile picture URL from the server
            profilePicture.src = `https://i.pravatar.cc/150?img=${i + 1}`;
            profilePicture.alt = 'Profile Picture';
            profilePicture.classList.add('profile-picture');
            sliderContainer.appendChild(profilePicture);

            // Create a div for the username
            const username = document.createElement('p');
            username.classList.add('person-name');
            username.textContent = personNames[i];
            sliderContainer.appendChild(username);

            const slider = document.createElement('input');
            slider.type = 'range';
            slider.min = '0';
            slider.max = '100';
            slider.value = rentValues[i];
            slider.classList.add('slider');

            slider.addEventListener('input', function () {
                rentValues[i] = parseFloat(this.value);
                adjustSliders(i);
                updateSliderValues();
            });
            sliderContainer.appendChild(slider);

            const sliderValueContainer = document.createElement('div');
            sliderValueContainer.classList.add('slider-value');

            const percentageInput = document.createElement('input');
            percentageInput.type = 'number';
            percentageInput.value = (totalRent * rentValues[i] / 100).toFixed(2);
            percentageInput.min = '0';
            percentageInput.max = '100';
            percentageInput.addEventListener('change', function () {
                rentValues[i] = parseFloat(this.value) * 100 / totalRent;
                adjustSliders(i);
                updateSliderValues();
                markDirty();
            });
            sliderValueContainer.textContent = '$';
            sliderValueContainer.appendChild(percentageInput);
            sliderContainer.appendChild(sliderValueContainer);

            const lockIcon = document.createElement('i');
            lockIcon.classList.add('fas', 'fa-unlock', 'slider-lock');
            lockIcon.style.color = sliderLocked[i] ? 'red' : 'green';
            lockIcon.addEventListener('click', () => {
                sliderLocked[i] = !sliderLocked[i];
                lockIcon.classList.toggle('fa-unlock', !sliderLocked[i]);
                lockIcon.classList.toggle('fa-lock', sliderLocked[i]);
                lockIcon.style.color = sliderLocked[i] ? 'red' : 'green';
            });
            sliderContainer.appendChild(lockIcon);

            slidersContainer.appendChild(sliderContainer);
        }
        updateSliderValues();
    }

    function adjustSliders(changedIndex) {
        let sum = rentValues.reduce((acc, val) => acc + val, 0);
        let difference = sum - 100;

        let otherSlidersSum = 0;
        let adjustCount = 0;
        for (let i = 0; i < numPeople; i++) {
            if (i !== changedIndex && !sliderLocked[i]) {
                otherSlidersSum += rentValues[i];
                adjustCount++;
            }
        }
        
        if (difference === 0 || adjustCount === 0 || otherSlidersSum <= 0) return; // No adjustment needed
        
        
        markDirty(); // Mark as dirty when sliders are adjusted
        
        for (let i = 0; i < numPeople; i++) {
            if (i !== changedIndex && !sliderLocked[i]) {
                rentValues[i] -= (rentValues[i] / otherSlidersSum) * difference;
                rentValues[i] = Math.max(0, Math.min(100, rentValues[i])); // Clamp values
            }
        }
    }

    function updateSliderValues() {
        const sliderValueContainers = document.querySelectorAll('.slider-value input[type="number"]');
        for (let i = 0; i < numPeople; i++) {
            sliderValueContainers[i].value = (totalRent * rentValues[i] / 100).toFixed(2);
            const slider = document.querySelectorAll('.slider')[i];
            slider.value = rentValues[i];
        }
        let currentTotalRent = 0
        for (let i = 0; i < numPeople; i++) {
            currentTotalRent += totalRent * rentValues[i] / 100
        }
        totalRentElement.textContent = currentTotalRent.toFixed(2);
        updateValidationStatus()
    }


    function updateValidationStatus() {
        const statusBar = document.getElementById('validationStatus');
        const delegatedTotal = rentValues.reduce((sum, val) => sum + (val * totalRent / 100), 0);
        const isValid = Math.round(delegatedTotal) === Math.round(totalRent);

        document.getElementById('saveDelegationsBtn').disabled = !isValid; // Disable save button if not valid
        document.getElementById('undoDelegationsBtn').disabled = !isDirty; // Disable undo button if not dirty

        if (isValid) {
            statusBar.textContent = `✅ Total Delegated: $${delegatedTotal.toFixed(2)} / $${totalRent.toFixed(2)} — Ready to save`;
            statusBar.classList.add('valid');
            statusBar.classList.remove('invalid');
        } else {
            const diff = delegatedTotal - totalRent;
            const sign = diff > 0 ? 'Overbudget' : 'Underbudget';
            statusBar.textContent = `❌ Total Delegated: $${delegatedTotal.toFixed(2)} / $${totalRent.toFixed(2)} — ${sign}`;
            statusBar.classList.add('invalid');
            statusBar.classList.remove('valid');
        }
    }


    // === DELEGATION COLLECTION FUNCTION ===
    function restoreDelegations(delegations) {
        rentValues = delegations.map(d => d.amount / totalRent * 100);
        personNames = delegations.map((d, i) =>
            flatmates.find(f => f.id === d.flatmate.id)?.username || `Person ${i + 1}`
        );
        updateSliderValues(); // Update the sliders with the restored values
        updateValidationStatus();
    }

    // === RESTORE DELEGATIONS FUNCTION ===
    // This function fetches the delegations from the server and restores them in the UI.
    async function restoreDelegationsFromServer() {
        try {
            const res = await fetch(`/api/flat/expense/delegations?expenseId=${expenseId}`);
            const delegations = await res.json();

            if (!delegations || delegations.length === 0) {
                console.log("ℹ️ No saved delegations — using even split.");
                rentValues = Array(numPeople).fill(100 / numPeople);
                updateSliderValues();
                clearDirty();
                return;
            }

            restoreDelegations(delegations);
            clearDirty();
        } catch (err) {
            console.warn("Failed to load delegations from server, resetting to even split.", err);
            rentValues = Array(numPeople).fill(100 / numPeople);
            updateSliderValues();
            clearDirty();
        }
    }


    // === UNDO BUTTON ===
    document.getElementById('undoDelegationsBtn').addEventListener('click', restoreDelegationsFromServer);

    // === SAVE BUTTON ===
    document.getElementById('saveDelegationsBtn').addEventListener('click', async () => {
        const payload = collectDelegationPayload();

        try {
            const response = await fetch(`/api/flat/expense/delegations?expenseId=${expenseId}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload),
                credentials: 'same-origin'
            });

            if (!response.ok) {
                const errorMsg = await response.text();
                alert("❌ Save failed: " + errorMsg);
                return;
            }

            clearDirty();
            document.getElementById('saveStatus').style.display = 'block';
            setTimeout(() => document.getElementById('saveStatus').style.display = 'none', 2000);
        } catch (err) {
            alert("❌ Could not reach server: " + err.message);
        }
    });


    // === FALLBACK CONTROL ===
    window.addEventListener("beforeunload", function (e) {
        if (isDirty) {
            e.preventDefault();
            e.returnValue = "You have unsaved changes. Are you sure you want to leave?"; // returnValue is marked as deprecated
        }
    });
});