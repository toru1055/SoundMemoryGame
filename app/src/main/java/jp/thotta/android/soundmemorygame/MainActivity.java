package jp.thotta.android.soundmemorygame;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_easy = (Button)findViewById(R.id.button_easy);
        Button btn_normal = (Button)findViewById(R.id.button_normal);
        Button btn_hard = (Button)findViewById(R.id.button_hard);
        Button btn_super_hard = (Button) findViewById(R.id.button_super_hard);
        btn_easy.setOnClickListener(new ModeButtonClickListener(2, 2, this));
        btn_normal.setOnClickListener(new ModeButtonClickListener(3, 3, this));
        btn_hard.setOnClickListener(new ModeButtonClickListener(4, 4, this));
        btn_super_hard.setOnClickListener(new ModeButtonClickListener(5, 5, this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScoreRecordDBHelper db = new ScoreRecordDBHelper(this);
        int highScore = db.getHighScore();
        TextView textHighScore = (TextView) findViewById(R.id.text_high_score);
        textHighScore.setText(String.valueOf(highScore));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
