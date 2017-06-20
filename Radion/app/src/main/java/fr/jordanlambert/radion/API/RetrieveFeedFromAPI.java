package fr.jordanlambert.radion.API;

import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jordan on 05/02/2017.
 */


public class RetrieveFeedFromAPI extends AsyncTask<Void, Void, String> {

    private static final String TAG = "RetrieveFeedTask class";
    private static final String API_KEY = "lwUWLTDhm1mshKNpTegeITNu8qlVp1puJaGjsnlsx0Jnlkb1X3";
    private static final String API_URL = "https://igdbcom-internet-game-database-v1.p.mashape.com/games/?fields=name%2Csummary%2Cslug%2Curl&limit=10&offset=0&order=release_dates.date%3Adesc&search=zelda";


    private Exception exception;

    protected void onPreExecute() {

        Log.d(TAG, "onPreExecute RetrieveFeedTask");
    }

    protected String doInBackground(Void... urls) {
        // Do some validation here

        try {
            URL url = new URL(API_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("X-Mashape-Key", API_KEY);

            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    protected void onPostExecute(String response) {
        if (response == null) {
            response = "THERE WAS AN ERROR";
        }

        // Parse JSON response
        try {
            JSONArray jResponse = new JSONArray(response);
            for (int i=0; i < jResponse.length(); i++)
            {
                try {
                    JSONObject oneObject = jResponse.getJSONObject(i);
                    // Pulling items from the array
                    String name = oneObject.getString("name");
                    String id = oneObject.getString("id");
                    String url = oneObject.getString("url");
                    String summary = oneObject.getString("summary");

                    Log.i("name of " + id + " ", name);

                } catch (JSONException e) {
                    Log.e(TAG, "Something went wrong during JSON parsing :/");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //responseView.setText(repsonse);

    }

}
