package nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class HowToPlayPage extends Activity {

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

        setContentView(R.layout.activity_how_to_play_page);

    }

    //Go to next layout when forward button is pressed
    public void howtoNextPage(View view) { setContentView(R.layout.activity_how_to_play_page2); }
    public void howtoNextPage2(View view) {setContentView(R.layout.activity_how_to_play_page_pause);}
    public void howtoNextPageBit(View view) {setContentView(R.layout.activity_how_to_play_page3);}
    public void howtoNextPagePause(View view) { setContentView(R.layout.activity_how_to_play_page_bitmoji); }
    public void howtoNextPage3(View view) {setContentView(R.layout.activity_how_to_play_page4);}
    public void howtoNextPage4(View view) {setContentView(R.layout.activity_how_to_play_page5);}
    public void howtoNextPage5(View view) {setContentView(R.layout.activity_how_to_play_page6);}
    public void howtoNextPage6(View view) {setContentView(R.layout.activity_how_to_play_page7);}
    public void howtoNextPage7(View view) {
        HowToPlayPage.this.startActivity(new Intent(HowToPlayPage.this, MainMenu.class));
        HowToPlayPage.this.finish();}

    //Go to previous layout when back button is pressed
    public void howtoPreviousPage(View view) {
        HowToPlayPage.this.startActivity(new Intent(HowToPlayPage.this, MainMenu.class));
        HowToPlayPage.this.finish();}
    public void howtoPreviousPage2(View view) {setContentView(R.layout.activity_how_to_play_page);}
    public void howtoPreviousBit(View view) {setContentView(R.layout.activity_how_to_play_page_pause);}
    public void howtoPreviousPause(View view) {setContentView(R.layout.activity_how_to_play_page2);}
    public void howtoPreviousPage3(View view) {setContentView(R.layout.activity_how_to_play_page_bitmoji);}
    public void howtoPreviousPage4(View view) {setContentView(R.layout.activity_how_to_play_page3);}
    public void howtoPreviousPage5(View view) {setContentView(R.layout.activity_how_to_play_page4);}
    public void howtoPreviousPage6(View view) {setContentView(R.layout.activity_how_to_play_page5);}
    public void howtoPreviousPage7(View view) {setContentView(R.layout.activity_how_to_play_page6);
    }

}

