package com.example.albam.classnote.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.widget.ArrayAdapter;

import com.example.albam.classnote.MainActivity;
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

    public static List<String> getCoursesFromServer() {
        try {
            FTPFile[] courses = ftp.listDirectories("/root/");
            List<String> courseList = new ArrayList<>();
            for (FTPFile file : courses) {
                courseList.add(file.getName());
            }
            return courseList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getOrError(List<String> strings, Activity activity) {
        if (strings == null) {
            new AlertDialog.Builder(activity).setMessage(R.string.could_not_connect).setTitle(R.string.error).create().show();
            return Collections.emptyList();
        }
        return strings;
    }

    public static List<String> getNotesFromCourse(String courses) {
        try {

            FTPFile[] files = ftp.listFiles("/root/" + courses);
            List<String> fileList = new ArrayList<>();
            for (FTPFile file : files) {
                fileList.add(file.getName());
            }
            return fileList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
