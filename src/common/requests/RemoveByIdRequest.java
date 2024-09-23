package common.requests;

import common.exceptions.WrongParameterException;
import Client.Handlers.Checker;

public class RemoveByIdRequest extends AbstractRequest implements RequestWithParameters{
    private long id;
    public RemoveByIdRequest(String commandName) {
        super(commandName);
    }

    @Override
    public void setParameters(String... parameters) throws WrongParameterException {
        if (!parameters[0].isEmpty()) {
            if (Checker.isCorrectNumber(parameters[0], Long.class)) {
                this.id = Long.parseLong(parameters[0]);
            } else {
                throw new WrongParameterException("Wrong number entered");
            }
        } else {
            throw new WrongParameterException("Parameter is empty");
        }

    }

    public long getId() {
        return id;
    }
}
