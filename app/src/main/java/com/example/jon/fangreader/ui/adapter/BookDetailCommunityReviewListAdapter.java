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
import com.example.jon.fangreader.model.bean.ReviewListBean;
import com.example.jon.fangreader.utils.DateUtil;
import com.example.jon.fangreader.utils.SystemUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jon on 2017/2/17.
 */

public class BookDetailCommunityReviewListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<ReviewListBean.Reviews> mReviews;
    private OnClickAndLoadListener mListener;
    private LayoutInflater mInflater;

    private static final int NORMAL = 0x1;
    private static final int BOTTOM = 0x2;
    private static final int NOMORE = 0x3;

    private boolean mNoMore = true;

    public BookDetailCommunityReviewListAdapter(Context mContext, List<ReviewListBean.Reviews> mReviews, OnClickAndLoadListener mListener) {
        this.mContext = mContext;
        this.mReviews = mReviews;
        this.mListener = mListener;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == NORMAL){
            return new ItemView(mInflater.inflate(R.layout.fragment_bookdetailcommunity_review_item,parent,false));
        }else if(viewType == BOTTOM){
            return new BottomView(mInflater.inflate(R.layout.fragment_bookdetailcommunity_discussion_load,parent,false));
        }else {
            return new NoMoreView(mInflater.inflate(R.layout.fragment_bookdetailcommunity_discussion_nomore,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ItemView){
            ItemView view = (ItemView)holder;
            ReviewListBean.Reviews bean = mReviews.get(position);
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

        }else if(holder instanceof BottomView){
            mListener.loadMore();

        }
    }

    @Override
    public int getItemCount() {
        return mReviews.size()+1;
    }

    public void setNoMore(boolean noMore){
        this.mNoMore = noMore;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == mReviews.size()){
            if(mNoMore){
                return NOMORE;
            }else {
                return BOTTOM;
            }
        }else {
            return NORMAL;
        }

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
    class BottomView extends RecyclerView.ViewHolder{

        public BottomView(View itemView) {
            super(itemView);
        }
    }
    class NoMoreView extends RecyclerView.ViewHolder{

        public NoMoreView(View itemView) {
            super(itemView);
        }
    }
    public interface OnClickAndLoadListener{
        void loadMore();
        void onClick(int position);
    }

}
