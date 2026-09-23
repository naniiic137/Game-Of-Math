package gameofmath;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Per-player game state, stored in the HTTP session. Holds the current problem (so the
 * correct answer never leaves the server), the score counters and a one-time feedback message.
 */
public final class GameState implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String SESSION_KEY = "game";
    private static final int MAX_ANSWER_LENGTH = 7;

    private Problem problem = Problem.random(ThreadLocalRandom.current());
    private int correct;
    private int attempts;
    private int streak;
    private int bestStreak;
    private String feedback;
    private boolean feedbackPositive;

    /**
     * Checks a submitted answer. Invalid input (empty, not a whole number) leaves the
     * current problem and the score unchanged.
     */
    public synchronized void submit(String rawAnswer) {
        String answer = rawAnswer == null ? "" : rawAnswer.trim();
        if (answer.isEmpty() || answer.length() > MAX_ANSWER_LENGTH || !answer.matches("-?\\d+")) {
            setFeedback("Please type a whole number, for example 42.", false);
            return;
        }
        int value = Integer.parseInt(answer);
        attempts++;
        if (problem.isCorrect(value)) {
            correct++;
            streak++;
            bestStreak = Math.max(bestStreak, streak);
            setFeedback("Correct! " + problem.getText() + " = " + problem.getAnswer(), true);
        } else {
            streak = 0;
            setFeedback("Not quite: " + problem.getText() + " = " + problem.getAnswer()
                    + " (you answered " + value + ")", false);
        }
        problem = Problem.random(ThreadLocalRandom.current());
    }

    public synchronized void reset() {
        correct = 0;
        attempts = 0;
        streak = 0;
        bestStreak = 0;
        feedback = null;
        problem = Problem.random(ThreadLocalRandom.current());
    }

    private void setFeedback(String message, boolean positive) {
        feedback = message;
        feedbackPositive = positive;
    }

    /** Returns the pending feedback message once, then clears it (post/redirect/get). */
    public synchronized String takeFeedback() {
        String message = feedback;
        feedback = null;
        return message;
    }

    public synchronized boolean isFeedbackPositive() {
        return feedbackPositive;
    }

    public synchronized String getProblemText() {
        return problem.getText();
    }

    public synchronized int getCorrect() {
        return correct;
    }

    public synchronized int getAttempts() {
        return attempts;
    }

    public synchronized int getStreak() {
        return streak;
    }

    public synchronized int getBestStreak() {
        return bestStreak;
    }
}
