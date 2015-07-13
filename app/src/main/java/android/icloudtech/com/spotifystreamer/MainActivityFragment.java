package android.icloudtech.com.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

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
                String jsonStr = Utility.getData(url);
                return Utility.getArtistList(jsonStr);
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
}
