package server.commands;

import common.requests.RequestDTO;
import common.responses.HelpResponse;
import common.responses.AbstractResponse;
import server.interfaces.SimpleCommand;

import java.util.HashSet;

public class Help extends AbstractCommand implements SimpleCommand {
    public Help(String consoleName) {
        super(consoleName, "(standart) Display list of available commands", "Command executed!");
    }

    @Override
    public AbstractResponse execute(RequestDTO requestDTO) {
        int padding = 35;
        HashSet<AbstractCommand> commands = new HashSet<>(commandProcessor.getCommands().values());
        StringBuilder output = new StringBuilder("List of available commands:\n");
        for (AbstractCommand command : commands) {
            output.append(String.format("%-" + padding + "s | %s\n", command.getConsoleName(), command.getConsoleDescription()));
        }
        return new HelpResponse(this.getConsoleName(), output.toString());
    }
}