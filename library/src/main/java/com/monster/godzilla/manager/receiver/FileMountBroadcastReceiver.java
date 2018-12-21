package com.monster.godzilla.manager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.monster.godzilla.FileManager;
import com.monster.godzilla.utlis.BroadCastReceiverUtlis;
import com.monster.godzilla.utlis.FileManagerSdcardUtils;
import com.monster.godzilla.utlis.Preconditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;

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
 * @date 2018/11/20/020
 */
class FileMountBroadcastReceiver extends BroadcastReceiver {
    HashSet<String> savePath;

    private int num;
    public static final int POST_CONTROLTHREAD = 1;
    public static final int POST_DATA_PROCESSING = 2;
    private AtomicBoolean isRun = new AtomicBoolean(false);
    private MountMonitor.MountListener iFileConnectionCallback;
    public HandlerThread HANDLER;
    private Handler mHandler;

    public FileMountBroadcastReceiver(MountMonitor.MountListener iFileConnectionCallback) {
        this.iFileConnectionCallback = iFileConnectionCallback;
        this.savePath = new HashSet<>();
        HANDLER = new HandlerThread("HandlerThread");
        HANDLER.start();
        mHandler = new Handler(HANDLER.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == POST_CONTROLTHREAD) {
                    postControlThread();
                } else if (msg.what == POST_DATA_PROCESSING) {
                    postDataProcessing();
                }
            }
        };
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (!FileManager.init().getFileManagerConfiguration().isWhetherToDisplayTheRemovableDisk()) {
            return;
        }
        if (intent.getData() == null) {
            return;
        }
        String usbPath = intent.getData().getPath();
        String action = intent.getAction();
        if (null == action || usbPath == null || usbPath.length() <= 0) {
            return;
        }
        if (!isRun.get()) {
            canDataProcessing();
            num = 0;
            isRun.set(true);
        }
        savePath.clear();
        savePath.addAll(FileManagerSdcardUtils.getAllSDPath());
        synchronized (FileMountBroadcastReceiver.this) {
            if (action.equals(Intent.ACTION_MEDIA_MOUNTED) || action.equals(Intent.ACTION_MEDIA_CHECKING)) {
                Log.i("增加   广播------->", action + "----------->" + FileManagerSdcardUtils.getAllSDPath().size());

                canControlThreadMsg();
                addControlThreadMsg();

                savePath.add(usbPath);


            } else if (action.equals(Intent.ACTION_MEDIA_REMOVED)
                    || action.equals(Intent.ACTION_MEDIA_UNMOUNTED)
                    || action.equals(Intent.ACTION_MEDIA_EJECT)) {
                Log.i("移除广播------->", action + "----------->" + FileManagerSdcardUtils.getAllSDPath().size());

                canControlThreadMsg();
                addControlThreadMsg();

                savePath.add(usbPath);

            }
        }
    }

    private void addControlThreadMsg() {
        mHandler.sendEmptyMessageDelayed(POST_CONTROLTHREAD, 500);
    }

    private void canControlThreadMsg() {
        if (mHandler.hasMessages(POST_CONTROLTHREAD)) {
            mHandler.removeMessages(POST_CONTROLTHREAD);
        }

    }

    private void addDataProcessing() {
        mHandler.sendEmptyMessage(POST_DATA_PROCESSING);
    }

    private void canDataProcessing() {
        if (mHandler.hasMessages(POST_CONTROLTHREAD)) {
            mHandler.removeMessages(POST_CONTROLTHREAD);
        }
    }

    private void postControlThread() {
        isRun.set(false);
        //Handler 被取消 不一定取消 了线程中的事情
        // 为了加开关控制。 分两个
        canDataProcessing();
        addDataProcessing();
    }

    private void postDataProcessing() {
        Collection<String> savePaths = dataProcessing();
        if (!isRun.get()) {
            iFileConnectionCallback.postChangeAfter(new ArrayList<>(savePaths));
        }
        for (String s : savePaths) {
            Log.i("sssssss------->", s);
        }
        num = 0;
        savePath.clear();
    }

    public void clear() {
        if (HANDLER != null) {
            HANDLER.quit();
            HANDLER = null;
        }
    }

    private synchronized Collection<String> dataProcessing() {
        Log.i("延迟广播------->", "num 大小-" + num);
        Log.i("延迟广播------->", "savePath 大小- >" + savePath.size());
        Log.i("延迟广播------->", "getAllSDPath 大小-" + FileManagerSdcardUtils.getAllSDPath().size());
        Collection<String> strings = FileManagerSdcardUtils.getAllSDPath();
        if (savePath.size() == strings.size()) {
            if (num == 10) {
                for (String s : savePath) {
                    if (!strings.contains(s)) {
                        return strings;
                    }
                }
                num++;
            } else if (num == 20) {
                for (String s : savePath) {
                    if (!strings.contains(s)) {
                        return strings;
                    }
                }
                return strings;
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                num++;
            }
            return dataProcessing();
        }
        return strings;
    }

    public static FileMountBroadcastReceiver registerReceiver(Context $args1, MountMonitor.MountListener iFileConnectionCallback) {
        Preconditions.checkNotNull($args1);
        Preconditions.checkNotNull(iFileConnectionCallback);
        FileMountBroadcastReceiver fileConnectBroadcastReceiver = new FileMountBroadcastReceiver(iFileConnectionCallback);
        return registerReceiver($args1, fileConnectBroadcastReceiver);
    }

    public static FileMountBroadcastReceiver registerReceiver(Context $args1, FileMountBroadcastReceiver fileConnectBroadcastReceiver) {
        IntentFilter intentUsbFilter = new IntentFilter();
        intentUsbFilter.setPriority(1000);
        intentUsbFilter.addDataScheme("file");
//        intentUsbFilter.addAction(Intent.ACTION_MEDIA_CHECKING);

        intentUsbFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);

        //手动 取消挂载
//        intentUsbFilter.addAction(Intent.ACTION_MEDIA_EJECT);
        intentUsbFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
//        intentUsbFilter.addAction(Intent.ACTION_MEDIA_REMOVED);

        BroadCastReceiverUtlis.registerReceiver($args1, fileConnectBroadcastReceiver, intentUsbFilter);
        return fileConnectBroadcastReceiver;
    }


}
