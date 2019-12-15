package com.github.magdanadratowska.dao;

import com.github.magdanadratowska.model.User;
import com.github.magdanadratowska.model.UserType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {

    private String jdbcURL = "jdbc:mysql://localhost:3306/m1448_proj_read?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private String jdbcUsername = "test";
    private String jdbcpassword = "test";
    private static final String SELECT_USER_BY_ID = "select * from user where id=?;";
    private static final String DELETE_USER_BY_ID = "delete from user where id=?;";
    private static final String SELECT_ALL_USERS = "select * from user;";
    private static final String COUNT_ALL_USERS = "select count(*) as count from user;";
    private static final String COUNT_ALL_USERS_WITH_QUERY = "select count(*) as count from user where  lower (username) like  lower (?) or lower (email) like lower (?);";
    private static final String SELECT_ALL_USERS_WITH_PAGES = "select * from user limit ?, ?;";
    private static final String SELECT_QUERY_USERS_WITH_PAGES = "select * from user where  lower (username) like  lower (?) or lower (email) like lower (?) limit ?, ?;";
    private static final String SELECT_USER_BY_EMAIL = "select * from user where email=?;";
    private static final String ADD_USER = "INSERT INTO user (username, email, password, register_date, user_type) VALUES (?, ?, ?, ?, ?);";
    private static final String UPDATE_USER = "update user set username=?, email=?, user_type=?, password=? where id=?;";

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

    public  Long getNummberOfUsers() {
        Long numOfUsers = 0L;
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(COUNT_ALL_USERS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                numOfUsers = resultSet.getLong("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  numOfUsers;
    }


    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                users.add(getUserFromDB(resultSet));
            }
        }
        return users;
    }

    public List<User> getUsersWithPages(Long limitFrom, Long numOfRows) throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS_WITH_PAGES)) {
            preparedStatement.setLong(1, limitFrom);
            preparedStatement.setLong(2, numOfRows);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                users.add(getUserFromDB(resultSet));
            }
        }
        return users;
    }

    public Optional<User> getUserById(Long i) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID)) {
            preparedStatement.setLong(1, i);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return Optional.ofNullable(getUserFromDB(resultSet));
        }
    }

    private User getUserFromDB(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setUsername(resultSet.getString("username"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setRegisterDate(resultSet.getTimestamp("register_date").toLocalDateTime());
        user.setUserType(UserType.valueOf(resultSet.getString("user_type")));

        return user;
    }

    public boolean addUser(User user) throws SQLException {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(ADD_USER, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getEmail());
                statement.setString(3, user.getPassword());
                statement.setTimestamp(4, Timestamp.valueOf(user.getRegisterDate()));
                statement.setString(5, user.getUserType().name());
                statement.executeUpdate();
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    Long generatedKey = resultSet.getLong(1);
                    System.out.println("Utworzono rekord o numerze " + generatedKey);
                    return true;
                }
            }
            return false;
        }
    }

    public Optional<User> getUserByEmail(String email) throws SQLException {

        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_EMAIL);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return Optional.ofNullable(getUserFromDB(resultSet));
            }
        }
        return Optional.empty();
    }

    public void deleteUserById(Long i) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_BY_ID)) {
            preparedStatement.setLong(1, i);
            int update = preparedStatement.executeUpdate();
        }
    }

    public void setAdminToUserById(Long userId) throws SQLException {
        Optional<User> optionalUser = getUserById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUserType(UserType.ADMIN);
            updateUser(user);
        }
    }

    public void setUserToUserById(Long userId) throws SQLException {
        Optional<User> optionalUser = getUserById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUserType(UserType.USER);
            updateUser(user);
        }
    }

    public void updateUser(User user) throws SQLException {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_USER)) {
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getEmail());
                statement.setString(3, user.getUserType().name());
                statement.setString(4, user.getPassword());
                statement.setLong(5, user.getId());
                statement.executeUpdate();
            }
        }
    }

    public long countAllUsersWithQuery(String query) {
        long count = 0;
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(COUNT_ALL_USERS_WITH_QUERY)) {
                preparedStatement.setString(1, "%"+query+"%");
                preparedStatement.setString(2, "%"+query+"%");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    count = resultSet.getLong("count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public List<User> getAllUsersWithQuery(String query, long limitFrom, int numOfRows)  {
        List<User> users = new ArrayList<>();
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY_USERS_WITH_PAGES)) {
            preparedStatement.setString(1, "%"+query+"%");
            preparedStatement.setString(2, "%"+query+"%");
            preparedStatement.setLong(3, limitFrom);
            preparedStatement.setLong(4, numOfRows);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                users.add(getUserFromDB(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;

    }
}
