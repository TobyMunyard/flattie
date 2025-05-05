document.addEventListener("DOMContentLoaded", () => {
    const flatId = document.getElementById("flatId").value;

    fetch(`/api/flats/${flatId}/pendingRequestsData`)
        .then(res => res.json())
        .then(data => {
            document.getElementById("flatName").textContent = data.flatName;
            const container = document.getElementById("requestList");

            if (data.requests.length === 0) {
                container.innerHTML = "<p>No pending requests.</p>";
                return;
            }

            data.requests.forEach(req => {
                const div = document.createElement("div");
                div.innerHTML = `
                    <p><strong>${req.username}</strong></p>
                    <button onclick="handleAction('${req.userId}', 'approve')">Approve</button>
                    <button onclick="handleAction('${req.userId}', 'reject')">Reject</button>
                    <hr>
                `;
                container.appendChild(div);
            });
        });
});

function handleAction(userId, action) {
    const flatId = document.getElementById("flatId").value;
    const method = action === "approve" ? "PUT" : "DELETE";
    const url = `/api/flats/${flatId}/members/${userId}/${action}`;

    fetch(url, { method })
        .then(res => {
            if (res.ok) {
                alert(`${action}d user`);
                window.location.reload();
            } else {
                alert("Failed to process request.");
            }
        });
}
