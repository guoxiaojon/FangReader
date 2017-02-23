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
import com.example.jon.fangreader.utils.DateUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jon on 2017/2/16.
 */

public class BookDiscussionDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<CommentListBean.CommentsBean> mComments;
    private RecyclerView.ViewHolder mHeader;
    private LayoutInflater mInflater;
    private LoadMoreListener mListener;

    private static final int HEADER = 0x1;
    private static final int NORMAL = 0x2;
    private static final int BOTTOM = 0x3;
    private static final int NOMORE = 0x4;

    private boolean mNoMore = true;

    public BookDiscussionDetailAdapter(Context mContext, List<CommentListBean.CommentsBean> mComments, RecyclerView.ViewHolder mHeader, LoadMoreListener listener) {
        this.mContext = mContext;
        this.mComments = mComments;
        this.mHeader = mHeader;
        this.mInflater = LayoutInflater.from(mContext);
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == HEADER){
            return mHeader;
        }else if(viewType == NORMAL){
            return new NormalView(mInflater.inflate(R.layout.activity_discussiondetail_item,parent,false));
        }else if(viewType == BOTTOM){
            return new BottomView(mInflater.inflate(R.layout.fragment_bookdetailcommunity_discussion_load,parent,false));
        }else {
            return new NoMoreView(mInflater.inflate(R.layout.fragment_bookdetailcommunity_discussion_nomore,parent,false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof NormalView){
            CommentListBean.CommentsBean bean = mComments.get(position-1);
            NormalView view = (NormalView)holder;
            ImageLoader.load(mContext,bean.getAuthor().getAvatar(),view.ivPic);
            view.tvName.setText(bean.getAuthor().getNickname());
            view.tvFloor.setText("第"+bean.getFloor()+"楼");
            view.tvRank.setText("lv."+bean.getAuthor().getLv());
            view.tvTime.setText(DateUtil.formatTime(bean.getCreated()));
            view.tvContent.setText(bean.getContent());
            if(bean.getReplyTo() != null){
                view.tvReply.setVisibility(View.VISIBLE);
                view.tvReplyName.setVisibility(View.VISIBLE);
                view.tvReplyFloor.setVisibility(View.VISIBLE);
                view.tvReplyName.setText(bean.getReplyTo().getAuthor().getNickname());
                view.tvReplyFloor.setText("("+bean.getReplyTo().getFloor()+"楼)");
            }else {
                view.tvReply.setVisibility(View.GONE);
                view.tvReplyName.setVisibility(View.GONE);
                view.tvReplyFloor.setVisibility(View.GONE);
            }

        }else if(holder instanceof BottomView){
            mListener.loadMore();
        }

    }

    public void setNoMore(boolean noMore){
        this.mNoMore = noMore;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return HEADER;
        }else {
            if(position == mComments.size()+1){
                if(position == 1)
                    return NOMORE;
                if(mNoMore){
                    return NOMORE;

                }else {
                    return BOTTOM;
                }
            }else {
                return NORMAL;
            }
        }
    }
    private boolean mInIt = true;
    public void cancelInIt(){
        this.mInIt = false;
    }
    @Override
    public int getItemCount() {
        if(mInIt)
            return 0;
        return mComments.size() + 2;
    }

    class NormalView extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_discussiondetail_item_pic)
        CircleImageView ivPic;
        @BindView(R.id.tv_discussiondetail_item_floor)
        TextView tvFloor;
        @BindView(R.id.tv_discussiondetail_item_name)
        TextView tvName;
        @BindView(R.id.tv_discussiondetail_item_rank)
        TextView tvRank;
        @BindView(R.id.tv_discussiondetail_item_time)
        TextView tvTime;
        @BindView(R.id.tv_discussiondetail_item_content)
        TextView tvContent;
        @BindView(R.id.tv_discussiondetail_item_reply)
        TextView tvReply;
        @BindView(R.id.tv_discussiondetail_item_replyname)
        TextView tvReplyName;
        @BindView(R.id.tv_discussiondetail_item_replyfloor)
        TextView tvReplyFloor;

        public NormalView(View itemView) {
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

    public interface LoadMoreListener{
        void loadMore();
    }


}
