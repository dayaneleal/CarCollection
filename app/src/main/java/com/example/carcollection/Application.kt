package com.example.carcollection

import android.app.Application
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.example.carcollection.data.database.DatabaseBuilder

class Application  : Application(), SingletonImageLoader.Factory {
    override fun onCreate() {
        super.onCreate()
        DatabaseBuilder.getInstance(this)
    }

    override fun newImageLoader(context: coil3.PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .components {
                add(OkHttpNetworkFetcherFactory())
            }
            .build()
    }
}
