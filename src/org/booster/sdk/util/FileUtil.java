package org.booster.sdk.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import org.booster.sdk.logging.HiLog;

public class FileUtil {
    // 创建文件夹
    public static void changeMode(String destDir, String chmodCmd) {
        System.out.println("==================chmod dir path: " + destDir);
        File destFile = new File(destDir);
        if (destFile != null && destFile.exists()) {
            Process p;
            int status;
            try {
                p = Runtime.getRuntime().exec(chmodCmd + " " + destDir);
                status = p.waitFor();
                if (status == 0) {
                    // chmod success
                    System.out.println("==================chmod " + destDir + "(" + chmodCmd
                        + ") succ!!!");
                } else {
                    // chmod Failed
                    System.out.println("==================chmod " + destDir + "(" + chmodCmd
                        + ") failed!!!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("===============chmod " + destDir + "(" + chmodCmd
                    + ") exception! Reason:" + e.toString());
            }
        } else {
            System.out.println("================changeMode dir(" + destDir
                + ") failed, File/Dir not exist!");
        }
    }

    /**
     * 移动文件
     * @param srcFileName 源文件全路径
     * @param destDirName 要移动到的文件夹
     * @param newFileName 新的文件名，若为空，则文件名不变
     * @return
     */
    public static boolean moveFile(String srcFileName, String destDirName, String newFileName) {

        File srcFile = new File(srcFileName);
        if (!srcFile.exists() || !srcFile.isFile())
            return false;

        File destDir = new File(destDirName);
        if (!destDir.exists())
            destDir.mkdirs();
        String fileName = newFileName;
        if (CommonTools.isEmpty(fileName)) {
            fileName = srcFile.getName();
        }
        return srcFile.renameTo(new File(destDirName + File.separator + fileName));
    }

    /**
     * 拷贝文件
     * @param srcFileName 源文件全路径
     * @param destDirName 要拷贝到的文件夹
     * @param newFileName 新的文件名，若为空，则文件名不变
     * @return
     */
    public static boolean copyFile(String srcFileName, String destDirName, String newFileName) {
        File srcFile = new File(srcFileName);
        if (!srcFile.exists() || !srcFile.isFile())
            return false;

        File destDir = new File(destDirName);
        if (!destDir.exists())
            if(!destDir.mkdir()){
                HiLog.e("can not create dest dir :  "+destDirName+"  ,quit !!!");
            }
        String destFileName = destDirName + "/" + srcFile.getName();
        if (!CommonTools.isEmpty(newFileName)) {
            destFileName = destDirName + "/" + newFileName;
        }
        File destFile = new File(destFileName);
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel in = null;
        FileChannel out = null;

        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);
            in = fis.getChannel();
            out = fos.getChannel();
            in.transferTo(0, in.size(), out);
            return true;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                fos.close();
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean saveContentToFile(String resourceDetailFileName, String resourceDetails)
        throws FileNotFoundException {
        PrintWriter fw = new PrintWriter(new FileOutputStream(resourceDetailFileName));
        fw.write(resourceDetails);// 写入文件
        fw.flush();
        fw.close();
        FileUtil.changeMode(resourceDetailFileName, "chmod 777");
        return true;
    }

}
