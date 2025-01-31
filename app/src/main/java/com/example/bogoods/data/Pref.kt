package com.example.bogoods.data

import android.content.Context
import android.content.SharedPreferences

class Pref {
    val USER_ID = "uidx"
    val COUNTER_ID = "counter"
    val statusLogin = "STATUS"


    var mContext: Context
    var sharedSet: SharedPreferences

    constructor(ctx: Context) {
        mContext = ctx
        sharedSet = mContext.getSharedPreferences("APLIKASITESDB", Context.MODE_PRIVATE)
    }

    fun saveUID(uid: String) {
        val edit = sharedSet.edit()
        edit.putString(USER_ID, uid)
        edit.apply()
    }

    fun getUID(): String? {
        return sharedSet.getString(USER_ID, " ")
    }

    fun getUIDD(): String? {
        return sharedSet.getString(USER_ID, " ")
    }
    fun saveCounterId(counter: Int) {
        val edit = sharedSet.edit()
        edit.putInt(COUNTER_ID, counter)
        edit.apply()
    }

    fun getCounterId(): Int {
        return sharedSet.getInt(COUNTER_ID, 1)
    }

    fun setStatusS(status: Boolean) {
        val edit = sharedSet.edit()
        edit.putBoolean(statusLogin, status)
        edit.apply()
    }

    fun cekStatusS(): Boolean? {
        return sharedSet.getBoolean(statusLogin, false)
    }
    fun setStatusR(status: Boolean) {
        val edit = sharedSet.edit()
        edit.putBoolean(statusLogin, status)
        edit.apply()
    }

    fun cekStatusR(): Boolean? {
        return sharedSet.getBoolean(statusLogin, false)
    }
    fun setStatus(status: Boolean) {
        val edit = sharedSet.edit()
        edit.putBoolean(statusLogin, status)
        edit.apply()
    }

    fun cekStatus(): Boolean? {
        return sharedSet.getBoolean(statusLogin, false)
    }
}
