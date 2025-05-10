// Editing account info
let modal = document.getElementById("editProfileModal");
let editButton = document.getElementById('editAccountButton');
let closeBtn = document.getElementById("closeModalBtn");

editButton.onclick = () => modal.style.display = "block";
closeBtn.onclick = () => modal.style.display = "none";

window.onclick = (event) => {
  if (event.target === modal) modal.style.display = "none";
};

document.getElementById("modalForm").onsubmit = (e) => {
  modal.style.display = "none";
};

// Changing password
let changePasswordModal = document.getElementById("changePasswordModal");
let changePasswordButton = document.getElementById('changePasswordButton');
let closePasswordModalBtn = document.getElementById("closePasswordModalBtn");

changePasswordButton.onclick = () => changePasswordModal.style.display = "block";
closePasswordModalBtn.onclick = () => changePasswordModal.style.display = "none";

window.onclick = (event) => {
  if (event.target === changePasswordModal) changePasswordModal.style.display = "none";
};

document.getElementById("changePasswordModalForm").onsubmit = (e) => {
  modal.style.display = "none";
};

// Function to update the displayed value for noise tolerance
function updateNoiseToleranceValue(value) {
    document.getElementById("noiseToleranceValue").textContent = value;
}

// Function to update the displayed value for cleanliness
function updateCleanlinessValue(value) {
    document.getElementById("cleanlinessValue").textContent = value;
}