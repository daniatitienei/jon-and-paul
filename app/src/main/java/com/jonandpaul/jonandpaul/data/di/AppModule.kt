package com.jonandpaul.jonandpaul.data.di

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jonandpaul.jonandpaul.CartDatabase
import com.jonandpaul.jonandpaul.CreditCardDatabase
import com.jonandpaul.jonandpaul.JonAndPaulApplication
import com.jonandpaul.jonandpaul.data.repository.CartDataSourceImpl
import com.jonandpaul.jonandpaul.data.repository.CreditCardDataSourceImpl
import com.jonandpaul.jonandpaul.data.repository.StoreAddressImpl
import com.jonandpaul.jonandpaul.domain.repository.CartDataSource
import com.jonandpaul.jonandpaul.domain.repository.CreditCardDataSource
import com.jonandpaul.jonandpaul.domain.repository.StoreAddress
import com.jonandpaul.jonandpaul.domain.use_case.address_datastore.AddressUseCases
import com.jonandpaul.jonandpaul.domain.use_case.address_datastore.GetAddress
import com.jonandpaul.jonandpaul.domain.use_case.address_datastore.SaveAddress
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
    fun provideCreditCardDataSource(app: Application): CreditCardDataSource =
        CreditCardDataSourceImpl(
            CreditCardDatabase(
                driver = AndroidSqliteDriver(
                    CreditCardDatabase.Schema,
                    app,
                    "credit_cards.db"
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
}