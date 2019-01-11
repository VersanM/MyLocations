package com.mihaiv.test.mylocations.screen.main

import android.support.annotation.StringRes
import com.mihaiv.test.mylocations.data.local.db.locations.Location

interface MainController {

    interface Navigation {

        fun goToLocationsList()

        fun goToLocationDetails(location: Location)
    }

    interface View {

        fun setToolbarTitle(@StringRes titleId: Int)

        fun showBackMenuIcon()

        fun hideBackMenuIcon()

        fun showAddMenuIcon()

        fun hideAddMenuIcon()

        fun showEditMenuIcon()

        fun hideEditMenuIcon()

        fun showDeleteMenuIcon()

        fun hideDeleteMenuIcon()
    }
}