package server.commands;

import common.model.SpaceMarine;
import common.requests.RemoveAnyByHeartCountRequest;
import common.requests.RequestDTO;
import common.responses.AbstractResponse;
import common.responses.RegardsResponse;

public class RemoveAnyByHeartCount extends AbstractCommand {
    public RemoveAnyByHeartCount(String consoleName) {
        super(consoleName, "(int heartCount) Remove marine with a given heart count from the Imperium Army", "Marine removed!");
    }

    @Override
    public AbstractResponse execute(RequestDTO requestDTO) {
        RemoveAnyByHeartCountRequest request = (RemoveAnyByHeartCountRequest) requestDTO.getRequest();
        SpaceMarine element = collectionProcessor.getMarinesByHeartCount(request.getHeartCount())[0];
        collectionProcessor.getCollection().remove(element);
        return new RegardsResponse(getConsoleName(), regards);
    }
}
