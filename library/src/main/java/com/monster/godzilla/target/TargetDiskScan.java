package com.monster.godzilla.target;

import com.monster.godzilla.FileManager;
import com.monster.godzilla.bean.FileManagerDiskInfo;
import com.monster.godzilla.interfaces.XFunc1;

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
public class TargetDiskScan extends BaseTarget implements Target.RequestScanDiskTargetImpl {

    private boolean kipScanDiskInfo;


    TargetDiskScan(boolean kipScanDiskInfo, XFunc1<List<FileManagerDiskInfo>> xFunc1) {
        super(xFunc1);
        this.kipScanDiskInfo = kipScanDiskInfo;
    }

    @Override
    public void call(List<FileManagerDiskInfo> diskInfos) {
        if(!kipScanDiskInfo){
            for(FileManagerDiskInfo diskInfo:diskInfos){
                FileManager.init().getDiskInfoManager().putDiskProperty(diskInfo.path,diskInfo);
            }
        }
        if(xFunc1!=null){
            xFunc1.call(diskInfos);
        }
        onDestroy();
    }

    @Override
    public void onStop() {

    }

}

