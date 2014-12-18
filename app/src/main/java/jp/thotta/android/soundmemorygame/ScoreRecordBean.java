package jp.thotta.android.soundmemorygame;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Created by thotta on 14/11/30.
 */
public class ScoreRecordBean {
    //TODO: You have to change this value, when you release this game.
    //public static final int STAGE_QUESTION_SIZE = 3;
    public static final int STAGE_QUESTION_SIZE = 12;

    public static final int GAME_MODE_EASY = 2;
    public static final int GAME_MODE_NORMAL = 3;
    public static final int GAME_MODE_HARD = 4;
    public static final int GAME_MODE_SUPER_HARD = 5;

    private int mode;
    private int score;
    private int questionSize;

    public int getScore() {
        return score;
    }
    public int getQuestion() {
        return questionSize;
    }

    public static String modeName(int mode) {
        switch (mode) {
            case GAME_MODE_EASY:
                return "Easy";
            case GAME_MODE_NORMAL:
                return "Normal";
            case GAME_MODE_HARD:
                return "Hard";
            case GAME_MODE_SUPER_HARD:
                return "Super Hard";
            default:
                return null;
        }
    }

    public ScoreRecordBean(int mode, int score, int questionSize) {
        this.mode = mode;
        this.score = score;
        this.questionSize = questionSize;
    }

    public boolean isCleared() {
        return (this.questionSize >= STAGE_QUESTION_SIZE);
    }

    @Override
    public String toString() {
        ByteArrayOutputStream buff = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(buff);
        ps.printf("%-15s",  modeName(mode));
        return buff.toString() + ": " +
                "Num=" + questionSize +
                ",   HighScore=" + score;
    }
}
