package server.commands;

import common.requests.RequestDTO;
import common.responses.InfoResponse;
import common.responses.AbstractResponse;
import server.interfaces.SimpleCommand;

public class Info extends AbstractCommand implements SimpleCommand {
    public Info(String consoleName) {
        super(consoleName, "(standart) Display information about the Imperium Army", "Command executed!");
    }

    @Override
    public AbstractResponse execute(RequestDTO requestDTO) {
        return new InfoResponse(getConsoleName(), collectionProcessor.getData());
    }
}
