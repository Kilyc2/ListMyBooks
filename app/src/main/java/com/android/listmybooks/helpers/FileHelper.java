package com.android.listmybooks.helpers;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHelper {

    public static File createFile(String path, String fileName) throws IOException{
        File file = new File(path, fileName);
        createFileIfNotExists(file);
        return file;
    }

    private static void createFileIfNotExists(File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    public static String saveBitmap(Bitmap bitmap, String path, String name) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        File file = createFile(path, name + ".jpg");
        writeFile(bytes, file);
        return file.getPath();
    }

    private static void writeFile(ByteArrayOutputStream bytes, File file) throws IOException {
        //write the bytes in file
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(bytes.toByteArray());
        // remember close de FileOutput
        fo.close();
    }
}
