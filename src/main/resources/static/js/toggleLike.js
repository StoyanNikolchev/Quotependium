function toggleLike(button, quoteId, likeCounterElement) {
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
                    updateLikeCount(button, likeCounterElement);
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

function updateLikeCount(button, likeCounterElement) {
    const likeText = likeCounterElement.textContent;
    let likeCount = parseInt(likeText.split(': ')[1]);

    const isLiked = button.classList.contains('liked-button');

    if (isLiked) {
        likeCount++;
    } else {
        likeCount--;
    }

    likeCounterElement.textContent = `Liked by: ${likeCount}`;
}