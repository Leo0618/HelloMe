package com.leo618.hellome.hello.hotfix;

import com.leo618.hellome.libcore.util.Logg;
import com.tencent.tinker.lib.listener.DefaultPatchListener;
import com.tencent.tinker.lib.listener.PatchListener;
import com.tencent.tinker.lib.patch.AbstractPatch;
import com.tencent.tinker.lib.patch.RepairPatch;
import com.tencent.tinker.lib.patch.UpgradePatch;
import com.tencent.tinker.lib.reporter.DefaultLoadReporter;
import com.tencent.tinker.lib.reporter.DefaultPatchReporter;
import com.tencent.tinker.lib.reporter.LoadReporter;
import com.tencent.tinker.lib.reporter.PatchReporter;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.ApplicationLike;


public class TinkerManager {
    private static final String TAG = "TinkerManager";

    private static boolean isInstalled = false;

    public static void sampleInstallTinker(ApplicationLike appLike) {
        if (isInstalled) {
            Logg.w(TAG, "install tinker, but has installed, ignore");
            return;
        }
        LoadReporter loadReporter = new DefaultLoadReporter(appLike.getApplication());
        PatchReporter patchReporter = new DefaultPatchReporter(appLike.getApplication());
        PatchListener patchListener = new DefaultPatchListener(appLike.getApplication());
        AbstractPatch upgradePatchProcessor = new UpgradePatch();
        AbstractPatch repairPatchProcessor = new RepairPatch();

        TinkerInstaller.install(appLike,
                loadReporter, patchReporter, patchListener,
                PatchResultService.class, upgradePatchProcessor, repairPatchProcessor);

        isInstalled = true;
    }

}
