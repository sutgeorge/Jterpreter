package Model;

import Model.ADT.*;
import Model.Statement.StatementInterface;
import Model.Value.Value;

import java.io.BufferedReader;

public class ProgramState {
    private StackInterface<StatementInterface> executionStack;
    private DictionaryInterface<String, Value> symbolTable;
    private DictionaryInterface<String, BufferedReader> fileTable;
    private ListInterface<Value> out;
    private HeapInterface heap;
    private StatementInterface originalProgram;

    public ProgramState(StackInterface<StatementInterface> executionStack, DictionaryInterface<String, Value> symbolTable,
                        DictionaryInterface<String, BufferedReader> fileTable, ListInterface<Value> out,
                        StatementInterface originalProgram) {
        this.executionStack = executionStack;
        this.symbolTable = symbolTable;
        this.fileTable = fileTable;
        this.out = out;
        this.originalProgram = (StatementInterface) originalProgram.clone();
        this.heap = new Heap();
        executionStack.push(originalProgram);
    }

    public static ProgramState createNewProgramState(StatementInterface statement) {
        StackInterface<StatementInterface> executionStack = new TheStack<StatementInterface>();
        DictionaryInterface<String, Value> symbolTable = new TheDictionary<String, Value>();
        DictionaryInterface<String, BufferedReader> fileTable = new TheDictionary<String, BufferedReader>();
        ListInterface<Value> output = new TheList<Value>();

        return new ProgramState(executionStack, symbolTable, fileTable, output, statement);
    }

    public StackInterface<StatementInterface> getExecutionStack() {
        return executionStack;
    }

    public DictionaryInterface<String, Value> getSymbolTable() {
        return symbolTable;
    }

    public DictionaryInterface<String, BufferedReader> getFileTable() {
        return fileTable;
    }

    public ListInterface<Value> getOutput() {
        return out;
    }

    public void setExecutionStack(StackInterface<StatementInterface> executionStack) {
        this.executionStack = executionStack;
    }

    public void setSymbolTable(DictionaryInterface<String, Value> symbolTable) {
        this.symbolTable = symbolTable;
    }

    public void setFileTable(DictionaryInterface<String, BufferedReader> fileTable) {
        this.fileTable = fileTable;
    }

    public void setOutput(ListInterface<Value> out) {
        this.out = out;
    }

    public String toString() {
        String text = "----------------------------------------------------------------------\n";
        String executionStackString = executionStack.toString();
        String symbolTableString = symbolTable.toString();
        String outString = out.toString();
        String fileTableString = fileTable.toString();

        text += "Execution stack: \n" + executionStackString + "\nSymbol table:\n" + symbolTableString + "\nOutput:\n" + outString + "\nFile table:\n" + fileTableString + "\n\n";
        return text;
    }
}