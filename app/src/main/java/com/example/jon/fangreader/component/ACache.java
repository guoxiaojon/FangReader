package com.example.jon.fangreader.component;

import android.util.Log;

import com.example.jon.fangreader.app.Constants;
import com.example.jon.fangreader.model.bean.ChapterForReadBean;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by jon on 2017/1/3.
 */

public class ACache {
    /**
     * 1.saveChapterForReadBean
     * 2.getChapterForReadBean
     * 3.getCahceSize
     * 4.deleteCache
     * */

    private static File getChapterCacheFile(String bookId,String chapterUrl,boolean read){
        File cacheDir = new File(Constants.PATH_CACHE+File.separator+bookId.hashCode());
        if(!cacheDir.exists()){
            cacheDir.mkdirs();
        }
        File cacheFile = new File(cacheDir,String.valueOf(chapterUrl.hashCode()));
        if(read && cacheFile.exists() && cacheFile.length() == 0){
            cacheFile.delete();
            return null;
        }

        Log.d("data3","cacheFile : "+cacheFile);///data/data/com.example.jon.fangreader/cache/data/cache/-647745752/-1873693441
        try {
            if(!cacheFile.createNewFile()){//已缓存
                if(read){
                    return cacheFile;
                }
            }else {
                if(read){
                    return null;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return cacheFile;
    }

    /**
     * @param bookId :要保存的图书的Id
     * @param url :要保存的图书对应章节的url
     * @param readBean :要保存的章节的bean
     * */
    public static void saveChapterForRead(String bookId,String url,ChapterForReadBean readBean){
        File file = getChapterCacheFile(bookId,url,false);
        if(file == null)
            return;
        try{
            Log.d("data2",readBean.getChapter().getTitle());
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            Log.d("data",file.toString());
            writer.write(readBean.getChapter().getTitle()+"\n"+readBean.getChapter().getBody());
            writer.close();

        }catch (Exception e){
            e.printStackTrace();
        }

    }



    public static void deleteCache(String bookId){
        File cacheDir = new File(Constants.PATH_CACHE+File.separator+bookId.hashCode());
        if(cacheDir.exists()){
            if(cacheDir.isDirectory()){
                for(File file:cacheDir.listFiles()){
                    file.delete();
                }
            }
            cacheDir.delete();
        }

    }

    public static File getChapterForRead(String bookId, String url){
        File file = getChapterCacheFile(bookId,url,true);
        if(file != null){
            try{
//                ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
//                ChapterForReadBean readBean = (ChapterForReadBean) in.readObject();
//                in.close();
//                return readBean;
                return file;

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getCacheSize(){
        File cacheDir = new File(Constants.PATH_CACHE);
        long sum = 0;
        if(!cacheDir.exists())
            return "0";
        if(cacheDir.isDirectory()){
            sum = getDirSize(cacheDir);
        }

       if(sum > 1024*1024){
           return String.valueOf(sum/1024/1024) + " MB";
       }else if(sum > 1024){
           return String.valueOf(sum/1024) +" KB";
       }else {
           return String.valueOf(sum) + " B";
       }

    }

    public static String getJsonCacheKey(Object... object){
        String key = "";
        for(Object o : object){
            key += "_"+o;
        }
        key.replaceFirst("_","");
        return key;
    }

    public static String getCacheJson(String key){
        File dir = new File(Constants.PATH_CACHE_JSON );
        dir.mkdirs();
        File file = new File(dir,key);
        if(file.exists()){
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                StringBuffer sb = new StringBuffer();
                String line = null;
                while ((line = reader.readLine()) != null){
                    sb.append(line);
                }
                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void saveCacheJson(String key,String json){
        File dir = new File(Constants.PATH_CACHE_JSON );
        dir.mkdirs();
        File file = new File(dir,key);
        try {
            file.createNewFile();
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static long getDirSize(File dir){
        long sum = 0;
        for(File file : dir.listFiles()){
            if(file.isFile()){
                sum +=file.length();
            }else if(file.isDirectory()){
                sum += getDirSize(file);
            }
        }

        return sum;
    }

}
