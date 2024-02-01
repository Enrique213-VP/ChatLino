package com.svape.chathappy

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

fun ImageView.load(url: String, thumbs: Boolean = true) {

    val requestBuilder: RequestBuilder<Drawable> = if (thumbs) {
        Glide.with(this.context)
            .asDrawable().sizeMultiplier(0.01f)
    } else {
        Glide.with(this.context)
            .asDrawable().sizeMultiplier(1f)
    }

    if (url.isNotEmpty()) {
        Glide.with(this.context).load(url).placeholder(R.drawable.loading).error(
            R.drawable.error
        )
            .thumbnail(requestBuilder)
            .listener(object : RequestListener<Drawable?> {
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {

                    return false
                }
            }).into(this)
    }
}