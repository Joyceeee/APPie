package nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie;

/*DOEN:
FIXEN ctrl F FIX
GIF
CODE SARAH ws4 --> Rolling gebruikt workshop
sara 5
UITLEG OVERAL BIJ

VRAAG ADRIE:
Maakt het uit als de code gigantisch lang is (moeten we onderverdelen
in aparte classfiles) of maakt het niet uit?)
 */

import android.app.Activity;
import android.graphics.Point;
import android.media.Image;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.view.*;
import android.os.Handler;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie.R;

//Extend Activity instead of AppCompatActivity to remove title bar
public class MainActivity extends Activity {

    //Initialising the GUI elements
    //Source: https://coding-with-sara.thinkific.com/courses/take/catch-the-ball-android-studio-game-tutorial/lessons/1510621-3-findviewbyid-ontouchevent
    private TextView timer;
    private ImageView bitmoji1;
    private ImageView cake1;
    private ImageView cake2;
    private ImageView umbrella;
    private ImageView sponge;

    //Initialising size screen
    private int screenWidth;
    private int screenHeight;
    private int frameWidth;

    //Position falling objects:
    private int cake1x;
    private int cake1y;
    private int cake2x;
    private int cake2y;
    private int spongex;
    private int spongey;
    private int umbrellax;
    private int umbrellay;


    //Position TEMPORARY DElETE
    private int boxX;

    //Timer Check -_DELETE
    private long check;

    //Timer
    //Source: http://en.proft.me/2017/11/18/how-create-count-timer-android/
    long startTime, timeInMilliseconds = 0;
    Handler customHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Remove title bar & notification bar for a nicer look
        Source: https://stackoverflow.com/questions/2591036/how-to-hide-the-title-bar-for-an-activity-in-xml-with-existing-custom-theme*/
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //Finding elements GUI
        //Source: https://coding-with-sara.thinkific.com/courses/take/catch-the-ball-android-studio-game-tutorial/lessons/1510621-3-findviewbyid-ontouchevent
        timer = (TextView)findViewById(R.id.timer);
        bitmoji1 = (ImageView)findViewById(R.id.bitmoji1);
        cake1 = (ImageView)findViewById(R.id.cake1);
        cake2 = (ImageView)findViewById(R.id.cake2);
        umbrella = (ImageView)findViewById(R.id.umbrella);
        sponge = (ImageView)findViewById(R.id.sponge);

        FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
        frameWidth = frame.getWidth();

        //Move falling objects out of screen --> FIXEN WANT NOG NIET OP MOBIEL GOED
        cake1.setX(-80);
        cake1.setY(-80);
        cake2.setX(-200);
        cake2.setY(200);
        sponge.setX(-200);
        sponge.setY(200);
        umbrella.setX(-200);
        umbrella.setY(200);

        //Get screen size
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        //Temporary DELETE
        boxX = 500;

        //Stop countdown, start game
        waitFunctionStart();


    }

    public void waitFunctionStart (){
        //Make fade in animation
        //Source:https://stackoverflow.com/questions/2597329/how-to-do-a-fadein-of-an-image-on-an-android-activity-screen
        final Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);

        //Countdown before game can start:
        //Source: https://developer.android.com/reference/android/os/CountDownTimer.html
        new CountDownTimer(3000, 1000) {
            //DELETE: Alleen om te checken
            public void onTick(long millisUntilFinished) {
                check = millisUntilFinished / 1000;
                System.out.println(check);
            }
            public void onFinish() {
                bitmoji1.setX(boxX);

                //Starting falling objects
                changePos();

                //Gif laten verdwijnen

                //Run timer
                //Source: http://en.proft.me/2017/11/18/how-create-count-timer-android/
                startTime = SystemClock.uptimeMillis();
                customHandler.postDelayed(updateTimerThread, 0);

                //Fade in time in background
                //Source: https://stackoverflow.com/questions/2597329/how-to-do-a-fadein-of-an-image-on-an-android-activity-screen
                timer.startAnimation(myFadeInAnimation);
                timer.setVisibility(View.VISIBLE);

                //Hier functie aanroepen die hij moet beginnen
                //Spel
                //UITLEGG
            }
        }.start();

    }



    //Make objects fall down
    //Source: https://coding-with-sara.thinkific.com/courses/take/catch-the-ball-android-studio-game-tutorial/lessons/1510625-5-set-the-balls
    //FIX
    public void changePos(){

// FIX FADE
        //Creating new runnable for falling objects
        //Source: https://stackoverflow.com/questions/1921514/how-to-run-a-runnable-thread-in-android-at-defined-intervals
        final Handler handlerFallingObjects = new Handler();

        final Runnable runnableFallingObjects = new Runnable() {
            public void run() {
                //Let cake 1 fall
               System.out.println("tesssst");
                handlerFallingObjects.postDelayed(this, 5);
                //Set speed
                cake1y -=20;
                if (cake1y<0){
                    //Set interval
                    cake1y = screenHeight+20;
                    cake1x = (int) Math.floor(Math.random()*(frameWidth - cake1.getWidth()));
                }
                cake1.setX(cake1x);
                cake1.setY(cake1y);
            }
        };
        handlerFallingObjects.postDelayed(runnableFallingObjects, 5);

    }

    //Display en run timer
    //Source: http://en.proft.me/2017/11/18/how-create-count-timer-android/
    //Adjust to Millisecond timer with help of https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
    public static String getDateFromMillis(long d) {
        SimpleDateFormat df = new SimpleDateFormat("mm:ss:SSS");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(d);
    }

    //Delay = 1 instead of 1000 to change to milliseconds
    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            timer.setText(getDateFromMillis(timeInMilliseconds));
            customHandler.postDelayed(this, 1);
        }
    };

    //VOOR TIJD STOP GEBRUIKEN   customHandler.removeCallbacks(updateTimerThread);


}
