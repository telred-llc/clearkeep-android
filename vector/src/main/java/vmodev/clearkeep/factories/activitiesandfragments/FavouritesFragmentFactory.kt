package vmodev.clearkeep.factories.activitiesandfragments

import im.vector.Matrix
import vmodev.clearkeep.applications.ClearKeepApplication
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IFragmentFactory
import vmodev.clearkeep.fragments.FavouritesFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment
import javax.inject.Inject

class FavouritesFragmentFactory constructor(private val application: IApplication) : IFragmentFactory {

    override fun createNewInstance(): IFragment {
        return FavouritesFragment.newInstance(Matrix.getInstance(application.getApplication()).defaultSession.myUserId);
    }
}