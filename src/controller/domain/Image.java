package controller.domain;

public class Image {

    private String name;
    private int width;
    private int height;
    private ImageKind kind;
    private String base64;

    public Image(String name, int width, int height, ImageKind kind, String base64) {

        this.name = name;
        this.width = width;
        this.height = height;
        this.kind = kind;
        this.base64 = base64;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }

    public ImageKind getKind() {
        return kind;
    }

    public String getBase64() {
        return base64;
    }
}
