package nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class SettingsPage extends Activity {

    //Initialise Buttons for sound settings
    private Button soundON;
    private Button soundOFF;

    //Initialise values for sound that can be stored with SharedPreferences
    private int soundValue = 1;
    private int dataStoreSound;

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

        //Find buttons for UI
        soundOFF = (Button)findViewById(R.id.sound_off);
        soundON = (Button)findViewById(R.id.sound_on);

        //Make sure UI elements have the right color
        if (soundValue==1){
            soundON.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
            soundOFF.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        else{soundON.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            soundOFF.setBackgroundColor(getResources().getColor(R.color.colorSecondary));}
    }

    //Respond on click by changing colors buttons
    public void soundON(View view) {
        soundValue = 1;
        //Get color from recources
        //SOURCE: https://stackoverflow.com/questions/5271387/get-color-int-from-color-resource
        soundON.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
        soundOFF.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    }

    public void soundOFF(View view) {
        soundValue = 2;
        soundON.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        soundOFF.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
    }

    protected void onPause(){
        super.onPause();

        //Store values internal when SettingsPage is left
        //Use apply instead of commit as commit should be prevented to be called from main thread
        //Source: https://developer.android.com/training/data-storage/shared-preferences.html#ReadSharedPreference
        SharedPreferences.Editor ed = getSharedPreferences("data_settings",MODE_PRIVATE).edit();
        ed.putInt("sound", soundValue);
        ed.apply();
    }
}
