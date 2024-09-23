package server.commands;

import common.exceptions.UserAlreadyExistsException;
import common.requests.AddUserRequest;
import common.requests.RequestDTO;
import common.responses.*;

import java.io.IOException;
import java.sql.SQLException;

public class Register extends AbstractCommand {
    public Register(String consoleName) {
        super(consoleName, "(standart) Registers a new user.", "New used added.");
    }

    @Override
    public AbstractResponse execute(RequestDTO requestDTO) throws IOException {
        AddUserRequest request = (AddUserRequest) requestDTO.getRequest();
        String login = request.getUser().getLogin();
        String password = request.getUser().getPassword();

        try {
            this.collectionProcessor.getConnection().addUser(login, password);
            return new RegardsResponse(getConsoleName(), regards);
        } catch (SQLException e) {
            System.out.println(e.toString());
        } catch (UserAlreadyExistsException e) {
            return new ErrorResponse(e.toString());
        }
        return new EmptyResponse();
    }
}
