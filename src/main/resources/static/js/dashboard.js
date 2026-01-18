// Redirect to login if not authenticated
if (!localStorage.getItem('isLoggedIn')) {
    window.location.href = 'index.html';
}

window.addEventListener('load', updateBudget);
window.addEventListener('load', updateOverdue);
window.addEventListener('load', updateIssued);

function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    sidebar.classList.toggle('active');
}

async function updateBudget() {
    try {
        const response = await fetch('http://localhost:8080/api/library/stats');

        if (!response.ok) {
            throw new Error("Server returned " + response.status);
        }

        const result = await response.json();

        const set = (id, value) => {
            const el = document.getElementById(id);
            if (el) el.innerText = value;
        };

        set("monthlyBudget", 60000);  // or result.totalBudget if you add it
        set("budgetLeft", result.budgetLeft);
        set("Spent", result.totalSpent);
        set("totalBooks", result.totalBooks);
        set("books_Issued", result.booksIssued);
        set("Overdue", result.booksIssued);

    } catch (err) {
        console.error(err);
        alert("Cannot fetch values from the server.");
    }
}

async function updateOverdue() {
    try {
        const response = await fetch("http://localhost:8080/api/library/overdue");
        if (!response.ok) throw new Error("Server error " + response.status);

        const result = await response.json();

        const list = document.querySelector(".overdue-container .book-list");
        if (!list) return;

        list.innerHTML = ""; // Clear old overdue list

        result.books.forEach(book => {

            const issueDate = new Date(book.issueDate).toLocaleDateString("en-GB", {
                day: "2-digit", month: "short", year: "numeric"
            });

            const dueDate = new Date(book.dueDate).toLocaleDateString("en-GB", {
                day: "2-digit", month: "short", year: "numeric"
            });

            const html = `
                <div class="book-item status-overdue">
                    <div class="book-details">
                        <h4>${book.userName}</h4>
                        <p><strong>Book:</strong> "${book.bookName}"</p>
                        <p><strong>Issue Date:</strong> ${issueDate}</p>
                        <p><strong>Due Date:</strong> ${dueDate}</p>
                    </div>
                    <div class="book-status">
                        ${book.daysOverdue} Days Overdue<br>₹${book.fine} Fine
                    </div>
                </div>
            `;

            list.insertAdjacentHTML("beforeend", html);
        });

    } catch (err) {
        console.error(err);
        alert("Cannot fetch overdue books from server.");
    }
}

async function updateIssued() {
    try {
        const response = await fetch('http://localhost:8080/api/library/issued/today');
        if (!response.ok) throw new Error("Server returned " + response.status);

        const result = await response.json();

        // Make sure we target the correct container
        const list = document.querySelector(".issued-container .book-list");
        if (!list) return;

        // ❌ DO NOT clear innerHTML — we want to keep previous content

        result.books.forEach(book => {

            const msDiff = new Date(book.dueDate) - new Date();
            const daysLeft = Math.ceil(msDiff / (1000 * 60 * 60 * 24));

            let statusText;
            let statusClass;

            if (daysLeft < 0) {
                statusText = `Overdue by ${Math.abs(daysLeft)} days`;
                statusClass = "status-overdue";
            } else if (daysLeft <= 3) {
                statusText = `${daysLeft} Days Left`;
                statusClass = "status-near-due";
            } else {
                statusText = `${daysLeft} Days Left`;
                statusClass = "status-on-time";
            }

            const issueDate = new Date(book.issuedDate).toLocaleDateString("en-GB", {
                day: "2-digit", month: "short", year: "numeric"
            });

            const dueDate = new Date(book.dueDate).toLocaleDateString("en-GB", {
                day: "2-digit", month: "short", year: "numeric"
            });

            const html = `
                <div class="book-item ${statusClass}">
                    <div class="book-details">
                        <h4>${book.userName}</h4>
                        <p><strong>Book:</strong> "${book.bookName}"</p>
                        <p><strong>Issue Date:</strong> ${issueDate}</p>
                        <p><strong>Due Date:</strong> ${dueDate}</p>
                    </div>
                    <div class="book-status">${statusText}</div>
                </div>
            `;

            list.insertAdjacentHTML("beforeend", html);
        });

    } catch (err) {
        console.error(err);
        alert("Cannot fetch issued books from the server.");
    }
}

function logout() {
    localStorage.removeItem('isLoggedIn');
    window.location.href = 'index.html';
}