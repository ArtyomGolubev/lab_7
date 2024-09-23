package server.commands;

import common.network.GuestUser;
import common.network.AbstractUser;
import common.requests.ClearRequest;
import common.requests.RequestDTO;
import common.responses.ErrorResponse;
import common.responses.AbstractResponse;
import common.responses.RegardsResponse;
import server.interfaces.SimpleCommand;

import java.sql.SQLException;

public class Clear extends AbstractCommand implements SimpleCommand {
    public Clear(String consoleName) {
        super(consoleName, "(standart) clear the Imperium Army", "Army cleared! Good luck defending against the xenos");
    }

    @Override
    public AbstractResponse execute(RequestDTO requestDTO) {
        ClearRequest request = (ClearRequest) requestDTO.getRequest();
        AbstractUser abstractUser = request.getUser();
        if (abstractUser instanceof GuestUser) {
            return new ErrorResponse("Since you're a guest, you can't delete marines.");
        }
        try {
            collectionProcessor.clearCollection(abstractUser);
            return new RegardsResponse(getConsoleName(), regards);
        } catch (SQLException e) {
            return new ErrorResponse("Marines can't be deleted.");
        }

    }
}
