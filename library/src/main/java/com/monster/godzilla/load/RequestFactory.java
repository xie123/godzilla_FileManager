package com.monster.godzilla.load;

import android.support.annotation.StringDef;

import com.monster.godzilla.FileManager;
import com.monster.godzilla.RequestBuilder;
import com.monster.godzilla.manager.Request;
import com.monster.godzilla.target.Target;
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
 * @date 2018/11/21/021
 */
public class RequestFactory {
    public static final String REQUEST_LOCAL_DISK = "RequestDiskPath";
    public static final String REQUEST_DISK_SCAN = "RequestDiskScan";
    public static final String REQUEST_BROADCAST_RECEIVER_DISK = "RequestBroadcastReceiverDiskTargetImpl";
    public static final String REQUEST_FILE_LIST = "RequestFileList";

    public static final String REQUEST_FILE_DELECT = "RequestFileDelect";
    public static final String REQUEST_FILE_STICK = "REQUEST_FILE_STICK";
    public static final String REQUEST_RENAME = "RequestFileRename";
    public static final String REQUEST_FILE_NEW_CREATER = "RequestFileNewCreater";
    @StringDef({REQUEST_LOCAL_DISK, REQUEST_DISK_SCAN, REQUEST_BROADCAST_RECEIVER_DISK, REQUEST_FILE_LIST, REQUEST_FILE_DELECT, REQUEST_FILE_STICK, REQUEST_RENAME, REQUEST_FILE_NEW_CREATER})
    public @interface IRequestType {

    }
    public static Request operate(String type, Target target,RequestBuilder builder) {
        switch (type) {
            case RequestFactory.REQUEST_LOCAL_DISK:
                return new RequestDiskPath( (Target.RequestLocalDiskTargetImpl) target,builder);
            case RequestFactory.REQUEST_DISK_SCAN:
                Preconditions.checkNotNull(builder.getScanPaths());
                return new RequestDiskScan( (Target.RequestScanDiskTargetImpl)target,builder);
            case RequestFactory.REQUEST_BROADCAST_RECEIVER_DISK:
                return new RequestBroadcastReceiverDisk((Target.RequestBroadcastReceiverDiskTargetImpl)target,builder);
            case RequestFactory.REQUEST_FILE_LIST:
                Preconditions.checkNotNull(builder.getFilePath());
                return new RequestFileList((Target.RequestFileListDiskTargetImpl)target,builder);

            case RequestFactory.REQUEST_FILE_DELECT:
                return new RequestFileDelect((Target.RequestOperateFileCallBack)target, builder);

            case RequestFactory.REQUEST_FILE_STICK:
                if(FileManager.init().getFileManagerConfiguration().getFileOperationHelper().isNeedToMove()){
                    return new RequestFileMove((Target.RequestOperateFileCallBack)target,builder);
                }
                return new RequestFileStick((Target.ReuestCopyImpl)target,builder);
            case RequestFactory.REQUEST_FILE_NEW_CREATER:
                return new RequestFileNewCreater((Target.RequestOperateFileCallBack)target,builder);
            case RequestFactory.REQUEST_RENAME:
                if(target instanceof Target.RequestOperateFileCallBack){
                    return new RequestFileRename((Target.RequestOperateFileCallBack)target,builder);
                }
            default:
                break;
        }
        return null;
    }
}
