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

  const addButton = document.getElementById("add-button");
  addButton.addEventListener("click", addItem);

  document.getElementById("item-input").addEventListener("keypress", (event) => {
    if (event.key === "Enter") {
      event.preventDefault(); // Prevent form submission (if inside a form)
      addItem();
    }
  });
});

async function addItem() {
  const itemInput = document.getElementById("item-input");
  const shoppingList = document.getElementById("shopping-list");
  const itemName = itemInput.value.trim();
  if (itemName === "") return; // Prevent adding empty items

  let quantity = 1;

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

  const itemData = { itemName, quantity };

  try {
    const response = await fetch("http://localhost:8080/shopping-list/add", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(itemData),
    });

    if (!response.ok) {
      console.error("Error adding item:", response.statusText);
      shoppingList.removeChild(listItem); // Remove from UI if backend fails
    }
  } catch (error) {
    console.error("Error adding item:", error);
    shoppingList.removeChild(listItem); // Remove from UI if request fails
  }

  itemInput.value = ""; // Clear input field after adding
}

async function loadShoppingList() {
  const shoppingList = document.getElementById("shopping-list");
  try {
    const response = await fetch("http://localhost:8080/shopping-list");
    const shoppingListItems = await response.json();

    shoppingList.innerHTML = ""; // Clear list before reloading

    shoppingListItems.forEach((item) => {
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

async function deleteItem(listItem) {
  const shoppingList = document.getElementById("shopping-list");
  const itemName = listItem.querySelector(".item-name").textContent;

  try {
    const response = await fetch(
      `http://localhost:8080/shopping-list/delete/${encodeURIComponent(itemName)}`,
      { method: "DELETE" }
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

