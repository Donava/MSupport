package com.j1024.msupport.version;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import mc.xwidget.McDialogTwoBtn;

/**
 * Created by don on 12/15/14.
 */
public class VersionUtil {
    public static void update(Context context){
        UmengUpdateAgent.update(context);
    }

    public static void forceUpdate(final FragmentActivity context){
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, final UpdateResponse updateInfo) {
                switch (updateStatus) {
                    case UpdateStatus.Yes:
                        UmengUpdateAgent.showUpdateDialog(context, updateInfo);
                        break;
                    case UpdateStatus.No:
                        Toast.makeText(context, "您当前已是最新版本", Toast.LENGTH_SHORT).show();
                        break;
                    case UpdateStatus.NoneWifi:
                        McDialogTwoBtn.newInstance("当前不是wifi连接，会消耗网络流量，是否更新", "更新", "取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                UmengUpdateAgent.showUpdateDialog(context, updateInfo);
                            }
                        }, null).fixedShow(context);
                        break;
                    case UpdateStatus.Timeout: // time out
                        Toast.makeText(context, "更新超时", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        UmengUpdateAgent.forceUpdate(context);
        UmengUpdateAgent.setUpdateAutoPopup(true);
    }

    public static String getVersionInfo(Context context){
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName+"("+pi.versionCode+")";
        } catch (PackageManager.NameNotFoundException ignored) {
            return "";
        }
    }
}
