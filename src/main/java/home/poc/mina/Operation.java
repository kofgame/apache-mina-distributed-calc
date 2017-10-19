package home.poc.mina;


public enum Operation {

    PLUS("PLUS"),
    MINUS("MINUS"),
    MULTIPLY("MULTIPLY"),
    DIVIDE("DIVIDE"),

    CLOSE("CLOSE");

    Operation(String name) {
        this.name = name;
    }

    public static Operation fromValue(String value) {
        return Operation.valueOf(value);
    }

    String name;
}