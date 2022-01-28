package com.jonandpaul.jonandpaul.data.di

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jonandpaul.jonandpaul.CartDatabase
import com.jonandpaul.jonandpaul.JonAndPaulApplication
import com.jonandpaul.jonandpaul.data.repository.CartDataSourceImpl
import com.jonandpaul.jonandpaul.data.repository.CountiesRepository
import com.jonandpaul.jonandpaul.data.repository.StoreAddressImpl
import com.jonandpaul.jonandpaul.domain.repository.CartDataSource
import com.jonandpaul.jonandpaul.domain.repository.CountiesApi
import com.jonandpaul.jonandpaul.domain.repository.StoreAddress
import com.jonandpaul.jonandpaul.domain.use_case.address_datastore.AddressUseCases
import com.jonandpaul.jonandpaul.domain.use_case.address_datastore.GetAddress
import com.jonandpaul.jonandpaul.domain.use_case.address_datastore.SaveAddress
import com.jonandpaul.jonandpaul.domain.use_case.counties_api.GetCounties
import com.jonandpaul.jonandpaul.ui.utils.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplication(): JonAndPaulApplication = JonAndPaulApplication()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .addLast(
            KotlinJsonAdapterFactory()
        ).build()

    @Provides
    @Singleton
    fun provideCartDataSource(app: Application): CartDataSource =
        CartDataSourceImpl(
            CartDatabase(
                driver = AndroidSqliteDriver(
                    CartDatabase.Schema,
                    app,
                    "cart.db"
                )
            )
        )

    @Provides
    @Singleton
    fun provideStoreAddressRepository(app: Application): StoreAddress =
        StoreAddressImpl(context = app)

    @Provides
    @Singleton
    fun provideAddressUseCases(repository: StoreAddress): AddressUseCases =
        AddressUseCases(
            getAddress = GetAddress(repository = repository),
            saveAddress = SaveAddress(repository = repository)
        )

    @Provides
    @Singleton
    fun provideCountiesApi(moshi: Moshi): CountiesApi = Retrofit.Builder()
        .baseUrl(Constants.ROMANIA_COUNTIES_API)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(CountiesApi::class.java)

    @Provides
    @Singleton
    fun provideCountiesRepository(api: CountiesApi) =
        CountiesRepository(api = api)

    @Provides
    @Singleton
    fun provideCountiesUseCase(repository: CountiesRepository) : GetCounties =
        GetCounties(repository = repository)
}