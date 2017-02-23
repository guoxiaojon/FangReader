package com.example.jon.fangreader.model.http;

import com.example.jon.fangreader.app.App;
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
import com.example.jon.fangreader.utils.DeviceUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;


/**
 * Created by jon on 2017/1/2.
 */

public class RetrofitHelper {
    private static OkHttpClient mOkHttpClient;
    private static Apis mApis;

    public RetrofitHelper(){
        init();
    }

    private void init(){
        initokHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Apis.HOST)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(mOkHttpClient)
                .build();
        mApis = retrofit.create(Apis.class);

    }

    private void initokHttpClient() {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                String url = original.url().toString();
                if (url.contains("book/") ||
                        url.contains("book-list/") ||
                        url.contains("toc/") ||
                        url.contains("post/") ||
                        url.contains("user/")) {

                    Request request = original.newBuilder()
                            .addHeader("User-Agent", "ZhuiShuShenQi/3.40[preload=false;locale=zh_CN;clientidbase=android-nvidia]") // 不能转UTF-8
                            .addHeader("X-User-Agent", "ZhuiShuShenQi/3.40[preload=false;locale=zh_CN;clientidbase=android-nvidia]")
                            .addHeader("X-Device-Id", DeviceUtil.getIMEI(App.getInstance()))
                            .addHeader("Host", "api.zhuishushenqi.com")
                            .addHeader("Connection", "Keep-Alive")
                            .addHeader("If-None-Match", "W/\"2a04-4nguJ+XAaA1yAeFHyxVImg\"")
                            .addHeader("If-Modified-Since", "Tue, 02 Aug 2016 03:20:06 UTC")
                            .build();

                    Response response = chain.proceed(request);
                    //调用response.body().string() 会  java.lang.IllegalStateException: closed
                    //Logger.e(response.body().string());
//                    ResponseBody originalBody = response.body();
//                    ResponseBody body = response.peekBody(originalBody.contentLength());
//                    Logger.e(body.string());
                    return response;

                }

                Response response  = chain.proceed(original);
//                ResponseBody originalBody = response.body();
//                ResponseBody body = response.peekBody(originalBody.contentLength());
//                Logger.e(body.string());

                return response;
            }
        };
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(interceptor)
                .build();
    }

    public Observable<RecommendBean> fetchRecommend(String gender){
        return mApis.getRecommendList(gender);
    }

    public Observable<BookTocBean> fetchBookToc(String bookId){
        return mApis.getBookToc(bookId);
    }

    public Observable<ChapterForReadBean> fetchChapter(String url){
        return mApis.getChapterForRead(url);
    }
    public Observable<DiscussionListBean> fetchBookDetailDiscussionList(String bookId, String sort, String type, String start, String limit){
        return mApis.getBookDetailDisscussionList(bookId, sort, type, start, limit);
    }
    public Observable<DiscussionDetailBean> fetchDiscussionDetail(String discussionId){
        return mApis.getBookDisscussionDetail(discussionId);
    }
    public Observable<CommentListBean> fetchBestCommentList(String discussionId){
        return mApis.getBestComments(discussionId);
    }
    public Observable<CommentListBean> fetchDiscussionCommentList(String discussionId,String start,String limit){
        return mApis.getBookDisscussionComments(discussionId,start,limit);
    }

    public Observable<ReviewListBean> fetchBookDetailReviewList(String bookId,String sort,String start,String limit){
        return mApis.getBookDetailReviewList(bookId, sort, start, limit);
    }
    public Observable<ReviewDetailBean> fetchBookReviewDetail(String bookReviewId){
        return mApis.getBookReviewDetail(bookReviewId);
    }
    public Observable<CommentListBean> fetchBookReviewCommentList(String bookReviewId,String start,String limit){
        return mApis.getBookReviewComments(bookReviewId,start,limit);
    }
    public Observable<BookDetailBean> fetchBookDetail(String bookId){
        return mApis.getBookDetail(bookId);
    }
    public Observable<HotReviewBean> fetchHotReview(String bookId){
        return mApis.getHotReview(bookId);
    }
    public Observable<RecommendBookList> fetchRecommendBookList(String booId,String limit){
        return mApis.getRecommendBookList(booId, limit);
    }
    public Observable<BookListDetailBean> fetchBookListDetail(String bookListId){
        return mApis.getBookListDetail(bookListId);
    }
}
