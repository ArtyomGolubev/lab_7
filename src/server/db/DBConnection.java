package server.db;

import common.exceptions.NotAnOwnerException;
import common.exceptions.WrongPasswordException;
import common.model.*;
import common.network.AbstractUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.concurrent.ConcurrentLinkedDeque;

public abstract class DBConnection {
    protected Connection connection;

    public DBConnection(String url, String login, String password) throws SQLException {
        this.connection = DriverManager.getConnection(url, login, password);
    }

    public abstract boolean authenticateUser(String login, String password) throws SQLException, WrongPasswordException;
    public abstract boolean addUser(String login, String password) throws SQLException;
    public abstract int addMarine(String name,
                                  Coordinates coordinates,
                                  LocalDate date,
                                  float health,
                                  int heartCount,
                                  AstartesCategory type,
                                  Weapon weapon,
                                  Chapter chapter,
                                  String ownerLogin) throws SQLException;
    public abstract int addPosition(Position position) throws SQLException;
    public abstract int addChapter(Chapter chapter) throws SQLException;
    public abstract int addCoordinates(Coordinates coordinates) throws SQLException;

    public abstract boolean updateMarine(long id,
                                         String name,
                                         Coordinates coordinates,
                                         float health,
                                         int heartCount,
                                         AstartesCategory type,
                                         Weapon weapon,
                                         Chapter chapter,
                                         String ownerLogin) throws SQLException, NotAnOwnerException;

    public abstract boolean removeById(long id, AbstractUser abstractUser) throws SQLException;
    public abstract ConcurrentLinkedDeque<SpaceMarine> getAllMarines() throws SQLException;
    public abstract int clearCollectionForUser(AbstractUser abstractUser) throws SQLException;
}
