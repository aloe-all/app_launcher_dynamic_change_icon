package com.crg.dynamicicon;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    //默认组件
    private ComponentName componentNameDefault;
    private ComponentName componentName2;
    private ComponentName componentName3;
    private PackageManager mPackageManager;
    private Handler mHandler;
    private static final int MSG_WHAT_DEFAULT = 0;
    private static final int MSG_WHAT_ICON2 = 1;
    private static final int MSG_WHAT_CON3 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_WHAT_DEFAULT:
                        enableComponentNameDefault();
                        mHandler.sendEmptyMessageDelayed(MSG_WHAT_ICON2, 4*1000);
                        break;
                    case MSG_WHAT_ICON2:
                        enableComponentName2();
//                        mHandler.sendEmptyMessageDelayed(MSG_WHAT_CON3, 4*1000);
                        break;
                    case MSG_WHAT_CON3:
                        enableComponentName3();
                        mHandler.sendEmptyMessageDelayed(MSG_WHAT_DEFAULT, 4*1000);
                        break;
                    default:
                        break;
                }
            }
        };

        pmTest();
    }

    /**
     * 设置第icon2图标生效
     */
    private void enableComponentName2() {
//        disableComponent(componentName3);
        enableComponent(componentName2);
        disableComponent(componentNameDefault);
//        startActivity(new Intent(this, getComponentName().getClass()));
//        resetLauncher();
//        Toast.makeText(this, "dynamic switch icon", Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置第icon3图标生效
     */
    private void enableComponentName3() {
        disableComponent(componentNameDefault);
        disableComponent(componentName2);
        enableComponent(componentName3);
//        resetLauncher();
//        Toast.makeText(this, "dynamic switch icon", Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置第 default 图标生效
     */
    private void enableComponentNameDefault() {
        disableComponent(componentName3);
        disableComponent(componentName2);
        enableComponent(componentNameDefault);
//        resetLauncher();
//        Toast.makeText(this, "dynamic switch default icon", Toast.LENGTH_SHORT).show();
    }

    /**
     * 启动组件
     *
     * @param componentName 组件名
     */
    private void enableComponent(ComponentName componentName) {
        //此方法用以启用和禁用组件，会覆盖Androidmanifest文件下定义的属性
        mPackageManager.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    /**
     * 禁用组件
     *
     * @param componentName 组件名
     */
    private void disableComponent(ComponentName componentName) {
        mPackageManager.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    //最后调用
    public void pmTest() {
        //获取到包管理类实例
        mPackageManager = getPackageManager();
        //得到此activity的全限定名
        componentNameDefault = getComponentName();
        //根据全限定名创建一个组件，即activity-alias 节点下的name：HomeActivity2 对应的组件
        componentName2 = new ComponentName(getBaseContext(), "com.crg.dynamicicon.dynamic_icon1");
        componentName3 = new ComponentName(getBaseContext(), "com.crg.dynamicicon.dynamic_icon2");
        componentNameDefault = new ComponentName(getBaseContext(), "com.crg.dynamicicon.MainActivity");
//        String action = getActionFromServer();//从后台获取到应该使用那一个组件，或者根据时间来判断
//        if ("comp2".equals(action)){
//            enableComponentName2();
//        }else if ("comp3".equals(action)){
//            enableComponentName3();
//        }
        mHandler.sendEmptyMessageDelayed(MSG_WHAT_ICON2, 5*60*1000);
    }

    private void resetLauncher() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        List<ResolveInfo> resolves = mPackageManager.queryIntentActivities(intent, 0);
        for (ResolveInfo res : resolves) {
            if (res.activityInfo != null) {
                ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                am.killBackgroundProcesses(res.activityInfo.packageName);
            }
        }
    }
}
