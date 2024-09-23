package common.requests;

import Client.Handlers.Checker;
import common.exceptions.BlankRequestException;
import common.exceptions.WrongParameterException;

public class RemoveAnyByHeartCountRequest extends AbstractRequest implements RequestWithParameters{
    private int heartCount;
    public RemoveAnyByHeartCountRequest(String commandName) {
        super(commandName);
    }

    @Override
    public void setParameters(String... parameters) throws WrongParameterException {
        int result = -1;
        String parameter = parameters[0];
        try {
            if (Checker.NullCheck(parameter) || Checker.EmptyArrayCheck(parameter.split(" "))) {
                throw new BlankRequestException("Blank string entered");
            }
            if (parameter.contains(" ")) {
                String[] splitted = parameter.split(" ");
                if (Checker.isCorrectNumber(splitted[0], Integer.class)) {
                    result = Integer.parseInt(splitted[0]);
                }
            } else if (Checker.isCorrectNumber(parameter, Integer.class)) {
                result = Integer.parseInt(parameter);
            } else {
                throw new WrongParameterException("Wrong number entered");
            }
            if (result > 0) {
                this.heartCount = result;
            } else {
                throw new WrongParameterException("Field can't be less than 0");
            }
        } catch (WrongParameterException | BlankRequestException e) {
            throw new WrongParameterException("Wrong parameter entered");
        }
    }

    public int getHeartCount() {
        return heartCount;
    }
}
