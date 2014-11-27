package jp.thotta.android.soundmemorygame;

import android.widget.TextView;

/**
 * Created by thotta on 14/11/27.
 */
public class LifeManager {
    private static final int MAX_LIFE = 3;
    private int life;
    private GameActivity gameActivity;

    public LifeManager(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
        initialize();
    }

    public void initialize() {
        this.life = MAX_LIFE;
        updateView();
    }

    public void deleteLife() {
        if(life > 0) {
            life--;
        }
        updateView();
    }

    public int getLife() {
        return life;
    }

    private void updateView() {
        String lifeString = "";
        TextView textView = (TextView) gameActivity.findViewById(R.id.text_life);
        int i=0;
        for(; i < life; i++) {
            lifeString += "★";
        }
        for(; i < MAX_LIFE; i++) {
            lifeString += "　";
        }
        textView.setText(lifeString);
    }
}
