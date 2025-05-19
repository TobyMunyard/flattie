// === SIDEBAR TOGGLE ===
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

document.getElementById("image").addEventListener("change", function () {
    const preview = document.getElementById("imagePreview");
    const file = this.files[0];
    if (file) {
        preview.src = URL.createObjectURL(file);
        preview.style.display = "block";
    } else {
        preview.style.display = "none";
    }
});

// Ticket modal logic
function openTicketModal(cell) {
  const row = cell.closest('tr');
  document.getElementById("modalDesc").innerText = row.children[0].innerText;
  document.getElementById("modalLocation").innerText = row.children[1].innerText;
  document.getElementById("modalUrgency").innerText = row.children[2].innerText;
  document.getElementById("modalStatus").innerText = row.children[3].innerText;
  document.getElementById("modalTime").innerText = row.children[4].innerText;
  document.getElementById("modalUser").innerText = row.children[5].innerText;

  const imageLink = row.children[6].querySelector("a");
  const modalImage = document.getElementById("modalImage");
  if (imageLink) {
    modalImage.src = imageLink.href;
    modalImage.style.display = "block";
  } else {
    modalImage.style.display = "none";
  }

  document.getElementById("ticketModal").style.display = "flex";
}

function closeTicketModal() {
  document.getElementById("ticketModal").style.display = "none";
}

// Filtering logic
document.getElementById("urgencyFilter").addEventListener("change", filterTickets);
document.getElementById("statusFilter").addEventListener("change", filterTickets);

function filterTickets() {
  const urgency = document.getElementById("urgencyFilter").value;
  const status = document.getElementById("statusFilter").value;
  const rows = document.querySelectorAll("#ticketTable tbody tr");

  rows.forEach(row => {
    const rowUrgency = row.getAttribute("data-urgency");
    const rowStatus = row.getAttribute("data-status");
    const matchesUrgency = urgency === "ALL" || rowUrgency === urgency;
    const matchesStatus = status === "ALL" || rowStatus === status;
    row.style.display = matchesUrgency && matchesStatus ? "" : "none";
  });
}
