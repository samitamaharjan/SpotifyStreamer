package android.icloudtech.com.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Samita on 7/9/15.
 */
public class TrackAdapter extends BaseAdapter {

    private Context context = null;
    private List<Track> trackList = null;
    private LayoutInflater inflater = null;

    public TrackAdapter(Context context, List<Track> trackList) {
        this.context = context;
        this.trackList = trackList;
        this.inflater = LayoutInflater.from(context);
    }

    public TrackAdapter(String str) {

    }

    @Override
    public int getCount() {
        return trackList.size();
    }

    @Override
    public Object getItem(int position) {
        return trackList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return trackList.get(position).getId();
    }

    public void add(Track track) {
        trackList.add(track);
    }

    public void clear() {
        trackList.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TrackViewHolder trackViewHolder = null;
        if (convertView == null) {
            // create View
            convertView = this.inflater.inflate(R.layout.list_main, parent, false);

            // create ViewHolder and relate view holder attributes to layout elements
            trackViewHolder = new TrackViewHolder();
            trackViewHolder.txtTrackName = (TextView) convertView.findViewById(R.id.list_textview);
            trackViewHolder.imgTrackImage = (ImageView) convertView.findViewById(R.id.list_imageview);

            // set ViewHolder to View
            convertView.setTag(trackViewHolder);
        } else {
            // get ViewHolder
            trackViewHolder = (TrackViewHolder)convertView.getTag();
        }

        // get Artist in given position
        Track track = trackList.get(position);

        // set Artist values to ViewHolder
        trackViewHolder.txtTrackName.setText(track.getTrackName());
        track.loadImage(this, trackViewHolder);
        return convertView;
    }
}
