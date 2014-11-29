package jp.thotta.android.soundmemorygame;

import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

/**
 * Created by thotta on 14/11/26.
 */
public class ScoreManager {
    private static final int MILL = 1000;
    private GameActivity gameActivity;
    private long startTime;
    private long endTime;
    private int score;

    public ScoreManager(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    public void initialize() {
        this.score = 0;;
        updateView();
    }

    public int getScore() {
        return score;
    }

    public void startScoring() {
        this.startTime = Calendar.getInstance().getTimeInMillis();
    }

    public void endScoring() {
        this.endTime = Calendar.getInstance().getTimeInMillis();
    }

    public void calcScore(int questionSize) {
        long duration = endTime - startTime;
        if(duration < MILL) {
            duration = MILL;
        }
        this.score += Math.pow(questionSize, 2) *
                Math.pow(gameActivity.getRow(), 3) * MILL / duration;
    }

    public void updateView() {
        TextView textView = (TextView) gameActivity.findViewById(R.id.text_score);
        textView.setText(String.valueOf(this.score));
    }
}
