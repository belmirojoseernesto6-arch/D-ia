package com.example.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NetworkService(private val context: Context) {

    private val connectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _isSimulatedOffline = MutableStateFlow(false)
    val isSimulatedOffline: StateFlow<Boolean> = _isSimulatedOffline.asStateFlow()

    private val _isRealOnline = MutableStateFlow(checkRealInternet())
    val isRealOnline: StateFlow<Boolean> = _isRealOnline.asStateFlow()

    private val _isOnline = MutableStateFlow(temInternet())
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    init {
        try {
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()

            connectivityManager.registerNetworkCallback(request, object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    _isRealOnline.value = true
                    updateEffectiveOnlineState()
                }

                override fun onLost(network: Network) {
                    _isRealOnline.value = checkRealInternet()
                    updateEffectiveOnlineState()
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    val hasInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    _isRealOnline.value = hasInternet
                    updateEffectiveOnlineState()
                }
            })
        } catch (_: Exception) {
            _isRealOnline.value = true
            updateEffectiveOnlineState()
        }
    }

    private fun checkRealInternet(): Boolean {
        return try {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } catch (_: Exception) {
            false
        }
    }

    private fun updateEffectiveOnlineState() {
        val effective = if (_isSimulatedOffline.value) false else _isRealOnline.value
        _isOnline.value = effective
    }

    /**
     * Verifica se o dispositivo tem internet no momento.
     * Retorna false se o usuário ativou modo offline ou se o aparelho estiver desconectado.
     */
    fun temInternet(): Boolean {
        if (_isSimulatedOffline.value) return false
        return checkRealInternet()
    }

    fun toggleSimulatedOffline(offline: Boolean) {
        _isSimulatedOffline.value = offline
        updateEffectiveOnlineState()
    }
}
