package nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.AutoCompleteTextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.*;

public class GameOver extends Activity {

    //Initialise check if onPause occurred or not
    private boolean onBackButtonDevice=false;

    private int count =0;
    private Integer IntegerScore;
    private String StringScore;
    private String StringScoreHighest;
    private TextView valueScore;
    private TextView HighScore;
    private AutoCompleteTextView namePlayer;
    private TextView score;

    /** The map of existing players retrieved from the server <name/id/score>. */
    private Map<String, IdScore > playerMap = new HashMap <String, IdScore>();

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
        //finish();
        /*Remove title bar & notification bar for a nicer look
        Source: https://stackoverflow.com/questions/2591036/how-to-hide-the-title-bar-for-an-activity-in-xml-with-existing-custom-theme*/
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game_over);

        // Retrieve the score from MainMenu named 'TextValue' and set this score as a Text to the TextView 'score' in the activity_game_over.xml
        String s = getIntent().getStringExtra("ScoreAtGameOver");
        valueScore=findViewById(R.id.score);
        valueScore.setText(s);

        // Retrieve the textview of the name and score from the xml layout
         namePlayer = findViewById(R.id.namePlayer);
         score = findViewById(R.id.score);

        // Retrieve the list of existing players from the server
        playerMap = getPlayers();

    }

    public void playagain(View view) {
        GameOver.this.startActivity(new Intent(GameOver.this, MainActivity.class));
        GameOver.this.finish();
    }

    public void gotoHome(View view) {
        GameOver.this.startActivity(new Intent(GameOver.this, MainMenu.class));
        GameOver.this.finish();
    }


    /**
     * Send an HTTP request to the server to get the list of player's names.
     */
    private Map<String, IdScore> getPlayers() {
        // Instantiate new instance of HttpRequest
        HttpRequest request = new HttpRequest();
        TreeMap<String, IdScore> treeMap = new TreeMap<>();
        try {
            // Send the HTTP request, passing the endpoint and the method (GET)
            String result = request.execute("/players", "GET").get();

            // Convert the result into a JSON object
            JSONObject json = new JSONObject(result);
            if (json.has("players")) {
                JSONArray players = json.getJSONArray("players");
                if (players != null){
                    for (int index = 0; index < players.length(); index++) {
                        JSONObject player = players.getJSONObject(index);
                        IdScore json_idscore = new IdScore(player.getInt("id"), player.getInt("score"));
                        playerMap.put(player.getString("name"), json_idscore);

                        JSONObject InfoPlayer = player;
                        try {
                            IntegerScore = (Integer) InfoPlayer.get("score");
                            StringScore = String.valueOf(IntegerScore);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        count++;
                        functionRevealHighscore(StringScore,count);
                    }
                }
                return playerMap;
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
        return treeMap;
    }


    public void functionRevealHighscore(String StringScore, Integer count) {
        for (int index2 = 7; index2 > StringScore.length(); ) {
            StringBuilder str = new StringBuilder(StringScore);
            str.insert(0, '0');
            StringScore = str.toString();
        }
        StringBuilder str = new StringBuilder(StringScore);
        str.insert(2, ':');
        str.insert(5, ':');
        StringScoreHighest = str.toString();

        //https://stackoverflow.com/questions/39559218/add-a-char-into-a-string-at-position-x/39559259

        if (count == 1) {
            HighScore = findViewById(R.id.highestscore);
            HighScore.setText(StringScoreHighest);
        }
    }

    /**
     * When the 'Save' button is clicked, create players,
     * then save the game.
     *
     *
     */
    public void onClickSave(View view) {
        String playerName = namePlayer.getText().toString();
        String scorePlayer = score.getText().toString();

        scorePlayer = scorePlayer.replace(":", "");
        int scorePlayerInt = Integer.parseInt(scorePlayer);

        //https://stackoverflow.com/questions/32414079/android-how-to-remove-character-from-string

        Integer id_temp = createPlayer(playerName, scorePlayerInt);
        IdScore idscore_return = new IdScore(id_temp, scorePlayerInt);
        playerMap.put(playerName, idscore_return );

        TextView textView = (TextView) findViewById(R.id.proberen);
        Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeinsavefeedback);
        textView.startAnimation(startAnimation);

        //https://www.viralandroid.com/2015/11/fade-in-animation-in-android.html

    }

    /**
     * Send an HTTP request to the server to create a player.
     *
     * @param playerName the player's name
     * @param scorePlayerInt the score of player
     * @return player id of the newly created player, 0 otherwise.
     */
    private Integer createPlayer(String playerName, Integer scorePlayerInt) {
        // Instantiate new instance of HttpRequest
        HttpRequest request = new HttpRequest();
        try {
            // Prepare a JSON with info of the player
            JSONObject data = new JSONObject();
            data.put("name", playerName);
            data.put("score", scorePlayerInt);

            // Send the HTTP request, passing the endpoint,
            // the method (POST) and the data
            String result = request.execute("/players", "POST",
                    data.toString()).get();

            // Convert the result into a JSON object
            JSONObject json = new JSONObject(result);
            if (json.has("playerId")) {
                return json.getInt("playerId");
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
}




