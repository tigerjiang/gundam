package org.booster.gundam.util;

import android.content.Context;
import android.content.pm.PackageInfo;


public class AndroidUtil {
    /**
     * 根据AppInfo 的 PackageName 获得PackageInfo
     * @param context
     * @param appInfo
     * @return
     */
    public static PackageInfo getPackageInfo(Context context, String packageName) {

        PackageInfo packageInfo = null;
        try {
            packageInfo =
                context.getPackageManager().getPackageInfo(
                    packageName, 0);
        } catch (Exception e) {
            packageInfo = null;
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return packageInfo;

    }
}
