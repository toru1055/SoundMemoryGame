package jp.thotta.android.soundmemorygame;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class SelectModeActivity extends Activity {
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate", "onCreate is called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode);

        Button btn_easy = (Button)findViewById(R.id.button_easy);
        Button btn_normal = (Button)findViewById(R.id.button_normal);
        Button btn_hard = (Button)findViewById(R.id.button_hard);
        Button btn_super_hard = (Button) findViewById(R.id.button_super_hard);
        btn_easy.setOnClickListener(new ModeButtonClickListener(2, 2, this));
        btn_normal.setOnClickListener(new ModeButtonClickListener(3, 3, this));
        btn_hard.setOnClickListener(new ModeButtonClickListener(4, 4, this));
        btn_super_hard.setOnClickListener(new ModeButtonClickListener(5, 5, this));

        makeAdView();
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

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

    private void setModeButtonStatusAll(ScoreRecordDBHelper db) {
        Button btn_normal = (Button)findViewById(R.id.button_normal);
        Button btn_hard = (Button)findViewById(R.id.button_hard);
        Button btn_super_hard = (Button) findViewById(R.id.button_super_hard);
        setModeButtonStatus(db, btn_normal, ScoreRecordBean.GAME_MODE_EASY);
        setModeButtonStatus(db, btn_hard, ScoreRecordBean.GAME_MODE_NORMAL);
        setModeButtonStatus(db, btn_super_hard, ScoreRecordBean.GAME_MODE_HARD);
    }
    private void setModeButtonStatus(ScoreRecordDBHelper db, Button button, int previousMode) {
        ScoreRecordBean previousModeResult = db.getHighScoreObject(previousMode);
        button.setEnabled(previousModeResult.isCleared());
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScoreRecordDBHelper db = new ScoreRecordDBHelper(this);
        setModeButtonStatusAll(db);
        setResults(db);
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

        if(id == R.id.action_back) {
            finish();
            return true;
        }
        if(id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
