package com.example.android.storedirectory.ui

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.storedirectory.di.ViewModelKey
import com.example.android.storedirectory.repository.Repository
import com.example.android.storedirectory.ui.detail.DetailFragment
import com.example.android.storedirectory.ui.detail.DetailViewModel
import com.example.android.storedirectory.ui.storelist.StoreListFragment
import com.example.android.storedirectory.ui.storelist.StoreListViewModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import javax.inject.Provider

@Module(
    includes = [
        UIModule.ProvideViewModel::class,
        UIModule.ProvideViewModelFactory::class
    ]
)
abstract class UIModule {

    @ContributesAndroidInjector(modules = [InjectIntoFragment::class])
    abstract fun bindStoreListFragment(): StoreListFragment

    @ContributesAndroidInjector(modules = [InjectIntoFragment::class])
    abstract fun bindDetailFragment(): DetailFragment

    @ContributesAndroidInjector(modules = [InjectIntoActivity::class])
    abstract fun bindMainActivity(): MainActivity

    @Module
    class ProvideViewModelFactory {
        @Provides
        fun provideViewModelFactory(
            providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
        ): ViewModelProvider.Factory =
            ViewModelFactory(providers)
    }

    @Module
    class ProvideViewModel {
        @Provides
        @IntoMap
        @ViewModelKey(StoreListViewModel::class)
        fun provideStoreListViewModel(repository: Repository): ViewModel =
            StoreListViewModel(repository)

        @Provides
        @IntoMap
        @ViewModelKey(DetailViewModel::class)
        fun provideDetailViewModel(): ViewModel =
            DetailViewModel()
    }

    @Module
    class InjectIntoFragment {
        @Provides
        fun provideStoreListViewModel(
            factory: ViewModelProvider.Factory,
            target: StoreListFragment
        ): StoreListViewModel =
            ViewModelProvider(target, factory).get(StoreListViewModel::class.java)

        @Provides
        fun provideDetailViewModel(
            factory: ViewModelProvider.Factory,
            target: DetailFragment
        ): DetailViewModel =
            ViewModelProvider(target, factory).get(DetailViewModel::class.java)
    }

    @Module
    class InjectIntoActivity {
        @Module
        companion object {
            @JvmStatic
            @Provides
            fun provideNetworkMonitor(context: Context): NetworkStateMonitor? {
                val connectivityManager = context.getSystemService<ConnectivityManager>()
                return if (connectivityManager != null) {
                    NetworkStateMonitor(connectivityManager)
                } else {
                    null
                }
            }
        }
    }
}