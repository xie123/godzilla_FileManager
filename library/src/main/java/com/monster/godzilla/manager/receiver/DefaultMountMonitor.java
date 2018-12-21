package com.monster.godzilla.manager.receiver;

import android.content.Context;

import com.monster.godzilla.utlis.BroadCastReceiverUtlis;

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
 * @date 2018/11/21/021
 */
public class DefaultMountMonitor implements MountMonitor {
    private final Context context;
    private final MountListener listener;
    private boolean isRegistered;
    private FileMountBroadcastReceiver fileConnectBroadcastReceiver;
    public DefaultMountMonitor(Context context, MountListener listener) {
        this.context = context;
        this.listener = listener;
    }


    private void register() {
        if (isRegistered) {
            return;
        }
        fileConnectBroadcastReceiver=  FileMountBroadcastReceiver.registerReceiver(context, listener);
        isRegistered = true;
    }

    private void unregister() {
        if (!isRegistered) {
            return;
        }
        if(fileConnectBroadcastReceiver!=null){
            fileConnectBroadcastReceiver.clear();
        }
        BroadCastReceiverUtlis.unregisterReceiver(context, fileConnectBroadcastReceiver);
        isRegistered = false;
    }

    @Override
    public void onStart() {
        register();
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        unregister();
    }
}
