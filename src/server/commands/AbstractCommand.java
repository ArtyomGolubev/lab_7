package server.commands;


import common.model.SpaceMarine;
import common.requests.RequestDTO;
import common.responses.EmptyResponse;
import common.responses.AbstractResponse;
import server.interfaces.FileHandler;
import server.managers.CollectionProcessor;
import server.managers.CommandProcessor;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;

public abstract class AbstractCommand {
    private final String consoleName;
    private final String consoleDescription;
    protected String regards;
    protected CollectionProcessor collectionProcessor;
    protected FileHandler fileHandler;
    protected CommandProcessor commandProcessor;

    public AbstractCommand(String consoleName, String consoleDescription) {
        this.consoleName = consoleName;
        this.consoleDescription = consoleDescription;
    }

    public AbstractCommand(String consoleName, String consoleDescription, String regards) {
        this.consoleName = consoleName;
        this.consoleDescription = consoleDescription;
        this.regards = regards;
    }

    public void setCollectionManager(CollectionProcessor collectionProcessor) {
        this.collectionProcessor = collectionProcessor;
    }

    public void setCommandManager(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }

    public void setFileManager(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    public String getConsoleName() {
        return consoleName;
    }

    public String getConsoleDescription() {
        return consoleDescription;
    }

    public void execute() {

    }

    public AbstractResponse execute(RequestDTO requestDTO) throws IOException {
        return new EmptyResponse();
    }

    protected LinkedList<SpaceMarine> convertToLinkedList(ConcurrentLinkedDeque deque) {
        synchronized (deque) {
            return new LinkedList<>(deque);
        }
    }

}
