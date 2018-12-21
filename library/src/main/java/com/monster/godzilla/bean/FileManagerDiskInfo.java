package com.monster.godzilla.bean;

import com.monster.godzilla.FileManager;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
public class FileManagerDiskInfo implements Serializable {
    public String path;
    public boolean islocalUsb;
    public boolean isExsit = true;
    public String name;

    public int fileCount;

    public boolean canWrite;
    public boolean canRead;


    public long sizeAvailable;

    public long sizeUsed;

    public long sizeTotal;


    /**
     * 统计该磁盘下 需要统计的 文件类型数
     */
    private Map<String /*文件归类类型*/,Set<String>> statisticClass;

    /**
     * 文件夹下的文件夹数量
     */
    private Map<String,Integer> numberOfFilesUnderTheFolder;



    public FileManagerDiskInfo() {
        statisticClass=new LinkedHashMap<>();
        numberOfFilesUnderTheFolder=new LinkedHashMap<>();
    }


    public Map<String/*归类类型 classify*/, Set<String/*路径地址*/>> getStatisticClass() {
        return statisticClass==null?new LinkedHashMap<>():statisticClass;
    }

    public synchronized void removeStatisticClass(String classify,Set<String> filePath){
        if(filePath==null||filePath.size()==0){
            return;
        }
        if(statisticClass.containsKey(classify)){
            Set<String> strings=  statisticClass.get(classify);
            strings.removeAll(filePath);
            statisticClass.put(classify,strings);
        }
    }
    public synchronized void addStatisticClass(String classify, Set<String> filePath) {
        statisticClass.put(classify,filePath);
    }
    public synchronized void addStatisticClass( String classify, String filePath) {
        if(!FileManager.init().getDiskInfoManager().classifyContains(classify)){
            return;
        }
        if(statisticClass.containsKey(classify)){
            Set<String> strings=statisticClass.get(classify);
            strings.add(filePath);
            statisticClass.put(classify,strings);
        }else{
            Set<String> strings=new HashSet<>();
            strings.add(filePath);
            statisticClass.put(classify,strings);
        }
    }

    public void addNumberOfFilesUnderTheFolder(String folderPath, int num) {
        if(numberOfFilesUnderTheFolder.containsKey(folderPath)){
            numberOfFilesUnderTheFolder.put(folderPath,numberOfFilesUnderTheFolder.get(folderPath)+num);
        }else{
            numberOfFilesUnderTheFolder.put(folderPath,num);
        }
    }

    public int getNumberOfFilesUnderTheFolder(String path) {
        Integer integer=numberOfFilesUnderTheFolder.get(path);
        if(integer==null){
            return 0;
        }
        return integer;
    }

    public synchronized void increaseFileCount() {
        increaseFileCount(1);
    }

    public synchronized void increaseFileCount(int count) {
        this.fileCount += count;
    }



}
