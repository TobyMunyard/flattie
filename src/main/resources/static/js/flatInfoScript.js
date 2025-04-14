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
});
