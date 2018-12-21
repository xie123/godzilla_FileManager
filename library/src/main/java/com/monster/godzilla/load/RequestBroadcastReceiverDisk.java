package com.monster.godzilla.load;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.monster.godzilla.FileManager;
import com.monster.godzilla.interfaces.XFunc1;
import com.monster.godzilla.manager.lifecycle.Lifecycle;
import com.monster.godzilla.manager.receiver.MountMonitor;
import com.monster.godzilla.manager.receiver.MountMonitorFactory;
import com.monster.godzilla.utlis.FileManagerSdcardUtils;
import com.monster.godzilla.utlis.ThreadManager;

import java.util.Collection;
import java.util.HashSet;

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
 * @date 2018/11/22/022
 */
public class RequestBroadcastReceiverDisk extends BaseRequest {
    private Lifecycle lifecycle;
    private Context context;
    private int addPathId = INIT;
    private int removePathId = INIT;
    <T> RequestBroadcastReceiverDisk(XFunc1<T> xFunc1,ICurrentParameter iCurrentParameter) {
        super(xFunc1,iCurrentParameter);
        this.context = iCurrentParameter.getContext();
        this.lifecycle =iCurrentParameter.getLifecycle();

    }


    @Override
    public void doSomething() {
        Log.e("BroadcastReceiverDisk", "~~~~~~~begin");
        requestId = (int) System.currentTimeMillis();

        MountMonitor connectivityMonitor = new MountMonitorFactory().build(context, new MountMonitor.MountListener(){

            @Override
            public void postChangeAfter(Collection<String> path) {
               RequestBroadcastReceiverDisk.this. postChangeAfter(path);
            }
        });
        lifecycle.addListener(connectivityMonitor);
    }


    @Override
    public void clear() {
        if (ThreadManager.getInstance().unSubscribe(addPathId)) {
            addPathId = CANCEL;
        }
        if (ThreadManager.getInstance().unSubscribe(removePathId)) {
            removePathId = CANCEL;
        }
        super.clear();
    }



    public void postChangeAfter(Collection<String> path) {
        synchronized (RequestBroadcastReceiverDisk.this) {
            Log.i("RequestBroadcast","剩下------->"+""+path.size());
            HashSet<String> savePath = (HashSet<String>) FileManager.init().getDiskInfoManager().getAllDiskPaths();
            String locadPath=FileManagerSdcardUtils.getLocalSDCardPath();


            for (String save : savePath) {
                if(!TextUtils.equals(locadPath,save)){
                    if (!path.contains(save)) {
                        FileManager.init().getDiskInfoManager().removeDiskProperty(save);
                    }
                }
            }
            FileManager.init().getDiskInfoManager().diskClear();
            FileManager.init().getDiskInfoManager().diskAdd(locadPath);
            FileManager.init().getDiskInfoManager().diskAddAll(path);

            complete(path);

        }
    }

}
