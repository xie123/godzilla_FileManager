package com.monster.godzilla.operate;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.monster.godzilla.utlis.FileManagerFileUtils;
import com.monster.godzilla.utlis.FileManagerUtil;
import com.monster.godzilla.utlis.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * <h2>简述:</h2>
 * <ol>无</ol>
 * <h2>功能描述:</h2>
 * <ol>无</ol>
 * <h2>修改历史:</h2>
 * <ol>无</ol>
 * </p>
 *
 * @author 11925
 * @date 2018/12/3/003
 */
public class FileOperationHelper {
    private static final String LOG_TAG = "FileOperation";


    private final List<String> mCurFileNameList =Collections.synchronizedList(new ArrayList<>()) ;

    private boolean mMoving;



    private FileFilter mFilter;

    private boolean needToMove;

    public FileOperationHelper(@NonNull  FileFilter f) {
        mFilter = f;
    }


    /**
     * 创建文件夹
     * @param path
     * @param name
     * @return
     */
    public boolean createFolder(String path, String name) {
        Log.v(LOG_TAG, "createFolder >>> " + path + "," + name);


        String nameTemp = name.trim();
        if (TextUtils.isEmpty(path)||TextUtils.isEmpty(nameTemp) || name.endsWith("/")) {
            return false;
        }
        File f = new File(FileManagerUtil.makePath(path, nameTemp));
        if (f.exists()) {
            return false;
        }
        return  f.mkdir();
    }

    /**
     * 复制
     * @param files
     */
    public void copy(String files) {
        needToMove=false;
        copyFileList(Collections.singletonList(files));
    }
    public void copy(List<String> files) {
        needToMove=false;
        copyFileList(files);
    }
    public void cut(String files){
        needToMove=true;
        copyFileList(Collections.singletonList(files));
    }
    public void cut(List<String> files){
        needToMove=true;
        copyFileList(files);
    }
    /**
     * 粘贴
     * @param path
     * @return
     */
    public boolean paste(String path) {
        if (mCurFileNameList.size() == 0) {
            return false;
        }
        for (String f : mCurFileNameList) {
          copyFile(f, path);
        }
        clear();
        return true;
    }

    /**
     * 是否能粘贴
     * @return
     */
    public boolean canPaste() {
        return mCurFileNameList.size() != 0;
    }

    /**
     * 开始移动
     * @param path
     */
    public boolean startMove(String  path) {
        if (mMoving) {
            return false;
        }
        mMoving = true;

        if (mCurFileNameList.size() == 0) {
            return false;
        }
        final String _path = path;
        int num=0;
        for (String f : mCurFileNameList) {

            File file = new File(f);
            String newPath = FileManagerUtil.makePath(path, FileManagerFileUtils.getFileName(f));
            boolean is=file.renameTo(new File(newPath));
            if(is) {
                num++;
            }
        }
        clear();
        mMoving=false;

        return num!=mCurFileNameList.size();
    }

    /**
     * 是否正在移动
     * @return
     */
    public boolean isMoveState() {
        return mMoving;
    }

