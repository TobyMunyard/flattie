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

document.addEventListener('DOMContentLoaded', function () {
    const itemInput = document.getElementById('item-input');
    const addButton = document.getElementById('add-button');
    const shoppingList = document.getElementById('shopping-list');

    addButton.addEventListener('click', addItem);
    // Event Listenr
    itemInput.addEventListener('keypress', function (event) {
        if (event.key === 'Enter') {
            addItem();
        }
    });
    // Function for adding a new item to the shopping list.
    function addItem() {
        const itemName = itemInput.value.trim();
        // Only add if the string is not empty
        if (itemName !== '') {
            const listItem = document.createElement('li');
            listItem.classList.add('list-item');

            let quantity = 1;
            const quantityContainer = document.createElement('div');
            quantityContainer.classList.add('quantity-container');

            const decreaseButton = document.createElement('button');
            decreaseButton.classList.add('quantity-button');
            decreaseButton.textContent = '-';
            decreaseButton.addEventListener('click', () => {
                if (quantity > 1) {
                    quantity--;
                    quantityValue.textContent = quantity;
                }
            });

            const increaseButton = document.createElement('button');
            increaseButton.classList.add('quantity-button');
            increaseButton.textContent = '+';
            increaseButton.addEventListener('click', () => {
                quantity++;
                quantityValue.textContent = quantity;
            });

            const quantityValue = document.createElement('span');
            quantityValue.classList.add('quantity-value');
            quantityValue.textContent = quantity;

            quantityContainer.appendChild(decreaseButton);
            quantityContainer.appendChild(quantityValue);
            quantityContainer.appendChild(increaseButton);

            const itemNameSpan = document.createElement('span');
            itemNameSpan.classList.add('item-name');
            itemNameSpan.textContent = itemName;
            itemNameSpan.addEventListener('click', () => {
                itemNameSpan.classList.toggle('completed');
            });

            listItem.appendChild(itemNameSpan);
            listItem.appendChild(quantityContainer);

            const deleteButton = document.createElement('button');
            deleteButton.classList.add('delete-button');
            deleteButton.textContent = 'Delete';
            deleteButton.addEventListener('click', deleteItem);

            listItem.appendChild(deleteButton);
            shoppingList.appendChild(listItem);
            itemInput.value = '';
        }
    }

    async function loadShoppingList() {

    }

    async function deleteItem(id){
        
    }

    function deleteItem(event) {
        const listItem = event.target.parentNode;
        shoppingList.removeChild(listItem);
    }
});