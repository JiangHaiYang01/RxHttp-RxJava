package com.allens.impl

import retrofit2.Retrofit
import java.util.prefs.PreferencesFactory

interface OnBuildClientListener {

    fun addBuildClient(): MutableSet<Any>

}