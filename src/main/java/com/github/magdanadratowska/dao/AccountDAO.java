package com.github.magdanadratowska.dao;

import com.github.magdanadratowska.model.Book;
import com.github.magdanadratowska.model.User;
import com.github.magdanadratowska.model.UserBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    private String jdbcURL = "jdbc:mysql://localhost:3306/m1448_proj_read?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private String jdbcUsername = "test";
    private String jdbcpassword = "test";

    private Logger logger = LoggerFactory.getLogger(AccountDAO.class);

    private static final String SELECT_LAST_BOOKS = "select * from (select * from user_book, book where user_book.id_book = book.id) T where id_user = ? order by addition_date desc limit 1";
    private static final String ADD_BOOK_TO_USER_LIST = "insert into user_book (id_user, id_book, addition_date) values (?, ?, ?)";
    private static final String REMOVE_BOOK_FROM_USER_LIST = "delete from user_book where id_user=? and id_book=?";
    private static final String SELECT_USERS_LIST = "select * from (select * from user_book, book where (user_book.id_book = book.id AND is_active=true)) T where id_user =?";
    private static final String SELECT_ALL_BOOKS_LIST_FOR_CURRENT_USER_WITH_DELETED_BOOKS = "select * from book B left join (select id as id2, is_active from (select * from book, user_book where (user_book.id_book = book.id)) T where id_user =?) U on (U.id2 = B.id) limit ?, ?";
    private static final String COUNT_SELECT_ALL_BOOKS = "select count(*) as count from book";
    private static final String SELECT_ALL_BOOKS_LIST_FOR_CURRENT_USER_WITH_QUERY = "select * from book B left join (select id as id2, is_active from (select * from book, user_book where (user_book.id_book = book.id)) T where id_user =?) U on (U.id2 = B.id) where lower(title) like lower(?) limit ?, ?;";
    private static final String COUNT_SELECT_ALL_BOOKS_LIST_FOR_CURRENT_USER_WITH_QUERY = "select count(*) as count from book B left join (select id as id2, is_active from (select * from book, user_book where (user_book.id_book = book.id)) T where id_user =?) U on (U.id2 = B.id) where lower(title) like lower(?);";
    private static final String SELECT_BOOK_DETAIL_FOR_CURRENT_USER = "select * from (select * from book where book.id =?) T left join (select * from user_book where id_user =?) L on (T.id = L.id_book);";
    private static final String DELETE_BOOK_FROM_USER_LIST = "update user_book set is_active = false WHERE (id_user=? AND id_book=?)";
    private static final String RESTORE_BOOK_TO_USER_LIST = "update user_book set is_active = true WHERE (id_user=? AND id_book=?)";
    private static final String UPDATE_BOOK_RATE = "update user_book set rate = ? where (id_user=? and id_book=?)";
    private static final String UPDATE_BOOK_REVIEW = "update user_book set review = ? where (id_user=? and id_book=?)";
    private static final String COUNT_READ_USERS_BOOK = "select count(*) from user_book where id_user=?;";
    private static final String DELETE_USER_RECORDS = "delete from user_book where id_user = ?;";

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

    public List<UserBook> getUsersBookList(long userId) {
        List<UserBook> usersBookList = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USERS_LIST);) {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int rate = resultSet.getInt("rate");
                long id = resultSet.getLong("id");
                String title = resultSet.getString("title");
                String authorName = resultSet.getString("author_name");
                String authorSurname = resultSet.getString("author_surname");
                Book book = new Book(id, title, authorName, authorSurname);
                String addition_date = resultSet.getString("addition_date");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime additionDateTime = LocalDateTime.parse(addition_date, formatter);
                LocalDateTime additionDate = LocalDateTime.from(additionDateTime);
                usersBookList.add(new UserBook(book, additionDate, rate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usersBookList;
    }

    public void addBookToUserList(long idBook, long idUser) throws SQLException {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(ADD_BOOK_TO_USER_LIST, Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, idUser);
                statement.setLong(2, idBook);
                statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                statement.executeUpdate();
                logger.info("add book to user list - userId {} idBook {} timeStamp {}", idUser, idBook, Timestamp.valueOf(LocalDateTime.now()));
            }
        }
    }

    @Deprecated
    public void removeBookFromUserList(long idBook, long idUser) throws SQLException {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(REMOVE_BOOK_FROM_USER_LIST, Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, idUser);
                statement.setLong(2, idBook);
                statement.executeUpdate();
                logger.info("remove book from user list - userId {} idBook {}", idUser, idBook);
            }
        }
    }

    public List<UserBook> getAllBookListForCurrentUserWithDeletedBooks(User user, long limitFrom, long numberOfRows) {
        List<UserBook> usersBookList = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BOOKS_LIST_FOR_CURRENT_USER_WITH_DELETED_BOOKS);) {
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setLong(2, limitFrom);
            preparedStatement.setLong(3, numberOfRows);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String title = resultSet.getString("title");
                String authorName = resultSet.getString("author_name");
                String authorSurname = resultSet.getString("author_surname");
                Book book = new Book(id, title, authorName, authorSurname);
                boolean isActive = resultSet.getBoolean("is_active");
                boolean isOwned = resultSet.getBoolean("id2");
                usersBookList.add(new UserBook(book, isActive, isOwned));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usersBookList;
    }

    public List<UserBook> getAllBookListForCurrentUserWithQuery(User user, String query, long limitFrom, long numberOfRows) {
        List<UserBook> usersBookList = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BOOKS_LIST_FOR_CURRENT_USER_WITH_QUERY);) {
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setString(2, "%" + query + "%");
            preparedStatement.setLong(3, limitFrom);
            preparedStatement.setLong(4, numberOfRows);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String title = resultSet.getString("title");
                String authorName = resultSet.getString("author_name");
                String authorSurname = resultSet.getString("author_surname");
                Book book = new Book(id, title, authorName, authorSurname);
                boolean isActive = resultSet.getBoolean("is_active");
                boolean isOwned = resultSet.getBoolean("id2");
                usersBookList.add(new UserBook(book, isActive, isOwned));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usersBookList;
    }

    public Long countAllBookListForCurrentUserWithQuery(User user, String query) {
        long result = 0;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(COUNT_SELECT_ALL_BOOKS_LIST_FOR_CURRENT_USER_WITH_QUERY);) {
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setString(2, "%" + query + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result = resultSet.getLong("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Long countAllBook(){
        long result = 0;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(COUNT_SELECT_ALL_BOOKS);) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result = resultSet.getLong("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public UserBook getBookDetailForCurrentUser(long bookId, User user) {
        UserBook userBook = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK_DETAIL_FOR_CURRENT_USER);) {
            preparedStatement.setLong(1, bookId);
            preparedStatement.setLong(2, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String authorName = resultSet.getString("author_name");
                String authorSurname = resultSet.getString("author_surname");
                Book book = new Book(bookId, title, authorName, authorSurname);
                int rate = resultSet.getInt("rate");
                boolean isActive = resultSet.getBoolean("is_active");
                String review = resultSet.getString("review");
                userBook = new UserBook(book, LocalDateTime.now(), rate, isActive, review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userBook;
    }

    public Book getLastReadBook(long userId) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        Book book = new Book();
        try {
            preparedStatement = connection.prepareStatement(SELECT_LAST_BOOKS);
            {
                preparedStatement.setLong(1, userId);

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("author_name"));
                    book.setAuthorName(resultSet.getString("author_name"));
                    book.setAuthorSurname(resultSet.getString("author_surname"));
                    book.setTitle(resultSet.getString("title"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    public long sumOfReadUserBooks(long userId) throws SQLException {
        long sumOfReadBooks = 0;
        try (Connection connection = getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(COUNT_READ_USERS_BOOK, Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, userId);
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                sumOfReadBooks = resultSet.getLong("count(*)");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return sumOfReadBooks;

    }

    public void deleteBookFromUserList(User user, Long bookId) {
        changeFlagIsActive(user, bookId, DELETE_BOOK_FROM_USER_LIST);
        logger.info("delete book from user list - userId {} idBook {}", user.getId(), bookId);
    }

    public void restoreBookToUserList(User user, Long bookId) {
        changeFlagIsActive(user, bookId, RESTORE_BOOK_TO_USER_LIST);
        logger.info("restore book from user list - userId {} idBook {}", user.getId(), bookId);
    }

    private void changeFlagIsActive(User user, Long bookId, String sql) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setLong(2, bookId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBookRate(int rate, long userId, long bookId) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BOOK_RATE);) {
            preparedStatement.setInt(1, rate);
            preparedStatement.setLong(2, userId);
            preparedStatement.setLong(3, bookId);
            preparedStatement.executeUpdate();
            logger.info("update rate for book on user list- userId {} idBook {} rate {}", userId, bookId, rate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBookReview(String review, long userId, long bookId) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BOOK_REVIEW);) {
            preparedStatement.setString(1, review);
            preparedStatement.setLong(2, userId);
            preparedStatement.setLong(3, bookId);
            preparedStatement.executeUpdate();
            logger.info("update review for book on user list- userId {} idBook {} review {}", userId, bookId, review);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUserbooksOfUser(Long userId) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_RECORDS)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
