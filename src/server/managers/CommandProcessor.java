package server.managers;

import server.commands.AbstractCommand;

import server.interfaces.FileHandler;


import java.util.HashMap;


public class CommandProcessor {
    private final HashMap<String, AbstractCommand> commands = new HashMap<>();
    private CollectionProcessor collectionProcessor;
    private final FileHandler fileHandler;

    public CommandProcessor(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    public void setCollectionProcessor(CollectionProcessor collectionProcessor) {
        this.collectionProcessor = collectionProcessor;
        for (String key : commands.keySet()) {
            commands.get(key).setCollectionManager(collectionProcessor);
            commands.get(key).setFileManager(fileHandler);
        }
    }

    public void addCommands(AbstractCommand... commands) {
        for (AbstractCommand command : commands) {
            this.commands.put(command.getConsoleName(), command);
            command.setCollectionManager(collectionProcessor);
            command.setFileManager(fileHandler);
            command.setCommandManager(this);
        }
    }

    public HashMap<String, AbstractCommand> getCommands() {
        return commands;
    }
}
