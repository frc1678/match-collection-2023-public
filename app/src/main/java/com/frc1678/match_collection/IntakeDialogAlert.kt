package com.frc1678.match_collection

import android.app.AlertDialog
import android.app.Dialog
import androidx.fragment.app.DialogFragment
import android.os.Bundle

class IntakeDialogAlert : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater
            builder.setView(inflater.inflate(R.layout.intake_popup, null))

            dialog?.setCanceledOnTouchOutside(false);

            builder.apply {
                setNegativeButton(
                    R.string.cancel
                ) { dialog, id ->
                    // User cancelled the dialog
                }
            }

            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}