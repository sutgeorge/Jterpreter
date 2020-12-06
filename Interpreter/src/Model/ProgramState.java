package Model;

import Exceptions.GeneralException;
import Exceptions.StackException;
import Model.ADT.*;
import Model.Statement.ForkStatement;
import Model.Statement.StatementInterface;
import Model.Type.Type;
import Model.Value.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class ProgramState {
    private StackInterface<StatementInterface> executionStack;
    private DictionaryInterface<String, Value> symbolTable;
    private DictionaryInterface<String, BufferedReader> fileTable;
    private ListInterface<Value> out;
    private HeapInterface heap;
    private StatementInterface originalProgram;
    private static Object lockForIDs = new Object();
    private static int numberOfIDs = 0;
    private int id = 0;

    public ProgramState(StackInterface<StatementInterface> executionStack, DictionaryInterface<String, Value> symbolTable,
                        DictionaryInterface<String, BufferedReader> fileTable, HeapInterface heap, ListInterface<Value> out,
                        StatementInterface originalProgram) {
        this.executionStack = executionStack;
        this.symbolTable = symbolTable;
        this.fileTable = fileTable;
        this.out = out;
        this.originalProgram = originalProgram;
        this.heap = heap;

        DictionaryInterface<String, Type> typeEnvironment = new TheDictionary<>();
        this.originalProgram.typecheck(typeEnvironment);

        synchronized (lockForIDs) {
            numberOfIDs++;
            this.id = numberOfIDs;
        }

        executionStack.push(originalProgram);
    }

    public static ProgramState createNewProgramState(StatementInterface statement) {
        StackInterface<StatementInterface> executionStack = new TheStack<StatementInterface>();
        DictionaryInterface<String, Value> symbolTable = new TheDictionary<String, Value>();
        DictionaryInterface<String, BufferedReader> fileTable = new TheDictionary<String, BufferedReader>();
        ListInterface<Value> output = new TheList<Value>();
        HeapInterface heap = new Heap();

        return new ProgramState(executionStack, symbolTable, fileTable, heap, output, statement);
    }

    public boolean isNotCompleted() {
        return !executionStack.isEmpty();
    }

    public ProgramState oneStepExecution() throws GeneralException, IOException {
        if (executionStack.isEmpty()) {
            throw new StackException("Can't execute instruction: Program state stack is empty.");
        }

        StatementInterface currentStatement = executionStack.pop();
        return currentStatement.execute(this);
    }

    public int getId() {
        return id;
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

    public HeapInterface getHeap() {
        return heap;
    }

    public ListInterface<Value> getOutput() {
        return out;
    }

    public StatementInterface getOriginalProgram() {
        return originalProgram;
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

    public void setHeap(HeapInterface heap) {
        this.heap = heap;
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
        String heapString = heap.toString();

        text += "Execution stack: \n" + executionStackString + "\nSymbol table:\n" + symbolTableString + "\nHeap:\n" + heapString + "\nOutput:\n" + outString + "\nFile table:\n" + fileTableString + "\n\n";
        return text;
    }
}
