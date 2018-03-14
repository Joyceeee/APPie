package nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie;

/*DOEN:
Fizen fade timer?
Fizen gif
FIXEN ctrl F FIX
Fixen rolling soepeler?
sara 6
UITLEG OVERAL BIJ
delete system.out.println

Bij splash nog laadding weg bovenin

VRAAG ADRIE:
Maakt het uit als de code gigantisch lang is (moeten we onderverdelen
in aparte classfiles) of maakt het niet uit?)
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
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
//import pl.droidsonroids.gif.GifImageView;

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
    //private GifImageView gif_countdown;

    //Initialising size screen
    public int screenWidth;
    public int screenHeight;

    //Position falling objects:
    private int cake1x;
    private float cake1y;
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

    //Making game go faster with speedFactor:
    private int speedFactor = 1000;

    //Making rolling player possible
    //Source: Workshop Four in a row Software
    private SensorControlListener sensorControlListener=new SensorControlListener();
    private OnControlListenerImpl onControlListenerImpl=new OnControlListenerImpl(this);
    public boolean motion=true;
    public boolean getMotion() { return motion; }

    //Making rolling possible with accelerometer
    //Source: Workshop Four in a Row Software
    @Override
    protected void onResume() {
        super.onResume();
        SensorManager sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorControlListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }
    protected void onPause() {
        super.onPause();
        SensorManager sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensorManager.unregisterListener(sensorControlListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Remove title bar & notification bar for a nicer look
        Source: https://stackoverflow.com/questions/2591036/how-to-hide-the-title-bar-for-an-activity-in-xml-with-existing-custom-theme*/
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //Making rolling possible with accelerometer
        //Source: Workshop Four in a Row Software
        sensorControlListener.setOnControlListener(onControlListenerImpl);

        //Finding elements GUI
        //Source: https://coding-with-sara.thinkific.com/courses/take/catch-the-ball-android-studio-game-tutorial/lessons/1510621-3-findviewbyid-ontouchevent
        timer = (TextView)findViewById(R.id.timer);
        bitmoji1 = (ImageView)findViewById(R.id.bitmoji1);
        cake1 = (ImageView)findViewById(R.id.cake1);
        cake2 = (ImageView)findViewById(R.id.cake2);
        umbrella = (ImageView)findViewById(R.id.umbrella);
        sponge = (ImageView)findViewById(R.id.sponge);
        //gif_countdown = (GifImageView) findViewById(R.id.gif_countdown);

        //Get screen size
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        //Move falling objects out of screen --> FIXEN WANT NOG NIET OP MOBIEL GOED
        cake1.setX(screenWidth);
        cake1.setY(screenHeight);
        cake2.setX(screenWidth);
        cake2.setY(screenHeight);
        sponge.setX(screenWidth);
        sponge.setY(screenHeight);
        umbrella.setX(screenWidth);
        umbrella.setY(screenHeight);

        //Temporary DELETE
        boxX = 500;

        //Stop countdown, start game
        waitFunctionStart();

        //Make sure the different falling objects not come into the game all at the same time
        waitFunctionCake2();
        waitFunctionUmbrella();
        waitFunctionSponge();

    }

    //A delay function for the GIF in the start
    public void waitFunctionStart (){
        //Make fade in animation
        //Source:https://stackoverflow.com/questions/2597329/how-to-do-a-fadein-of-an-image-on-an-android-activity-screen
        final Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);

        //Waiting for countdown to end:
        //Source: https://developer.android.com/reference/android/os/CountDownTimer.html
        new CountDownTimer(5000, 1000) {
            //DELETE: Alleen om te checken
            public void onTick(long millisUntilFinished) {
                check = millisUntilFinished / 1000;
                System.out.println(check);
            }

            public void onFinish() {
                //DELETE/FIX

                //Starting falling objects
                changePosCake1();

                //Making Gif Countdown disappear
                //gif_countdown.setVisibility(View.INVISIBLE);

                //Run timer
                //Source: http://en.proft.me/2017/11/18/how-create-count-timer-android/
                startTime = SystemClock.uptimeMillis();
                customHandler.postDelayed(updateTimerThread, 0);

                //Fade in time in background
                //Source: https://stackoverflow.com/questions/2597329/how-to-do-a-fadein-of-an-image-on-an-android-activity-screen
                timer.startAnimation(myFadeInAnimation);
                timer.setVisibility(View.VISIBLE);

            }
        }.start();
    }

    //A delay function for when cake2 is introduced in the game
    public void waitFunctionCake2 (){
        //Waiting for countdown to end:
        //Source: https://developer.android.com/reference/android/os/CountDownTimer.html
        new CountDownTimer(9500, 1000) {
            //DELETE: Alleen om te checken
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                //Starting falling objects
                changePosCake2();
            }
        }.start();
    }

    //A delay function for when the umbrella is introduced in the game
    public void waitFunctionUmbrella (){
        //Waiting for countdown to end:
        //Source: https://developer.android.com/reference/android/os/CountDownTimer.html
        new CountDownTimer(17730, 1000) {
            //DELETE: Alleen om te checken
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                //Starting falling objects
                changePosUmbrella();
            }
        }.start();
    }

    //A delay function for when the sponge is introduced in the game
    public void waitFunctionSponge (){
        //Waiting for countdown to end:
        //Source: https://developer.android.com/reference/android/os/CountDownTimer.html
        new CountDownTimer(21450, 1000) {
            //DELETE: Alleen om te checken
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                //Starting falling objects
                changePosSponge();
            }
        }.start();
    }



    //Make objects fall down
    //Source: https://coding-with-sara.thinkific.com/courses/take/catch-the-ball-android-studio-game-tutorial/lessons/1510625-5-set-the-balls
    public void changePosCake1(){

        //Creating new runnable for falling objects
        //Source: https://stackoverflow.com/questions/1921514/how-to-run-a-runnable-thread-in-android-at-defined-intervals
        final Handler handlerFallingObjects = new Handler();

        final Runnable runnableFallingObjects = new Runnable() {
            public void run() {
                handlerFallingObjects.postDelayed(this, 5);
                //Let cake 1 fall
                //Set speed
                //Including speedfactor to increase speed falling objects
                cake1y +=(12*speedFactor)/1000;
                if (cake1y>screenHeight-150){
                    //Set interval
                    cake1y = -screenHeight+1700;
                    cake1x = (int) Math.floor(Math.random()*(screenWidth - cake1.getWidth()));
                }
                cake1.setX(cake1x);
                cake1.setY(cake1y);

                //Increasing speedfactor so that falling goes faster
                speedFactor+=1;
            }
        };
        handlerFallingObjects.postDelayed(runnableFallingObjects, 5);

    }

    public void changePosCake2(){

        //Creating new runnable for falling objects
        //Source: https://stackoverflow.com/questions/1921514/how-to-run-a-runnable-thread-in-android-at-defined-intervals
        final Handler handlerFallingObjects2 = new Handler();

        final Runnable runnableFallingObjects2 = new Runnable() {
            public void run() {
                handlerFallingObjects2.postDelayed(this, 5);
                //Let cake 2 fall
                //Set speed
                cake2y +=(8*speedFactor)/1000;
                if (cake2y>screenHeight-150){
                    //Set interval
                    cake2y = -screenHeight+600;
                    cake2x = (int) Math.floor(Math.random()*(screenWidth - cake2.getWidth()));
                }
                cake2.setX(cake2x);
                cake2.setY(cake2y);
            }
        };
        handlerFallingObjects2.postDelayed(runnableFallingObjects2, 5);
    }

    public void changePosUmbrella(){

        //Creating new runnable for falling objects
        //Source: https://stackoverflow.com/questions/1921514/how-to-run-a-runnable-thread-in-android-at-defined-intervals
        final Handler handlerFallingObjects = new Handler();

        final Runnable runnableFallingObjects = new Runnable() {
            public void run() {
                handlerFallingObjects.postDelayed(this, 5);
                //Let cake 1 fall
                //Set speed
                umbrellay +=(16*speedFactor)/1000;
                if (umbrellay>screenHeight-150){
                    //Set interval
                    umbrellay = -screenHeight-550;
                    umbrellax = (int) Math.floor(Math.random()*(screenWidth - umbrella.getWidth()));
                }
                umbrella.setX(umbrellax);
                umbrella.setY(umbrellay);
            }
        };
        handlerFallingObjects.postDelayed(runnableFallingObjects, 5);

    }

    public void changePosSponge(){

        //Creating new runnable for falling objects
        //Source: https://stackoverflow.com/questions/1921514/how-to-run-a-runnable-thread-in-android-at-defined-intervals
        final Handler handlerFallingObjects = new Handler();

        final Runnable runnableFallingObjects = new Runnable() {
            public void run() {
                handlerFallingObjects.postDelayed(this, 5);
                //Let sponge fall
                //Set speed
                spongey +=(18*speedFactor)/1000;
                if (spongey>screenHeight-150){
                    //Set interval
                    spongey = -screenHeight;
                    spongex = (int) Math.floor(Math.random()*(screenWidth - sponge.getWidth()));
                }
                sponge.setX(spongex);
                sponge.setY(spongey);
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
