package server.commands;


import common.model.SpaceMarine;
import common.requests.AddRequest;
import common.requests.RequestDTO;
import common.responses.AbstractResponse;
import common.responses.RegardsResponse;
import server.interfaces.SimpleCommand;

import java.time.LocalDate;

public class RemoveGreater extends AbstractCommand implements SimpleCommand {
    public RemoveGreater(String consoleName) {
        super(consoleName, "(standart) Remove marines greater than given", "Marines removed!");
    }

    @Override
    public AbstractResponse execute(RequestDTO requestDTO) {
        AddRequest request = (AddRequest) requestDTO.getRequest();
        SpaceMarine comparator = new SpaceMarine(
                (long) (Math.random() * Long.MAX_VALUE),
                request.getName(),
                request.getCoordinates(),
                LocalDate.now(),
                request.getHealth(),
                request.getHeartCount(),
                request.getCategory(),
                request.getWeapon(),
                request.getChapter()
        );
        collectionProcessor.getCollection().removeIf(marine -> comparator.compareTo(marine) < 0);
        return new RegardsResponse(getConsoleName(), regards);
    }
}
