package com.monster.godzilla.operate;

import com.monster.godzilla.bean.FileManagerFileInfo;

import java.util.Comparator;
import java.util.HashMap;

/**
 * <p>
 * <h2>简述:</h2>
 * <ol>排序</ol>
 * <h2>功能描述:</h2>
 * <ol>无</ol>
 * <h2>修改历史:</h2>
 * <ol>无</ol>
 * </p>
 *
 * @author 11925
 * @date 2018/12/1/001
 */
public class FileSortHelper {

    public enum SortMethod {
        name, size, date, type
    }

    private SortMethod mSort=SortMethod.type;
    private boolean mFileFirst;
    private HashMap<SortMethod, Comparator> mComparatorList = new HashMap<>();

    public FileSortHelper() {
        mSort = SortMethod.name;
        mComparatorList.put(SortMethod.name, cmpName);
        mComparatorList.put(SortMethod.size, cmpSize);
        mComparatorList.put(SortMethod.date, cmpDate);
        mComparatorList.put(SortMethod.type, cmpType);
    }

    public void setSortMethog(SortMethod s) {
        mSort = s;
    }

    public SortMethod getSortMethod() {
        return mSort;
    }

    public void setFileFirst(boolean f) {
        mFileFirst = f;
    }
    public Comparator getComparator() {
        return mComparatorList.get(mSort);
    }

    private abstract class FileComparator implements Comparator<FileManagerFileInfo> {

        @Override
        public int compare(FileManagerFileInfo object1, FileManagerFileInfo object2) {
            if (object1.isDir() == object2.isDir()) {
                return doCompare(object1, object2);
            }

            if (mFileFirst) {
                // the files are listed before the dirs
                return (object1.isDir() ? 1 : -1);
            } else {
                // the dir-s are listed before the files
                return object1.isDir() ? -1 : 1;
            }
        }

        protected abstract int doCompare(FileManagerFileInfo object1, FileManagerFileInfo object2);
    }

    public Comparator cmpName = new FileComparator() {
        @Override
        public int doCompare(FileManagerFileInfo object1, FileManagerFileInfo object2) {
            return object1.getFileName().compareToIgnoreCase(object2.getFileName());
        }
    };

    public Comparator cmpSize = new FileComparator() {
        @Override
        public int doCompare(FileManagerFileInfo object1, FileManagerFileInfo object2) {
            return longToCompareInt(object1.getFileSize() - object2.getFileSize());
        }
    };

    public Comparator cmpDate = new FileComparator() {
        @Override
        public int doCompare(FileManagerFileInfo object1, FileManagerFileInfo object2) {
            return longToCompareInt(object2.getTimestamp() - object1.getTimestamp());
        }
    };

    private int longToCompareInt(long result) {
        return result > 0 ? 1 : (result < 0 ? -1 : 0);
    }

    public Comparator cmpType = new FileComparator() {
        @Override
        public int doCompare(FileManagerFileInfo lhs, FileManagerFileInfo another1) {
            int result =lhs.getExtensionName().compareToIgnoreCase( another1.getExtensionName());
            if (result != 0) {
                return result;
            }
            return lhs.getFileName().compareToIgnoreCase(another1.getFileName());
        }
    };

}
