package com.example.administrator.searchforlovedones.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.administrator.searchforlovedones.R;
import com.example.administrator.searchforlovedones.faceserver.FaceServer;
import com.example.administrator.searchforlovedones.widget.ProgressDialog;
import com.arcsoft.imageutil.ArcSoftImageFormat;
import com.arcsoft.imageutil.ArcSoftImageUtil;
import com.arcsoft.imageutil.ArcSoftImageUtilError;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.support.constraint.Constraints.TAG;

/**
 * 批量注册页面
 */
public class FaceManageActivity extends BaseActivity {
    //注册图所在的目录
    private static final String ROOT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "arcfacedemo";
    private static final String REGISTER_DIR = ROOT_DIR + File.separator + "register";
    private static final String REGISTER_FAILED_DIR = ROOT_DIR + File.separator + "failed";
    private ExecutorService executorService;

    private TextView tvNotificationRegisterResult;

    ProgressDialog progressDialog = null;
    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    private static String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_manage);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        executorService = Executors.newSingleThreadExecutor();
        tvNotificationRegisterResult = findViewById(R.id.notification_register_result);
        progressDialog = new ProgressDialog(this);
        FaceServer.getInstance().init(this);
    }

    @Override
    protected void onDestroy() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        FaceServer.getInstance().unInit();
        super.onDestroy();
    }

    public void batchRegister(View view) {
        if (checkPermissions(NEEDED_PERMISSIONS)) {
            doRegister();
        } else {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
        }
    }

    private void doRegister() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                File[] jpgFiles = new File[0];
                //图片数量
                final int totalCount = 0;
                //成功注册数量
                int successCount = 0;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.setMaxProgress(totalCount);
                        progressDialog.show();
                        tvNotificationRegisterResult.setText("");
                        tvNotificationRegisterResult.append(getString(R.string.batch_process_processing_please_wait));
                    }
                });
                for (int i = 0; i < totalCount; i++) {
                    final int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (progressDialog != null) {
                                progressDialog.refreshProgress(finalI);
                            }
                        }
                    });
                    final File jpgFile = jpgFiles[i];
                    //获取一个bitmap
                    Bitmap bitmap = BitmapFactory.decodeFile(jpgFile.getAbsolutePath());
                    //bitmap为空
                    if (bitmap == null) {
                        File failedFile = new File(REGISTER_FAILED_DIR + File.separator + jpgFile.getName());
                        if (!failedFile.getParentFile().exists()) {
                            failedFile.getParentFile().mkdirs();
                        }
                        jpgFile.renameTo(failedFile);
                        continue;
                    }
                    //从BITMAP获取信息

                    bitmap = ArcSoftImageUtil.getAlignedBitmap(bitmap, true);
                    //如果还是为空
                    if (bitmap == null) {
                        File failedFile = new File(REGISTER_FAILED_DIR + File.separator + jpgFile.getName());
                        if (!failedFile.getParentFile().exists()) {
                            failedFile.getParentFile().mkdirs();
                        }
                        jpgFile.renameTo(failedFile);
                        continue;
                    }
                    //人脸信息不为空
                    byte[] bgr24 = ArcSoftImageUtil.createImageData(bitmap.getWidth(), bitmap.getHeight(), ArcSoftImageFormat.BGR24);
                    int transformCode = ArcSoftImageUtil.bitmapToImageData(bitmap, bgr24, ArcSoftImageFormat.BGR24);
                    //不成功
                    if (transformCode != ArcSoftImageUtilError.CODE_SUCCESS) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                tvNotificationRegisterResult.append("");
                            }
                        });
                        return;
                    }
                    //成功
                    boolean success = FaceServer.getInstance().registerBgr24(FaceManageActivity.this, bgr24, bitmap.getWidth(), bitmap.getHeight(),
                            jpgFile.getName().substring(0, jpgFile.getName().lastIndexOf(".")));
                    if (!success) {
                        File failedFile = new File(REGISTER_FAILED_DIR + File.separator + jpgFile.getName());
                        if (!failedFile.getParentFile().exists()) {
                            failedFile.getParentFile().mkdirs();
                        }
                        jpgFile.renameTo(failedFile);
                    } else {
                        //成功数量++
                        successCount++;
                    }
                }
                final int finalSuccessCount = successCount;
                //处理完成
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        tvNotificationRegisterResult.append(getString(R.string.batch_process_finished_info, totalCount, finalSuccessCount, totalCount - finalSuccessCount, REGISTER_FAILED_DIR));
                    }
                });
                Log.i(FaceManageActivity.class.getSimpleName(), "run: " + executorService.isShutdown());
            }
        });
    }

    @Override
    void afterRequestPermission(int requestCode, boolean isAllGranted) {
        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            if (isAllGranted) {
                doRegister();
            } else {
                showToast(getString(R.string.permission_denied));
            }
        }
    }

    public void clearFaces(View view) {
        int faceNum = FaceServer.getInstance().getFaceNumber(this);
        if (faceNum == 0) {
            showToast(getString(R.string.batch_process_no_face_need_to_delete));
        } else {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.batch_process_notification)
                    .setMessage(getString(R.string.batch_process_confirm_delete, faceNum))
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int deleteCount = FaceServer.getInstance().clearAllFaces(FaceManageActivity.this);
                            showToast(deleteCount + " faces cleared!");
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .create();
            dialog.show();
        }
    }
    //获取照片
    public static Bitmap getbitmap(String imageUri) {
        Log.e("img", imageUri);
        // 显示网络上的图片
        Bitmap bitmap = null;
        try {
            URL myFileUrl = new URL(imageUri);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();

            Log.e(TAG, "image download finished." + imageUri);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            bitmap = null;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "getbitmap bmp fail---");
            bitmap = null;
        }
        return bitmap;
    }
}
