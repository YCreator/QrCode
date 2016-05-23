package com.genye.myapplication.ui;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.genye.myapplication.Constants;
import com.genye.myapplication.R;
import com.genye.myapplication.base.BaseAppCompatActivity;
import com.genye.myapplication.db.OrmDataBaseHelper;
import com.genye.myapplication.ui.adapter.base.BaseLvAdapter;
import com.genye.myapplication.ui.adapter.base.util.AdapterItem;
import com.genye.myapplication.ui.adapter.item.TextItem;
import com.genye.myapplication.utils.DatabaseDump;
import com.genye.myapplication.utils.FileUtils;
import com.genye.myapplication.utils.ThreadPoolManager;
import com.genye.myapplication.utils.ToastUtil;
import com.genye.myapplication.utils.ZipUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

public class ResultActivity extends BaseAppCompatActivity implements View.OnClickListener {

    @Bind(R.id.list)
    ListView listView;
    @Bind(R.id.btn_zip)
    Button btnZip;
    @Bind(R.id.btn_del)
    Button btnDel;
    @Bind(R.id.tv_count)
    TextView tvCount;

    private BaseLvAdapter<String> adapter;

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_result;
    }

    @Override
    public void initPageView() {

    }

    @Override
    public void initPageViewListener() {
        btnDel.setOnClickListener(this);
        btnZip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_zip:
                if (adapter != null && adapter.getData().size() > 0) {
                    showLoadingDialog("loading", true);
                    ThreadPoolManager.getInstance().startTask(new Runnable() {
                        @Override
                        public void run() {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd");
                            String zipFile = FileUtils.getAppMainFile().getAbsolutePath() + "/" + format.format(new Date()) + ".zip";
                            try {
                                ZipUtil.zip(FileUtils.getChileFile(Constants.RESULT).getAbsolutePath(), zipFile);
                                getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissLoadingDialog();
                                        ToastUtil.showShort(ResultActivity.this, "压缩成功");
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                                getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissLoadingDialog();
                                        ToastUtil.showShort(ResultActivity.this, "压缩失败");
                                    }
                                });
                            }
                        }
                    });
                }
                break;
            case R.id.btn_del:
                getDialog().show();
                break;
        }
    }

    Handler handler ;

    public Handler getHandler() {
        synchronized (this) {
            if (handler == null) {
                handler = new Handler(getMainLooper());
            }
        }
        return handler;
    }

    @Override
    protected void onResume() {
        super.onResume();
        notifyAdapter();
    }

    private void notifyAdapter() {
        if (adapter == null) {
            adapter = new BaseLvAdapter<String>(checkFiles()) {
                @NonNull
                @Override
                public AdapterItem<String> onCreateItem(int viewType) {
                    return new TextItem();
                }
            };
            listView.setAdapter(adapter);
        } else {
            adapter.setData(checkFiles());
        }
    }

    private List<String> checkFiles() {
        String[] filesName = FileUtils.getChileFile(Constants.RESULT).list();
        tvCount.setText(String.valueOf(filesName.length));
        return Arrays.asList(filesName);
    }

    private AlertDialog getDialog() {
        return new AlertDialog.Builder(this)
                .setTitle("是否删除目前生成的所有二维码文件")
                .setNegativeButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showLoadingDialog("loading",true);
                        ThreadPoolManager.getInstance().startTask(new Runnable() {
                            @Override
                            public void run() {
                                FileUtils.deleteFiles(FileUtils.getChileFile(Constants.RESULT));
                                getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissLoadingDialog();
                                        notifyAdapter();
                                    }
                                });
                            }
                        });
                    }
                })
                .setPositiveButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_scan:
                launchActivity(ScanEditActivity.class);
                break;
            case R.id.action_export:
                DatabaseDump dump = new DatabaseDump(OrmDataBaseHelper.getHelper(this).getReadableDatabase(),"");//DatabaseManager.getmDatabaseHelper().getReadableDatabase(), "");
                dump.exportData();
                ToastUtil.showShort(this, "成功导出数据，位置："+FileUtils.getChileFile(Constants.EXCEL).getAbsolutePath());
                break;
            case R.id.action_create_qr:
                launchActivity(MainActivity.class);
                break;
            /*case R.id.action_test:
                TestModel model = new TestModel();
                model.setName("hi");
                model.setDateTime(new Date());
                try {
                    TestDao dao = new TestDao(this);
                    GenericRawResults<TestModel> result =  dao.getDao().queryRaw("", new RawRowMapper<TestModel>() {
                        @Override
                        public TestModel mapRow(String[] strings, String[] strings1) throws SQLException {
                            TestModel model = new TestModel();
                            model.setId(Integer.valueOf(strings1[0]));
                            model.setName(String.valueOf(strings1[1]));
                            model.setDateTime(new Date(Long.valueOf(strings1[2])));
                            return model;
                        }
                    });
                   // List<TestModel> list = new ArrayList<>();
                    Iterator<TestModel> its = result.iterator();
                    while (its.hasNext()) {
                        TestModel mo = its.next();
                        TLog.log(mo.toString());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }
}
