package com.monster.godzilla.target;

import android.text.TextUtils;

import com.monster.godzilla.FileManager;
import com.monster.godzilla.bean.CopyBean;
import com.monster.godzilla.bean.FileManagerDiskInfo;
import com.monster.godzilla.utlis.FileManagerScanUtil;
import com.monster.godzilla.utlis.HandlerUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
public class TargetOperateCopy extends BaseTarget implements Target.ReuestCopyImpl {
     TargetOperateCopy(ReuestCopyImpl xFunc1) {
        super(xFunc1);
    }

    public TargetOperateCopy(RequestOperateFileImpl baseTarget) {
        super(baseTarget);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStartCopy() {
        if(xFunc1==null){
            return;
        }
        HandlerUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(xFunc1 instanceof ReuestCopyImpl){
                    ((ReuestCopyImpl)xFunc1).onStartCopy();
                }
            }
        });

    }

    @Override
    public void onPauseCopy() {
        if(xFunc1==null){
            return;
        }
         HandlerUtil.runOnUiThread(new Runnable() {
             @Override
             public void run() {
                 if(xFunc1 instanceof ReuestCopyImpl){
                     ((ReuestCopyImpl)xFunc1).onPauseCopy();
                 }

             }
         });

    }

    @Override
    public void postUpdateCopy(String fileName, long allSize, long hasDelete, int progress) {
         if(xFunc1==null){
             return;
         }
         HandlerUtil.runOnUiThread(new Runnable() {
             @Override
             public void run() {
                 if(xFunc1 instanceof ReuestCopyImpl){
                     ((ReuestCopyImpl)xFunc1).postUpdateCopy(fileName,allSize,hasDelete,progress);
                 }
             }
         });

    }

    @Override
    public void onCancelCopy(String mDestinationPath,long mCopyedSize, List<String> mCopySuccessfulPath) {
        if(xFunc1==null){
            return;
        }
        CopyBean copyBean=new CopyBean();
        copyBean.mDestinationPath = mDestinationPath;
        copyBean.mSourceFiles = new ArrayList<String>() {
            {
                addAll(mCopySuccessfulPath);
            }
        };
        copyBean.mCopyedSize = mCopyedSize;

        dataProcessing(copyBean);
        HandlerUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(xFunc1 instanceof ReuestCopyImpl){
                    ((ReuestCopyImpl)xFunc1).onCancelCopy(mDestinationPath,mCopyedSize, mCopySuccessfulPath);
                }else if(xFunc1 instanceof RequestOperateFileImpl){
                    ((RequestOperateFileImpl)xFunc1).call(true);
                }
            }
        });
    }

    @Override
    public void onFinishCopy(long hasDeletedSize) {
        if(xFunc1==null){
            return;
        }
        HandlerUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(xFunc1 instanceof ReuestCopyImpl){
                    ((ReuestCopyImpl)xFunc1).onFinishCopy(hasDeletedSize);
                }
            }
        });
    }

    @Override
    public void onResumeCopy() {
        if(xFunc1==null){
            return;
        }
        HandlerUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(xFunc1 instanceof ReuestCopyImpl){
                    ((ReuestCopyImpl)xFunc1).onResumeCopy();
                }
        }
        });

    }

    @Override
    public void call(CopyBean copyBean) {
        if(xFunc1==null){
            return;
        }
        dataProcessing(copyBean);
        HandlerUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(xFunc1 instanceof ReuestCopyImpl){
                    ((ReuestCopyImpl)xFunc1).call(copyBean);
                }else if(xFunc1 instanceof RequestOperateFileImpl){
                    ((RequestOperateFileImpl)xFunc1).call(true);
                }
            }
        });

    }

    private void dataProcessing(CopyBean copyBean){
        FileManagerDiskInfo original = FileManager.init().getDiskInfoManager().getDiskProperty(copyBean.mSourceFiles.get(0));

        FileManagerDiskInfo target = FileManager.init().getDiskInfoManager().getDiskProperty(copyBean.mDestinationPath);

        FileManagerDiskInfo fileManagerDiskInfo;
        for (String path : copyBean.mSourceFiles) {
            fileManagerDiskInfo =new FileManagerDiskInfo();
            FileManagerScanUtil.findDirectory(fileManagerDiskInfo,new File(path));
            if(copyBean.statisticClass==null){
                copyBean.statisticClass=fileManagerDiskInfo.getStatisticClass();
            }else{
                copyBean.statisticClass.putAll(fileManagerDiskInfo.getStatisticClass());
            }
            int num=fileManagerDiskInfo.fileCount==0?1:fileManagerDiskInfo.fileCount;
            copyBean.fileCount= copyBean.fileCount + num;

        }


        if(original!=null&&target!=null){
            if(!TextUtils.equals(original.path,target.path)){


                original.sizeUsed = original.sizeUsed - copyBean.mCopyedSize;
                original.sizeAvailable = original.sizeAvailable + copyBean.mCopyedSize;
                original.fileCount = (int) (original.fileCount - copyBean.fileCount);

                Map<String /*文件归类类型*/,Set<String>> statisticClass=  original.getStatisticClass();
                for (String classify:statisticClass.keySet()){
                    original.removeStatisticClass(classify,statisticClass.get(classify));
                }


                FileManager.init().getDiskInfoManager().putDiskProperty(original.path, original);



                target.sizeUsed = target.sizeUsed + copyBean.mCopyedSize;
                target.sizeAvailable = target.sizeAvailable - copyBean.mCopyedSize;
                target.fileCount = (int) (target.fileCount + copyBean.fileCount);

                 statisticClass=  target.getStatisticClass();
                for (String classify:statisticClass.keySet()){
                    target.removeStatisticClass(classify,statisticClass.get(classify));
                }

                FileManager.init().getDiskInfoManager().putDiskProperty(target.path, target);

            }else{


                target.sizeUsed = target.sizeUsed + copyBean.mCopyedSize;
                target.sizeAvailable = target.sizeAvailable - copyBean.mCopyedSize;
                target.fileCount =  (target.fileCount + copyBean.fileCount);

                Map<String /*文件归类类型*/,Set<String>> statisticClass=  target.getStatisticClass();
                for (String classify:statisticClass.keySet()){
                    target.removeStatisticClass(classify,statisticClass.get(classify));
                }


                FileManager.init().getDiskInfoManager().putDiskProperty(target.path, target);
            }
        }
    }
}
