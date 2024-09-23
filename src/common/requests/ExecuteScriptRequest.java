package common.requests;

import Client.Handlers.Checker;

public class ExecuteScriptRequest extends AbstractRequest implements RequestWithParameters{
    String filename;
    public ExecuteScriptRequest(String commandName) {
        super(commandName);
    }

    @Override
    public void setParameters(String... parameters) {
        if (Checker.ValidNameCheck(parameters[0])) {
            this.filename = parameters[0];
        }
    }

    public String getFilename() {
        return filename;
    }
}
