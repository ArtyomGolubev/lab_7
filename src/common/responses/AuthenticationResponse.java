package common.responses;

import common.network.AbstractUser;

public class AuthenticationResponse extends AbstractResponse {
    private AbstractUser user;
    private boolean result;
    public AuthenticationResponse(String commandName, String message, AbstractUser user, boolean result) {
        super(commandName, message);
        this.user = user;
        this.result = result;
    }

    public AbstractUser getUser() {
        return user;
    }

    public void setUser(AbstractUser abstractUser) {
        this.user = abstractUser;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
