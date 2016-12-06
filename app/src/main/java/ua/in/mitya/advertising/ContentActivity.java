package ua.in.mitya.advertising;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.VideoView;

public class ContentActivity extends AppCompatActivity {

    private String video, clickUrl;
    private VideoView videoView;
    private int position = 0;
    private ScrollView scrollView;
    private int scrollY;
    private ProgressDialog downloadVideoDialog;
    SharedPreferences sPref;

    final String SAVED_TIME = "saved_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        video = getIntent().getStringExtra("video");
        clickUrl = getIntent().getStringExtra("click");

        scrollView = (ScrollView) findViewById(R.id.activity_content);

        downloadVideoDialog = new ProgressDialog(ContentActivity.this);
        downloadVideoDialog.setMessage(getResources().getString(R.string.download_message));
        downloadVideoDialog.setCancelable(false);
        downloadVideoDialog.show();

        videoView = (VideoView) findViewById(R.id.video_view);
        videoView.setVideoURI(Uri.parse(video));
        videoView.setMediaController(null);
        videoView.requestFocus();

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent mediaIntent = new Intent(ContentActivity.this, AdvertisementActivity.class);
                mediaIntent.putExtra("click", clickUrl);
                videoView.pause();
                saveTime();
                startActivity(mediaIntent);
                return false;
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                downloadVideoDialog.dismiss();
                loadTime();
                videoView.seekTo(position);
                if (position >= 0) {
                    videoView.start();
                } else {
                    videoView.pause();
                }
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                scrollY = scrollView.getScrollY();
                if (scrollY >= videoView.getHeight() / 2) {
                    videoView.pause();
                } else if (scrollY == 0) {
                    videoView.start();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sPref.edit().clear().apply();
        finish();
    }

    void saveTime() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt(SAVED_TIME, videoView.getCurrentPosition());
        ed.apply();
    }

    void loadTime() {
        sPref = getPreferences(MODE_PRIVATE);
        position = sPref.getInt(SAVED_TIME, 0);
    }
}
