package gameofmath;

import java.io.Serializable;
import java.util.Random;

/**
 * One arithmetic question. Problems are generated so every answer is a whole number:
 * subtraction never goes below zero and division always divides exactly by a non-zero divisor.
 * The problem (and its answer) is kept in the HTTP session, never sent to the browser.
 */
public final class Problem implements Serializable {

    private static final long serialVersionUID = 1L;
    // Unicode escapes keep the source file pure ASCII (Eclipse on Windows defaults to Cp1252).
    private static final char TIMES = '×';
    private static final char DIVIDE = '÷';

    private final int left;
    private final int right;
    private final char operator;
    private final int answer;

    private Problem(int left, char operator, int right, int answer) {
        this.left = left;
        this.operator = operator;
        this.right = right;
        this.answer = answer;
    }

    public static Problem random(Random random) {
        switch (random.nextInt(4)) {
            case 0: {
                int a = random.nextInt(100);
                int b = random.nextInt(100);
                return new Problem(a, '+', b, a + b);
            }
            case 1: {
                int a = random.nextInt(100);
                int b = random.nextInt(100);
                // Larger number first so the answer is never negative.
                return new Problem(Math.max(a, b), '-', Math.min(a, b), Math.abs(a - b));
            }
            case 2: {
                // Times tables keep multiplication doable in your head.
                int a = random.nextInt(13);
                int b = random.nextInt(13);
                return new Problem(a, TIMES, b, a * b);
            }
            default: {
                // Build division from a multiplication: divisor 1..12, quotient 0..12.
                int divisor = 1 + random.nextInt(12);
                int quotient = random.nextInt(13);
                return new Problem(divisor * quotient, DIVIDE, divisor, quotient);
            }
        }
    }

    public boolean isCorrect(int candidate) {
        return candidate == answer;
    }

    public int getAnswer() {
        return answer;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public char getOperator() {
        return operator;
    }

    /** Human-readable form such as "36 &#247; 4". */
    public String getText() {
        return left + " " + operator + " " + right;
    }
}
