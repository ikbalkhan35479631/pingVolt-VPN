package com.example.ui

import android.content.Context
import android.content.Intent
import android.net.VpnService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.VpnRepository
import com.example.model.ConnectionStats
import com.example.model.DnsOption
import com.example.model.PingHistoryPoint
import com.example.model.PingTarget
import com.example.model.ServerCategory
import com.example.model.VpnProtocol
import com.example.model.VpnServer
import com.example.model.VpnState
import com.example.vpn.PingVoltVpnService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class VpnUiState(
    val vpnState: VpnState = VpnState.DISCONNECTED,
    val selectedServer: VpnServer,
    val servers: List<VpnServer> = emptyList(),
    val filteredServers: List<VpnServer> = emptyList(),
    val selectedCategory: ServerCategory = ServerCategory.ALL,
    val searchQuery: String = "",
    val selectedProtocol: VpnProtocol = VpnProtocol.TURBO_UDP,
    val isGamingMode: Boolean = true,
    val isKillSwitch: Boolean = true,
    val stats: ConnectionStats = ConnectionStats(),
    val pingHistory: List<PingHistoryPoint> = emptyList(),
    val diagnosticTargets: List<PingTarget> = emptyList(),
    val isDiagnosing: Boolean = false,
    val selectedDnsOption: DnsOption,
    val customPrimaryDns: String = "1.1.1.1",
    val customSecondaryDns: String = "8.8.8.8",
    val presetDnsList: List<DnsOption> = emptyList(),
    val developerAvatarUrl: String = VpnRepository.DEFAULT_TELEGRAM_AVATAR_URL,
    val isAvatarSyncing: Boolean = false,
    val avatarSyncStatus: String = "Synced with Telegram",
    val activeTab: Int = 0 // 0: Shield/Home, 1: Servers, 2: Diagnostics/Speed, 3: Developer
)

