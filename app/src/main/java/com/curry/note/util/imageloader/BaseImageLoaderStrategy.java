package com.curry.note.util.imageloader;

import android.content.Context;

/**
 * Created by lib on 2017/7/25.
 */

public interface BaseImageLoaderStrategy<T extends ImageConfig> {
    void loadImage(Context ctx, T config);
    void clear(Context ctx, T config);
}
