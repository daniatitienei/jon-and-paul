package com.jonandpaul.jonandpaul.data.di

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jonandpaul.jonandpaul.CartDatabase
import com.jonandpaul.jonandpaul.JonAndPaulApplication
import com.jonandpaul.jonandpaul.data.repository.CartDataSourceImpl
import com.jonandpaul.jonandpaul.domain.repository.CartDataSource
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
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
    fun provideCartDataSource(driver: SqlDriver): CartDataSource =
        CartDataSourceImpl(CartDatabase(driver = driver))

    @Provides
    @Singleton
    fun provideCartDatabase(app: Application): SqlDriver =
        AndroidSqliteDriver(CartDatabase.Schema, app, "cart.db")
}