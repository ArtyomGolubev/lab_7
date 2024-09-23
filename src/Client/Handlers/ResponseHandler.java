package Client.Handlers;


import common.responses.*;

public class ResponseHandler {
    private ConsoleHandler consoleHandler;

    public String handleResponse(AbstractResponse response) {
        if (response instanceof ErrorResponse) {
            return "ERROR: " + response;
        } else if (response instanceof EmptyResponse) {
            return null;
        } else if (response instanceof RegardsResponse) {
            return "SUCCESSFUL: " + response;
        } else if (response instanceof ExitResponse) {
            System.exit(0);
        } else if (response instanceof AuthenticationResponse) {
            if (((AuthenticationResponse) response).getResult()) {
                this.consoleHandler.setAbstractUser(((AuthenticationResponse) response).getUser());
                this.consoleHandler.setAuthorized(true);
                return "Authorization completed. Welcome home.";
            } else {
                this.consoleHandler.setAuthorized(false);
                return "Authorization failed. Get out of here, imposter.";
            }
        }
        return response.toString();
    }

    public void setConsoleHandler(ConsoleHandler consoleHandler) {
        this.consoleHandler = consoleHandler;
    }
}
