package com.monster.godzilla;

import android.content.Context;
import android.support.annotation.NonNull;

import com.monster.godzilla.interfaces.ITimeOutCallBack;
import com.monster.godzilla.interfaces.XFunc1;
import com.monster.godzilla.load.ICurrentParameter;
import com.monster.godzilla.load.RequestBroadcastReceiverDisk;
import com.monster.godzilla.load.RequestFactory;
import com.monster.godzilla.manager.Request;
import com.monster.godzilla.manager.RequestTracker;
import com.monster.godzilla.manager.lifecycle.Lifecycle;
import com.monster.godzilla.target.Target;
import com.monster.godzilla.target.TargetFactory;
import com.monster.godzilla.utlis.Preconditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class RequestBuilder implements ICurrentParameter{
    private final FileManager fileManager;
    protected final Context context;
    private final RequestTracker requestTracker;
    private final RequestManager requestManager;
    protected final Lifecycle lifecycle;

    /////////////////////////////////添加的属性
    //////////是否强制刷新///////////////////////////
    private boolean isforcedRefreshDiskInfo =false;
    /**
     * 是否跳过扫描磁盘信息
     */
    private boolean isKipScanDiskInfo=false;
    ///////////检查磁盘信息路径//////////////////////////
    private Set<String> checkedPath;
    ///////////检查路径上的文件//////////////////////////
    private String filePath;
    private String fileName;

    //////////////是否是粘性事件 不是的话会运行结束后 自动清理  是的话 删除 粘贴会重新调用 ////////////////
    private boolean isStickEvent;
    private @RequestFactory.IRequestType
    String type;//请求类型
    private ITimeOutCallBack mTimeoutCallBack;

    private XFunc1<Throwable> mErrorCallBack;


    public  RequestBuilder(Context context, FileManager fileManager, RequestManager requestManager,RequestTracker requestTracker, Lifecycle lifecycle,@RequestFactory.IRequestType  String type) {
        this.fileManager = fileManager;
        this.context = context;
        this.requestManager=requestManager;
        this.requestTracker = requestTracker;
        this.lifecycle = lifecycle;
        this.type=type;
    }



    public RequestBuilder isforcedRefresh(){
        isforcedRefreshDiskInfo =true;
        return this;
    }

    /**
     * 检查磁盘信息路径
     * @param checkedPaths
     * @return
     */
    public RequestBuilder checkedDiskPath(@NonNull List<String> checkedPaths) {
        if(checkedPaths.size()==0){
            throw new NullPointerException();
        }
        checkedPath=new HashSet<>(checkedPaths);
        return this;
    }
    public RequestBuilder checkedDiskPath(String checkedPaths) {
        Preconditions.checkStringNotNull(checkedPaths);
        checkedDiskPath(new ArrayList<String>(){{
            add(checkedPaths);
        }});
        return this;
    }

    /**
     * 获取指定路径下的文件列表
     * @param path
     * @return
     */
    public RequestBuilder checkedFilelist(@NonNull  String path){
        Preconditions.checkStringNotNull(path);
        filePath=path;
        return this;
    }

    public RequestBuilder isKipScanDiskInfo(boolean kipScanDiskInfo) {
        isKipScanDiskInfo = kipScanDiskInfo;   return this;
    }

    public RequestBuilder delectFilePath(@NonNull  String path){
        Preconditions.checkStringNotNull(path);
        filePath=path;
        return this;
    }

    public RequestBuilder stickFilePath(@NonNull  String path){
        Preconditions.checkStringNotNull(path);
        filePath=path;
        return this;
    }
    public RequestBuilder rename(@NonNull  String path,String name){
        Preconditions.checkStringNotNull(path);
        fileName=name;
        filePath=path;
        return this;
    }
    public RequestBuilder createFolder(@NonNull  String path,String folderName){
        Preconditions.checkStringNotNull(path);
        fileName=folderName;
        filePath=path;
        return this;
    }




    public RequestBuilder stickEvent(boolean stickEvent) {
        isStickEvent = stickEvent;
        return this;
    }

    public RequestBuilder addTimeoutCallBack(ITimeOutCallBack xFunc0) {
        this.mTimeoutCallBack=xFunc0;return this;
    }

    public RequestBuilder addErrorCallBack(XFunc1<Throwable> mErrorCallBack) {
        this.mErrorCallBack = mErrorCallBack;return this;
    }

    public <T>  Target into(XFunc1<T> b){
        Target target=  TargetFactory.operate(type,b,this);
        Preconditions.checkNotNull(target);
        Request request= RequestFactory.operate(type,target,this);
        Preconditions.checkNotNull(request);
        target.setRequest(request);
        lifecycle.addListener(target);
        if(request instanceof RequestBroadcastReceiverDisk){
            requestTracker.runStrongRequest(request);
        }else{
            requestTracker.runRequest(request);
        }

        return target;
    }
    @Override
    public boolean getIsKipScanDiskInfo() {
        return isKipScanDiskInfo;
    }

    @Override
    public boolean getIsforcedRefresh(){
        return isforcedRefreshDiskInfo;
    }
    @Override
    public Collection<String> getScanPaths() {
        return checkedPath;
    }


    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public RequestTracker getRequestTracker() {
        return requestTracker;
    }

    @Override
    public <T> XFunc1<T> callback() {
        return null;
    }

    @Override
    public ITimeOutCallBack getTimeOutCallBack() {
        return mTimeoutCallBack;
    }

    @Override
    public Lifecycle getLifecycle() {
        return lifecycle;
    }

    @Override
    public String getScanFileListPath() {
        return filePath;
    }

    @Override
    public String getDelectFile() {
        return filePath;
    }

    @Override
    public String getOperateFilePath() {
        return filePath;
    }

    @Override
    public String getOperateFileName() {
        return fileName;
    }



    public String getType() {
        return type;
    }

    public String getFilePath() {
        return filePath;
    }
    @Override
    public XFunc1<Throwable> getErrorCallBack() {
        return mErrorCallBack;
    }

    public String getFileName() {
        return fileName;
    }
}
