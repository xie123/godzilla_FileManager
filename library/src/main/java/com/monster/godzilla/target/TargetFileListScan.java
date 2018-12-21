package com.monster.godzilla.target;

import com.monster.godzilla.DiskInfoManager;
import com.monster.godzilla.FileManager;
import com.monster.godzilla.bean.FileManagerFileInfo;
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
public class TargetFileListScan extends BaseTarget implements Target.RequestFileListDiskTargetImpl {
    private DiskInfoManager diskInfoManager;
     TargetFileListScan(XFunc1<List<FileManagerFileInfo>> xFunc1) {
        super(xFunc1);
        diskInfoManager=FileManager.init().getDiskInfoManager();
    }
    @Override
    public void call(List<FileManagerFileInfo> strings) {
        if(xFunc1!=null){
            xFunc1.call(strings);
        }
        onDestroy();
    }
}
