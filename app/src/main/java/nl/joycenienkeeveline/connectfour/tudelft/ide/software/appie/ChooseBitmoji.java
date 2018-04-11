package nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

//Import Carousel function
//Source:https://www.youtube.com/watch?v=sTJm1Ys9jMI
//https://stackoverflow.com/questions/7766630/changing-viewpager-to-enable-infinite-page-scrolling
//https://github.com/Devlight/InfiniteCycleViewPager
import com.gigamole.infinitecycleviewpager.VerticalInfiniteCycleViewPager;
import java.util.ArrayList;
import java.util.List;

public class ChooseBitmoji extends Activity {

    //Make Carousel possible by creating list and initialising viewer
    List<Integer> lstImages = new ArrayList<>();
    private VerticalInfiniteCycleViewPager pager;
    private MyAdapter adapter;

    //Initialise check if onPause occurred or not
    private boolean onBackButtonDevice=false;

    //Initialise values for selected Bitmoji that can be stored with SharedPreferences
    private int valueBitmojiSelected;

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

        //Store values internal when ChooseBitmojiPage is left
        //Use apply instead of commit as commit should be prevented to be called from main thread
        //Source: https://developer.android.com/training/data-storage/shared-preferences.html#ReadSharedPreference
        SharedPreferences.Editor ed = getSharedPreferences("bitmoji_settings",MODE_PRIVATE).edit();
        ed.putInt("bitmoji", valueBitmojiSelected);
        ed.apply();

        //Give a sign that onPause occurred
        onBackButtonDevice=true;
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

        setContentView(R.layout.activity_choose_bitmoji);

        //Include images in the Carousel
        initData();

        //Include the Carousel UI element
        pager = (VerticalInfiniteCycleViewPager)findViewById(R.id.vertical_cycle);
        adapter = new MyAdapter(lstImages,getBaseContext());
        pager.setAdapter(adapter);
    }

    //Set which images must be implemented in Carousel
    private void initData() {
        lstImages.add(R.drawable.bitmojiman);
        lstImages.add(R.drawable.bitmojiwoman);
        lstImages.add(R.drawable.bitmojiempty);
    }

    //Go to the game or photo option when the START button is pressed
    public void clickStart(View view) {
        if (pager.getRealItem()==0){valueBitmojiSelected=0;
            ChooseBitmoji.this.startActivity(new Intent(ChooseBitmoji.this, MainActivity.class));
            ChooseBitmoji.this.finish();}
        if (pager.getRealItem()==1){valueBitmojiSelected=1;
            ChooseBitmoji.this.startActivity(new Intent(ChooseBitmoji.this, MainActivity.class));
            ChooseBitmoji.this.finish();}
        if (pager.getRealItem()==2){valueBitmojiSelected=2;
            ChooseBitmoji.this.startActivity(new Intent(ChooseBitmoji.this, ChoiceFrameActivity.class));
            ChooseBitmoji.this.finish();}
    }
}
