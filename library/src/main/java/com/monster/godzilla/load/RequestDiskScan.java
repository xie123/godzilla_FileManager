package com.monster.godzilla.load;

import com.monster.godzilla.bean.FileManagerDiskInfo;
import com.monster.godzilla.interfaces.XFunc1;
import com.monster.godzilla.utlis.FileManagerSdcardUtils;
import com.monster.godzilla.utlis.ThreadManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * <h2>简述:</h2>
 * <ol>扫描 指定磁盘 信息   只返回有效的磁盘信息     </ol>
 * <h2>功能描述:</h2>
 * <ol>无</ol>
 * <h2>修改历史:</h2>
 * <ol>无</ol>
 * </p>
 *
 * @author 11925
 * @date 2018/11/20/020
 */
 public class RequestDiskScan extends BaseRequest {
    private  Set<String> usbPaths;
    private boolean isforcedRefresh;
    private boolean isKipScanDiskInfo;




    <T> RequestDiskScan(XFunc1<T> xFunc1, ICurrentParameter iCurrentParameter){
        super(xFunc1,iCurrentParameter);
        this.usbPaths=new HashSet<>(iCurrentParameter.getScanPaths());
        this.isforcedRefresh=iCurrentParameter.getIsforcedRefresh();
        this.isKipScanDiskInfo=iCurrentParameter.getIsKipScanDiskInfo();
    }



    @Override
    public void timeOut() {
        super.timeOut();
    }

    @Override
    public void doSomething() {


        postTimeOut();
        requestId= (int) System.currentTimeMillis();

        ThreadManager.getInstance().getShortThreadPool().excute(requestId, new ThreadManager.OnExecuteCallback<List<FileManagerDiskInfo>>() {
            @Override
            public void onRun(ThreadManager.Subscriber<? super List<FileManagerDiskInfo>> subscriber) {
                if(isforcedRefresh){
                    List<FileManagerDiskInfo> diskInfos=new ArrayList<>();
                    for (String path : usbPaths) {
                        FileManagerDiskInfo newUsbBean = FileManagerSdcardUtils.getSDCardInfo(path, !isKipScanDiskInfo);
                        if (newUsbBean != null) {
                            newUsbBean.isExsit=true;
                        }else{
                            newUsbBean=new FileManagerDiskInfo();
                            newUsbBean.isExsit=false;
                        }
                        diskInfos.add(newUsbBean);
                    }
                    subscriber.onNext(diskInfos);
                }else {
                    List<FileManagerDiskInfo> diskInfos = new ArrayList<>();
                    for (String path : usbPaths) {
                        boolean hasIn = diskInfoManager.diskPropertyContainsKey(path);
                        if (!hasIn) {
                            FileManagerDiskInfo newUsbBean = FileManagerSdcardUtils.getSDCardInfo(path, !isKipScanDiskInfo);
                            if (newUsbBean != null) {
                                newUsbBean.isExsit = true;
                            } else {
                                newUsbBean = new FileManagerDiskInfo();
                                newUsbBean.isExsit = false;
                            }
                            diskInfos.add(newUsbBean);
                        } else {
                            diskInfos.add(diskInfoManager.getDiskProperty(path));
                        }
                    }
                    subscriber.onNext(diskInfos);
                }

                }

            @Override
            public void onNext(List<FileManagerDiskInfo> fileManagerDiskInfos) {
                    stopTimeOut();
                    if(!isTimeOut.get()){
                        complete(fileManagerDiskInfos);
                    }

            }

            @Override
            public void onError(Throwable throwable) {
                error(throwable);
            }
        });
    }


    @Override
    public void clear() {

        super.clear();
        if(requestId==INIT||requestId==CANCEL){
            return;
        }
        if( ThreadManager.getInstance().unSubscribe(requestId)){
            requestId=CANCEL;
        }
    }
}
