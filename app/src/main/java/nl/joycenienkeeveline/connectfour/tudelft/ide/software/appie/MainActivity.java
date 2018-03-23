package nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie;

/*DOEN:
ERGENS AANGEVEN OF HIER OF IN VERSLAG WAT ER ALLEMAAL INSTELBAAR IS:
COLLISION BEREIK
SNELHEID OBJECTS
INTRO OBJECTS
GROOTTE OBJECTS
TIJDVERSNELLING
PUNTEN OPTEL/AFTREK
ERGENS VERMELDEN DAT SCHERM NIET UITGAAT TIJDENS GAME


FIXEN ctrl F FIX
Fixen gif
Fizen afmetingen collision detection

Pauze?
Stop runnables when other page
Code in klassen

Methogs in klassen opdelen

sara 6+
UITLEG OVERAL BIJ
delete system.out.println
//SCORE DISPLAY __> RENZEN 1000 en 0 opstellen
*/

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.Image;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.os.Vibrator;
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

//Creating nice score bar
//Source: https://www.youtube.com/watch?v=7d-IWhjxXIA
import me.itangqi.waveloadingview.WaveLoadingView;

//Extend Activity instead of AppCompatActivity to remove title bar
public class MainActivity extends Activity {

    //Initialising the GUI elements
    //Source: https://coding-with-sara.thinkific.com/courses/take/catch-the-ball-android-studio-game-tutorial/lessons/1510621-3-findviewbyid-ontouchevent
    private TextView timer;
    //DELETE private TextView scoreLabel;
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
    //Make speedfactor larger to decrease the acceleration
    private int speedFactor = 3000;

    //Initialising score and elements needed for visuals
    private int score = 1000;
    private int umbrellaFactor = 1;
    WaveLoadingView waveLoadingView;
    int updateScore=0;
    // COLORFIX int colorStart;

    //Set colors score bar
    int colorScore5 = 0xFFFF4081;
    int colorScore4 = 0xFFC83064;
    int colorScore3 = 0xFF9F2A52;
    int colorScore2 = 0xFF6E1C38;
    int colorScore1 = 0xFF370E1C;

    //Initialise Handlers
    Handler handlerFallingObjects = new Handler();
    Handler handlerFallingObjects2 = new Handler();
    Handler handlerFallingObjects3 = new Handler();
    Handler handlerFallingObjects4 = new Handler();
    Handler handlerScoreTimerDown = new Handler();

    //Making rolling player possible
    //Source: Workshop Four in a row Software
    private SensorControlListener sensorControlListener=new SensorControlListener();
    private OnControlListenerImpl onControlListenerImpl=new OnControlListenerImpl(this);
    public boolean motion=true;
    public boolean getMotion() { return motion; }

    //Sounds when collision
    private SoundPlayer sound;
    private int soundValue = 1;
    private int dataStoreSound;

    //Initialise check if onPause occurred or not
    private boolean onBackButtonDevice=false;

    //Making rolling possible with accelerometer
    //Source: Workshop Four in a Row Software
    @Override
    protected void onResume() {
        super.onResume();
        SensorManager sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorControlListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);

