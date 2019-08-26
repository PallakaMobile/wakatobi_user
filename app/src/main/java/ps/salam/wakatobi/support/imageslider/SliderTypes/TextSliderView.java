package ps.salam.wakatobi.support.imageslider.SliderTypes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import ps.salam.wakatobi.R;


/**
 * This is a slider with a titleDescription TextView.
 */
public class TextSliderView extends BaseSliderView {
    public TextSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.image_slider_render_type_text, null);
        ImageView target = (ImageView) v.findViewById(R.id.daimajia_slider_image);
        ImageView targetClone = (ImageView) v.findViewById(R.id.daimajia_slider_image_1);
        TextView titleDescription = (TextView) v.findViewById(R.id.tv_title_description);
        ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.loading_bar);
        ProgressBar progressBar1 = (ProgressBar) v.findViewById(R.id.loading_bar1);
        titleDescription.setText(getDescription());
        TextView contentDescription = (TextView) v.findViewById(R.id.tv_content_description);
        contentDescription.setText(getContentDescription());
        bindEventAndShow(v, target, targetClone,progressBar,progressBar1);
        return v;
    }
}
