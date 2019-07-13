package com.xxm.minidouyin.util;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

public class ImageHelper {
    public static void displayWebImage(String url, ImageView imageView) {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }

    public static void displayLocalImage(Uri uri, ImageView imageView) {
        Glide.with(imageView.getContext()).load(uri).into(imageView);
    }

    public static void displayVideoImage(Uri uri, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(uri )
                .into( imageView);
    }
}
