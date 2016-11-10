package com.leo618.hellome.hello;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leo618.hellome.R;
import com.leo618.hellome.libcore.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected int setContentViewResId() {
        return R.layout.activity_main;
    }

    @Bind(R.id.tv_title)
    TextView mTitle;
    @Bind(R.id.iv_title_right_1)
    ImageView mInfo;

    @Override
    protected void doBusiness() {
        findView(R.id.iv_title_left_1).setVisibility(View.GONE);
        mTitle.setText(R.string.app_name);
        mInfo.setImageResource(android.R.drawable.ic_menu_info_details);
    }

    @OnClick({R.id.iv_title_right_1})
    public void onClickOfButtons(View view) {
        switch (view.getId()) {
            case R.id.iv_title_right_1://show info
                showAuthorInfo();
                break;
        }
    }

    private void showAuthorInfo() {
        loadingShow();
        


    }

}
