<p align="center">
<img src="src/main/resources/static/images/logo.png" alt="Logo" width="200" >
</p>

# Quotependium

Quotependium is a web application designed for managing and sharing quotes from books. This application allows users to contribute and discover memorable quotes, providing a community-driven platform for literary enthusiasts.

## Features

- **User Contributions**: Add and manage quotes and books.
- **User Profile Management**: Edit usernames and profile pictures.
- **Quote Management**: Save favourite quotes to account.
- **Random Quote Generator**: Display random quotes on the front page.
- **Quote of the Day**: Scheduled daily quote display.
- **Admin Panel**: Manage user roles and permissions.
- **Security**: User authentication and authorization using Spring Security.

## Technologies Used

- **Backend**: Spring Boot, Hibernate, Fetch API
- **Frontend**: HTML, CSS, JavaScript, Thymeleaf, Bootstrap
- **Database**: MySQL
- **Security**: Spring Security

## API Endpoints

### Get Quote by ID
- **Endpoint**: `/api/quotes/{id}`
- **Method**: GET
- **Description**: Retrieve a specific quote by its ID.

### Get Random Quote
- **Endpoint**: `/api/quotes/random`
- **Method**: GET
- **Description**: Retrieve a random quote.

## Screenshots
### Home Page
![Home Page](screenshots/home-page.png)

### Registration
Registration and login pages have field validations
![Registration](screenshots/register-page.png)

### User Profile
![User Profile](screenshots/user-profile-page.png)

### Admin Panel
![Admin Panel](screenshots/admin-panel-page.png)

### Submit ISBN
Accepts 13-digit ISBNs. They can be entered either as clean numbers, with spaces, with dashes, or with "ISBN" at the start. The backend converts them to clean numbers.
![Submit ISBN](screenshots/submit-isbn-page.png)

### Submit New Book
When the book with the given ISBN is not in the database, Quotependium prompts the user to enter information about it before submitting their quote.
![Submit New Book](screenshots/submit-book.png)

### Browse Quotes, Books, and Authors
![Browse](screenshots/browse-page.png)

### Dynamic Layout for Mobile
![Dynamic Layout](screenshots/dynamic-layout-for-mobile.png)

## License
This project is licensed under the MIT License.
