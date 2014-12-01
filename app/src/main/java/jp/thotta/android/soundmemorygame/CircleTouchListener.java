package jp.thotta.android.soundmemorygame;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by thotta on 14/11/16.
 */
public class CircleTouchListener implements View.OnTouchListener {
    private final static SoundGenerator fSoundGenerator = SoundGenerator.getInstance();
    private Activity fGameActivity;

    public CircleTouchListener(Activity activity) {
        this.fGameActivity = activity;
    }

    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN) {
            if (GameManager.getInstance().getStatus() == GameManager.STATUS_WAIT_ANSWER) {
                final boolean isCorrect = GameManager.getInstance().answer(v.getId());
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        if(isCorrect) {
                            fSoundGenerator.play(v.getId());
                        } else {
                            fSoundGenerator.playBoo();
                        }
                    }
                };
                new Thread(r).start();
            } else {
                Toast.makeText(
                        fGameActivity,
                        "Question hasn't finished! Please wait.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
        return false;
    }
}
