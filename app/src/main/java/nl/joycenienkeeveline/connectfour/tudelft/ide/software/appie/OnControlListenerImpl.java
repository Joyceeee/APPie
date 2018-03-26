//Making a listener for rolling the bitmoji
//Source: Workshop Four in a row Software
package nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie;

//KIJKEN NAAR COMMENTS EN SYSTEMOUT

import android.widget.ImageView;

/**
 * Created by mdehoogh on 22/02/2018.
 */

public class OnControlListenerImpl implements SensorControlListener.OnControlListener {

    //Set how fast accelerometer must respond:
    private static final int ROLLING_SPEED=20;
    private float sensorX;
    private long time=0;
    private boolean moving=false;
    private int screenHeight;

    private MainActivity mainActivity;
    public OnControlListenerImpl(MainActivity mainActivity) {
        this.mainActivity=mainActivity;
    }

    @Override
    public void onRoll(float x){

        if (mainActivity.getMotion()) {
            System.out.println("Rollin' babeee jaaaa");
            sensorX=sensorX-x*ROLLING_SPEED;
            ImageView bitmoji1=(ImageView)mainActivity.findViewById(R.id.bitmoji1);
            ImageView umbrellacovering=(ImageView)mainActivity.findViewById(R.id.bitmojiumbrella1);
            ImageView vlek1=(ImageView)mainActivity.findViewById(R.id.vlek1);
            ImageView vlek2=(ImageView)mainActivity.findViewById(R.id.vlek2);
            ImageView vlekplu1=(ImageView)mainActivity.findViewById(R.id.vlekplu1);
            ImageView vlekplu2=(ImageView)mainActivity.findViewById(R.id.vlekplu2);

            //Set boundaries field in which bitmoji must move
            if (sensorX<(-bitmoji1.getWidth()/2)) sensorX=-bitmoji1.getWidth()/2;
            if (sensorX+bitmoji1.getWidth()>mainActivity.screenWidth+(bitmoji1.getWidth()/2))
                sensorX=mainActivity.screenWidth-(bitmoji1.getWidth()/2);

            screenHeight = mainActivity.screenHeight;
            //Set new position bitmoji and cake smudges
            bitmoji1.setX(sensorX);
            vlek1.setX(sensorX);
            vlek2.setX(sensorX);

            //Make sure umbrella moves along on the right place, just like the cake smudges
            umbrellacovering.setX(sensorX+bitmoji1.getWidth()/5);
            vlekplu1.setX(sensorX+bitmoji1.getWidth()/5);
            vlekplu2.setX(sensorX+bitmoji1.getWidth()/5);

            time=System.currentTimeMillis(); // reset the time
            moving=true;
        }
    }

}
