package com.seniorlancer.mycamera;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pedro.vlc.VlcListener;
import com.pedro.vlc.VlcVideoLibrary;

import org.videolan.libvlc.MediaPlayer;

import java.util.Arrays;

public class CameraMonitorActivity extends AppCompatActivity implements VlcListener {
    private VlcVideoLibrary vlcVideoLibrary;

    private SurfaceView surfaceView;
    private TextView bufferingTxt;

    String sampleRTSP = "rtsp://192.168.105.39:1935/vod/sample.mp4";
    Handler handler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_monitor);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        surfaceView = (SurfaceView) findViewById(R.id.surface_monitor_camera);
        bufferingTxt = (TextView) findViewById(R.id.buffering_txt);

        initPlayer();

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                vlcVideoLibrary.play(sampleRTSP);
            }
        }, 100);

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError() {
        bufferingTxt.setVisibility(View.GONE);
        vlcVideoLibrary.stop();

    }

    private void initPlayer() {
        vlcVideoLibrary = null;
        vlcVideoLibrary = new VlcVideoLibrary(this, this, surfaceView);
    }

    @Override
    public void onBuffering(MediaPlayer.Event event) {
        if (event.getBuffering() < 1) {
            bufferingTxt.setVisibility(View.VISIBLE);
            bufferingTxt.setText("Connecting!");
        } else if (event.getBuffering() < 100) {
            bufferingTxt.setVisibility(View.VISIBLE);
            bufferingTxt.setText("Loading " + event.getBuffering() + "%");
        } else {
            bufferingTxt.setVisibility(View.GONE);
        }
    }

}
