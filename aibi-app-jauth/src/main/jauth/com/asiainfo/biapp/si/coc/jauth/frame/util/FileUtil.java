/*
 * @(#)FileUtil.java
 *
 * CopyRight (c) 2018 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.biapp.si.coc.jauth.frame.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.asiainfo.biapp.si.coc.jauth.frame.dao.BaseDao;

import au.com.bytecode.opencsv.CSVWriter;




/**
 * Title : FileUtil
 * <p/>
 * Description : 
 * <p/>
 * CopyRight : CopyRight (c) 2018
 * <p/>
 * Company : 北京亚信智慧数据科技有限公司
 * <p/>
 * JDK Version Used : JDK 5.0 +
 * <p/>
 * Modification History	:
 * <p/>
 * <pre>NO.    Date    Modified By    Why & What is modified</pre>
 * <pre>1    2018年1月11日    zhangnan7        Created</pre>
 * <p/>
 *
 * @author  zhangnan7
 * @version 1.0.0.2018年1月11日
 */
public class FileUtil {


    /**
     * sql生成文件（csv or text）
     * @param sql
     * @param jndiName
     * @param params
     * @param title
     * @param columns
     * @param fileName
     * @param encode
     * @param dimCols
     * @param quote
     * @param waterMarkCode
     * @param bufferedRowSize
     * @return
     * @version ZJ
     */
    public boolean sql2File(final String title, String fileName,
            final String encode, int bufferedRowSize, BaseDao dao) {
        
        LogUtil.info("sql2File2: titles=" + title + ";fileName=" + fileName + ";encode=" + encode);

        long t1 = System.currentTimeMillis();
        boolean flag = true;
        try {
            List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
            final int bufferSize = bufferedRowSize;
            final String file = fileName;
            
            int start = 1;
            while (true) {
                if (dataList.size() >= bufferSize){
                    if (fileName.toLowerCase().endsWith("csv") || fileName.toLowerCase().endsWith("txt")) {//纯文本文本格式
                        flag = this.writeToTextFile1(dataList, title, file, encode);
                    }
                    dataList.clear();
                    start++;
                }else{
                    if (fileName.toLowerCase().endsWith("csv") || fileName.toLowerCase().endsWith("txt")) {//纯文本文本格式
                        flag = this.writeToTextFile1(dataList, title, file, encode);
                    }
                    break;
                }
            }
        }catch (Exception e) {
            flag = false;
            LogUtil.error("createFile(" + fileName + ") error:", e);
        } finally {
            LogUtil.info("The cost of sql2File2 is :  " + (System.currentTimeMillis() - t1) + "ms");
        }
        
        return flag;
    }

	/**文件写入
     * 
     * @param datas
     * @param title
     * @param columns
     * @param fileName
     * @param encode
     * @param dimCols
     * @param quote
     * @return
     */
    public static boolean writeToTextFile1(List<Map<String, String>> datas, String title, String fileName, String encode) {
        boolean flag = true;
        CSVWriter cw = null;
        Writer osw = null;
        int bufferdsize = 2048;
        try {
            FileUtil.createLocDir(fileName);//创建目录
            boolean hasExists = new File(fileName).exists();
            osw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), encode), bufferdsize);
            cw = new CSVWriter(osw);
            if (!hasExists) {
                if (StringUtil.isNotEmpty(title)) {
                    cw.writeNext(title.replace("\"", "").split(","));
                }
            }
            List<String> data = new ArrayList<String>();
            for (Map<String, String> m : datas) {
                if (m.containsKey("rownum")) {  //不把序号写入文件
                    m.remove("rownum");
                }
                for (String col : m.keySet()) {
                    data.add(String.valueOf(m.get(col)).replace("\"", ""));
                }
                if (data.size() > 0) {
                    cw.writeNext(data.toArray(new String[] {}));
                    data.clear();
                }
                cw.flush();
            }
        } catch (Exception e) {
            flag = false;
            LogUtil.error("createFile(" + fileName + ") error:", e);
        } finally {
            try {
                if (cw != null) {
                    cw.close();
                }
            } catch (Exception e) {
                LogUtil.warn("IO关闭异常："+e.getMessage());
            }
        }
        return flag;
    }

    /**
     * 创建文件目录
     * @param fileName 文件名（全路径）或目录
     * @throws IOException 
     */
    public static File createLocDir(String fileName) throws Exception {
        File f = new File(fileName);
        File dir = null;
        if (f != null && !f.exists()) {
            if (f.isDirectory()) {//判断是否是文件
                dir = f;
            } else {
                dir = f.getParentFile();
            }
            if (dir != null && !dir.exists()) {
                boolean result = dir.mkdirs();
                if (!result) {
                    LogUtil.error("can not mkdir [" + dir + "] ,please check OS User'S privilege!");
                    throw new Exception("can not mkdir [" + dir + "] ,please check OS User'S privilege!");
                }
            }
        } else {
            dir = f.isFile() ? f.getParentFile() : f;
        }
        return dir;
    }
    
    
}
