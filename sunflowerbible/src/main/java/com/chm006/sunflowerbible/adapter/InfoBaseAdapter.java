package com.chm006.sunflowerbible.adapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * listView列表加载页面
 * Created by chenmin on 2016/8/4.
 */
public abstract class InfoBaseAdapter extends BaseAdapter {
    private List<Object> data;
    private LayoutInflater inflater;
    private int layoutId;

    public InfoBaseAdapter(List<Object> data, LayoutInflater inflater, int layoutId) {
        this.data = data;
        this.inflater = inflater;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void notifyDataSetChanged(List<Object> data) {
        this.data = data;
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layoutId, null);
        }
        ViewHolder vh = ViewHolder.getViewHolder(convertView);
        Object item = getItem(position);

        setData(vh, item);

        return convertView;
    }

    /**
     * 设置item数据
     * @param vh
     * @param itemData
     */
    public abstract void setData(ViewHolder vh, Object itemData);

    public static class ViewHolder {
        private SparseArray<View> viewHolder;
        private View view;

        public static ViewHolder getViewHolder(View view) {
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            }
            return viewHolder;
        }

        private ViewHolder(View view) {
            this.view = view;
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }

        public <T extends View> T get(int id) {
            View childView = viewHolder.get(id);
            if (childView == null) {
                childView = view.findViewById(id);
                viewHolder.put(id, childView);
            }
            return (T) childView;
        }

        public View getConvertView() {
            return view;
        }

        public TextView getTextView(int id) {
            return get(id);
        }

        public Button getButton(int id) {
            return get(id);
        }

        public ImageView getImageView(int id) {
            return get(id);
        }

        public void setTextView(int id, CharSequence charSequence) {
            getTextView(id).setText(charSequence);
        }
    }
}
