package net.leevanec.finalstuffv2;

import java.util.Stack;

public class ExpressionEvaluator {

    public static double evaluateExpression(String expression) throws IllegalArgumentException {
        // Validate expression
        if (expression == null || expression.isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be null or empty");
        }

        Stack<Double> operands = new Stack<>();
        Stack<Character> operators = new Stack<>();
        boolean negativeNumber = false;

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            if (ch == ' ') {
                continue; // Ignore white spaces
            } else if (Character.isDigit(ch) || (ch == '-' && i == 0)) {
                // Extract the operand
                StringBuilder operandStr = new StringBuilder();
                if (ch == '-') {
                    negativeNumber = true;
                    continue; // Skip adding '-' to operandStr
                }
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    operandStr.append(expression.charAt(i++));
                }
                i--; // Move back one step to correctly evaluate the operand
                double operand = Double.parseDouble(operandStr.toString());
                if (negativeNumber) {
                    operand *= -1; // Negate the operand if it was a negative number
                    negativeNumber = false; // Reset flag
                }
                operands.push(operand);
            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^' || ch == '√') {
                while (!operators.empty() && hasPrecedence(ch, operators.peek())) {
                    applyOperation(operands, operators.pop());
                }
                operators.push(ch);
            } else {
                throw new IllegalArgumentException("Invalid character in expression: " + ch);
            }
        }

        while (!operators.empty()) {
            applyOperation(operands, operators.pop());
        }

        return operands.pop();
    }

    private static void applyOperation(Stack<Double> operands, char operator) {
        if (operator == '√') {
            double operand = operands.pop();
            if (operand < 0) {
                throw new ArithmeticException("Square root of a negative number is undefined");
            }
            operands.push(Math.sqrt(operand));
        } else {
            double operand2 = operands.pop();
            double operand1 = operands.pop();
            switch (operator) {
                case '+':
                    operands.push(operand1 + operand2);
                    break;
                case '-':
                    operands.push(operand1 - operand2);
                    break;
                case '*':
                    operands.push(operand1 * operand2);
                    break;
                case '/':
                    if (operand2 == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    operands.push(operand1 / operand2);
                    break;
                case '^':
                    operands.push(Math.pow(operand1, operand2));
                    break;
            }
        }
    }

    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return false;
        }
        if (op1 == '^' && (op2 == '*' || op2 == '/' || op2 == '+' || op2 == '-')) {
            return false;
        }
        return true;
    }
}