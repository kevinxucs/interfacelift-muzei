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
            Dimension dimen = client.getSuitablePhotoDimension();

            Log.d(TAG, "Suggested wallpaper size: " + dimen.toString());

            InterfaceliftWallpaper sugWallpaper = client.getLatestWallpaper(dimen);

            if (sugWallpaper == null || sugWallpaper.getDownloads() == null) {
                Log.w(TAG, "wallpaper null");
                throw new RetryException();
            }

            if (sugWallpaper.getDownloads().size() == 0) {
                Log.w(TAG, "No photos returned from API.");
                scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS);
                return;
            }

            // TODO: Implement viewIntent for photo description

            InterfaceliftWallpaper.Download sugWallpaperDownload = sugWallpaper.getDownloads().get(0);

            Dimension alterDimen = new Dimension(sugWallpaperDownload.getResolution().width + 1, sugWallpaperDownload.getResolution().height + 1);
            InterfaceliftWallpaper alterWallpaper = client.getLatestWallpaper(alterDimen);

            InterfaceliftWallpaper wallpaper;
            InterfaceliftWallpaper.Download wallpaperDownload;
            if (alterWallpaper != null
                    && alterWallpaper.getDownloads() != null
                    && alterWallpaper.getDownloads().size() > 0
                    && alterWallpaper.getTimestamp() > sugWallpaper.getTimestamp()) {
                Log.d(TAG, "Using alternative wallpaper size: " + alterDimen.toString());

                wallpaper = alterWallpaper;
                wallpaperDownload = alterWallpaper.getDownloads().get(0);
            } else {
                wallpaper = sugWallpaper;
                wallpaperDownload = sugWallpaperDownload;
            }

            Log.d(TAG, "Found wallpaper size: " + wallpaperDownload.getResolution());
            Log.d(TAG, "Found wallpaper url: " + wallpaperDownload.getUrl());

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
