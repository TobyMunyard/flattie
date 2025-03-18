function openNav() {
            const sidebar = document.getElementById("mySidebar");
            const main = document.getElementById("main");
            const openButton = document.querySelector(".openbtn");

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
            const flatGrid = document.getElementById('flat-grid');

            const flats = [
                { name: 'Cozy Corner', code: 'CC123', members: ['John', 'Jane', 'Peter'], image: 'https://source.unsplash.com/random/350x220?flat=1', description: 'A charming flat with a spacious living room and a sunny balcony.' },
                { name: 'Sunny Side Up', code: 'SSU456', members: ['Alice', 'Bob'], image: 'https://source.unsplash.com/random/350x220?flat=2', description: 'Bright and cheerful, this flat features a modern kitchen and a cozy dining area.' },
                { name: 'The Loft', code: 'TL789', members: ['Charlie', 'David', 'Eve'], image: 'https://source.unsplash.com/random/350x220?flat=3', description: 'Located in the heart of the city, this loft offers stunning views and a stylish urban living experience.' }
            ];

            function displayFlats() {
                flatGrid.innerHTML = '';
                flats.forEach(flat => {
                    const card = document.createElement('div');
                    card.classList.add('flat-card');

                    const cardFront = document.createElement('div');
                    cardFront.classList.add('flat-card-front');

                    const flatImage = document.createElement('img');
                    flatImage.classList.add('flat-image');
                    flatImage.src = flat.image;
                    flatImage.alt = flat.name;

                    const flatName = document.createElement('h3');
                    flatName.classList.add('flat-name');
                    flatName.textContent = flat.name;

                    cardFront.appendChild(flatImage);
                    cardFront.appendChild(flatName);

                    const cardBack = document.createElement('div');
                    cardBack.classList.add('flat-card-back');
                    cardBack.classList.add('flat-details');

                    const flatDetailsTitle = document.createElement('h3');
                    flatDetailsTitle.textContent = 'Description';

                    const flatDescription = document.createElement('p');
                    flatDescription.textContent = flat.description;

                    const flatCode = document.createElement('p');
                    flatCode.innerHTML = `<strong>Flat Code:</strong> ${flat.code}`;

                    const flatMembers = document.createElement('p');
                    flatMembers.innerHTML = `<strong>Members:</strong> ${flat.members.join(', ')}`;

                    cardBack.appendChild(flatDetailsTitle);
                    cardBack.appendChild(flatDescription);
                    cardBack.appendChild(flatCode);
                    cardBack.appendChild(flatMembers);

                    card.appendChild(cardFront);
                    card.appendChild(cardBack);

                    flatGrid.appendChild(card);
                });
            }

            displayFlats();
        });