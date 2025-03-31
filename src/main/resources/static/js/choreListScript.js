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

// document.getElementById('add-chore').addEventListener('click', function () {
//     const choreName = document.getElementById('chore-input').value;
//     const assignment = document.getElementById('assignment').value;
//     const priority = document.getElementById('priority').value;
//     const frequency = document.getElementById('frequency').value;

//     fetch('/choreList/add', {
//         method: 'POST',
//         headers: { 'Content-Type': 'application/json' },
//         body: JSON.stringify({
//             choreName: choreName,
//             assignment: assignment,
//             priority: priority,
//             frequency: frequency
//         })
//     }).then(response => response.json())
//         .then(data => {
//             if (data.success) {
//                 // Test alert to confirm success
//                 alert('Chore added successfully!');
//                 // Update chore list dynamically
//                 addChoreToTable(data.chore);
//             } else {
//                 alert('Failed to add chore');
//             }
//         });
// });

function addChoreToTable(chore) {
    const table = document.getElementById('chore-list');
    const row = document.createElement('tr');
    row.innerHTML = `
        <td>${chore.choreName}</td>
        <td>${chore.assignment}</td>
        <td>${chore.priority}</td>
        <td><button onclick="editChore(${chore.id})">Edit</button></td>
        <td><button onclick="deleteChore(${chore.id})">Delete</button></td>
    `;
    table.appendChild(row);
}

document.getElementById('search-button').addEventListener('click', function () {
    const query = document.getElementById('search-chore').value;
    fetch(`/chore/search?query=${query}`).then(response => response.json())
        .then(data => {
            // Clear current table and re-add searched chores
            document.getElementById('chore-list').innerHTML = '';
            data.forEach(chore => addChoreToTable(chore));
        });
});

function deleteChore(id) {
    fetch(`/chore/delete/${id}`, { method: 'DELETE' }).then(response => {
        if (response.ok) {
            // Remove from table
            const row = document.querySelector(`#chore-list tr[data-id='${id}']`);
            row.remove();
        }
    });
}
