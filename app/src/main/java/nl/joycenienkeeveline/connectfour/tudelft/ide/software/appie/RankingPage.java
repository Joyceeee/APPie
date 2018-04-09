package nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

public class RankingPage extends Activity {

    private Map<String, IdScore > playerMap = new HashMap<String, IdScore>();
    private int count =0;
    private String StringNameNumber;
    private String StringName;
    private Integer IntegerScore;
    private String StringScore;
    private String StringScoreReveal;
    private TextView NameFirstPlace;
    private TextView ScoreFirstPlace;
    private TextView NameSecondPlace;
    private TextView ScoreSecondPlace;
    private TextView NameThirdPlace;
    private TextView ScoreThirdPlace;
    private TextView NameFourthPlace;
    private TextView ScoreFourthPlace;
    private TextView NameFifthPlace;
    private TextView ScoreFifthPlace;
    private TextView NameSixthPlace;
    private TextView ScoreSixthPlace;
    private TextView NameSeventhPlace;
    private TextView ScoreSeventhPlace;
    private TextView NameEighthPlace;
    private TextView ScoreEighthPlace;
    private TextView NameNinethPlace;
    private TextView ScoreNinethPlace;
    private TextView NameTenthPlace;
    private TextView ScoreTenthPlace;



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

        setContentView(R.layout.activity_ranking_page);

        // Retrieve the list of existing players with id and scores from the server
        playerMap = getPlayers();

    }

    /**
     * Get the list of all players and their score from the server.
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
                        System.out.println(player);

                        JSONObject InfoPlayer = player;
                        try {
                            StringName = (String) InfoPlayer.get("name");
                            IntegerScore = (Integer) InfoPlayer.get("score");
                            StringScore = String.valueOf(IntegerScore);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        count++;
                        functionRevealTopTen(StringName,StringScore,count);

                    }
                }
                return playerMap;
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
        return treeMap;
    }

    public void functionRevealTopTen(String StringName, String StringScore, Integer count) {
        for (int index2 = 7; index2 > StringScore.length();) {
            StringBuilder str = new StringBuilder(StringScore);
            str.insert(0, '0');
            StringScore = str.toString();
        }
        StringBuilder str = new StringBuilder(StringScore);
        str.insert(2, ':');
        str.insert(5, ':');
        StringScoreReveal = str.toString();

        //https://stackoverflow.com/questions/39559218/add-a-char-into-a-string-at-position-x/39559259

        if(count == 1){
            StringBuilder str2 = new StringBuilder(StringName);
            str2.insert(0, '1');
            str2.insert(1, ' ');
            str2.insert(2, ' ');
            StringNameNumber = str2.toString();

            NameFirstPlace=findViewById(R.id.firstplaceName);
            NameFirstPlace.setText(StringNameNumber);

            ScoreFirstPlace=findViewById(R.id.firstplaceScore);
            ScoreFirstPlace.setText(StringScoreReveal);

        }

        if(count == 2){
            StringBuilder str2 = new StringBuilder(StringName);
            str2.insert(0, '2');
            str2.insert(1, ' ');
            str2.insert(2, ' ');
            StringNameNumber = str2.toString();

            NameSecondPlace=findViewById(R.id.secondplaceName);
            NameSecondPlace.setText(StringNameNumber);

            ScoreSecondPlace=findViewById(R.id.secondplaceScore);
            ScoreSecondPlace.setText(StringScoreReveal);
        }

        if(count == 3){
            StringBuilder str2 = new StringBuilder(StringName);
            str2.insert(0, '3');
            str2.insert(1, ' ');
            str2.insert(2, ' ');
            StringNameNumber = str2.toString();

            NameThirdPlace=findViewById(R.id.thirdplaceName);
            NameThirdPlace.setText(StringNameNumber);

            ScoreThirdPlace=findViewById(R.id.thirdplaceScore);
            ScoreThirdPlace.setText(StringScoreReveal);
        }

        if(count == 4){
            StringBuilder str2 = new StringBuilder(StringName);
            str2.insert(0, '4');
            str2.insert(1, ' ');
            str2.insert(2, ' ');
            StringNameNumber = str2.toString();

            NameFourthPlace=findViewById(R.id.fourthplaceName);
            NameFourthPlace.setText(StringNameNumber);

            ScoreFourthPlace=findViewById(R.id.fourthplaceScore);
            ScoreFourthPlace.setText(StringScoreReveal);
        }

        if(count == 5){
            StringBuilder str2 = new StringBuilder(StringName);
            str2.insert(0, '5');
            str2.insert(1, ' ');
            str2.insert(2, ' ');
            StringNameNumber = str2.toString();

            NameFifthPlace=findViewById(R.id.fifthplaceName);
            NameFifthPlace.setText(StringNameNumber);

            ScoreFifthPlace=findViewById(R.id.fifthplaceScore);
            ScoreFifthPlace.setText(StringScoreReveal);
        }

        if(count == 6){
            StringBuilder str2 = new StringBuilder(StringName);
            str2.insert(0, '6');
            str2.insert(1, ' ');
            str2.insert(2, ' ');
            StringNameNumber = str2.toString();

            NameSixthPlace=findViewById(R.id.sixthplaceName);
            NameSixthPlace.setText(StringNameNumber);

            ScoreSixthPlace=findViewById(R.id.sixthplaceScore);
            ScoreSixthPlace.setText(StringScoreReveal);
        }

        if(count == 7){
            StringBuilder str2 = new StringBuilder(StringName);
            str2.insert(0, '7');
            str2.insert(1, ' ');
            str2.insert(2, ' ');
            StringNameNumber = str2.toString();

            NameSeventhPlace=findViewById(R.id.seventhplaceName);
            NameSeventhPlace.setText(StringNameNumber);

            ScoreSeventhPlace=findViewById(R.id.seventhplaceScore);
            ScoreSeventhPlace.setText(StringScoreReveal);
        }

        if(count == 8){
            StringBuilder str2 = new StringBuilder(StringName);
            str2.insert(0, '8');
            str2.insert(1, ' ');
            str2.insert(2, ' ');
            StringNameNumber = str2.toString();

            NameEighthPlace=findViewById(R.id.eighthplaceName);
            NameEighthPlace.setText(StringNameNumber);

            ScoreEighthPlace=findViewById(R.id.eighthplaceScore);
            ScoreEighthPlace.setText(StringScoreReveal);
        }

        if(count == 9){
            StringBuilder str2 = new StringBuilder(StringName);
            str2.insert(0, '9');
            str2.insert(1, ' ');
            str2.insert(2, ' ');
            StringNameNumber = str2.toString();

            NameNinethPlace=findViewById(R.id.ninethplaceName);
            NameNinethPlace.setText(StringNameNumber);

            ScoreNinethPlace=findViewById(R.id.ninethplaceScore);
            ScoreNinethPlace.setText(StringScoreReveal);
        }

        if(count == 10){
            StringBuilder str2 = new StringBuilder(StringName);
            str2.insert(0, '1');
            str2.insert(1, '0');
            str2.insert(2, ' ');
            str2.insert(3, ' ');
            StringNameNumber = str2.toString();

            NameTenthPlace=findViewById(R.id.tenthplaceName);
            NameTenthPlace.setText(StringNameNumber);

            ScoreTenthPlace=findViewById(R.id.tenthplaceScore);
            ScoreTenthPlace.setText(StringScoreReveal);
        }

    }
    }

// https://www.guvi.in/qa/227/how-to-find-the-integer-length-in-java
