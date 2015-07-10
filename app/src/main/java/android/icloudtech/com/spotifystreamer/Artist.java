package android.icloudtech.com.spotifystreamer;

import android.net.Uri;

/**
 * Created by Samita on 7/9/15.
 */
public class Artist {

    private int id;
    private String name;
    private Uri imageUri;

    public Artist(int id, String name, String strImageUri) {
        this.name = name;
        setImageUri(strImageUri);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(String strImageUri) {
        this.imageUri = Uri.parse(strImageUri);
    }
}
