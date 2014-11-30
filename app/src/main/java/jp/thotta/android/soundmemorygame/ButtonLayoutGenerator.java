package jp.thotta.android.soundmemorygame;

import android.app.Activity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * Created by thotta on 14/11/16.
 */
public class ButtonLayoutGenerator {
    private static String[] onkai = new String[16];
    private static void setOnkai() {
        onkai[0] = "ド";
        onkai[1] = "レ";
        onkai[2] = "ミ";
        onkai[3] = "ファ";
        onkai[4] = "ソ";
        onkai[5] = "ラ";
        onkai[6] = "シ";
        onkai[7] = "ド";
        onkai[8] = "レ";
        onkai[9] = "ミ";
        onkai[10] = "ファ";
        onkai[11] = "ソ";
        onkai[12] = "ラ";
        onkai[13] = "シ";
        onkai[14] = "ド";
        onkai[15] = "レ";
    }
    public static TableLayout generate(int row, int column, Activity activity) {
        setOnkai();
        TableLayout tableLayout = (TableLayout)activity.findViewById(R.id.button_table);
        CircleTouchListener circleTouchListener = new CircleTouchListener(activity);
        for(int iRow = 0; iRow < row; iRow++) {
            TableRow tableRow = new TableRow(activity);
            for(int iCol = 0; iCol < column; iCol++) {
                Button btn = new Button(activity);
                float scale = activity.getResources().getDisplayMetrics().density;
                int widthPixels = (int)(activity.getResources().getDisplayMetrics().widthPixels * 0.9);
                btn.setWidth((int)(widthPixels / column));
                btn.setHeight((int) (widthPixels / column));
                btn.setId(iRow * row + iCol);
                btn.setBackgroundResource(R.drawable.circle_button);
                if(row <= 4) {
                    btn.setText(onkai[btn.getId()]);
                } else {
                    btn.setText(String.valueOf(btn.getId()));
                }
                btn.setOnTouchListener(circleTouchListener);
                tableRow.addView(btn);
            }
            tableLayout.addView(tableRow);
        }
        return tableLayout;
    }
}
