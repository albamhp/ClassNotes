package com.example.albam.classnote.util;

import android.app.Activity;
import android.app.AlertDialog;

import com.example.albam.classnote.R;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FTPUtils {

    private static FTPClient ftp;   // = new FTPClient();

    static {
        ftp = new FTPClient();
        try {
            ftp.connect("128.199.87.171");
            ftp.login("anonymous", "classnote");
            ftp.enterLocalPassiveMode();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[] asArray(List<String> files) {
        return files.toArray(new String[files.size()]);
    }

    public static List<String> getOrError(List<String> strings, Activity activity) {
        if (strings == null) {
            new AlertDialog.Builder(activity).setMessage(R.string.could_not_connect).setTitle(R.string.error).create().show();
            return Collections.emptyList();
        }
        return strings;
    }

    public static List<String> getDirectories(String dir) {
        try {
            return filesToStrings(ftp.listDirectories("/root/" + dir));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getFiles(String dir) {
        try {
            return filesToStrings(ftp.listFiles("/root/" + dir));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<String> filesToStrings(FTPFile[] files) {
        List<String> fileList = new ArrayList<>();
        for (FTPFile file : files) {
            fileList.add(file.getName());
        }
        return fileList;
    }

}
