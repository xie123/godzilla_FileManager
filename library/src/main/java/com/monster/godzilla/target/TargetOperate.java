package com.monster.godzilla.target;

import android.text.TextUtils;

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
public class TargetOperate extends BaseTarget implements Target.RequestOperateFileCallBack {
    TargetOperate(RequestOperateFileImpl xFunc1) {
        super(xFunc1);
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void call(OperatBean operatBean) {

        if (operatBean.operatPath.size() == 0) {
            if (xFunc1 != null) {
                xFunc1.call(false);
            }
            return;
        }
        if(!operatBean.isSuccess){
            if (xFunc1 != null) {
                xFunc1.call(false);
            }
            return;
        }
        FileManagerDiskInfo original = FileManager.init().getDiskInfoManager().getDiskProperty(operatBean.operatPath.get(0));
        FileManagerDiskInfo target = FileManager.init().getDiskInfoManager().getDiskProperty(operatBean.path);


        if(original!=null&&target!=null){
            if(!TextUtils.equals(original.path,target.path)){
                //不是一个磁盘
                original.sizeUsed = original.sizeUsed - operatBean.mOperateSize;
                original.sizeAvailable = original.sizeAvailable + operatBean.mOperateSize;
                original.fileCount = (int) (original.fileCount - operatBean.fileCount);


                Map<String /*文件归类类型*/,Set<String>> statisticClass=  original.getStatisticClass();
                for (String classify:statisticClass.keySet()){
                    original.removeStatisticClass(classify,statisticClass.get(classify));
                }


                FileManager.init().getDiskInfoManager().putDiskProperty(original.path, original);



                target.sizeUsed = target.sizeUsed + operatBean.mOperateSize;
                target.sizeAvailable = target.sizeAvailable - operatBean.mOperateSize;
                target.fileCount = (int) (target.fileCount + operatBean.fileCount);

                 statisticClass=  target.getStatisticClass();
                for (String classify:statisticClass.keySet()){
                    target.removeStatisticClass(classify,statisticClass.get(classify));
                }


                FileManager.init().getDiskInfoManager().putDiskProperty(target.path, target);

            }
        }
        if (xFunc1 != null) {
            xFunc1.call(operatBean.isSuccess);
        }
        onDestroy();
    }
}
