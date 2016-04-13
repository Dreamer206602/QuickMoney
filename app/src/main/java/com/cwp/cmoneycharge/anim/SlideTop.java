package com.cwp.cmoneycharge.anim;

import android.view.View;

import com.nineoldandroids.animation.ObjectAnimator;


public class SlideTop extends BaseEffects {

    @Override
    protected void setupAnimation(View view) {
        getAnimatorSet().playTogether(
                ObjectAnimator.ofFloat(view, "translationY", -300, 0).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration*3/2)

        );
    }
}
