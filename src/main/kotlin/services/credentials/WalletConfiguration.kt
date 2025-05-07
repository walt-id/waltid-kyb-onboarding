package id.walt.services.credentials

import kotlinx.serialization.Serializable

@Serializable
data class WalletConfiguration(
    val walletUrl: String,
)