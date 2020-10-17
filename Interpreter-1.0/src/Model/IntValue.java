package Model;

public class IntValue implements Value {
    private int value;

    public IntValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public Type getType() {
        return new IntType();
    }

    public String toString() {
        return String.valueOf(value);
    }
}