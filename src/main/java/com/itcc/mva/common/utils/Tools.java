package com.itcc.mva.common.utils;

import java.io.File;

public class Tools {
    /**
     * 查找本地包含字符串的文件名
     * @param path
     * @param fname
     * @return
     */
    public static String findTrueFname(String path,String fname) {
        String trueFname="";
        File file = new File(path);
        File[] fileList = file.listFiles();
        for(int i=0;i<fileList.length;i++) {
            if(fileList[i].toString().contains(fname)) {
                trueFname=fileList[i].toString();
                break;
            }
        }
        return trueFname.substring(trueFname.lastIndexOf("\\")+1, trueFname.length());
    }

    /**
     * 以分隔符 取 最大数组值
     * @param value
     * @param split
     * @return
     */
    public static String getSplitMaxValue(String value,String split) {
        return value.split(split)[value.split(split).length-1];
    }
}
