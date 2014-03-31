package net.kevxu.muzei.interfacelift;

public final class Dimension {
    final int width;
    final int height;

    protected Dimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    protected Dimension(String resolution) {
        int splitIndex = resolution.indexOf("x");
        if (splitIndex < 1 || splitIndex == resolution.length() - 1) {
            throw new IllegalArgumentException("Can not resolve string " + resolution);
        }

        this.width = Integer.valueOf(resolution.substring(0, splitIndex));
        this.height = Integer.valueOf(resolution.substring(splitIndex + 1, resolution.length()));
    }

    @Override
    public String toString() {
        return width + "x" + height;
    }
}
