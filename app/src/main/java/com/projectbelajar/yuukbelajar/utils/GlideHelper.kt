package com.projectbelajar.yuukbelajar.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

object GlideHelper {

    fun setImage(context: Context, urlImage: String, imageView: ImageView){
        Glide.with(context)
                .load(urlImage)
//                .placeholder(R.drawable.img_no_image)
//                .error(R.drawable.img_no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)

    }
}