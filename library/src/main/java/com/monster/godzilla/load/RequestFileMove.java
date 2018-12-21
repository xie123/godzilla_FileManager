package com.monster.godzilla.load;

import com.monster.godzilla.bean.FileManagerDiskInfo;
import com.monster.godzilla.bean.OperatBean;
import com.monster.godzilla.interfaces.XFunc1;
import com.monster.godzilla.utlis.FileManagerScanUtil;

import java.io.File;
import java.util.ArrayList;

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
 * @date 2018/12/15/015
 */
public class RequestFileMove extends BaseRequest {
    private String filePath;

    <T> RequestFileMove(XFunc1<T> xFunc1,ICurrentParameter iCurrentParameter) {
        super(xFunc1,iCurrentParameter);filePath=iCurrentParameter.getOperateFilePath();
    }



    @Override
    public void doSomething() {
        OperatBean delectBean = new OperatBean();

        if (mConfiguration.getFileOperationHelper().isMoveState()) {
            delectBean.isSuccess = false;
        } else if (!mConfiguration.getFileOperationHelper().canMove(filePath)) {
            delectBean.isSuccess = false;
        } else if (mConfiguration.getFileOperationHelper().isNeedToMove()) {
            delectBean.path = filePath;
            delectBean.operatPath = new ArrayList<String>() {
                {
                    addAll(mConfiguration.getFileOperationHelper().getFileList());
                }
            };

            FileManagerDiskInfo fileManagerDiskInfo;
            for (String path : delectBean.operatPath) {
                fileManagerDiskInfo =new FileManagerDiskInfo();
                FileManagerScanUtil.findDirectory(fileManagerDiskInfo,new File(path));
                if(delectBean.statisticClass==null){
                    delectBean.statisticClass=fileManagerDiskInfo.getStatisticClass();
                }else{
                    delectBean.statisticClass.putAll(fileManagerDiskInfo.getStatisticClass());
                }
                int num=fileManagerDiskInfo.fileCount==0?1:fileManagerDiskInfo.fileCount;
                delectBean.fileCount= delectBean.fileCount + num;

            }

            delectBean.isSuccess = mConfiguration.getFileOperationHelper().startMove(filePath);
        } else {
            delectBean.isSuccess = false;
        }
        complete(delectBean);
    }


    @Override
    public void clear() {
        requestId = CANCEL;
        super.clear();
    }
}
