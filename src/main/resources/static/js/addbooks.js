// Authentication check
if (!localStorage.getItem('isLoggedIn')) {
    window.location.href = 'index.html';
}

function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    sidebar.classList.toggle('active');
}

function logout() {
    localStorage.removeItem('isLoggedIn');
    window.location.href = 'index.html';
}

// Image Preview
document.getElementById('cover').addEventListener('change', function(e) {
    const file = e.target.files[0];
    const preview = document.getElementById('coverPreview');
    if (file) {
        const reader = new FileReader();
        reader.onload = function(event) {
            preview.src = event.target.result;
            preview.style.display = 'block';
        }
        reader.readAsDataURL(file);
    } else {
        preview.style.display = 'none';
    }
});

document.getElementById('addBookForm').addEventListener('submit', async function(e) {
    e.preventDefault();

    const formData = new FormData(this);

    try {
        const response = await fetch("http://localhost:8080/api/library/books_new", {
            method: "POST",
            body: formData
        });

        const result = await response.json();

        if (response.ok) {
            document.getElementById('successMessage').style.display = 'block';
            this.reset();
            document.getElementById('coverPreview').style.display = 'none';

            setTimeout(() => {
                document.getElementById('successMessage').style.display = 'none';
            }, 3000);
        } else {
            alert("Failed to add book: " + result.error);
        }

    } catch (err) {
        console.error(err);
        alert("Server not reachable");
    }
});