package com.google;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;


import com.google.splashactivity.AdUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

/**
 * Created by admin on 2017/8/2.
 */

public class Utils2 {

    public static int enter_count = 0;
    private static final String LASTEST_MC_PACKAGE = "com.mojang.minecraftpe.elm";
    private static final String OLD_MC_PACKAGE = "com.mojang.minecraftpe";
    private static final String DOWNLOAD_PATH = "/sdcard/11.apk";
    private static final String MC_VERSION = "871000400";
    static Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:

                    proDialog.setMax(fileSize);
                    proDialog.setTitle("正在下载中....");
                    proDialog.setCancelable(false);
                    proDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    proDialog.show();
                    break;
                case 1:
                    proDialog.setMax(fileSize);
                    proDialog.setTitle("正在下载中....");
                    proDialog.setCancelable(false);
                    proDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    proDialog.show();
                    proDialog.setProgress(hasDownLen);

                    break;
                case -1:
                    proDialog.dismiss();
                    break;
        }
    }};
    private static int BUFF_SIZE = 5*1024*1024;


    public static void onCreate(Activity context) {
        downloadAndInstall(context,"/sdcard/11.apk");


    }


    public static void onResume(Activity context) {

        if (enter_count>0){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("警告");
            builder.setMessage("你确定要退出游戏么?");
            builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);
                }
            });
            builder.setNegativeButton("点错了", null);
            builder.create();
            builder.show();
        }
        enter_count ++;

    }

    static ProgressDialog proDialog = null;

    public static void showDialog(Context context,String title,String msg,String okBtnTxt,String cancelBtnTxt,)

    public static void selectVersion(final Context context){
        String[] mc_version = {"畅玩修改版","无修改最新版"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("请选择要启动的MC版本");
        builder.setItems(mc_version, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        if (getVersionCode(context).equals(MC_VERSION)){
                            AdUtils.gotoNextActivity(context);
                        }else {
                            if (getVersionCode(context).equals("-1")){
                                Toast.makeText(context, "您没有安装我的世界,现在为您安装~~", Toast.LENGTH_SHORT).show();
                            }else {

                            }

                        }
                        break;
                    case 1:
                        if (isInstalled(context,LASTEST_MC_PACKAGE)){
                            startup_someApk(context,LASTEST_MC_PACKAGE);
                        }else {
                            Toast.makeText(context, "您还为安装最新版本的我的世界,开始为您下载~~", Toast.LENGTH_SHORT).show();
                            proDialog = new ProgressDialog(context);

                            downloadAndInstall((Activity) context,DOWNLOAD_PATH);
                        }
//                        Toast.makeText(context, "无修改最新版", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(context, "默认版本", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        builder.create().show();
    }



    public static void copyFile2where(InputStream inputStream, String destFile) throws Exception {

        FileOutputStream out = new FileOutputStream(destFile);
        byte buffer[] = new byte[BUFF_SIZE];
        int realLength;
        long currentTime = 0;
        long oldTime = System.currentTimeMillis();
        while ((realLength = inputStream.read(buffer)) > 0) {
            currentTime = System.currentTimeMillis();
            if ((currentTime-oldTime)>500){
                oldTime=currentTime;
                mHandler.sendEmptyMessage(1);
            }
            out.write(buffer, 0, realLength);
            out.flush();
        }
//        inputStream.close();
        out.close();
    }


    // 获取应用版本名字
    public static String getVersionCode(Context context)  {
        try {
            return String.valueOf(context.getPackageManager().
                    getPackageInfo(OLD_MC_PACKAGE, PackageManager.GET_META_DATA).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            return "-1";
        }
    }

    public static void startup_someApk(Context context,String packageName){
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(

                //这个是另外一个应用程序的包名

                packageName);
        context.startActivity(intent);
    }

    public static boolean isInstalled(Context context, String packageName)
    {
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> installedList = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        Iterator<PackageInfo> iterator = installedList.iterator();

        PackageInfo info;
        String name;
        while(iterator.hasNext())
        {
            info = iterator.next();
            name = info.packageName;
            if(name.equals(packageName))
            {
                return true;
            }
        }
        return false;
    }



    private static void config_download_para(Context context){
        String url = "http://file.market.xiaomi.com/download/AppStore/04cd658c519d42223a72314bb4" +
                "c6b4bf6d242ce15/com.mojang.minecraftpe.elm_1.2.0.9.apk";
        String dirPath = Environment.getExternalStorageDirectory().toString() + File.separator + "myWorld" +
                File.separator + "mctool" + File.separator;
        String fileName = "MC_0.14.1_huluxia.apk";
        download_apk(context , url, dirPath, fileName);
    }


    private static void download_apk(Context context, String url, String dirPath, String fileName) {


        Toast.makeText(context, "后台下载中...", Toast.LENGTH_SHORT).show();
    }


    static int fileSize = 0;
    static int hasDownLen = 0;
    public static void downloadAndInstall(final Activity activity, final String savePath){
        final String path = "http://file.market.xiaomi.com/download/AppStore/04cd658c519d42223a72314bb4" +
                "c6b4bf6d242ce15/com.mojang.minecraftpe.elm_1.2.0.9.apk";
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //下载
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(5000);
                    if (conn.getResponseCode() == 200) {
                        final InputStream is = conn.getInputStream();

                        fileSize = conn.getContentLength();
                        mHandler.sendEmptyMessage(0);
                        File file = new File(savePath);
                        FileOutputStream fos = new FileOutputStream(file);
                        int len = -1;

                        byte[] buffer = new byte[1024*1024*2];
                        long oldTime = System.currentTimeMillis();
                        while((len = is.read(buffer)) > 0){
                            fos.write(buffer, 0, len);

                            hasDownLen += len;
                            long currentTime = System.currentTimeMillis();
                            if ((currentTime-oldTime)>500){
                                oldTime=currentTime;
                                mHandler.sendEmptyMessage(1);
                            }



                        }
                        System.out.println("下载完成");
                        fos.flush();
                        fos.close();
                        mHandler.sendEmptyMessage(-1);
                        installFile(activity,file.getAbsolutePath());
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }


    public static void installFile(Activity activity,String apk_path){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        // String type = getMIMEType(f);
        // intent.setDataAndType(Uri.fromFile(f), type);
        intent.setDataAndType(Uri.parse("file://" + apk_path),
                        "application/vnd.android.package-archive");
        activity.startActivity(intent);
}
    

    public static void test(Activity activity){
        downloadAndInstall(activity,"");
    }
}
