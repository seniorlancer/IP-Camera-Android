package com.seniorlancer.mycamera;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
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

public class CameraMonitorActivity extends AppCompatActivity implements VlcListener, View.OnClickListener, SurfaceHolder.Callback {
    private VlcVideoLibrary vlcVideoLibrary;

    private SurfaceView surfaceView;
    private TextView bufferingTxt;
    private Button btnRecording;

    private SurfaceHolder surfaceHolder;

    String sampleRTSP = "rtsp://192.168.105.39:1935/vod/sample.mp4";
    Boolean bRecording = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_monitor);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        surfaceView = (SurfaceView) findViewById(R.id.surface_monitor_player);
        bufferingTxt = (TextView) findViewById(R.id.textview_monitor_loading);
        btnRecording = (Button) findViewById(R.id.btn_monitor_recording);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        btnRecording.setOnClickListener(this);

        initPlayer();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_monitor_recording:
                if(bRecording) {
                    btnRecording.setText(getResources().getString(R.string.start_record));
                } else {
                    btnRecording.setText(getResources().getString(R.string.stop_record));
                }
                bRecording = !bRecording;
                break;
            default:
                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        vlcVideoLibrary.play(sampleRTSP);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
