package ru.levelup.jdbc;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class DbManage {

    public static Connection enableConnection() throws SQLException {
        // URL: jdbc:<vendor_name>://<host>:<port>/<db_name>[?options]
        Connection connection =  DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/coto-chat",
                "postgres",
                "root"
        );
        return connection;
    }

    //Получение полного списка пользователей
    public static void getUsers(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery("select *from users left join users_info on id = user_id");
        while (resultSet.next()) {
           long id = resultSet.getLong("id");
           String login = resultSet.getString("login");
           String password = resultSet.getString("password");
           String name = resultSet.getString("name");
           String email = resultSet.getString("email");
           String phone = resultSet.getString("phone");
           System.out.println(id + "\t\t\t" + login + "\t\t\t" + password + "\t\t\t" + name + "\t\t\t" + email + "\t\t\t" + phone);
        }
    }

    //Получение ID по логину из другой таблицы
    public static long getUserId(Statement statement, String login_goal) throws SQLException {
        ResultSet resultSet = statement.executeQuery("select *from users left join users_info on id = user_id");
        long id = 0;
        while (resultSet.next()) {
            id = resultSet.getLong("id");
            String login = resultSet.getString("login");
            String password = resultSet.getString("password");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            String phone = resultSet.getString("phone");
            if (login.equals(login_goal)) {
                break;
            }
        }
        return id;
    }

    //Добавление пользователя
    public static boolean addUser(Connection connection, String login, String password) {
        try {
        PreparedStatement preparedStatement = connection.prepareStatement("insert into users (login, password) values (?,?)");
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, password);
        int count = 0;
            count = preparedStatement.executeUpdate();
            System.out.println("Number of added users: " + count);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //Удаление пользователя
    public static boolean deleteUser(Connection connection, String login) {
        try {
        PreparedStatement preparedStatement = connection.prepareStatement("delete from users where login = ?");
        preparedStatement.setString(1, login);
        int count = 0;
            count = preparedStatement.executeUpdate();
            System.out.println("Number of deleted users: " + count);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //Обновление информации о пользователе
    public static boolean updateUserInfo(Connection connection, String login, String name, String email) {
        try {
        PreparedStatement preparedStatement = connection.prepareStatement("update users_info set name = ?, email = ? where user_id in " +
                "(select id from users where login = ?)");
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, email);
        preparedStatement.setString(3, login);
            int count = preparedStatement.executeUpdate();
            System.out.println("Количество измененных строк: " + count);
            if (count == 0) {
                addUserInfo(connection, login, name, email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
               return false;
        }
        return true;
    }

    //Добавление информации о пользователе
    public static boolean addUserInfo(Connection connection, String login, String name, String email) {
        try {
          long id = getUserId(connection.createStatement(),login);
        PreparedStatement preparedStatement = connection.prepareStatement("insert into users_info (user_id, name, last_login, email, phone) values " +
                "(?, ?, '2019-05-24', ?, '1488')");
        preparedStatement.setLong(1, id);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, email);
            int count = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //Поиск по логину информации о пользователе
    public static void findByLogin (Connection connection, String login_goal) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("select *from users left join users_info on id = user_id where login = ?");
        preparedStatement.setString(1, login_goal);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
            long id = resultSet.getLong("id");
            String login = resultSet.getString("login");
            String password = resultSet.getString("password");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            String phone = resultSet.getString("phone");
            System.out.println(id + "\t\t\t" + login + "\t\t\t" + password + "\t\t\t" + name + "\t\t\t" + email + "\t\t\t" + phone);
    }

    //Авторизация по паролю и логину
    public static void autorization( Connection connection, String login_goal, String password_goal) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement("select password from users where login = ?");
        preparedStatement.setString(1, login_goal);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        if (resultSet.getString("password").equals(password_goal)) {

            preparedStatement = connection.prepareStatement("select *from users left join users_info on id = user_id where login = ? and password = ?");
            preparedStatement.setString(1, login_goal);
            preparedStatement.setString(2, password_goal);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            long id = resultSet.getLong("id");
            String login = resultSet.getString("login");
            String password = resultSet.getString("password");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            String phone = resultSet.getString("phone");
            System.out.println(id + "\t\t\t" + login + "\t\t\t" + password + "\t\t\t" + name + "\t\t\t" + email + "\t\t\t" + phone);
        } else {
            System.out.println("Имя пользователя или пароль неверны");
        }
    }


}
