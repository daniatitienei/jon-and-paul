package com.jonandpaul.jonandpaul.ui.screens.inspect_order

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InspectOrderViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _state = mutableStateOf(InspectOrderState())
    val state: State<InspectOrderState> = _state

    init {
        getOrderInfo()
    }

    private fun getOrderInfo() {
        _state.value = _state.value.copy(isLoading = true)

        val orderId = savedStateHandle.get<String>("orderId")!!.toInt()

        firestore.collection("user/${auth.currentUser!!.uid}/orders")
            .whereEqualTo("id", orderId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents)
                Log.d("order", orderId.toString())
//                _state.value = _state.value.copy(
//                    order = documents.documents.first().toObject()!!,
//                    isLoading = false
//                )
            }
    }
}