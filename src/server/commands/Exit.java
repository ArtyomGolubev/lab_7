package server.commands;

import server.interfaces.SimpleCommand;

public class Exit extends AbstractCommand implements SimpleCommand {
    public Exit(String consoleName) {
        super(consoleName, "(standart) Close the Imperium Army management program", "Program closed");
    }

    @Override
    public void execute() {
        System.exit(0);
    }
}
