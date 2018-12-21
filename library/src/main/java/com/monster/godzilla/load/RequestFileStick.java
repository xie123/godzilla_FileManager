package com.monster.godzilla.load;

import android.util.Log;

import com.monster.godzilla.bean.CopyBean;
import com.monster.godzilla.interfaces.XFunc1;
import com.monster.godzilla.load.stick.CopyFileEngine;
import com.monster.godzilla.target.Target;
import com.monster.godzilla.utlis.ThreadManager;

import java.util.List;

/**
 * <p>
 * <h2>简述:</h2>
 * <ol>文件粘贴</ol>
 * <h2>功能描述:</h2>
 * <ol>无</ol>
 * <h2>修改历史:</h2>
 * <ol>无</ol>
 * </p>
 *
 * @author 11925
 * @date 2018/11/22/022
 */
public class RequestFileStick extends BaseRequest {
    public static final String TAG = RequestFileStick.class.getName();

    private String stickPath;
    private CopyFileEngine copyFileEngine;


    <T> RequestFileStick(XFunc1<T> xFunc1,ICurrentParameter iCurrentParameter) {
        super(xFunc1,iCurrentParameter);
        this.stickPath = iCurrentParameter.getOperateFilePath();
    }


    @Override
    public void doSomething() {
        if (copyFileEngine != null && copyFileEngine.isRunning()) {
            if (isPause()) {
                copyFileEngine.resume();
            }
            return;
        }
        requestId = (int) System.currentTimeMillis();


        //开始 复制 操作
        copyFileEngine = new CopyFileEngine();

        copyFileEngine.registerCallback(new Target.ReuestCopyImpl() {
            @Override
            public void onStartCopy() {
                Log.i(TAG, "开始复制");
                if (success instanceof Target.ReuestCopyImpl) {
                    ((Target.ReuestCopyImpl) success).onStartCopy();
                }

            }

            @Override
            public void onPauseCopy() {
                Log.i(TAG, "暂停复制");
                if (success instanceof Target.ReuestCopyImpl) {
                    ((Target.ReuestCopyImpl) success).onPauseCopy();
                }
            }

            @Override
            public void postUpdateCopy(String fileName, long allSize, long hasDelete, int progress) {
                if (success instanceof Target.ReuestCopyImpl) {
                    ((Target.ReuestCopyImpl) success).postUpdateCopy(fileName, allSize, hasDelete, progress);
                }
                Log.i(TAG, "fileName" + fileName + "----" + "allSize" + allSize + "-----" + "hasDelete" + hasDelete + "-------" + "progress" + progress);
            }

            @Override
            public void onCancelCopy(String path,long hasDeletedSize, List<String> mCopySuccessfulPath) {
                if (success instanceof Target.ReuestCopyImpl) {
                    ((Target.ReuestCopyImpl) success).onCancelCopy(path,hasDeletedSize, mCopySuccessfulPath);
                }
                Log.i(TAG, "取消复制----hasDeletedSize---" + hasDeletedSize);
            }

            @Override
            public void onFinishCopy(long hasDeletedSize) {
                Log.i(TAG, "复制结束----hasDeletedSize---" + hasDeletedSize);
                mConfiguration.getFileOperationHelper().clear();
                if (success instanceof Target.ReuestCopyImpl) {
                    ((Target.ReuestCopyImpl) success).onFinishCopy(hasDeletedSize);
                }
            }

            @Override
            public void onResumeCopy() {
                Log.i(TAG, "恢复复制----");
                if (success instanceof Target.ReuestCopyImpl) {
                    ((Target.ReuestCopyImpl) success).onResumeCopy();
                }

            }

            @Override
            public void call(CopyBean copyBean) {
                Log.i(TAG, "成功----");
                if (success instanceof Target.ReuestCopyImpl) {
                    ((Target.ReuestCopyImpl) success).call(copyBean);
                }
                complete(copyBean);

            }
        });
        if (copyFileEngine != null) {
            copyFileEngine.start(mConfiguration.getFileOperationHelper().getFileList(), stickPath);
        }

    }

    @Override
    public <T> void complete(T t) {
        isRunning = false;
        isComplete = true;
        stopTimeOut();

        Log.i("BaseRequest", "success!=null" + (success != null));

        if (success instanceof Target.ReuestCopyImpl) {
            ((Target.ReuestCopyImpl) success).call((CopyBean) t);
            return;
        }
        if (success instanceof Target.RequestOperateFileImpl) {
            ((Target.RequestOperateFileImpl) success).call((Boolean) t);
        }
    }

    @Override
    public void timeOut() {
        super.timeOut();
    }

    @Override
    public boolean isRunning() {

        if (copyFileEngine != null) {
            return copyFileEngine.isRunning();
        }
        return super.isRunning();
    }

    @Override
    public boolean isCancelled() {
        if (copyFileEngine != null) {
            return copyFileEngine.isCancel();
        }
        return super.isCancelled();
    }

    @Override
    public boolean isPause() {
        if (copyFileEngine != null) {
            return copyFileEngine.isPause();
        }
        return super.isPause();

    }

    @Override
    public void pause() {
        if (copyFileEngine != null) {
            copyFileEngine.pause();
        }
        postTimeOut();
        super.pause();
    }

    @Override
    public void cancel() {
        if (copyFileEngine != null) {
            copyFileEngine.cancel();
        }
        super.cancel();
    }

    @Override
    public void clear() {
        if (ThreadManager.getInstance().unSubscribe(requestId)) {
            requestId = CANCEL;
        }
        cancel();
        super.clear();
    }
}
