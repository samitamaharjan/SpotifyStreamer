package android.icloudtech.com.spotifystreamer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Samita on 7/9/15.
 */
public class Track {

    private int id;
    private String trackId;
    private String trackName;
    private URL imageUri;

    private TrackAdapter trackAdapter;
    private Bitmap bitmap;

    public Track(int id, String trackId, String trackName, String strImageUri) {
        this.trackId = trackId;
        this.trackName= trackName;
        setImageUri(strImageUri);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public URL getImageUri() {
        return imageUri;
    }

    public void setImageUri(String strImageUri) {
        try {
            this.imageUri = new URL(strImageUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadImage(TrackAdapter trackAdapter, TrackViewHolder trackViewHolder) {
        if (trackAdapter != null) {
            this.trackAdapter = trackAdapter;
            if (this.getImageUri() != null) {
                new ImageLoadTask().execute(this.getImageUri());
            }
            trackViewHolder.imgTrackImage.setImageBitmap(bitmap);
        }
    }

    private class ImageLoadTask extends AsyncTask<URL, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(URL... params) {
            Bitmap bitmap = null;

            try {
                URL url = params[0];
                InputStream in = url.openConnection().getInputStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap bm) {
            if (bm != null) {
                bitmap = bm;
                if (trackAdapter != null) {
                    trackAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
