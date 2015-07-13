package android.icloudtech.com.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    ArtistAdapter artistAdapter = null;
    SearchView search;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final List<Artist> artistList = new ArrayList<Artist>();
        artistAdapter = new ArtistAdapter(getActivity(), artistList);

        View rootview = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView) rootview.findViewById(R.id.list_view);

        SearchView  searchView = (SearchView) rootview.findViewById(R.id.search_view);
        searchView.setQueryHint("Start typing to search...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                FetchSpotifyTask spotifyTask= new FetchSpotifyTask();
                spotifyTask.execute(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                FetchSpotifyTask spotifyTask= new FetchSpotifyTask();
                spotifyTask.execute(newText);
                return true;
            }
        });

        listView.setAdapter(artistAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist selectedArtist = (Artist) artistAdapter.getItem(position);
                //Toast.makeText(getActivity(), selectedArtist.getName(), Toast.LENGTH_SHORT).show();

                Intent detailIntent = new Intent(getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_TEXT, selectedArtist.getArtistId());
                startActivity(detailIntent);
            }
        });

        /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = prefs.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));*/

        /*FetchSpotifyTask spotifyTask= new FetchSpotifyTask();
        spotifyTask.execute("john");*/

        return rootview;

    }

    public class FetchSpotifyTask extends AsyncTask<String, Void, List<Artist>> {
        private final String LOG_TAG = FetchSpotifyTask.class.getSimpleName();

        @Override
        protected List<Artist> doInBackground(String... params) {
            try {
                String url = "https://api.spotify.com/v1/search?q="+params[0]+"&type=artist";
                String jsonStr = getData(url);
                return getArtistList(jsonStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Artist> artistList) {
            if (artistList != null) {
                artistAdapter.clear();
                for (Artist artist : artistList) {
                    artistAdapter.add(artist);
                }
                artistAdapter.notifyDataSetChanged();
            }
        }
    }

    private List<Artist> getArtistList(String jsonStr)
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

    public String getData(String strUrl) {
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
