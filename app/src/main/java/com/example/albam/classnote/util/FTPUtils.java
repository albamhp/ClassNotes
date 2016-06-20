package com.example.albam.classnote.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.webkit.MimeTypeMap;

import com.example.albam.classnote.R;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FTPUtils {

    private static FTPClient ftp;   // = new FTPClient();

    static {
        ftp = new FTPClient();
        try {
            ftp.connect("128.199.131.115");
            ftp.login("anonymous", "classnote");
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
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

    public static boolean isFile(String dir, String file){
        try {
            for (FTPFile ftpFile : ftp.listFiles("/root" + dir)) {
                if (ftpFile.getName().equals(file)){
                    return ftpFile.isFile();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void uploadFile(Uri uri, String dir, String fileName, Activity activity){
        try {

            ftp.changeWorkingDirectory("/root" + dir);
            ftp.enterLocalActiveMode();
            ContentResolver contentResolver = activity.getContentResolver();
            InputStream input = contentResolver.openInputStream(uri);
            boolean success = ftp.storeFile(fileName + "." + getMimeType(activity, uri), input);
            if (success) {
                new AlertDialog.Builder(activity).setMessage(R.string.file_loaded).create().show();
                return;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        new AlertDialog.Builder(activity).setMessage(R.string.file_not_loaded).setTitle(R.string.error).create().show();
    }

    public static String getMimeType(Activity activity, Uri uri) {
        String extension;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(activity.getContentResolver().getType(uri));
        } else {
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
        }
        return extension;
    }

    public static void downloadFile(String dir, String fileName, Activity activity){
        try {

            ftp.changeWorkingDirectory("/root" + dir);
            ftp.enterLocalPassiveMode();

            File downloadFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + fileName );
            //downloadFile.createNewFile();
            //get output stream
            OutputStream output = new BufferedOutputStream(new FileOutputStream(downloadFile));
            //get the file from the remote system
            boolean success = ftp.retrieveFile(fileName, output);
            //close output stream
            output.close();
            if (success) {
                new AlertDialog.Builder(activity).setMessage(R.string.file_downloaded).create().show();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        new AlertDialog.Builder(activity).setMessage(R.string.file_not_downloaded).setTitle(R.string.error).create().show();
    }

    public static List<String> getDirectories(String dir) {
        try {
            return filesToStrings(ftp.listDirectories("/root" + dir));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getFiles(String dir) {
        try {
            return filesToStrings(ftp.listFiles("/root" + dir));
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
