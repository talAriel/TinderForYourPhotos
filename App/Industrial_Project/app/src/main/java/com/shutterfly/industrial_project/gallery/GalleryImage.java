package com.shutterfly.industrial_project.gallery;


import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.Animation;
import com.mindorks.placeholderview.annotations.Animate;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

import com.shutterfly.industrial_project.R;


/*
 *  Created by:
 *              Ariel Tal
 *              Schwartz Adi
 */


@Animate(Animation.CARD_TOP_IN_DESC)
@NonReusable
@Layout(R.layout.gallery_image)
public class GalleryImage {

    @View(R.id.galleryImage)
    private ImageView image;

    private String mUlr;
    private Context mContext;


    public GalleryImage(Context context, String ulr) {
        mContext = context;
        mUlr = ulr;
    }

    @Resolve
    private void onResolved() {
        Glide.with(mContext).load(mUlr).into(image);
    }

}
