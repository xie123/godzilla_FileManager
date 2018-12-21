package com.monster.godzilla.load;

import android.util.Log;

import com.monster.godzilla.interfaces.XFunc1;
import com.monster.godzilla.utlis.FileManagerSdcardUtils;
import com.monster.godzilla.utlis.ThreadManager;
import com.monster.godzilla.utlis.ThreadManager.OnExecuteCallback;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * <p>
 * <h2>简述:</h2>
 * <ol> 获取 磁盘 路径 </ol>
 * <h2>功能描述:</h2>
 * <ol>无</ol>
 * <h2>修改历史:</h2>
 * <ol>无</ol>
 * </p>
 *
 * @author 11925
 * @date 2018/11/21/021
 */
class RequestDiskPath extends BaseRequest {
    public static final String TAG = RequestDiskPath.class.getName();
    private HashSet<String> paths;


    <T> RequestDiskPath(XFunc1<T> xFunc1,ICurrentParameter iCurrentParameter) {
        super(xFunc1,iCurrentParameter);
        paths = new HashSet<>();
    }

    @Override
    public void doSomething() {
        Log.i(TAG, "doSomething");
        postTimeOut();
        requestId = (int) System.currentTimeMillis();
        ThreadManager.getInstance().getShortThreadPool().excute(requestId, new OnExecuteCallback<HashSet<String>>() {
            @Override
            public void onRun(ThreadManager.Subscriber<? super HashSet<String>> subscriber) {
                paths.clear();
                //添加本地磁盘
                if (mConfiguration.isWhetherToDisplayLocalDisk()) {
                    String localSDCardPath = FileManagerSdcardUtils.getLocalSDCardPath();
                    if (localSDCardPath != null) {
                        paths.add(localSDCardPath);
                    }
                }
                //只需要本地磁盘的话，就不添加
                HashSet<String> localUsbs = null;
                if (mConfiguration.isWhetherToDisplayTheRemovableDisk()) {
                    localUsbs = FileManagerSdcardUtils.getAllSDPath();
                    if (!mConfiguration.isWhetherToDisplayLocalDisk()) {
                        localUsbs.remove(FileManagerSdcardUtils.getLocalSDCardPath());
                    }
                }
                if (localUsbs != null) {
                    paths.addAll(localUsbs);
                }
                subscriber.onNext(paths);
            }

            @Override
            public void onNext(HashSet<String> strings) {
                stopTimeOut();
                if (!isTimeOut.get()) {
                    complete(new ArrayList<>(strings));
                } else {
                    timeOut();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                error(throwable);
            }
        });


    }


    @Override
    public void clear() {
        if (ThreadManager.getInstance().unSubscribe(requestId)) {
            requestId = CANCEL;
        }
        super.clear();
    }
}
