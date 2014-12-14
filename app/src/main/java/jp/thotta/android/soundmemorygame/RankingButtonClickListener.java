package jp.thotta.android.soundmemorygame;

import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

/**
 * Created by thotta on 14/12/05.
 */
public class RankingButtonClickListener implements View.OnClickListener {
    private SelectModeActivity selectModeActivity;

    public RankingButtonClickListener(SelectModeActivity activity) {
        this.selectModeActivity = activity;
    }

    @Override
    public void onClick(View v) {
        // Display google game's leader board.
        GoogleApiClient googleApiClient = selectModeActivity.getApiClient();
        if(googleApiClient.isConnected()) {
            selectModeActivity.debugLog("[RankingButtonClickListener.onClick] googleApiClient is connected.");
            try {
                selectModeActivity.startActivityForResult(
                        Games.Leaderboards.getLeaderboardIntent(
                                googleApiClient,
                                selectModeActivity.getString(R.string.leader_board_id)
                        ),
                        selectModeActivity.REQUEST_LEADER_BOARD
                );
            } catch(SecurityException e) {
                showConnectingMessage();
                selectModeActivity.debugLog("[RankingButtonClickListener.onClick] " +
                        "Catch SecurityException: " + e.getMessage());
                selectModeActivity.disconnectGoogleApi();
                selectModeActivity.connectGoogleApi();
            }
        } else {
            showConnectingMessage();
            selectModeActivity.debugLog("[RankingButtonClickListener.onClick] googleApiClient is NOT connected.");
            selectModeActivity.connectGoogleApi();
        }
    }

    private void showConnectingMessage() {
        Toast.makeText(selectModeActivity, "Connecting to Google Play Game.", Toast.LENGTH_SHORT).show();
    }
}
