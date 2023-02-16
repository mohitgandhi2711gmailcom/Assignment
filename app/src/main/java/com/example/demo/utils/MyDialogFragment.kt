package com.example.demo.utils

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.demo.R

class MyDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertDialogBuilder = AlertDialog.Builder(context)
        val view = activity?.layoutInflater?.inflate(R.layout.task_popup, null)
        alertDialogBuilder.setView(view)
        return alertDialogBuilder.create()
    }
}