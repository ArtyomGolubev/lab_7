package server.db;

import common.exceptions.UserAlreadyExistsException;
import common.exceptions.NotAnOwnerException;
import common.exceptions.WrongPasswordException;
import common.model.*;
import common.network.AbstractUser;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * create table users (
 *     id serial primary key,
 *     login varchar(255) not null unique,
 *     password varchar(255) not null,
 *     salt varchar(255) not null
 * );
 *
 * CREATE TYPE astartes_category_enum AS ENUM ('ASSAULT', 'SUPPRESSOR', 'TERMINATOR');
 * CREATE TYPE weapon_enum AS ENUM ('HEAVY_BOLTGUN', 'FLAMER', 'GRENADE_LAUNCHER', 'MULTI_MELTA');
 *
 * CREATE TABLE marines (
 *     id SERIAL PRIMARY KEY,
 *     name VARCHAR(255) NOT NULL,
 *     coordinates_id INTEGER NOT NULL REFERENCES coordinates(id),
 *     creation_date DATE NOT NULL,
 *     health FLOAT NOT NULL,
 *     heart_count INTEGER NOT NULL,
 *     astartes_category astartes_category NOT NULL,
 *     weapon weapon NOT NULL,
 *     chapter_id INTEGER NOT NULL REFERENCES chapter(id),
 *     owner_login VARCHAR(255) NOT NULL REFERENCES users(login)
 * );
 *
 * create table coordinates (
 *     id serial primary key,
 *     x float not null,
 *     y float not null
 * );
 *
 * create table chapter (
 *     id serial primary key,
 *     chapter_name varchar(255) not null,
 *     position_id integer not null references positions(id)
 * );
 *
 * create table positions (
 *     id serial primary key,
 *     pos_x double precision not null,
 *     pos_y double precision not null,
 *     pos_z integer not null
 * );
 */

public class PostgresConnection extends DBConnection {
    private final PasswordProcessor passwordProcessor = new PasswordProcessor();
    protected PostgresConnection(String url, String login, String password) throws SQLException {
        super(url, login, password);
    }

    @Override
    public boolean authenticateUser(String login, String password) throws SQLException, WrongPasswordException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE login = ?");
        ps.setString(1, login);

        ResultSet resultSet = ps.executeQuery();

