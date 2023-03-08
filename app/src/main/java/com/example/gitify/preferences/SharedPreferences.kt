package com.example.gitify.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.gitify.utils.Constants

class SharedPreferences(context: Context) {
  private var preference = context.getSharedPreferences(PREF_NAME, PREF_MODE)

  companion object{
    private const val PREF_NAME = Constants.APP_NAME
    private const val PREF_MODE = Context.MODE_PRIVATE
    private val PREF_ACCESS_TOKEN = Pair("ACCESS_TOKEN", "")
  }

  var accessToken: String?
    get() = preference.getString(PREF_ACCESS_TOKEN.first, PREF_ACCESS_TOKEN.second)
    set(value) = preference.edit{
      it.putString(PREF_ACCESS_TOKEN.first, value)
    }
}

private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit){
  val editor = edit()
  operation(editor)
  editor.apply()
}