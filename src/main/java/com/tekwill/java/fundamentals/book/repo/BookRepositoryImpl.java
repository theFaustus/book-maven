package com.tekwill.java.fundamentals.book.repo;


import com.tekwill.java.fundamentals.book.domain.Book;
import com.tekwill.java.fundamentals.book.domain.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookRepositoryImpl implements BookRepository {
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/book-db?user=postgres&password=123456";
    private static final Logger logger = LoggerFactory.getLogger(BookRepositoryImpl.class);

    @Override
    public Book findBook(String isbn) {
//        Book foundBook = null;
//        for (Book b : books) {
//            if (b.getIsbn().equals(isbn))
//                foundBook = b;
//        }
//        if (foundBook == null) {
////            throw new BookNotFoundRuntimeException("Book with isbn=" + isbn + " not found!"); runtime exception
//            throw new BookNotFoundCheckedException("Book with isbn=" + isbn + " not found!");
//        }
//        return foundBook;

        Book book = null;
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement st1 = connection.prepareStatement("SELECT * FROM book WHERE isbn = ?");
             PreparedStatement st2 = connection.prepareStatement("SELECT * FROM page WHERE book_id = ?")) {

            st1.setString(1, isbn);

            try (ResultSet r = st1.executeQuery()) {
                while (r.next()) {
                    book = new Book(r.getLong("id"),
                                    r.getString("isbn"),
                                    r.getString("name"),
                                    r.getBoolean("is_rare"),
                                    r.getInt("number_of_pages"));
                }

                st2.setLong(1, book.getId());

                try (ResultSet r2 = st2.executeQuery()) {
                    while (r2.next()) {
                        Page page = new Page(r2.getLong("id"),
                                             r2.getString("content"),
                                             r2.getInt("page_number"));
                        book.addPage(page);
                    }
                }
            }

        } catch (SQLException e) {
            logger.error("Something bad happened during fetching a book with isbn = {}", isbn, e);
        }

        return book;
//        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/book-db", "postgres", "123456")){
//            System.out.println("Using user/password obtained " + connection.getClientInfo());
//        } catch (SQLException e){
//            e.printStackTrace();
//        }
//
//        Properties properties = new Properties();
//        properties.setProperty("user", "postgres");
//        properties.setProperty("password", "123456");
//
//        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/book-db", properties)){
//            System.out.println("Using props obtained " + connection.getClientInfo());
//        } catch (SQLException e){
//            e.printStackTrace();
//        }

    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement st1 = connection.prepareStatement("SELECT * FROM book");
             PreparedStatement st2 = connection.prepareStatement("SELECT * FROM page WHERE book_id = ?")) {

            try (ResultSet r = st1.executeQuery()) {
                while (r.next()) {
                    Book book = new Book(r.getLong("id"),
                                         r.getString("isbn"),
                                         r.getString("name"),
                                         r.getBoolean("is_rare"),
                                         r.getInt("number_of_pages"));
                    books.add(book);
                }
            }

            for (Book book : books) {
                st2.setLong(1, book.getId());
                try (ResultSet r2 = st2.executeQuery()) {
                    while (r2.next()) {
                        Page page = new Page(r2.getLong("id"),
                                             r2.getString("content"),
                                             r2.getInt("page_number"));
                        book.addPage(page);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    @Override
    public int updateBookNameByBookId(String newBookName, Long bookId) {
        int result = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement st = connection.prepareStatement("UPDATE book b SET name = ? WHERE b.id = ?")) {

            st.setString(1, newBookName);
            st.setLong(2, bookId);

            result = st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int deleteBook(Long bookId) {
        int result = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement st1 = connection.prepareStatement("DELETE FROM page p WHERE p.book_id = ?");
             PreparedStatement st2 = connection.prepareStatement("DELETE FROM book b WHERE b.id = ?")) {

            //delete the orphans first;
            st1.setLong(1, bookId);
            st1.executeUpdate();

            //delete parent
            st2.setLong(1, bookId);
            result = st2.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int saveBook(Book book) {
        int result = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement st1 = connection.prepareStatement(
                     "INSERT INTO book(isbn, name, is_rare, number_of_pages) VALUES (?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS);
             PreparedStatement st2 = connection.prepareStatement(
                     "INSERT INTO page(content, page_number, book_id) VALUES (?, ?, ?)")) {

//            final PreparedStatement st1 = connection.prepareStatement(
//                    "insert into book(id, isbn, name, is_rare, number_of_pages) values (?, ?, ?, ?, ?)");
//            st1.setLong(1, book.getId());
//            st1.setString(2, book.getIsbn());
//            st1.setString(3, book.getName());
//            st1.setBoolean(4, book.isRare());
//            st1.setInt(5, book.getNumberOfPages());
//            result = st1.executeUpdate();

            //auto id

            st1.setString(1, book.getIsbn());
            st1.setString(2, book.getName());
            st1.setBoolean(3, book.isRare());
            st1.setInt(4, book.getNumberOfPages());

            result = st1.executeUpdate();
            //retrieve auto generated id and set it on book
            try (ResultSet generatedKeys = st1.getGeneratedKeys()) {
                if (generatedKeys.next())
                    book.setId(generatedKeys.getLong(1));
            }

            for (Page page : book.getPages()) {
                st2.setString(1, page.getContent());
                st2.setInt(2, page.getPageNumber());
                st2.setLong(3, book.getId());
                st2.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


}
