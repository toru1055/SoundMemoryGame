package jp.thotta.android.soundmemorygame;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.plus.Plus;


public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private AdView adView;
    private static final int RC_SIGN_IN = 9001;
    public static final int REQUEST_LEADER_BOARD = 9002;

    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate", "onCreate is called");
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

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
        makeAdView();
    }

    private void makeAdView() {
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.ad_unit_id));
        adView.setAdSize(AdSize.BANNER);
        FrameLayout layout = (FrameLayout) findViewById(R.id.layout_ad_frame);
        layout.addView(adView);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder()
                .addTestDevice(getString(R.string.test_device_id))
                .build();
        adView.loadAd(adRequest);
    }

    public GoogleApiClient getApiClient() {
        return mGoogleApiClient;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient.isConnected()) {
            Log.d("SoundMemoryGame", "[onStart] GoogleApiClient is connected.");
        } else {
            Log.d("SoundMemoryGame", "[onStart] GoogleApiClient is NOT connected.");
            if(!mResolvingConnectionFailure) {
                Log.d("SoundMemoryGame", "[onStart] mResolvingConnectionFailure is FALSE.");
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            Log.d("SoundMemoryGame", "[onStop] GoogleApiClient is connected.");
            mGoogleApiClient.disconnect();
        } else {
            Log.d("SoundMemoryGame", "[onStop] GoogleApiClient is NOT connected.");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("SoundMemoryGame", "[onDestroy] onDestroy was called.");
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
        int highScore = db.getHighScore();
        if(mGoogleApiClient.isConnected()) {
            debugLog("[onResume] submit leader score.");
            Games.Leaderboards.submitScore(mGoogleApiClient,
                    getString(R.string.leader_board_id), highScore);
        } else {
            debugLog("[onResume] GoogleApi is NOT connected.");
        }
        TextView textHighScore = (TextView) findViewById(R.id.text_high_score);
        textHighScore.setText(String.valueOf(highScore));
        showResults(db);
        setModeButtonStatusAll(db);
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

    private void showResults(ScoreRecordDBHelper db) {
        showResultView(db, R.id.text_results_easy, ScoreRecordBean.GAME_MODE_EASY);
        showResultView(db, R.id.text_results_normal, ScoreRecordBean.GAME_MODE_NORMAL);
        showResultView(db, R.id.text_results_hard, ScoreRecordBean.GAME_MODE_HARD);
        showResultView(db, R.id.text_results_super_hard, ScoreRecordBean.GAME_MODE_SUPER_HARD);
    }

    private void showResultView(ScoreRecordDBHelper db, int viewId, int mode) {
        TextView resultView = (TextView) findViewById(viewId);
        ScoreRecordBean result = db.getHighScoreObject(mode);
        resultView.setText(result.toString());
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("SoundMemoryGame", "GoogleApiClient.onConnected was called.");
        ScoreRecordDBHelper db = new ScoreRecordDBHelper(this);
        int highScore = db.getHighScore();
        debugLog("[onConnected] submit leader score.");
        Games.Leaderboards.submitScore(mGoogleApiClient,
                getString(R.string.leader_board_id), highScore);

        Button worldRankingButton = (Button) findViewById(R.id.button_world_ranking);
        worldRankingButton.setOnClickListener(new RankingButtonClickListener(this));
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("SoundMemoryGame", "GoogleApiClient.onConnectionSuspended was called.");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("SoundMemoryGame", "[onActivityResult] requestCode=" +
                requestCode + ", resultCode=" + resultCode);
        if(requestCode == RC_SIGN_IN) {
            mResolvingConnectionFailure = false;
            if(resultCode == RESULT_OK) {
                Log.d("SoundMemoryGame", "[onActivityResult] result OK.");
                if(!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            } else {
                Log.d("SoundMemoryGame", "[onActivityResult] resultCode is NOT OK.");
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        debugLog("[onConnectionFailed] onConnectionFailed method was called.");
        if (mResolvingConnectionFailure) {
            // already resolving
            return;
        }

        int errorCode = connectionResult.getErrorCode();
        if(connectionResult.hasResolution()) {
            debugLog("[onConnectionFailed] connectionResult.hasResolution()");
            if (errorCode == ConnectionResult.SIGN_IN_REQUIRED) {
                try {
                    debugLog("[onConnectionFailed] connectionResult.startResolutionForResult(this, RC_SIGN_IN)");
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                } catch (IntentSender.SendIntentException e) {
                    debugLog("[onConnectionFailed] Catch startResolutionForResult error: " + e.getMessage() );
                    mGoogleApiClient.connect();
                }
            } else {
                debugLog("[onConnectionFailed] errorCode is NOT SIGN_IN_REQUIRED");
            }
        } else {
            mResolvingConnectionFailure = true;
            Log.d("SoundMemoryGame", "connectionResult.hasResolution() is false.");
        }
        String connectionResultString = connectionResult.toString();
        Log.d("SoundMemoryGame", "GoogleApiClient.onConnectionFailed was called. ErrorCode=" +
                String.valueOf(errorCode) + ", ConnectionResult=" + connectionResultString);
    }

    private void debugLog(String l) {
        Log.d("SoundMemoryGame", l);
    }
}
