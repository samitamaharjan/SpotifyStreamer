package android.icloudtech.com.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    ArrayAdapter<String> arrayAdapter = null;
    SearchView search;


    //This arraylist will have data as pulled from server.
    //ArrayList<Product> productResults = new ArrayList<Product>();

    // In order to filter the product results based on the search strings
    //ArrayList<Product> filterProductResults = new ArrayList<Product>() ;

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
        playList.add("Coldplay magic");
        playList.add("Coldplay china town");
        playList.add("Coldplay paradise");
        playList.add("Coldplay scientist");

        arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_main, R.id.list_textview,playList);
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView) rootview.findViewById(R.id.list_view);
        listView.setAdapter(arrayAdapter);

       FetchSpotifyTask spotifyTask= new FetchSpotifyTask();
       spotifyTask.execute(" ");

        return rootview;

    }



    }

public class FetchSpotifyTask extends AsyncTask<String, void, String[]>{
    private final String LOG_TAG = FetchSpotifyTask.class.getSimpleName();

    @Override
    protected String[] doInBackground(String...params){
        HttpURLConnection urlConnection = null;
        //BufferedReader = null;


    }

}
