package com.example.jon.fangreader.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jon.fangreader.R;
import com.example.jon.fangreader.component.ACache;
import com.example.jon.fangreader.model.bean.BookTocBean;

import java.util.List;

/**
 * Created by jon on 2017/2/12.
 */

public class PopListAdapter extends BaseAdapter {
    private Context mContext;
    private List<BookTocBean.MixToc.Chapter> mChapers;
    private String mBookId;
    private int mCurrChapter;


    public PopListAdapter(Context context,String bookId, int currChapter,List<BookTocBean.MixToc.Chapter> chapters){
        this.mChapers = chapters;
        this.mContext = context;
        this.mBookId = bookId;
        this.mCurrChapter = currChapter;


    }
    @Override
    public int getCount() {
        return mChapers.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            View view = LayoutInflater.from(mContext).inflate(R.layout.activity_read_poplist_item,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.tv_read_poplist_item_title);
            viewHolder.imageView = (ImageView)view.findViewById(R.id.iv_read_poplist_item_img);
            convertView = view;
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(mChapers.get(position).getTitle());
        if(ACache.getChapterForRead(mBookId,mChapers.get(position).getLink()) == null){
            viewHolder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_toc_item_normal));
        }else {
            viewHolder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_toc_item_download));

        }
        if(position == mCurrChapter){
            viewHolder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_toc_item_activated));

        }
        return convertView;
    }

    public void setCurrChapter(int currChapter){
        this.mCurrChapter = currChapter;
    }

    class ViewHolder {
        TextView textView;
        ImageView imageView;


    }

    public interface OnItemClickListener {
        void onClick(int position);
    }
}
