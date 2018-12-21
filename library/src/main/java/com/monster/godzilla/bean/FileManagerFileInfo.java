package com.monster.godzilla.bean;

import com.monster.godzilla.config.FileType;

import java.io.Serializable;

/**
 * <p>
 * <h2>简述:</h2>
 * <ol>文件信息</ol>
 * <h2>功能描述:</h2>
 * <ol>无</ol>
 * <h2>修改历史:</h2>
 * <ol>无</ol>
 * </p>
 *
 * @author 11925
 * @date 2018/11/20/020
 */
public class FileManagerFileInfo implements Serializable {


    private String path;

    private String fileName;
    private String extensionName;
    private long fileSize;

    private String time;
    private long timestamp;

    private FileType fileType;

    private boolean isDir;

    private boolean canRead;

    private boolean canWrite;

    private boolean isHidden;


    /*
     * 子文件数
     */
    private int childFileNum;
    /**
     * 上一级目录
     */
    private String upperLevelDirectory;


    public String getExtensionName() {
        return extensionName==null?"":extensionName;
    }

    public void setExtensionName(String extensionName) {
        this.extensionName = extensionName;
    }

    public boolean isCanRead() {
        return canRead;
    }

    public void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }

    public boolean isCanWrite() {
        return canWrite;
    }

    public void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public FileManagerFileInfo() {
    }


    public boolean isDir() {
        return isDir;
    }

    public void setDir(boolean dir) {
        isDir = dir;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getChildFileNum() {
        return childFileNum;
    }

    public void setChildFileNum(int childFileNum) {
        this.childFileNum = childFileNum;
    }

    public String getUpperLevelDirectory() {
        return upperLevelDirectory;
    }

    public void setUpperLevelDirectory(String upperLevelDirectory) {
        this.upperLevelDirectory = upperLevelDirectory;
    }





    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }


    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
