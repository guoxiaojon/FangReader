package com.example.jon.fangreader.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jon.fangreader.R;
import com.example.jon.fangreader.component.ImageLoader;
import com.example.jon.fangreader.model.bean.RecommendBean;
import com.example.jon.fangreader.utils.DateUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

/**
 * Created by jon on 2017/1/4.
 */

public class BookShelfAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<RecommendBean.Book> mBooks;
    private OnClickListener mOnClickListener;

    private LayoutInflater mInflater;

    private static final int ITEM_DATA = 0x1;
    private static final int ITEM_BOTTOM = 0x2;

    public BookShelfAdapter(Context context, List<RecommendBean.Book> books,OnClickListener onClickListener){
        this.mContext = context;
        this.mBooks = books;
        this.mOnClickListener = onClickListener;
        mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getItemViewType(int position) {
        if(position == mBooks.size()){
            return ITEM_BOTTOM;
        }
        return ITEM_DATA;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_DATA){
            return new ItemView(mInflater.inflate(R.layout.fragment_bookshelf_item,parent,false));
        }
        return new BottomView(mInflater.inflate(R.layout.fragment_bookshelf_item_bottom,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ItemView){
            ItemView view = (ItemView)holder;
            ImageLoader.load(mContext,mBooks.get(position).getCover(),view.ivPic);
            view.tvTitle.setText(mBooks.get(position).getTitle());
            view.tvDescription.setText(DateUtil.formatTime(mBooks.get(position).getUpdated())+":"+mBooks.get(position).getLastChapter());

            if(mBooks.get(position).isTop()){
                view.ivSetTop.setVisibility(View.VISIBLE);
            }else {
                view.ivSetTop.setVisibility(View.INVISIBLE);
            }

            if(mBooks.get(position).isReaded()){
                view.ivUpdate.setVisibility(View.INVISIBLE);
            }else {
                view.ivUpdate.setVisibility(View.VISIBLE);
            }

            if(mBooks.get(position).isSelectState()){
                view.cbSelect.setVisibility(View.VISIBLE);
                view.cbSelect.setChecked(mBooks.get(position).isSelected());

            }else {
                view.cbSelect.setVisibility(GONE);
            }

            view.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    mBooks.get(position).setSelected(b);
                }
            });

            view.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnClickListener.onLongClick(mBooks.get(position));
                    return true;
                }
            });
            view.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnClickListener.onClick(mBooks.get(position),position);
                }
            });
        }else {
            ((BottomView)holder).view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnClickListener.onClick(null,-1);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return mBooks.size()+1;
    }

    class ItemView extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_pic)
        ImageView ivPic;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_description)
        TextView tvDescription;
        @BindView(R.id.cb_select)
        CheckBox cbSelect;
        @BindView(R.id.iv_update)
        ImageView ivUpdate;
        @BindView(R.id.iv_settop)
        ImageView ivSetTop;

        View view;


        public ItemView(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            view = itemView;
        }
    }

    class BottomView extends RecyclerView.ViewHolder{
        View view;

        public BottomView(View itemView) {
            super(itemView);
            view = itemView;
        }
    }


    public interface OnClickListener{
        void onLongClick(RecommendBean.Book book);
        void onClick(RecommendBean.Book book,int position);
    }
}
