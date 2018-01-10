package com.reactnativecomponent.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeControllerBuilder;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.views.image.ReactImageManager;
import com.facebook.react.views.image.ReactImageView;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;


public class RCTLoaderImageViewManager extends SimpleViewManager<RCTLoaderImageView> {
    private static final String REACT_CLASS = "RCTLoaderImageView";//要与类名一致
    private ImageLoaderConfiguration config;
    private DisplayImageOptions options;
    private Context context;

    private int memorySize = 5;
    public int threadSize = 3;
    private int readTimeout = 30;
    private int connectTimeout = 5;

    public String loadingImage = "";
    public String emptyUriImage = "";
    public String failImage = "";
    private boolean cacheInMemory = false;
    private boolean cacheOnDisc = true;
    private int Round = 0;
    private int fadeInDuration = 0;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    public RCTLoaderImageViewManager(Context context) {
        this.context = context;
    }

    @Override
    protected RCTLoaderImageView createViewInstance(ThemedReactContext reactContext) {
        if (context == null) {
            context = reactContext;
        }
        return new RCTLoaderImageView(context);
    }

    private void initOptions() {
        if (options == null) {

            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(getSplashId(loadingImage))
                    .showImageForEmptyUri(getSplashId(emptyUriImage))
                    .showImageOnFail(getSplashId(failImage))
                    .cacheInMemory(cacheInMemory)
                    .cacheOnDisc(cacheOnDisc)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new FadeInBitmapDisplayer(fadeInDuration))
                    .build();//构建完成
        }
    }

    private void initConfig(Context reactContext) {
        if (config == null) {
            config = new ImageLoaderConfiguration.Builder(reactContext)
                    .threadPoolSize(threadSize)
                    .threadPriority(Thread.NORM_PRIORITY - 1)
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    .diskCacheExtraOptions(480, 320, null)
                    .build();
            ImageLoader.getInstance().init(config);
        }
    }

    @ReactProp(name = "options")
    public void setData(RCTLoaderImageView view, ReadableMap map) {
        if (map.hasKey("threadSize")) {
            this.threadSize = map.getInt("threadSize");
        }
        if (map.hasKey("fadeInDuration")) {
            this.fadeInDuration = map.getInt("fadeInDuration");
        }

        this.loadingImage = map.getString("placeholder");
        this.emptyUriImage = map.getString("placeholder");
        this.failImage = map.getString("placeholder");
        String src = map.getString("src");
        String rowID = map.getString("rowID");
        view.setRowID(rowID);

        initConfig(context);
        initOptions();
        view.loaderImage(src, options);
    }

    private int getSplashId(String s) {
        if (context != null && s != null && !s.isEmpty()) {
            int drawableId = context.getResources().getIdentifier(s, "drawable", context.getClass().getPackage().getName());
            if (drawableId == 0) {
                drawableId = context.getResources().getIdentifier(s, "drawable", context.getPackageName());
            }
            return drawableId;
        }
        return 0;
    }
}
