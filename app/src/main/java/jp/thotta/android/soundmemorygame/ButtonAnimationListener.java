package jp.thotta.android.soundmemorygame;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;

/**
 * Created by thotta on 14/11/22.
 */
public class ButtonAnimationListener implements Animation.AnimationListener {
    private Button fButton;
    private Animation fNextAnimation;
    private static SoundGenerator soundGenerator = new SoundGenerator();

    public ButtonAnimationListener(Button button, Animation nextAnimation) {
        this.fNextAnimation = nextAnimation;
        this.fButton = button;
    }

    public static Animation pushAnimation(Button button, Animation animation) {
        Animation newAnimation = new RotateAnimation(0, 0);
        if(button.getId() != R.id.button_start_stop) {
            newAnimation.setDuration(200);
        } else {
            newAnimation.setDuration(1000);
        }
        newAnimation.setAnimationListener(new ButtonAnimationListener(button, animation));
        return newAnimation;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        if(fButton.getId() != R.id.button_start_stop) {
            fButton.setPressed(true);
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if(fButton.getId() != R.id.button_start_stop) {
            soundGenerator.play(fButton.getId());
            fButton.setPressed(false);
        }
        if(fNextAnimation != null &&
                GameManager.getInstance().getStatus() == GameManager.STATUS_QUESTIONING) {
            fButton.startAnimation(fNextAnimation);
        } else {
            GameManager.getInstance().finishQuestion();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
