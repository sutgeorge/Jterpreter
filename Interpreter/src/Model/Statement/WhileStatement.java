package Model.Statement;

import Exceptions.GeneralException;
import Exceptions.LogicException;
import Model.ADT.DictionaryInterface;
import Model.ADT.HeapInterface;
import Model.ADT.StackInterface;
import Model.Expression.GeneralExpression;
import Model.ProgramState;
import Model.Type.BoolType;
import Model.Type.Type;
import Model.Value.BoolValue;
import Model.Value.Value;

import java.io.IOException;

public class WhileStatement implements StatementInterface {
    private GeneralExpression expression;
    private StatementInterface statement;

    public WhileStatement(GeneralExpression expression, StatementInterface statement) {
        this.expression = expression;
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws GeneralException, IOException {
        StackInterface stack = state.getExecutionStack();
        DictionaryInterface<String, Value> symbolTable = state.getSymbolTable();
        HeapInterface heap = state.getHeap();
        Value expressionValue = expression.evaluate(symbolTable, heap);

        if (!(expressionValue.getType() instanceof BoolType))
            throw new LogicException("The type of the evaluated expression's result should be BoolValue.");

        BoolValue expressionBoolValue = (BoolValue)expressionValue;
        if (expressionBoolValue.getValue()) {
            stack.push(this.clone());
            stack.push(statement);
            state.setExecutionStack(stack);
        }

        return null;
    }

    @Override
    public StatementInterface clone() {
        return new WhileStatement(expression, statement);
    }

    @Override
    public DictionaryInterface<String, Type> typecheck(DictionaryInterface<String, Type> typeEnvironment) throws GeneralException {
        Type expressionType = expression.typecheck(typeEnvironment);
        if (!expressionType.equals(new BoolType()))
            throw new GeneralException("The expression of the while statement should be a LogicExpression or a RelationalExpression");

        statement.typecheck(typeEnvironment.clone());
        return typeEnvironment;
    }

    public String toString() {
        return "while(" + expression.toString() + ")" + statement.toString();
    }
}
