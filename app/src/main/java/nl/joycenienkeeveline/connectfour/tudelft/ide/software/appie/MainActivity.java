package nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie;

/*DOEN:
FIXEN ctrl F FIX
Fixen gif
Fizen afmetingen collision detection

Pauze?
Stop runnables when other page
"Algemene methods" --> Zie ook fix

Methogs in klassen opdelen

sara 6
UITLEG OVERAL BIJ
delete system.out.println
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
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

import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.net.ssl.SSLContext;

import nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie.R;
//import pl.droidsonroids.gif.GifImageView;

//Extend Activity instead of AppCompatActivity to remove title bar
public class MainActivity extends Activity {

    //Initialising the GUI elements
    //Source: https://coding-with-sara.thinkific.com/courses/take/catch-the-ball-android-studio-game-tutorial/lessons/1510621-3-findviewbyid-ontouchevent
    private TextView timer;
    private ImageView bitmoji1;
    private ImageView umbrellacovering;
    private ImageView cake1;
    private ImageView cake2;
    private ImageView umbrella;
    private ImageView sponge;
    private GifImageView gifImageViewGame;

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

    //Timer Check -_DELETE
    private long check;

    //Timer
    //Source: http://en.proft.me/2017/11/18/how-create-count-timer-android/
    long startTime, timeInMilliseconds = 0;
    Handler customHandler = new Handler();

    //Making game go faster with speedFactor:
    private int speedFactor = 1000;

    //Initialising score
    private int score = 1000;

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
        umbrellacovering = (ImageView)findViewById(R.id.bitmojiumbrella1);
        cake1 = (ImageView)findViewById(R.id.cake1);
        cake2 = (ImageView)findViewById(R.id.cake2);
        umbrella = (ImageView)findViewById(R.id.umbrella);
        sponge = (ImageView)findViewById(R.id.sponge);
        gifImageViewGame =(GifImageView)findViewById(R.id.gifImageViewGame);

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

        //Making Gif appear
        try{
            InputStream inputStream = getAssets().open("countdown.gif");
            byte [] bytes = IOUtils.toByteArray(inputStream);
            gifImageViewGame.setBytes(bytes);
            gifImageViewGame.startAnimation();
        }
        catch (IOException ex)
        {
        }

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
                //Starting falling objects
                changePosCake1();

                //Start score going down with time
                scoreTimer();

                //Making Gif disappear
                gifImageViewGame.setVisibility(View.INVISIBLE);

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

    /*
    FIXEN ALGEMENE DELAY METHOD
    //A delay function for when cake2 is introduced in the game
    public void waitFunction (int timeToWait, final Object startChangingPosition){
        //Waiting for countdown to end:
        //Source: https://developer.android.com/reference/android/os/CountDownTimer.html
        new CountDownTimer(timeToWait, 1000) {
            //DELETE: Alleen om te checken
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                //Starting falling objects
               startChangingPosition;
            }
        }.start();
    }*/


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

    //Calculating and displaying new score
    public void Score(int points){
        //Make score not become larger than maximum score
        if (score>1000){
            score =1000;
        }

        else if (score>=0){
            score += points;
        }

        //Check if one is game over or not
        else {
            //FIX --> Hier verwijzing einde game
            score=0;
        }

        //HIER FIX VISUAL APPEARANCE
    }


    //Act upon collisions
    public void hitCheck(){
        //if umbrellabitmoji inactive
        if(viewsOverlap(cake1,bitmoji1)){
            cake1y=screenHeight;
            Score(10);
        }

        if(viewsOverlap(cake2,bitmoji1)){
            cake2y=screenHeight;
            Score(20);
        }

        if(viewsOverlap(umbrella,bitmoji1)){
            umbrellay=screenHeight;
        }

        if(viewsOverlap(sponge,bitmoji1)){
            spongey=screenHeight;
            Score(-100);
        }

        System.out.print("scoreCatchingCakes");
        System.out.println(score);

    }

    //Check for collision
    //https://stackoverflow.com/questions/24600378/how-to-detect-when-a-imageview-is-in-collision-with-another-imageview
    //https://laaptu.wordpress.com/2013/12/12/android-view-basics-coordinatesmarginpaddingdippx/
   private boolean viewsOverlap(View v1, View v2) {
       //Determine position of image v1 and set a rectangle around it
       int[] v1_coords = new int[2];
       v1.getLocationOnScreen(v1_coords);
       int v1_w = v1.getWidth();
       int v1_h = v1.getHeight();
       Rect v1_rect;
       Rect v4_rect;

       //Check whether picture is umbrella (needs 2 rectangles because of dimensions)
       if(v1==umbrella){
           //Rectangle sheet
           v1_rect = new Rect(v1_coords[0]+(v1_w/10), v1_coords[1]+(v1_h/10)+(v1_h/5), v1_coords[0] + v1_w-(v1_w/10), v1_coords[1] + v1_h-(v1_h/10));
           //Rectangle handle
           v4_rect = new Rect(v1_coords[0]+(v1_w/10)+(v1_w/3), v1_coords[1]+(v1_h/10), v1_coords[0] + v1_w-(v1_w/10)-(v1_w/3), v1_coords[1] + v1_h-(v1_h/10)-(v1_h/5*4));
       }
       else{
           v1_rect = new Rect(v1_coords[0]+(v1_w/10), v1_coords[1]+(v1_h/10), v1_coords[0] + v1_w-(v1_w/10), v1_coords[1] + v1_h-(v1_h/10));
           v4_rect = new Rect(0, 0, 1, 1);
       }

       //Determine position of image v2 and set a rectangle around it
        int[] v2_coords = new int[2];
        v2.getLocationOnScreen(v2_coords);
        int v2_w = v2.getWidth();
        int v2_h = v2.getHeight();
       //Separating the bitmoji into multiple rectangles for the head and the body by adjusting the boundaries
        //Rectangle body
        Rect v2_rect = new Rect(v2_coords[0], v2_coords[1]+(v2_h/2), v2_coords[0] + v2_w, v2_coords[1] + v2_h);
        //Rectangle head
        Rect v3_rect = new Rect(v2_coords[0]+(v2_w/3), v2_coords[1], v2_coords[0] + v2_w-(v2_w/3), v2_coords[1] + v2_h-(v2_h/2));

        //Check whether collision
        if ((v1_rect.intersect(v2_rect) || v1_rect.contains(v2_rect) || v2_rect.contains(v1_rect))
                ||
                (v1_rect.intersect(v3_rect) || v1_rect.contains(v3_rect) || v3_rect.contains(v1_rect))
                ||
                (v4_rect.intersect(v2_rect) || v4_rect.contains(v2_rect) || v2_rect.contains(v4_rect))
                ||
                (v4_rect.intersect(v3_rect) || v4_rect.contains(v3_rect) || v3_rect.contains(v4_rect))
                ){
            System.out.println("GERAAKT Body");
            return  true;
        }
        else return false;
    }

    //Make objects fall down
    //Source: https://coding-with-sara.thinkific.com/courses/take/catch-the-ball-android-studio-game-tutorial/lessons/1510625-5-set-the-balls
    public void changePosCake1(){

        //Creating new runnable for falling objects
        //Source: https://stackoverflow.com/questions/1921514/how-to-run-a-runnable-thread-in-android-at-defined-intervals
        final Handler handlerFallingObjects = new Handler();

        final Runnable runnableFallingObjects = new Runnable() {
            public void run() {
                hitCheck();
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

    /*
    //FIX ALGEMENE FUNCTIE
    public void changePosObject(int objecty, int objectx, final View image){

        hitCheck();

        objectx;

        //Creating new runnable for falling objects
        //Source: https://stackoverflow.com/questions/1921514/how-to-run-a-runnable-thread-in-android-at-defined-intervals
        final Handler handlerFallingObjects = new Handler();

        final Runnable runnableFallingObjects = new Runnable() {
            public void run() {
                handlerFallingObjects.postDelayed(this, 5);
                //Let sponge fall
                //Set speed
                objecty +=(18*speedFactor)/1000;
                if (objecty>screenHeight-150){
                    //Set interval
                    objecty = -screenHeight;
                    objectx = (int) Math.floor(Math.random()*(screenWidth - image.getWidth()));
                }
                image.setX(objectx);
                image.setY(spongey);
            }
        };
        handlerFallingObjects.postDelayed(runnableFallingObjects, 5);

    }*/


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

    //Creating new runnable to let score decrease over time
    public void scoreTimer(){
        final Handler handlerScoreTimerDown = new Handler();

        final Runnable runnableScoreTimer = new Runnable() {
            public void run() {
                handlerScoreTimerDown.postDelayed(this, 1000);
                if (score<=0){
                    // NAAR END OF GAME
                }
                else{
                    score-=10;
                }
            }
        };
        handlerScoreTimerDown.postDelayed(runnableScoreTimer, 5);
    }
}
