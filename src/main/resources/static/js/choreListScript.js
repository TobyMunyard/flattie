let flatmateUsernames = [];

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

// === CHORE TABLE RENDERING ===
function addChoreToTable(chore) {
    const choreList = document.getElementById('chore-list');
    const row = document.createElement('tr');

    row.innerHTML = `
        <td class="chore-name" data-id="${chore.id}">${chore.choreName}</td>
        <td>${chore.assignment}</td>
        <td>${chore.priority}</td>
        <td>
            <button onclick="editChore('${chore.id}', '${chore.choreName}', '${chore.assignment}', '${chore.priority}', '${chore.frequency}')">
                Edit
            </button>
        </td>
        <td><button class="delete-btn" data-id="${chore.id}">Delete</button></td>
    `;

    choreList.appendChild(row);
}

// === DYNAMIC CHORE EDIT HANDLING ===
function updateChoreRow(chore) {
    const row = document.querySelector(`.chore-name[data-id="${chore.id}"]`)?.closest('tr');
    if (!row) return;

    // Update each cell
    row.querySelector('.chore-name').textContent = chore.choreName;
    row.children[1].textContent = chore.assignment;
    row.children[2].textContent = chore.priority;

    // Update edit button with new params
    row.children[3].innerHTML = `
        <button onclick="editChore('${chore.id}', '${chore.choreName}', '${chore.assignment}', '${chore.priority}', '${chore.frequency}')">
            Edit
        </button>
    `;
}

/** === EDIT MODE HANDLING === 
 * Should remove add values into field, change the button text to "Update Chore" 
 * and then on submit, it should send a POST request to the server with the chore ID.
*/
function editChore(id, name, assignment, priority, frequency) {
    // Fill form fields with chore data
    document.getElementById('chore-input').value = name;
    document.getElementById('assignment').value = assignment;
    document.getElementById('priority').value = priority;
    document.getElementById('frequency').value = frequency;
    document.getElementById('edit-id').value = id; // hidden input

    // Change button text to indicate edit mode
    document.getElementById('add-chore').textContent = "Update Chore";
    // show cancel button
    document.getElementById('cancel-edit').style.display = "inline-block";
}

// === AJAX FORM SUBMISSION (Add / Edit) ===
document.getElementById('chore-form').addEventListener('submit', function (e) {
    e.preventDefault();

    const form = document.getElementById('chore-form');
    const choreId = document.getElementById('edit-id').value;
    const formData = new FormData(form);
    const url = choreId ? `/chore/edit/${choreId}` : '/choreList/add';

    // Check if the assignment is "Random" and replace it with a random flatmate username
    // This is done before sending the form data to the server
    const assignment = formData.get('assignment');

    if (assignment === 'Random') {
        if (flatmateUsernames.length > 0) {
            const randomUsername = flatmateUsernames[Math.floor(Math.random() * flatmateUsernames.length)];
            formData.set('assignment', randomUsername); // Replace "Random" with actual flatmate
        } else {
            alert("⚠️ No flatmates available to assign.");
            return;
        }
    }

    fetch(url, {
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': getCSRFToken()
        },
        body: formData
    })
        .then(response => {
            if (!response.ok) throw new Error('Failed to save chore');
            return response.json(); // Expect JSON back from backend
        })
        .then(chore => {
            // Check if choreId is present to determine if it's an edit or add
            if (!choreId) {
                // Dynamically add the new chore row
                addChoreToTable(chore);
            } else {
                updateChoreRow(chore); // Update existing row
            }

            // Reset the form and mode
            form.reset();
            document.getElementById('edit-id').value = ''; // Clear hidden input
            document.getElementById('add-chore').textContent = "Add Chore"; // Reset button text
            document.getElementById('cancel-edit').style.display = "none"; // hide cancel button

        })
        .catch(err => {
            console.error('Error saving chore:', err);
            alert('Failed to save chore.');
        });
});

// === FETCH FLATMATES FOR ASSIGNMENT ===
// This function fetches flatmates from the server and populates the assignment dropdown
document.addEventListener('DOMContentLoaded', () => {
    const assignmentSelect = document.getElementById('assignment');
    const form = document.getElementById('chore-form');
    const errorBanner = document.getElementById('flatmate-error');

    fetch('/api/flat/flatmates')
        .then(response => {
            if (!response.ok) throw new Error("Fetch failed");
            return response.json();
        })
        .then(flatmates => {
            assignmentSelect.innerHTML = `
                <optgroup label="System">
                    <option value="Unassigned">Unassigned</option>
                    <option value="Random">Random</option>
                </optgroup>
                <optgroup label="Flatmates" id="flatmate-options"></optgroup>
            `;

            const flatmateGroup = document.getElementById('flatmate-options');
            flatmates.forEach(fm => {
                const opt = document.createElement('option');
                opt.value = fm.username;
                opt.textContent = fm.username;
                flatmateGroup.appendChild(opt);

                flatmateUsernames.push(fm.username); // Store usernames for Random assignment
            });
        })
        .catch(error => {
            console.error("Failed to fetch flatmates for chore assignment:", error);

            // Show error banner
            errorBanner.style.display = "block";

            // Disable the form
            Array.from(form.elements).forEach(el => el.disabled = true);
        });
});

// === DELETE BUTTON HANDLER ===
document.getElementById('chore-list').addEventListener('click', function (event) {
    if (event.target && event.target.classList.contains('delete-btn')) {
        const choreId = event.target.getAttribute('data-id');

        fetch('/chore/delete/' + choreId, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    event.target.closest('tr').remove(); // Remove row on success
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

// === CANCEL EDIT BUTTON HANDLER ===
document.getElementById('cancel-edit').addEventListener('click', function () {
    // Reset the form and mode
    const form = document.getElementById('chore-form');
    form.reset();
    document.getElementById('edit-id').value = '';
    document.getElementById('add-chore').textContent = "Add Chore";
    this.style.display = "none"; // hide the cancel button again
});


// === TOGGLE COMPLETION ===
document.addEventListener('click', function (event) {
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
                if (!response.ok) throw new Error('Failed to toggle status');

                event.target.classList.toggle('completed'); // Toggle CSS class
            })
            .catch(error => {
                console.error('Error toggling status:', error);
                alert('Could not update chore status.');
            });
    }
});

// === SEARCH FUNCTION ===
document.getElementById('search-button').addEventListener('click', function () {
    const query = document.getElementById('search-chore').value;
    fetch(`/chore/search?query=${query}`)
        .then(response => response.json())
        .then(data => {
            const list = document.getElementById('chore-list');
            list.innerHTML = ''; // Clear old rows
            data.forEach(chore => addChoreToTable(chore)); // Populate results
        });
});

document.getElementById('clear-search').addEventListener('click', function () {
    let query = document.getElementById('search-chore').value;
    if (query) {
        document.getElementById('search-chore').value = ''; // Clear search input
        query = ''; // Reset query
        fetch(`/chore/search?query=${query}`)
            .then(response => response.json())
            .then(data => {
                const list = document.getElementById('chore-list');
                list.innerHTML = ''; // Clear old rows
                data.forEach(chore => addChoreToTable(chore)); // Populate results
            });
    }
});

// === CSRF TOKEN FETCHER ===
function getCSRFToken() {
    const tokenMeta = document.querySelector('meta[name="_csrf"]');
    return tokenMeta ? tokenMeta.getAttribute('content') : '';
}
