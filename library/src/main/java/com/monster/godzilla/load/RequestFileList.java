package com.monster.godzilla.load;

import com.monster.godzilla.FileManager;
import com.monster.godzilla.bean.FileManagerFileInfo;
import com.monster.godzilla.interfaces.XFunc1;
import com.monster.godzilla.utlis.FileManagerFileUtils;
import com.monster.godzilla.utlis.Preconditions;
import com.monster.godzilla.utlis.ThreadManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * <h2>简述:</h2>
 * <ol>通过路径获取 文件列表 </ol>
 * <h2>功能描述:</h2>
 * <ol>无</ol>
 * <h2>修改历史:</h2>
 * <ol>无</ol>
 * </p>
 *
 * @author 11925
 * @date 2018/11/22/022
 */
public class RequestFileList extends  BaseRequest {
    private String fileListPath;

    <T> RequestFileList(XFunc1<T> xFunc1 ,ICurrentParameter iCurrentParameter) {
        super(xFunc1,iCurrentParameter);
        this.fileListPath=iCurrentParameter.getScanFileListPath();
    }


    @Override
    public void doSomething() {
        Preconditions.checkNotNull(fileListPath);

        postTimeOut();

        requestId= (int) System.currentTimeMillis();

        ThreadManager.getInstance().getShortThreadPool().excute(requestId, new ThreadManager.OnExecuteCallback<List<FileManagerFileInfo>>() {
            @Override
            public void onRun(ThreadManager.Subscriber<? super List<FileManagerFileInfo>> subscriber) {
                List<FileManagerFileInfo> datas = new ArrayList<>();
                File[] files = FileManagerFileUtils.fileFilter(new File(fileListPath));
                if (files != null) {
                    for (File file : files) {
                        if (file.exists()) {
                            FileManagerFileInfo fileBean = FileManagerFileUtils.getFileInfoFromFile(file);
                            datas.add(fileBean);
                        }
                    }
                }

                Collections.sort(datas, FileManager.init().getFileManagerConfiguration().getFileSortHelper().cmpType);

                subscriber.onNext(datas);
            }

            @Override
            public void onNext(List<FileManagerFileInfo> fileManagerFileInfos) {
                stopTimeOut();
                if(!isTimeOut.get()){
                    complete(fileManagerFileInfos);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                error(throwable);
                throwable.printStackTrace();
            }
        });

    }



    @Override
    public void clear() {


        super.clear();
        if(requestId ==INIT|| requestId ==CANCEL){
            return;
        }
        if(ThreadManager.getInstance().unSubscribe(requestId)){
            requestId =CANCEL;
        }

    }

}
