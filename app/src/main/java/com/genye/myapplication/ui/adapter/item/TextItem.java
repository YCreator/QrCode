package com.genye.myapplication.ui.adapter.item;

import android.view.View;
import android.widget.TextView;

import com.genye.myapplication.R;
import com.genye.myapplication.ui.adapter.base.util.AdapterItem;

import butterknife.Bind;

/**
 * Created by yzd on 2016/5/18.
 */
public class TextItem implements AdapterItem<String> {

    @Bind(R.id.tv_text)
    TextView text;

    @Override
    public int getLayoutResId() {
        return R.layout.string_item;
    }

    @Override
    public void initItemViews(View itemView) {

    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onUpdateViews(String model, int position) {
        text.setText(model);
    }
}
