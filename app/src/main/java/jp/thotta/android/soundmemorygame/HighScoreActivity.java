package jp.thotta.android.soundmemorygame;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import jp.thotta.android.soundmemorygame.R;

public class HighScoreActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        ScoreRecordDBHelper db = new ScoreRecordDBHelper(this);
        int highScore = db.getHighScore();
        TextView textHighScore = (TextView) findViewById(R.id.text_value_highscore);
        textHighScore.setText(String.valueOf(highScore));
        setResults(db);
        Button buttonReturn = (Button)findViewById(R.id.button_return_to_top);
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)v.getContext()).finish();
            }
        });
    }

    private void setResults(ScoreRecordDBHelper db) {
        setModeResult(db, ScoreRecordBean.GAME_MODE_EASY,
                R.id.text_value_question_easy, R.id.text_value_score_easy);
        setModeResult(db, ScoreRecordBean.GAME_MODE_NORMAL,
                R.id.text_value_question_normal, R.id.text_value_score_normal);
        setModeResult(db, ScoreRecordBean.GAME_MODE_HARD,
                R.id.text_value_question_hard, R.id.text_value_score_hard);
        setModeResult(db, ScoreRecordBean.GAME_MODE_SUPER_HARD,
                R.id.text_value_question_super_hard, R.id.text_value_score_super_hard);
    }
    private void setModeResult(ScoreRecordDBHelper db, int mode, int viewQuestionId, int viewScoreId) {
        TextView viewQuestion = (TextView) findViewById(viewQuestionId);
        TextView viewScore = (TextView) findViewById(viewScoreId);
        ScoreRecordBean result = db.getHighScoreObject(mode);
        viewQuestion.setText(String.valueOf(result.getQuestion()));
        viewScore.setText(String.valueOf(result.getScore()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_high_score, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_back) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
