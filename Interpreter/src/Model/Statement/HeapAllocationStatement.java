package Model.Statement;

import Exceptions.DictionaryException;
import Exceptions.GeneralException;
import Exceptions.HeapException;
import Model.ADT.DictionaryInterface;
import Model.ADT.Heap;
import Model.ADT.HeapInterface;
import Model.ADT.TheDictionary;
import Model.Expression.GeneralExpression;
import Model.Expression.VariableExpression;
import Model.ProgramState;
import Model.Type.RefType;
import Model.Type.Type;
import Model.Value.IntValue;
import Model.Value.RefValue;
import Model.Value.Value;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class HeapAllocationStatement implements StatementInterface {
    private String variableName;
    private GeneralExpression expression;

    public HeapAllocationStatement(String variableName, GeneralExpression expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws GeneralException, IOException {
        DictionaryInterface<String, Value> symbolTable = state.getSymbolTable();
        HeapInterface heap = state.getHeap();
        Value expressionValue = expression.evaluate(symbolTable, heap);
        Type expressionType = expressionValue.getType();

        if (!symbolTable.isDefined(variableName))
            throw new HeapException("Variable \"" + variableName + "\" not found in the symbol table.");

        RefValue value = (RefValue)symbolTable.lookup(variableName);
        boolean invalidAddress = false;
        if (heap.isDefined(value.getAddress())) {
            invalidAddress = true;
        }

        heap.put(expressionValue);
        RefValue refValue = new RefValue(heap.getCurrentMaximumKey(), expressionType);

        if (invalidAddress) {
            Map<Integer, Value> newMap = heap.getContent().entrySet().stream()
                    .map(x -> {
                        if (x.getValue().getType() instanceof RefType) {
                            RefValue refValueInner = (RefValue)x.getValue();
                            refValueInner.setAddress(heap.getCurrentMaximumKey());
                            x.setValue(refValueInner);
                        }
                        return x;})
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            heap.setContent(newMap);
        }

        symbolTable.put(variableName, refValue);
        state.setSymbolTable(symbolTable);

        return null;
    }

    @Override
    public StatementInterface clone() {
        return new HeapAllocationStatement(variableName, expression);
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
        return "new(" + variableName + "," + expression.toString() + ")";
    }
}
