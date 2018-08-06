package com.gmail.gaganag50.greendao


import android.app.Application
import android.util.Log
import com.gmail.gaganag50.greendao.db.MyObjectBox
import io.objectbox.BoxStore

class App : Application() {

    var boxStore: BoxStore? = null
        private set

    override fun onCreate() {
        super.onCreate()

        // do this once, for example in your Application class
        boxStore = MyObjectBox.builder().androidContext(this@App).build()


        Log.d("App", "Using ObjectBox " + BoxStore.getVersion() + " (" + BoxStore.getVersionNative() + ")")
    }

    companion object {

        val TAG = "ObjectBoxExample"
        val EXTERNAL_DIR = false
    }
}