        //If onPause occurred, go to Menu page when returning
        if(onBackButtonDevice == true){
            Intent intent = new Intent(this,
                    MainMenu.class);
            startActivity(intent);
        }
    }

    protected void onPause() {
        super.onPause();
        SensorManager sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensorManager.unregisterListener(sensorControlListener);

        //Make runnables stop when game is left and return to the menu page
        score = 0;
        endOfGame();

        //Give a sign that onPause occurred
        onBackButtonDevice=true;
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

        //Making sounds possible
        sound = new SoundPlayer(this);

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
        //DELETE scoreLabel = (TextView)findViewById(R.id.scoreLabel);
        waveLoadingView = (WaveLoadingView)findViewById(R.id.waveLoadingView);
        waveLoadingView.setProgressValue(100);

        //Get screen size
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        //Check if sounds are wanted or not
        SharedPreferences preferences = getSharedPreferences("data_settings",MODE_PRIVATE);
        //Get last value for sound
        dataStoreSound = preferences.getInt("sound", soundValue);
        //When no value for sound, use default value
        if(dataStoreSound==1||dataStoreSound==2){soundValue=dataStoreSound;}

        //Move falling objects out of screen --> FIXEN WANT NOG NIET OP MOBIEL GOED
        cake1.setX(screenWidth);
        cake1.setY(screenHeight);
        cake2.setX(screenWidth);
        cake2.setY(screenHeight);
        sponge.setX(screenWidth);
        sponge.setY(screenHeight);
        umbrella.setX(screenWidth);
        umbrella.setY(screenHeight);

        //Receive color of Score Bar
        //COLORFIX colorStart = waveLoadingView.getWaveColor();

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
        waitFunction(5000,5);
        //waitFunctionStart();

        //Make sure the different falling objects not come into the game all at the same time
        //waitFunctionCake2();
        waitFunction(9500,2);
        waitFunction(17730,3);
        waitFunction(25450,4);
    }

    //A delay function introduced in the game
    public void waitFunction (int timeToWait, final int functionCall){
        //Make fade in animation for timer in background
        //Source:https://stackoverflow.com/questions/2597329/how-to-do-a-fadein-of-an-image-on-an-android-activity-screen
        final Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);

        //Waiting for countdown to end:
        //Source: https://developer.android.com/reference/android/os/CountDownTimer.html
        new CountDownTimer(timeToWait, 1000) {
            //DELETE: Alleen om te checken
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                if(score>0) {
                    //Starting falling objects
                    if (functionCall == 2) {
                        changePosCake2();
                    } else if (functionCall == 3) {
                        changePosSponge();
                    } else if (functionCall == 4) {
                        changePosUmbrella();
                    } else if (functionCall == 5) {
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

                    //When the umbrella is caught, no falling object influences the score for 10 seconds
                    else if (functionCall == 6) {
                        //Let the umbrella over the bitmoji disappear again
                        umbrellacovering.setVisibility(View.INVISIBLE);
                        //Let the falling objects influence the score again
                        umbrellaFactor = 1;
                    }
                }
            }
        }.start();
    }

    //Calculating and displaying new score
    public void Score(int points){
        if (score>0){
            score += points*umbrellaFactor;
        }

        //Check if one is game over or not
        else {
            //FIX --> Hier verwijzing einde game
            endOfGame();
            //Make device vibrate when game over as feedback
            vibrate();
        }

        //DELETE scoreLabel.setText(String.valueOf(score));
        //Make score bar change
        scoreVisual();
        //HIER FIX VISUAL APPEARANCE
    }


    public void scoreVisual(){
       /*COLORFIX
        //Check if color of Score Bar needs to change
        if(updateScore==score){}
        else{
            System.out.print("CHECK SCORE VOOR VISUAL        ");
            System.out.println(updateScore);
            startColorAnimation(waveLoadingView);}
        updateScore=score;
        */
       //Update color score bar if color does not fit to score range
        int currentColor = waveLoadingView.getWaveColor();
        if (score>=600){
            if(currentColor==colorScore5){}
            else{startColorAnimation(waveLoadingView,currentColor,colorScore5);}}
        else if (score>=300){
            if(currentColor==colorScore4){}
            else{startColorAnimation(waveLoadingView,currentColor,colorScore4);}}
        else if (score>=100){
            if(currentColor==colorScore3){}
            else{startColorAnimation(waveLoadingView,currentColor,colorScore3);}}
        else if (score>=30){
            if(currentColor==colorScore2){}
            else{startColorAnimation(waveLoadingView,currentColor,colorScore2);}}
        else if (score>=-500){
            if(currentColor==colorScore1){}
            else{startColorAnimation(waveLoadingView,currentColor,colorScore1);}}

        //Make score not become larger than maximum score
        if (score>1000){
            score =1000;
        }
        //Make score not become smaller than minimum score
        if (score<=0){
            score =0;
        }
        System.out.print("CHECK SCORE VOOR VISUAL        ");
        System.out.println(score);

        //Change the score bar to the right value
        waveLoadingView.setProgressValue(score/10);

    }

    /*
    COLORFIX
    //Make color score bar change shortly when score changes as feedback
    //Source: https://www.youtube.com/watch?v=bSgUn2rZiko
    private void startColorAnimation(WaveLoadingView waveLoadingView){
        int colorEnd = 0xFFCB3367;

        ValueAnimator colorAnim = ObjectAnimator.ofInt(waveLoadingView,"WaveColor",colorStart,colorEnd);

        colorAnim.setDuration(300);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(1);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();
    }*/

    //Make color score bar change based on score
    //Source: https://www.youtube.com/watch?v=bSgUn2rZiko
    private void startColorAnimation(WaveLoadingView waveLoadingView,int startColor,int endColor){
        ValueAnimator colorAnim = ObjectAnimator.ofInt(waveLoadingView,"WaveColor",startColor,endColor);

        colorAnim.setDuration(300);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(1);
        colorAnim.start();
    }

    //Make device vibratie for given time period
    //Source: https://stackoverflow.com/questions/13950338/how-to-make-an-android-device-vibrate
    public void vibrate(){
        // Get instance of Vibrator from current Context
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Vibrate for 400 milliseconds
        v.vibrate(400);
    }

    //Act upon collisions
    public void hitCheck(){
        if(viewsOverlap(cake1,bitmoji1)){
            cake1y=screenHeight;
            Score(20);
            //Play sound when collision
            if(soundValue==1){sound.playCakeSound();}
        }

        if(viewsOverlap(cake2,bitmoji1)){
            cake2y=screenHeight;
            Score(30);
            if(soundValue==1){sound.playCakeSound();}
        }

        if(viewsOverlap(umbrella,bitmoji1)){
            umbrellay=screenHeight;
            if(soundValue==1){sound.playUmbrellaSound();}
            if(umbrellacovering.getVisibility()==View.INVISIBLE) {
                //Let the umbrella cover the bitmoji
                umbrellacovering.setVisibility(View.VISIBLE);
                //When the umbrella is caught, no falling object influences the score for 6 seconds
                waitFunction(6000, 6);
                umbrellaFactor = 0;
            }
        }

        if(viewsOverlap(sponge,bitmoji1)){
            spongey=screenHeight;
            Score(-150);
            if(soundValue==1){sound.playSpongeSound();}
            System.out.println("GERAAKT Spons");
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
           v1_rect = new Rect(v1_coords[0]+(v1_w/10), v1_coords[1]+(v1_h/10), v1_coords[0] + v1_w-(v1_w/10), v1_coords[1] +(v1_h/2)-(v1_h/10));
           //Rectangle handle
           v4_rect = new Rect(v1_coords[0]+(v1_w/10)+(v1_w/3), v1_coords[1]+(v1_h/10)+(v1_h/2), v1_coords[0] + v1_w-(v1_w/10)-(v1_w/3), v1_coords[1] + v1_h);
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
        Rect v2_rect = new Rect(v2_coords[0], v2_coords[1]+(v2_h/7*6), v2_coords[0] + v2_w, v2_coords[1] + v2_h);
        //Rectangle head
        Rect v3_rect = new Rect(v2_coords[0]+(v2_w/3), v2_coords[1], v2_coords[0] + v2_w-(v2_w/3), v2_coords[1] + v2_h-(v2_h/7));

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

       //Check whether the umbrella covering is active or not to see if extra collision detection is needed
       else if (umbrellacovering.getVisibility()==View.VISIBLE){
           return (viewsOverlapUmbrella(v1));
       }
       else return false;
    }

    //Check for collision with umbrella and falling objects
    private boolean viewsOverlapUmbrella(View v1) {
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
            v1_rect = new Rect(v1_coords[0]+(v1_w/10), v1_coords[1]+(v1_h/10), v1_coords[0] + v1_w-(v1_w/10), v1_coords[1] +(v1_h/2)-(v1_h/10));
            //Rectangle handle
            v4_rect = new Rect(v1_coords[0]+(v1_w/10)+(v1_w/3), v1_coords[1]+(v1_h/10)+(v1_h/2), v1_coords[0] + v1_w-(v1_w/10)-(v1_w/3), v1_coords[1] + v1_h);
        }
        else{
            v1_rect = new Rect(v1_coords[0]+(v1_w/10), v1_coords[1]+(v1_h/10), v1_coords[0] + v1_w-(v1_w/10), v1_coords[1] + v1_h-(v1_h/10));
            v4_rect = new Rect(0, 0, 1, 1);
        }

        //Determine position of image v2 and set a rectangle around it
        int[] v2_coords = new int[2];
        umbrellacovering.getLocationOnScreen(v2_coords);
        int v2_w = umbrellacovering.getWidth();
        int v2_h = umbrellacovering.getHeight();
        //Separating the umbrella into multiple rectangles as done already above
        //Rectangle sheet
        Rect v2_rect = new Rect(v2_coords[0]+(v2_w/10), v2_coords[1]+(v2_h/10), v2_coords[0] + v2_w-(v2_w/10), v2_coords[1] + v2_h-(v2_h/10)+(v2_h/2));
        //Rectangle handle
        Rect v3_rect = new Rect(v2_coords[0]+(v2_w/10)+(v2_w/3), v2_coords[1]+(v2_h/10)+(v2_h/2), v2_coords[0] + v2_w-(v2_w/10)-(v2_w/3), v2_coords[1] + v2_h);

        //Check whether collision
        if ((v1_rect.intersect(v2_rect) || v1_rect.contains(v2_rect) || v2_rect.contains(v1_rect))
                ||
                (v1_rect.intersect(v3_rect) || v1_rect.contains(v3_rect) || v3_rect.contains(v1_rect))
                ||
                (v4_rect.intersect(v2_rect) || v4_rect.contains(v2_rect) || v2_rect.contains(v4_rect))
                ||
                (v4_rect.intersect(v3_rect) || v4_rect.contains(v3_rect) || v3_rect.contains(v4_rect))
                ){
            System.out.println("GERAAKT Umbrella");
            return  true;
        }
        else return false;
    }


    //Make objects fall down by starting a runnable per object
    //Source: https://coding-with-sara.thinkific.com/courses/take/catch-the-ball-android-studio-game-tutorial/lessons/1510625-5-set-the-balls
    public Runnable runnableFallingObjects = new Runnable() {
        public void run() {
            handlerFallingObjects.postDelayed(this, 5);
            hitCheck();
            //Let cake 1 fall
            //Set speed
            //Including speedfactor to increase speed falling objects
            cake1y +=(10*speedFactor)/3000;
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

    public void changePosCake1(){
        handlerFallingObjects.postDelayed(runnableFallingObjects,5);
    }

    //Creating new runnable for falling objects
    //Source: https://stackoverflow.com/questions/1921514/how-to-run-a-runnable-thread-in-android-at-defined-intervals
    public Runnable runnableFallingObjects2 = new Runnable() {
        public void run() {
            handlerFallingObjects2.postDelayed(this, 5);
            //Let cake 2 fall
            //Set speed
            cake2y +=(9*speedFactor)/3000;
            if (cake2y>screenHeight-150){
                //Set interval
                cake2y = -screenHeight+600;
                cake2x = (int) Math.floor(Math.random()*(screenWidth - cake2.getWidth()));
            }
            cake2.setX(cake2x);
            cake2.setY(cake2y);
        }
    };

    public void changePosCake2(){
        handlerFallingObjects2.postDelayed(runnableFallingObjects2,5);
    }

    //Creating new runnable for falling objects
    //Source: https://stackoverflow.com/questions/1921514/how-to-run-a-runnable-thread-in-android-at-defined-intervals
    private Runnable runnableFallingObjects3 = new Runnable() {
        public void run() {
            handlerFallingObjects3.postDelayed(this, 5);
            //Let cake 1 fall
            //Set speed
            umbrellay +=(12*speedFactor)/3000;
            if (umbrellay>screenHeight-150){
                //Set interval
                umbrellay = -screenHeight-550;
                umbrellax = (int) Math.floor(Math.random()*(screenWidth - umbrella.getWidth()));
            }
            umbrella.setX(umbrellax);
            umbrella.setY(umbrellay);
        }
    };

    public void changePosUmbrella(){
        handlerFallingObjects3.postDelayed(runnableFallingObjects3,5);
    }

    //Creating new runnable for falling objects
    //Source: https://stackoverflow.com/questions/1921514/how-to-run-a-runnable-thread-in-android-at-defined-intervals
    public Runnable runnableFallingObjects4 = new Runnable() {
        public void run() {
            handlerFallingObjects4.postDelayed(this, 5);
            //Let sponge fall
            //Set speed
            spongey +=(13*speedFactor)/3000;
            if (spongey>screenHeight-150){
                //Set interval
                spongey = -screenHeight;
                spongex = (int) Math.floor(Math.random()*(screenWidth - sponge.getWidth()));
            }
            sponge.setX(spongex);
            sponge.setY(spongey);
        }
    };

    public void changePosSponge(){
        handlerFallingObjects4.postDelayed(runnableFallingObjects4,5);
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

    //Creating new runnable to let score decrease over time
    private Runnable runnableScoreTimer = new Runnable() {
        public void run() {
            handlerScoreTimerDown.postDelayed(this, 1000);
            if (score<=0){
                // NAAR END OF GAME
                endOfGame();
                //Make device vibrate when game over as feedback
                vibrate();

            }
            else{
                score-=20;
            }
            scoreVisual();
        }
    };

    public void scoreTimer(){
        handlerScoreTimerDown.postDelayed(runnableScoreTimer,0);
    }

    /*
    //Make runnables stop when back button device is pressed
    //Let the game thereupon return to the Menu
    //Source: https://stackoverflow.com/questions/5312334/how-to-handle-back-button-in-activity
    public void onBackPressed() {
        score=0;
        endOfGame();
        Intent intent = new Intent(this,
                MainMenu.class);
        startActivity(intent);
    }*/

    //Stop all runnables when game is over to prevent overload
    //Source: https://stackoverflow.com/questions/18671067/how-to-stop-handler-runnable
    public void endOfGame(){
        //Stop objects from falling
        handlerFallingObjects4.removeCallbacks(runnableFallingObjects4);
        handlerFallingObjects3.removeCallbacks(runnableFallingObjects3);
        handlerFallingObjects2.removeCallbacks(runnableFallingObjects2);
        handlerFallingObjects.removeCallbacks(runnableFallingObjects);

        //Stop timer and score from dropping
        customHandler.removeCallbacks(updateTimerThread);
        handlerScoreTimerDown.removeCallbacks(runnableScoreTimer);

        //Stop motion of bitmoji
        motion = false;
        }
}
