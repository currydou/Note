package com.curry.note.util.imageloader.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.curry.note.R;
import com.curry.note.util.imageloader.BaseImageLoaderStrategy;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class GlideImageLoaderStrategy implements BaseImageLoaderStrategy<GlideImageConfig>, GlideAppliesOptions {

    @Override
    public void loadImage(Context ctx, GlideImageConfig config) {
        if (ctx == null) throw new IllegalStateException("Context is required");
        if (config == null) throw new IllegalStateException("GlideImageConfig is required");
//        if (TextUtils.isEmpty(config.getUrl())) throw new IllegalStateException("Url is required");
        if (config.getImageView() == null) throw new IllegalStateException("Imageview is required");
        if (config.getImageView().getTag(R.id.b_tag_glide_image) != null
                && TextUtils.equals(config.getUrl(), config.getImageView().getTag(R.id.b_tag_glide_image).toString())) {
            //解决闪动问题，相同地址不做多次加载
            return;
        }
        config.getImageView().setTag(R.id.b_tag_glide_image, config.getUrl());//解决闪动问题，相同地址不做多次加载
        GlideRequests requests;
        requests = GlideApp.with(ctx);
        GlideRequest<Drawable> glideRequest = requests.load(config.getUrl()).dontAnimate().fitCenter();

        switch (config.getCacheStrategy()) {//缓存策略
            case 0:
                glideRequest = glideRequest.diskCacheStrategy(DiskCacheStrategy.ALL);
                break;
            case 1:
                glideRequest = glideRequest.diskCacheStrategy(DiskCacheStrategy.NONE);
                break;
            case 2:
                glideRequest = glideRequest.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                break;
            case 3:
                glideRequest = glideRequest.diskCacheStrategy(DiskCacheStrategy.DATA);
                break;
            case 4:
                glideRequest = glideRequest.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                break;
        }
        if (config.getTransformation() != null) {//glide用它来改变图形的形状
            glideRequest = glideRequest.transform(config.getTransformation());
        }


        if (config.getPlaceholder() != 0)//设置占位符
            glideRequest = glideRequest.placeholder(config.getPlaceholder());

        if (config.getErrorPic() != 0)//设置错误的图片
            glideRequest = glideRequest.error(config.getErrorPic());

        if (config.getFallback() != 0)//设置请求 url 为空图片
            glideRequest = glideRequest.fallback(config.getFallback());

        glideRequest
                .into(config.getImageView());
    }

    @Override
    public void clear(final Context ctx, GlideImageConfig config) {
        if (ctx == null) throw new NullPointerException("Context is required");
        if (config == null) throw new NullPointerException("ImageConfigImpl is required");

        if (config.getImageViews() != null && config.getImageViews().length > 0) {//取消在执行的任务并且释放资源
            for (ImageView imageView : config.getImageViews()) {
                GlideApp.get(ctx).getRequestManagerRetriever().get(ctx).clear(imageView);
            }
        }

        if (config.isClearDiskCache()) {//清除本地缓存
            Observable.just(0)
                    .observeOn(Schedulers.io())
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(@NonNull Integer integer) throws Exception {
                            Glide.get(ctx).clearDiskCache();
                        }
                    });
        }

        if (config.isClearMemory()) {//清除内存缓存
            Observable.just(0)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(@NonNull Integer integer) throws Exception {
                            Glide.get(ctx).clearMemory();
                        }
                    });
        }

    }

    @Override
    public void applyGlideOptions(Context context, GlideBuilder builder) {
//        Timber.w("GlideImageLoaderStrategy applyGlideOptions");
    }
}
