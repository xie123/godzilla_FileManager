package com.monster.godzilla.load;

import com.monster.godzilla.bean.FileManagerDiskInfo;
import com.monster.godzilla.bean.OperatBean;
import com.monster.godzilla.interfaces.XFunc1;
import com.monster.godzilla.utlis.FileManagerScanUtil;
import com.monster.godzilla.utlis.FileUtils;
import com.monster.godzilla.utlis.ThreadManager;

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
 * @date 2018/11/22/022
 */
public class RequestFileDelect extends  BaseRequest{
    private String delectPath;

     <T>RequestFileDelect(XFunc1<T> xFunc1,ICurrentParameter iCurrentParameter) {
        super(xFunc1,iCurrentParameter); this.delectPath=iCurrentParameter.getDelectFile();
    }


    @Override
    public void doSomething() {
        requestId= (int) System.currentTimeMillis();

        ThreadManager.getInstance().getShortThreadPool().excute(requestId, new ThreadManager.OnExecuteCallback<OperatBean>() {
            @Override
            public void onRun(ThreadManager.Subscriber<? super OperatBean> subscriber) {
                OperatBean delectBean=new OperatBean();
                delectBean.path=delectPath;
                delectBean.operatPath=new ArrayList<String>(){ {
                        add(delectPath);
                    }
                };
                delectBean.mOperateSize =FileUtils.getDirLength(delectPath);

                FileManagerDiskInfo fileManagerDiskInfo=new FileManagerDiskInfo();
                FileManagerScanUtil.findDirectory(fileManagerDiskInfo,new File(delectPath));
                delectBean.statisticClass=fileManagerDiskInfo.getStatisticClass();
                delectBean.fileCount=fileManagerDiskInfo.fileCount==0?1:fileManagerDiskInfo.fileCount;
                delectBean.isSuccess= mConfiguration.getFileOperationHelper().delete(delectPath);

                subscriber.onNext(delectBean);
            }

            @Override
            public void onNext(OperatBean operatBean) {
                complete(operatBean);
            }

            @Override
            public void onError(Throwable throwable) {
                error(throwable);
            }
        });

    }
    @Override
    public void clear() {
        if( ThreadManager.getInstance().unSubscribe(requestId)){
            requestId =CANCEL;
        }
        super.clear();
    }
}
