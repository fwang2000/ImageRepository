package controller.helpers;

import controller.domain.ImageKind;
import controller.exceptions.ImageDatasetError;
import controller.exceptions.NotFoundError;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.exceptions.ResultTooLargeError;
import org.json.simple.*;

public class DataProcessor {

    public JSONArray loadDataset(String id) throws NotFoundError, ResultTooLargeError {

        String dirPath = "./images/" + id;

        JSONArray validImages = new JSONArray();

        File dir = new File(dirPath);

        File[] directoryList = dir.listFiles();

        if (directoryList != null) {

            if (directoryList.length > 250) {

                throw new ResultTooLargeError();
            }

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

    private JSONObject processData(File img) {

        BufferedImage bufferedImage;

        try {

            bufferedImage = ImageIO.read(img);

        } catch (IOException e) {

            e.printStackTrace();
            return null;
        }

        JSONObject image = new JSONObject();

        image.put("name", img.getName());
        image.put("width", bufferedImage.getWidth());
        image.put("height", bufferedImage.getHeight());
        try {

            image.put("kind", this.getExtension(img));

        } catch (ImageDatasetError e) {

            return null;
        }
        image.put("base64", this.encodeImgToBase64(img));

        return image;
    }

    private ImageKind getExtension(File img) throws ImageDatasetError {

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
            if (fileInputStreamReader.read(bytes) == -1) {

                System.exit(1);
            }
            encodedImage = Base64.getEncoder().encodeToString(bytes);

        } catch (Exception e) {

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
