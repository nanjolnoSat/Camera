package com.mishaki.camera;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.mishaki.camera.camera.CameraPreview;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Administrator on 2017/4/29.
 */

public class CFActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String FILE_NAME = "file_name";

    private CameraPreview cameraSv;
    private Camera mCamera;

    private Button cf_shoot_again_btn;
    private Button cf_shot_btn;
    private Button cf_confirm_btn;

    private String fileName;
    private byte[] mData;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cf);
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.hor_tv_rotate);
        anim.setFillAfter(true);
        TextView cf_tips_tv = (TextView) findViewById(R.id.cf_tips_tv);
        cf_tips_tv.setAnimation(anim);
        cf_tips_tv.setText("请将证件放入框内");
        cameraSv = (CameraPreview)findViewById(R.id.cf_frame_camera_sv);
        mCamera = Camera.open(0);
        cameraSv.setCamera(mCamera);
        cameraSv.surfaceCreated(cameraSv.getHolder());

        cf_shoot_again_btn = (Button)findViewById(R.id.cf_shoot_again_btn);
        cf_shot_btn = (Button)findViewById(R.id.cf_shot_btn);
        cf_confirm_btn = (Button)findViewById(R.id.cf_confirm_btn);
        OnClickUtil.setOnclick(this,cf_shoot_again_btn,cf_shot_btn,cf_confirm_btn);

        if(getIntent() != null){
            fileName = getIntent().getStringExtra(FILE_NAME);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cf_shot_btn://拍摄
                mCamera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        mData = data;
                    }
                });
                cf_shot_btn.setVisibility(View.INVISIBLE);
                cf_shoot_again_btn.setVisibility(View.VISIBLE);
                cf_confirm_btn.setVisibility(View.VISIBLE);
                break;
            case R.id.cf_shoot_again_btn://重拍
                cameraSv.surfaceCreated(cameraSv.getHolder());
                cf_shot_btn.setVisibility(View.VISIBLE);
                cf_shoot_again_btn.setVisibility(View.INVISIBLE);
                cf_confirm_btn.setVisibility(View.INVISIBLE);
                break;
            case R.id.cf_confirm_btn://确定
                if(mData != null && mData.length != 0) {
                    ByteArrayInputStream is = new ByteArrayInputStream(mData);
//                    File file = new File(fileName);
                    File file = new File(getExternalCacheDir(),"a.jpeg");
                    try {
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        FileOutputStream os = new FileOutputStream(file);
                        os.write(mData);
                        os.flush();
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCamera.release();
    }
}
