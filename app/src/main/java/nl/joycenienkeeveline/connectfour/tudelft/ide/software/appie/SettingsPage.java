package nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.opengl.Visibility;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class SettingsPage extends Activity {

    //Initialise Buttons for sound settings
    private Button soundON;
    private Button soundOFF;

    //Initialse UI elements
    private ImageView soundONImage;
    private ImageView soundOFFImage;
    private ImageView middle1;
    private ImageView middle2;
    private ImageView right;
    private ImageView advancedtext;
    private ImageView intermediatetext;
    private ImageView beginnertext;
    private ImageView experttext;

    //Initialise values for sound that can be stored with SharedPreferences
    private int soundValue = 1;
    private int dataStoreSound;

    //Initialise check for level difficulty
    private int advancedValue=1;
    private int dataStoreAdvanced;

    //When back button device is pressed return to menu
    @Override
    public void onBackPressed(){
        super.onBackPressed();

        Intent intent = new Intent(this,
                MainMenu.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Remove title bar & notification bar for a nicer look
        Source: https://stackoverflow.com/questions/2591036/how-to-hide-the-title-bar-for-an-activity-in-xml-with-existing-custom-theme*/
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_settings_page);

        /*Store settings internal via SharedPreferences
        Source:https://stackoverflow.com/questions/23024831/android-shared-preferences-example
        Source:https://developer.android.com/training/data-storage/shared-preferences.html#ReadSharedPreference
        Source:http://www.vogella.com/tutorials/AndroidFileBasedPersistence/article.html
        Source:https://developer.android.com/reference/android/app/Activity.html#SavingPersistentState
        */
        SharedPreferences preferences = getSharedPreferences("data_settings",MODE_PRIVATE);
        //Get last value for sound
        dataStoreSound = preferences.getInt("sound", soundValue);
        //When no value for sound, use default value
        if(dataStoreSound==1||dataStoreSound==2){soundValue=dataStoreSound;}
        //Get last value for level difficulty
        dataStoreAdvanced = preferences.getInt("advanced", advancedValue);
        //When no value for difficulty, use default value
        if(dataStoreAdvanced==1||dataStoreAdvanced==2||dataStoreAdvanced==3||dataStoreAdvanced==4)
        {advancedValue=dataStoreAdvanced;}

        //Find buttons for UI
        soundOFF = (Button)findViewById(R.id.sound_off);
        soundON = (Button)findViewById(R.id.sound_on);

        //Find images for UI
        soundOFFImage = (ImageView)findViewById(R.id.soundoff);
        soundONImage = (ImageView)findViewById(R.id.soundon);
        middle1 = (ImageView)findViewById(R.id.middleselectedlevel1);
        middle2 = (ImageView)findViewById(R.id.middleselectedlevel2);
        right = (ImageView)findViewById(R.id.rightselectedlevel);
        advancedtext = (ImageView)findViewById(R.id.advancedtext);
        beginnertext = (ImageView)findViewById(R.id.beginnertext);
        intermediatetext = (ImageView)findViewById(R.id.intermediatetext);
        experttext= (ImageView)findViewById(R.id.experttext);

        //Make sure UI elements have the right visibility based on shared preferences
        if (soundValue==1){visibilityImagesSoundON();}
        else{visibilityImagesSoundOFF();}

        if (dataStoreAdvanced==1){visibilityLeft();}
        if (dataStoreAdvanced==2){visibilityMiddle1();}
        if (dataStoreAdvanced==3){visibilityMiddle2();}
        if (dataStoreAdvanced==4){visibilityRight();}
    }

    public void visibilityLeft(){
        middle1.setVisibility(View.INVISIBLE);
        middle2.setVisibility(View.INVISIBLE);
        right.setVisibility(View.INVISIBLE);
        beginnertext.setVisibility(View.VISIBLE);
        intermediatetext.setVisibility(View.INVISIBLE);
        advancedtext.setVisibility(View.INVISIBLE);
        experttext.setVisibility(View.INVISIBLE);
    }

    public void visibilityMiddle1(){
        middle1.setVisibility(View.VISIBLE);
        middle2.setVisibility(View.INVISIBLE);
        right.setVisibility(View.INVISIBLE);
        beginnertext.setVisibility(View.INVISIBLE);
        intermediatetext.setVisibility(View.VISIBLE);
        advancedtext.setVisibility(View.INVISIBLE);
        experttext.setVisibility(View.INVISIBLE);
    }

    public void visibilityMiddle2(){
        middle1.setVisibility(View.VISIBLE);
        middle2.setVisibility(View.VISIBLE);
        right.setVisibility(View.INVISIBLE);
        beginnertext.setVisibility(View.INVISIBLE);
        intermediatetext.setVisibility(View.INVISIBLE);
        advancedtext.setVisibility(View.VISIBLE);
        experttext.setVisibility(View.INVISIBLE);
    }

    public void visibilityRight(){
        middle1.setVisibility(View.VISIBLE);
        middle2.setVisibility(View.VISIBLE);
        right.setVisibility(View.VISIBLE);
        beginnertext.setVisibility(View.INVISIBLE);
        intermediatetext.setVisibility(View.INVISIBLE);
        advancedtext.setVisibility(View.INVISIBLE);
        experttext.setVisibility(View.VISIBLE);
    }

    public void visibilityImagesSoundON(){
        soundONImage.setVisibility(View.VISIBLE);
        soundOFFImage.setVisibility(View.INVISIBLE);
    }

    public void visibilityImagesSoundOFF(){
        soundOFFImage.setVisibility(View.VISIBLE);
        soundONImage.setVisibility(View.INVISIBLE);
    }

    //Respond on click by changing visibility images
    public void soundON(View view) {
        soundValue = 1;
        visibilityImagesSoundON();

        /*DELETE
        //Get color from resources
        //SOURCE: https://stackoverflow.com/questions/5271387/get-color-int-from-color-resource
        soundON.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
        soundOFF.setBackgroundColor(getResources().getColor(R.color.colorPrimary));*/
    }

    public void soundOFF(View view) {
        soundValue = 2;
        visibilityImagesSoundOFF();
    }

    public void advanced1(View view) {
        advancedValue = 1;
        visibilityLeft();
    }

    public void advanced2(View view) {
        advancedValue = 2;
        visibilityMiddle1();
    }

    public void advanced3(View view) {
        advancedValue = 3;
        visibilityMiddle2();
    }

    public void advanced4(View view) {
        advancedValue = 4;
        visibilityRight();
        System.out.println("CHECK");
    }

    protected void onPause(){
        super.onPause();

        //Store values internal when SettingsPage is left
        //Use apply instead of commit as commit should be prevented to be called from main thread
        //Source: https://developer.android.com/training/data-storage/shared-preferences.html#ReadSharedPreference
        SharedPreferences.Editor ed = getSharedPreferences("data_settings",MODE_PRIVATE).edit();
        ed.putInt("sound", soundValue);
        ed.putInt("advanced", advancedValue);
        ed.apply();
    }
}
