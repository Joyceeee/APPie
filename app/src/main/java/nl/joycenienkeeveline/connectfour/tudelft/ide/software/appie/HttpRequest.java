package nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpRequest extends AsyncTask<String, Void, String> {
    /**
     * Maximum time to read data from the server, in milliseconds.
     */
    private static final int READ_TIMEOUT = 5000;
    /**
     * Maximum time to establish a connection with the server, in milliseconds.
     */
    private static final int CONNECTION_TIMEOUT = 5000;
    /**
     * URL of the web server (change to your own server).
     */
    private static final String SERVER_URL = "http://145.94.181.231:8081";

    /**
     * Handle the connection with the server in the background.
     *
     * @param params [0] endpoint (/players, /games...)
     *               [1] method (GET, POST...)
     *               [2] body (optional, content to send to the server)
     * @return content return by the server
     */
    @Override
    protected String doInBackground(String... params) {
        String endpoint = params[0];
        String method = params[1];
        String result = null;
        String inputLine;
        try {
            // Create a URL object holding our url
            URL url = new URL(SERVER_URL + endpoint);
            // Create a connection
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            // Set methods and timeouts
            connection.setRequestMethod(method);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setRequestProperty("Content-Type", "application/json");

            // If there is a third parameter, pass it as body of the request.
            if (params.length == 3) {
                String body = params[2];
                byte[] outputInBytes = body.getBytes("UTF-8");
                OutputStream os = connection.getOutputStream();
                os.write(outputInBytes);
                os.close();
            }
            // Connect to our url
            connection.connect();
            // Create a new InputStreamReader to receive the result
            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());
            // Create a new buffered reader and String Builder
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            // Check if the line we are reading is not null
            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            // Set our result equal to our stringBuilder
            result = stringBuilder.toString();
            // Close our InputStream and Buffered reader
            reader.close();
            streamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}

