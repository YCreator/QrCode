package com.genye.myapplication.ui.adapter.base;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * @author Jack Tony
 * @date 2015/11/21
 * <p/>
 * 如果调用{@link #notifyDataSetChanged()}来更新，
 * 它会自动调用{@link #instantiateItem(ViewGroup, int)}重新new出需要的item，算是完全初始化一次。
 */
abstract class BasePagerAdapter<T> extends PagerAdapter {
    public static final String TAG = BasePagerAdapter.class.getSimpleName();

    protected T currentItem;

    private boolean isNotifying = false;

    /**
     * 这的cache的最大大小是：type x pageSize
     */
    private final PagerCache<T> mCache;

    public BasePagerAdapter() {
        mCache = new PagerCache<>();
    }

    /**
     * 注意：这里必须是view和view的比较
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == getViewFromItem((T) obj, 0);
    }

    @Override
    public T instantiateItem(ViewGroup container, int position) {
        T item = mCache.getItem(getItemType(position));
        if (item == null) {
            item = onCreateItem((ViewPager) container, position);
        }
        // 通过item得到将要被add到viewpager中的view
        View view = getViewFromItem(item, position);
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        container.addView(view);
        return item;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (object != currentItem) {
            // 可能是currentItem不等于null，可能是二者不同
            currentItem = (T) object;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        T item = (T) object;
        // 现在通过item拿到其中的view，然后remove掉
        container.removeView(getViewFromItem(item, position));
        if (!isNotifying) {
            Object type = getItemType(position);
            mCache.putItem(type, item);
        }

    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void notifyDataSetChanged() {
        isNotifying = true;
        super.notifyDataSetChanged();
        isNotifying = false;
    }

    public int getItemType(int position) {
        return -1; // default
    }

    public T getCurrentItem() {
        return currentItem;
    }

    protected PagerCache<T> getCache() {
        return mCache;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 交给子类的实现
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 这里要实现一个从item拿到view的规则
     *
     * @param item     包含view的item对象
     * @param position item所处的位置
     * @return item中的view对象
     */

    protected abstract
    @NonNull
    View getViewFromItem(T item, int position);

    /**
     * 当缓存中无法得到所需item时才会调用
     *
     * @return 需要放入容器的view
     */
    protected abstract T onCreateItem(ViewPager viewPager, int position);

    ///////////////////////////////////////////////////////////////////////////
    // 缓存类
    ///////////////////////////////////////////////////////////////////////////

    public static class PagerCache<T> {

        private Map<Object, Queue<T>> mCacheMap;

        public PagerCache() {
            mCacheMap = new ArrayMap<>();
        }

        /**
         * @param type item key
         * @return cache中的item，如果拿不到就返回null
         */
        public T getItem(Object type) {
            Queue<T> queue = mCacheMap.get(type);
            return queue != null ? queue.poll() : null;
        }

        /**
         * @param type item's type
         */
        public void putItem(Object type, T item) {
            Queue<T> queue;
            if ((queue = mCacheMap.get(type)) == null) {
                queue = new LinkedList<>();
                mCacheMap.put(type, queue);
            }
            queue.offer(item);
        }
    }


}
