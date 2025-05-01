// Adding a new notice to the flat noticeboard
let createNoticeModal = document.getElementById("createNoticeModal");
let createNoticeButton = document.getElementById('createNoticeButton');
let closeModalBtn = document.getElementById("closeModalBtn");

createNoticeButton.onclick = () => createNoticeModal.style.display = "block";
closeModalBtn.onclick = () => createNoticeModal.style.display = "none";

window.onclick = (event) => {
  if (event.target === createNoticeModal) createNoticeModal.style.display = "none";
};

document.getElementById("modalForm").onsubmit = (e) => {
  modal.style.display = "none";
};