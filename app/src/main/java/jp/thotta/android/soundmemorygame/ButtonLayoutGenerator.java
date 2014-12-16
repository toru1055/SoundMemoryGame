package jp.thotta.android.soundmemorygame;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * Created by thotta on 14/11/16.
 */
public class ButtonLayoutGenerator {
    private static String[] onkai = new String[16];
    private static void setOnkai() {
        onkai[0] = "Do";
        onkai[1] = "Re";
        onkai[2] = "Mi";
        onkai[3] = "Fa";
        onkai[4] = "Sol";
        onkai[5] = "La";
        onkai[6] = "Si";
        onkai[7] = "Do'";
        onkai[8] = "Re'";
        onkai[9] = "Mi'";
        onkai[10] = "Fa'";
        onkai[11] = "Sol'";
        onkai[12] = "La'";
        onkai[13] = "Si'";
        onkai[14] = "Do''";
        onkai[15] = "Re''";
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
                int widthPixels = (int)(activity.getResources().getDisplayMetrics().widthPixels - 32 * scale);
                int circlePixels = widthPixels / column;
                btn.setWidth(circlePixels);
                btn.setHeight(circlePixels);
                btn.setId(iRow * row + iCol);
                btn.setBackgroundResource(R.drawable.circle_button);
                btn.setText(onkai[btn.getId() % onkai.length]);
                btn.setOnTouchListener(circleTouchListener);
                btn.setTextColor(Color.parseColor("#ffffff"));
                btn.setTextSize(circlePixels / 4);
                btn.setTypeface(null, Typeface.BOLD);
                tableRow.addView(btn);
            }
            tableLayout.addView(tableRow);
        }
        return tableLayout;
    }
}
