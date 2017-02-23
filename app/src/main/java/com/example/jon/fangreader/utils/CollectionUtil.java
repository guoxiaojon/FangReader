package com.example.jon.fangreader.utils;

import com.example.jon.fangreader.app.Constants;
import com.example.jon.fangreader.model.bean.RecommendBean;
import com.example.jon.fangreader.model.bean.RecommendBookList;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jon on 2017/1/3.
 */

public class CollectionUtil {
    /**
     * 操作Recommend.Book 中的 id,title,update,laseChapter
     *
     * 1.getCollectionList
     * 2.addCollectionItem
     * 3.deleteCollectionItem
     * 4.setLastChapterAndUpdateTime
     * 5.setTop
     * */

    public static synchronized void saveCollectionList(List<RecommendBean.Book> books){
        File collecDir = new File(Constants.PATH_COLLECTION);
        if(!collecDir.exists()){
            collecDir.mkdirs();
        }
        File collecFile = new File(collecDir,"collection");
        try {
            collecFile.createNewFile();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(collecFile));
            out.writeObject(books);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static synchronized List<RecommendBean.Book> getCollectionList(){
        File file = new File(new File(Constants.PATH_COLLECTION),"collection");
        if(file.exists()){
            ObjectInputStream in = null;
            try {
                in = new ObjectInputStream(new FileInputStream(file));
                List<RecommendBean.Book> books = (List<RecommendBean.Book>)in.readObject();
                in.close();
                return books;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        Logger.e("收藏列表不存在");
        return null;

    }
    public static void setLastChapterAndUpdateTime(String bookId,String lastChapter,String updateTime){
        List<RecommendBean.Book> books = getCollectionList();
        for(RecommendBean.Book book : books){
            if(book.getId().equals(bookId)){
                book.setLastChapter(lastChapter);
                book.setUpdated(updateTime);
                book.setReaded(false);

            }
        }
        saveCollectionList(books);

    }

    public static void deleteCollectionItem(String bookId){
        List<RecommendBean.Book> books = getCollectionList();
        for(Iterator<RecommendBean.Book> iterator = books.iterator();iterator.hasNext();){
            if(iterator.next().getId().equals(bookId)){
                iterator.remove();
            }
        }
        saveCollectionList(books);
    }
    /**
     * @return true 添加成功 ，false 该书目已存在
     * */
    public static boolean addCollectionItem(RecommendBean.Book book){
        List<RecommendBean.Book> books = getCollectionList();
        if(!books.contains(book)){
            books.add(book);
            saveCollectionList(books);
            return true;

        }
        return false;
    }

    /**
     * 置顶
     * */

    public static void setTop(RecommendBean.Book book){
        List<RecommendBean.Book> list = getCollectionList();
        list.get(0).setTop(false);

        for(int i=0;i<list.size();i++){
            if(list.get(i).equals(book)){
                list.get(i).setTop(true);
                RecommendBean.Book temp = list.get(0);
                list.set(0,list.get(i));
                list.set(i,temp);
                break;

            }
        }
        saveCollectionList(list);
    }

    public static void cancelTop(RecommendBean.Book book){
        List<RecommendBean.Book> list = getCollectionList();
        for(int i=0;i<list.size();i++){
            if(list.get(i).equals(book)){
                list.get(i).setTop(false);
                break;
            }
        }
        sortCollectionListByCollecTime(list);
        saveCollectionList(list);


    }


    private static void sortCollectionListByCollecTime(List<RecommendBean.Book> list) {
        Collections.sort(list,new Comparator<RecommendBean.Book>() {

            @Override
            public int compare(RecommendBean.Book book, RecommendBean.Book t1) {
                return book.getCollecTime()<t1.getCollecTime()?-1:1;
            }
        });

    }

    /**
     * 操作书单收藏
     * */
    public static boolean addBookListCollection(RecommendBookList.RecommendBook collecBean){
            File dir = new File(Constants.PATH_COLLECTION_BOOKLIST);
            dir.mkdirs();
            File file = new File(dir,"booklist_collection");
            try {
                file.createNewFile();
                List<RecommendBookList.RecommendBook> list = getBookListCollections();
                if(list.contains(collecBean)){
                    return false;
                }else {
                    list.add(collecBean);
                    saveBookListCollection(list);
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;


    }

    public static void saveBookListCollection(List<RecommendBookList.RecommendBook> list){
        synchronized (RecommendBookList.RecommendBook.class){
            File dir = new File(Constants.PATH_COLLECTION_BOOKLIST);
            dir.mkdirs();
            File file = new File(dir,"booklist_collection");
            try {
                file.createNewFile();
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
                out.writeObject(list);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public static void deleteBookListCollection(RecommendBookList.RecommendBook collecBean){
        File dir = new File(Constants.PATH_COLLECTION_BOOKLIST);
        dir.mkdirs();
        File file = new File(dir,"booklist_collection");
        if(file.exists()){
            List<RecommendBookList.RecommendBook> list = getBookListCollections();
            for(Iterator<RecommendBookList.RecommendBook> iterator = list.iterator(); iterator.hasNext();){
                if(iterator.next().equals(collecBean)){
                    iterator.remove();
                }
            }
            saveBookListCollection(list);
        }
    }

    public static List<RecommendBookList.RecommendBook> getBookListCollections(){
        synchronized (RecommendBookList.RecommendBook.class){
            File dir = new File(Constants.PATH_COLLECTION_BOOKLIST);
            dir.mkdirs();
            File file = new File(dir,"booklist_collection");
            List<RecommendBookList.RecommendBook> list = new ArrayList<>();
            if(file.exists()){
                try {
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                    list = ( List<RecommendBookList.RecommendBook>)in.readObject();
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return list;
        }

    }
}
