package com.monster.godzilla.load.stick;

import com.monster.godzilla.bean.CopyBean;
import com.monster.godzilla.target.Target;
import com.monster.godzilla.utlis.FileManagerFileUtils;
import com.monster.godzilla.utlis.FileManagerSdcardUtils;
import com.monster.godzilla.utlis.FileManagerUtil;
import com.monster.godzilla.utlis.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CopyFileEngine implements com.monster.godzilla.interfaces.ICopyFiless {

    private Target.ReuestCopyImpl  mCallback = null;
    private CopyThread mThread = null;
    private List<String> mSourceFiles = null;

    /**
     * 收集创建成功的 文件或者文件夹
     */
    private List<String> mCopySuccessfulPath;

    /**
     * 收集 复制失败的文件或者文件夹
     */
    private List<String> copyThePathOfTheError;



    private String mDestinationPath = null;

    private long mAllFileSize = 0;
    private long mCopyedSize = 0;
    private int mProgress = 0;
    private int mLastProgress = 0;
    private long mLastCopySize = 0;

    /**
     * 设计为 从开始到结束都是 true ,
     */
    private boolean mIsRunning = true;
    private boolean mIsWait = false;

    private final Object mLockObj = new Object();
    private boolean isFinish;

    public CopyFileEngine() {
        copyThePathOfTheError=new ArrayList<>();
        mCopySuccessfulPath=new ArrayList<>();
    }

    @Override
    public void start(List<String> files, String des)  {
        mSourceFiles = files;
        mDestinationPath = des;
        if (mThread == null || !mThread.isAlive()) {

            mThread = new CopyThread();
            init();
            mThread.start();
            if (mCallback != null) {
                mCallback.onStartCopy();
            }
        }
    }

    private void init() {
        mAllFileSize = 0;
        mCopyedSize = 0;
        mIsRunning = true;
        mIsWait = false;
        mProgress = 0;
        mLastProgress = 0;
        mLastCopySize = 0;
        isFinish=false;
    }

    @Override
    public void cancel()  {
        mIsRunning = false;
        mIsWait = false;
        synchronized (mLockObj) {
            mLockObj.notify();
        }
        if(isFinish){
           return;
        }
        if (mCallback != null) {
            mCallback.onCancelCopy(mDestinationPath,mCopyedSize, mCopySuccessfulPath);
        }
    }

    @Override
    public void pause()  {
        mIsWait = true;
        synchronized (mLockObj) {
            mLockObj.notify();
        }
        if(!isFinish){
            if (mCallback != null) {
                mCallback.onPauseCopy();
            }
        }

    }

    @Override
    public void resume(){
        mIsWait = false;
        synchronized (mLockObj) {
            mLockObj.notify();
        }
        if (mCallback != null) {
            mCallback.onResumeCopy();
        }
    }

    @Override
    public boolean isCancel() {
        synchronized (mLockObj) {
            mLockObj.notify();
        }
        return !mIsRunning&&!mIsWait;
    }

    @Override
    public boolean isRunning() {
        synchronized (mLockObj) {
            mLockObj.notify();
        }
        return mIsRunning;
    }

    @Override
    public boolean isPause() {

        synchronized (mLockObj) {
            mLockObj.notify();
        }
       return mIsWait ;
    }

    @Override
    public void registerCallback(Target.ReuestCopyImpl callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(Target.ReuestCopyImpl callback) {
        mCallback = null;
    }


    class CopyThread extends Thread {

        @Override
        public void run() {

            if (mCallback != null) {
                mCallback.postUpdateCopy("正在统计中....", mAllFileSize, mCopyedSize, 1);
            }
            mAllFileSize = FileManagerFileUtils.getFileSize(mSourceFiles);

            // 复制文件
            for (int i = 0; i < mSourceFiles.size(); i++) {
                if (!mIsRunning) {
                    break;
                }
                while (mIsWait) {
                    System.out.println("--->等待复制中");
                    synchronized (mLockObj) {
                        try {
                            mLockObj.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                File file = new File(mSourceFiles.get(i));
                if (file.exists() && file.isFile()) {
                    copyFile(mSourceFiles.get(i), mDestinationPath);
                } else if (file.exists() && file.isDirectory()) {
                    copyFolder(mSourceFiles.get(i), mDestinationPath);
                }

            }
//            mNotyfication.setNotyfiy(mNotificationId - 1, "复制完成", mAllFileSize, mAllFileSize, 100);
            // mNotyfication.clear(mNotificationId - 1);

            // 回调通知界面
            if (mCallback != null) {
                if (!mIsRunning) {
                    mCallback.onCancelCopy(mDestinationPath,mCopyedSize, mCopySuccessfulPath);
                } else {
                    CopyBean copyBean=new CopyBean();
                    copyBean.mCopyThePathOfTheError=copyThePathOfTheError;
                    copyBean.mSourceFiles=mSourceFiles;
                    copyBean.mDestinationPath=mDestinationPath;
                    copyBean.mCopyedSize=mCopyedSize;





                    mCallback.call(copyBean);
                    mCallback.onFinishCopy(mCopyedSize);
                }
            }
            if(mIsRunning){
               isFinish=true;
            }
        }
    }

    /**
     * 复制单个文件
     *  复制 文件不中断
     * @param oldPath
     *            String 原文件路径 如：c:/fqf.txt
     *            String 复制父路径
     * @return boolean
     */
    public void copyFile(String oldPath, String destDirPath) {
        File oldFile = new File(oldPath);
        InputStream in = null;
        FileOutputStream out = null;
        try {
            String newPath = null;
            if (destDirPath.endsWith(File.separator)) {
                newPath = destDirPath + oldFile.getName();
            } else {
                newPath = destDirPath + File.separator + oldFile.getName();
            }


            File newFile = new File(newPath);

            if (newFile.exists()) {
                newFile = new File(FileManagerFileUtils.getNewFileName(newPath));
            }

            in = new FileInputStream(oldFile); // 读入原文件
            out = new FileOutputStream(newFile);
            long len = oldFile.length();
            if (len > 4 * FileManagerSdcardUtils.SIZE_MB) {
                len = 4 * FileManagerSdcardUtils.SIZE_MB;
            }
            byte[] buffer = new byte[(int) len];

            int byteread = 0;
            while ((byteread = in.read(buffer)) != -1) {
                // System.out.println("--->" + mCopyedSize);
                out.write(buffer, 0, byteread);
                if (byteread == 0) {
                    break;
                }
                // 更新到进度条
                updateToProgress(oldFile, byteread);
            }
        } catch (IOException e) {
            System.out.println("出错啦~~~" + e);
            copyThePathOfTheError.add(oldPath);
        } finally {
            mCopySuccessfulPath.add(destDirPath);
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 更新到进度
     * @param oldFile
     * @param byteread
     */
    private void updateToProgress(File oldFile, int byteread) {
        mCopyedSize += byteread;
        mLastProgress = mProgress;
        if (mAllFileSize != 0) {
            mProgress = (int) (mCopyedSize * 100 / mAllFileSize);
        } else {
            mProgress = 100;
        }
        if (mLastProgress < mProgress || mCopyedSize - mLastCopySize > 8 * FileManagerSdcardUtils.SIZE_MB) {
            mLastCopySize = mCopyedSize;
            if (mCallback != null) {
                mCallback.postUpdateCopy(oldFile.getName(), mAllFileSize, mCopyedSize, mProgress);
            }
//            mNotyfication.setNotyfiy(mNotificationId - 1, oldFile.getName(), mAllFileSize, mCopyedSize, mProgress);
        }
    }

    /**
     * 复制整个文件夹内容
     * 
     *            String 原文件路径 如：c:/fqf
     *            String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public void copyFolder(String sourceDirPath, String targetDirPath) {
        try {
            File oldDir = new File(sourceDirPath);
            File newDir = new File(targetDirPath + File.separator + oldDir.getName());
            if (newDir.exists()) {
                newDir = new File(FileManagerFileUtils.getNewFolderPath(sourceDirPath));// 添加副本
            }
            FileManagerUtil.add777(newDir);

            if( !FileUtils.createOrExistsDir(newDir)){
                copyThePathOfTheError.add(sourceDirPath);
                return;
            }
            mCopySuccessfulPath.add(newDir.getAbsolutePath());
            targetDirPath = newDir.getCanonicalPath();// /

            File[] files = FileManagerFileUtils.fileFilter(oldDir);

            if (files == null) {
                return;
            }


            for (File file : files) {
                if (!mIsRunning) {
                    break;
                }

                while (mIsWait) {
                    synchronized (mLockObj) {
                        try {
                            mLockObj.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (file.isFile()) {
                    String sourceFile = sourceDirPath + File.separator + file.getName();
                    copyFile(sourceFile, targetDirPath);
                } else if (file.isDirectory()) {// 如果是子文件夹
                    String sourceDir = sourceDirPath + File.separator + file.getName();
                    String targetDir = targetDirPath ;
                    copyFolder(sourceDir, targetDir);
                }
            }
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
            copyThePathOfTheError.add(sourceDirPath);
            e.printStackTrace();
        }
    }

}
