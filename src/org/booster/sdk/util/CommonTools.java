package org.booster.sdk.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * @Description: 一组公共的工具方法
 * @author Merry.Zhao
 * @date 2014-3-13 下午6:45:21
 */
public class CommonTools {

    private final static DateFormat YYYY_MM_DD_HH_MM_SS_SSS =
        new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.equals("")) {
            return true;
        }
        return false;

    }

    /**
     * 获取UTC时间，精确到秒
     * @return
     */
    public static long getUTCTime() {
        final java.util.Calendar cal = java.util.Calendar.getInstance();
        final int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        final int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return cal.getTimeInMillis() / 1000;
    }

    /**
     * 从URL中获得所需的参数的值
     * @param url
     * @param key
     * @return
     */
    public static String getValueFromURL(String url, String key) {
        if (CommonTools.isEmpty(url) || url.indexOf(key) == -1) {
            return null;
        } else {
            String tokenizer = "?";
            if (url.indexOf("&") != -1) {
                tokenizer = "&";
            }
            int i = url.lastIndexOf(tokenizer);
            String ss = url.substring(i + 1, url.length());
            return ss.substring(ss.indexOf("=") + 1, ss.length());
        }
    }

    public static String getCurrentDateTime() {
        Date date = new Date();
        return YYYY_MM_DD_HH_MM_SS_SSS.format(date);
    }

    public static String toUTF_8(String str) {
        if (isEmpty(str)) {
            return "";
        }
        String result = "";
        try {
            byte[] bs = str.getBytes();
            result = new String(bs, Constants.ENCODE);
        } catch (Exception e) {
            Log.e("SDKUtil", "UnsupportedEncoding!!");
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(CommonTools.getCurrentDateTime());
    }

    public static int getRandomNumber(int size) {
        Random ran = new Random();
        return ran.nextInt(size);
    }
    public static String getMacAddress(Context context) {
//        return "F0-4D-A2-55-93-2F";
        try {
            String deviceCode = loadFileAsString("/sys/class/net/eth0/address").substring(0, 17);
            if (deviceCode == null) {
                deviceCode = "";
            }
            return deviceCode;
        } catch (IOException e) {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String devicecode = info.getMacAddress();
            if (devicecode == null) {
                devicecode = "";
            }
            System.out.println("mac address is :::::::::::    "+devicecode);
            return devicecode;
        }
    }
    public static String loadFileAsString(String filePath) throws IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }
}
