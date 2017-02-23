package com.example.jon.fangreader.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jon.fangreader.R;
import com.example.jon.fangreader.component.ImageLoader;
import com.example.jon.fangreader.model.bean.DiscussionListBean;
import com.example.jon.fangreader.utils.DateUtil;
import com.example.jon.fangreader.utils.SharePreferenceUtil;
import com.example.jon.fangreader.utils.SystemUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jon on 2017/2/14.
 */

public class BookDetailCommunityDiscussionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<DiscussionListBean.PostBean> mPostBeans;
    private RecyclerView mRecyclerView;
    private LayoutInflater mInflater;
    private ClickAndLoadListener mListener;


    private static final int TYPE_NORMAL = 0x1;
    private static final int TYPE_BOTTOM = 0x2;
    private static final int TYPE_NOMORE = 0x3;

    private boolean mNoMore;

    public BookDetailCommunityDiscussionAdapter(Context mContext,List<DiscussionListBean.PostBean> mPostBeans, RecyclerView mRecyclerView,ClickAndLoadListener listener) {
        this.mContext = mContext;
        this.mPostBeans = mPostBeans;
        this.mRecyclerView = mRecyclerView;
        this.mInflater = LayoutInflater.from(mContext);
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if(viewType == TYPE_NORMAL){
            holder = new NormalView(mInflater.inflate(R.layout.fragment_bookdetailcommunity_discussion_item,parent,false));

        }else if(viewType == TYPE_BOTTOM){
            holder = new BottomView(mInflater.inflate(R.layout.fragment_bookdetailcommunity_discussion_load,parent,false));
        }else {
            holder = new NoMoreView(mInflater.inflate(R.layout.fragment_bookdetailcommunity_discussion_nomore,parent,false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(mPostBeans.size() == 0){
            return;
        }
        if(holder instanceof NormalView){
            NormalView view = (NormalView)holder;
            DiscussionListBean.PostBean bean = mPostBeans.get(position);
            view.setName(bean.getAuthor().getNickname())
                    .setRank(bean.getAuthor().getLv())
                    .setContent(bean.getTitle())
                    .setComment(bean.getCommentCount())
                    .setLike(bean.getLikeCount());
            view.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClick(position);
                }
            });
            if(!SharePreferenceUtil.isNoImage()){
                ImageLoader.load(mContext,bean.getAuthor().getAvatar(),view.ivPic);
            }

            Drawable draw = ContextCompat.getDrawable(mContext, R.mipmap.ic_book_review_like);
            draw.setBounds(0, 0, (int)SystemUtils.dp2px(mContext,14), (int)SystemUtils.dp2px(mContext,14));
            view.tvLike.setCompoundDrawables(draw, null, null, null);

            if(bean.getType().equals("vote")){
                Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.ic_notif_vote);
                drawable.setBounds(0, 0, (int)SystemUtils.dp2px(mContext,17), (int)SystemUtils.dp2px(mContext,17));
                view.tvComment.setCompoundDrawables(drawable, null, null, null);
            }else {
                Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.ic_notif_post);
                drawable.setBounds(0, 0, (int)SystemUtils.dp2px(mContext,17), (int)SystemUtils.dp2px(mContext,17));
                view.tvComment.setCompoundDrawables(drawable, null, null, null);
            }
            if(bean.getState().equals("hot")){
                view.tvTime.setVisibility(View.GONE);
                view.tvHot.setVisibility(View.VISIBLE);
                view.tvFavourite.setVisibility(View.GONE);
            }else if(bean.getState().equals("distillate")){
                view.tvTime.setVisibility(View.GONE);
                view.tvHot.setVisibility(View.GONE);
                view.tvFavourite.setVisibility(View.VISIBLE);

            }else {
                view.tvTime.setVisibility(View.VISIBLE);
                view.tvHot.setVisibility(View.GONE);
                view.tvFavourite.setVisibility(View.GONE);
                view.setTime(DateUtil.formatTime(bean.getCreated()));

            }


        }else if( holder instanceof  BottomView){
            mListener.loadMore();
        }
    }

    @Override
    public int getItemCount() {
        return mPostBeans.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        if( position == mPostBeans.size() && mPostBeans.size() > 0 && manager.findFirstVisibleItemPosition() > 1){
            if(mNoMore){
                return TYPE_NOMORE;
            }else{
                return TYPE_BOTTOM;
            }

        }else {
            return TYPE_NORMAL;
        }

    }

    public void setNoMore(boolean noMore){
        this.mNoMore = noMore;
    }

    class NormalView extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_bookdetailcommunity_discussion_pic)
        CircleImageView ivPic;
        @BindView(R.id.tv_bookdetailcommunity_discussion_nickname)
        TextView tvName;
        @BindView(R.id.tv_bookdetailcommunity_discussion_rank)
        TextView tvRank;
        @BindView(R.id.tv_bookdetailcommunity_discussion_content)
        TextView tvContent;
        @BindView(R.id.tv_bookdetailcommunity_discussion_comment)
        TextView tvComment;
        @BindView(R.id.tv_bookdetailcommunity_discussion_like)
        TextView tvLike;
        @BindView(R.id.tv_bookdetailcommunity_discussion_time)
        TextView tvTime;
        @BindView(R.id.tv_bookdetailcommunity_discussion_hot)
        TextView tvHot;
        @BindView(R.id.tv_bookdetailcommunity_discussion_favourite)
        TextView tvFavourite;

        public NormalView(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        public NormalView setName(String name){
            this.tvName.setText(name);
            return this;
        }
        public NormalView setRank(int rank){
            this.tvRank.setText("lv."+rank);
            return this;
        }
        public NormalView setContent(String content){
            this.tvContent.setText(content);
            return this;
        }
        public NormalView setComment(int comment){
            this.tvComment.setText(String.valueOf(comment));
            return this;
        }
        public NormalView setLike(int like){
            this.tvLike.setText(String.valueOf(like));
            return this;
        }
        public NormalView setTime(String time){
            this.tvTime.setText(time);
            return this;
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



    public interface ClickAndLoadListener{
        void loadMore();
        void onClick(int position);
    }

}
