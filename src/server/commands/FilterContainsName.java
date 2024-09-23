package server.commands;

import common.model.SpaceMarine;
import common.requests.FilterContainsNameRequest;
import common.requests.RequestDTO;
import common.responses.AbstractResponse;
import common.responses.ShowResponse;

import java.io.IOException;
import java.util.LinkedList;

public class FilterContainsName extends AbstractCommand {
    public FilterContainsName(String consoleName) {
        super(consoleName, "(String name) Display marines whose 'name' field contains a given substring", "Command executed!");
    }

    @Override
    public AbstractResponse execute(RequestDTO requestDTO) throws IOException {
        FilterContainsNameRequest request = (FilterContainsNameRequest) requestDTO.getRequest();
        LinkedList<SpaceMarine> elements = collectionProcessor.getMarinesByName(request.getSubstring());
        ShowResponse response = new ShowResponse(getConsoleName(), "");
        response.setOrganizations(elements);
        return response;
    }
}
