package com.example.jon.fangreader.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jon.fangreader.R;
import com.example.jon.fangreader.component.ImageLoader;
import com.example.jon.fangreader.model.bean.HotReviewBean;
import com.example.jon.fangreader.utils.DateUtil;
import com.example.jon.fangreader.utils.SystemUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jon on 2017/2/22.
 */

public class BookDetailHotReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<HotReviewBean.Reviews> mReviewList;
    private Context mContext;
    private OnClickListener mListener;

    public BookDetailHotReviewAdapter(List<HotReviewBean.Reviews> mReviewList, Context mContext, OnClickListener listener) {
        this.mReviewList = mReviewList;
        this.mContext = mContext;
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemView(LayoutInflater.from(mContext).inflate(R.layout.fragment_bookdetailcommunity_review_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ItemView view = (ItemView)holder;
        HotReviewBean.Reviews bean = mReviewList.get(position);
        view.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClick(position);
            }
        });
        ImageLoader.load(mContext,bean.getAuthor().getAvatar(),view.ivPic);
        view.tvName.setText(bean.getAuthor().getNickname());
        view.tvRank.setText("lv."+bean.getAuthor().getLv());
        view.tvTime.setText(DateUtil.formatTime(bean.getCreated()));
        view.tvTitle.setText(bean.getTitle());
        view.tvContent.setText(bean.getContent());
        view.tvLike.setText(String.valueOf(bean.getLikeCount()));
        Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.post_item_like);
        drawable.setBounds(0, 0, (int) SystemUtils.dp2px(mContext,17), (int)SystemUtils.dp2px(mContext,17));
        view.tvLike.setCompoundDrawables(drawable, null, null, null);

        view.rbRate.setProgress(bean.getRating());

    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    class ItemView extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_bookdetailcommunity_review_pic)
        CircleImageView ivPic;
        @BindView(R.id.tv_bookdetailcommunity_review_name)
        TextView tvName;
        @BindView(R.id.tv_bookdetailcommunity_review_rank)
        TextView tvRank;
        @BindView(R.id.tv_bookdetailcommunity_review_time)
        TextView tvTime;
        @BindView(R.id.tv_bookdetailcommunity_review_title)
        TextView tvTitle;
        @BindView(R.id.rb_bookdetailcommunity_review_bar)
        RatingBar rbRate;
        @BindView(R.id.tv_bookdetailcommunity_review_content)
        TextView tvContent;
        @BindView(R.id.tv_bookdetailcommunity_review_like)
        TextView tvLike;

        public ItemView(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnClickListener{
        void onClick(int position);
    }
}
