package nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.*;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

// extends Activity instead of AppCompatActivity to remove title bar.
public class MainMenu extends Activity {

    private Button otherscreen;
    private Button sparkButtonoverlay;
    private Button sparkButtonoverlay2;
    private SparkButton sparkButton;
    private Handler mHandler = new Handler();

    //Initialise screen size
    private int screenWidth;
    private int screenHeight;

    //Position buttons and titlebar
    private SparkButton play;
    private SparkButton ranking;
    private SparkButton how_to_play;
    private SparkButton settings;
    private ImageView titlebar;

    //Height UI items
    public int play_height;
    private int titlebar_height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Remove title bar & notification bar for a nicer look
        Source: https://stackoverflow.com/questions/2591036/how-to-hide-the-title-bar-for-an-activity-in-xml-with-existing-custom-theme*/
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_menu);

        sparkButtonoverlay=(Button)findViewById(R.id.spark_button_overlay);
        sparkButtonoverlay2=(Button)findViewById(R.id.spark_button_overlay2);
        //sparkButton=(SparkButton)findViewById(R.id.spark_button);

        //Finding button images
        play = (SparkButton) findViewById(R.id.spark_button);
        settings = (SparkButton) findViewById(R.id.spark_button4);
        how_to_play = (SparkButton) findViewById(R.id.spark_button3);
        ranking = (SparkButton) findViewById(R.id.spark_button2);

        //Get screen size
        //Source: see MainActivity
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        //Getting height for UI elements
        //Source: https://stackoverflow.com/questions/3591784/views-getwidth-and-getheight-returns-0
        final View heightTitlebar = (ImageView) findViewById(R.id.titelbar);
        heightTitlebar.post(new Runnable() {
            @Override
            public void run() {
                titlebar_height = heightTitlebar.getHeight(); //height is ready
            }
        });
        final View heightPlay = (SparkButton) findViewById(R.id.spark_button);
        heightPlay.post(new Runnable() {
            @Override
            public void run() {
                play_height = heightPlay.getHeight(); //height is ready

                //Set new y positions buttons with an equal distribution over the height of the device
                play.setY((screenHeight-titlebar_height) / 5 - (play_height / 2));
                ranking.setY((screenHeight - titlebar_height) / 5 * 2 - (play_height / 2));
                how_to_play.setY((screenHeight - titlebar_height) / 5 * 3 - (play_height / 2));
                settings.setY((screenHeight - titlebar_height)/5*4 - (play_height / 2));
            }
        });

    }


    public void playClick(View view) {
        long t1 = System.currentTimeMillis();
        while (true) {
            long t2 = System.currentTimeMillis();
            if (t2 - t1 > 1500) break;
        }

        if (view==sparkButtonoverlay){
        Intent intent = new Intent(this,
                InstructionPageBeforePlay.class);
        startActivity(intent);}
        else{Intent intent = new Intent(this,
                SettingsPage.class);
            startActivity(intent);}
    }

    //Let app close when back button is pressed
    //Source: https://stackoverflow.com/questions/5312334/how-to-handle-back-button-in-activity
    //Source: https://stackoverflow.com/questions/6014028/closing-application-with-exit-button
    public void onBackPressed() {
        finish();
    }

    //sparkButton.setEventListener(new SparkEventListener(){
    //@Override
    //void onEvent(ImageView button, boolean buttonState) {
    //   if (buttonState) {
    // Button is active
    //   } else {
    //      // Button is inactive3
    //  }
    // }
    //});
}



