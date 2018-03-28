package nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by evelinepientje on 26-03-18.
 */

public class GameOver extends Activity {

    //Initialise check if onPause occurred or not
    private boolean onBackButtonDevice=false;

    @Override
    protected void onResume(){
        super.onResume();

        //If onPause occurred, go to Menu page when returning
        if(onBackButtonDevice == true){
            Intent intent = new Intent(this,
                    MainMenu.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();

        //Give a sign that onPause occurred
        onBackButtonDevice=true;
    }

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

        setContentView(R.layout.activity_game_over);
    }

    public void playagain(View view) {
        GameOver.this.startActivity(new Intent(GameOver.this, MainActivity.class));
        GameOver.this.finish();
    }

    public void gotoHome(View view) {
        GameOver.this.startActivity(new Intent(GameOver.this, MainMenu.class));
        GameOver.this.finish();
    }
}

