package com.domicilios.carlos.climapp.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.widget.TextView;

import com.domicilios.carlos.climapp.R;
import com.domicilios.carlos.climapp.controllers.ClimaActivity;

/**
 * Utility class to manage standard animations in DBD Control
 *
 * @author <a href="mailto:carlosfelipetorres75@gmail.com">Carlos Torres</a>
 */
public final class AnimationUtils {

    /** Private constructor to avoid instances **/
    private AnimationUtils() {}

    /**
     * This method creates an Object Animator based on the targeted view, the property to be
     * animated and the initial value and final value
     *
     * @param view
     *         Target view
     * @param property
     *         Property to be animated
     * @param init
     *         Initial value
     * @param end
     *         Final value
     * @param duration
     *         Animation duration
     *
     * @return ObjectAnimator with the given animated property
     */
    @NonNull
    public static ObjectAnimator createObjectAnimator(View view, String property, float init,
                                                      float end, long duration) {
        ObjectAnimator scaleXAnimation = ObjectAnimator.ofFloat(view, property, init, end);
        scaleXAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleXAnimation.setDuration(duration);
        return scaleXAnimation;
    }

    /**
     * Configura la animacion para la lista y la flecha
     *
     * @param view
     *         item a animar
     */
    public static void configurarAnimacion(final Context context, final View view, boolean out,
                                           final Intent intent) {
        int animId = R.anim.anim_scale_out;
        int visibility = View.VISIBLE;

        if (!out) {
            animId = R.anim.anim_scale_in;
        }

        Animation anim = android.view.animation.AnimationUtils.loadAnimation(context, animId);
        final int finalVisibility = visibility;
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(finalVisibility);
                context.startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        view.startAnimation(anim);
    }

    /**
     * Configura la animacion para la lista y la flecha
     *
     * @param view
     *         item a animar
     */
    public static void configurarAnimacion(final Context context, final View view, boolean out) {
        int animId = R.anim.anim_scale_out;
        int visibility = View.VISIBLE;

        if (!out) {
            animId = R.anim.anim_scale_in;
        }

        Animation anim = android.view.animation.AnimationUtils.loadAnimation(context, animId);
        final int finalVisibility = visibility;
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(finalVisibility);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        view.startAnimation(anim);
    }

}
