package com.genye.myapplication.ui.adapter.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.genye.myapplication.ui.adapter.base.util.AdapterItem;
import com.genye.myapplication.ui.adapter.base.util.IAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Jack Tony
 * @date 2015/11/29
 */
public abstract class CommonPagerAdapter<T> extends BasePagerAdapter<View> implements IAdapter<T> {

    private List<T> mData;

    private LayoutInflater mInflater;

    private boolean mIsLazy = false;

    public CommonPagerAdapter(@Nullable List<T> data) {
       this(data, false);
    }

    public CommonPagerAdapter(@Nullable List<T> data, boolean isLazy) {
        if (data == null) data = new ArrayList<>();
        mData = data;
        mIsLazy = isLazy;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @NonNull
    @Override
    protected View getViewFromItem(View item, int position) {
        return item;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        View view = super.instantiateItem(container, position);
        if (!mIsLazy) {
           initItem(position, view);
        }
        return view;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, @NonNull Object object) {
        if (mIsLazy && object != currentItem) {
            // 如果是懒加载，那么这里应该放置数据更新的操作
            initItem(position, (View) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    protected View onCreateItem(ViewPager viewPager, int position) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(viewPager.getContext());
        }
        AdapterItem<T> item = onCreateItem(getItemType(position));
        View view = mInflater.inflate(item.getLayoutResId(), null);
      //  view.setTag(R.id.tag_item, item); // 万一你要用到这个item可以通过这个tag拿到
        item.initItemViews(view);
        item.onSetViews();
        return view;
    }

    public void setIsLazy(boolean isLazy) {
        mIsLazy = isLazy;
    }

    /**
     * 强烈建议返回string,int,bool类似的基础对象做type
     */
    @Override
    public int getItemType(int position) {
       return 0;
    }

    @Override
    public void setData(@NonNull List<T> data) {
        mData = data;
    }

    @Override
    public List<T> getData() {
        return mData;
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }


    @Override
    public void addItem(@NonNull T item) {

    }

    @Override
    public void addItem(int position, @NonNull T item) {

    }

    @Override
    public void addAll(int position, List<T> data) {

    }

    @Override
    public void addAll(List<T> data) {

    }

    @Override
    public void itemsClear() {

    }

    @Override
    public T removeItem(int position) {
        return null;
    }

    @Override
    public boolean removeItem(T model) {
        return false;
    }

    @Override
    public void removeSubList(int startPosition, int count) {

    }

    @Override
    public void swap(int fromPosition, int toPosition) {

    }

    @SuppressWarnings("unchecked")
    private void initItem(int position, View view) {
      //  AdapterItem item = (AdapterItem) view.getTag(R.id.tag_item);
        //item.onUpdateViews(mData.get(position), position);
    }

}
