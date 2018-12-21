package com.monster.godzilla.target;

import com.monster.godzilla.FileManager;
import com.monster.godzilla.bean.FileManagerDiskInfo;
import com.monster.godzilla.bean.OperatBean;

import java.util.Map;
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
 * @date 2018/11/22/022
 */
public class TargetOperateDelect extends BaseTarget implements Target.RequestOperateFileCallBack {
     TargetOperateDelect(RequestOperateFileImpl xFunc1) {
        super(xFunc1);
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void call(OperatBean delectBean) {
        if(delectBean.isSuccess){
            FileManagerDiskInfo fileManagerDiskInfo=     FileManager.init().getDiskInfoManager().getDiskProperty(delectBean.path);
            if(fileManagerDiskInfo!=null){
                fileManagerDiskInfo.sizeUsed=fileManagerDiskInfo.sizeUsed-delectBean.mOperateSize;
                fileManagerDiskInfo.sizeAvailable=fileManagerDiskInfo.sizeAvailable+delectBean.mOperateSize;
                fileManagerDiskInfo.fileCount= (int) (fileManagerDiskInfo.fileCount-delectBean.fileCount);

                Map<String /*文件归类类型*/,Set<String>> statisticClass=  delectBean.statisticClass;
                for (String classify:statisticClass.keySet()){
                    fileManagerDiskInfo.removeStatisticClass(classify,statisticClass.get(classify));
                }

                FileManager.init().getDiskInfoManager().putDiskProperty(fileManagerDiskInfo.path,fileManagerDiskInfo);
            }
        }
        if(xFunc1!=null){
            xFunc1.call(delectBean.isSuccess);
        }
        onDestroy();
    }
}
