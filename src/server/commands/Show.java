package server.commands;

import common.requests.RequestDTO;
import common.requests.ShowRequest;
import common.responses.EmptyResponse;
import common.responses.ErrorResponse;
import common.responses.AbstractResponse;
import common.responses.ShowResponse;
import server.interfaces.SimpleCommand;
import common.model.SpaceMarine;

import java.util.LinkedList;


public class Show extends AbstractCommand implements SimpleCommand {

    public Show(String consoleName) {
        super(consoleName, "(int amount) Shows the Imperium Army list", "Imperium Army list shown!");
    }

    @Override
    public AbstractResponse execute(RequestDTO requestDTO) {
        ShowRequest request = (ShowRequest) requestDTO.getRequest();
        if (request != null) {
            if (request.getQuantity() < 0) {
                ShowResponse response = new ShowResponse(getConsoleName(), regards);
                response.setOrganizations(convertToLinkedList(collectionProcessor.getCollection()));
                return response;
            } else {
                LinkedList<SpaceMarine> organizationsToSend = new LinkedList<>();
                if (request.getQuantity() <= collectionProcessor.getCollection().size()) {
                    for (int i = 0; i < request.getQuantity(); i++) {
                        organizationsToSend.add(collectionProcessor.getMarineAtIndex(i));
                    }
                    ShowResponse response = new ShowResponse(getConsoleName(), regards);
                    response.setOrganizations(organizationsToSend);
                    return response;
                } else {
                    return new ErrorResponse("The army is big and fearsome.. But not like that.");
                }
            }
        } else {
            return new EmptyResponse();
        }
    }

}
