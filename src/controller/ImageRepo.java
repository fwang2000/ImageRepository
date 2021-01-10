package controller;

import controller.domain.Image;
import controller.exceptions.ImageDatasetError;
import controller.exceptions.InvalidIDError;
import controller.exceptions.NotFoundError;
import controller.helpers.DataProcessor;
import controller.helpers.DiskStorer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.*;

public class ImageRepo implements IImageRepo {

    private final Map<String, List<Image>> DATASETS;
    private final DataProcessor DATA_PROCESSOR;
    private final DiskStorer DISK_STORER;

    public ImageRepo() {

        DATASETS = new HashMap<>();
        DATA_PROCESSOR = new DataProcessor();
        DISK_STORER = new DiskStorer();
    }

    @Override
    public String[] addDataset(String id) {

        if (!this.idValid(id) || DATASETS.containsKey(id)) {

            throw new InvalidIDError();
        }

        List<Image> datasetImages = DATA_PROCESSOR.loadDataset(id);

        DATASETS.put(id, datasetImages);
        DISK_STORER.store(id, datasetImages);

        return new String[0];
    }

    private boolean idValid(String id) {

        String regex = "[A-Za-z0-9\\-]+";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(id);

        return m.matches();
    }

    @Override
    public String removeDataset(String id) {

        if (!this.idValid(id)) {

            throw new InvalidIDError();
        }

        String path = "./data/" + id + "-dataset.txt";
        File f = new File(path);

        if (f.exists() && !f.isDirectory()) {

            if (!f.delete()) {

                throw new ImageDatasetError();
            };

        } else {

            throw new NotFoundError();
        }

        DATASETS.remove(id);

        return id;
    }

    @Override
    public String[] performQuery(String query) {


        return new String[0];
    }
}
