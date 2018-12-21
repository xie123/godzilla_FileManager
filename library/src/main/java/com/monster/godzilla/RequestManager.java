package com.monster.godzilla;

import android.content.Context;

import com.monster.godzilla.load.RequestFactory;
import com.monster.godzilla.manager.Request;
import com.monster.godzilla.manager.RequestManagerTreeNode;
import com.monster.godzilla.manager.RequestTracker;
import com.monster.godzilla.manager.lifecycle.Lifecycle;
import com.monster.godzilla.manager.lifecycle.LifecycleListener;
import com.monster.godzilla.utlis.FileManagerUtil;
import com.monster.godzilla.utlis.HandlerUtil;


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
 * @date 2018/10/29/029
 */
public class RequestManager implements LifecycleListener {
    private final Context context;
    private final Lifecycle lifecycle;
    private final RequestManagerTreeNode treeNode;
    private final RequestTracker requestTracker;
    private final FileManager fileManager;

    public RequestManager(Context context, Lifecycle lifecycle, RequestManagerTreeNode treeNode) {
      this(context,lifecycle,treeNode, new RequestTracker());
    }
    public RequestManager(Context context, Lifecycle lifecycle, RequestManagerTreeNode treeNode, RequestTracker requestTracker) {
        this.context = context;
        this.lifecycle = lifecycle;
        this.treeNode = treeNode;
        this.requestTracker = requestTracker;
        this.fileManager = FileManager.init();



        if (FileManagerUtil.isOnBackgroundThread()) {
            HandlerUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lifecycle.addListener(RequestManager.this);
                }
            });
        } else {
            lifecycle.addListener(this);
        }
    }

    @Override
    public void onStart() {
        resumeRequests();
    }

    /**
     * 恢复 所有 请求
     */
    public void resumeRequestsRecursive() {
//        FileManagerUtil.assertMainThread();
        resumeRequests();
        for (RequestManager requestManager : treeNode.getDescendants()) {
            requestManager.resumeRequests();
        }
    }

    public void resumeRequests() {
//        FileManagerUtil.assertMainThread();
        requestTracker.resumeRequests();
    }

    ///////////////isPaused/////////////////////////////////////
    public boolean isPaused() {
//        FileManagerUtil.assertMainThread();
        return requestTracker.isPaused();
    }
    public void pauseRequests() {
//        FileManagerUtil.assertMainThread();
        requestTracker.pauseRequests();
    }
    ///////////////stop/////////////////////////////////////
    @Override
    public void onStop() {
        pauseRequests();
    }

    public void pauseRequestsRecursive() {
        FileManagerUtil.assertMainThread();
        pauseRequests();
        for (RequestManager requestManager : treeNode.getDescendants()) {
            requestManager.pauseRequests();
        }
    }

    ////////////////onDestroy///////////////////////////
    @Override
    public void onDestroy() {
        requestTracker.clearRequests();

        treeNode.getDescendants().remove(this);
    }
    public void onLowMemory() {
      
    }

    public void onTrimMemory(int level) {

    }

    public RequestBuilder transform(@RequestFactory.IRequestType  String type){
       return new RequestBuilder(context,fileManager,this,requestTracker,lifecycle,type);
    }

    //同一个 生命周期内的 执行操作
    public static void clear(Request request) {
        request.clear();
    }



}
