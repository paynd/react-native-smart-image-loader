package com.reactnativecomponent.imageloader;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.react.bridge.ReactMethod;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


public class RCTLoaderImageView extends ImageView {
    String src;
    DisplayImageOptions options;

    public String getRowID() {
        return rowID;
    }

    public void setRowID(String rowID) {
        this.rowID = rowID;
    }

    private String rowID;

    public RCTLoaderImageView(Context context) {
        super(context);
    }

    public void loaderImage(String src, DisplayImageOptions options) {
        if (src != null && options != null) {
            ImageLoader imageLoader = ImageLoader.getInstance();
            this.src = src;
            this.options = options;
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (visibility == View.VISIBLE) {
            imageDisplay(imageLoader);
        } else if (visibility == View.INVISIBLE || visibility == View.GONE) {
            imageLoader.cancelDisplayTask(this);
        }

    }

    /**
     * imageLoader显示图片
     *
     * @param imageLoader
     */

    private void imageDisplay(ImageLoader imageLoader) {
        this.setDrawingCacheEnabled(false);

        imageLoader.displayImage(src, RCTLoaderImageView.this, options);
    }
}
