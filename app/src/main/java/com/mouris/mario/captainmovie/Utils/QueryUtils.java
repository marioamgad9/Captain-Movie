package com.mouris.mario.captainmovie.Utils;

import android.util.Log;

import com.mouris.mario.captainmovie.Data.Movie;

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

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY_QUERY = "?api_key=834352e8708bf5fb62e4147667d02fc0";

    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    private QueryUtils() {
    }

    public static Movie fetchMovie(int movieId) {
        String request_url = BASE_URL + movieId + API_KEY_QUERY;

        URL url = createUrl(request_url);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        return extractMovieFromJson(jsonResponse);
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "There was an error while parsing URL", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            int response_code = urlConnection.getResponseCode();

            if (response_code == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code" + response_code);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
            if (inputStream != null) inputStream.close();
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        } else {
            Log.e(LOG_TAG, "InputStream is null");
        }

        return output.toString();
    }

    private static Movie extractMovieFromJson(String movieJson) {
        Movie movie = new Movie();
        try {
            JSONObject movieJsonObject = new JSONObject(movieJson);
            movie.homepage = movieJsonObject.getString("homepage");
            movie.title = movieJsonObject.getString("title");
            movie.overview = movieJsonObject.getString("overview");
            movie.release_date = movieJsonObject.getString("release_date");
            movie.vote_average = movieJsonObject.getDouble("vote_average");
            movie.poster_path = POSTER_BASE_URL + movieJsonObject.getString("poster_path");

            JSONArray genresJsonArray = movieJsonObject.getJSONArray("genres");
            for (int i=0 ; i < genresJsonArray.length(); i++) {
                JSONObject genreJsonObject = genresJsonArray.getJSONObject(i);
                movie.genres.add(genreJsonObject.getString("name"));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "There was an error in extractMovieFromJson", e);
        }

        return movie;
    }
}
