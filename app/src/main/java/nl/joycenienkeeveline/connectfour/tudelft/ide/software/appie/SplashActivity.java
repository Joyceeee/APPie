package nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

//import pl.droidsonroids.gif.GifImageView;

public class SplashActivity extends Activity {

    private GifImageView gifImageView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        gifImageView =(GifImageView)findViewById(R.id.gifImageView);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(progressBar.VISIBLE);

        try{
            InputStream inputStream = getAssets().open("dancingcupcake.gif");
            byte [] bytes = IOUtils.toByteArray(inputStream);
            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();
        }
        catch (IOException ex)
        {

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainMenu.class));
                SplashActivity.this.finish();
            }
        },3000);
    }
}



    //https://www.youtube.com/watch?v=Mas3Lnd3WtM
