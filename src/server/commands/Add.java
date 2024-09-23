package server.commands;

import common.model.SpaceMarine;
import common.requests.AddRequest;
import common.requests.RequestDTO;
import common.responses.EmptyResponse;
import common.responses.AbstractResponse;
import common.responses.RegardsResponse;
import server.interfaces.SimpleCommand;

import java.time.LocalDate;

public class Add extends AbstractCommand implements SimpleCommand {
    public Add(String consoleName) {
        super(consoleName, "(standart) Add a new marine to the Imperium Army", "New marine joined the Imperium Army ranks!");
    }

    @Override
    public AbstractResponse execute(RequestDTO requestDTO) {
        AddRequest request = (AddRequest) requestDTO.getRequest();
        if (request != null) {
            SpaceMarine spaceMarine = new SpaceMarine(
                    (long) (Math.random() * Long.MAX_VALUE),
                    request.getName(),
                    request.getCoordinates(),
                    LocalDate.now(),
                    request.getHealth(),
                    request.getHeartCount(),
                    request.getCategory(),
                    request.getWeapon(),
                    request.getChapter(),
                    request.getUser().getLogin()
            );
            collectionProcessor.addNewMarine(spaceMarine);
            return new RegardsResponse(getConsoleName(), regards);
        }
        return new EmptyResponse();
    }
}
