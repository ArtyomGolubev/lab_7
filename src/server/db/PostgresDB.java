package server.db;

import java.sql.SQLException;

public class PostgresDB extends AbstractDB {
    private final String url;
    private final String login;
    private final String password;

    public PostgresDB(String url, String login, String password) {
        this.url = url;
        this.login = login;
        this.password = password;
    }

    @Override
    public DBConnection createConnection() throws SQLException {
        return new PostgresConnection(url, login, password);
    }
}
