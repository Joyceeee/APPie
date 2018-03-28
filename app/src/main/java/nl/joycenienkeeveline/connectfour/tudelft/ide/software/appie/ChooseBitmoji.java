package nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

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
            Intent intent = new Intent(this,
                    MainMenu.class);
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

        setContentView(R.layout.activity_choose_bitmoji);

        //Include images in the Carousel
        initData();

        //Include the Carousel UI element
        pager = (VerticalInfiniteCycleViewPager)findViewById(R.id.vertical_cycle);
        adapter = new MyAdapter(lstImages,getBaseContext());
        pager.setAdapter(adapter);

/*
        //Wait a couple of second before opening game DELETE
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkIfOnPauseOccurred();
                getdata();
            }
        },5000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkIfOnPauseOccurred();
                getdata();
            }
        },10000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkIfOnPauseOccurred();
                getdata();
            }
        },15000);*/

        //React upon image change
        //https://stackoverflow.com/questions/7766630/changing-viewpager-to-enable-infinite-page-scrolling
        //https://github.com/Devlight/InfiniteCycleViewPager
        /*DELETE
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            //When image has new position, pass this info to the onClickListener of "START"
            @Override
            public void onPageScrollStateChanged(int state) {
                System.out.println("state");
               // if (pager.getRealItem()==0){initData0();}
                //if (pager.getRealItem()==1){initData1();}
                //if (pager.getRealItem()==2){initData2();}
            }
        });*/
    }

    //Set which images must be implemented in Carousel
    private void initData() {
        lstImages.add(R.drawable.bitmojiman);
        lstImages.add(R.drawable.bitmojiwoman);
        lstImages.add(R.drawable.bitmojiempty);
    }

    //Check if back button is pressed, if not open the game DELETE
    public void checkIfOnPauseOccurred(){
        //When no onPause occurred, go to next page
        //if(onBackButtonDevice==false){
          //  ChooseBitmoji.this.startActivity(new Intent(ChooseBitmoji.this, MainActivity.class));
            //ChooseBitmoji.this.finish();
        //}
    }

    public void clickStart(View view) {
        if (pager.getRealItem()==0){valueBitmojiSelected=0;}
        if (pager.getRealItem()==1){valueBitmojiSelected=1;}
        if (pager.getRealItem()==2){valueBitmojiSelected=2;}

        ChooseBitmoji.this.startActivity(new Intent(ChooseBitmoji.this, MainActivity.class));
        ChooseBitmoji.this.finish();
    }
}
