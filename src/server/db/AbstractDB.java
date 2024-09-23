package server.db;

import java.sql.SQLException;

public abstract class AbstractDB {
    public abstract DBConnection createConnection() throws SQLException;
}
