package home.poc.mina.calculator;

import java.util.List;


/**
 * Simple dummy calculator, able to perform basic arithmetic operations
 */
public class DummyCalculator {

    public int calculate(Operation operation, List<Integer> args) {
        validateInputArguments(args);
        int result = args.remove(0);
        switch(operation) {
            case PLUS:
                for (Integer arg : args) {
                    result += arg;
                }
                return result;
            case MINUS:
                for (Integer arg : args) {
                    result -= arg;
                }
                return result;
            case MULTIPLY:
                for (Integer arg : args) {
                    result *= arg;
                }
                return result;
            case DIVIDE:
                for (Integer arg : args) {
                    result /= arg;
                }
                return result;
            default:
                throw new IllegalArgumentException("Unrecognized operator");
        }
    }

    private void validateInputArguments(List<Integer> args) {
        boolean containsNegative = args.stream().filter(
                arg -> arg < 0
        ).findAny().isPresent();
        if (containsNegative) {
            throw new IllegalArgumentException("Arguments should be only positive numbers");
        }
    }

}