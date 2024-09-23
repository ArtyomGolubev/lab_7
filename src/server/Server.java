package server;

import server.commands.*;
import server.db.AbstractDB;
import server.db.DBConnection;
import server.db.PostgresDB;
import server.interfaces.FileHandler;
import server.managers.*;
import server.network.TCPServer;

import java.io.IOException;
import java.sql.SQLException;

public class Server {
    public static void main(String[] args) throws IOException {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Exit signal received (Ctrl+D).");
            }
        });
        FileHandler csvHandler = new FileProcessor();
        CollectionProcessor collectionProcessor = new CollectionProcessor(csvHandler);
        CommandProcessor commandProcessor = new CommandProcessor(csvHandler);
        commandProcessor.setCollectionProcessor(collectionProcessor);
        commandProcessor.addCommands(
                new Add("add"),
                new Clear("clear"),
                new Show("show"),
                new Help("help"),
                new Exit("exit"),
                new RemoveById("remove_by_id"),
                new Update("update"),
                new Shuffle("shuffle"),
                new FilterContainsName("filter_contains_name"),
                new FilterLessThanHealth("filter_less_than_health"),
                new RemoveAnyByHeartCount("remove_any_by_heart_count"),
                new Info("info"),
                new RemoveGreater("remove_greater"),
                new RemoveLower("remove_lower"),
                new Login("login"),
                new Register("register"));
        RequestProcessor requestProcessor = new RequestProcessor(commandProcessor);
        TCPServer server = new TCPServer(requestProcessor, new Logger("logs.log"));
        AbstractDB db = new PostgresDB("jdbc:postgresql://localhost:5432/studs", "postgres", "12345");
        //AbstractDB db = new PostgresDB("jdbc:postgresql://pg:5432/studs", "s408454", "KjNxnfuHCrXlbafE");
        try {
            DBConnection connection = db.createConnection();
            collectionProcessor.setConnection(connection);
            collectionProcessor.loadCollectionFromDB();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        try {
            server.openConnection();
            server.run();
        } finally {
            server.close();
        }
    }
}
