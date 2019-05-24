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

      Connection connection = DbManage.enableConnection();
      //DbManage.getUsers(connection.createStatement());
      //DbManage.addUser(connection, "skrillex", "password179");
      //DbManage.deleteUser(connection, "test_login_15");
       //DbManage.updateUserInfo(connection, "skrillex", "masha", "yandex");
      //DbManage.addUserInfo(connection, "skrillex", "igor", "@mail.ru");
      //DbManage.updateUserInfo(connection, "login", "dexter", "ya@sobaka.ru");
      //DbManage.findByLogin(connection, "skrillex");
        DbManage.autorization(connection, "skrillex", "password179");
      //DbManage.getUsers(connection.createStatement());
      connection.close();
  }

}
