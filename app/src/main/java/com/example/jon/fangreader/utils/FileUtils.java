package com.example.jon.fangreader.utils;

import com.example.jon.fangreader.app.Constants;
import com.example.jon.fangreader.model.bean.BookMark;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jon on 2017/2/12.
 */

public class FileUtils {
    public static synchronized void saveBookMark(List<BookMark> marks,String bookId) {
        File dir = new File(Constants.PATH_BOOKMARK);
        dir.mkdirs();
        File file = new File(dir,String.valueOf(bookId.hashCode()));

        try {
            file.createNewFile();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(marks);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized List<BookMark> getBookMark(String bookId) {
        File file = new File(Constants.PATH_BOOKMARK+File.separator+bookId.hashCode());
        if (file.exists()) {
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                List<BookMark> marks = (List<BookMark>) in.readObject();
                in.close();
                return marks;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        return new ArrayList<>();

    }
}
