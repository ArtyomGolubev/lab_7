package Client;

import Client.Handlers.ConsoleHandler;
import Client.Handlers.RequestHandler;
import Client.Handlers.ResponseHandler;
import Client.Handlers.Sender;
import Client.network.TCPClient;
import common.exceptions.ConnectionFailedException;
import common.requests.*;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class Client {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 4728;

    public static void main(String[] args) throws IOException {

        RequestHandler requestManager = new RequestHandler(
                new ShowRequest("show"),
                new HelpRequest("help"),
                new InfoRequest("info"),
                new RemoveByIdRequest("remove_by_id"),
                new FilterLessThanHealthRequest("filter_less_than_health"),
                new ClearRequest("clear"),
                new ShuffleRequest("shuffle"),
                new AddRequest("add"),
                new UpdateRequest("update"),
                new AddRequest("remove_greater"),
                new AddRequest("remove_lower"),
                new RemoveAnyByHeartCountRequest("remove_by_heart_count"),
                new ExecuteScriptRequest("execute_script"),
                new ExitRequest("exit"),
                new FilterContainsNameRequest("filter_contains_name"),
                new AuthenticationRequest("login"),
                new AddUserRequest("register"));

        TCPClient tcpClient = new TCPClient(SERVER_ADDRESS, SERVER_PORT);
        Sender sender = new Sender(tcpClient);
        ResponseHandler responseHandler = new ResponseHandler();
        ConsoleHandler consoleHandler = new ConsoleHandler(requestManager, sender, responseHandler);
        try {
            tcpClient.run();
            consoleHandler.listen();
        } catch (ConnectionFailedException e) {
            consoleHandler.printError("Server offline");
        } catch (SocketTimeoutException e) {
            consoleHandler.printError(e.toString());
        }
    }
}
