package common.network;

import java.io.Serializable;

public abstract class AbstractUser implements Serializable {
    protected String login;
    protected String password;

    public abstract void setLogin(String login);
    public abstract String getLogin();
    public abstract void setPassword(String password);
    public abstract String getPassword();
}
