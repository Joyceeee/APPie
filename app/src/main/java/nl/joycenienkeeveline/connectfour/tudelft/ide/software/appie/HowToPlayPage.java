package nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.GestureDetectorCompat;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.content.Context;





public class HowToPlayPage extends Activity{

    //private GestureDetectorCompat gDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Remove title bar & notification bar for a nicer look
        Source: https://stackoverflow.com/questions/2591036/how-to-hide-the-title-bar-for-an-activity-in-xml-with-existing-custom-theme*/
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //this.gDetector = new GestureDetectorCompat(this, this);
        //gDetector.setOnDoubleTapListener(this);
        setContentView(R.layout.activity_how_to_play_page);
    }

    //@Override
    //public boolean onDown(MotionEvent event) {
    //    setContentView(R.layout.activity_how_to_play_page2);
    //}

    public void howtoNextPage(View view) {
        setContentView(R.layout.activity_how_to_play_page2);
    }

    public void howtoNextPage2(View view) {
        setContentView(R.layout.activity_how_to_play_page3);
    }

    public void howtoNextPage3(View view) {
        setContentView(R.layout.activity_how_to_play_page4);
    }

    public void howtoNextPage4(View view) {
        setContentView(R.layout.activity_how_to_play_page5);
    }

    public void howtoNextPage5(View view) {
        setContentView(R.layout.activity_how_to_play_page6);
    }

    public void howtoNextPage6(View view) {
        setContentView(R.layout.activity_how_to_play_page7);
    }

    public void howtoNextPage7(View view) {
        Intent intent = new Intent(this,
                MainMenu.class);
        startActivity(intent);
    }


    //}



}

