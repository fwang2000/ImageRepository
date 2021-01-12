package test;

import controller.ImageRepo;

public class AddDatasetTest  {

    private static ImageRepo imageRepo = new ImageRepo();

    public static void main(String[] args) {

        String id = "images";
        imageRepo.addDataset("images");
    }
}
