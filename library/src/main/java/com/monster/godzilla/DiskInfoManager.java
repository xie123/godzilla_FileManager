package com.monster.godzilla;

import android.support.annotation.NonNull;

import com.monster.godzilla.bean.FileManagerDiskInfo;
import com.monster.godzilla.config.FileType;
import com.monster.godzilla.bean.file.ApkFile;
import com.monster.godzilla.bean.file.FolderFile;
import com.monster.godzilla.bean.file.ImageFile;
import com.monster.godzilla.bean.file.MusicFile;
import com.monster.godzilla.bean.file.NoneFile;
import com.monster.godzilla.bean.file.PsdFile;
import com.monster.godzilla.bean.file.TxtFile;
import com.monster.godzilla.bean.file.VideoFile;
import com.monster.godzilla.bean.file.WpsExcelFile;
import com.monster.godzilla.bean.file.WpsPdfFile;
import com.monster.godzilla.bean.file.WpsPptFile;
import com.monster.godzilla.bean.file.WpsWordFile;
import com.monster.godzilla.bean.file.ZipFile;
import com.monster.godzilla.interfaces.IFileManagerFileType;
import com.monster.godzilla.interfaces.IProvider;
import com.monster.godzilla.utlis.FileManagerUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
 * @date 2018/11/20/020
 */
public class DiskInfoManager implements IProvider<DiskInfoManager> {

    final Set<String> mDiskPaths;

    private Map<String/*磁盘路径*/, FileManagerDiskInfo> mDiskAllInfo;

    private final LinkedHashMap<String /*文件类型*/, FileType> fileTypes = new LinkedHashMap<>();
    //
    private final LinkedHashMap<String /*文件类型*/, String/*文件归档类型 */> classTypes = new LinkedHashMap<>();

    public DiskInfoManager() {
        this.mDiskPaths =Collections.synchronizedSet(new HashSet<>());
        mDiskAllInfo = new LinkedHashMap<>();


        addFileType(new ApkFile());
        addFileType(new ImageFile());
        addFileType(new MusicFile());
        addFileType(new PsdFile());
        addFileType(new TxtFile());
        addFileType(new VideoFile());
        addFileType(new WpsExcelFile());
        addFileType(new WpsPdfFile());
        addFileType(new WpsPptFile());
        addFileType(new ZipFile());
        addFileType(new WpsWordFile());
        addFileType(new FolderFile());
        addFileType(new NoneFile());
        
        
        
    }

    @Override
    public DiskInfoManager call() {
        return this;
    }
    ///////////////////////文件归类///////////////////////

    public boolean classifyContains(String classify){
        return classTypes.containsValue(classify);
    }

////////////////////////////文件类型 过滤 //////////////////////////////////////////////

    /**
     * 通过 归类类型 获取 文件类型
     * @param fileClassify
     * @return
     */
    public List<FileType> getFileClassify(String fileClassify) {
        List<FileType> fileClass=new ArrayList<>();
        for (String set : fileTypes.keySet()) {
            FileType fileType1 = fileTypes.get(set);
            if (fileType1.accept().equals(fileClassify)) {
                fileClass.add(fileType1);
            }
        }
        return fileClass;
    }

    /**
     * 通过文件
     * @param fileType
     * @return
     */
    public FileType getFileTypeByType(String fileType) {
        return fileTypes.get(fileType);
    }

    public  void addFileType(FileType fileType) {
        fileTypes.put(fileType.getType(), fileType);
        classTypes.put(fileType.getType(),fileType.accept());
    }

    /**
     * 通过文件路径获取类型
     * @param filePath
     * @return
     */
    public FileType getFileTypeByPath(String filePath) {
        for (String set : fileTypes.keySet()) {
            FileType fileType1 = fileTypes.get(set);
            if (!fileType1.getType().equals(IFileManagerFileType.NONE)) {
                if (fileType1.judge(filePath)) {
                    return fileType1;
                }
            }
        }
        return fileTypes.get(IFileManagerFileType.NONE);
    }

    /**
     * 通过文件获取类型
     * @param file
     * @return
     */
    public FileType getFileTypeByPath(File file) {
        return getFileTypeByPath(file.getAbsolutePath());
    }

    ///////////////////////////////磁盘 属性的 操作//////////////////////////////////////////////////////////////


    public int diskPropertysize() {
        return mDiskAllInfo.size();
    }

    public boolean diskPropertyContainsKey(String key) {
        return mDiskAllInfo.containsKey(key);
    }


    public boolean diskPropertyContainsValue(FileManagerDiskInfo value) {
        return mDiskAllInfo.containsValue(value);
    }


    public FileManagerDiskInfo getDiskProperty(String key) {
        for (String path:mDiskAllInfo.keySet()){
            if(FileManagerUtil.containsPath(path,key)){
                return mDiskAllInfo.get(path);
            }
        }
        return null;
    }


    public FileManagerDiskInfo putDiskProperty(String key, FileManagerDiskInfo value) {
        return mDiskAllInfo.put(key, value);
    }


    public FileManagerDiskInfo removeDiskProperty(String key) {
        return mDiskAllInfo.remove(key);
    }


    public void putAllDiskProperty(@NonNull Map<? extends String, ? extends FileManagerDiskInfo> m) {
        mDiskAllInfo.putAll(m);
    }


    public void clearDiskProperty() {
        mDiskAllInfo.clear();
    }

    public List<FileManagerDiskInfo> toListDiskProperty() {
        Collection<FileManagerDiskInfo> set = mDiskAllInfo.values();
        return new ArrayList<>(set);
    }


    /////////////////////////磁盘 的 操作 ///////////////////////////////////////////////////////////////
    public  synchronized  int diskSize() {
        return mDiskPaths.size();
    }

    public synchronized boolean diskContains(String o) {
        for (String path:mDiskPaths){
            if(FileManagerUtil.containsPath(path,o)){
                return true;
            }
        }
        return false;
    }

    public synchronized boolean diskAdd(String s) {
        return mDiskPaths.add(s);
    }


    public synchronized boolean diskRemove(String o) {
        return mDiskPaths.remove(o);
    }


    public  synchronized boolean diskContainsAll(@NonNull Collection<?> c) {
        return mDiskPaths.containsAll(c);
    }


    public synchronized boolean diskAddAll(@NonNull Collection<? extends String> c) {
        return mDiskPaths.addAll(c);
    }


    public synchronized boolean diskRemoveAll(@NonNull Collection<?> c) {
        return mDiskPaths.removeAll(c);
    }


    public synchronized boolean diskRetainAll(@NonNull Collection<?> c) {
        return mDiskPaths.retainAll(c);
    }


    public synchronized void diskClear() {
        mDiskPaths.clear();
    }

    public  synchronized Set<String> getAllDiskPaths() {
        return new HashSet<String>(){{
            addAll(mDiskPaths);
        }};
    }
}
