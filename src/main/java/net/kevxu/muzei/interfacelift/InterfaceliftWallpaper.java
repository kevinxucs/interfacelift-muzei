package net.kevxu.muzei.interfacelift;

import android.net.Uri;

import java.util.List;

@SuppressWarnings("unused")
public class InterfaceliftWallpaper {
    private int id;
    private int submitterId;
    private String fileBase;
    private String display;
    private String description;
    private String extension;
    private int cameraId;
    private int lensId;
    private double focalLength;
    private double aperture;
    private double shutterSpeed;
    private int iso;
    private double latitude;
    private double longitude;
    private long timestamp;
    private String name;
    private String website;
    private String twitter;
    private int wallpapersByPhotographer;
    private String cameraBrand;
    private String cameraModel;
    private List<Download> downloads;

    public String getToken() {
        return String.valueOf(id);
    }

    public int getId() {
        return id;
    }

    public int getSubmitterId() {
        return submitterId;
    }

    public String getFileBase() {
        return fileBase;
    }

    public String getDisplay() {
        return display;
    }

    public String getDescription() {
        return description;
    }

    public String getExtension() {
        return extension;
    }

    public int getCameraId() {
        return cameraId;
    }

    public int getLensId() {
        return lensId;
    }

    public double getFocalLength() {
        return focalLength;
    }

    public double getAperture() {
        return aperture;
    }

    public double getShutterSpeed() {
        return shutterSpeed;
    }

    public int getIso() {
        return iso;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getName() {
        return name;
    }

    public String getWebsite() {
        return website;
    }

    public String getTwitter() {
        return twitter;
    }

    public int getWallpapersByPhotographer() {
        return wallpapersByPhotographer;
    }

    public String getCameraBrand() {
        return cameraBrand;
    }

    public String getCameraModel() {
        return cameraModel;
    }

    public List<Download> getDownloads() {
        return downloads;
    }

    static class Download {
        private String resolution;
        private String url;
        private int sizeInBytes;

        public Uri getUri() {
            return Uri.parse(url);
        }

        public String getResolution() {
            return resolution;
        }

        public String getUrl() {
            return url;
        }

        public int getSizeInBytes() {
            return sizeInBytes;
        }

        static class Builder {
            private Download download;

            public Builder() {
                download = new Download();
            }

            public Download build() {
                return download;
            }

            public Builder setResolution(String resolution) {
                download.resolution = resolution;
                return this;
            }

            public Builder setUrl(String url) {
                download.url = url;
                return this;
            }

            public Builder setSizeInBytes(int sizeInBytes) {
                download.sizeInBytes = sizeInBytes;
                return this;
            }
        }
    }

    static class Builder {
        private InterfaceliftWallpaper wallpaper;

        public Builder() {
            wallpaper = new InterfaceliftWallpaper();
        }

        public InterfaceliftWallpaper build() {
            return wallpaper;
        }

        public Builder setId(int id) {
            wallpaper.id = id;
            return this;
        }

        public Builder setSubmitterId(int submitterId) {
            wallpaper.submitterId = submitterId;
            return this;
        }

        public Builder setFileBase(String fileBase) {
            wallpaper.fileBase = fileBase;
            return this;
        }

        public Builder setDisplay(String display) {
            wallpaper.display = display;
            return this;
        }

        public Builder setDescription(String description) {
            wallpaper.description = description;
            return this;
        }

        public Builder setExtension(String extension) {
            wallpaper.extension = extension;
            return this;
        }

        public Builder setCameraId(int cameraId) {
            wallpaper.cameraId = cameraId;
            return this;
        }

        public Builder setLensId(int lensId) {
            wallpaper.lensId = lensId;
            return this;
        }

        public Builder setFocalLength(double focalLength) {
            wallpaper.focalLength = focalLength;
            return this;
        }

        public Builder setAperture(double aperture) {
            wallpaper.aperture = aperture;
            return this;
        }

        public Builder setShutterSpeed(double shutterSpeed) {
            wallpaper.shutterSpeed = shutterSpeed;
            return this;
        }

        public Builder setIso(int iso) {
            wallpaper.iso = iso;
            return this;
        }

        public Builder setLatitude(double latitude) {
            wallpaper.latitude = latitude;
            return this;
        }

        public Builder setLongitude(double longitude) {
            wallpaper.longitude = longitude;
            return this;
        }

        public Builder setTimestamp(long timestamp) {
            wallpaper.timestamp = timestamp;
            return this;
        }

        public Builder setName(String name) {
            wallpaper.name = name;
            return this;
        }

        public Builder setWebsite(String website) {
            wallpaper.website = website;
            return this;
        }

        public Builder setTwitter(String twitter) {
            wallpaper.twitter = twitter;
            return this;
        }

        public Builder setWallpapersByPhotographer(int wallpapersByPhotographer) {
            wallpaper.wallpapersByPhotographer = wallpapersByPhotographer;
            return this;
        }

        public Builder setCameraBrand(String cameraBrand) {
            wallpaper.cameraBrand = cameraBrand;
            return this;
        }

        public Builder setCameraModel(String cameraModel) {
            wallpaper.cameraModel = cameraModel;
            return this;
        }

        public Builder setDownloads(List<Download> downloads) {
            wallpaper.downloads = downloads;
            return this;
        }
    }
}
