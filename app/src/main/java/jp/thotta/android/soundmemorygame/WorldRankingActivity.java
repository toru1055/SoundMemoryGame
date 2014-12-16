package jp.thotta.android.soundmemorygame;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;

public class WorldRankingActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private static final int REQUEST_LEADER_BOARD = 9002;
    private boolean mResolvingConnectionFailure = false;

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        Toast.makeText(this, "Connecting to Google Play Game.", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_ranking);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        Button buttonReturn = (Button)findViewById(R.id.button_return_to_top);
        Button buttonRanking = (Button)findViewById(R.id.button_world_ranking);
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)v.getContext()).finish();
            }
        });
        buttonRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleApiClient.disconnect();
                mGoogleApiClient.connect();
                Toast.makeText(v.getContext(), "Connecting to Google Play Game.", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_world_ranking, menu);
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

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("SoundMemoryGame", "WorldRankingActivity.onConnected was called.");
        ScoreRecordDBHelper db = new ScoreRecordDBHelper(this);
        int highScore = db.getHighScore();
        Games.Leaderboards.submitScore(mGoogleApiClient,
                getString(R.string.leader_board_id), highScore);
        startActivityForResult(
                Games.Leaderboards.getLeaderboardIntent(
                        mGoogleApiClient,
                        getString(R.string.leader_board_id)
                ),
                REQUEST_LEADER_BOARD
        );
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            // already resolving
            return;
        }

        int errorCode = connectionResult.getErrorCode();
        if(connectionResult.hasResolution()) {
            if (errorCode == ConnectionResult.SIGN_IN_REQUIRED) {
                try {
                    Log.d("SoundMemoryGame",
                            "WorldRankingActivity.onConnectionFailed: SIGN_IN_REQUIRED.");
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                } catch (IntentSender.SendIntentException e) {
                    mGoogleApiClient.connect();
                }
            } else {
                Toast.makeText(this, "Connection Error.", Toast.LENGTH_LONG).show();
            }
        } else {
            mResolvingConnectionFailure = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            mResolvingConnectionFailure = false;
            if(resultCode == RESULT_OK) {
                if(!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            } else {
                Log.d("SoundMemoryGame",
                        "WorldRankingActivity.onActivityResult: Result is NOT ok.");
                Toast.makeText(this, "Connection was canceled.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
