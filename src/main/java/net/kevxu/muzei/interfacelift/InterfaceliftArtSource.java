package net.kevxu.muzei.interfacelift;

import android.util.Log;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;

import org.json.JSONException;

import java.io.IOException;

public class InterfaceliftArtSource extends RemoteMuzeiArtSource {
    private static final String TAG = "InterfaceliftArtSource";
    private static final String SOURCE_NAME = "InterfaceLIFT";

    private static final int ROTATE_TIME_MILLIS = 3 * 60 * 60 * 1000; // rotate every 3 hours

    public InterfaceliftArtSource() {
        super(SOURCE_NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onTryUpdate(int reason) throws RetryException {
        String currentToken = (getCurrentArtwork() != null) ? getCurrentArtwork().getToken() : null;

        final InterfaceliftMacdropsClient client = new InterfaceliftMacdropsClient(this);

        try {
            InterfaceliftWallpaper wallpaper = client.getLatestWallpaper();

            if (wallpaper == null || wallpaper.getDownloads() == null) {
                Log.e(TAG, "wallpaper null");
                throw new RetryException();
            }

            if (wallpaper.getDownloads().size() == 0) {
                Log.w(TAG, "No photos returned from API.");
                scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS);
                return;
            }

            // TODO: Implement viewIntent for photo description


            InterfaceliftWallpaper.Download wallpaperDownload = wallpaper.getDownloads().get(0);
            Log.v(TAG, "Found wallpaper size: " + wallpaperDownload.getResolution());

            String token = wallpaper.getToken();
            if (!token.equals(currentToken)) {
                publishArtwork(new Artwork.Builder()
                        .title(wallpaper.getDisplay())
                        .byline(wallpaper.getName())
                        .imageUri(wallpaperDownload.getUri())
                        .token(token)
                        .build());
            }

            scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS);
        } catch (IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));
            throw new RetryException();
        } catch (JSONException e) {
            Log.e(TAG, Log.getStackTraceString(e));
            scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS);
        }
    }
}
