package jp.thotta.android.soundmemorygame;

import android.view.View;

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
        GoogleApiClient googleApiClient = mainActivity.getApiClient();
        if(googleApiClient.isConnected()) {
            mainActivity.startActivityForResult(
                    Games.Leaderboards.getLeaderboardIntent(
                            googleApiClient,
                            mainActivity.getString(R.string.leader_board_id)
                    ),
                    mainActivity.REQUEST_LEADER_BOARD
            );
        } else {
            //TODO: Display error dialog.
        }
    }
}
