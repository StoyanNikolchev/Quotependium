function toggleLike(quoteId) {
    fetch('/like-quote/' + quoteId, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content')
        }
    })
        .then(response => {
            if (response.ok) {
                console.log("Like toggled successfully");
            } else {
                console.log("Error toggling like");
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}