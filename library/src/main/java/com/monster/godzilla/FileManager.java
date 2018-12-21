package com.monster.godzilla;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.monster.godzilla.manager.Request;
import com.monster.godzilla.manager.lifecycle.RequestManagerRetriever;
import com.monster.godzilla.target.Target;
import com.monster.godzilla.utlis.DoubleCheck;
import com.monster.godzilla.interfaces.IProvider;
import com.monster.godzilla.utlis.Preconditions;

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
public class FileManager {
    private IProvider<FileManagerConfiguration> mFileManagerConfigurationProvider;
    private IProvider<DiskInfoManager> mDiskInfoManager;


    static class FileManagers {
        static FileManager fileManager=new FileManager();
    }

    private FileManager() {
        mFileManagerConfigurationProvider=  DoubleCheck.provider(new FileManagerConfiguration());
        mDiskInfoManager=   DoubleCheck.provider(new DiskInfoManager());

    }

    public static FileManager init(){
        return FileManagers.fileManager;
    }


    public FileManagerConfiguration getFileManagerConfiguration() {
        Preconditions.checkNotNull(mFileManagerConfigurationProvider);
        return mFileManagerConfigurationProvider.call();
    }

    public DiskInfoManager getDiskInfoManager() {
        Preconditions.checkNotNull(mDiskInfoManager);
        return mDiskInfoManager.call();
    }


    public static void clear(Target target) {
        Request request = target.getRequest();
        if (request != null) {
            request.clear();
            target.setRequest(null);
        }
    }



    public static RequestManager with(Context context) {
        RequestManagerRetriever retriever = RequestManagerRetriever.get();
        return retriever.get(context);
    }
    public static RequestManager with(android.support.v4.app.Fragment fragment) {
        RequestManagerRetriever retriever = new RequestManagerRetriever();
        return retriever.get(fragment);
    }
    public static RequestManager with(Fragment fragment) {
        RequestManagerRetriever retriever = new RequestManagerRetriever();
        return retriever.get(fragment);
    }
    public static RequestManager with(Activity activity) {
        RequestManagerRetriever retriever = new RequestManagerRetriever();
        return retriever.get(activity);
    }
    public static RequestManager with(FragmentActivity activity) {
        RequestManagerRetriever retriever = new RequestManagerRetriever();
        return  retriever.get(activity);
    }
}
