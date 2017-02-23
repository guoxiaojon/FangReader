package com.example.jon.fangreader.app;

import java.io.File;

/**
 * Created by jon on 2017/1/2.
 */

public class Constants {

    //PATH
    public static final String PATH_DATA = App.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";
    public static final String PATH_CACHE = PATH_DATA + File.separator + "cache";
    public static final String PATH_CACHE_IMAGE = PATH_CACHE + File.separator + "img";
    public static final String PATH_COLLECTION = PATH_DATA + File.separator + "collection";
    public static final String PATH_BOOKMARK = PATH_DATA + File.separator + "bookmark";
    public static final String PATH_CACHE_JSON  =PATH_CACHE + File.separator + "json";
    public static final String PATH_COLLECTION_BOOKLIST = PATH_DATA + File.separator + "booklist";
    //SP
    public static final String SP_NAME = "fr_sharepreference";
    public static final String SP_GENDER = "gender";
    public static final String SP_STATUS_CHAPTER = "status_chapter";
    public static final String SP_STATUS_BEGIN = "status_begin";
    public static final String SP_STATUS_END = "status_end";
    public static final String SP_MODE = "mode";
    public static final String SP_ISNIGHT = "is_night";
    public static final String SP_AUTOBIRGHT = "auto_birght";
    public static final String SP_USEVOLUME = "use_volume";
    public static final String SP_TEXTSIZE = "text_size";
    public static final String SP_BIRGHT = "birght";
    public static final String SP_NOIMAGE = "no_image";


    //TYPE
    public static final String TYPE_MALE = "male";
    public static final String TYPE_FEMALE = "female";

    //sort
    //默认
    public static final String SORT_DEFAULT = "updated";
    //最新发布
    public static final String SORT_CREATED = "created";
    //最有用
    public static final String SORT_HELPFUL = "helpful";
    //评论最多
    public static final String SORT_COMMENT_COUNT = "comment-count";
}
