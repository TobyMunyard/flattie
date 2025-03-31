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
loadShoppingList(); // Load items when the page is loaded
  const itemInput = document.getElementById("item-input");
  const itemQuantity = document.getElementById("item-quantity");
  const addButton = document.getElementById("add-button");
  const shoppingList = document.getElementById("shopping-list");

  addButton.addEventListener("click", addItem);
  // Event Listenr
  itemInput.addEventListener("keypress", function (event) {
    if (event.key === "Enter") {
      addItem();
    }
  });
  // Function for adding a new item to the shopping list.
  async function addItem() {
    const itemName = itemInput.value.trim();
    // Only add if the string is not empty
    if (itemName !== "") {
      let quantity = 1;

      // Create the item elements
      const listItem = document.createElement("li");
      listItem.classList.add("list-item");
      const quantityContainer = document.createElement("div");
      quantityContainer.classList.add("quantity-container");

      const decreaseButton = document.createElement("button");
      decreaseButton.classList.add("quantity-button");
      decreaseButton.textContent = "-";
      decreaseButton.addEventListener("click", () => {
        if (quantity > 1) {
          quantity--;
          quantityValue.textContent = quantity;
        }
      });

      const increaseButton = document.createElement("button");
      increaseButton.classList.add("quantity-button");
      increaseButton.textContent = "+";
      increaseButton.addEventListener("click", () => {
        quantity++;
        quantityValue.textContent = quantity;
      });

      const quantityValue = document.createElement("span");
      quantityValue.classList.add("quantity-value");
      quantityValue.textContent = quantity;

      quantityContainer.appendChild(decreaseButton);
      quantityContainer.appendChild(quantityValue);
      quantityContainer.appendChild(increaseButton);

      const itemNameSpan = document.createElement("span");
      itemNameSpan.classList.add("item-name");
      itemNameSpan.textContent = itemName;
      itemNameSpan.addEventListener("click", () => {
        itemNameSpan.classList.toggle("completed");
      });

      listItem.appendChild(itemNameSpan);
      listItem.appendChild(quantityContainer);

      const deleteButton = document.createElement("button");
      deleteButton.classList.add("delete-button");
      deleteButton.textContent = "Delete";
      deleteButton.addEventListener("click", () => deleteItem(listItem));
      listItem.appendChild(deleteButton);
      shoppingList.appendChild(listItem);

      // Send item data to the backend
      const itemData = {
        itemName: itemName,
        quantity: quantity,
      };

      try {
        const response = await fetch(
          "http://localhost:8080/shopping-list/add",
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(itemData),
          }
        );
        const data = await response.json();
        console.log("Item added:", data);

        // loadShoppingList(); // This will reload the entire list of items
      } catch (error) {
        console.error("Error adding item:", error);
      }

      itemInput.value = ""; // Clear input field
    }
  }

  async function loadShoppingList() {
    try {
      const response = await fetch("http://localhost:8080/shopping-list");
      const shoppingListItems = await response.json();
  
      // Clear the current list before reloading
      shoppingList.innerHTML = "";  // This clears any existing list items
  
      // Add each item to the list
      shoppingListItems.forEach(item => {
        const listItem = document.createElement("li");
        listItem.classList.add("list-item");
  
        const quantityContainer = document.createElement("div");
        quantityContainer.classList.add("quantity-container");
  
        const decreaseButton = document.createElement("button");
        decreaseButton.classList.add("quantity-button");
        decreaseButton.textContent = "-";
        decreaseButton.addEventListener("click", () => {
          if (item.quantity > 1) {
            item.quantity--;
            quantityValue.textContent = item.quantity;
          }
        });
  
        const increaseButton = document.createElement("button");
        increaseButton.classList.add("quantity-button");
        increaseButton.textContent = "+";
        increaseButton.addEventListener("click", () => {
          item.quantity++;
          quantityValue.textContent = item.quantity;
        });
  
        const quantityValue = document.createElement("span");
        quantityValue.classList.add("quantity-value");
        quantityValue.textContent = item.quantity;
  
        quantityContainer.appendChild(decreaseButton);
        quantityContainer.appendChild(quantityValue);
        quantityContainer.appendChild(increaseButton);
  
        const itemNameSpan = document.createElement("span");
        itemNameSpan.classList.add("item-name");
        itemNameSpan.textContent = item.itemName;
        itemNameSpan.addEventListener("click", () => {
          itemNameSpan.classList.toggle("completed");
        });
  
        listItem.appendChild(itemNameSpan);
        listItem.appendChild(quantityContainer);
  
        const deleteButton = document.createElement("button");
        deleteButton.classList.add("delete-button");
        deleteButton.textContent = "Delete";
        deleteButton.addEventListener("click", () => deleteItem(listItem));
        listItem.appendChild(deleteButton);
        shoppingList.appendChild(listItem);
      });
    } catch (error) {
      console.error("Error loading shopping list:", error);
    }
  }
  

  // Function to delete an item from both UI and backend
  async function deleteItem(listItem) {
    const itemName = listItem.querySelector(".item-name").textContent;

    try {
      // Call backend to delete item by name (or other identifier)
      const response = await fetch(
        `http://localhost:8080/shopping-list/delete/${itemName}`,
        {
          method: "DELETE",
        }
      );
      if (response.ok) {
        shoppingList.removeChild(listItem);
      } else {
        console.error("Failed to delete item from backend");
      }
    } catch (error) {
      console.error("Error deleting item:", error);
    }
  }
});
