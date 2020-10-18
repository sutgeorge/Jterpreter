package Controller;
import Exceptions.GeneralException;
import Model.ProgramState;
import Model.StackInterface;
import Model.StatementInterface;
import Repository.RepositoryInterface;

public class Controller {
    private RepositoryInterface repository;

    public Controller(RepositoryInterface repository) {
        this.repository = repository;
    }

    public ProgramState oneStepExecution(ProgramState state) throws GeneralException {
        StackInterface<StatementInterface> stack = state.getExecutionStack();

        if (stack.isEmpty())
            throw new GeneralException("Program state stack is empty.");

        StatementInterface currentStatement = stack.pop();
        return currentStatement.execute(state);
    }

    public void completeExecution() throws GeneralException {
        ProgramState state = repository.getCurrentProgramState();
        StackInterface<StatementInterface> stack = state.getExecutionStack();

        while (!stack.isEmpty()) {
            this.oneStepExecution(state);
            stack = state.getExecutionStack();

            System.out.println(state.toString());
        }
    }
}