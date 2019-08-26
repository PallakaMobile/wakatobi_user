package ps.salam.wakatobi.support.imageslider.SliderTypes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import ps.salam.wakatobi.R;


/**
 * a simple slider view, which just show an image. If you want to make your own slider view,
 * <p>
 * just extend BaseSliderView, and implement getView() method.
 */
public class DefaultSliderView extends BaseSliderView {

    public DefaultSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.image_slider_render_type_default, null);
        ImageView target = (ImageView) v.findViewById(R.id.daimajia_slider_image);
        ImageView targetClone = (ImageView) v.findViewById(R.id.daimajia_slider_image_1);

        ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.loading_bar);
        ProgressBar progressBar1 = (ProgressBar) v.findViewById(R.id.loading_bar1);
        bindEventAndShow(v, target, targetClone, progressBar, progressBar1);
        return v;
    }
}
