package com.monster.godzilla.load;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.CallSuper;
import android.util.Log;

import com.monster.godzilla.DiskInfoManager;
import com.monster.godzilla.FileManager;
import com.monster.godzilla.FileManagerConfiguration;
import com.monster.godzilla.interfaces.ITimeOutCallBack;
import com.monster.godzilla.interfaces.XFunc1;
import com.monster.godzilla.manager.Request;
import com.monster.godzilla.manager.RequestTracker;

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
 * @date 2018/11/21/021
 */
public abstract  class BaseRequest implements Request, XFunc1<Message> {
    static final int INIT = -1;
    protected  static final int CANCEL = -2;
    protected  volatile  boolean isRunning;
    protected   boolean isComplete;

   protected int requestId =INIT;


  protected   MyHandler myHandler;
    protected XFunc1<Throwable> errorCallBack;
    protected XFunc1 success;
    protected ITimeOutCallBack timeoutCallBack;
    protected  FileManagerConfiguration mConfiguration;
    protected DiskInfoManager diskInfoManager;
    protected  RequestTracker requestTracker;


    protected  <T> BaseRequest(XFunc1<T> xFunc1,ICurrentParameter iCurrentParameter) {
        this.timeoutCallBack=iCurrentParameter.getTimeOutCallBack();
        myHandler=new MyHandler(this);
        this.errorCallBack=iCurrentParameter.getErrorCallBack();
        this.success=xFunc1;
        this.requestTracker=iCurrentParameter.getRequestTracker();
        this.mConfiguration =FileManager.init().getFileManagerConfiguration();
        this.diskInfoManager=FileManager.init().getDiskInfoManager();
    }



    /**
     *
     */
    protected abstract  void doSomething();

    public <T> void complete(T t){
        Log.i("BaseRequest","complete");
        isRunning=false;
        isComplete=true;
        stopTimeOut();

        Log.i("BaseRequest","success!=null" + (success!=null));
        if(success!=null){
            success.call(t);
        }
    }
    public void timeOut(){
        isRunning=false;
        isComplete=true;

        Log.i("BaseRequest","timeOut");
        if(timeoutCallBack!=null){
            timeoutCallBack.timeout();
        }
    }
    public void error(Throwable e){
        isRunning=false;
        isComplete=true;
        if(errorCallBack!=null){
            errorCallBack.call(e);
        }
        stopTimeOut();
    }

    @Override
    public boolean isPause() {

        return false;
    }

    @CallSuper
    @Override
    public  final  void begin() {
        isRunning=true;
        isComplete=false;




        doSomething();
    }

    @Override
    public void cancel() {
        isRunning=false;
        isComplete=true;
    }

    @CallSuper
    @Override
    public boolean isRunning() {
        return isRunning;
    }
    @CallSuper
    @Override
    public void pause() {

    }
    @Override
    public boolean isCancelled() {
        return requestId == CANCEL;
    }

    @CallSuper
    @Override
    public boolean isComplete() {
        return isComplete;
    }
    @CallSuper
    @Override
    public void clear() {
        isRunning=false;
        isComplete=false;
        requestTracker.removeRequest(this);
        stopTimeOut();
    }

    @Override
    public void call(Message message) {

    }

    public static class MyHandler extends Handler {
        private XFunc1<Message> messageXFunc1;

        public MyHandler(XFunc1<Message> messageXFunc1) {
            this.messageXFunc1 = messageXFunc1;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(messageXFunc1!=null){
                messageXFunc1.call(msg);
            }
        }
    }


    protected final AtomicBoolean isTimeOut=new AtomicBoolean(false);
    private final Runnable timeOutRunnable=new Runnable() {
        @Override
        public void run() {
            isTimeOut.set(true);
            timeOut();

        }
    };

    protected void postTimeOut(){
        myHandler.removeCallbacks(timeOutRunnable);
        myHandler.postDelayed(timeOutRunnable,FileManager.init().getFileManagerConfiguration().getTimeOut());
    }
    protected void stopTimeOut(){
        myHandler.removeCallbacks(timeOutRunnable);
    }


}
