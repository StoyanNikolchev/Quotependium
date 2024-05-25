function toggleLike(button, quoteId) {
    fetch('/like-quote/' + quoteId, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content')
        }
    })
        .then(response => {
            if (response.ok) {
                if (button) {
                    button.textContent = button.textContent === "Like" ? "Liked" : "Like";
                    button.classList.toggle('like-button');
                    button.classList.toggle('liked-button');
                    console.log("Like toggled successfully");
                } else {
                    console.log("Button not found");
                }
            } else {
                console.log("Error toggling like");
            }
        })
        .catch(error => {
            console.error("Error:", error);
        });
}