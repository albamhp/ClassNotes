package com.example.albam.classnote.util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FTPUtils {

    private static FTPClient ftp = new FTPClient();

    static {
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

    public static List<String> getNotesFromCourse(String course) {
        try {
            FTPFile[] files = ftp.listFiles("/root/" + course);
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
