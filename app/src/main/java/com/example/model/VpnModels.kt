package com.example.model

enum class VpnState {
    DISCONNECTED,
    CONNECTING,
    OPTIMIZING,
    CONNECTED
}

enum class ServerCategory(val label: String) {
    FASTEST("Auto Best"),
    GAMING("Gaming (Low Ping)"),
    STREAMING("Streaming & P2P"),
    ALL("All Regions")
}

data class VpnServer(
    val id: String,
    val name: String,
    val country: String,
    val countryCode: String,
    val flagEmoji: String,
    val city: String,
    val basePingMs: Int,
    val currentPingMs: Int = basePingMs,
    val serverLoad: Int, // 0 - 100%
    val ipAddress: String,
    val isGamingOptimized: Boolean = true,
    val isStreamingOptimized: Boolean = false,
    val isVip: Boolean = false,
    val categories: List<ServerCategory> = listOf(ServerCategory.ALL)
)

enum class VpnProtocol(val displayName: String, val subtitle: String, val badge: String) {
    TURBO_UDP("Turbo UDP (WireGuard)", "Ultra-low latency optimized for mobile esports", "FASTEST"),
    HYPER_IKEV2("Hyper-IKEv2", "Instant re-connection when switching 4G/5G/WiFi", "STABLE"),
    SHADOW_STEALTH("Shadow Stealth", "Obfuscated packets to bypass firewalls and DPI", "STEALTH"),
    OPENVPN_TCP("OpenVPN Shield", "Maximum AES-256 military-grade encryption", "SECURE")
}

data class ConnectionStats(
    val downloadKbps: Long = 0L,
    val uploadKbps: Long = 0L,
    val totalBytesDown: Long = 0L,
    val totalBytesUp: Long = 0L,
    val connectedSeconds: Long = 0L,
    val currentPingMs: Int = 18,
    val jitterMs: Double = 1.2,
    val packetLossPercent: Double = 0.0,
    val publicIp: String = "104.28.19.42",
    val encryption: String = "ChaCha20-Poly1305"
)

enum class DiagnosticStatus {
    IDLE,
    TESTING,
    SUCCESS,
    FAILED
}

data class PingTarget(
    val id: String,
    val name: String,
    val category: String, // "Gaming", "DNS", "Voice", "Cloud"
    val host: String,
    val port: Int = 443,
    val iconName: String = "dns",
    val pingMs: Int? = null,
    val jitterMs: Double? = null,
    val status: DiagnosticStatus = DiagnosticStatus.IDLE
)

data class PingHistoryPoint(
    val timestampMs: Long,
    val pingMs: Int
)

data class DnsOption(
    val id: String,
    val name: String,
    val provider: String,
    val primaryIp: String,
    val secondaryIp: String = "",
    val description: String = "",
    val isCustom: Boolean = false
)
