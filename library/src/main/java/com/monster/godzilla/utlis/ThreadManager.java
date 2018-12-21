package com.monster.godzilla.utlis;

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
 * @date 2018/12/15/015
 */

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by penghuilou on 2016/1/25.
 * 线程管理类，因为android对于线程的支持不是好，所以尽量少开线程，所以创建了一个线程管理类，用来维护线程，把程序中使用的线程放到一个线程池中
 * ThreadManager 应该设置为单例模式
 */
public class ThreadManager {
    private static final HashMap<Object,Runnable> tasks = new HashMap<>();



    private ThreadManager(){}
    private static class ThreadManagers{
       public static  ThreadManager threadManager=new ThreadManager();
    }

    private ThreadPoolProxy mShortThreadPool;
    private ThreadPoolProxy mLongThreadPool;

    /**
     * 获取短线程池，多用于操作本地文件
     * @return 线程数较小线程池
     */
    public synchronized ThreadPoolProxy getShortThreadPool() {
        if(mShortThreadPool == null){
            mShortThreadPool = new ThreadPoolProxy(3,3,5000l);
        }
        return mShortThreadPool;
    }

    /**
     * 获取长线程池，多用于请求网络资源
     * @return 线程数较多的线程池
     */
    public synchronized ThreadPoolProxy getLongThreadPool() {
        if(mLongThreadPool == null){
            mLongThreadPool = new ThreadPoolProxy(10,10,5000l);
        }
        return mLongThreadPool;
    }

    /**
     * 获取线程管理对象
     * @return 线程管理者
     */
    public static ThreadManager getInstance(){
        return ThreadManagers.threadManager;
    }

    /**
     * 线程池代理类
     */
    public class ThreadPoolProxy{


        private ThreadPoolExecutor mPool;
        private int mCorePoolSize;
        private int mMaximumPoolSize;
        private long mKeepAliveTime;

        /**
         * 构造函数
         * @param mCorePoolSize 线程池维护的线程数
         * @param mMaximumPoolSize 额外开启线程数
         * @param mKeepAliveTime 当线程池中的线程为空时，线程池的存活时间
         */
        public ThreadPoolProxy(int mCorePoolSize, int mMaximumPoolSize, long mKeepAliveTime) {
            this.mCorePoolSize = mCorePoolSize;
            this.mMaximumPoolSize = Integer.MAX_VALUE;
            this.mKeepAliveTime = mKeepAliveTime;
        }

        /***
         * 执行一个异步线程
         * @param onExecuteCallback 要执行的异步任务
         */
        public  <T> void excute(Object tag, final OnExecuteCallback<T> onExecuteCallback){
            Runnable runnable=   new Runnable() {
                @Override
                public void run() {
                    if (onExecuteCallback != null) {
                    Subscriber<T> subscriber = null;
                        try {
                            subscriber = new Subscriber<T>(onExecuteCallback, tag);
                            onExecuteCallback.onRun(subscriber);
                            subscriber.onCompleted();
                        } catch (final Exception e) {
                            if (subscriber != null) {
                                subscriber.onError(e);
                            }
                        }
                    }
                }
            };
            if(mPool==null){
                mPool = new ThreadPoolExecutor(mCorePoolSize,mMaximumPoolSize,mKeepAliveTime, TimeUnit.MILLISECONDS,new LinkedBlockingDeque<Runnable>(10));
            }
            if(!mPool.isShutdown() && !mPool.isTerminated()){
                mPool.execute(runnable);
            }
            addTask(tag,runnable);
        }

        /**
         * 取消一个线程
         * @param runnabel 要取消的异步任务
         */
        public void cancle(Runnable runnabel){
            if(mPool!=null&&!mPool.isShutdown()&&!mPool.isTerminated()){
                mPool.remove(runnabel);
            }
        }
    }


    private static void addTask(Object tag,Runnable future) {
        if(tasks.containsKey(tag)){
            ThreadManager.getInstance().getShortThreadPool().cancle(tasks.get(tag));
        }
        tasks.put(tag,future);
    }

    private static boolean removeTask(Object tag) {
        Runnable future=tasks.remove(tag);
        if(future!=null){
            ThreadManager.getInstance().getShortThreadPool().cancle(tasks.get(tag));
        }
        return true;
    }


    public  boolean unSubscribe(Object tag) {
        return removeTask(tag);
    }

    @SuppressWarnings("unused")
    public  void unSubscribeAll() {
        tasks.clear();
    }




    public static class Subscriber<T> {

      OnExecuteCallback<T> onExecuteCallback;
        Object tag;

        public   Subscriber(OnExecuteCallback<T> onExecuteCallback, Object tag) {
            this.onExecuteCallback = onExecuteCallback;
            this.tag = tag;
        }
        public void onNext(final T t) {
            if (!isUnsubscribed()) {
                HandlerUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (onExecuteCallback != null) {
                                onExecuteCallback.onNext(t);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }

        public void onError(final Throwable throwable) {
            try {
                if (onExecuteCallback != null && !isUnsubscribed()) {
                    onExecuteCallback.onError(throwable);
                    unsubscribe();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        void onCompleted() {
            try {
                if (onExecuteCallback != null && !isUnsubscribed()) {
                    onExecuteCallback.onCompleted();
                    unsubscribe();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public boolean isUnsubscribed() {
            return !tasks.containsKey(tag);
        }

        public boolean unsubscribe() {
            return removeTask(tag);
        }

    }

    public static abstract class OnExecuteCallback<T> {

        public abstract void onRun(Subscriber<? super T> subscriber);

        public abstract void onNext(T t);

        public abstract void onError(Throwable throwable);

        public void onCompleted() {

        }
    }

}