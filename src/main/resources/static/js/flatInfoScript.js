// This script handles the modal functionality for editing flat information
// This was decoupled from the html page from Rocky
document.addEventListener('DOMContentLoaded', () => {
    const modal = document.getElementById('editModal');
    const openButton = document.querySelector('button[onclick="openModal()"]');
    const closeButtons = modal?.querySelectorAll('.close, .button[type="button"]');

    function openModal() {
        if (modal) {
            modal.style.display = 'block';
        }
    }

    function closeModal() {
        if (modal) {
            modal.style.display = 'none';
        }
    }

    // Bind open modal
    if (openButton) {
        openButton.addEventListener('click', openModal);
    }

    // Bind close modal
    if (closeButtons) {
        closeButtons.forEach(btn => btn.addEventListener('click', closeModal));
    }

    // Close modal on outside click
    window.addEventListener('click', (event) => {
        if (event.target === modal) {
            closeModal();
        }
    });

    // Enable check requests button navigation
    const checkRequestsBtn = document.getElementById('checkRequestsBtn');
    if (checkRequestsBtn) {
        const flatId = checkRequestsBtn.dataset.flatid;
        checkRequestsBtn.href = `/pendingRequests.html?flatId=${flatId}`;
    }
});


// Flatmates Modal functionality
const flatmatesModal = document.getElementById('flatmatesModal');
const flatmatesBtn = document.getElementById('viewFlatmatesBtn');
const closeFlatmatesBtn = document.getElementById('closeFlatmatesBtn');
const flatmatesList = document.getElementById('flatmatesList');

function openFlatmatesModal() {
    fetch('/api/flat/flatmates')
        .then(res => res.json())
        .then(data => {
            flatmatesList.innerHTML = '';
            data.forEach(user => {
                const div = document.createElement('div');
                div.innerHTML = `
                    <p><strong>Name:</strong> ${user.name}</p>
                    <p><strong>Bio:</strong> ${user.bio || 'N/A'}</p>
                    <p><strong>Noise Tolerance:</strong> ${user.noiseTolerance ?? 'N/A'}</p>
                    <p><strong>Cleanliness:</strong> ${user.cleanliness ?? 'N/A'}</p>
                    <p><strong>Role Level:</strong> ${user.role}
                    <hr>
                `;
                flatmatesList.appendChild(div);
            });
            flatmatesModal.style.display = 'block';
        })
        .catch(err => {
            alert('Could not load flatmates');
            console.error(err);
        });
}

function closeFlatmatesModal() {
    flatmatesModal.style.display = 'none';
}

if (flatmatesBtn) flatmatesBtn.addEventListener('click', openFlatmatesModal);
if (closeFlatmatesBtn) closeFlatmatesBtn.addEventListener('click', closeFlatmatesModal);

// Close modal on outside click
window.addEventListener('click', (event) => {
    if (event.target === flatmatesModal) {
        closeFlatmatesModal();
    }
});
