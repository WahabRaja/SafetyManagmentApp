package com.simplemobiletools.calculator.retrofituploadimage;

/**
 * Created by haseeb on 10/8/2017.
 */

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

public class SongsManager {
    // SDCard Path
    final String MEDIA_PATH = Environment.getExternalStorageDirectory()
            .getPath() + "/";
    private ArrayList<String> songsList= new ArrayList<String>();
    private String mp3Pattern = ".jpg";
    private String pngPattern = ".png";
    private String mp4Pattern = ".mp4";
    // Constructor
    public SongsManager() {

    }

    /**
     * Function to read all mp3 files and store the details in
     * ArrayList
     * */
    public ArrayList<String> getPlayList() {
        if (MEDIA_PATH != null) {
            File home = new File(MEDIA_PATH);
            File[] listFiles = home.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    System.out.println(file.getAbsolutePath());
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addSongToList(file);
                    }
                }
            }
        }
        // return songs list array
        return songsList;
    }

    private void scanDirectory(File directory) {
        if (directory != null) {
            File[] listFiles = directory.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addSongToList(file);
                    }

                }
            }
        }
    }

    private void addSongToList(File song) {

//adding JPEG and PNG format Images
        if (song.getName().endsWith(mp3Pattern) || song.getName().endsWith(pngPattern)|| song.getName().endsWith(mp4Pattern)) {
            songsList.add(song.getPath());
        }
    }
}

