package com.dzm.ffmpeg.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @description get data from asserts
 *
 * Created by deng on 2019/7/14.
 */
public class AssertsUtil {

    private AssertsUtil( ){
        // do nothing
    }

    /**
     * 读取asserts目录下的文件
     *
     * @param fileName eg:"pcmTestData.pcm"
     * @return 对应文件的内容
     *
     * */
    public static String readFileFromAssets(Context context, String fileName) throws IOException, IllegalArgumentException {
        if (null == context || TextUtils.isEmpty( fileName )){
            throw new IllegalArgumentException( "bad arguments!" );
        }

        AssetManager assetManager = context.getAssets();
        InputStream input = assetManager.open(fileName);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = input.read(buffer)) != -1) {
            output.write(buffer, 0, length);
        }
        output.close();
        input.close();

        return output.toString();
    }

    /**
     * 列出Asserts文件夹下的所有文件
     *
     * @return asserts目录下的文件名列表
     * */
    public static List<String> getAssertsFiles(Context context ) throws IllegalArgumentException{
        if (null == context){
            throw new IllegalArgumentException( "bad arguments!" );
        }

        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            e.printStackTrace( );
        }

        return (null == files) ? null : Arrays.asList(files);
    }

    /**
     *  从assets目录中复制整个文件夹内容
     *  @param  context  Context 使用CopyFiles类的Activity
     *  @param  oldPath  String  原文件路径  如：/aa
     *  @param  newPath  String  复制后路径  如：xx:/bb/cc
     */
    public static void copyFilesFassets(Context context,String oldPath,String newPath) {
        try {
            //获取assets目录下的所有文件及目录名
            String fileNames[] = context.getAssets().list(oldPath);
            if (fileNames.length > 0) { //如果是目录
                File file = new File(newPath);
                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFassets(context,oldPath + "/" + fileName,newPath+"/"+fileName);
                }
            } else {//如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount=0;
                while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