        if (resultSet.next()) {
            byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
            byte[] salt = resultSet.getBytes("salt");
            byte[] expectedPassword = resultSet.getBytes("password");

            if (this.passwordProcessor.passwordCheck(passwordBytes, salt, expectedPassword)) {
                return true;
            }
        }
        throw new WrongPasswordException("Wrong password");
    }

    @Override
    public boolean addUser(String login, String password) throws SQLException {
        if (findUser(login)) {
            throw new UserAlreadyExistsException("User with such login already exists");
        }

        byte[] salt = this.passwordProcessor.getSalt();
        byte[] passwordHash = this.passwordProcessor.hash(password.getBytes(StandardCharsets.UTF_8), salt);

        PreparedStatement ps = connection.prepareStatement("INSERT INTO users (login, password, salt) VALUES (?, ?, ?)");
        ps.setString(1, login);
        ps.setBytes(2, passwordHash);
        ps.setBytes(3, salt);

        return ps.execute();
    }

    private boolean findUser(String login) throws SQLException {
        PreparedStatement ps = this.connection.prepareStatement("SELECT 1 FROM users WHERE login = ?");
        ps.setString(1, login);
        ResultSet resultSet = ps.executeQuery();
        return resultSet.next();
    }

    @Override
    public int addMarine(String name, Coordinates coordinates, LocalDate date, float health, int heartCount, AstartesCategory category, Weapon weapon, Chapter chapter, String ownerLogin) throws SQLException {
        int coordinatesId = addCoordinates(coordinates);
        int chapterId = addChapter(chapter);
        PreparedStatement ps = this.connection.prepareStatement("INSERT INTO marines (" +
                "name, coordinates_id, creation_date, health, heart_count, astartes_category, weapon, chapter_id, owner_login)" +
                "VALUES (?, ?, ?, ?, ?, CAST(? AS astartes_category_enum), CAST(? AS weapon_enum), ?, ?) RETURNING id");

        ps.setString(1, name);
        ps.setInt(2, coordinatesId);
        ps.setDate(3, Date.valueOf(date));
        ps.setFloat(4, health);
        ps.setInt(5, heartCount);
        ps.setString(6, category.name());
        ps.setString(7, weapon.name());
        ps.setInt(8, chapterId);
        ps.setString(9, ownerLogin);

        int id = -1;
        ResultSet resultSet = ps.executeQuery();
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }
        return id;
    }

    @Override
    public int addPosition(Position position) throws SQLException {
        PreparedStatement ps = this.connection.prepareStatement("INSERT INTO positions (pos_x, pos_y, pos_z) VALUES (?, ?, ?) RETURNING id");
        ps.setDouble(1, position.getPos_x());
        ps.setDouble(2, position.getPos_y());
        ps.setLong(3, position.getPos_z());

        int id = -1;
        ResultSet resultSet = ps.executeQuery();
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }
        return id;
    }

    @Override
    public int addChapter(Chapter chapter) throws SQLException {
        int id = -1;
        int positionId;
        if ((positionId = addPosition(chapter.getChapterPosition())) > 0) {
            PreparedStatement ps = this.connection.prepareStatement(
                    "INSERT INTO chapter (chapter_name, position_id) VALUES (?, ?) RETURNING id");
            ps.setString(1, chapter.getChapterName());
            ps.setInt(2, positionId);

            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
        }
        return id;
    }

    @Override
    public int addCoordinates(Coordinates coordinates) throws SQLException {
        PreparedStatement ps = this.connection.prepareStatement("INSERT INTO coordinates (x, y) VALUES (?, ?) RETURNING id");
        ps.setFloat(1, coordinates.getX());
        ps.setFloat(2, coordinates.getY());
        int id = -1;
        ResultSet resultSet = ps.executeQuery();
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }
        return id;
    }

    private int checkIfExists(Coordinates coordinates) throws SQLException {
        PreparedStatement ps = this.connection.prepareStatement("SELECT id FROM coordinates " +
                "WHERE x = ? AND y = ?");
        ps.setFloat(1, coordinates.getX());
        ps.setFloat(2, coordinates.getY());

        ResultSet resultSet = ps.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("id");
        }
        return -1;
    }

    private int checkIfExists(Chapter chapter) throws SQLException {
        PreparedStatement ps = this.connection.prepareStatement("SELECT ch.id FROM chapter ch " +
                "JOIN positions p ON ch.position_id = p.id " +
                "WHERE p.pos_x = ? AND p.pos_y = ? AND p.pos_z = ? AND ch.chapter_name = ?");

        Position position = chapter.getChapterPosition();

        ps.setDouble(1, position.getPos_x());
        ps.setDouble(2, position.getPos_y());
        ps.setLong(3, position.getPos_z());
        ps.setString(4, chapter.getChapterName());

        ResultSet resultSet = ps.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("id");
        }
        return -1;
    }

    @Override
    public boolean updateMarine(long id, String name, Coordinates coordinates, float health, int heartCount, AstartesCategory category, Weapon weapon, Chapter chapter, String ownerLogin) throws SQLException, NotAnOwnerException {
        if (!marineOwnerCheck(ownerLogin, id)) {
            throw new NotAnOwnerException("You are not an owner of that marine");
        }
        PreparedStatement ps = this.connection.prepareStatement("UPDATE marines" +
                " SET (name, coordinates_id, health, heart_count, astartes_category, weapon, chapter_id) =" +
                " (?, ?, ?, ?, CAST(? AS astartes_category_enum), CAST(? AS weapon_enum), ?)" +
                "WHERE id = ?");

        int coordinatesId = checkIfExists(coordinates);
        if (coordinatesId < 0) {
            coordinatesId = addCoordinates(coordinates);
        }
        int addressId = checkIfExists(chapter);
        if (addressId < 0) {
            addressId = addChapter(chapter);
        }

        ps.setString(1, name);
        ps.setInt(2, coordinatesId);
        ps.setFloat(3, health);
        ps.setInt(4, heartCount);
        ps.setString(5, category.name());
        ps.setString(6, weapon.name());
        ps.setInt(7, addressId);
        ps.setLong(8, id);

        return ps.executeUpdate() > 0;
    }

    @Override
    public boolean removeById(long id, AbstractUser abstractUser) throws SQLException {
        String login = abstractUser.getLogin();
        if (marineOwnerCheck(login, id)) {
            PreparedStatement ps = this.connection.prepareStatement("DELETE FROM marines WHERE id = ?");
            ps.setLong(1, id);

            int count = ps.executeUpdate();
            return count == 1;
        }
        return false;
    }

    private boolean marineOwnerCheck(String login, long id) throws SQLException {
        PreparedStatement ps = this.connection.prepareStatement("SELECT owner_login FROM marines WHERE id = ?");
        ps.setLong(1, id);
        ResultSet resultSet = ps.executeQuery();

        if (resultSet.next()) {
            String realOwner = resultSet.getString("owner_login");
            return realOwner.equals(login);
        }
        return false;
    }

    @Override
    public ConcurrentLinkedDeque<SpaceMarine> getAllMarines() throws SQLException {
        ConcurrentLinkedDeque<SpaceMarine> result = new ConcurrentLinkedDeque<>();
        String statement = "select m.*, co.x, co.y, p.pos_x, p.pos_y, p.pos_z, ch.chapter_name FROM marines m " +
                "JOIN coordinates co ON m.coordinates_id = co.id " +
                "JOIN chapter ch ON m.chapter_id = ch.id " +
                "JOIN positions p ON ch.position_id = p.id";

        PreparedStatement ps = this.connection.prepareStatement(statement);
        ResultSet resultSet = ps.executeQuery();

        while (resultSet.next()) {
            SpaceMarine spaceMarine = setToMarines(resultSet);
            result.add(spaceMarine);
        }
        return result;
    }

    @Override
    public int clearCollectionForUser(AbstractUser abstractUser) throws SQLException {
        String login  = abstractUser.getLogin();
        int quantity = 0;
        PreparedStatement ps1 = this.connection.prepareStatement("SELECT COUNT(*) FROM marines WHERE owner_login = ?");
        PreparedStatement ps2 = this.connection.prepareStatement("DELETE FROM marines WHERE owner_login = ?");

        ps1.setString(1, login);
        ps2.setString(1, login);
        ResultSet resultSet = ps1.executeQuery();
        if (resultSet.next()) {
            quantity = resultSet.getInt("count");
        }

        if (ps2.execute()) {
            return quantity;
        }
        return 0;
    }

    private SpaceMarine setToMarines(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        Float x = resultSet.getFloat("x");
        float y = resultSet.getFloat("y");
        Coordinates coordinates = new Coordinates(x, y);
        LocalDate creationDate = resultSet.getDate("creation_date").toLocalDate();
        float health = resultSet.getFloat("health");
        int heartCount = resultSet.getInt("heart_count");
        AstartesCategory category = AstartesCategory.valueOf(resultSet.getString("astartes_category"));
        Weapon weapon = Weapon.valueOf(resultSet.getString("weapon"));
        String chapterName = resultSet.getString("chapter_name");
        double posX = resultSet.getDouble("pos_x");
        double posY = resultSet.getDouble("pos_y");
        long posZ = resultSet.getLong("pos_z");
        Position position = new Position(posX, posY, posZ);
        Chapter chapter = new Chapter(chapterName, position);
        String ownerLogin = resultSet.getString("owner_login");

        return new SpaceMarine(
                id,
                name,
                coordinates,
                creationDate,
                health,
                heartCount,
                category,
                weapon,
                chapter,
                ownerLogin);
    }

}
