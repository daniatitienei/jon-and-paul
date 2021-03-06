package com.jonandpaul.jonandpaul.data.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jonandpaul.jonandpaul.data.repository.*
import com.jonandpaul.jonandpaul.domain.repository.*
import com.jonandpaul.jonandpaul.domain.use_case.address_datastore.ShippingDetailsUseCases
import com.jonandpaul.jonandpaul.domain.use_case.address_datastore.GetShippingDetails
import com.jonandpaul.jonandpaul.domain.use_case.address_datastore.SaveShippingDetails
import com.jonandpaul.jonandpaul.domain.use_case.counties_api.GetCounties
import com.jonandpaul.jonandpaul.domain.use_case.firestore.FirestoreUseCases
import com.jonandpaul.jonandpaul.domain.use_case.firestore.cart.*
import com.jonandpaul.jonandpaul.domain.use_case.firestore.favorites.DeleteFavorite
import com.jonandpaul.jonandpaul.domain.use_case.firestore.favorites.FavoritesUseCases
import com.jonandpaul.jonandpaul.domain.use_case.firestore.favorites.GetFavorites
import com.jonandpaul.jonandpaul.domain.use_case.firestore.favorites.InsertFavorite
import com.jonandpaul.jonandpaul.domain.use_case.firestore.orders.GetOrderById
import com.jonandpaul.jonandpaul.domain.use_case.firestore.orders.GetOrders
import com.jonandpaul.jonandpaul.domain.use_case.firestore.orders.OrderUseCases
import com.jonandpaul.jonandpaul.domain.use_case.firestore.products.GetProducts
import com.jonandpaul.jonandpaul.domain.use_case.firestore.products.GetSuggestions
import com.jonandpaul.jonandpaul.ui.utils.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@ExperimentalCoroutinesApi
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

    @Provides
    @Singleton
    fun provideFavoritesRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): FavoritesRepository = FavoritesRepositoryImpl(
        auth = auth,
        firestore = firestore
    )

    @Provides
    @Singleton
    fun provideCartRepository(auth: FirebaseAuth, firestore: FirebaseFirestore): CartRepository =
        CartRepositoryImpl(auth = auth, firestore = firestore)

    @Provides
    @Singleton
    fun provideFavoritesUseCases(
        repository: FavoritesRepository
    ): FavoritesUseCases = FavoritesUseCases(
        getFavorites = GetFavorites(repository = repository),
        deleteFavorite = DeleteFavorite(repository = repository),
        insertFavorite = InsertFavorite(repository = repository)
    )

    @Provides
    @Singleton
    fun provideProductsRepository(
        firestore: FirebaseFirestore
    ): ProductsRepository = ProductsRepositoryImpl(firestore = firestore)

    @Provides
    @Singleton
    fun provideGetProductsUseCases(
        repository: ProductsRepository
    ): GetProducts = GetProducts(repository = repository)

    @Provides
    @Singleton
    fun provideGetSuggestionsUseCases(
        repository: ProductsRepository
    ): GetSuggestions = GetSuggestions(repository = repository)

    @Provides
    @Singleton
    fun provideOrdersRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): OrderRepository = OrderRepositoryImpl(auth = auth, firestore = firestore)

    @Singleton
    @Provides
    fun provideCartUseCases(
        repository: CartRepository
    ): CartUseCases = CartUseCases(
        getCartItems = GetCartItems(repository = repository),
        insertCartItem = InsertCartItem(repository = repository),
        deleteCartItem = DeleteCartItem(repository = repository),
        updateQuantity = UpdateQuantity(repository = repository),
        clearCart = ClearCart(repository = repository)
    )

    @Provides
    @Singleton
    fun provideOrderUseCases(
        repository: OrderRepository
    ): OrderUseCases = OrderUseCases(
        getOrderById = GetOrderById(repository = repository),
        getOrders = GetOrders(repository = repository)
    )

    @Provides
    @Singleton
    fun provideFirestoreUseCases(
        getProducts: GetProducts,
        favoritesUseCases: FavoritesUseCases,
        getSuggestions: GetSuggestions,
        cartUseCases: CartUseCases,
        orderUseCases: OrderUseCases
    ): FirestoreUseCases = FirestoreUseCases(
        getProducts = getProducts,
        favorites = favoritesUseCases,
        getSuggestions = getSuggestions,
        cart = cartUseCases,
        orders = orderUseCases
    )
}