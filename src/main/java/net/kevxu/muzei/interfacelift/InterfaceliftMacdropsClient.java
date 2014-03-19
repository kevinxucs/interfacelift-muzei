package net.kevxu.muzei.interfacelift;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class InterfaceliftMacdropsClient {
    private static final String TAG = "InterfaceliftMacdropsClient";

    private static final String USER_AGENT = "Macdrops 1.1 rv:4 (Macintosh; Mac OS X 10.9.2; en_US)";
    private static final String QUERY_HOST = "http://macdrops.ifl.cc";
    private static final String QUERY_URL = "/v1/date.json?res=%s&lic=%s";

    private static final String DEFAULT_LICENSE = "";

    private final Context mContext;
    private final String mLicense;

    public InterfaceliftMacdropsClient(Context context) {
        this(context, DEFAULT_LICENSE);
    }

    public InterfaceliftMacdropsClient(Context context, String license) {
        mContext = context;
        mLicense = license;

        // Enable Cookie for HttpURLConnection
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
    }

    static final class Dimension {
        int width;
        int height;

        protected Dimension(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public String toString() {
            return width + "x" + height;
        }
    }

    /**
     * Get suitable photo size based on the screen size of phone.
     *
     * @return Dimension Dimension of suitable photo size.
     */
    protected Dimension getSuitablePhotoDimension() {
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        final int width = size.x;
        final int height = size.y;

        int screenLayout = mContext.getResources().getConfiguration().screenLayout;
        boolean isXlarge = ((screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
        boolean isLarge = ((screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        final boolean isTablet = (isXlarge || isLarge);

        Dimension dimen;

        if (!isTablet) {
            // Wallpaper for phone needs at least [width x 2] x height
            dimen = new Dimension(width * 2, height);
        } else {
            // Wallpaper for tablet needs at least [long edge] x [long edge]
            int longEdge = width > height ? width : height;
            dimen = new Dimension(longEdge, longEdge);
        }

        return dimen;
    }

    protected String getUserAgent() {
        return USER_AGENT;
    }

    protected String getQueryUrl() {
        Dimension dimen = getSuitablePhotoDimension();
        Log.d(TAG, "Suggested wallpaper size: " + dimen.toString());

        return QUERY_HOST + String.format(QUERY_URL, dimen.toString(), mLicense);
    }

    protected String fetchPlainText(String query) throws IOException {
        URL url = new URL(query);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", USER_AGENT);

        try {
            InputStream in = new BufferedInputStream(connection.getInputStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            return new String(out.toByteArray(), "UTF-8");
        } finally {
            connection.disconnect();
        }
    }

    public InterfaceliftWallpaper getLatestWallpaper() throws IOException, JSONException {
        String respJsonString = fetchPlainText(getQueryUrl());
        JSONObject respJson = (JSONObject) new JSONTokener(respJsonString).nextValue();

        JSONObject wallpaperJson = respJson.getJSONObject("wallpaper");
        JSONArray downloadsJson = wallpaperJson.getJSONArray("downloads");

        List<InterfaceliftWallpaper.Download> downloads = new ArrayList<InterfaceliftWallpaper.Download>(downloadsJson.length());
        for (int i = 0; i < downloadsJson.length(); i++) {
            JSONObject downloadJson = downloadsJson.getJSONObject(i);
            InterfaceliftWallpaper.Download download = new InterfaceliftWallpaper.Download.Builder()
                    .setResolution(downloadJson.getString("res"))
                    .setUrl(downloadJson.getString("url"))
                    .setSizeInBytes(downloadJson.getInt("size_in_bytes"))
                    .build();
            downloads.add(download);
        }

        return new InterfaceliftWallpaper.Builder()
                .setId(wallpaperJson.getInt("id"))
                .setSubmitterId(wallpaperJson.getInt("submitter_id"))
                .setFileBase(wallpaperJson.getString("file_base"))
                .setDisplay(wallpaperJson.getString("display"))
                .setDescription(wallpaperJson.getString("description"))
                .setExtension(wallpaperJson.getString("extension"))
                .setCameraId(wallpaperJson.getInt("camera_id"))
                .setLensId(wallpaperJson.getInt("lens_id"))
                .setFocalLength(wallpaperJson.getDouble("focal_length"))
                .setAperture(wallpaperJson.getDouble("aperture"))
                .setShutterSpeed(wallpaperJson.getDouble("shutter_speed"))
                .setIso(wallpaperJson.getInt("iso"))
                .setLatitude(wallpaperJson.getDouble("latitude"))
                .setLongitude(wallpaperJson.getDouble("longitude"))
                .setTimestamp(wallpaperJson.getLong("timestamp"))
                .setName(wallpaperJson.getString("name"))
                .setWebsite(wallpaperJson.getString("website"))
                .setTwitter(wallpaperJson.getString("twitter"))
                .setWallpapersByPhotographer(wallpaperJson.getInt("wallpapers_by_photographer"))
                .setCameraBrand(wallpaperJson.getString("camera_brand"))
                .setCameraModel(wallpaperJson.getString("camera_model"))
                .setDownloads(downloads)
                .build();
    }
}
