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
import com.example.jon.fangreader.model.bean.RecommendBookList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jon on 2017/2/22.
 */

public class BookDetailRecommdBooksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<RecommendBookList.RecommendBook> mRecommendBookList;
    private Context mContext;
    private OnClickListener mListener;

    public BookDetailRecommdBooksAdapter(List<RecommendBookList.RecommendBook> mRecommendBookList, Context mContext,OnClickListener mListener) {
        this.mRecommendBookList = mRecommendBookList;
        this.mContext = mContext;
        this.mListener = mListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemView(LayoutInflater.from(mContext).inflate(R.layout.activity_bookdetail_recommendbooklist_item,parent,false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ItemView view = (ItemView)holder;
        view.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClick(position);
            }
        });
        RecommendBookList.RecommendBook book = mRecommendBookList.get(position);
        ImageLoader.load(mContext,book.getCover(),view.ivPic);
        view.tvTitle.setText(book.getTitle());
        view.tvName.setText(book.getAuthor());
        view.tvDescri.setText(book.getDesc());
        view.tvTotal.setText("共"+book.getBookCount()+"本书 | ");
        view.tvCollecNum.setText(book.getCollectorCount()+"人收藏");
    }

    @Override
    public int getItemCount() {
        return mRecommendBookList.size();
    }

    class ItemView extends RecyclerView.ViewHolder{

        @BindView(R.id.iv_recommbooks_pic)
        ImageView ivPic;
        @BindView(R.id.tv_recommbooks_title)
        TextView tvTitle;
        @BindView(R.id.tv_recommbooks_name)
        TextView tvName;
        @BindView(R.id.tv_recommbooks_descri)
        TextView tvDescri;
        @BindView(R.id.tv_recommbooks_totalnum)
        TextView tvTotal;
        @BindView(R.id.tv_recommbooks_collecnum)
        TextView tvCollecNum;

        public ItemView(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnClickListener{
        void onClick(int position);
    }
}
