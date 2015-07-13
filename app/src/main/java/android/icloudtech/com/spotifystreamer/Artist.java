package android.icloudtech.com.spotifystreamer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Samita on 7/9/15.
 */
public class Artist {

    private int id;
    private String artistId;
    private String name;
    private URL imageUri;

    private ArtistAdapter artistAdapter;
    private Bitmap bitmap;

    public Artist(int id, String artistId, String name, String strImageUri) {
        this.artistId = artistId;
        this.name = name;
        setImageUri(strImageUri);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void loadImage(ArtistAdapter artistAdapter, ViewHolder viewHolder) {
        if (artistAdapter != null) {
            this.artistAdapter = artistAdapter;
            if (this.getImageUri() != null) {
                new ImageLoadTask().execute(this.getImageUri());
            }
            viewHolder.imgArtistImg.setImageBitmap(bitmap);
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
                if (artistAdapter != null) {
                    artistAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
