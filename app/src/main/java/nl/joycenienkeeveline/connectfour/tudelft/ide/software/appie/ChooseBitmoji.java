package nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class ChooseBitmoji extends Activity {

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
        }
    }
    protected void onPause(){
        super.onPause();

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

        setContentView(R.layout.activity_instruction_page_before_play);

        //Wait a couple of second before opening game
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkIfOnPauseOccurred();
            }
        },5000);
    }

    //Check if back button is pressed, if not open the game
    public void checkIfOnPauseOccurred(){
        //When no onPause occured, go to next page
        if(onBackButtonDevice==false){
            ChooseBitmoji.this.startActivity(new Intent(ChooseBitmoji.this, InstructionPageBeforePlay.class));
            ChooseBitmoji.this.finish();
        }
    }

}
