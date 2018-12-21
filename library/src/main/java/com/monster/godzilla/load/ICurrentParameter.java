package com.monster.godzilla.load;

import android.content.Context;

import com.monster.godzilla.interfaces.ITimeOutCallBack;
import com.monster.godzilla.interfaces.XFunc1;
import com.monster.godzilla.manager.RequestTracker;
import com.monster.godzilla.manager.lifecycle.Lifecycle;

import java.util.Collection;

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
 * @date 2018/12/19/019
 */
public interface ICurrentParameter {
    public XFunc1<Throwable> getErrorCallBack();
    public RequestTracker getRequestTracker();
    public <T> XFunc1<T> callback();
    public ITimeOutCallBack getTimeOutCallBack();


    public Collection<String> getScanPaths();
    public boolean getIsforcedRefresh();
   public boolean getIsKipScanDiskInfo();


    Context getContext();

    Lifecycle getLifecycle();

    String getScanFileListPath();

    String getDelectFile();

    String getOperateFilePath();

    String getOperateFileName();
}
