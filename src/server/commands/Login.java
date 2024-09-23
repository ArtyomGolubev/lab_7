package server.commands;

import common.exceptions.WrongPasswordException;
import common.network.AbstractUser;
import common.requests.AuthenticationRequest;
import common.requests.RequestDTO;
import common.responses.AuthenticationResponse;
import common.responses.EmptyResponse;
import common.responses.ErrorResponse;
import common.responses.AbstractResponse;

import java.io.IOException;
import java.sql.SQLException;

public class Login extends AbstractCommand {
    public Login(String consoleName) {
        super(consoleName, "(standart) Authorizes the user.", "User authorized.");
    }

    @Override
    public AbstractResponse execute(RequestDTO requestDTO) throws IOException {
        AuthenticationRequest request = (AuthenticationRequest) requestDTO.getRequest();
        AbstractUser abstractUser = request.getUser();
        String login = abstractUser.getLogin();
        String password = abstractUser.getPassword();

        try {
            if (this.collectionProcessor.getConnection().authenticateUser(login, password)) {
                return new AuthenticationResponse(request.getCommandName(), regards, abstractUser, true);
            }
        } catch (SQLException e) {
            return new AuthenticationResponse(request.getCommandName(), "Exit denied", abstractUser, false);
        } catch (WrongPasswordException e) {
            return new ErrorResponse(e.toString());
        }
        return new EmptyResponse();
    }
}
