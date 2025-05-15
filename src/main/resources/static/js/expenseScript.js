// Global variables
let flatmates = [];
let expenses = [];

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

    // --- Data Fetching ---
    Promise.all([
        fetch('/api/flat/flatmates').then(res => res.json()),
        fetch('/api/flat/expenses').then(res => res.json())
    ]).then(([flatmateData, expenseData]) => {
        flatmates = flatmateData;  // Full objects (keep id + username)
        expenses = expenseData.map(e => ({
            id: e.id,
            name: e.name,
            amount: parseFloat(e.totalAmount),
            description: e.description || '',
            delegations: {} // will be filled per-expense later
        }));
        renderExpenses();
    }).catch(err => {
        console.error("Failed to load initial data", err);
    });


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

            if (!isVisible && !sliderContainer.hasChildNodes()) {
                fetch(`/api/flat/expense/delegations?expenseId=${expense.id}`)
                    .then(res => res.json())
                    .then(data => {
                        data.forEach(d => {
                            expense.delegations[d.flatmate.id] = d.amount;
                        });
                        createSlidersForExpense(expense, sliderContainer);
                    })
                    .catch(err => console.error("Failed to load delegations", err));
            }
        });

        card.appendChild(cardHeader);
        card.appendChild(slidersContainer);
        return card;
    }


    function createSlidersForExpense(expense, container) {
        container.innerHTML = '';
        let totalAmount = expense.amount;
        let numPeople = flatmates.length;

        let rentValues = flatmates.map(flatmate => {
            const amount = expense.delegations[flatmate.id] || 0;
            return (amount / totalAmount) * 100;
        });
        let sliderLocked = Array(numPeople).fill(false);

        const totalDisplay = document.createElement('div');
        totalDisplay.classList.add('expense-split-total');
        container.appendChild(totalDisplay);

        flatmates.forEach((flatmate, i) => {
            const sliderDiv = document.createElement('div');
            sliderDiv.classList.add('slider-container', 'expense-slider');

            const nameLabel = document.createElement('span');
            nameLabel.textContent = flatmate.username;
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
            amountInput.value = (totalAmount * rentValues[i] / 100).toFixed(2);
            amountInput.min = '0';
            amountInput.max = totalAmount.toFixed(2);
            amountInput.step = '0.01';
            sliderValueContainer.textContent = '$';
            sliderValueContainer.appendChild(amountInput);

            slider.addEventListener('input', function () {
                rentValues[i] = parseFloat(this.value);
                adjustSliders(i, rentValues, sliderLocked, numPeople);
                updateSliderAmountsAndTotal(expense, rentValues, container, totalAmount);
            });

            amountInput.addEventListener('change', function () {
                rentValues[i] = (parseFloat(this.value) / totalAmount) * 100;
                adjustSliders(i, rentValues, sliderLocked, numPeople);
                updateSliderAmountsAndTotal(expense, rentValues, container, totalAmount);
            });

            sliderDiv.appendChild(slider);
            sliderDiv.appendChild(sliderValueContainer);
            container.appendChild(sliderDiv);
        });

        const saveButton = document.createElement('button');
        saveButton.textContent = '💾 Save';
        saveButton.classList.add('save-delegation-btn');
        saveButton.addEventListener('click', () => saveDelegations(expense));
        container.appendChild(saveButton);

        updateSliderAmountsAndTotal(expense, rentValues, container, totalAmount);
    }

    function adjustSliders(changedIndex, rentValues, sliderLocked, numPeople) {
        let sum = rentValues.reduce((acc, val) => acc + val, 0);
        let difference = sum - 100;
        if (Math.abs(difference) < 0.01) return;

        let adjustableSliders = [];
        for (let i = 0; i < numPeople; i++) {
            if (i !== changedIndex && !sliderLocked[i]) {
                adjustableSliders.push(i);
            }
        }

        if (adjustableSliders.length === 0) return;

        let adjustment = difference / adjustableSliders.length;
        adjustableSliders.forEach(i => {
            rentValues[i] -= adjustment;
            rentValues[i] = Math.max(0, Math.min(100, rentValues[i]));
        });
    }

    function updateSliderAmountsAndTotal(expense, rentValues, container, totalAmount) {
        const sliders = container.querySelectorAll('.slider');
        const amountInputs = container.querySelectorAll('.slider-value input[type="number"]');

        flatmates.forEach((flatmate, i) => {
            const amount = (totalAmount * rentValues[i]) / 100;
            expense.delegations[flatmate.id] = amount;
            amountInputs[i].value = amount.toFixed(2);
            sliders[i].value = rentValues[i];
        });

        const totalDisplay = container.querySelector('.expense-split-total');
        const delegatedTotal = rentValues.reduce((sum, val) => sum + (val * totalAmount / 100), 0);
        const remaining = totalAmount - delegatedTotal;
        totalDisplay.textContent = `Total: $${totalAmount.toFixed(2)} | Remaining: $${remaining.toFixed(2)}`;
        totalDisplay.style.color = Math.abs(remaining) > 0.01 ? 'red' : 'inherit';
    }


    async function addExpense() {
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

        const payload = {
            name,
            totalAmount: amountStr,
            expenseMonth: new Date().toISOString().split('T')[0] // YYYY-MM-DD
        };

        try {
            const res = await fetch('/api/flat/expense/create', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            const json = await res.json();
            if (json.status === "success") {
                const newExpense = {
                    id: json.data.expenseId,
                    name,
                    amount,
                    description,
                    delegations: {}
                };
                expenses.push(newExpense);
                renderExpenses();
                expenseNameInput.value = '';
                expenseAmountInput.value = '';
                expenseDescriptionInput.value = '';
            } else {
                alert("❌ " + json.message);
            }
        } catch (err) {
            alert("❌ Failed to create expense: " + err.message);
        }
    }

    async function deleteExpense(id) {
        try {
            const res = await fetch(`/api/flat/expense/delete/${id}`, {
                method: 'POST'
            });
            const json = await res.json();
            if (json.status === "success") {
                expenses = expenses.filter(e => e.id !== id);
                renderExpenses();
            } else {
                alert("❌ " + json.message);
            }
        } catch (err) {
            alert("❌ Failed to delete expense: " + err.message);
        }
    }

    async function saveDelegations(expense) {
        const payload = flatmates.map(flatmate => ({
            flatmate: { id: flatmate.id },
            amount: parseFloat((expense.delegations[flatmate.id] || 0).toFixed(2))
        }));
    
        try {
            const res = await fetch(`/api/flat/expense/delegations?expenseId=${expense.id}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });
    
            const json = await res.json();
            if (json.status === "success") {
                // Auto-collapse the sliders
                const card = document.querySelector(`.expense-card[data-id="${expense.id}"]`);
                const sliderContainer = card.querySelector('.expense-sliders-container');
                sliderContainer.style.display = 'none';
    
                // Small success message
                const successMessage = document.createElement('div');
                successMessage.textContent = "Delegations Saved!";
                successMessage.style.color = "green";
                successMessage.style.fontWeight = "bold";
                successMessage.style.textAlign = "center";
                successMessage.style.marginTop = "10px";
                sliderContainer.parentElement.insertBefore(successMessage, sliderContainer);
    
                setTimeout(() => {
                    successMessage.remove();
                }, 1500);
    
            } else {
                alert("❌ " + json.message);
            }
        } catch (err) {
            alert("❌ Failed to save delegations: " + err.message);
        }
    }
    

    // --- Event Listeners ---
    addExpenseBtn.addEventListener('click', addExpense);

    // --- Initial Render ---
    renderExpenses();
});