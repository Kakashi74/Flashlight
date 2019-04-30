package com.shanapTech.flashlight;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageButton flashLight;
    private Camera camera;
    private Parameters parameter;
    private boolean deviceHasFlash;
    private boolean isFlashLightOn = false;
    private MediaPlayer mediaPlayer ;
    private ConstraintLayout constraintLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         constraintLayout = findViewById(R.id.layout);

         mediaPlayer = MediaPlayer.create(MainActivity.this , R.raw.sound);


        flashLight = findViewById(R.id.flash_light);
        deviceHasFlash = getApplication().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if(!deviceHasFlash){
            Toast.makeText(MainActivity.this, "Sorry, you device does not have any camera", Toast.LENGTH_LONG).show();
            return;
        }
        else{
            this.camera = Camera.open(0);
            parameter = this.camera.getParameters();
        }

        flashLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFlashLightOn){
                    turnOnTheFlash();
                    play_Sound();
                    constraintLayout.getResources().getColor(R.color.white);
                }else{
                    turnOffTheFlash();
                    play_Sound();
                }
            }
        });
    }

    private void turnOffTheFlash() {
        parameter.setFlashMode(Parameters.FLASH_MODE_OFF);
        this.camera.setParameters(parameter);
        this.camera.stopPreview();
        isFlashLightOn = false;
        flashLight.setImageResource(R.drawable.ic_play);
    }

    private void turnOnTheFlash() {
        if(this.camera != null){
            parameter = this.camera.getParameters();
            parameter.setFlashMode(Parameters.FLASH_MODE_TORCH);
            this.camera.setParameters(parameter);
            this.camera.startPreview();
            isFlashLightOn = true;
            flashLight.setImageResource(R.drawable.ic_pause);
        }
    }

    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                parameter = camera.getParameters();
            } catch (RuntimeException e) {
                Toast.makeText(MainActivity.this , e.getMessage().toString() , Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void play_Sound()
    {
        mediaPlayer.start();

    }
    @Override
    protected void onStop() {
        super.onStop();
        if(this.camera != null){
            this.camera.release();
            this.camera = null;
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        turnOffTheFlash();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(deviceHasFlash){
            turnOffTheFlash();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCamera();
    }
}

