package controller;

import controller.exceptions.ImageDatasetError;
import controller.exceptions.InvalidIDError;
import controller.exceptions.NotFoundError;
import org.json.simple.JSONObject;

import java.util.Set;

public interface IImageRepo {

    Set<String> addDataset(String id) throws InvalidIDError;

    String removeDataset(String id) throws InvalidIDError, NotFoundError, ImageDatasetError;
}
