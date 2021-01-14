package controller;

import controller.exceptions.ImageDatasetError;
import controller.exceptions.InvalidIDError;
import controller.exceptions.NotFoundError;
import controller.exceptions.ResultTooLargeError;
import controller.helpers.DataProcessor;
import controller.helpers.DiskStorer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.*;

public class ImageRepo implements IImageRepo {

    private final Map<String, JSONArray> DATASETS;
    private final DataProcessor DATA_PROCESSOR;
    private final DiskStorer DISK_STORER;

    public ImageRepo() {

        DATASETS = new HashMap<>();
        DATA_PROCESSOR = new DataProcessor();
        DISK_STORER = new DiskStorer();
    }

    @Override
    public Set<String> addDataset(String id) throws InvalidIDError {

        if (this.idValid(id) || DATASETS.containsKey(id)) {

            throw new InvalidIDError();
        }

        try {

            JSONArray datasetImages = DATA_PROCESSOR.loadDataset(id);
            DATASETS.put(id, datasetImages);
            DISK_STORER.store(id, datasetImages);

        } catch (NotFoundError e) {

            System.out.println(id + " folder not found.");

        } catch (ResultTooLargeError e) {

            System.out.println(id + " has too many images.");
        }

        return DATASETS.keySet();
    }

    private boolean idValid(String id) {

        String regex = "[A-Za-z0-9\\-]+";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(id);

        return !m.matches();
    }

    @Override
    public String removeDataset(String id) throws InvalidIDError, NotFoundError, ImageDatasetError {

        if (this.idValid(id)) {

            throw new InvalidIDError();
        }

        String path = "./data/" + id + "-dataset.txt";
        File f = new File(path);

        if (f.exists() && !f.isDirectory()) {

            if (!f.delete()) {

                throw new ImageDatasetError();
            }

        } else {

            throw new NotFoundError();
        }

        DATASETS.remove(id);

        return id;
    }
}
