package com.example.jon.fangreader.model.http;

import com.example.jon.fangreader.model.bean.BookDetailBean;
import com.example.jon.fangreader.model.bean.BookListDetailBean;
import com.example.jon.fangreader.model.bean.BookTocBean;
import com.example.jon.fangreader.model.bean.ChapterForReadBean;
import com.example.jon.fangreader.model.bean.CommentListBean;
import com.example.jon.fangreader.model.bean.DiscussionDetailBean;
import com.example.jon.fangreader.model.bean.DiscussionListBean;
import com.example.jon.fangreader.model.bean.HotReviewBean;
import com.example.jon.fangreader.model.bean.RecommendBean;
import com.example.jon.fangreader.model.bean.RecommendBookList;
import com.example.jon.fangreader.model.bean.ReviewDetailBean;
import com.example.jon.fangreader.model.bean.ReviewListBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by jon on 2017/1/2.
 */

public interface Apis {
    String HOST = "http://api.zhuishushenqi.com";
    String IMG_BASE_URL = "http://statics.zhuishushenqi.com";

    @GET("/book/recommend")
    Observable<RecommendBean> getRecommendList(@Query("gender") String gender);

    @GET("mix-atoc/{bookId}")
    Observable<BookTocBean> getBookToc(@Path("bookId") String bookId);

    @GET("http://chapter2.zhuishushenqi.com/chapter/{url}")
    Observable<ChapterForReadBean> getChapterForRead(@Path("url") String url);

    /**
     * 获取书籍详情讨论列表
     *
     * @param book  bookId
     * @param sort  updated(默认排序)
     *              created(最新发布)
     *              comment-count(最多评论)
     * @param type  normal
     *              vote
     * @param start 0
     * @param limit 20
     * @return
     */
    @GET("/post/by-book")
    Observable<DiscussionListBean> getBookDetailDisscussionList(@Query("book") String book, @Query("sort") String sort, @Query("type") String type, @Query("start") String start, @Query("limit") String limit);



    /**
     * 获取综合讨论区帖子详情
     *
     * @param disscussionId  _id
     * @return
     */
    @GET("/post/{disscussionId}")
    Observable<DiscussionDetailBean> getBookDisscussionDetail(@Path("disscussionId") String disscussionId);

    /**
     * 获取神评论列表(综合讨论区、书评区、书荒区皆为同一接口)
     *
     * @param disscussionId->_id
     * @return
     */
    @GET("/post/{disscussionId}/comment/best")
    Observable<CommentListBean> getBestComments(@Path("disscussionId") String disscussionId);

    /**
     * 获取综合讨论区帖子详情内的评论列表
     *
     * @param disscussionId     _id
     * @param start              0
     * @param limit              30
     * @return
     */
    @GET("/post/{disscussionId}/comment")
    Observable<CommentListBean> getBookDisscussionComments(@Path("disscussionId") String disscussionId, @Query("start") String start, @Query("limit") String limit);

    /**
     * 获取书籍详情书评列表
     *
     * @param bookId  bookId
     * @param sort  updated(默认排序)
     *              created(最新发布)
     *              helpful(最有用的)
     *              comment-count(最多评论)
     * @param start 0
     * @param limit 20
     * @return
     */
    @GET("/post/review/by-book")
    Observable<ReviewListBean> getBookDetailReviewList(@Query("book") String bookId, @Query("sort") String sort, @Query("start") String start, @Query("limit") String limit);


    /**
     * 获取书评区帖子详情
     *
     * @param bookReviewId _id
     * @return
     */
    @GET("/post/review/{bookReviewId}")
    Observable<ReviewDetailBean> getBookReviewDetail(@Path("bookReviewId") String bookReviewId);

    /**
     * 获取书评区、书荒区帖子详情内的评论列表
     *
     * @param bookReviewId->_id
     * @param start             0
     * @param limit             30
     * @return
     */
    @GET("/post/review/{bookReviewId}/comment")
    Observable<CommentListBean> getBookReviewComments(@Path("bookReviewId") String bookReviewId, @Query("start") String start, @Query("limit") String limit);


    /**
     * 获取书籍详细信息
     * */
    @GET("/book/{bookId}")
    Observable<BookDetailBean> getBookDetail(@Path("bookId") String bookId);


    /**
     * 热门评论
     *
     * @param bookid
     * @return
     */
    @GET("/post/review/best-by-book")
    Observable<HotReviewBean> getHotReview(@Query("book") String bookid);


    /**
     * 根据bookid 获取含有相似图书的推荐书单
     * */
    @GET("/book-list/{bookId}/recommend")
    Observable<RecommendBookList> getRecommendBookList(@Path("bookId") String bookId, @Query("limit") String limit);

    /**
     * 获取书单详情
     *
     * @return
     */
    @GET("/book-list/{bookListId}")
    Observable<BookListDetailBean> getBookListDetail(@Path("bookListId") String bookListId);


}
