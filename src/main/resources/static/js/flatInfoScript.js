// Flat Info Modal JS

document.addEventListener("DOMContentLoaded", () => {
  const modal = document.getElementById("editModal");
  const openButton = document.querySelector('button[onclick="openModal()"]');
  const closeButtons = modal?.querySelectorAll('.close, .button[type="button"]');

  function openModal() {
    if (modal) modal.style.display = "block";
  }

  function closeModal() {
    if (modal) modal.style.display = "none";
  }

  if (openButton) openButton.addEventListener("click", openModal);
  if (closeButtons) closeButtons.forEach((btn) => btn.addEventListener("click", closeModal));

  window.addEventListener("click", (event) => {
    if (event.target === modal) closeModal();
  });
});

const flatmatesModal = document.getElementById("flatmatesModal");
const flatmatesBtn = document.getElementById("viewFlatmatesBtn");
const closeFlatmatesBtn = document.getElementById("closeFlatmatesBtn");
const flatmatesList = document.getElementById("flatmatesList");

function openFlatmatesModal() {
  fetch("/api/flat/flatmates")
    .then((res) => res.json())
    .then((data) => {
      flatmatesList.innerHTML = "";
      data.forEach((user) => {
        let userActions = "";

        if (
          currentUserRole === "OWNER" &&
          user.role !== "OWNER" &&
          user.id !== parseInt(currentUserId)
        ) {
          userActions = `
            <button onclick="toggleRole(${user.id}, '${user.role}')">
              ${user.role === "ADMIN" ? "Demote to MEMBER" : "Promote to ADMIN"}
            </button>
            <form method="POST" action="/api/flat/${currentFlatId}/members/${user.id}/remove" 
                  onsubmit="return confirm('Are you sure you want to remove this flatmate?');" style="display:inline;">
              <button type="submit" style="margin-left: 10px; background-color: #f44336; color: white;">Remove</button>
            </form>
          `;
        }

        const div = document.createElement("div");
        div.innerHTML = `
          <p><strong>Name:</strong> ${user.name}</p>
          <p><strong>Bio:</strong> ${user.bio || "N/A"}</p>
          <p><strong>Noise Tolerance:</strong> ${user.noiseTolerance ?? "N/A"}</p>
          <p><strong>Cleanliness:</strong> ${user.cleanliness ?? "N/A"}</p>
          <p><strong>Role Level:</strong> <span id="role-${user.id}">${user.role}</span></p>
          ${userActions}
          <hr>
        `;
        flatmatesList.appendChild(div);
      });
      flatmatesModal.style.display = "block";
    })
    .catch((err) => {
      alert("Could not load flatmates");
      console.error(err);
    });
}

function closeFlatmatesModal() {
  flatmatesModal.style.display = "none";
}

function toggleRole(userId, currentRole) {
  const newRole = currentRole === "ADMIN" ? "MEMBER" : "ADMIN";

  fetch(`/api/flat/${currentFlatId}/members/${userId}/role?role=${newRole}`, {
    method: "PUT",
  })
    .then((res) => {
      if (!res.ok) throw new Error("Failed to update role");
      return res.text();
    })
    .then((msg) => {
      document.getElementById(`role-${userId}`).innerText = newRole;
      openFlatmatesModal();
    })
    .catch((err) => {
      alert("Error updating role: " + err.message);
    });
}

if (flatmatesBtn) flatmatesBtn.addEventListener("click", openFlatmatesModal);
if (closeFlatmatesBtn) closeFlatmatesBtn.addEventListener("click", closeFlatmatesModal);

window.addEventListener("click", (event) => {
  if (event.target === flatmatesModal) closeFlatmatesModal();
});