package android.icloudtech.com.spotifystreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

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


public class MainActivityFragment extends Fragment {
    ArrayAdapter<String> arrayAdapter = null;
    SearchView search;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View searchview = inflater.inflate(R.layout.fragment_main, container,false);
        SearchView  search = (SearchView) searchview.findViewById(R.id.search_view);
        search.setQueryHint("Start typing to search...");

        List <String> playList = new ArrayList<String>();
        /*playList.add("Coldplay magic");
        playList.add("Coldplay china town");
        playList.add("Coldplay paradise");
        playList.add("Coldplay scientist");*/

        arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_main, R.id.list_textview,playList);
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView) rootview.findViewById(R.id.list_view);
        listView.setAdapter(arrayAdapter);

       FetchSpotifyTask spotifyTask= new FetchSpotifyTask();
       spotifyTask.execute("coldplay");

        return rootview;

    }

    public class FetchSpotifyTask extends AsyncTask<String, Void, String[]> {
        private final String LOG_TAG = FetchSpotifyTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {
            try {
                String url = "https://api.spotify.com/v1/search?q="+params[0]+"&type=artist";
                String jsonStr = getData(url);
                String artists[] = getArtistArray(jsonStr);
                return artists;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                arrayAdapter.clear();
                for (String dayForecastStr : result) {
                    arrayAdapter.add(dayForecastStr);
                }
            }
        }
    }

    private String[] getArtistArray(String jsonStr)
            throws JSONException {

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

                /*images = item.getJSONArray("images");
                imageURL = images.getJSONObject(0).getString("url");*/
                arr[i] = artistName; // + " " + imageURL;
            }
        }
        return arr;
    }

    public String getData(String strUrl) {
        String jsonStr = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        URL url = null;

        try {
            // Create the request to Spotify, and open the connection
            url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
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
