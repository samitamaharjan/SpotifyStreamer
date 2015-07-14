package android.icloudtech.com.spotifystreamer;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samita on 7/13/15.
 */
public class Utility {

    public static List<Track> getTrackList(String jsonStr)
            throws JSONException {

        List<Track> trackList = new ArrayList<Track>();

        String[] arr = null;
        JSONObject completeObj = new JSONObject(jsonStr);
        JSONArray trackArray = completeObj.getJSONArray("tracks");

        String trackId = null;
        String imageURL = null;
        String albumName = null;
        String trackName = null;
        for (int i = 0; i < trackArray.length(); i++) {
            JSONObject trackJSON = trackArray.getJSONObject(i);

            trackId = trackJSON.getString("id");

            JSONObject albumJSON = trackJSON.getJSONObject("album");
            JSONArray albumImagesJSON = albumJSON.getJSONArray("images");
            imageURL = albumImagesJSON.getJSONObject(0).getString("url");

            albumName = albumJSON.getString("name");

            trackName = trackJSON.getString("name");

            Track track = new Track(R.drawable.images, trackId, trackName, imageURL);
            trackList.add(track);
        }
        return trackList;
    }

    public static List<Artist> getArtistList(String jsonStr)
            throws JSONException {

        List<Artist> artistList = new ArrayList<Artist>();

        String[] arr = null;
        JSONObject completeObj = new JSONObject(jsonStr);
        JSONObject artists = completeObj.getJSONObject("artists");
        JSONArray items = artists.getJSONArray("items");
        JSONArray images = null;
        String imageURL = null;

        if (items != null) {
            arr = new String[items.length()];
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                String artistName = item.getString("name");

                images = item.getJSONArray("images");
                if (images != null && images.length() > 0) {
                    JSONObject imageObj = images.getJSONObject(0);
                    if (imageObj != null) {
                        imageURL = imageObj.getString("url");
                    }
                } else {
                    continue;
                }
                String artistId = item.getString("id");

                Artist artist = new Artist(R.drawable.images, artistId, artistName, imageURL);
                artistList.add(artist);
            }
        }
        return artistList;
    }

    public static String getData(String strUrl) {
        String jsonStr = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        URL url = null;
        InputStream inputStream = null;

        try {
            // Create the request to Spotify, and open the connection
            url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                jsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                jsonStr = null;
            }
            jsonStr = buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            jsonStr = null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
        return jsonStr;
    }
}
