package common.requests;

import Client.Handlers.Checker;
import common.exceptions.BlankRequestException;
import common.exceptions.WrongParameterException;

public class FilterLessThanHealthRequest extends AbstractRequest implements RequestWithParameters{
    private float health;
    public FilterLessThanHealthRequest(String commandName) {
        super(commandName);
    }

    public void setParameters(String... parameters) throws WrongParameterException {
        float result = -1.0f;
        String parameter = parameters[0];
        try {
            if (Checker.NullCheck(parameter) || Checker.EmptyArrayCheck(parameter.split(" "))) {
                throw new BlankRequestException("Blank string entered");
            }
            if (parameter.contains(" ")) {
                String[] splitted = parameter.split(" ");
                if (Checker.isCorrectNumber(splitted[0], Float.class)) {
                    result = Float.parseFloat(splitted[0]);
                }
            } else if (Checker.isCorrectNumber(parameter, Float.class)) {
                result = Float.parseFloat(parameter);
            } else {
                throw new WrongParameterException("Wrong number entered.");
            }
            if (result > 0) {
                this.health = result;
            } else {
                throw new WrongParameterException("Field can't be less than 0");
            }
        } catch (WrongParameterException | BlankRequestException e) {
            throw new WrongParameterException("Wrong parameter entered");
        }
    }

    public float getHealth() {
        return health;
    }
}
