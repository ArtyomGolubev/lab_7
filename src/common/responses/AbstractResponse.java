package common.responses;

import java.io.Serializable;

public abstract class AbstractResponse implements Serializable {
    protected String commandName;
    protected String message;

    public AbstractResponse(String commandName, String message) {
        this.commandName = commandName;
        this.message = message;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
