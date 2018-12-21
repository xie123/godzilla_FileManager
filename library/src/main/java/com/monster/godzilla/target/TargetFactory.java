package com.monster.godzilla.target;

import com.monster.godzilla.interfaces.CallBack;
import com.monster.godzilla.load.ICurrentParameter;
import com.monster.godzilla.load.RequestFactory;

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
public class TargetFactory {

    public static   Target operate(String type, CallBack baseTarget, ICurrentParameter requestBuilder) {
        switch (type) {
            case RequestFactory.REQUEST_LOCAL_DISK:
                return  new TargetDiskPathScan((Target.RequestLocalDiskTargetImpl) baseTarget);
            case RequestFactory.REQUEST_DISK_SCAN:
                return new TargetDiskScan(requestBuilder.getIsKipScanDiskInfo(),(Target.RequestScanDiskTargetImpl) baseTarget);
            case RequestFactory.REQUEST_BROADCAST_RECEIVER_DISK:
                return new TargetDiskBroadcastReceiver((Target.RequestBroadcastReceiverDiskTargetImpl) baseTarget);
            case RequestFactory.REQUEST_FILE_LIST:
                return new TargetFileListScan((Target.RequestFileListDiskTargetImpl) baseTarget);
            case RequestFactory.REQUEST_FILE_DELECT:
                return new TargetOperateDelect((Target.RequestOperateFileImpl) baseTarget);
            case RequestFactory.REQUEST_RENAME:
                return new TargetOperate((Target.RequestOperateFileImpl) baseTarget);
            case RequestFactory.REQUEST_FILE_NEW_CREATER:
                return new TargetOperate((Target.RequestOperateFileImpl) baseTarget);
            case RequestFactory.REQUEST_FILE_STICK:
                if(baseTarget instanceof Target.RequestOperateFileImpl){
                        return new TargetOperate((Target.RequestOperateFileImpl) baseTarget);
                }
                if(baseTarget instanceof Target.ReuestCopyImpl){
                    return new TargetOperateCopy((Target.ReuestCopyImpl) baseTarget);
                }
              default:
                break;
        }
        return null;
    }
}
