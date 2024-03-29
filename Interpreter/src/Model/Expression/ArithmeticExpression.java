package Model.Expression;

import Exceptions.ArithmeticException;
import Exceptions.GeneralException;
import Exceptions.TypeException;
import Model.ADT.DictionaryInterface;
import Model.ADT.HeapInterface;
import Model.Type.IntType;
import Model.Type.Type;
import Model.Value.IntValue;
import Model.Value.Value;

public class ArithmeticExpression extends GeneralExpression {
    GeneralExpression first_expression, second_expression;
    int operation;
    String operator;

    public ArithmeticExpression(String operator, GeneralExpression first_expression, GeneralExpression second_expression) {
        switch (operator) {
            case "+":
                this.operation = 1;
                this.operator = "+";
                break;
            case "-":
                this.operation = 2;
                this.operator = "-";
                break;
            case "*":
                this.operation = 3;
                this.operator = "*";
                break;
            case "/":
                this.operation = 4;
                this.operator = "/";
                break;
        }

        this.first_expression = first_expression;
        this.second_expression = second_expression;
    }

    @Override
    public Value evaluate(DictionaryInterface<String, Value> table, HeapInterface heap) throws GeneralException {
        Value first_value, second_value;
        first_value = first_expression.evaluate(table, heap);

        if (first_value.getType().equals(new IntType())) {
            second_value = second_expression.evaluate(table, heap);

            if (second_value.getType().equals(new IntType())) {
                IntValue first_integer = (IntValue)first_value;
                IntValue second_integer = (IntValue)second_value;

                int n1, n2;
                n1 = first_integer.getValue();
                n2 = second_integer.getValue();
                if (operation == 1)
                    return new IntValue(n1+n2);
                if (operation == 2)
                    return new IntValue(n1-n2);
                if (operation == 3)
                    return new IntValue(n1*n2);
                if (operation == 4) {
                    if (n2 == 0) {
                        throw new ArithmeticException("Division by zero error.");
                    } else {
                        return new IntValue(n1/n2);
                    }
                }
            } else {
                throw new ArithmeticException("The second operand is not an integer.");
            }
        } else {
            throw new ArithmeticException("The first operand is not an integer.");
        }

        return null;
    }

    public Type typecheck(DictionaryInterface<String, Type> typeEnvironment) throws TypeException {
        Type first_type, second_type;
        first_type = first_expression.typecheck(typeEnvironment);
        second_type = second_expression.typecheck(typeEnvironment);

        if (!first_type.equals(new IntType()))
            throw new TypeException("The first operand is not an integer.");

        if (!second_type.equals(new IntType()))
            throw new TypeException("The second operand is not an integer.");

        return new IntType();
    }

    public String toString() {
        return first_expression.toString() + operator + second_expression.toString();
    }
}
