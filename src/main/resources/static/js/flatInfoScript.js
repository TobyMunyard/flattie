// This script handles the modal functionality for editing flat information
// This was decoupled from the html page from Rocky
document.addEventListener("DOMContentLoaded", () => {
  const modal = document.getElementById("editModal");
  const openButton = document.querySelector('button[onclick="openModal()"]');
  const closeButtons = modal?.querySelectorAll(
    '.close, .button[type="button"]'
  );

  function openModal() {
    if (modal) {
      modal.style.display = "block";
    }
  }

  function closeModal() {
    if (modal) {
      modal.style.display = "none";
    }
  }

  // Bind open modal
  if (openButton) {
    openButton.addEventListener("click", openModal);
  }

  // Bind close modal
  if (closeButtons) {
    closeButtons.forEach((btn) => btn.addEventListener("click", closeModal));
  }

  // Close modal on outside click
  window.addEventListener("click", (event) => {
    if (event.target === modal) {
      closeModal();
    }
  });

  // Enable check requests button navigation
  const checkRequestsBtn = document.getElementById("checkRequestsBtn");
  if (checkRequestsBtn) {
    const flatId = checkRequestsBtn.dataset.flatid;
    checkRequestsBtn.href = `/pendingRequests.html?flatId=${flatId}`;
  }
});

// Flatmates Modal functionality
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
        console.log("The user id is:" + user.id + "The current logged in user id is" + currentUserId)
        const div = document.createElement("div");
        div.innerHTML = `
                    <p><strong>Name:</strong> ${user.name}</p>
                    <p><strong>Bio:</strong> ${user.bio || "N/A"}</p>
                    <p><strong>Noise Tolerance:</strong> ${user.noiseTolerance ?? "N/A"}</p>
                    <p><strong>Cleanliness:</strong> ${user.cleanliness ?? "N/A"}</p>
                    <p><strong>Role Level:</strong> <span id="role-${user.id}">${user.role}</span></p>
                    ${user.role !== "OWNER" && user.id !== parseInt(currentUserId) ? `
                        <button onclick="toggleRole(${user.id}, '${user.role}')">
                          ${user.role === "ADMIN" ? "Demote to MEMBER" : "Promote to ADMIN"}
                        </button>
                      ` : ''}
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
      openFlatmatesModal(); // Refresh modal
    })
    .catch((err) => {
      alert("Error updating role: " + err.message);
    });
}

if (flatmatesBtn) flatmatesBtn.addEventListener("click", openFlatmatesModal);
if (closeFlatmatesBtn)
  closeFlatmatesBtn.addEventListener("click", closeFlatmatesModal);

// Close modal on outside click
window.addEventListener("click", (event) => {
  if (event.target === flatmatesModal) {
    closeFlatmatesModal();
  }
});
