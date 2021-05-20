package com.amansiol.fruitlia.model;

import java.util.ArrayList;

public class Folder {
    String folderName;
    ArrayList<String> filepaths;

    public Folder(String folderName, ArrayList<String> filepaths) {
        this.folderName = folderName;
        this.filepaths = filepaths;
    }

    public Folder() {
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public ArrayList<String> getFilepaths() {
        return filepaths;
    }

    public void setFilepaths(ArrayList<String> filepaths) {
        this.filepaths = filepaths;
    }
}
