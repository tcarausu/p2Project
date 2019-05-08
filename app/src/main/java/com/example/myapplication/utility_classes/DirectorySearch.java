package com.example.myapplication.utility_classes;

import java.io.File;
import java.util.ArrayList;

/**
 * Class used to search for all the files in
 * the directories declared in the FilePath.class and return as a list
 */


public class DirectorySearch {

    /**
     * Searching Directory and return a list of all the directories
     * @param directory
     * @return
     */
    public static ArrayList<String> getDirectory(String directory){
        ArrayList<String> dirPathArray = new ArrayList<>();
        File file = new File(directory);
        File [] fileList = file.listFiles();

        for (int i = 0; i < fileList.length; i++){
            if (fileList[i].isDirectory()){
                dirPathArray.add(fileList[i].getAbsolutePath());
            }
        }
        return dirPathArray;
    }

    /**
     * Getting all the files in the directories
     */

    public static ArrayList<String> getFiles(String directory){
        ArrayList<String> dirPathArray = new ArrayList<>();
        File file = new File(directory);
        File [] fileList = file.listFiles();

        for (int i = 0; i < fileList.length; i++){
            if (fileList[i].isFile()){
                dirPathArray.add(fileList[i].getAbsolutePath());
            }
        }
        return dirPathArray;
    }
}
