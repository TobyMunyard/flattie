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
document.getElementById('chore-list').addEventListener('click', function(event) {
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

