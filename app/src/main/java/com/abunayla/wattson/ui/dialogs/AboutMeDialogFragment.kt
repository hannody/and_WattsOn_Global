package com.abunayla.wattson.ui.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.abunayla.wattson.R


class AboutMeDialogFragment : DialogFragment() {
    private val intent = Intent(Intent.ACTION_VIEW)
    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.about_me_layout, null))

                // Add action buttons
                .setPositiveButton(R.string.positive_btn_text
                ) { _, _ ->
                    // sign in the user ...
                    intent.data = Uri.parse(getString(R.string.abunayla_com))
                    startActivity(intent)
                }
                .setNegativeButton(R.string.negative_btn_text
                ) { dialog, _ ->
                    getDialog()?.cancel()
                    dialog.dismiss()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}