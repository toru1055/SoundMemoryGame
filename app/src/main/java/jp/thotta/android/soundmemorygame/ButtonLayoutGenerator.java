package jp.thotta.android.soundmemorygame;

import android.app.Activity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import static android.view.ViewGroup.LayoutParams;

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
//        tableLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        for(int iRow = 0; iRow < row; iRow++) {
            TableRow tableRow = new TableRow(activity);
            for(int iCol = 0; iCol < column; iCol++) {
                Button btn = new Button(activity);
                final float scale = activity.getResources().getDisplayMetrics().density;
                btn.setWidth((int)(120*scale));
                btn.setHeight((int) (120 * scale));
                btn.setId(iRow * row + iCol);
                btn.setBackground(activity.getResources().getDrawable(R.drawable.circle_button));
                btn.setText("[" + iRow + ", " + iCol + "] " + onkai[btn.getId()]);
                btn.setOnTouchListener(new CircleTouchListener());
                tableRow.addView(btn);
            }
            tableLayout.addView(tableRow);
        }
        return tableLayout;
    }
}
