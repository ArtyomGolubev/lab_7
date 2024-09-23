package server.commands;

import common.requests.FilterLessThanHealthRequest;
import common.requests.RequestDTO;
import common.responses.AbstractResponse;
import common.responses.ShowResponse;
import common.model.SpaceMarine;

import java.util.LinkedList;
import java.util.List;

public class FilterLessThanHealth extends AbstractCommand {
    public FilterLessThanHealth(String consoleName) {
        super(consoleName, "(float health) Display every marine with amount of HP less than given", "Command executed!");
    }

    @Override
    public AbstractResponse execute(RequestDTO requestDTO) {
        FilterLessThanHealthRequest request = (FilterLessThanHealthRequest) requestDTO.getRequest();
        LinkedList<SpaceMarine> elements = new LinkedList<>(List.of(collectionProcessor.getMarinesLessThanCertainHealth(request.getHealth())));
        ShowResponse response = new ShowResponse(getConsoleName(), regards);
        response.setOrganizations(elements);
        return response;
    }
}
