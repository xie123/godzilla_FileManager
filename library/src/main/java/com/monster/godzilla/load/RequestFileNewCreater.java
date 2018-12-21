package com.monster.godzilla.load;

import com.monster.godzilla.bean.OperatBean;
import com.monster.godzilla.interfaces.XFunc1;

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
 * @date 2018/11/30/030
 */
public class RequestFileNewCreater extends  BaseRequest{
    private String filePath;
    private String fileName;

    <T> RequestFileNewCreater(XFunc1<T> xFunc1, ICurrentParameter iCurrentParameter) {
        super(xFunc1,iCurrentParameter);
        this.filePath=iCurrentParameter.getOperateFilePath();
        this.fileName=iCurrentParameter.getOperateFileName();
    }



    @Override
    public void doSomething() {


        OperatBean delectBean=new OperatBean();
        delectBean.path=filePath;
        delectBean.operatPath=new ArrayList<String>(){
            {
                add(filePath);
            }
        };
        delectBean.mOperateSize =0;
        delectBean.fileCount=1;
        delectBean.isSuccess= mConfiguration.getFileOperationHelper().createFolder(filePath,fileName);


        complete(delectBean);
    }


    @Override
    public void clear() {
        requestId =CANCEL;
        super.clear();
    }
}
