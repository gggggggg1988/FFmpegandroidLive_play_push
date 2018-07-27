package com.ws.ffmpegandroidwslive;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ws.ffmpegandroidcameralive.WSPlayer;

import java.io.IOException;

public class PushVideo extends AppCompatActivity {

    private static final String TAG= "PushVideoActivity";
    private Button mTakeButton;
    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private boolean isRecording = false;
    private Button whatch_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_video);

        final Camera.PreviewCallback mPreviewCallbacx=new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] arg0, Camera arg1) {

                WSPlayer.start(arg0);
            }
        };

        mTakeButton=(Button)findViewById(R.id.take_button);

        PackageManager pm=this.getPackageManager();
        boolean hasCamera=pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
                pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
                Build.VERSION.SDK_INT<Build.VERSION_CODES.GINGERBREAD;
        if(!hasCamera)
            mTakeButton.setEnabled(false);

        mTakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(mCamera!=null)
                {
                    if (isRecording) {
                        mTakeButton.setText("Start");
                        mCamera.setPreviewCallback(null);
                        Toast.makeText(PushVideo.this, "encode done", Toast.LENGTH_SHORT).show();
                        isRecording = false;
                    }else {
                        mTakeButton.setText("Stop");
                        WSPlayer.initialize(mCamera.getParameters().getPreviewSize().width,mCamera.getParameters().getPreviewSize().height,"rtmp://192.168.0.195/oflaDemo/stream1");
                        mCamera.setPreviewCallback(mPreviewCallbacx);
                        isRecording = true;
                    }
                }
            }
        });


        mSurfaceView=(SurfaceView)findViewById(R.id.surfaceView1);
        SurfaceHolder holder=mSurfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder arg0) {
                // TODO Auto-generated method stub
                if(mCamera!=null)
                {
                    mCamera.stopPreview();
                    mSurfaceView = null;
                    mSurfaceHolder = null;
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder arg0) {
                // TODO Auto-generated method stub
                try{
                    if(mCamera!=null){
                        mCamera.setPreviewDisplay(arg0);
                        mSurfaceHolder=arg0;
                    }
                }catch(IOException exception){
                    Log.e(TAG, "Error setting up preview display", exception);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                if(mCamera==null) return;
                Camera.Parameters parameters=mCamera.getParameters();
                parameters.setPreviewSize(640,480);
                parameters.setPictureSize(640,480);
                mCamera.setParameters(parameters);
                try{
                    mCamera.startPreview();
                    mSurfaceHolder=arg0;
                }catch(Exception e){
                    Log.e(TAG, "could not start preview", e);
                    mCamera.release();
                    mCamera=null;
                }
            }
        });

    }

    @TargetApi(9)
    @Override
    protected void onResume(){
        super.onResume();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.GINGERBREAD){
            mCamera=Camera.open(0);
        }else
        {
            mCamera=Camera.open();
        }
        mCamera.setDisplayOrientation(90);
    }

    @Override
    protected void onPause(){
        super.onPause();
        WSPlayer.stop();
        WSPlayer.close();
        if(mCamera!=null){
            mCamera.release();
            mCamera=null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WSPlayer.stop();
        WSPlayer.close();
        if(mCamera!=null){
            mCamera.release();
            mCamera=null;
        }
    }


}
