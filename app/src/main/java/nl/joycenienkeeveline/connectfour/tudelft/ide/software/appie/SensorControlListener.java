//Making a listener for rolling the bitmoji
//Source: Workshop Four in a row Software

package nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by mdehoogh on 22/02/2018.
 */

public class SensorControlListener implements SensorEventListener {

    /** OnShakeListener that is called when shake is detected. */
    private OnControlListener mControlListener=null;

    /**
     * Interface for shake gesture.
     */
    public interface OnControlListener {
        /** Called when shake gesture is detected.*/
        void onRoll(float x); // rotation around X axis
    }

    public void setOnControlListener(OnControlListener listener) {
        mControlListener = listener;
        if (mControlListener!=null)
            Log.i("SensorControlListener","On control listener registered!");
        else
            Log.e("SensorControlListener","No on control listener registered!");
    }

    @Override
    public void onSensorChanged(SensorEvent se) {
        // Get sensor data
        float x = se.values[SensorManager.DATA_X];

        // Determine whether to be rolling
        // Sensitivity
        if (x<-1 || x>1) mControlListener.onRoll(x);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor,int accuracy) {
    }

}
