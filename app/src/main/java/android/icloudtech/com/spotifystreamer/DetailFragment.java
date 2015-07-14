package android.icloudtech.com.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Samita on 7/13/15.
 */
public class DetailFragment extends Fragment {

    TrackAdapter trackAdapter = null;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        String artistId = intent.getStringExtra(Intent.EXTRA_TEXT);
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ((TextView)(rootView.findViewById(R.id.fragment_detail))).setText(artistId);

        FetchTopTracksTask task = new FetchTopTracksTask();
        task.execute(artistId);
        return rootView;
    }

    public class FetchTopTracksTask extends AsyncTask<String, Void, List<Track>> {
        private final String LOG_TAG = FetchTopTracksTask.class.getSimpleName();

        @Override
        protected List<Track> doInBackground(String... params) {
            try {
                String url = "https://api.spotify.com/v1/artists/" + params[0] + "/top-tracks?country=US";
                String jsonStr = Utility.getData(url);
                return Utility.getTrackList(jsonStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Track> trackList) {
            if (trackAdapter != null) {
                trackAdapter.clear();
                for (Track track : trackList) {
                    trackAdapter.add(track);
                }
                trackAdapter.notifyDataSetChanged();
            }
        }
    }
}