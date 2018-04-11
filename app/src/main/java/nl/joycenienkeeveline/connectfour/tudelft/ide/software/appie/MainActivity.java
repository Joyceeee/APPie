package nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.os.Vibrator;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.view.*;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

//Creating nice score bar
//Source: https://www.youtube.com/watch?v=7d-IWhjxXIA
import me.itangqi.waveloadingview.WaveLoadingView;

//Make nice pause/playbutton possible
//Source: See SparkButton MainMenu
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

//Extend Activity instead of AppCompatActivity to remove title bar
public class MainActivity extends Activity {

    //Initialising the GUI elements
    //Source: https://coding-with-sara.thinkific.com/courses/take/catch-the-ball-android-studio-game-tutorial/lessons/1510621-3-findviewbyid-ontouchevent
    private TextView timer;
    private ImageView bitmoji1;
    private ImageView bitmoji2;
    private ImageView bitmoji3;
    private ImageView bitmoji4;
    private ImageView umbrellacovering;
    private ImageView vlek1;
    private ImageView vlek2;
    private ImageView vlekplu1;
    private ImageView vlekplu2;
    private ImageView cake1;
    private ImageView cake2;
    private ImageView umbrella;
    private ImageView sponge;
    private ImageView photoGallery;
    private ImageView photoGalleryBitmoji;
    private ImageView covergame;
    private ImageView photocoverman;
    private ImageView photocoverwoman;
    private ImageView framebuttonpicture;
    private ImageView confirm;
    private ImageView redo;
    private Button boundary;
    private Button confirmpicture;
    private Button retakepicture;
    private Button pauseplay;
    private SparkButton playpauseButton;

    //Initialise UI elements countdown
    //private GifImageView gifImageViewGame;
    private ImageView one;
    private ImageView two;
    private ImageView three;
    private ImageView start;

    //Initialise elements to be able to import photos from gallery
    Uri uri;
    Intent GalIntent, CropIntent ;

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
    public boolean motion=false;
    public boolean getMotion() { return motion; }

    //Sounds when collision
    private SoundPlayer sound;
    private int soundValue = 1;
    private int dataStoreSound;

    //Getting right bitmoji
    private int selectedframe;
    private int valueBitmojiSelected;
    private int bitmojiToImplement;
    private int bitmojiToImplementOwnPhoto;

    //Getting level difficulty
    private int increasementDifficulty;
    private int advancedValue=1;
    private int dataStoreAdvanced;

    //Initialise check if onPause occurred or not
    private boolean onBackButtonDevice=false;
    private int checkGalleryOpen;

