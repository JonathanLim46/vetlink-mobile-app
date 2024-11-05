package com.example.vetlink

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable

class LoadingAlert(private val activity: Activity) {

    private lateinit var dialog: AlertDialog

    fun startAlertDialog(){
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.layout_progress_bar, null)

        builder.setView(view)
        builder.setCancelable(false)
        dialog = builder.create()

        dialog.show()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }

    fun stopAlertDialog(){
        dialog.dismiss()
    }
}