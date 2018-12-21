package com.monster.godzilla.target;

import com.monster.godzilla.bean.CopyBean;
import com.monster.godzilla.bean.FileManagerDiskInfo;
import com.monster.godzilla.bean.FileManagerFileInfo;
import com.monster.godzilla.bean.OperatBean;
import com.monster.godzilla.interfaces.XFunc1;
import com.monster.godzilla.manager.Request;
import com.monster.godzilla.manager.lifecycle.LifecycleListener;

import java.util.Collection;
import java.util.List;

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
public interface Target extends LifecycleListener {
    void setRequest(Request request);

    Request getRequest();



    /**
     * 扫描磁盘信息监听回调
     */
    public interface RequestScanDiskTargetImpl extends XFunc1<List<FileManagerDiskInfo>> {

    }

    /**
     * 扫描磁盘路径回调
     */
    public interface RequestLocalDiskTargetImpl extends XFunc1<List<String>> {
        @Override
        void call(List<String> strings);
    }

    /**
     * 扫描磁盘文件
     */
    public interface RequestFileListDiskTargetImpl extends XFunc1<List<FileManagerFileInfo>> {

    }

    /**
     * 广播 回调
     */
    public interface RequestBroadcastReceiverDiskTargetImpl extends XFunc1<Collection<String>> {
        /**
         * 返回的是当前存在的
         * @param diskAddPath
         */
        @Override
        void call(Collection<String> diskAddPath);
    }

    public interface RequestOperateFileCallBack extends XFunc1<OperatBean> {
    }
    /**
     * 操作回调
     */
    public interface RequestOperateFileImpl extends XFunc1<Boolean> {

    }


    public interface ReuestCopyImpl extends XFunc1<CopyBean>{
        //开始复制
        void onStartCopy();
        //暂停复制
        void onPauseCopy();
        //更新 数据
        void postUpdateCopy(String fileName, long allSize, long hasDelete, int progress);
        //取消复制
        void onCancelCopy(String path, long mCopyedSize, List<String> mCopySuccessfulPath);
        //结束 复制
        void onFinishCopy(long hasDeletedSize);
        //恢复 复制
        void onResumeCopy();

        //成功 复制
        @Override
        void call(CopyBean copyBean);
    }


}