    //Initialise check if Pause option occurred or not
    private int pauseOption;

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
            Intent intent = new Intent(this, MainMenu.class);
            startActivity(intent);
            finish();
        }
    }

    protected void onPause() {
        super.onPause();
        SensorManager sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensorManager.unregisterListener(sensorControlListener);

        if (checkGalleryOpen==0){
            //Make runnables stop when game is left and return to the menu page
            score = 0;
            endOfGame();

            //Give a sign that onPause occurred
            onBackButtonDevice=true;}
    }

    //When back button device is pressed return to menu
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
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
        bitmoji2 = (ImageView)findViewById(R.id.bitmoji2);
        bitmoji3 = (ImageView)findViewById(R.id.bitmoji3);
        bitmoji4 = (ImageView)findViewById(R.id.bitmoji4);
        umbrellacovering = (ImageView)findViewById(R.id.bitmojiumbrella1);
        vlek1 = (ImageView)findViewById(R.id.vlek1);
        vlek2 = (ImageView)findViewById(R.id.vlek2);
        vlekplu1 = (ImageView)findViewById(R.id.vlekplu1);
        vlekplu2 = (ImageView)findViewById(R.id.vlekplu2);
        cake1 = (ImageView)findViewById(R.id.cake1);
        cake2 = (ImageView)findViewById(R.id.cake2);
        umbrella = (ImageView)findViewById(R.id.umbrella);
        sponge = (ImageView)findViewById(R.id.sponge);
        pauseplay = (Button)findViewById(R.id.pauseplay);
        one = (ImageView)findViewById(R.id.een);
        two = (ImageView)findViewById(R.id.twee);
        three = (ImageView)findViewById(R.id.drie);
        start = (ImageView)findViewById(R.id.start);
        covergame = (ImageView)findViewById(R.id.covergame);
        photocoverman = (ImageView)findViewById(R.id.photocoverman);
        photocoverwoman = (ImageView)findViewById(R.id.photocoverwoman);
        framebuttonpicture = (ImageView)findViewById(R.id.btn_framebuttonpicture);
        confirm = (ImageView)findViewById(R.id.confirm);
        redo = (ImageView)findViewById(R.id.redo);
        boundary = (Button)findViewById(R.id.btn_boundary);
        confirmpicture = (Button)findViewById(R.id.btn_confirmpicture);
        retakepicture= (Button)findViewById(R.id.btn_retakepicture);
        playpauseButton = (SparkButton) findViewById(R.id.pausetoplaybutton);
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

        //Check which bitmoji to implement and make the right one visible
        SharedPreferences preferences2 = getSharedPreferences("bitmoji_settings",MODE_PRIVATE);
        SharedPreferences preferences3 = getSharedPreferences("bitmoji_frame",MODE_PRIVATE);
        bitmojiToImplement = preferences2.getInt("bitmoji", valueBitmojiSelected);
        bitmojiToImplementOwnPhoto = preferences3.getInt("frame",selectedframe);
        if(bitmojiToImplement==0){bitmoji1.setVisibility(View.VISIBLE);}
        if(bitmojiToImplement==1){bitmoji2.setVisibility(View.VISIBLE);}

        //Check which level of difficulty is wanted
        //Get last value for level difficulty
        dataStoreAdvanced = preferences.getInt("advanced", advancedValue);
        //When no value for difficulty, use default value
        if(dataStoreAdvanced==1||dataStoreAdvanced==2||dataStoreAdvanced==3||dataStoreAdvanced==4)
        {advancedValue=dataStoreAdvanced;}

        //Adjust decreasement score based on time with level of difficulty
        if(advancedValue==1){increasementDifficulty=0;}
        if(advancedValue==2){increasementDifficulty=5;}
        if(advancedValue==3){increasementDifficulty=10;}
        if(advancedValue==4){increasementDifficulty=15;}

        //Move falling objects out of screen
        cake1.setX(screenWidth);
        cake1.setY(screenHeight);
        cake2.setX(screenWidth);
        cake2.setY(screenHeight);
        sponge.setX(screenWidth);
        sponge.setY(screenHeight);
        umbrella.setX(screenWidth);
        umbrella.setY(screenHeight);

        //Make continue game possible after pause
        //Source: https://www.programcreek.com/java-api-examples/?code=varunest/SparkButton/SparkButton-master/sparkbutton/src/main/java/com/varunest/sparkbutton/SparkEventListener.java#
        //Source: https://github.com/varunest/SparkButton
        playpauseButton.setEventListener(new SparkEventListener(){
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {
                //Continue the runnables and the sensor control
                motion = true;
                handlerFallingObjects4.postDelayed(runnableFallingObjects4, 5);
                handlerFallingObjects3.postDelayed(runnableFallingObjects3, 5);
                handlerFallingObjects2.postDelayed(runnableFallingObjects2, 5);
                handlerFallingObjects.postDelayed(runnableFallingObjects, 5);
                customHandler.postDelayed(updateTimerThread, 0);
                handlerScoreTimerDown.postDelayed(runnableScoreTimer, 1);

                //If umbrella is visible continue waitfunction umbrella
                if(umbrellacovering.getVisibility()==View.VISIBLE){
                    waitFunction(3000, 6);}

                //Make pause button invisible again and pauseoverlay visible again
                playpauseButton.setVisibility(View.INVISIBLE);
                pauseplay.setVisibility(View.VISIBLE);

                //Set Sparkbutton unchecked again
                //https://github.com/varunest/SparkButton
                playpauseButton.setChecked(false);

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {
            }
        });

        //When chosen for own picture, pick image from gallery and crop to right dimensions
        if(bitmojiToImplement==2){
            //Make other UI elements visible
            covergame.setVisibility(View.VISIBLE);
            framebuttonpicture.setVisibility(View.VISIBLE);
            confirm.setVisibility(View.VISIBLE);
            redo.setVisibility(View.VISIBLE);
            boundary.setVisibility(View.VISIBLE);
            confirmpicture.setVisibility(View.VISIBLE);
            retakepicture.setVisibility(View.VISIBLE);

            //Make sure right frame is selected
            photocoverman.setVisibility(View.VISIBLE);photocoverwoman.setVisibility(View.VISIBLE);
            if(bitmojiToImplementOwnPhoto==0){photocoverman.setVisibility(View.INVISIBLE);}
            else{photocoverwoman.setVisibility(View.INVISIBLE);}

            //Start photo activity
            photoGallery = (ImageView) findViewById(R.id.photogallery);
            photoGalleryBitmoji = (ImageView) findViewById(R.id.photogallerybitmoji);
            checkGalleryOpen=1;
            GetImageFromGallery();
        }

        //Else start game by making Gif appear
        else{startGameWithGIF();}
    }

    //########################################################################
    //Import photo from external storage and crop it to the right dimensions
    //Source: https://www.android-examples.com/android-image-cropping-example-tutorial-pick-gallery-camera/
    //Source: https://www.youtube.com/watch?v=rYzkv_KuZo4
    public void GetImageFromGallery(){

        GalIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), 2);

    }

    //React upon intent activities
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            ImageCropFunction();
        }

        else if (requestCode == 2) {
            if (data != null) {
                uri = data.getData();
                ImageCropFunction();
            }
        }
        else if (requestCode == 1) {

            if (data != null) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("data");
                photoGallery.setImageBitmap(bitmap);
                photoGalleryBitmoji.setImageBitmap(bitmap);
            }
        }
    }

    //Crop image to wanted dimensions
    public void ImageCropFunction() {
        // Image Crop Code
        try {
            CropIntent = new Intent("com.android.camera.action.CROP");
            CropIntent.setDataAndType(uri, "image/*");
            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 170);
            CropIntent.putExtra("outputY", 230);
            CropIntent.putExtra("aspectX", 3);
            CropIntent.putExtra("aspectY", 4);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);
            startActivityForResult(CropIntent, 1);
        } catch (ActivityNotFoundException e) {
        }
    }
    //Stop camera activity
    //##############################################################

    public void startGameWithGIF(){
        //Make rollin' from bitmoji possible
        motion=true;
        checkGalleryOpen=0;
        //Show number 3
        three.setVisibility(View.VISIBLE);
        //Show number 2
        waitFunction(1000,7);
        //Show number 1
        waitFunction(2000,8);
        //Show start
        waitFunction(3000,9);

        //Stop countdown, start game
        waitFunction(4000,5);

        //Make sure the different falling objects not come into the game all at the same time
        //waitFunctionCake2();
        waitFunction(8500,2);
        waitFunction(16730,3);
        waitFunction(24450,4);
    }

    //A delay function introduced in the game
    public void waitFunction (int timeToWait, final int functionCall){
        //Make fade in animation for timer in background
        //Source:https://stackoverflow.com/questions/2597329/how-to-do-a-fadein-of-an-image-on-an-android-activity-screen
        final Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);

        //Waiting for countdown to end:
        //Source: https://developer.android.com/reference/android/os/CountDownTimer.html
        new CountDownTimer(timeToWait, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                if(score>0) {
                    //Starting falling objects if now pause has occurred yet
                    if (functionCall == 2 && pauseOption != 1) {
                        changePosCake2();
                    } else if (functionCall == 3 && pauseOption != 1) {
                        changePosSponge();
                    } else if (functionCall == 4 && pauseOption != 1) {
                        changePosUmbrella();
                    } else if (functionCall == 5 && pauseOption != 1) {
                        //Starting falling objects
                        changePosCake1();

                        //Start score going down with time
                        scoreTimer();

                        //Making Gif disappear
                        //gifImageViewGame.setVisibility(View.INVISIBLE);

                        //Making countdown disappear
                        start.setVisibility(View.INVISIBLE);

                        //Check if bitmoji frame must be implemented
                        if(bitmojiToImplement==2&&bitmojiToImplement==0){bitmoji3.setVisibility(View.VISIBLE);}
                        if(bitmojiToImplement==2&&bitmojiToImplement==1){bitmoji4.setVisibility(View.VISIBLE);}

                        //Make score bar visible
                        waveLoadingView.setVisibility(View.VISIBLE);

                        //Making Pause option possible
                        pauseplay.setClickable(true);

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
                        //Let the umbrella over the bitmoji with its smudges disappear again when there is no pause
                        if(pauseplay.getVisibility()== View.VISIBLE) {
                            umbrellacovering.setVisibility(View.INVISIBLE);
                            vlekplu1.setVisibility(View.INVISIBLE);
                            vlekplu2.setVisibility(View.INVISIBLE);
                            //Let the falling objects influence the score again
                            umbrellaFactor = 1;}
                    }

                    else if (functionCall == 7 && pauseOption != 1) {
                        three.setVisibility(View.INVISIBLE);
                        two.setVisibility(View.VISIBLE);}

                    else if (functionCall == 8 && pauseOption != 1) {
                        two.setVisibility(View.INVISIBLE);
                        one.setVisibility(View.VISIBLE);}

                    else if (functionCall == 9 && pauseOption != 1) {
                        one.setVisibility(View.INVISIBLE);
                        start.setVisibility(View.VISIBLE);}
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
            //Stop runnables when game over
            endOfGame();
            //Make device vibrate when game over as feedback
            vibrate();
            //Go to the GameOver page
            goGameOver();
        }

        //Make score bar change
        scoreVisual();
    }   

    public void goGameOver(){
       // waitFunction(2500,10);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //MainActivity.this.startActivity(new Intent(MainActivity.this, GameOver.class));
                //MainActivity.this.finish();
                if(onBackButtonDevice == false){
                    Intent intent = new Intent();
                    intent.putExtra("ScoreAtGameOver", timer.getText().toString());
                    intent.setClass(MainActivity.this, GameOver.class);
                    startActivity(intent);}
            }
        },2500);
    }

    public void scoreVisual(){
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
            score =1000;}

        //Make score not become smaller than minimum score
        if (score<=0){
            score =0;}

        //Change the score bar to the right value
        waveLoadingView.setProgressValue(score/10);
    }

    //Make color score bar change based on score
    //Source: https://www.youtube.com/watch?v=bSgUn2rZiko
    private void startColorAnimation(WaveLoadingView waveLoadingView,int startColor,int endColor){
        ValueAnimator colorAnim = ObjectAnimator.ofInt(waveLoadingView,"WaveColor",startColor,endColor);
        colorAnim.setDuration(300);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(1);
        colorAnim.start();
    }

    //Make device vibrate for given time period
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
            Score(30);
            //Play sound when collision
            if(soundValue==1){sound.playCakeSound();}
            //Cause smudges
            smudgesCausedByCakes();
        }

        if(viewsOverlap(cake2,bitmoji1)){
            cake2y=screenHeight;
            Score(20);
            if(soundValue==1){sound.playCakeSound();}
            //Cause smudges
            smudgesCausedByCakes();
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
            //Make umbrella and bitmoji clean from smudges
            smudgesCleanedBySponge();
        }
    }

    public void smudgesCleanedBySponge(){
        //Only make bitmoji clean when umbrella is not visible
        if(umbrellacovering.getVisibility()==View.VISIBLE){
        vlekplu1.setVisibility(View.INVISIBLE);
        vlekplu2.setVisibility(View.INVISIBLE);}
        //Else make bitmoji clean
        else{vlek1.setVisibility(View.INVISIBLE);
            vlek2.setVisibility(View.INVISIBLE);}
    }

    public void smudgesCausedByCakes(){
        //Give umbrella stains if visible
        if(umbrellacovering.getVisibility()==View.VISIBLE){
            if (vlekplu1.getVisibility()==View.VISIBLE)
            {vlekplu2.setVisibility(View.VISIBLE);
                vlekplu1.setVisibility(View.INVISIBLE);}
            else{vlekplu1.setVisibility(View.VISIBLE);
                vlekplu2.setVisibility(View.INVISIBLE);}
        }
        //Else give bitmoji smudges each time it hits a cake
        else{
            if (vlek1.getVisibility()==View.VISIBLE)
            {vlek2.setVisibility(View.VISIBLE);
                vlek1.setVisibility(View.INVISIBLE);}
            else{vlek1.setVisibility(View.VISIBLE);
                vlek2.setVisibility(View.INVISIBLE);}
        }
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
                cake1y = -screenHeight+800;
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
                //Go to the end of the game
                endOfGame();
                //Make device vibrate when game over as feedback
                vibrate();
                //Go to the GameOver page
                goGameOver();

            }
            else{
                score-=(20+increasementDifficulty);
            }
            scoreVisual();
        }
    };

    public void scoreTimer(){
        handlerScoreTimerDown.postDelayed(runnableScoreTimer,0);
    }

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


    //Make game pause when screen is touched
    public void pauseplay(View view) {
        //Prevent pause when game is over
        if(score <= 0){}
        else {
            if (pauseplay.getVisibility()==View.VISIBLE){
                //Make pauseplay disappear to make the Sparkbutton clickable
                pauseplay.setVisibility(View.INVISIBLE);

                //Let system know pause occurred
                pauseOption = 1;
                endOfGame();

                //Make pausebutton appear
                playpauseButton.setVisibility(View.VISIBLE);
            }
    }

}

    public void retake(View view) {
        GetImageFromGallery();
    }

    public void confirm(View view) {
        //Set UI elements for check photo to invisible
        covergame.setVisibility(View.INVISIBLE);
        photocoverman.setVisibility(View.INVISIBLE);
        photocoverwoman.setVisibility(View.INVISIBLE);
        framebuttonpicture.setVisibility(View.INVISIBLE);
        confirm.setVisibility(View.INVISIBLE);
        redo.setVisibility(View.INVISIBLE);
        boundary.setVisibility(View.INVISIBLE);
        confirmpicture.setVisibility(View.INVISIBLE);
        retakepicture.setVisibility(View.INVISIBLE);
        photoGallery.setVisibility(View.INVISIBLE);

        //Make the photo visible behind the bitmoji to move along with it
        photoGalleryBitmoji.setVisibility(View.VISIBLE);

        //Make right bitmoji visible
        if(bitmojiToImplementOwnPhoto==0){bitmoji3.setVisibility(View.VISIBLE);}
        else{bitmoji4.setVisibility(View.VISIBLE);}

        //Start game
        startGameWithGIF();
    }
}

