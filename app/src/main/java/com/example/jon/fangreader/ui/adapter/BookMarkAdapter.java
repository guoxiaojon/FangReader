package com.example.jon.fangreader.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jon.fangreader.R;
import com.example.jon.fangreader.model.bean.BookMark;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jon on 2017/2/12.
 */

public class BookMarkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BookMark> mBookMarks;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    public BookMarkAdapter(Context context,List<BookMark> marks,OnItemClickListener listener){
        this.mContext = context;
        this.mBookMarks = marks;
        mOnItemClickListener = listener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemView(LayoutInflater.from(mContext).inflate(R.layout.activity_read_bookmark_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((ItemView)holder).textView.setText(mBookMarks.get(position).getmDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBookMarks.size();
    }
    class ItemView extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_read_bottomlist_bookmark_list_item)
        TextView textView;
        public ItemView(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
    public interface OnItemClickListener{
        void onClick(int position);
    }
}
