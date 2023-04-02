package com.example.quizkiakya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spalsh_screen);
        LottieAnimationView anim = findViewById(R.id.animationView);
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    anim.playAnimation();
                    sleep(5000);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                finally {
                    Intent intent = new Intent(SplashScreen.this,LoginWindow.class);
                    finish();
                    startActivity(intent);
                }
            }
        };thread.start();
    }
}