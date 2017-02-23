package com.example.jon.fangreader.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jon.fangreader.R;
import com.example.jon.fangreader.component.ImageLoader;
import com.example.jon.fangreader.model.bean.BookListDetailBean;
import com.example.jon.fangreader.utils.TextUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jon on 2017/2/23.
 */

public class BookListDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int HEADER = 0x1;
    public static final int NORMAL = 0x2;

    private List<BookListDetailBean.BookListBean.BooksBean> mBookList;
    private RecyclerView.ViewHolder mHeader;
    private Context mContext;
    private OnClickListener mListener;

    public BookListDetailAdapter(List<BookListDetailBean.BookListBean.BooksBean> mBookList, RecyclerView.ViewHolder mHeader, Context mContext, OnClickListener mListener) {
        this.mBookList = mBookList;
        this.mHeader = mHeader;
        this.mContext = mContext;
        this.mListener = mListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == HEADER){
            return mHeader;
        }else {
            return  new ItemView(LayoutInflater.from(mContext).inflate(R.layout.activity_booklistdetail_item,parent,false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(position > 0){
            BookListDetailBean.BookListBean.BooksBean.BookBean bean = mBookList.get(position).getBook();
            ItemView view = (ItemView)holder;
            ImageLoader.load(mContext,bean.getCover(),view.ivBookPic);
            view.tvTitle.setText(bean.getTitle());
            view.tvAuthor.setText(bean.getAuthor());
            view.tvSubCount.setText(bean.getLatelyFollower()+"人在追 | ");
            view.tvWordCount.setText(TextUtil.formatNum(bean.getWordCount()));
            view.tvDesc.setText(bean.getLongIntro());
            view.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClick(position);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return HEADER;
        }else {
            return NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return mBookList.size() + 1;
    }

    class ItemView extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_booklistdetail_item_bookpic)
        ImageView ivBookPic;
        @BindView(R.id.tv_booklistdetail_item_title)
        TextView tvTitle;
        @BindView(R.id.tv_booklistdetail_item_name)
        TextView tvAuthor;
        @BindView(R.id.tv_booklistdetail_item_subcount)
        TextView tvSubCount;
        @BindView(R.id.tv_booklistdetail_item_wordcount)
        TextView tvWordCount;
        @BindView(R.id.tv_booklistdetail_item_content)
        TextView tvDesc;


        public ItemView(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnClickListener{
        void onClick(int position);
    }
}
