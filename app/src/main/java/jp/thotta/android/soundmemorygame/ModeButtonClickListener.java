package jp.thotta.android.soundmemorygame;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

/**
 * Created by thotta on 14/11/16.
 */
public class ModeButtonClickListener implements View.OnClickListener {
    private int fColumn;
    private int fRow;
    private Activity fActivity;

    public ModeButtonClickListener(int row, int column, Activity activity) {
        super();
        this.fColumn = column;
        this.fRow = row;
        this.fActivity = activity;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(fActivity, GameActivity.class);
        intent.putExtra("column", fColumn);
        intent.putExtra("row", fRow);
        fActivity.startActivity(intent);
    }
}