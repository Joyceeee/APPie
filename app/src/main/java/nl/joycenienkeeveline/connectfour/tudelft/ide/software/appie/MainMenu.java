package nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.varunest.sparkbutton.SparkButton;

// extends Activity instead of AppCompatActivity to remove title bar.
public class MainMenu extends Activity {

    private Button otherscreen;

    //Initialise screen size
    private int screenWidth;
    private int screenHeight;

    //Position buttons
    private SparkButton play;
    private SparkButton ranking;
    private SparkButton how_to_play;
    private SparkButton settings;

    private ImageView titlebar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Remove title bar & notification bar for a nicer look
        Source: https://stackoverflow.com/questions/2591036/how-to-hide-the-title-bar-for-an-activity-in-xml-with-existing-custom-theme*/
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_menu);

        otherscreen=(Button)findViewById(R.id.other_screen_button);

        //Get screen size
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        //Finding button images
        play = (SparkButton) findViewById(R.id.spark_button);
        settings = (SparkButton) findViewById(R.id.spark_button4);
        how_to_play = (SparkButton) findViewById(R.id.spark_button3);
        ranking = (SparkButton) findViewById(R.id.spark_button2);
        titlebar = (ImageView)findViewById(R.id.titelbar);

        play.setY(1280);
        ranking.setY((screenHeight-50)/8*2-165);
        how_to_play.setY((screenHeight-50)/8*3-165);
        settings.setY((screenHeight-50)/8*4-165);


    }

    public void newGameButtonClicked(View view) {
        Intent intent = new Intent(this,
                InstructionPageBeforePlay.class);
        //startActivity(intent);
        System.out.print("screenHeight");
        System.out.println(screenHeight);
        System.out.print("play");
        System.out.println(play.getY());
        System.out.print("gety");
        System.out.println(((screenHeight/4)-165));
        }

}



