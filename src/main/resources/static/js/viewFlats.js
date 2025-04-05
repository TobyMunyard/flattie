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
        { name: 'Cozy Corner', code: 'CC123', address: "123 SomeStreet, AreaPlace", price: "500/week", members: ['John', 'Jane', 'Peter'], image: 'https://cdn.apartmenttherapy.info/image/upload/v1619013756/at/house%20tours/2021-04/Erin%20K/KERR-130-CLARKSON-2R-01-020577-EDIT-WEB.jpg', description: 'A charming flat with a spacious living room and a sunny balcony.' },
        { name: 'Sunny Side Up', code: 'SSU456', address: "321 OtherPlace, PlaceArea", price: "150/month", members: ['Alice', 'Bob'], image: 'https://cloudfront-ap-southeast-2.images.arcpublishing.com/nzme/LSN3ODKTWFCUTBT5TEBBAQ6ARM.jpg', description: 'Bright and cheerful, this flat features a modern kitchen and a cozy dining area.' },
        { name: 'The Loft', code: 'TL789', address: "34 BeansStreet, Can", price: "200/week", members: ['Charlie', 'David', 'Eve'], image: 'https://www.apartmentguide.com/blog/wp-content/uploads/2019/10/flat_hero.jpg', description: 'Located in the heart of the city, this loft offers stunning views and a stylish urban living experience.' }
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

            const flatPanel = document.createElement('div');
            flatPanel.classList.add('flat-panel');

            const editButton = document.createElement('button');
            editButton.classList.add('edit-button');
            editButton.textContent = 'Edit Flat';
            editButton.style.position = 'absolute';
            editButton.style.top = '10px';
            editButton.style.right = '10px';

            flatPanel.appendChild(editButton);

            const panelTitle = document.createElement('h3');
            panelTitle.textContent = flat.name;
            flatPanel.appendChild(panelTitle);

            const addressPara = document.createElement('p');
            addressPara.innerHTML = `<strong>Address:</strong> ${flat.address}`;
            flatPanel.appendChild(addressPara);

            const pricePara = document.createElement('p');
            pricePara.innerHTML = `<strong>Price:</strong> ${flat.price}`;
            flatPanel.appendChild(pricePara);

            const descriptionPara = document.createElement('p');
            descriptionPara.innerHTML = `<strong>Description:</strong> ${flat.description}`;
            flatPanel.appendChild(descriptionPara);

            const membersTitle = document.createElement('h3');
            membersTitle.textContent = 'Members';
            flatPanel.appendChild(membersTitle);

            // Create a list of members (random avatars at the moment)
            const memberList = document.createElement('ul');
            memberList.classList.add('member-list');
            flat.members.forEach((member, index) => {
                const memberItem = document.createElement('li');
                const memberImage = document.createElement('img');
                memberImage.src = `https://i.pravatar.cc/40?img=${index + 5}`;
                memberImage.alt = member;
                memberItem.appendChild(memberImage);
                const memberName = document.createElement('span');
                memberName.textContent = member;
                memberItem.appendChild(memberName);
                memberList.appendChild(memberItem);
            });
            flatPanel.appendChild(memberList);


            card.appendChild(cardFront);
            card.appendChild(flatPanel);

            card.addEventListener('click', function () {
                card.classList.toggle('active');
            });

            flatGrid.appendChild(card);
        });
    }

    displayFlats();
});