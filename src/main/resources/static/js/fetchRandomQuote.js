function fetchRandomQuote() {
    fetch('/api/quotes/random')
        .then(response => response.json())
        .then(randomQuote => {
            const quoteContentElement = document.getElementById('quote-content');
            const authorsElement = document.getElementById('authors');
            const bookTitleElement = document.getElementById('book-title');
            const likeButton = document.getElementById('generator-like-button');
            const likeCounter = document.getElementById('generator-like-counter');

            quoteContentElement.textContent = `"${randomQuote.text}"`;
            authorsElement.textContent = `Authors: ${randomQuote.authors.join(', ')}`;
            bookTitleElement.textContent = `${randomQuote.bookTitle}`;

            likeButton.setAttribute('data-quote-id', randomQuote.id);
            likeButton.setAttribute('onclick', `toggleLike(this, ${randomQuote.id}, document.getElementById('generator-like-counter'))`);

            if (randomQuote.likedByUserIds.includes(currentUserId)) {
                likeButton.textContent = 'Liked';
                likeButton.classList.add('liked-button');
                likeButton.classList.remove('like-button');
            } else {
                likeButton.textContent = 'Like';
                likeButton.classList.add('like-button');
                likeButton.classList.remove('liked-button');
            }

            likeCounter.textContent = `Liked by: ${randomQuote.likedByUserIds.length}`;
        })
        .catch(error => console.error('Error fetching random quote:', error));
}

fetchRandomQuote();