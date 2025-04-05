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

function addChoreToTable(chore) {
    const choreList = document.getElementById('chore-list');
    const row = document.createElement('tr');

    row.innerHTML = `
        <td>${chore.choreName}</td>
        <td>${chore.assignment}</td>
        <td>${chore.priority}</td>
        <td><button onclick="editChore(${chore.id})">Edit</button></td>
        <td><button class="delete-btn" data-id="${chore.id}">Delete</button></td>
    `;

    choreList.appendChild(row);
}

// Dynamically add delete button functionality to each created chore
document.getElementById('chore-list').addEventListener('click', function (event) {
    if (event.target && event.target.classList.contains('delete-btn')) {
        const choreId = event.target.getAttribute('data-id');

        fetch('/chore/delete/' + choreId, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    // Remove the row from the table without reloading
                    event.target.closest('tr').remove();
                } else {
                    alert('Failed to delete chore');
                }
            })
            .catch(error => {
                console.error('Error deleting chore:', error);
                alert('Error deleting chore');
            });
    }
});

document.getElementById('search-button').addEventListener('click', function () {
    const query = document.getElementById('search-chore').value;
    fetch(`/chore/search?query=${query}`).then(response => response.json())
        .then(data => {
            // Clear current table and re-add searched chores
            document.getElementById('chore-list').innerHTML = '';
            data.forEach(chore => addChoreToTable(chore));
        });
});

// Function to handle toggling the completion status of a chore
document.addEventListener('click', function (event) {
    // Check if the clicked element is the chore name (the clickable part to toggle completion)
    if (event.target.classList.contains('chore-name')) {
        const choreId = event.target.getAttribute('data-id');

        fetch(`/chore/toggleComplete/${choreId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': getCSRFToken()
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to toggle chore status');
                }

                // Find the row of the clicked chore
                const choreRow = event.target.closest('tr');
                // Find the chore name cell in that row
                const choreNameCell = choreRow.querySelector('.chore-name');
                
                // Toggle the 'completed' class
                choreNameCell.classList.toggle('completed');
            })
            .catch(error => {
                console.error('Error toggling chore status:', error);
                alert('Could not update chore status.');
            });
    }
});

function getCSRFToken() {
    const tokenMeta = document.querySelector('meta[name="_csrf"]');
    return tokenMeta ? tokenMeta.getAttribute('content') : '';
}

