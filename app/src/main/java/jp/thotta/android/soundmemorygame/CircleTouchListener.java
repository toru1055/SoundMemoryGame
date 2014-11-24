package jp.thotta.android.soundmemorygame;

import android.os.AsyncTask;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by thotta on 14/11/16.
 */
public class CircleTouchListener implements View.OnTouchListener {
    private final static SoundGenerator fSoundGenerator = new SoundGenerator();

    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN) {
            GameManager.getInstance().answer(v.getId());
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    fSoundGenerator.play(v.getId());
                }
            };
            new Thread(r).start();
        }
        return false;
    }
}
