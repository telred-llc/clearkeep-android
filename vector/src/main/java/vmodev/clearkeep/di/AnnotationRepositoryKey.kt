package vmodev.clearkeep.di

import dagger.MapKey
import vmodev.clearkeep.repositories.interfaces.IRepository
import kotlin.reflect.KClass

@MustBeDocumented
@Target(
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_SETTER,
        AnnotationTarget.PROPERTY_GETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class AnnotationRepositoryKey(val value: KClass<out IRepository>)