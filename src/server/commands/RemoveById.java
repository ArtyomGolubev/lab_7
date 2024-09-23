package server.commands;

import common.exceptions.NotFoundException;
import common.network.GuestUser;
import common.network.AbstractUser;
import common.requests.RemoveByIdRequest;
import common.requests.RequestDTO;
import common.responses.ErrorResponse;
import common.responses.AbstractResponse;
import common.responses.RegardsResponse;

import java.sql.SQLException;

public class RemoveById extends AbstractCommand {
    public RemoveById(String consoleName) {
        super(consoleName, "(long id) Remove marine from the Imperium Army by id", "Marine removed!");
    }

    @Override
    public AbstractResponse execute(RequestDTO requestDTO) {
        RemoveByIdRequest request = (RemoveByIdRequest) requestDTO.getRequest();
        AbstractUser abstractUser = request.getUser();
        if (abstractUser instanceof GuestUser) {
            return new ErrorResponse("You must be authorized to delete marines");
        }
        try {
            collectionProcessor.removeById(request.getId(), abstractUser);
            return new RegardsResponse(getConsoleName(), regards);
        } catch (NotFoundException e) {
            return new ErrorResponse(e.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
