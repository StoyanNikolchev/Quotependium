function fetchRandomQuote() {
    fetch('/api/quotes/random')
        .then(response => response.json())
        .then(randomQuote => {
            const quoteContentElement = document.getElementById('quote-content');
            const authorsElement = document.getElementById('authors');
            const bookTitleElement = document.getElementById('book-title');

            quoteContentElement.textContent = `"${randomQuote.text}"`;
            authorsElement.textContent = `Authors: ${randomQuote.authors.join(', ')}`;
            bookTitleElement.textContent = `Book: ${randomQuote.bookTitle}`;
        })
        .catch(error => console.error('Error fetching random quote:', error));
}

fetchRandomQuote();