package com.chm006.library.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * RecyclerView适配器
 * 实现了RecyclerView添加点击事件（参考ListView的条目点击事件）
 * Created by chenmin on 2016/9/27.
 */
public abstract class BaseRVAdapter extends RecyclerView.Adapter<BaseRVAdapter.RVHolder> implements View.OnClickListener {
    public List<?> list;
    private Context context;
    private AdapterView.OnItemClickListener onItemClickListener;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public BaseRVAdapter(Context context, List<?> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(onCreateViewLayoutID(viewType), null);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return new RVHolder(view);
    }

    /**
     * 初始化view控件的抽象方法
     *
     * @param viewType 填充布局的id
     * @return 填充布局的id
     */
    public abstract int onCreateViewLayoutID(int viewType);

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onViewRecycled(final RVHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(final RVHolder holder, final int position) {
        onBindViewHolder(holder.getViewHolder(), position);
        if (onItemClickListener != null) {
            holder.getViewHolder().getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(null, v, holder.getPosition(), holder.getItemId());
                }
            });
        }
    }

    public abstract void onBindViewHolder(ViewHolder holder, int position);

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //模拟ListView的OnItemClickListener
    public interface OnRecyclerViewItemClickListener {
        /**
         * @param view 被点击的Item对象
         * @param data 填充Item的Bean类对象
         */
        void onItemClick(View view, Object data);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, v.getTag());
        }
    }

    //RecyclerView的Holder
    public class RVHolder extends RecyclerView.ViewHolder {
        private ViewHolder viewHolder;

        public RVHolder(View itemView) {
            super(itemView);
            viewHolder = ViewHolder.getViewHolder(itemView);
        }

        public ViewHolder getViewHolder() {
            return viewHolder;
        }
    }

    //普通的Holder
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
