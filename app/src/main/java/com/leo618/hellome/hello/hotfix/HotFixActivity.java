package com.leo618.hellome.hello.hotfix;

import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leo618.hellome.R;
import com.leo618.hellome.libcore.base.BaseActivity;
import com.tencent.tinker.lib.tinker.TinkerInstaller;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * function:
 *
 * <p></p>
 * Created by lzj on 2016/11/14.
 */

public class HotFixActivity extends BaseActivity {
    @Override
    protected int setContentViewResId() {
        return R.layout.activity_hotfix;
    }

    @Bind(R.id.dataText)
    TextView mDataText;
    @Bind(R.id.dataImg)
    ImageView mDataImg;


    @Override
    protected void doBusiness() {
        ((TextView) findView(R.id.tv_title)).setText(R.string.test_hotfix);
    }

    @OnClick({R.id.iv_title_left_1, R.id.btn_test, R.id.btn_fix, R.id.btn_restart})
    public void onClickOfButtons(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left_1:
                finish();
                break;
            case R.id.btn_test:
                showBugState();
                break;
            case R.id.btn_fix:
                TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(),
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk");
                break;
            case R.id.btn_restart:
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
        }
    }

    private void showBugState() {
        mDataText.setText("当前有一个bug");
        mDataImg.setImageResource(R.drawable.hf_hasbug);
//        mDataText.setText("bug已经解决啦!");
//        mDataImg.setImageResource(R.drawable.hf_nobug);
    }

}
