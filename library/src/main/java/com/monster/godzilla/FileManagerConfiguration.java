package com.monster.godzilla;

import com.monster.godzilla.interfaces.IProvider;
import com.monster.godzilla.operate.FileOperationHelper;
import com.monster.godzilla.operate.FileSortHelper;
import com.monster.godzilla.operate.FilenameTypeFilter;

import java.io.FileFilter;

/**
 * <p>
 * <h2>简述:</h2>
 * <ol>配置一些全局 常量</ol>
 * <h2>功能描述:</h2>
 * <ol>无</ol>
 * <h2>修改历史:</h2>
 * <ol>无</ol>
 * </p>
 *
 * @author 11925
 * @date 2018/11/20/020
 */
public class FileManagerConfiguration implements IProvider<FileManagerConfiguration> {

    /**
     * 是否显示移动磁盘
     */
    private boolean isWhetherToDisplayTheRemovableDisk=true;
    /**
     * 是否显示本地磁盘
     */
    private boolean isWhetherToDisplayLocalDisk=true;


    /***
     * 文件读取/操作  超时 时间
     */
    private long timeOut=1000*30;

    /**
     * 是否读取隐藏文件
     */
    private boolean whetherToReadHiddenFiles=false;
    /**
     * 排序类
     */
    private FileSortHelper fileSortHelper;
    /**
     * 操作类
     */
    private FileOperationHelper fileOperationHelper;




    private FileFilter filenameFilter;

    public FileManagerConfiguration() {
        fileSortHelper=new FileSortHelper();
        filenameFilter=new FilenameTypeFilter();
        fileOperationHelper =new FileOperationHelper(filenameFilter);



    }

    public FileFilter getFilenameFilter() {
        return filenameFilter;
    }

    public void setFilenameFilter(FileFilter filenameFilter) {
        this.filenameFilter = filenameFilter;
    }

    public FileOperationHelper getFileOperationHelper() {
        return fileOperationHelper;
    }


    public FileSortHelper getFileSortHelper() {
        return fileSortHelper;
    }

    public void setSortMethod(FileSortHelper.SortMethod sortMethod) {
        fileSortHelper.setSortMethog(sortMethod);
    }

    public boolean isWhetherToReadHiddenFiles() {
        return whetherToReadHiddenFiles;
    }

    public void setWhetherToReadHiddenFiles(boolean whetherToReadHiddenFiles) {
        this.whetherToReadHiddenFiles = whetherToReadHiddenFiles;
    }

    public long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }

    public boolean isWhetherToDisplayTheRemovableDisk() {
        return isWhetherToDisplayTheRemovableDisk;
    }

    public boolean isWhetherToDisplayLocalDisk() {
        return isWhetherToDisplayLocalDisk;
    }





    @Override
    public FileManagerConfiguration call() {
        return this;
    }



}
