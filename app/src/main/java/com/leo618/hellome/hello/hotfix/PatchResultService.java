package com.leo618.hellome.hello.hotfix;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.leo618.hellome.libcore.util.Logg;
import com.tencent.tinker.lib.service.DefaultTinkerResultService;
import com.tencent.tinker.lib.service.PatchResult;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.util.TinkerServiceInternals;
import com.tencent.tinker.loader.shareutil.SharePatchFileUtil;

import java.io.File;

public class PatchResultService extends DefaultTinkerResultService {
    private static final String TAG = "PatchResultService";

    @Override
    public void onPatchResult(final PatchResult result) {
        if (result == null) {
            Logg.e(TAG, "PatchResultService received null result!!!!");
            return;
        }
        Logg.i(TAG, "PatchResultService receive result: %s", result.toString());

        //first, we want to kill the recover process
        TinkerServiceInternals.killTinkerPatchServiceProcess(getApplicationContext());

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (result.isSuccess) {
                    Toast.makeText(getApplicationContext(), "patch success, please restart process", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "patch fail, please check reason", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (result.isSuccess && result.isUpgradePatch) {
            File rawFile = new File(result.rawPatchFilePath);
            if (rawFile.exists()) {
                Logg.i(TAG, "save delete raw patch file");
                SharePatchFileUtil.safeDeleteFile(rawFile);
            }
            if (checkIfNeedKill(result)) {
                Logg.i(TAG, "tinker wait screen to restart process");
                new ScreenState(getApplicationContext(), new ScreenState.IOnScreenOff() {
                    @Override
                    public void onScreenOff() {
                        restartProcess();
                    }
                });
            } else {
                Logg.i(TAG, "I have already install the newly patch version!");
            }
        }

        //repair current patch fail, just clean!
        if (!result.isSuccess && !result.isUpgradePatch) {
            //if you have not install tinker this moment, you can use TinkerApplicationHelper api
            Tinker.with(getApplicationContext()).cleanPatch();
        }
    }

    /**
     * you can restart your process through service or broadcast
     */
    private void restartProcess() {
        Logg.i(TAG, "app is background now, i can kill quietly");
        //you can send service or broadcast intent to restart your process
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    static class ScreenState {
        interface IOnScreenOff {
            void onScreenOff();
        }

        ScreenState(Context context, final IOnScreenOff onScreenOffInterface) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            context.registerReceiver(new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent in) {
                    String action = in == null ? "" : in.getAction();
                    Logg.i(TAG, "ScreenReceiver action [%s] ", action);
                    if (Intent.ACTION_SCREEN_OFF.equals(action)) {

                        context.unregisterReceiver(this);

                        if (onScreenOffInterface != null) {
                            onScreenOffInterface.onScreenOff();
                        }
                    }
                }
            }, filter);
        }
    }

}
