package ru.levelup.jdbc;

import java.sql.*;

public class JdbcExample {

  static {
      try {
          Class.forName("org.postgresql.Driver");
      } catch (ClassNotFoundException e) {
          throw new RuntimeException(e);
      }
  }

  public static void main (String args[]) throws SQLException {

      // URL: jdbc:<vendor_name>://<host>:<port>/<db_name>[?options]
     Connection connection =  DriverManager.getConnection(
              "jdbc:postgresql://localhost:5432/coto-chat",
              "postgres",
              "root"
      );

      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("select *from users");

      long maxId = 0;
      long minId = 0;

      while(resultSet.next()) {
          long id = resultSet.getLong("id");
          String login = resultSet.getString("login");
          String password = resultSet.getString("password");
          System.out.println(id + " " + login + " " + password);

          maxId = Math.max(id, maxId);
          minId = Math.min(id, minId);
      }

      PreparedStatement preparedStatement = connection.prepareStatement("insert into users (login, password) values (?,?)");

      preparedStatement.setString(1, "test_login_" + maxId);
      preparedStatement.setString(2, "test_password");

      int count = preparedStatement.executeUpdate();
      System.out.println("Affected rows: " + count);

      PreparedStatement deleteStatement = connection.prepareStatement("delete from users where id = ?");
      deleteStatement.setLong(1, maxId);
      int deleted = deleteStatement.executeUpdate();
      System.out.println("Deleted rows: " + deleted);

  }

}
