/*
 * Copyright (c) 2015 Henry Addo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.addhen.android.raiburari.presentation.ui.widget;

import com.addhen.android.raiburari.R;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


/**
 * @author Henry Addo
 */
public class FontSupportedTextView extends AppCompatTextView {

    private TypefaceManager mTypefaceManager;

    public FontSupportedTextView(Context context) {
        this(context, null);
    }

    public FontSupportedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FontSupportedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mTypefaceManager = new TypefaceManager(context.getAssets());
        if (!isInEditMode()) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FontSupportedTextView);
            if (a.hasValue(R.styleable.FontSupportedTextView_fontFile)) {
                setFont(a.getString(R.styleable.FontSupportedTextView_fontFile));
            }
            a.recycle();
        }
    }

    public void setFont(final String customFont) {
        Typeface typeface = mTypefaceManager.getTypeface(customFont);
        if (typeface != null) {
            setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
            setTypeface(typeface);
        }
    }

    private static class TypefaceManager {

        private final LruCache<String, Typeface> mCache;

        private final AssetManager mAssetManager;

        public TypefaceManager(@NonNull AssetManager assetManager) {
            mAssetManager = assetManager;
            mCache = new LruCache<>(3);
        }

        public Typeface getTypeface(final String filename) {
            Typeface typeface = mCache.get(filename);
            if (typeface == null) {
                typeface = Typeface.createFromAsset(mAssetManager, "fonts/" + filename);
                mCache.put(filename, typeface);
            }
            return typeface;
        }

    }
}
