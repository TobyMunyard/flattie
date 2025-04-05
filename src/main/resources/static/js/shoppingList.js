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

document.addEventListener("DOMContentLoaded", function () {
    loadShoppingList();

    document.getElementById("add-button").addEventListener("click", addItem);

    document.getElementById("item-input").addEventListener("keypress", (event) => {
        if (event.key === "Enter") {
            event.preventDefault();
            addItem();
        }
    });
});

// Fetch and display the shopping list
async function loadShoppingList() {
    const shoppingList = document.getElementById("shopping-list");
    try {
        const response = await fetch("http://localhost:8080/shopping-list");

        if (response.status === 302) {
            // If status is 302, it means the user is being redirected
            const redirectUrl = response.headers.get('Location');
            if (redirectUrl) {
                window.location.href = redirectUrl; // Redirect to the login or join-flat page
            }
        } else if (response.ok) {
            const shoppingListItems = await response.json();
            shoppingList.innerHTML = ""; // Clear previous list

            shoppingListItems.forEach((item) => {
                addItemToDOM(item);
            });
        } else {
            console.error("Failed to load shopping list:", response.statusText);
        }
    } catch (error) {
        console.error("Error loading shopping list:", error);
    }
}

// Add item to the shopping list
async function addItem() {
    const itemNameInput = document.getElementById("item-input");
    const itemName = itemNameInput.value.trim();
    const itemQuantityInput = document.getElementById("item-quantity");
    const quantity = parseInt(itemQuantityInput.value.trim()) || 1;

    if (!itemName) return;

    const itemData = { itemName, quantity };
    console.log("Sending data:", itemData);

    try {
        const response = await fetch("http://localhost:8080/shopping-list/add", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(itemData),
        });

        if (response.ok) {
            const newItem = await response.json();
            addItemToDOM(newItem);
            itemNameInput.value = "";
        } else {
            console.error("Error adding item:", response.statusText);
        }
    } catch (error) {
        console.error("Error adding item:", error);
    }
}

// Add item to the DOM
function addItemToDOM(item) {
    const shoppingList = document.getElementById("shopping-list");

    const listItem = document.createElement("li");
    listItem.classList.add("list-item");
    listItem.setAttribute("data-item-id", item.id);

    // Item name
    const itemNameSpan = document.createElement("span");
    itemNameSpan.classList.add("item-name")
    itemNameSpan.textContent = item.itemName;
    itemNameSpan.addEventListener("click", () => updateItemName(listItem));

    // Quantity controls
    const quantityContainer = document.createElement("div");
    quantityContainer.classList.add("quantity-container");

    const decreaseButton = document.createElement("button");
    decreaseButton.classList.add("quantity-button");
    decreaseButton.textContent = "-";
    decreaseButton.addEventListener("click", () => updateQuantity(listItem, -1));

    const quantityValue = document.createElement("span");
    quantityValue.classList.add("quantity-value");
    quantityValue.textContent = item.quantity;

    const increaseButton = document.createElement("button");
    increaseButton.classList.add("quantity-button");
    increaseButton.textContent = "+";
    increaseButton.addEventListener("click", () => updateQuantity(listItem, 1));

    quantityContainer.append(decreaseButton, quantityValue, increaseButton);

    // Delete button
    const deleteButton = document.createElement("button");
    deleteButton.classList.add("delete-button");
    deleteButton.textContent = "Delete";
    deleteButton.addEventListener("click", () => deleteItem(listItem));

    listItem.append(itemNameSpan, quantityContainer, deleteButton);
    shoppingList.appendChild(listItem);
}

// Update item name
async function updateItemName(listItem) {
    const itemId = listItem.dataset.itemId;
    const itemNameSpan = listItem.querySelector(".item-name");
    const newName = prompt("Enter new item name:", itemNameSpan.textContent);

    if (newName && newName.trim() !== "") {
        try {
            const response = await fetch(`http://localhost:8080/shopping-list/update/${itemId}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ itemName: newName.trim() }),
            });

            if (response.ok) {
                itemNameSpan.textContent = newName.trim();
            } else {
                console.error("Failed to update item name.");
            }
        } catch (error) {
            console.error("Error updating item name:", error);
        }
    }
}

// Update item quantity
async function updateQuantity(listItem, change) {
    const itemId = listItem.dataset.itemId;
    const quantityValue = listItem.querySelector(".quantity-value");
    let newQuantity = parseInt(quantityValue.textContent) + change;

    if (newQuantity < 1) return;

    try {
        const response = await fetch(`http://localhost:8080/shopping-list/update/${itemId}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ quantity: newQuantity }),
        });

        if (response.ok) {
            quantityValue.textContent = newQuantity;
        } else {
            console.error("Failed to update quantity.");
        }
    } catch (error) {
        console.error("Error updating quantity:", error);
    }
}

// Delete item
async function deleteItem(listItem) {
    const shoppingList = document.getElementById("shopping-list");
    const itemId = listItem.dataset.itemId;

    try {
        const response = await fetch(`http://localhost:8080/shopping-list/delete/${itemId}`, {
            method: "DELETE",
        });

        if (response.ok) {
            shoppingList.removeChild(listItem);
        } else {
            console.error("Failed to delete item.");
        }
    } catch (error) {
        console.error("Error deleting item:", error);
    }
}

