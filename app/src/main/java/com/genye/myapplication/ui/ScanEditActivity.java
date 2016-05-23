package com.genye.myapplication.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.genye.myapplication.Constants;
import com.genye.myapplication.R;
import com.genye.myapplication.base.BaseAppCompatActivity;
import com.genye.myapplication.db.LocalService;
import com.genye.myapplication.model.AccountModel;
import com.genye.myapplication.utils.BitmapUtils;
import com.genye.myapplication.utils.FileUtils;
import com.genye.myapplication.utils.QRCodeUtils;
import com.genye.myapplication.utils.ThreadPoolManager;
import com.genye.myapplication.utils.ToastUtil;
import com.genye.myapplication.zxing.activity.MipcaActivityCapture;

import java.util.Arrays;

import butterknife.Bind;

public class ScanEditActivity extends BaseAppCompatActivity implements View.OnClickListener {

    //edit view
    @Bind(R.id.tv_alipay_code)
    TextView tvAlipayCode;
    @Bind(R.id.btn_alipay_scan)
    Button btnAlipayScanf;
    @Bind(R.id.tv_wx_code)
    TextView tvWxCode;
    @Bind(R.id.btn_wx_scan)
    Button btnWxScan;
    @Bind(R.id.et_shop_name)
    TextInputEditText etShopName;
    @Bind(R.id.et_alipay_account)
    TextInputEditText etAlipayAccount;
    @Bind(R.id.et_wx_account)
    TextInputEditText etWxAccount;
    /*@Bind(R.id.sp_type)
    AppCompatSpinner spType;*/
    @Bind(R.id.sp_mode)
    AppCompatSpinner spModel;
    @Bind(R.id.btn_create)
    Button btnCreate;

    //result view
    @Bind(R.id.iv_alipay_img)
    ImageView ivAlipayImg;
    @Bind(R.id.iv_wx_img)
    ImageView ivWxImg;
    @Bind(R.id.btn_reset)
    Button btnReset;
    @Bind(R.id.btn_continue)
    Button btnContinue;
    @Bind(R.id.ll_result)
    ViewGroup vgResult;
    @Bind(R.id.rl_edit)
    ViewGroup vgEdit;

    String[] types = new String[]{"支付宝", "微信"};
    String[] modes = new String[]{"A", "B"};
    AccountModel model;

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_scan_edit;
    }

    @Override
    public void initPageView() {
      /*  ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, Arrays.asList(types));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(adapter);*/
        model = new AccountModel();

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Arrays.asList(modes));
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spModel.setAdapter(adapter1);
    }

    @Override
    public void initPageViewListener() {
        btnContinue.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnAlipayScanf.setOnClickListener(this);
        btnWxScan.setOnClickListener(this);
        spModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_alipay_scan:
                this.startActivityForResult(new Intent(this, MipcaActivityCapture.class), 0);
                break;
            case R.id.btn_wx_scan:
                this.startActivityForResult(new Intent(this, MipcaActivityCapture.class), 1);
                break;
            case R.id.btn_create:
                model.setAlipayCode(tvAlipayCode.getText().toString());
                model.setAlipayAccount(etAlipayAccount.getText().toString());
                model.setWxCode(tvWxCode.getText().toString());
                model.setWxAccount(etWxAccount.getText().toString());
                model.setShopName(etShopName.getText().toString());
                model.setType(spModel.getSelectedItem().toString());

                if (model.getAlipayCode().equals("") && model.getWxCode().equals("")) {
                    ToastUtil.showShort(this, "二维码内容为空，请重新扫描");
                    return;
                }

                if (model.getShopName().equals("")) {
                    ToastUtil.showShort(this, "档口名和档口地址不能为空");
                    return;
                }

                showLoadingDialog("loading", true);
                ThreadPoolManager.getInstance().startTask(new Runnable() {
                    @Override
                    public void run() {
                        startCreateTask(model);
                    }
                });
                break;
            case R.id.btn_reset:
                vgResult.setVisibility(View.GONE);
                vgEdit.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_continue:
                model.clearn();
                vgResult.setVisibility(View.GONE);
                vgEdit.setVisibility(View.VISIBLE);
                tvAlipayCode.setText("");
                tvWxCode.setText("");
                etShopName.setText("");
                etAlipayAccount.setText("");
                etWxAccount.setText("");
                spModel.setSelection(0);
                break;
        }
    }

    private void startCreateTask(AccountModel accountModel) {
        int logo;
        String title;
        final Bitmap alipayCode;
        final Bitmap wxCode;
        if (!accountModel.getAlipayCode().equals("")) {
            logo = R.drawable.z;
            title = String.format("%sZ%s", accountModel.getShopName(), accountModel.getType());
            alipayCode = createCode(accountModel.getAlipayCode(), title, logo);
        } else {
            alipayCode = null;
        }
        if (!accountModel.getWxCode().equals("")) {
            logo = R.drawable.w;
            title = String.format("%sW%s", accountModel.getShopName(), accountModel.getType());
            wxCode = createCode(accountModel.getWxCode(), title, logo);
        } else {
            wxCode = null;
        }
        saveInfo(accountModel);
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                dismissLoadingDialog();
                vgResult.setVisibility(View.VISIBLE);
                vgEdit.setVisibility(View.GONE);
                if (alipayCode != null) {
                    ivAlipayImg.setImageBitmap(alipayCode);
                } else {
                    ivAlipayImg.setImageResource(R.mipmap.ic_launcher);
                }
                if (wxCode != null) {
                    ivWxImg.setImageBitmap(wxCode);
                } else {
                    ivWxImg.setImageResource(R.mipmap.ic_launcher);
                }
            }
        });
    }

    private void saveInfo (AccountModel accountModel) {
        LocalService.getInstance(this).open();
        Cursor cursor = LocalService.getInstance(this).findByShopName(accountModel.getShopName());
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            accountModel.setId(id);
            accountModel.checkType(cursor.getString(4));
            LocalService.getInstance(this).updateAcount(accountModel);
        } else {
            LocalService.getInstance(this).addAcount(accountModel);
        }
        if (cursor != null) cursor.close();
        LocalService.getInstance(this).close();
    }

    private Bitmap createCode(String code, String title, int logo) {
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), logo);
        try {
            Bitmap qrCode = QRCodeUtils.qrEncode(code, bitmap, 500, 500);
            int size;
            if (title.length() > 0 && title.length() < 26) {
                size = 30;
            } else {
                size = 25;
            }
            Bitmap text = BitmapUtils.drawTextToBitmap(title, 450, size, size, Typeface.DEFAULT);
            String path = FileUtils.getChileFile(Constants.RESULT).getAbsolutePath();
            Bitmap result = BitmapUtils.composeImg(qrCode, text);
            FileUtils.saveBitmap(result, path, title + ".jpg");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 0: {
                    String msg = data.getStringExtra("msg");
                    tvAlipayCode.setText(msg);
                    break;
                }
                case 1: {
                    String msg = data.getStringExtra("msg");
                    tvWxCode.setText(msg);
                    break;
                }

            }
        }
    }

    Handler handler;

    public Handler getHandler() {
        synchronized (this) {
            if (handler == null) {
                handler = new Handler(getMainLooper());
            }
        }
        return handler;
    }
}
