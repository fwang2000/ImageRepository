package controller.helpers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DiskStorer {

    public void store(String id, JSONArray images) {

        String path = "./data/";
        String fileString = id + "-dataset.txt";

        try {

            FileWriter writer = new FileWriter(path + fileString);
            writer.write(images.toJSONString());
            writer.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
