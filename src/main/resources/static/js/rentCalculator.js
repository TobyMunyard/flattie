// === STATE FLAGS ===
let isDirty = false;
let initialDelegations = [];

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

// === DELEGATION COLLECTION FUNCTION ===
function restoreDelegations(delegations) {
    numPeople = delegations.length;
    rentValues = delegations.map(d => (d.amount / totalRent) * 100);
    personNames = delegations.map((_, i) => `Person ${i + 1}`);
    sliderLocked = Array(numPeople).fill(false);
    createSliders();
}

// === DELEGATION PAYLOAD FUNCTION ===
// This function collects the delegation payload for saving to the server.
function collectDelegationPayload() {
    return rentValues.map((value, i) => {
        return {
            flatmate: { id: i + 1 }, // TEMPORARY ID — replace when real flatmate list is loaded
            amount: parseFloat((totalRent * value / 100).toFixed(2))
        };
    });
}

// === RENT CALCULATOR ===
// This script handles the dynamic rent calculator functionality, including slider creation, value adjustment, and event handling.
document.addEventListener('DOMContentLoaded', function () {
    const totalRentElement = document.getElementById('totalRent');
    const slidersContainer = document.getElementById('sliders');
    const numPeopleInput = document.getElementById('numPeople');
    const increasePeopleButton = document.getElementById('increasePeople');
    const decreasePeopleButton = document.getElementById('decreasePeople');
    let numPeople = parseInt(numPeopleInput.value);
    let rentValues = Array(numPeople).fill(100 / numPeople); // Initial equal distribution
    let totalRent = 0;

    // Fetch the initial rent value from the server
    fetch('/api/flat/rent')
        .then(response => response.json())
        .then(data => {
            totalRent = parseFloat(data);
            totalRentElement.textContent = totalRent.toFixed(2);
            createSliders(); // only create sliders *after* rent is loaded
            console.log(totalRent);
        })
        .catch(error => {
            console.error("Failed to fetch rent:", error);
            totalRent = 404; // fallback default
            totalRentElement.textContent = totalRent.toFixed(2);
            createSliders();
        });

    let personNames = Array(numPeople).fill('').map((_, i) => `Person ${i + 1}`);
    let sliderLocked = Array(numPeople).fill(false);

    function createSliders() {
        slidersContainer.innerHTML = ''; // Clear existing sliders
        rentValues = Array(numPeople).fill(100 / numPeople); // Reset rent values
        personNames = Array(numPeople).fill('').map((_, i) => personNames[i] || `Person ${i + 1}`);
        sliderLocked = Array(numPeople).fill(false);

        for (let i = 0; i < numPeople; i++) {
            const sliderContainer = document.createElement('div');
            sliderContainer.classList.add('slider-container');

            const profilePicture = document.createElement('img');
            profilePicture.src = `https://i.pravatar.cc/150?img=${i + 1}`;
            profilePicture.alt = 'Profile Picture';
            profilePicture.classList.add('profile-picture');
            sliderContainer.appendChild(profilePicture);

            const nameInput = document.createElement('input');
            nameInput.type = 'text';
            nameInput.classList.add('person-name');
            nameInput.value = personNames[i];
            nameInput.addEventListener('change', function () {
                personNames[i] = this.value;
            });
            sliderContainer.appendChild(nameInput);

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
            lockIcon.classList.add('fas', 'fa-lock', 'slider-lock');
            lockIcon.style.color = sliderLocked[i] ? 'green' : 'red';
            lockIcon.addEventListener('click', () => {
                sliderLocked[i] = !sliderLocked[i];
                lockIcon.classList.toggle('fa-lock', !sliderLocked[i]);
                lockIcon.classList.toggle('fa-unlock', sliderLocked[i]);
                lockIcon.style.color = sliderLocked[i] ? 'green' : 'red';
            });
            sliderContainer.appendChild(lockIcon);

            slidersContainer.appendChild(sliderContainer);
        }
        updateSliderValues();
    }

    function adjustSliders(changedIndex) {
        let sum = rentValues.reduce((acc, val) => acc + val, 0);
        let difference = sum - 100;

        if (difference === 0) return; // No adjustment needed
        isDirty = true; // Mark as dirty when sliders are adjusted
        let otherSlidersSum = 0;
        let adjustCount = 0;
        for (let i = 0; i < numPeople; i++) {
            if (i !== changedIndex && !sliderLocked[i]) {
                otherSlidersSum += rentValues[i];
                adjustCount++;
            }
        }

        if (otherSlidersSum <= 0 || adjustCount === 0) {
            return;
        }

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

    increasePeopleButton.addEventListener('click', function () {
        numPeople = parseInt(numPeopleInput.value);
        numPeople++;
        numPeopleInput.value = numPeople;
        createSliders();
    });

    decreasePeopleButton.addEventListener('click', function () {
        numPeople = parseInt(numPeopleInput.value);
        if (numPeople > 1) {
            numPeople--;
            numPeopleInput.value = numPeople;
            createSliders();
        }
    });

    numPeopleInput.addEventListener('change', function () {
        numPeople = parseInt(this.value);
        if (isNaN(numPeople) || numPeople < 1) {
            numPeople = 1;
            this.value = 1;
        }
        createSliders();
    });

    // === FALLBACK CONTROL ===
    window.addEventListener("beforeunload", function (e) {
        if (isDirty) {
            e.preventDefault();
            e.returnValue = "You have unsaved changes. Are you sure you want to leave?"; // returnValue is marked as deprecated
        }
    });
});

// === SAVE BUTTON ===
document.getElementById('saveDelegationsBtn').addEventListener('click', async () => {
    const payload = collectDelegationPayload();

    await fetch(`/api/flat/expense/delegations?expenseId=5`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });

    clearDirty();
    document.getElementById('saveStatus').style.display = 'block';
    setTimeout(() => document.getElementById('saveStatus').style.display = 'none', 2000);
});

// === UNDO BUTTON ===
document.getElementById('undoDelegationsBtn').addEventListener('click', async () => {
    const response = await fetch(`/api/flat/expense/delegations?expenseId=5`);
    const delegations = await response.json();
    restoreDelegations(delegations); // you'll implement this
    clearDirty();
});

// === CSRF TOKEN FETCHER ===
function getCSRFToken() {
    const tokenMeta = document.querySelector('meta[name="_csrf"]');
    return tokenMeta ? tokenMeta.getAttribute('content') : '';
}
