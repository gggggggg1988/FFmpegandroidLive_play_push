package com.ws.ffmpegandroidwslive;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {


    private static final String TAG= "MainActivity";
    private Button mTakeButton;
    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private boolean isRecording = false;
    private Button whatch_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mTakeButton=(Button)findViewById(R.id.take_button);
        mTakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, PushVideo.class);
                startActivity(in);
            }
        });
        whatch_btn = (Button) findViewById(R.id.pull_stream);
        whatch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, WhatchActivity.class);
                startActivity(in);
            }
        });




    }






}


