package Model;

import Exceptions.GeneralException;

public class PrintStatement implements StatementInterface {
    GeneralExpression expression;

    public String toString() {
        return "print(" + expression.toString() + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) throws GeneralException {
        return state;
    }
}
