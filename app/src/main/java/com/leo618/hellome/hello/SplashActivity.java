package com.leo618.hellome.hello;

import android.Manifest;
import android.support.annotation.NonNull;

import com.leo618.hellome.R;
import com.leo618.hellome.libcore.CrashHandler;
import com.leo618.hellome.libcore.base.BaseActivity;
import com.leo618.hellome.libcore.common.DeviceInfo;
import com.leo618.hellome.libcore.util.FileStorageUtil;
import com.leo618.hellome.libcore.util.PermissionUtil;
import com.leo618.hellome.libcore.util.UIUtil;

public class SplashActivity extends BaseActivity {

    @Override
    protected boolean enableSystemBarManager() {
        return false;
    }

    @Override
    protected int setContentViewResId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void doBusiness() {
        checkInitPermissions();
    }

    /** 在启动页添加进去app必要的权限确认 */
    private void checkInitPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {//6.0 or uper
            String[] permissions = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE
            };
            String[] resultPermissions = PermissionUtil.needCheckPermissions(permissions);
            if (resultPermissions.length == 0) {
                handleResultAfterInitPermissions();
            } else {
                requestPermissions(resultPermissions, 100);
            }
        } else {
            handleResultAfterInitPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (PermissionUtil.passPermissions(grantResults)) {
                handleResultAfterInitPermissions();
            } else {
                UIUtil.showToastShort(getString(R.string.hint_permission_splash));
                finish();
            }
        }
    }

    private void handleResultAfterInitPermissions() {
        FileStorageUtil.initAppDir();//初始化应用文件目录
        DeviceInfo.init(this);// 初始化设备信息
        CrashHandler.init(UIUtil.getContext());
        UIUtil.postDelayed(mDelayEnterRunnable, 3000);
    }

    private Runnable mDelayEnterRunnable = new Runnable() {
        @Override
        public void run() {
            startActivity(MainActivity.class);
            finish();
        }
    };

    @Override
    public void onBackPressed() {
        // cannot go back
    }

    @Override
    protected void onDestroy() {
        UIUtil.removeCallbacksFromMainLooper(mDelayEnterRunnable);
        super.onDestroy();
    }
}
