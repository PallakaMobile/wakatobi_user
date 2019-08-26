package ps.salam.wakatobi.support.imageslider.Animations;

import android.view.View;

import ps.salam.wakatobi.R;

/**
 * A demo class to show how to use {@link ps.salam.wakatobi.support.imageslider.Animations.BaseAnimationInterface}
 * to make  your custom animation in {@link ps.salam.wakatobi.support.imageslider.Tricks.ViewPagerEx.PageTransformer} action.
 */
public class DescriptionAnimation implements BaseAnimationInterface {

    @Override
    public void onPrepareCurrentItemLeaveScreen(View current) {
        View descriptionLayout = current.findViewById(R.id.description_layout);
        if (descriptionLayout != null) {
            current.findViewById(R.id.description_layout).setVisibility(View.INVISIBLE);
        }
    }

    /**
     * When next item is coming to show, let'Response hide the titleDescription layout.
     *
     * @param next
     */
    @Override
    public void onPrepareNextItemShowInScreen(View next) {
        View descriptionLayout = next.findViewById(R.id.description_layout);
        if (descriptionLayout != null) {
            next.findViewById(R.id.description_layout).setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onCurrentItemDisappear(View view) {

    }

    /**
     * When next item show in ViewPagerEx, let'Response make an animation to show the
     * titleDescription layout.
     *
     * @param view
     */
    @Override
    public void onNextItemAppear(View view) {

        View descriptionLayout = view.findViewById(R.id.description_layout);
        if (descriptionLayout != null) {

            view.findViewById(R.id.description_layout).setVisibility(View.VISIBLE);
        }

    }
}
