package controller.helpers;

import controller.domain.Image;
import controller.domain.ImageKind;
import controller.exceptions.ImageDatasetError;
import controller.exceptions.NotFoundError;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.*;

public class DataProcessor {

    public List<Image> loadDataset(String id) throws NotFoundError {

        String dirPath = "/../../../images/" + id;

        List<Image> validImages = new ArrayList<>();

        File dir = new File(dirPath);
        File[] directoryList = dir.listFiles();

        if (directoryList != null) {

            for (File child : directoryList) {

                if (this.isImage(child.getName())) {

                    validImages.add(this.processData(child));
                }
            }

        } else {

            throw new NotFoundError();
        }

        while (validImages.remove(null));

        return validImages;
    }

    private Image processData(File img) {

        JSONObject jsonObject = new JSONObject();

        BufferedImage image;

        try {

            image = ImageIO.read(img);

        } catch (IOException e) {

            e.printStackTrace();
            return null;
        }

        String name = img.getName();
        int width = image.getWidth();
        int height = image.getHeight();
        ImageKind kind = this.getExtension(img);
        String base64 = this.encodeImgToBase64(img);

        return new Image(name, width, height, kind, base64);
    }

    private ImageKind getExtension(File img) {

        String imgName = img.getName();
        switch (imgName.substring(imgName.lastIndexOf(".") + 1)) {

            case "gif":

                return ImageKind.GIF;

            case "jpg":

                return ImageKind.JPG;

            case "jpeg":

                return ImageKind.JPEG;

            case "png":

                return ImageKind.PNG;

            default:

                throw new ImageDatasetError();
        }
    }

    private String encodeImgToBase64(File img) {

        String encodedImage = null;

        try {

            FileInputStream fileInputStreamReader = new FileInputStream(img.getAbsolutePath());
            byte[] bytes = new byte[(int) img.length()];
            fileInputStreamReader.read(bytes);
            encodedImage = Base64.getEncoder().encodeToString(bytes);

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return encodedImage;
    }

    private boolean isImage(String fileName) {

        if (fileName == null) {

            return false;
        }

        String regex = "([^\\s]+(\\.(?i)(jpe?g|png|gif))$)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(fileName);

        return m.matches();
    }
}
