package controller;

import controller.domain.ImageDataset;

public interface IImageRepo {

    String[] addDataset(String id);

    String removeDataset(String id);

    String[] performQuery(String query);
}
