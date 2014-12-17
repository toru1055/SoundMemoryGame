package jp.thotta.android.soundmemorygame;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class TopActivity extends Activity {
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
        setContentView(R.layout.activity_top);

        Button buttonPlay = (Button) findViewById(R.id.button_play);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, SelectModeActivity.class);
                context.startActivity(intent);
            }
        });

        Button buttonHighScore = (Button) findViewById(R.id.button_highScore);
        buttonHighScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, HighScoreActivity.class);
                context.startActivity(intent);
            }
        });

        Button buttonWorldRanking = (Button) findViewById(R.id.button_ranking);
        buttonWorldRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, WorldRankingActivity.class);
                context.startActivity(intent);
            }
        });

        makeAdView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_cancel) {
            moveTaskToBack(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
