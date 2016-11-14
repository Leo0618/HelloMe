package com.leo618.hellome.hello.hotfix;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leo618.hellome.R;
import com.leo618.hellome.libcore.base.BaseActivity;

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

    @OnClick({R.id.iv_title_left_1, R.id.btn_test, R.id.btn_fix})
    public void onClickOfButtons(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left_1:
                finish();
                break;
            case R.id.btn_test:
                showBugState();
                break;
            case R.id.btn_fix:
                break;
        }
    }

    private void showBugState() {
        mDataText.setText("当前有一个bug");
        mDataImg.setImageResource(R.drawable.hf_hasbug);
    }

}
