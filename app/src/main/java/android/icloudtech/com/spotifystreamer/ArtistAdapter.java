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
public class ArtistAdapter extends BaseAdapter {

    private Context context = null;
    private List<Artist> artistList = null;
    private LayoutInflater inflater = null;

    public ArtistAdapter(Context context, List<Artist> artistList) {
        this.context = context;
        this.artistList = artistList;
        this.inflater = LayoutInflater.from(context);
    }

    public ArtistAdapter(String str) {

    }

    @Override
    public int getCount() {
        return artistList.size();
    }

    @Override
    public Object getItem(int position) {
        return artistList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return artistList.get(position).getId();
    }

    public void add(Artist artist) {
        artistList.add(artist);
    }

    public void clear() {
        artistList.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            // create View
            convertView = this.inflater.inflate(R.layout.list_main, parent, false);

            // create ViewHolder and relate view holder attributes to layout elements
            viewHolder = new ViewHolder();
            viewHolder.txtArtistName = (TextView) convertView.findViewById(R.id.list_textview);
            viewHolder.imgArtistImg = (ImageView) convertView.findViewById(R.id.list_imageview);

            // set ViewHolder to View
            convertView.setTag(viewHolder);
        } else {
            // get ViewHolder
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // get Artist in given position
        Artist artist = artistList.get(position);

        // set Artist values to ViewHolder
        viewHolder.txtArtistName.setText(artist.getName());
        artist.loadImage(this, viewHolder);
        return convertView;
    }
}
