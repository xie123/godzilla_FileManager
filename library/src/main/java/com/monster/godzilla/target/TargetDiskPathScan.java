package com.monster.godzilla.target;

import android.support.annotation.NonNull;

import com.monster.godzilla.DiskInfoManager;
import com.monster.godzilla.FileManager;
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
public class TargetDiskPathScan extends BaseTarget implements Target.RequestLocalDiskTargetImpl {
    private DiskInfoManager diskInfoManager;
     TargetDiskPathScan(XFunc1<List<String>> xFunc1) {
        super(xFunc1);
        diskInfoManager=FileManager.init().getDiskInfoManager();
    }
    @Override
    public void call(@NonNull List<String> scannedPath) {

        diskInfoManager.diskAddAll(scannedPath);
        if(xFunc1!=null){
            xFunc1.call(scannedPath);
        }
        onDestroy();
    }
}
