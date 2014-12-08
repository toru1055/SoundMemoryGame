package jp.thotta.android.soundmemorygame;

import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

/**
 * Created by thotta on 14/12/05.
 */
public class RankingButtonClickListener implements View.OnClickListener {
    private MainActivity mainActivity;

    public RankingButtonClickListener(MainActivity activity) {
        this.mainActivity = activity;
    }

    @Override
    public void onClick(View v) {
        // Display google game's leader board.
        GoogleApiClient googleApiClient = mainActivity.getApiClient();
        if(googleApiClient.isConnected()) {
            mainActivity.debugLog("[RankingButtonClickListener.onClick] googleApiClient is connected.");
            try {
                mainActivity.startActivityForResult(
                        Games.Leaderboards.getLeaderboardIntent(
                                googleApiClient,
                                mainActivity.getString(R.string.leader_board_id)
                        ),
                        mainActivity.REQUEST_LEADER_BOARD
                );
            } catch(SecurityException e) {
                showConnectingMessage();
                mainActivity.debugLog("[RankingButtonClickListener.onClick] " +
                        "Catch SecurityException: " + e.getMessage());
                mainActivity.disconnectGoogleApi();
                mainActivity.connectGoogleApi();
            }
        } else {
            showConnectingMessage();
            mainActivity.debugLog("[RankingButtonClickListener.onClick] googleApiClient is NOT connected.");
            mainActivity.connectGoogleApi();
        }
    }

    private void showConnectingMessage() {
        Toast.makeText(mainActivity, "Connecting to Google Play Game.", Toast.LENGTH_SHORT).show();
    }
}
