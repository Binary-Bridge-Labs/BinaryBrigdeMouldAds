package com.binarybrigde.dev.ads.spinkit.style;

import android.animation.ValueAnimator;
import android.graphics.Rect;

import com.binarybrigde.dev.ads.spinkit.animation.SpriteAnimatorBuilder;
import com.binarybrigde.dev.ads.spinkit.sprite.RectSprite;


/**
 * Created by ybq.
 */
public class RotatingPlane extends RectSprite {
    @Override
    protected void onBoundsChange(Rect bounds) {
        setDrawBounds(clipSquare(bounds));
    }

    @Override
    public ValueAnimator onCreateAnimation() {
        float fractions[] = new float[]{0f, 0.5f, 1f};
        return new SpriteAnimatorBuilder(this).
                rotateX(fractions, 0, -180, -180).
                rotateY(fractions, 0, 0, -180).
                duration(1200).
                easeInOut(fractions)
                .build();
    }
}
