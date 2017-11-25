package com.example.android.quakereport.parser;

/**
 * Created by ASPIRE on 013, 13, Nov, 2017.
 */

import android.util.Log;

import com.example.android.quakereport.model.Earthquake;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {}

    public static List<Earthquake> fetchJsonFromUrl(String usgsURL){
        URL url = createURL(usgsURL);

        String jsonResponse = null;
        jsonResponse =  makeHttpRequest(url);

        List<Earthquake> earthquakes = extractEarthquakes(jsonResponse);

        return earthquakes;
    }

    private static String makeHttpRequest(URL url) {
        String jsonResponse = null;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == 200){
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = fethRawJsonResponse(inputStream);
            }
        } catch (IOException e) {
            Log.e("IOException","an Error occured setting up the connection", e);
        }
        return jsonResponse;
    }

    private static String fethRawJsonResponse(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    private static URL createURL(String sampleUsgsApi) {
        URL url = null;
        try {
            url = new URL(sampleUsgsApi);
        } catch (MalformedURLException e) {
            Log.e("MalformedException:", "Error Creating a URL", e);
        }
        return url;
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<Earthquake> extractEarthquakes(String jsonResponse) {

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

            JSONObject root = new JSONObject(jsonResponse);
            JSONArray features = root.getJSONArray("features".toLowerCase());

            for (int index = 0; index < features.length(); index++){
                JSONObject object = features.getJSONObject(index);
                JSONObject properties = object.getJSONObject("properties".toLowerCase());
                double magnitude = properties.getDouble("mag".toLowerCase());
                String place = properties.getString("place".toLowerCase());
                long time = properties.getLong("time".toLowerCase());
                String url = properties.getString("url".toLowerCase());

                earthquakes.add(new Earthquake(magnitude, place, time, url));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

}