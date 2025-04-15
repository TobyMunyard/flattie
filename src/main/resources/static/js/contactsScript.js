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

document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('maintenance-request-form');
    const ticketList = document.getElementById('ticket-list');
    const descriptionInput = document.getElementById('issue-description');
    const locationInput = document.getElementById('issue-location');
    const urgencySelect = document.getElementById('urgency');

    // Load tickets from localStorage
    let maintenanceTickets = JSON.parse(localStorage.getItem('maintenanceTickets')) || [];

    function renderTickets() {
        ticketList.innerHTML = ''; // Clear existing list
        maintenanceTickets.forEach((ticket, index) => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${ticket.description}</td>
                <td>${ticket.location || 'N/A'}</td>
                <td class="urgency-${ticket.urgency.toLowerCase()}">${ticket.urgency}</td>
                <td>${new Date(ticket.submittedDate).toLocaleDateString()}</td>
                <td>
                    <select class="status-select" data-index="${index}">
                        <option value="Pending" ${ticket.status === 'Pending' ? 'selected' : ''}>Pending</option>
                        <option value="In Progress" ${ticket.status === 'In Progress' ? 'selected' : ''}>In Progress</option>
                        <option value="Completed" ${ticket.status === 'Completed' ? 'selected' : ''}>Completed</option>
                    </select>
                </td>
                <td><button class="delete-ticket-button" data-index="${index}"><i class="fas fa-trash-alt"></i></button></td>
            `;
            ticketList.appendChild(row);
        });

        // Add event listeners for status changes
        document.querySelectorAll('.status-select').forEach(select => {
            select.addEventListener('change', updateTicketStatus);
        });
         // Add event listeners for delete buttons
        document.querySelectorAll('.delete-ticket-button').forEach(button => {
            button.addEventListener('click', deleteTicket);
        });
    }

    function addTicket(event) {
        event.preventDefault(); // Prevent page reload

        const newTicket = {
            description: descriptionInput.value.trim(),
            location: locationInput.value.trim(),
            urgency: urgencySelect.value,
            submittedDate: new Date().toISOString(),
            status: 'Pending' // Default status
        };

        if (newTicket.description) {
            maintenanceTickets.push(newTicket);
            saveTickets();
            renderTickets();
            form.reset(); // Clear the form
        } else {
            alert('Please provide an issue description.');
        }
    }

     function updateTicketStatus(event) {
        const index = event.target.dataset.index;
        const newStatus = event.target.value;
        maintenanceTickets[index].status = newStatus;
        saveTickets();
        // Optionally re-render or just update visually if needed
        // renderTickets(); // Re-rendering ensures consistency
    }

    function deleteTicket(event) {
         // Go up the DOM tree to find the button if the icon was clicked
        const button = event.target.closest('.delete-ticket-button');
         if (button) {
            const index = button.dataset.index;
            maintenanceTickets.splice(index, 1); // Remove ticket from array
            saveTickets();
            renderTickets(); // Re-render the list
        }
    }

    function saveTickets() {
        localStorage.setItem('maintenanceTickets', JSON.stringify(maintenanceTickets));
    }

    form.addEventListener('submit', addTicket);

    // Initial render of tickets
    renderTickets();
});