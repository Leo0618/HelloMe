package com.leo618.hellome.libcore.util;

import android.text.TextUtils;
import android.widget.ImageView;

import com.leo618.hellome.R;
import com.leo618.hellome.libcore.common.URLConstant;
import com.squareup.okhttp.internal.Util;
import com.squareup.picasso.Picasso;



/**
 * function : 图片加载工具类.
 * <p></p>
 *
 * Created by lzj on 2016/1/28.
 */
@SuppressWarnings("unused")
public class ImgLoadUtil {
    private static final String TAG = Util.class.getSimpleName();

    private static final int defaultPlaceholderForImg = R.mipmap.ic_launcher;
    private static final int defaultPlaceholderForAvatar = R.mipmap.ic_launcher;

    private static Picasso getPicasso() {
        return Picasso.with(UIUtil.getContext());
    }

    /**
     * 加载图片(需要拼接host)
     *
     * @param imgFileNameStr   图片文件名称 eg: up_files/img.jpg
     * @param targetImageView  目标ImageView
     * @param placeholderResId 默认图片id
     */
    public static void loadImgBySplice(String imgFileNameStr, ImageView targetImageView, int placeholderResId) {
        if (TextUtils.isEmpty(imgFileNameStr)) {
            getPicasso().load(placeholderResId > 0 ? placeholderResId : defaultPlaceholderForImg).into(targetImageView);
            return;
        }
        String imgUrl = URLConstant.URL_BASE_C + imgFileNameStr;
        getPicasso().load(imgUrl).placeholder(placeholderResId > 0 ? placeholderResId : defaultPlaceholderForImg).into(targetImageView);
    }


    /**
     * 加载图片(加载完整地址包括网络地址和本地地址)
     *
     * @param imgPath          图片地址 eg: http://www.ypwl.com/up_files/picture.jpg , /storage0/DCIM/picture.jpg
     * @param targetImageView  目标ImageView
     * @param placeholderResId 默认图片资源id
     */
    public static void loadImgByPath(String imgPath, ImageView targetImageView, int placeholderResId) {
        if (TextUtils.isEmpty(imgPath)) {
            getPicasso().load(placeholderResId > 0 ? placeholderResId : defaultPlaceholderForImg).into(targetImageView);
            return;
        }
        if (!imgPath.startsWith("http") && !imgPath.startsWith("file://")) {
            imgPath = "file://" + imgPath;
        }
        getPicasso().load(imgPath).placeholder(placeholderResId > 0 ? placeholderResId : defaultPlaceholderForImg).into(targetImageView);
    }

    /**
     * 加载头像(需要拼接host)
     *
     * @param avatarFileNameStr avatar文件名称 eg: up_files/user_avatar.jpg
     * @param targetImageView   目标ImageView
     * @param placeholderResId  默认图片id
     */
    public static void loadAvatarBySplice(String avatarFileNameStr, ImageView targetImageView, int placeholderResId) {
        if (TextUtils.isEmpty(avatarFileNameStr)) {
            getPicasso().load(placeholderResId > 0 ? placeholderResId : defaultPlaceholderForAvatar).into(targetImageView);
            return;
        }
        String imgUrl = URLConstant.URL_BASE_C + avatarFileNameStr;
        getPicasso().load(imgUrl).placeholder(placeholderResId > 0 ? placeholderResId : defaultPlaceholderForAvatar).into(targetImageView);
    }

    /**
     * 加载头像(加载完整地址包括网络地址和本地地址)
     *
     * @param avatarPath       avatar图片地址 eg: http://www.ypwl.com/up_files/user_avatar.jpg , /storage0/DCIM/avatar.jpg
     * @param targetImageView  目标ImageView
     * @param placeholderResId 默认图片id
     */
    public static void loadAvatarByPath(String avatarPath, ImageView targetImageView, int placeholderResId) {
        if (TextUtils.isEmpty(avatarPath)) {
            getPicasso().load(placeholderResId > 0 ? placeholderResId : defaultPlaceholderForAvatar).into(targetImageView);
            return;
        }
        if (!avatarPath.startsWith("http") && !avatarPath.startsWith("file://")) {
            avatarPath = "file://" + avatarPath;
        }
        getPicasso().load(avatarPath).placeholder(placeholderResId > 0 ? placeholderResId : defaultPlaceholderForAvatar).into(targetImageView);
    }
}
