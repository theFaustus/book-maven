package com.tekwill.java.fundamentals.book.controller;


import com.tekwill.java.fundamentals.book.domain.Book;
import com.tekwill.java.fundamentals.book.service.BookService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    public void renderViewBookByISBNPage(String isbn) {
        Book bookByIsbn = bookService.getBookByIsbn(isbn);
        System.out.println("<h1>Book by ISBN page</h1><p>" + bookByIsbn + "</p>");
        System.out.println("<nav>tekwill</nav>");
    }

    public void renderViewAllBooks() {
        System.out.println("<h1>All books</h1><p>");
        bookService.getAllBooks().forEach(b -> System.out.println(b));
        System.out.println("</p>");
        System.out.println("<nav>tekwill</nav>");
    }

    public void renderMessageAfterBookNameUpdated(String newBookName, Long bookId) {
        System.out.println("<h1>All books</h1><p>");
        System.out.println("<div class='alert'> Successfully updated " + bookService.updateBookNameByBookId(newBookName,
                                                                                                            bookId) + " rows </div>");
        System.out.println("</p>");
        System.out.println("<nav>tekwill</nav>");
    }

    public void renderMessageAfterDeletedBook(Long bookId) {
        System.out.println("<h1>All books</h1><p>");
        System.out.println(
                "<div class='alert'> Successfully deleted " + bookService.deleteBook(bookId) + " rows </div>");
        System.out.println("</p>");
        System.out.println("<nav>tekwill</nav>");
    }

    public void renderMessageAfterSavedBook(Book book) {
        System.out.println("<h1>All books</h1><p>");
        System.out.println("<div class='alert'> Successfully saved " + bookService.saveBook(book) + " rows </div>");
        System.out.println("</p>");
        System.out.println("<nav>tekwill</nav>");
    }
}
