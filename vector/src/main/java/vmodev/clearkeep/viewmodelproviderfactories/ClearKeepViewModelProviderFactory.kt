package vmodev.clearkeep.viewmodelproviderfactories

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ClearKeepViewModelProviderFactory @Inject constructor(private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<out ViewModel>>) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var creator = creators[modelClass];
        if (creator == null) {
            val obj = creators.entries.firstOrNull { entry -> modelClass.isAssignableFrom(entry.key) };
            if (obj != null) {
                val value = obj.value;
                if (value == null) {
                    throw IllegalArgumentException("unknown model class $modelClass");
                } else {
                    try {
                        @Suppress("UNCHECKED_CAST")
                        return value.get() as T;
                    } catch (e: Exception) {
                        throw RuntimeException(e);
                    }
                }
            } else {
                throw IllegalArgumentException("unknown model class $modelClass");
            }
        } else {
            try {
                @Suppress("UNCHECKED_CAST")
                return creator.get() as T;
            } catch (e: Exception) {
                throw RuntimeException(e);
            }
        }
    }
}