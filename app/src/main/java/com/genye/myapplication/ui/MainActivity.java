package com.genye.myapplication.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.genye.myapplication.Constants;
import com.genye.myapplication.R;
import com.genye.myapplication.base.BaseAppCompatActivity;
import com.genye.myapplication.model.CodeModel;
import com.genye.myapplication.ui.adapter.base.BaseRvAdapter;
import com.genye.myapplication.ui.adapter.base.util.AdapterItem;
import com.genye.myapplication.ui.adapter.decorator.DividerItemDecoration;
import com.genye.myapplication.ui.adapter.item.StringItem;
import com.genye.myapplication.utils.BusProvider;
import com.genye.myapplication.utils.FileUtils;
import com.genye.myapplication.utils.ImageUtils;
import com.genye.myapplication.utils.QRCodeUtils;
import com.genye.myapplication.utils.RGBLuminanceSource;
import com.genye.myapplication.utils.ThreadPoolManager;
import com.genye.myapplication.utils.ToastUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.HybridBinarizer;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Hashtable;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends BaseAppCompatActivity implements View.OnClickListener {

    @Bind(R.id.reciclerView)
    RecyclerView recyclerView;
    @Bind(R.id.btn_w_check)
    Button btnWeixin;
    @Bind(R.id.btn_z_check)
    Button btnAlipay;
    @Bind(R.id.btn_create)
    Button btnCreate;

    BaseRvAdapter<CodeModel> adapter;

    private String fPath = "";

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public void initPageView() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void initPageViewListener() {
        btnWeixin.setOnClickListener(this);
        btnAlipay.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        File file = null;
        switch (v.getId()) {
            case R.id.btn_w_check:
                file = FileUtils.getChileFile(Constants.W_PATH);
                fPath = Constants.W_PATH;
                checkDirectory(file, btnWeixin, "二维码目录");
                break;
            case R.id.btn_create:
                createAll();
                break;
        }

    }

    private void checkDirectory(File file, @NonNull Button btn, String txt) {
        List<CodeModel> ls = new ArrayList<>();
        if (file != null && file.isDirectory()) {
            String[] listName = file.list();
            btn.setText(String.format("%s(%s)",txt,listName.length));
            for (String s : listName) {
                CodeModel cm = new CodeModel();
                cm.setName(s);
                cm.setIscomplete(false);
                ls.add(cm);
            }
        }
        if (adapter == null) {
            adapter = new BaseRvAdapter<CodeModel>(ls) {

                @NonNull
                @Override
                public AdapterItem<CodeModel> onCreateItem(int viewType) {
                    return new StringItem();
                }
            };
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setData(ls);
        }
    }

    @Subscribe
    public void crateImg(final CodeModel path) {
      //  codeModel = path;
        showLoadingDialog("loading",true);
        ThreadPoolManager.getInstance().startTask(new Runnable() {
            @Override
            public void run() {
                doTask(path, fPath, notifySingle());
            }
        });
    }

    private void createAll() {
        if (adapter == null) return;
        showLoadingDialog("loading",true);
        ThreadPoolManager.getInstance().startTask(new Runnable() {
            @Override
            public void run() {
                i = 0;
                for (CodeModel path : adapter.getData()) {
                    doTask(path, fPath, notifyToast());
                }
                getHandler().post(notifySingle());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    /*
    public String decoderQRCode(String imgPath) {
        // QRCode 二维码图片的文件
        File imageFile = new File(imgPath);
        BufferedImage bufImg = null;
        String content = null;
        try {
            bufImg = ImageIO.read(imageFile);
            QRCodeDecoder decoder = new QRCodeDecoder();
            content = new String(decoder.decode(new TwoDimensionCodeImage(bufImg)), "utf-8");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (DecodingFailedException dfe) {
            System.out.println("Error: " + dfe.getMessage());
            dfe.printStackTrace();
        }
        return content;
    }*/

    public Result checkCode(Bitmap scanBitmap, float width, float height) {
        Hashtable<DecodeHintType, Object> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.allOf(BarcodeFormat.class));
      /*   BitmapFactory.Options options = new BitmapFactory.Options();
       options.inJustDecodeBounds = true; // 先获取原大小
        scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小*/
        scanBitmap = ImageUtils.zoomBitmap(scanBitmap, width, height);
       /* int sampleSize = (int) (options.outHeight / (float) 200);

        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);*/
        RGBLuminanceSource source;
        if (scanBitmap != null) {
            Log.i("size", scanBitmap.getWidth()+"_"+ scanBitmap.getHeight());
            source = new RGBLuminanceSource(scanBitmap);
        } else {
            Log.i("size", "null");
            return null;
        }

        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
       // QRCodeReader reader = new QRCodeReader();
        MultiFormatReader mMultiFormatReader = new MultiFormatReader();
        mMultiFormatReader.setHints(hints);

        try {
            Log.i("reader", "try");
           //return reader.decode(bitmap1, hints);
            return mMultiFormatReader.decode(bitmap1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("reader", "catch");
        return null;
    }

    private Bitmap composeImg(Bitmap code, Bitmap text) {
        Bitmap newbmp = Bitmap.createBitmap(500, 560, Bitmap.Config.RGB_565);
        Canvas cv = new Canvas(newbmp);
        cv.drawColor(Color.WHITE);
        cv.drawBitmap(code, 0, 0, null);//在 0，0坐标开始画入bg
        cv.drawBitmap(text, 20, 510, null);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();//存储
        return newbmp;
    }

    /**
     * 将文本变为bitmap
     * @param message
     * @param width
     * @param height
     * @param typeface
     * @return
     */
    private Bitmap drawTextToBitmap(String message, int width, int height
            , float textSize, Typeface typeface) {
        Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap,0,0,null);
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);
        //textPaint.setFakeBoldText(true);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(typeface);
        StaticLayout sl  = new StaticLayout(message
                ,textPaint,bitmap.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        canvas.translate(0,0);
        sl.draw(canvas);
        return bitmap;
    }

    private void doTask(CodeModel model, String fpath, Runnable runnable) {
        String fname = model.getName();
        int logo;
        if (fname.contains("ZA.") || fname.contains("ZB.")
                || fname.contains("zA.") || fname.contains("zB.")
                || fname.contains("Za.") || fname.contains("Zb.")
                || fname.contains("za.") || fname.contains("zb.")) {
            logo = R.drawable.z;
        } else {
            logo = R.drawable.w;
        }
        String mPath = FileUtils.getChileFile(fpath).getAbsolutePath()+"/"+ fname;
        Bitmap scanBitmap = BitmapFactory.decodeFile(mPath);
        float width = scanBitmap.getWidth();
        float height = scanBitmap.getHeight();
        float haftWidth = width / 2;
        Result result = null;
        while ( width >= haftWidth) {
            result = checkCode(scanBitmap, width, height);
            if (result != null) {
                break;
            }
            width = width * 0.9f;
            height = height * 0.9f;
        }
        if (result == null) {
            Log.i("result", "null");
            model.setIscomplete(true);
            model.setCode("error");
            if (runnable != null) {
                getHandler().post(runnable);
            }
            return;
        }

        final String msg = result.getText();
        Bitmap bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), logo);
        try {
            String path = FileUtils.getChileFile(Constants.RESULT).getAbsolutePath();
            Bitmap code = QRCodeUtils.qrEncode(msg, bitmap, 500, 500);

            String name = fname.replace(".jpg","").replace(".png","");
            int size;
            if (name.length() > 0 && name.length() < 26) {
                size = 30;
            } else {
                size = 25;
            }
            Bitmap text = drawTextToBitmap(name, 450, size, size , Typeface.DEFAULT);
            FileUtils.saveBitmap(composeImg(code, text), path, fname);
            model.setIscomplete(true);
            model.setCode(msg);
            if (runnable != null) {
                getHandler().post(runnable);
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        Log.i("result", result.getText());
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

    private Runnable notifySingle() {
        return new Runnable() {
            @Override
            public void run() {
                dismissLoadingDialog();
                adapter.notifyDataSetChanged();
            }
        };
    }
    int i;
    private Runnable notifyToast() {
        return new Runnable() {
            @Override
            public void run() {
                ToastUtil.showShort(MainActivity.this, String.format("已扫描完:%s", i++));
            }
        };
    }

}
