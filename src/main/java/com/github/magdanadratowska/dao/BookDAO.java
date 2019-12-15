package com.github.magdanadratowska.dao;

import com.github.magdanadratowska.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDAO {

    private String jdbcURL = "jdbc:mysql://localhost:3306/m1448_proj_read?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private String jdbcUsername = "test";
    private String jdbcpassword = "test";

    private Logger logger = LoggerFactory.getLogger(BookDAO.class);

    private static final String SELECT_ALL_BOOKS = "select * from book;";
    private static final String SELECT_BOOK_BY_ID = "select * from book where id=?;";
    private static final String UPDATE_BOOK = "update book set title = ?, author_name = ?, author_surname = ? where id = ?";
    private static final String ADD_BOOK = "insert into book (title, author_name, author_surname) values (?, ?, ?)";

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcpassword);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void updateBook(long bookId, String title, String author_name, String author_surname) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BOOK);) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, author_name);
            preparedStatement.setString(3, author_surname);
            preparedStatement.setLong(4, bookId);
            preparedStatement.executeUpdate();
            logger.info("update book - idBook {} title {} author_name {} author_surname {}", bookId, title, author_name, author_surname);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBook(Book book){
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_BOOK);) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthorName());
            preparedStatement.setString(3, book.getAuthorSurname());
            preparedStatement.executeUpdate();
            logger.debug("add book - title {} author_name {} author_surname {}",
                    book.getTitle(), book.getAuthorName(), book.getAuthorSurname());
        } catch (SQLException e) {
            logger.warn("add book error - SQL Exception", e);
        }
    }

    @Deprecated
    public List<Book> readBooksList() {
        List<Book> books = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BOOKS);) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String authorName = resultSet.getString("author_name");
                String authorSurname = resultSet.getString("author_surname");
                books.add(new Book(id, title, authorName, authorSurname));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public Optional<Book> findBookById(long id) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK_BY_ID);) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            String title = resultSet.getString("title");
            String authorName = resultSet.getString("author_name");
            String authorSurname = resultSet.getString("author_surname");
            return Optional.of(new Book(id, title, authorName, authorSurname));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


}