class VpnViewModel(
    private val repository: VpnRepository = VpnRepository()
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow(ServerCategory.ALL)
    private val _activeTab = MutableStateFlow(0)

    val uiState: StateFlow<VpnUiState> = combine(
        PingVoltVpnService.vpnState,
        repository.selectedServer,
        repository.servers,
        _selectedCategory,
        _searchQuery,
        repository.selectedProtocol,
        repository.isGamingMode,
        repository.isKillSwitchEnabled,
        PingVoltVpnService.activeStats,
        repository.pingHistory,
        repository.diagnosticTargets,
        repository.isDiagnosing,
        repository.selectedDnsOption,
        repository.customPrimaryDns,
        repository.customSecondaryDns,
        repository.developerAvatarUrl,
        repository.isAvatarSyncing,
        repository.lastAvatarSyncStatus,
        _activeTab
    ) { args ->
        val vpnState = args[0] as VpnState
        val selectedServer = args[1] as VpnServer
        val servers = args[2] as List<VpnServer>
        val category = args[3] as ServerCategory
        val query = args[4] as String
        val protocol = args[5] as VpnProtocol
        val gamingMode = args[6] as Boolean
        val killSwitch = args[7] as Boolean
        val stats = args[8] as ConnectionStats
        val pingHistory = args[9] as List<PingHistoryPoint>
        val diagTargets = args[10] as List<PingTarget>
        val isDiagnosing = args[11] as Boolean
        val selectedDns = args[12] as DnsOption
        val customPrimary = args[13] as String
        val customSecondary = args[14] as String
        val avatarUrl = args[15] as String
        val isSyncing = args[16] as Boolean
        val syncStatus = args[17] as String
        val activeTab = args[18] as Int

        val filtered = servers.filter { server ->
            val matchesCategory = when (category) {
                ServerCategory.ALL -> true
                ServerCategory.FASTEST -> server.currentPingMs <= 20
                ServerCategory.GAMING -> server.isGamingOptimized
                ServerCategory.STREAMING -> server.isStreamingOptimized
            }
            val matchesQuery = query.isBlank() ||
                    server.name.contains(query, ignoreCase = true) ||
                    server.country.contains(query, ignoreCase = true) ||
                    server.city.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }.sortedBy { it.currentPingMs }

        VpnUiState(
            vpnState = vpnState,
            selectedServer = selectedServer,
            servers = servers,
            filteredServers = filtered,
            selectedCategory = category,
            searchQuery = query,
            selectedProtocol = protocol,
            isGamingMode = gamingMode,
            isKillSwitch = killSwitch,
            stats = stats,
            pingHistory = pingHistory,
            diagnosticTargets = diagTargets,
            isDiagnosing = isDiagnosing,
            selectedDnsOption = selectedDns,
            customPrimaryDns = customPrimary,
            customSecondaryDns = customSecondary,
            presetDnsList = repository.presetDnsOptions,
            developerAvatarUrl = avatarUrl,
            isAvatarSyncing = isSyncing,
            avatarSyncStatus = syncStatus,
            activeTab = activeTab
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = VpnUiState(
            selectedServer = repository.selectedServer.value,
            servers = repository.servers.value,
            filteredServers = repository.servers.value,
            selectedDnsOption = repository.selectedDnsOption.value,
            presetDnsList = repository.presetDnsOptions,
            developerAvatarUrl = repository.developerAvatarUrl.value,
            isAvatarSyncing = false,
            avatarSyncStatus = repository.lastAvatarSyncStatus.value
        )
    )

    init {
        // Sync latest Telegram profile picture on app launch
        refreshTelegramAvatar()
        // Feed ping history updates when connected
        viewModelScope.launch {
            PingVoltVpnService.activeStats.collect { stats ->
                if (PingVoltVpnService.vpnState.value == VpnState.CONNECTED) {
                    repository.addPingPoint(stats.currentPingMs)
                }
            }
        }
        // Background refresh of server latency
        viewModelScope.launch {
            while (true) {
                delay(15000)
                repository.refreshServerPings()
            }
        }
    }

    fun handleConnectToggle(context: Context, onPrepareNeeded: (Intent) -> Unit) {
        val currentState = PingVoltVpnService.vpnState.value
        if (currentState == VpnState.CONNECTED || currentState == VpnState.CONNECTING || currentState == VpnState.OPTIMIZING) {
            PingVoltVpnService.stopVpn(context)
        } else {
            try {
                val prepareIntent = VpnService.prepare(context)
                if (prepareIntent != null) {
                    onPrepareNeeded(prepareIntent)
                } else {
                    startVpnDirectly(context)
                }
            } catch (e: Exception) {
                android.util.Log.w("VpnViewModel", "prepareIntent failed", e)
                startVpnDirectly(context)
            }
        }
    }

    fun onVpnPermissionGranted(context: Context) {
        startVpnDirectly(context)
    }

    private fun startVpnDirectly(context: Context) {
        val server = repository.selectedServer.value
        val effectivePing = if (repository.isGamingMode.value) {
            (server.currentPingMs - 3).coerceAtLeast(6)
        } else {
            server.currentPingMs
        }
        val (dnsPrimary, dnsSecondary) = repository.getEffectiveDnsIps()
        PingVoltVpnService.startVpn(context, server.name, effectivePing, dnsPrimary, dnsSecondary)
    }

    fun selectServer(server: VpnServer, context: Context? = null) {
        repository.selectServer(server)
        if (PingVoltVpnService.vpnState.value == VpnState.CONNECTED && context != null) {
            // Reconnect to new server with chosen DNS
            PingVoltVpnService.stopVpn(context)
            val effectivePing = if (repository.isGamingMode.value) (server.currentPingMs - 3).coerceAtLeast(6) else server.currentPingMs
            val (dnsPrimary, dnsSecondary) = repository.getEffectiveDnsIps()
            PingVoltVpnService.startVpn(context, server.name, effectivePing, dnsPrimary, dnsSecondary)
        }
    }

    fun selectCategory(category: ServerCategory) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectProtocol(protocol: VpnProtocol) {
        repository.selectProtocol(protocol)
    }

    fun setGamingMode(enabled: Boolean) {
        repository.setGamingMode(enabled)
    }

    fun setKillSwitch(enabled: Boolean) {
        repository.setKillSwitch(enabled)
    }

    fun setDnsOption(dns: DnsOption, context: Context? = null) {
        repository.setDnsOption(dns)
        if (PingVoltVpnService.vpnState.value == VpnState.CONNECTED && context != null) {
            val server = repository.selectedServer.value
            val effectivePing = if (repository.isGamingMode.value) (server.currentPingMs - 3).coerceAtLeast(6) else server.currentPingMs
            val (p, s) = repository.getEffectiveDnsIps()
            PingVoltVpnService.stopVpn(context)
            PingVoltVpnService.startVpn(context, server.name, effectivePing, p, s)
        }
    }

    fun setCustomDns(primary: String, secondary: String, context: Context? = null) {
        repository.setCustomDns(primary, secondary)
        if (PingVoltVpnService.vpnState.value == VpnState.CONNECTED && context != null) {
            val server = repository.selectedServer.value
            val effectivePing = if (repository.isGamingMode.value) (server.currentPingMs - 3).coerceAtLeast(6) else server.currentPingMs
            val (p, s) = repository.getEffectiveDnsIps()
            PingVoltVpnService.stopVpn(context)
            PingVoltVpnService.startVpn(context, server.name, effectivePing, p, s)
        }
    }

    fun setActiveTab(tab: Int) {
        _activeTab.value = tab
    }

    fun runDiagnostics() {
        viewModelScope.launch {
            repository.runFullDiagnostics()
        }
    }

    fun autoSelectFastest(context: Context? = null) {
        repository.autoPickFastestServer()
        if (PingVoltVpnService.vpnState.value == VpnState.CONNECTED && context != null) {
            selectServer(repository.selectedServer.value, context)
        }
    }

    fun refreshTelegramAvatar() {
        viewModelScope.launch {
            repository.syncTelegramProfilePic()
        }
    }
}
