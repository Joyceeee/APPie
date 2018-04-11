package nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class ChoiceFrameActivity extends Activity {

    //Initalise UI elements
    private ImageView woman;
    private ImageView man;

    //Initialise check if onPause occurred or not
    private boolean onBackButtonDevice=false;

    //Initialise values for selected Frame that can be stored with SharedPreferences
    public int selectedframe=1;

    @Override
    protected void onResume(){
        super.onResume();

        //If onPause occurred, go to Menu page when returning
        if(onBackButtonDevice == true){
            Intent intent = new Intent(this, MainMenu.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();

        //Store values internal when ChoiceFrameActivity is left
        //Use apply instead of commit as commit should be prevented to be called from main thread
        //Source: https://developer.android.com/training/data-storage/shared-preferences.html#ReadSharedPreference
        SharedPreferences.Editor ed = getSharedPreferences("bitmoji_frame",MODE_PRIVATE).edit();
        ed.putInt("frame", selectedframe);
        ed.apply();

        //Give a sign that onPause occurred
        onBackButtonDevice=true;
    }

    //When back button device is pressed return to menu
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Remove title bar & notification bar for a nicer look
        Source: https://stackoverflow.com/questions/2591036/how-to-hide-the-title-bar-for-an-activity-in-xml-with-existing-custom-theme*/
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_choice_frame);

        //Find UI elements with id
        woman = (ImageView)findViewById(R.id.woman_frame);
        man = (ImageView)findViewById(R.id.man_frame);
    }

    //If go is pressed, start the game
    public void clickStartGame(View view) {
        ChoiceFrameActivity.this.startActivity(new Intent(ChoiceFrameActivity.this, MainActivity.class));
        ChoiceFrameActivity.this.finish();
    }

    //Make clear that picture is selected by turning the other picture to a 'vague' state
    //Pas the selected state to the system by setting int to selectedframe
    public void womanSelected(View view) {
        selectedframe = 0;
        woman.setVisibility(View.VISIBLE);
        man.setVisibility(View.INVISIBLE);
    }

    public void manSelected(View view) {
        selectedframe = 1;
        woman.setVisibility(View.INVISIBLE);
        man.setVisibility(View.VISIBLE);
    }
}
