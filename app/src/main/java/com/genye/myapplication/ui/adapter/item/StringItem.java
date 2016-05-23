package com.genye.myapplication.ui.adapter.item;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.genye.myapplication.R;
import com.genye.myapplication.model.CodeModel;
import com.genye.myapplication.ui.adapter.base.util.AdapterItem;
import com.genye.myapplication.utils.BusProvider;

import butterknife.Bind;

/**
 * Created by yzd on 2016/5/14.
 */
public class StringItem implements AdapterItem<CodeModel> {

    @Bind(R.id.file_name)
    TextView title;
    @Bind(R.id.btn_create)
    Button btn;
    @Bind(R.id.file_code)
    TextView code;

    @Override
    public int getLayoutResId() {
        return R.layout.item_layout;
    }

    @Override
    public void initItemViews(View root) {

    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onUpdateViews(final CodeModel model, int position) {
        title.setText(model.getName());
        if (model.iscomplete()) {
            if (model.getCode().equals("error")) {
                btn.setText("生成失败");
                btn.setTextColor(0xffff0000);
                btn.setEnabled(false);
            } else{
                btn.setText("已生成");
                btn.setTextColor(0xff00ff00);
                btn.setEnabled(false);
            }
        } else {
            btn.setText("生成");
            btn.setEnabled(true);
            btn.setTextColor(0xff696969);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BusProvider.getInstance().post(model);
                }
            });
        }
        if (model.getCode() != null) {
            code.setText(String.format("code:%s",model.getCode()));
        } else {
            code.setText("code:");
        }

    }
}
