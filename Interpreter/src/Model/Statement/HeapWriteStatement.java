package Model.Statement;

import Exceptions.DictionaryException;
import Exceptions.GeneralException;
import Exceptions.HeapException;
import Exceptions.UndefinedSymbolException;
import Model.ADT.DictionaryInterface;
import Model.ADT.Heap;
import Model.ADT.HeapInterface;
import Model.Expression.GeneralExpression;
import Model.Expression.VariableExpression;
import Model.ProgramState;
import Model.Type.RefType;
import Model.Type.Type;
import Model.Value.RefValue;
import Model.Value.Value;
import java.io.IOException;

public class HeapWriteStatement implements StatementInterface {
    private String variableName;
    private GeneralExpression expression;

    public HeapWriteStatement(String variableName, GeneralExpression expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws GeneralException, IOException {
        DictionaryInterface<String, Value> symbolTable = state.getSymbolTable();
        Heap heap = (Heap)state.getHeap();

        try {
            Value variable = symbolTable.lookup(variableName);
            if (!(variable.getType() instanceof RefType))
                throw new HeapException("The type of the variable given as an argument to the HeapWrite statement is not RefType.");

            RefValue refValue = (RefValue)variable;
            Type locationType = refValue.getLocationType();
            if (!heap.isDefined(refValue.getAddress()))
                throw new HeapException("The specified address cannot be found on the heap.");

            Value expressionValue = expression.evaluate(symbolTable, heap);
            if (!(expressionValue.getType().equals(locationType)))
                throw new HeapException("The type of the expression\'s result does not match the type of the referred value.");

            heap.put(refValue.getAddress(), expressionValue);
        } catch (DictionaryException e) {
            throw new UndefinedSymbolException("The variable given as an argument to the HeapWrite statement hasn't been declared.");
        }

        return null;
    }

    @Override
    public StatementInterface clone() {
        return new HeapWriteStatement(variableName, expression);
    }

    @Override
    public DictionaryInterface<String, Type> typecheck(DictionaryInterface<String, Type> typeEnvironment) throws GeneralException {
        Type variableType = typeEnvironment.lookup(variableName);
        Type expressionType = expression.typecheck(typeEnvironment);

        if (!variableType.equals(new RefType(expressionType)))
            throw new HeapException("The expression type should match the reference type.");

        return typeEnvironment;
    }

    public String toString() {
        return "wH(" + variableName + "," + expression.toString() + ")";
    }
}
