package com.mihaiv.test.mylocations.screen.main

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import com.mihaiv.test.mylocations.R
import com.mihaiv.test.mylocations.data.local.db.locations.Location
import com.mihaiv.test.mylocations.screen.base.BaseActivity
import com.mihaiv.test.mylocations.screen.main.details.LocationDetailsFragment
import com.mihaiv.test.mylocations.screen.main.list.LocationsListFragment
import com.mihaiv.test.mylocations.utils.ui.AddOrEditLocationDialog
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : BaseActivity<MainViewModel, MainViewModelFactory>(), MainController.Navigation,
    MainController.View {

    companion object {
        const val PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION = 1357
    }

    override fun getViewModelClass() = MainViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (supportFragmentManager.backStackEntryCount == 0) {
            goToLocationsList()
        }

        handleMenuOptionsClick()
    }

    private fun handleMenuOptionsClick() {
        handleAddMenuClick()
        handleEditMenuClick()
        handleDeleteMenuClick()
        handleBackMenuClick()
    }

    private fun handleAddMenuClick() {
        appBarAddMenuIcon.setOnClickListener {
            val dialog = AddOrEditLocationDialog(this, AddOrEditLocationDialog.Mode.ADD_MODE) {
                viewModel.addNewLocation(it)
            }
            dialog.show()
        }
    }

    private fun handleEditMenuClick() {
        appBarEditMenuIcon.setOnClickListener {
            // TODO: To be implemented
        }
    }

    private fun handleDeleteMenuClick() {
        appBarDeleteMenuIcon.setOnClickListener {
            showDeleteLocationDialog()
        }
    }

    private fun showDeleteLocationDialog() {
        AlertDialog.Builder(this)
            .setMessage(R.string.delete_location_message)
            .setPositiveButton(R.string.confirm) { _, _ ->
                viewModel.deleteSelectedLocation()
                onBackPressed() // to navigate to the previous screen because there is no location selected anymore (we can select the nearest location and remain on the same screen)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun handleBackMenuClick() {
        appBarBackMenuIcon.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            moveTaskToBack(true)
        } else {
            super.onBackPressed()
        }
    }

    override fun goToLocationsList() {
        clearStack()
        switchFragment(LocationsListFragment(), R.id.mainContainer, true)
    }

    override fun goToLocationDetails(location: Location) =
        switchFragment(LocationDetailsFragment.createFragment(location), R.id.mainContainer, true)

    override fun setToolbarTitle(titleId: Int) {
        appBarTitleTextView.text = getString(titleId)
    }

    override fun showBackMenuIcon() {
        appBarBackMenuIcon.visibility = View.VISIBLE
    }

    override fun hideBackMenuIcon() {
        appBarBackMenuIcon.visibility = View.GONE
    }

    override fun showAddMenuIcon() {
        appBarAddMenuIcon.visibility = View.VISIBLE
    }

    override fun hideAddMenuIcon() {
        appBarAddMenuIcon.visibility = View.GONE
    }

    override fun showEditMenuIcon() {
        appBarEditMenuIcon.visibility = View.VISIBLE
    }

    override fun hideEditMenuIcon() {
        appBarEditMenuIcon.visibility = View.GONE
    }

    override fun showDeleteMenuIcon() {
        appBarDeleteMenuIcon.visibility = View.VISIBLE
    }

    override fun hideDeleteMenuIcon() {
        appBarDeleteMenuIcon.visibility = View.GONE
    }
}
