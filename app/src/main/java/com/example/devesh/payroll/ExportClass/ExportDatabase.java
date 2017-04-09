package com.example.devesh.payroll.ExportClass;

import android.os.Environment;

import com.example.devesh.payroll.Database.MyDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by devesh on 2/4/17.
 */

public class ExportDatabase {

    public static final String NEW_LINE = "\n\n";

    public static void Export() throws IOException {
        File inFile = new File(Environment.getDataDirectory() + "/data/com.example.devesh.payroll/databases/" + MyDatabase.DB_NAME);
        FileInputStream fileInputStream = new FileInputStream(inFile);

        File outFile = new File(Environment.getExternalStorageDirectory() + "/" + MyDatabase.DB_NAME);
        FileOutputStream fileOutputStream = new FileOutputStream(outFile);

        byte[] buffer = new byte[1024];
        int length;

        while ((length = fileInputStream.read(buffer)) > 0) {
            fileOutputStream.write(buffer, 0, length);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        fileInputStream.close();
    }

    public static void createFile(ArrayList<String> addToFile, String fileName) throws IOException {
        File file = new File(Environment.getExternalStorageDirectory(), fileName);

        if (!file.isFile()) {
            file.createNewFile();
        }

        FileOutputStream fileOutputStream = new FileOutputStream(file, true);
        int length = addToFile.size();

        for (int i = 0; i < length; i++) {
            fileOutputStream.write(addToFile.get(i).getBytes());
            fileOutputStream.write(NEW_LINE.getBytes());
        }
        fileOutputStream.flush();
        fileOutputStream.close();
    }

}
