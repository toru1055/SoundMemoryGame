package jp.thotta.android.soundmemorygame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import static android.view.View.OnClickListener;


public class GameActivity extends Activity {
    private int fColumn = 0;
    private int fRow = 0;
    private final GameManager gameManager = GameManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        fColumn = intent.getIntExtra("column", 2);
        fRow = intent.getIntExtra("row", 2);
        ButtonLayoutGenerator.generate(fRow, fColumn, this);
        gameManager.setActivity(this);
        Button btn = (Button) findViewById(R.id.button_start_stop);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GameManager.getInstance().getStatus() == GameManager.STATUS_INITIALIZED) {
                    gameManager.startGame();
                    ((Button) v).setText("Stop");
                }
            }
        });
    }

    public View findButtonByRowCol(int row, int col) {
        return findViewById(row * fRow + col);
    }

    public int getRow() {
        return fRow;
    }
    public int getColumn() {
        return fColumn;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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
