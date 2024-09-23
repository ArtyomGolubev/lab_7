package common.requests;

import common.network.AbstractUser;

import java.io.Serializable;

public abstract class AbstractRequest implements Serializable {
    protected String commandName;
    protected String message = null;
    protected AbstractUser user;

    public AbstractRequest(String commandName) {
        this.commandName = commandName;
    }

    public void setMessage(String message) {
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
        return "Request{" +
                "commandName='" + commandName + '\'' +
                ", message='" + message + '\'' +
                '}';
    }


    public AbstractUser getUser() {
        return user;
    }

    public void setUser(AbstractUser user) {
        this.user = user;
    }
}
