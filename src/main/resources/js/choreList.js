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
    const choreInput = document.getElementById('chore-input');
    const assignToSelect = document.getElementById('assign-to');
    const prioritySelect = document.getElementById('priority');
    const addChoreButton = document.getElementById('add-chore');
    const choreList = document.getElementById('chore-list');
    const searchChoreInput = document.getElementById('search-chore');
    const sortBySelect = document.getElementById('sort-by');
    const recurrenceInput = document.getElementById('recurrence');
    const choreTable = document.getElementById('chore-table');
    const searchButton = document.getElementById('search-button');
    const clearSearchButton = document.getElementById('clear-search');

    let chores = [];
    let originalChores = [];
    let searchTerm = '';

    addChoreButton.addEventListener('click', addChore);

    function addChore() {
        const choreName = choreInput.value.trim();
        const assignedTo = assignToSelect.value;
        const priority = prioritySelect.value;
        const recurrence = recurrenceInput.value;

        if (choreName !== '') {
            const chore = {
                name: choreName,
                assignedTo: assignedTo,
                priority: priority,
                recurrence: recurrence,
                completed: false
            };

            chores.push(chore);
            originalChores.push({...chore});
            renderChores();
            choreInput.value = '';
            recurrenceInput.value = 0;
        }
    }

    function renderChores() {
        choreList.innerHTML = '';

        chores.forEach((chore, index) => {
            const choreItem = document.createElement('tr');
            choreItem.classList.add('chore-item');
            choreItem.dataset.index = index;

            const assignedName = chore.assignedTo === 'unassigned' ? 'Unassigned' : chore.assignedTo;

            choreItem.innerHTML = `
                <td><b>${chore.name}</b></td>
                <td><p style="font-style: italic;">${assignedName}</p></td>
                <td><b>${chore.priority}</b></td>
                <td><button class="edit-button">Edit</button></td>
                <td><button class="delete-button">Delete</button></td>
            `;

            choreList.appendChild(choreItem);

            const deleteButton = choreItem.querySelector('.delete-button');
            deleteButton.addEventListener('click', deleteChore);
            const editButton = choreItem.querySelector('.edit-button');
            editButton.addEventListener('click', editChore);
        });
    }

    function deleteChore(event) {
        const choreItem = event.target.parentNode.parentNode;
        const index = choreItem.dataset.index;
        chores.splice(index, 1);
        originalChores.splice(index, 1);
        renderChores();
    }

    function editChore(event) {
        const choreItem = event.target.parentNode.parentNode;
        const index = choreItem.dataset.index;
        const chore = chores[index];

        choreInput.value = chore.name;
        assignToSelect.value = chore.assignedTo;
        prioritySelect.value = chore.priority;
        recurrenceInput.value = chore.recurrence;

        chores.splice(index, 1);
        originalChores.splice(index, 1);
        renderChores();
    }

    sortBySelect.addEventListener('change', () => {
        const sortBy = sortBySelect.value;
        sortChores(sortBy);
    });

    function sortChores(sortBy) {
        switch (sortBy) {
            case 'chore':
                chores.sort((a, b) => a.name.localeCompare(b.name));
                break;
            case 'assignment':
                chores.sort((a, b) => a.assignedTo.localeCompare(b.assignedTo));
                break;
            case 'priority':
                chores.sort((a, b) => a.priority - b.priority);
                break;
        }
        renderChores();
    }

    searchChoreInput.addEventListener('input', (event) => {
      searchTerm = searchChoreInput.value.toLowerCase();
      filterChores();
    });

    searchButton.addEventListener('click', () => {
      searchTerm = searchChoreInput.value.toLowerCase();
      filterChores();
    });

    clearSearchButton.addEventListener('click', () => {
        searchChoreInput.value = '';
        searchTerm = '';
        chores = [...originalChores];
        renderChores();
    });

    function filterChores() {
      if (!searchTerm) {
        chores = [...originalChores];
      } else {
        chores = originalChores.filter(chore => chore.name.toLowerCase().includes(searchTerm));
      }
        renderChores();
    }

    choreTable.querySelectorAll('th').forEach(th => {
        th.addEventListener('click', () => {
            const sortBy = th.dataset.sort;
            sortChores(sortBy);
        });
    });

    originalChores = [...chores]
    renderChores();
});