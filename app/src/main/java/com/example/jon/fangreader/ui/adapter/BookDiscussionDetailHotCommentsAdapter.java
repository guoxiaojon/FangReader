package com.example.jon.fangreader.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jon.fangreader.R;
import com.example.jon.fangreader.component.ImageLoader;
import com.example.jon.fangreader.model.bean.CommentListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jon on 2017/2/16.
 */

public class BookDiscussionDetailHotCommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CommentListBean.CommentsBean> mHotComments;
    private Context mContext;

    public BookDiscussionDetailHotCommentsAdapter(List<CommentListBean.CommentsBean> mHotComments, Context mContext) {
        this.mHotComments = mHotComments;
        this.mContext = mContext;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemView(LayoutInflater.from(mContext).inflate(R.layout.activity_discussiondetail_hotcomments_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemView view = (ItemView)holder;
        CommentListBean.CommentsBean bean = mHotComments.get(position);
        ImageLoader.load(mContext,bean.getAuthor().getAvatar(),view.ivPic);
        view.tvFloor.setText("第"+bean.getFloor()+"楼");
        view.tvName.setText(bean.getAuthor().getNickname());
        view.tvRank.setText("lv."+bean.getAuthor().getLv());
        view.tvApprove.setText("❤"+bean.getLikeCount()+"次同感");
        view.tvContent.setText(bean.getContent());
    }

    @Override
    public int getItemCount() {
        return mHotComments.size();
    }
    class ItemView extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_discussiondetail_hotcomments_item_pic)
        CircleImageView ivPic;
        @BindView(R.id.tv_discussiondetail_hotcomments_item_name)
        TextView tvName;
        @BindView(R.id.tv_discussiondetail_hotcomments_item_floor)
        TextView tvFloor;
        @BindView(R.id.tv_discussiondetail_hotcomments_item_rank)
        TextView tvRank;
        @BindView(R.id.tv_discussiondetail_hotcomments_item_approve)
        TextView tvApprove;
        @BindView(R.id.tv_discussiondetail_hotcomments_item_content)
        TextView tvContent;

        public ItemView(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
