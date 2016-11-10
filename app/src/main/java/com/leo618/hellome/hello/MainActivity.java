package com.leo618.hellome.hello;

import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leo618.hellome.R;
import com.leo618.hellome.hello.bean.AuthorInfoBean;
import com.leo618.hellome.libcore.base.BaseActivity;
import com.leo618.hellome.libcore.common.URLConstant;
import com.leo618.hellome.libcore.interf.IRequestCallback;
import com.leo618.hellome.libcore.manager.net.NetManager;
import com.leo618.hellome.libcore.util.Logg;
import com.leo618.hellome.libcore.util.UIUtil;

import java.util.HashMap;
import java.util.Map;

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
        Map<String, String> params = new HashMap<>();
        params.put("phone", "15673200007");
        params.put("password", "123456");
        NetManager.get(URLConstant.AUTHOR_INFO, params, new IRequestCallback<AuthorInfoBean>() {
            @Override
            public void onFailure(Exception e) {
                loadingDismiss();
                if (e != null && !TextUtils.isEmpty(e.getMessage())) {
                    UIUtil.showToastShort(e.getMessage());
                }
            }

            @Override
            public void onSuccess(AuthorInfoBean bean) {
                loadingDismiss();

                Logg.e(TAG, "info:" + bean.toString());

                String msg = "autor:leo" + "\n"
                        + "phone:18820285271" + "\n"
                        + "QQ:619827587";

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("作者信息")
                        .setPositiveButton(R.string.sure, null)
                        .setMessage(msg)
                        .create().show();
            }
        });
    }

}
