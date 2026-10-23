package com.example

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.VpnViewModel
import com.example.ui.screens.DeveloperInformationScreen
import com.example.ui.screens.PingDiagnosticScreen
import com.example.ui.screens.ServerListScreen
import com.example.ui.screens.TunnelScreen
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DeepNavyBg
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SurfaceCardBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                PingVoltApp()
            }
        }
    }
}

@Composable
fun PingVoltApp(
    viewModel: VpnViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // VPN Permission Launcher
    val vpnPrepareLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.onVpnPermissionGranted(context)
        }
    }

    // Android 13+ Notification Permission Launcher
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { /* Gracefully handle notification permission */ }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // BackHandler: Return to Home (Tab 0) if on secondary tabs
    if (uiState.activeTab != 0) {
        BackHandler {
            viewModel.setActiveTab(0)
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            NavigationBar(
                containerColor = SurfaceCardBg,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .border(width = 1.dp, color = BorderSubtle)
                    .testTag("bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = uiState.activeTab == 0,
                    onClick = { viewModel.setActiveTab(0) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Tunnel Tab",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Tunnel",
                            fontWeight = if (uiState.activeTab == 0) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CyberCyan,
                        selectedTextColor = CyberCyan,
                        indicatorColor = CyberCyan.copy(alpha = 0.18f),
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_item_tunnel")
                )

                NavigationBarItem(
                    selected = uiState.activeTab == 1,
                    onClick = { viewModel.setActiveTab(1) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = "Servers Tab",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Nodes",
                            fontWeight = if (uiState.activeTab == 1) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CyberCyan,
                        selectedTextColor = CyberCyan,
                        indicatorColor = CyberCyan.copy(alpha = 0.18f),
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_item_servers")
                )

                NavigationBarItem(
                    selected = uiState.activeTab == 2,
                    onClick = { viewModel.setActiveTab(2) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = "Diagnostics Tab",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Ping Test",
                            fontWeight = if (uiState.activeTab == 2) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CyberCyan,
                        selectedTextColor = CyberCyan,
                        indicatorColor = CyberCyan.copy(alpha = 0.18f),
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_item_diagnostics")
                )

                NavigationBarItem(
                    selected = uiState.activeTab == 3,
                    onClick = { viewModel.setActiveTab(3) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Developer Tab",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Developer",
                            fontWeight = if (uiState.activeTab == 3) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CyberCyan,
                        selectedTextColor = CyberCyan,
                        indicatorColor = CyberCyan.copy(alpha = 0.18f),
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_item_developer")
                )
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyBg)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.activeTab) {
                0 -> TunnelScreen(
                    uiState = uiState,
                    onConnectToggle = {
                        viewModel.handleConnectToggle(context) { prepareIntent ->
                            vpnPrepareLauncher.launch(prepareIntent)
                        }
                    },
                    onServerClick = { viewModel.setActiveTab(1) },
                    onGamingModeChange = { viewModel.setGamingMode(it) },
                    onOpenDeveloperSystem = { viewModel.setActiveTab(3) },
                    onSelectDns = { viewModel.setDnsOption(it, context) },
                    onSaveCustomDns = { p, s -> viewModel.setCustomDns(p, s, context) }
                )
                1 -> ServerListScreen(
                    uiState = uiState,
                    onServerSelect = { server ->
                        viewModel.selectServer(server, context)
                        viewModel.setActiveTab(0)
                    },
                    onCategorySelect = { viewModel.selectCategory(it) },
                    onSearchChange = { viewModel.setSearchQuery(it) },
                    onAutoPickFastest = {
                        viewModel.autoSelectFastest(context)
                        viewModel.setActiveTab(0)
                    }
                )
                2 -> PingDiagnosticScreen(
                    uiState = uiState,
                    onRunDiagnostics = { viewModel.runDiagnostics() },
                    onProtocolSelect = { viewModel.selectProtocol(it) },
                    onKillSwitchChange = { viewModel.setKillSwitch(it) },
                    onSelectDns = { viewModel.setDnsOption(it, context) },
                    onSaveCustomDns = { p, s -> viewModel.setCustomDns(p, s, context) }
                )
                3 -> DeveloperInformationScreen(
                    uiState = uiState,
                    onRefreshAvatar = { viewModel.refreshTelegramAvatar() }
                )
            }
        }
    }
}
