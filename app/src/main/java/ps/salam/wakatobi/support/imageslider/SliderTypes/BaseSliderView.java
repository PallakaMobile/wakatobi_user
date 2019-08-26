package ps.salam.wakatobi.support.imageslider.SliderTypes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import ps.salam.wakatobi.R;
import ps.salam.wakatobi.utils.BlurTransformation;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;

/**
 * When you want to make your own slider view, you must extends from this class.
 * BaseSliderView provides some useful methods.
 * I provide two example: {@link ps.salam.wakatobi.support.imageslider.SliderTypes.DefaultSliderView} and
 * {@link ps.salam.wakatobi.support.imageslider.SliderTypes.TextSliderView}
 * if you want to show progressbar, you just need to set a progressbar id as @+id/loading_bar.
 */
public abstract class BaseSliderView {

    protected Context mContext;

    private Bundle mBundle;

    /**
     * Error place holder image.
     */
    private int mErrorPlaceHolderRes;

    /**
     * Empty imageView placeholder.
     */
    private int mEmptyPlaceHolderRes;

    private String mUrl;
    private File mFile;
    private int mRes;

    protected OnSliderClickListener mOnSliderClickListener;

    private boolean mErrorDisappear;

    private ImageLoadListener mLoadListener;

    private String mTitleDescription;
    private String mContentDescription;

    private Glide mGlide;

    /**
     * Scale type of the image.
     */
    private ScaleType mScaleType = ScaleType.CenterCrop;

    public enum ScaleType {
        CenterCrop, CenterInside, Fit, FitCenterCrop
    }

    protected BaseSliderView(Context context) {
        mContext = context;
    }

    /**
     * the placeholder image when loading image from url or file.
     *
     * @param resId Image resource id
     * @return
     */
    public BaseSliderView empty(int resId) {
        mEmptyPlaceHolderRes = resId;
        return this;
    }

    /**
     * determine whether remove the image which failed to download or load from file
     *
     * @param disappear
     * @return
     */
    public BaseSliderView errorDisappear(boolean disappear) {
        mErrorDisappear = disappear;
        return this;
    }

    /**
     * if you set errorDisappear false, this will set a error placeholder image.
     *
     * @param resId image resource id
     * @return
     */
    public BaseSliderView error(int resId) {
        mErrorPlaceHolderRes = resId;
        return this;
    }

    /**
     * the titleDescription of a slider image.
     *
     * @param titleDescription
     * @return
     */
    public BaseSliderView titleDescription(String titleDescription) {
        mTitleDescription = titleDescription;
        return this;
    }


    public BaseSliderView contentDescription(String contentDescription) {
        mContentDescription = contentDescription;
        return this;
    }

    /**
     * set a url as a image that preparing to load
     *
     * @param url
     * @return
     */
    public BaseSliderView image(String url) {
        if (mFile != null || mRes != 0) {
            throw new IllegalStateException("Call multi image function," +
                    "you only have permission to call it once");
        }
        mUrl = url;
        return this;
    }

    /**
     * set a file as a image that will to load
     *
     * @param file
     * @return
     */
    public BaseSliderView image(File file) {
        if (mUrl != null || mRes != 0) {
            throw new IllegalStateException("Call multi image function," +
                    "you only have permission to call it once");
        }
        mFile = file;
        return this;
    }

    public BaseSliderView image(int res) {
        if (mUrl != null || mFile != null) {
            throw new IllegalStateException("Call multi image function," +
                    "you only have permission to call it once");
        }
        mRes = res;
        return this;
    }

    /**
     * lets users add a bundle of additional information
     *
     * @param bundle
     * @return
     */
    public BaseSliderView bundle(Bundle bundle) {
        mBundle = bundle;
        return this;
    }

    public String getUrl() {
        return mUrl;
    }

    public boolean isErrorDisappear() {
        return mErrorDisappear;
    }

    public int getEmpty() {
        return mEmptyPlaceHolderRes;
    }

    public int getError() {
        return mErrorPlaceHolderRes;
    }

    public String getDescription() {
        return mTitleDescription;
    }

    public String getContentDescription() {
        return mContentDescription;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * set a slider image click listener
     *
     * @param l
     * @return
     */
    public BaseSliderView setOnSliderClickListener(OnSliderClickListener l) {
        mOnSliderClickListener = l;
        return this;
    }

    /**
     * When you want to implement your own slider view, please call this method in the end in `getView()` method
     *
     * @param v               the whole view
     * @param targetImageView where to place image
     */
    protected void bindEventAndShow(final View v, final ImageView targetImageView, final ImageView targetImageViewClone
            , final ProgressBar progressBar, final ProgressBar progressBar1) {
        final BaseSliderView me = this;

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSliderClickListener != null) {
                    mOnSliderClickListener.onSliderClick(me);
                }
            }
        });

        if (targetImageView == null)
            return;

        if (targetImageViewClone == null)
            return;

        if (progressBar == null)
            return;
        if (progressBar1 == null)
            return;

        if (mLoadListener != null) {
            mLoadListener.onStart(me);
        }

        RequestManager p = Glide.with(mContext);
        DrawableTypeRequest rq = null;
        if (mUrl != null) {
            rq = (DrawableTypeRequest) p.load(mUrl).thumbnail(0.5f).crossFade().placeholder(R.drawable.progressbar)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT);
        } else if (mFile != null) {
            rq = (DrawableTypeRequest) p.load(mFile).thumbnail(0.5f).crossFade().placeholder(R.drawable.progressbar)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT);
        } else if (mRes != 0) {
            rq = (DrawableTypeRequest) p.load(mRes).thumbnail(0.5f).crossFade().placeholder(R.drawable.progressbar)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT);
        } else {
            return;
        }

        if (rq == null) {
            return;
        }

        if (getEmpty() != 0) {
            rq.placeholder(getEmpty());
        }

        if (getError() != 0) {
            rq.error(getError());
        }

        switch (mScaleType) {
//            case Fit:
//                rq.crossFade();
//                break;
            case CenterCrop:
                rq.centerCrop();
                break;
            case FitCenterCrop:
                rq.fitCenter();
                break;
        }


        rq.into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, GlideAnimation<? super Drawable> glideAnimation) {
                progressBar.setVisibility(View.GONE);
                targetImageView.setImageDrawable(resource);

            }

            @Override
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        rq.bitmapTransform(new BlurTransformation(getContext())).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
                progressBar1.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                progressBar1.setVisibility(View.GONE);
                targetImageViewClone.setImageDrawable(resource);
            }
        });
    }


    public BaseSliderView setScaleType(ScaleType type) {
        mScaleType = type;
        return this;
    }

    public ScaleType getScaleType() {
        return mScaleType;
    }

    /**
     * the extended class have to implement getView(), which is called by the adapter,
     * every extended class response to render their own view.
     *
     * @return
     */
    public abstract View getView();

    /**
     * set a listener to get a message , if load error.
     *
     * @param l
     */
    public void setOnImageLoadListener(ImageLoadListener l) {
        mLoadListener = l;
    }

    public interface OnSliderClickListener {
        public void onSliderClick(BaseSliderView slider);
    }

    /**
     * when you have some extra information, please put it in this bundle.
     *
     * @return
     */
    public Bundle getBundle() {
        return mBundle;
    }

    public interface ImageLoadListener {
        public void onStart(BaseSliderView target);

        public void onEnd(boolean result, BaseSliderView target);
    }

    public Glide getmGlide() {
        return mGlide;
    }

    public void setmGlide(Glide mGlide) {
        this.mGlide = mGlide;
    }
}
