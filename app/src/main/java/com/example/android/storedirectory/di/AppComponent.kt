package com.example.android.storedirectory.di

import android.content.Context
import com.example.android.storedirectory.StoreDirectoryApplication
import com.example.android.storedirectory.data.DataModule
import com.example.android.storedirectory.ui.UIModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        DataModule::class,
        UIModule::class
    ]
)
@Singleton
interface AppComponent {
    fun inject(app: StoreDirectoryApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: StoreDirectoryApplication): Builder

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}