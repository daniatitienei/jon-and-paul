package com.jonandpaul.jonandpaul.data.di

import android.app.Application
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jonandpaul.jonandpaul.CartDatabase
import com.jonandpaul.jonandpaul.data.repository.CartDataSourceImpl
import com.jonandpaul.jonandpaul.data.repository.CountiesRepository
import com.jonandpaul.jonandpaul.data.repository.StoreShippingDetailsImpl
import com.jonandpaul.jonandpaul.domain.repository.CartDataSource
import com.jonandpaul.jonandpaul.domain.repository.CountiesApi
import com.jonandpaul.jonandpaul.domain.repository.StoreShippingDetails
import com.jonandpaul.jonandpaul.domain.use_case.address_datastore.ShippingDetailsUseCases
import com.jonandpaul.jonandpaul.domain.use_case.address_datastore.GetShippingDetails
import com.jonandpaul.jonandpaul.domain.use_case.address_datastore.SaveShippingDetails
import com.jonandpaul.jonandpaul.domain.use_case.counties_api.GetCounties
import com.jonandpaul.jonandpaul.ui.utils.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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
    fun provideCartDataSource(@ApplicationContext context: Context): CartDataSource =
        CartDataSourceImpl(
            CartDatabase(
                driver = AndroidSqliteDriver(
                    CartDatabase.Schema,
                    context = context,
                    "cart.db"
                )
            )
        )

    @Provides
    @Singleton
    fun provideStoreAddressRepository(
        @ApplicationContext context: Context, moshi: Moshi
    ): StoreShippingDetails =
        StoreShippingDetailsImpl(context = context, moshi = moshi)

    @Provides
    @Singleton
    fun provideAddressUseCases(repository: StoreShippingDetails): ShippingDetailsUseCases =
        ShippingDetailsUseCases(
            getShippingDetails = GetShippingDetails(repository = repository),
            saveShippingDetails = SaveShippingDetails(repository = repository)
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
    fun provideCountiesUseCase(repository: CountiesRepository): GetCounties =
        GetCounties(repository = repository)
}