function openNav() {
    const sidebar = document.getElementById("mySidebar");
    const main = document.getElementById("main");
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

document.addEventListener('DOMContentLoaded', function () {
    const addExpenseBtn = document.getElementById('add-expense-btn');
    const expenseListContainer = document.getElementById('expense-list');
    const expenseNameInput = document.getElementById('expense-name');
    const expenseAmountInput = document.getElementById('expense-amount');
    const expenseDescriptionInput = document.getElementById('expense-description');

    // --- Sample Data ---
    let flatmates = ['Alice', 'Bob', 'Charlie']; // Replace with actual flatmate data later
    let expenses = [
        { id: 1, name: "Groceries", amount: 150.75, description: "Weekly shop at Coles", delegations: { 'Alice': 50.25, 'Bob': 50.25, 'Charlie': 50.25 } },
        { id: 2, name: "Electricity Bill", amount: 95.00, description: "Monthly power", delegations: { 'Alice': 31.67, 'Bob': 31.67, 'Charlie': 31.66 } }
    ];
    let nextExpenseId = 3;

    // --- Functions ---

    function renderExpenses() {
        expenseListContainer.innerHTML = ''; // Clear existing list
        expenses.forEach(expense => {
            const card = createExpenseCard(expense);
            expenseListContainer.appendChild(card);
        });
    }

    function createExpenseCard(expense) {
        const card = document.createElement('div');
        card.classList.add('expense-card');
        card.dataset.id = expense.id;

        const cardHeader = document.createElement('div');
        cardHeader.classList.add('expense-card-header');
        cardHeader.innerHTML = `
                    <h3>${expense.name} - $${expense.amount.toFixed(2)}</h3>
                    <div>
                        <button class="edit-split-btn"><i class="fas fa-sliders-h"></i> Edit Split</button>
                        <button class="delete-expense-btn"><i class="fas fa-trash"></i> Delete</button>
                    </div>
                `;
        if (expense.description) {
            const description = document.createElement('p');
            description.classList.add('expense-description');
            description.textContent = expense.description;
            cardHeader.insertBefore(description, cardHeader.querySelector('div'));
        }


        const slidersContainer = document.createElement('div');
        slidersContainer.classList.add('expense-sliders-container');
        slidersContainer.style.display = 'none'; // Initially hidden

        // Add event listener for delete button
        cardHeader.querySelector('.delete-expense-btn').addEventListener('click', () => {
            deleteExpense(expense.id);
        });

        // Add event listener for edit split button
        cardHeader.querySelector('.edit-split-btn').addEventListener('click', (e) => {
            const sliderContainer = e.target.closest('.expense-card').querySelector('.expense-sliders-container');
            const isVisible = sliderContainer.style.display !== 'none';
            sliderContainer.style.display = isVisible ? 'none' : 'block';
            if (!isVisible && !sliderContainer.hasChildNodes()) { // Only create sliders if not already created and visible
                createSlidersForExpense(expense, sliderContainer);
            }
        });

        card.appendChild(cardHeader);
        card.appendChild(slidersContainer);
        return card;
    }


    function createSlidersForExpense(expense, container) {
        container.innerHTML = ''; // Clear previous sliders if any
        let currentDelegations = { ...expense.delegations }; // Copy delegations for this instance
        let totalAmount = expense.amount;
        let numPeople = flatmates.length;

        // Initialize delegations if they don't exist or don't match flatmates
        if (!currentDelegations || Object.keys(currentDelegations).length !== numPeople) {
            const initialSplit = totalAmount / numPeople;
            currentDelegations = {};
            flatmates.forEach(name => {
                currentDelegations[name] = initialSplit;
            });
            expense.delegations = { ...currentDelegations }; // Update main expense object
        }


        let rentValues = flatmates.map(name => (currentDelegations[name] / totalAmount) * 100); // Calculate percentage for sliders
        let sliderLocked = Array(numPeople).fill(false);

        const totalDisplay = document.createElement('div');
        totalDisplay.classList.add('expense-split-total');
        totalDisplay.textContent = `Total: $${totalAmount.toFixed(2)} | Remaining: $0.00`; // Initial remaining
        container.appendChild(totalDisplay);


        flatmates.forEach((name, i) => {
            const sliderDiv = document.createElement('div');
            sliderDiv.classList.add('slider-container', 'expense-slider'); // Reuse some rent-calc styles

            const nameLabel = document.createElement('span');
            nameLabel.textContent = name;
            nameLabel.style.marginRight = '10px';
            sliderDiv.appendChild(nameLabel);

            const slider = document.createElement('input');
            slider.type = 'range';
            slider.min = '0';
            slider.max = '100';
            slider.value = rentValues[i];
            slider.classList.add('slider');

            const sliderValueContainer = document.createElement('div');
            sliderValueContainer.classList.add('slider-value');

            const amountInput = document.createElement('input');
            amountInput.type = 'number';
            amountInput.value = currentDelegations[name].toFixed(2);
            amountInput.min = '0';
            amountInput.max = totalAmount.toFixed(2);
            amountInput.step = '0.01';
            sliderValueContainer.textContent = '$'; // Prefix for amount input
            sliderValueContainer.appendChild(amountInput);


            slider.addEventListener('input', function () {
                rentValues[i] = parseFloat(this.value);
                adjustSliders(i, rentValues, sliderLocked, numPeople);
                updateSliderAmountsAndTotal(i, rentValues, totalAmount, container, flatmates, expense);
            });

            amountInput.addEventListener('change', function () {
                let enteredAmount = parseFloat(this.value);
                if (isNaN(enteredAmount) || enteredAmount < 0) enteredAmount = 0;
                if (enteredAmount > totalAmount) enteredAmount = totalAmount;
                this.value = enteredAmount.toFixed(2); // Format input

                rentValues[i] = (enteredAmount / totalAmount) * 100;
                adjustSliders(i, rentValues, sliderLocked, numPeople);
                updateSliderAmountsAndTotal(i, rentValues, totalAmount, container, flatmates, expense);

            });


            sliderDiv.appendChild(slider);
            sliderDiv.appendChild(sliderValueContainer);
            // Add lock icon (optional, maybe too complex for now)

            container.appendChild(sliderDiv);
        });
        updateSliderAmountsAndTotal(-1, rentValues, totalAmount, container, flatmates, expense); // Initial update for total/remaining
    }

    function adjustSliders(changedIndex, rentValues, sliderLocked, numPeople) {
        let sum = rentValues.reduce((acc, val) => acc + val, 0);
        let difference = sum - 100;

        if (Math.abs(difference) < 0.01) return; // Allow for small floating point inaccuracies

        let adjustableSliders = [];
        let adjustableSum = 0;

        for (let i = 0; i < numPeople; i++) {
            if (i !== changedIndex && !sliderLocked[i]) {
                adjustableSliders.push(i);
                adjustableSum += rentValues[i];
            }
        }

        if (adjustableSliders.length === 0 || adjustableSum <= 0 && difference > 0) {
            // If no sliders to adjust or trying to increase when sum is 0, force the changed slider back if possible
            rentValues[changedIndex] -= difference;
            rentValues[changedIndex] = Math.max(0, Math.min(100, rentValues[changedIndex]));
            return;
        }

        let totalAdjustment = -difference; // Amount to add/remove across other sliders

        adjustableSliders.forEach(i => {
            let adjustment = (rentValues[i] / adjustableSum) * totalAdjustment;
            let newValue = rentValues[i] + adjustment;
            rentValues[i] = Math.max(0, Math.min(100, newValue)); // Clamp value
        });


        // Final pass to ensure sum is exactly 100 due to clamping/rounding
        sum = rentValues.reduce((acc, val) => acc + val, 0);
        difference = sum - 100;

        if (Math.abs(difference) > 0.01 && adjustableSliders.length > 0) {
            // Distribute remaining difference evenly if small
            let adjustmentPerSlider = difference / adjustableSliders.length;
            adjustableSliders.forEach(i => {
                rentValues[i] -= adjustmentPerSlider;
                rentValues[i] = Math.max(0, Math.min(100, rentValues[i]));
            });
        }
    }


    function updateSliderAmountsAndTotal(changedIndex, rentValues, totalAmount, container, flatmates, expense) {
        const sliders = container.querySelectorAll('.slider');
        const amountInputs = container.querySelectorAll('.slider-value input[type="number"]');
        let currentTotalDelegated = 0;

        flatmates.forEach((name, i) => {
            const delegatedAmount = (totalAmount * rentValues[i]) / 100;
            expense.delegations[name] = delegatedAmount; // Update the main expense object's delegation
            currentTotalDelegated += delegatedAmount;
            amountInputs[i].value = delegatedAmount.toFixed(2);
            sliders[i].value = rentValues[i]; // Ensure slider position matches
        });

        const totalDisplay = container.querySelector('.expense-split-total');
        const remaining = totalAmount - currentTotalDelegated;
        totalDisplay.textContent = `Total: $${totalAmount.toFixed(2)} | Remaining: $${remaining.toFixed(2)}`;
        totalDisplay.style.color = Math.abs(remaining) > 0.01 ? 'red' : 'inherit'; // Highlight if not balanced
    }


    function addExpense() {
        const name = expenseNameInput.value.trim();
        const amountStr = expenseAmountInput.value;
        const description = expenseDescriptionInput.value.trim();

        if (!name || !amountStr) {
            alert('Please enter expense name and amount.');
            return;
        }

        const amount = parseFloat(amountStr);
        if (isNaN(amount) || amount <= 0) {
            alert('Please enter a valid positive amount.');
            return;
        }

        // Default: Split evenly
        const numPeople = flatmates.length;
        const initialSplit = amount / numPeople;
        const delegations = {};
        flatmates.forEach(fmName => {
            delegations[fmName] = initialSplit;
        });


        const newExpense = {
            id: nextExpenseId++,
            name: name,
            amount: amount,
            description: description,
            delegations: delegations
        };

        expenses.push(newExpense);
        renderExpenses();

        // Clear form
        expenseNameInput.value = '';
        expenseAmountInput.value = '';
        expenseDescriptionInput.value = '';
    }

    function deleteExpense(id) {
        expenses = expenses.filter(expense => expense.id !== id);
        renderExpenses();
    }

    // --- Event Listeners ---
    addExpenseBtn.addEventListener('click', addExpense);

    // --- Initial Render ---
    renderExpenses();
});