    /**
     * 是否能移动
     * @param path
     * @return
     */
    public boolean canMove(String path) {
        for (String f : mCurFileNameList) {
            if (!new File(f).isDirectory()) {
                continue;
            }
            if (FileManagerUtil.containsPath(f, path)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 清空
     */
    public void clear() {
        needToMove=false;
        synchronized(mCurFileNameList) {
            mCurFileNameList.clear();
        }
    }



    public List<String> getFileList() {
        return mCurFileNameList;
    }




    /**
     * 该文件是否被选中
     * @param path
     * @return
     */
    public boolean isFileSelected(String path) {
        synchronized(mCurFileNameList) {
            for (String f : mCurFileNameList) {
                if (f.equalsIgnoreCase(path)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 同步
     * 重命名
     * @param filePath 目标文件路径
     * @param newName
     * @return
     */
    public boolean rename(String filePath, String newName) {
        if (TextUtils.isEmpty(filePath) ||TextUtils.isEmpty(newName)||newName.contains("/")|| newName.contains(" ")) {
            Log.e(LOG_TAG, "rename: null parameter");
            return false;
        }
       return FileUtils.rename(filePath,newName);
    }

    /**
     * 从  删除指定的文件
     * @param files
     * @return
     */
    public boolean delete(List<String> files) {
        copyFileList(files);
        for (String f : mCurFileNameList) {
            deleteFile(f);
        }
        clear();
        return true;
    }
    public boolean delete(String files) {
        copyFileList(files);
        for (String f : mCurFileNameList) {
            String s= deleteFile(f);
            if(TextUtils.isEmpty(s)){
                clear();
                return false;
            }
        }
        clear();
        return true;
    }

    /**
     * 删除文件
     * @param filePath
     * @return
     */
    public static String deleteFile(String filePath) {
        if (filePath != null) {
            File file = new File(filePath);
            if (file.exists()) {
                if (file.isDirectory()) {
                    return FileUtils.deleteDir(file) ? filePath : null;
                }else{
                    return FileUtils.deleteFile(filePath)?filePath:null;
                }
            }
        }
        Log.v(LOG_TAG, "deleteFile >>> " + filePath);
        return null;
    }

    /**
     * 删除文件
     * @param dir
     * @return
     */
    private static String deleteDir(File dir) {

        if (dir == null || !dir.exists()) {
            return null;
        }
        String absolutePath = dir.getAbsolutePath();
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    if (FileManagerUtil.isNormalFile(absolutePath)) {
                        deleteDir(file);
                    }
                } else {
                    boolean delete = file.delete();
                }
            }
        }
        boolean delete = dir.delete();
        return delete ? absolutePath : null;

    }


    /**
     *
     * @param path
     * @param dest
     */
    private String copyFile(String path, String dest) {
        if (path == null || dest == null) {
            Log.e(LOG_TAG, "copyFile: null parameter");
            return null;
        }
        File dir=new File(dest);

        if (dir.isFile()) {
            dir = new File(dir.getParent());
        }
        File src= new File(path);
        File target = FileManagerFileUtils.getNewFile(dir,src, new File(dir, FileManagerFileUtils.getFileName(path)), new AtomicInteger(0));
        if (target != null) {
            if (src.isDirectory()) {
                a(src,target);
                return target.exists() ? target.getAbsolutePath() : null;
            } else {
                b(src,target);
                return target.exists() ? target.getAbsolutePath() : null;
            }
        }
        return null;
    }

    static void a(File src, File target){
        //不予许复制到子目录
        if (target.getAbsolutePath().startsWith(src.getAbsolutePath() + File.separator)) {
            return ;
        }
        if (!target.exists()) {
            boolean mkdirs = target.mkdirs();
            if (!mkdirs) {
                return ;
            }
        }
        // 复制文件夹
        File[] currentFiles=FileManagerFileUtils.fileFilter(src);
        if (currentFiles != null) {
            for (File currentFile : currentFiles) {
                // 如果当前为子目录则递归
                if (currentFile.isDirectory()) {
                    a(new File(currentFile + "/"), new File(target.getAbsolutePath() + "/" + currentFile.getName() + "/"));
                } else {
                    b(currentFile, new File(target.getAbsolutePath()  + "/" + currentFile.getName()));
                }
            }
        }
    }
    static void b(File src, File target){
        FileChannel input = null;
        FileChannel output = null;

        try {
            input = new FileInputStream(src).getChannel();
            output = new FileOutputStream(target).getChannel();
            output.transferFrom(input, 0, input.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    private  void copyFileList(String files) {
        synchronized(mCurFileNameList) {
            mCurFileNameList.clear();
            mCurFileNameList.add(files);
        }
    }


    private  void copyFileList(List<String> files) {
        synchronized(mCurFileNameList) {
            mCurFileNameList.clear();
            mCurFileNameList.addAll(files);
        }
    }

    public boolean isNeedToMove() {
        return needToMove;
    }
}

