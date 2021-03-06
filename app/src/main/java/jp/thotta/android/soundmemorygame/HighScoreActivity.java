package jp.thotta.android.soundmemorygame;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import jp.thotta.android.soundmemorygame.R;

public class HighScoreActivity extends Activity {
    private AdView adView;

    private void makeAdView() {
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.ad_unit_id));
        adView.setAdSize(AdSize.SMART_BANNER);
        FrameLayout layout = (FrameLayout) findViewById(R.id.layout_ad_frame);
        layout.addView(adView);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder()
                .addTestDevice(getString(R.string.test_device_id))
                .build();
        adView.loadAd(adRequest);
    }

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
        makeAdView();
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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

        if(id == android.R.id.home) {
            finish();
            return true;
        }
        if(id == R.id.action_back) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
