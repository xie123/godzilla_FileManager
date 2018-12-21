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
public class RequestFileRename extends  BaseRequest{
    private String delectPath;
    private String name;

    <T> RequestFileRename(XFunc1<T> xFunc1, ICurrentParameter iCurrentParameter) {
        super(xFunc1,iCurrentParameter);
        this.delectPath=iCurrentParameter.getOperateFilePath();
        this.name=iCurrentParameter.getOperateFileName();
    }


    @Override
    public void doSomething() {
        OperatBean delectBean=new OperatBean();
        delectBean.path=delectPath;
        delectBean.operatPath=new ArrayList<String>(){
            {
                add(delectPath);
            }
        };
        delectBean.mOperateSize =0;
        delectBean.fileCount=0;
        delectBean.isSuccess= mConfiguration.getFileOperationHelper().rename(delectPath,name);
        complete(delectBean);
    }
    @Override
    public void clear() {
        requestId =CANCEL;
        super.clear();
    }
}
