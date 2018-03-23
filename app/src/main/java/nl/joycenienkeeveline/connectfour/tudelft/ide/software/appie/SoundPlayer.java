package nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import static android.media.AudioManager.STREAM_MUSIC;

/**
 * Created by joyce on 22-3-2018.
 */

/*Play sound when bitmoji hits objects
Source: https://stackoverflow.com/questions/30889497/why-does-android-studio-not-recognize-wav-mp3-files
Source: https://coding-with-sara.thinkific.com/courses/take/catch-the-ball-android-studio-game-tutorial/lessons/1510640-9-add-sound-effects
 */
public class SoundPlayer {

    private AudioAttributes audioAttributes;
    final int SOUND_POOL_MAX = 2;

    private static SoundPool soundPool;
    private static int cakeSound;
    private static int umbrellaSound;
    private static int spongeSound;

    public SoundPlayer(Context context){

        //Soundpool is deprecated in API level 21. (Lollipop)
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){

            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(SOUND_POOL_MAX)
                    .build();
        } else{
            //SoundPool (int maxStreams, int streamType, int srcQuality)
            soundPool = new SoundPool(SOUND_POOL_MAX, AudioManager.STREAM_MUSIC,0);
        }

        cakeSound = soundPool.load(context, R.raw.cakesound, 1);
        umbrellaSound = soundPool.load(context, R.raw.umbrellasound, 1);
        spongeSound = soundPool.load(context, R.raw.spongesound, 1);
    }

    public void playCakeSound(){

        //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(cakeSound,0.5f,0.5f,1,0,1.0f);

    }

    public void playUmbrellaSound(){

        //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(umbrellaSound,1.0f,1.0f,1,0,1.0f);

    }

    public void playSpongeSound(){

        //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(spongeSound,1.0f,1.0f,1,0,1.0f);

    }
}
