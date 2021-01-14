package test;

import controller.ImageRepo;

public class AddDatasetTest  {

    private static ImageRepo imageRepo = new ImageRepo();

    public static void main(String[] args) {

        try {
            imageRepo.addDataset("images");
        } catch (Exception e) {

            System.out.println("nope");
        }
    }
}
