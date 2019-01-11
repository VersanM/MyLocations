package com.mihaiv.test.mylocations.utils.ui

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import com.mihaiv.test.mylocations.R
import com.mihaiv.test.mylocations.data.local.db.locations.Location
import com.mihaiv.test.mylocations.utils.addTextChangedListener
import kotlinx.android.synthetic.main.add_edit_location_dialog.*


class AddOrEditLocationDialog(
    private var activity: Activity,
    private val openMode: Mode,
    private val onYesClicked: (Location) -> Unit
) : Dialog(activity) {

    enum class Mode {
        ADD_MODE,
        EDIT_MODE;
    }

    var yes: TextView? = null
    var no: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.add_edit_location_dialog)

        if (openMode == Mode.EDIT_MODE) {
            addEditLocationDialogTitle.text = activity.getString(R.string.edit_location_dialog_title)
        }

        yes = findViewById(R.id.addEditOkButton)
        no = findViewById(R.id.addEditCancelButton)

        addTextListeners()

        yes?.setOnClickListener {
            // TODO : validate inputs!!!
            val location = Location(
                latitude = addEditLocationLatitude.text.toString().toDouble(),
                longitude = addEditLocationLongitude.text.toString().toDouble(),
                tag = addEditLocationTag.text.toString(),
                address = addEditLocationAddress.text.toString()
            )
            onYesClicked(location)
            dismiss()
        }

        no?.setOnClickListener {
            dismiss()
        }

    }

    private fun addTextListeners() {
        addEditLocationLatitude.addTextChangedListener {
            enableYesButtonIfNeeded()
        }
        addEditLocationLongitude.addTextChangedListener {
            enableYesButtonIfNeeded()
        }
        addEditLocationTag.addTextChangedListener {
            enableYesButtonIfNeeded()
        }
        addEditLocationAddress.addTextChangedListener {
            enableYesButtonIfNeeded()
        }
    }

    private fun enableYesButtonIfNeeded() {
        addEditOkButton.isEnabled = addEditLocationLatitude.text.toString().isNotEmpty() &&
                addEditLocationLongitude.text.toString().isNotEmpty() &&
                addEditLocationTag.text.toString().isNotEmpty() &&
                addEditLocationAddress.text.toString().isNotEmpty()
    }
}