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