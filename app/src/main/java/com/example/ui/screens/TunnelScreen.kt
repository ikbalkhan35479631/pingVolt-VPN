package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.model.DnsOption
import com.example.model.VpnState
import com.example.ui.VpnUiState
import com.example.ui.components.CyberPowerDial
import com.example.ui.components.DnsSelectorBottomSheet
import com.example.ui.components.PingWaveformChart
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DeepNavyBg
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.SurfaceCardBg
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun TunnelScreen(
    uiState: VpnUiState,
    onConnectToggle: () -> Unit,
    onServerClick: () -> Unit,
    onGamingModeChange: (Boolean) -> Unit,
    onOpenDeveloperSystem: () -> Unit,
    onSelectDns: (DnsOption) -> Unit,
    onSaveCustomDns: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isConnected = uiState.vpnState == VpnState.CONNECTED
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var showDnsSheet by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DeepNavyBg)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(CyberCyan.copy(alpha = 0.2f), ElectricBlue.copy(alpha = 0.1f))
                            )
                        )
                        .border(1.dp, CyberCyan.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "PingVolt Logo",
                        tint = CyberCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "PINGVOLT",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "ULTRA LOW-PING ENGINE",
                        style = MaterialTheme.typography.labelSmall,
                        color = CyberCyan,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Developer quick-badge with live Telegram avatar
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(SurfaceCardBg)
                        .border(1.dp, CyberCyan.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                        .clickable { onOpenDeveloperSystem() }
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .border(1.dp, CyberCyan, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(uiState.developerAvatarUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Developer Avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Md:Ikbal khan",
                            color = TextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Status chip
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isConnected) NeonGreen.copy(alpha = 0.15f) else SurfaceElevated)
                        .border(
                            1.dp,
                            if (isConnected) NeonGreen.copy(alpha = 0.4f) else BorderSubtle,
                            RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(if (isConnected) NeonGreen else TextMuted)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = if (isConnected) "${uiState.stats.currentPingMs} ms" else "READY",
                            color = if (isConnected) NeonGreen else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Center Power Dial
        CyberPowerDial(
            vpnState = uiState.vpnState,
            currentPing = uiState.stats.currentPingMs,
            onClick = onConnectToggle
        )

        Spacer(modifier = Modifier.height(26.dp))

        // Selected Server Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .clickable { onServerClick() }
                .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                .testTag("selected_server_card"),
            colors = CardDefaults.cardColors(containerColor = SurfaceCardBg)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = uiState.selectedServer.flagEmoji,
                        fontSize = 28.sp
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = uiState.selectedServer.name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            if (uiState.selectedServer.isGamingOptimized) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(CyberCyan.copy(alpha = 0.15f))
                                        .padding(horizontal = 5.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "TURBO",
                                        color = CyberCyan,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${uiState.selectedServer.city}, ${uiState.selectedServer.country} • ${uiState.selectedProtocol.displayName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${uiState.selectedServer.currentPingMs} ms",
                            color = if (uiState.selectedServer.currentPingMs < 25) NeonGreen else CyberCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "CHANGE",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Change Server",
                        tint = TextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Phone DNS Card (Custom & Any DNS Selection - Prominent DNS Changer)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, CyberCyan.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                .testTag("phone_dns_card"),
            colors = CardDefaults.cardColors(containerColor = SurfaceCardBg)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(CyberCyan.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Dns,
                                contentDescription = "DNS",
                                tint = CyberCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "PHONE DNS RESOLVER",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimary,
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(NeonGreen.copy(alpha = 0.18f))
                                        .padding(horizontal = 5.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "ডিএনএস পরিবর্তন",
                                        color = NeonGreen,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            val dnsIps = if (uiState.selectedDnsOption.isCustom) {
                                "${uiState.customPrimaryDns} • ${uiState.customSecondaryDns}"
                            } else {
                                "${uiState.selectedDnsOption.primaryIp} • ${uiState.selectedDnsOption.secondaryIp}"
                            }
                            Text(
                                text = "Active: ${uiState.selectedDnsOption.name} ($dnsIps)",
                                style = MaterialTheme.typography.bodySmall,
                                color = CyberCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceElevated)
                            .border(1.dp, BorderSubtle, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "ACTIVE",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = NeonGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Quick DNS Selector Horizontal Chips
                Text(
                    text = "QUICK SWITCH DNS (এক ক্লিকে ডিএনএস পরিবর্তন):",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(uiState.presetDnsList) { dns ->
                        val isSelected = uiState.selectedDnsOption.id == dns.id
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) CyberCyan.copy(alpha = 0.22f) else SurfaceElevated)
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) CyberCyan else BorderSubtle,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    if (dns.isCustom) {
                                        showDnsSheet = true
                                    } else {
                                        onSelectDns(dns)
                                    }
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = NeonGreen,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(
                                    text = when {
                                        dns.id.contains("cf") -> "⚡ Cloudflare"
                                        dns.id.contains("gg") -> "🌐 Google"
                                        dns.id.contains("ag") -> "🛡️ AdGuard"
                                        dns.id.contains("q9") -> "🔒 Quad9"
                                        dns.id.contains("od") -> "⚡ OpenDNS"
                                        else -> "✏️ Custom DNS"
                                    },
                                    color = if (isSelected) CyberCyan else TextPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Prominent Change DNS Button
                Button(
                    onClick = { showDnsSheet = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("change_dns_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyberCyan.copy(alpha = 0.15f),
                        contentColor = CyberCyan
                    ),
                    border = BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Change DNS",
                        modifier = Modifier.size(16.dp),
                        tint = CyberCyan
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "CHANGE / CONFIGURE DNS (ডিএনএস পরিবর্তন করুন)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = CyberCyan
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Gaming Turbo Mode Toggle Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(
                    1.dp,
                    if (uiState.isGamingMode) CyberCyan.copy(alpha = 0.35f) else BorderSubtle,
                    RoundedCornerShape(16.dp)
                )
                .testTag("gaming_mode_card"),
            colors = CardDefaults.cardColors(
                containerColor = if (uiState.isGamingMode) SurfaceElevated else SurfaceCardBg
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (uiState.isGamingMode) CyberCyan.copy(alpha = 0.2f) else SurfaceElevated
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SportsEsports,
                            contentDescription = "Gaming Mode",
                            tint = if (uiState.isGamingMode) CyberCyan else TextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Gaming Turbo Ping",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Tuned 1420 MTU & fast packet pacing",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Switch(
                    checked = uiState.isGamingMode,
                    onCheckedChange = onGamingModeChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF00382E),
                        checkedTrackColor = CyberCyan,
                        uncheckedThumbColor = TextMuted,
                        uncheckedTrackColor = SurfaceElevated
                    ),
                    modifier = Modifier.testTag("gaming_mode_switch")
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Real-Time Telemetry Metrics Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TelemetryMetricCard(
                modifier = Modifier.weight(1f),
                title = "LIVE PING",
                value = if (isConnected) "${uiState.stats.currentPingMs} ms" else "${uiState.selectedServer.currentPingMs} ms",
                subtitle = if (isConnected) "ULTRA FAST" else "STANDBY",
                color = if (isConnected) NeonGreen else CyberCyan,
                icon = Icons.Default.Speed
            )
            TelemetryMetricCard(
                modifier = Modifier.weight(1f),
                title = "JITTER",
                value = if (isConnected) "±${uiState.stats.jitterMs} ms" else "±0.0 ms",
                subtitle = "ZERO SPIKES",
                color = ElectricBlue,
                icon = Icons.Default.Wifi
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val downMB = String.format(Locale.US, "%.1f MB/s", uiState.stats.downloadKbps / 1024f)
            val upMB = String.format(Locale.US, "%.1f MB/s", uiState.stats.uploadKbps / 1024f)

            TelemetryMetricCard(
                modifier = Modifier.weight(1f),
                title = "DOWNLOAD",
                value = if (isConnected) downMB else "0.0 MB/s",
                subtitle = "BANDWIDTH",
                color = CyberCyan,
                icon = Icons.Default.ArrowDownward
            )
            TelemetryMetricCard(
                modifier = Modifier.weight(1f),
                title = "UPLOAD",
                value = if (isConnected) upMB else "0.0 MB/s",
                subtitle = "BANDWIDTH",
                color = NeonGreen,
                icon = Icons.Default.ArrowUpward
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Live Ping Waveform Sparkline
        PingWaveformChart(
            pingHistory = uiState.pingHistory,
            currentPing = uiState.stats.currentPingMs,
            isConnected = isConnected
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Connection detail pills
        if (isConnected) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCardBg)
                    .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Duration",
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    val minutes = uiState.stats.connectedSeconds / 60
                    val seconds = uiState.stats.connectedSeconds % 60
                    Text(
                        text = String.format(Locale.US, "Active: %02d:%02d", minutes, seconds),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Encryption",
                        tint = NeonGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ChaCha20 Shield",
                        style = MaterialTheme.typography.bodySmall,
                        color = NeonGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Professional Creator & Developer Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .clickable { onOpenDeveloperSystem() }
                .border(1.dp, CyberCyan.copy(alpha = 0.35f), RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = SurfaceElevated)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(CyberCyan.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = CyberCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Developer: Md:Ikbal khan",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(NeonGreen.copy(alpha = 0.15f))
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "VERIFIED",
                                    color = NeonGreen,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                        Text(
                            text = "Telegram: @Tamim238966 • Tap to view developer information",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    }
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "View Profile",
                    tint = TextSecondary,
                    modifier = Modifier.size(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }

    if (showDnsSheet) {
        DnsSelectorBottomSheet(
            selectedDns = uiState.selectedDnsOption,
            presetList = uiState.presetDnsList,
            customPrimary = uiState.customPrimaryDns,
            customSecondary = uiState.customSecondaryDns,
            onSelectDns = {
                onSelectDns(it)
                if (!it.isCustom) showDnsSheet = false
            },
            onSaveCustomDns = { p, s ->
                onSaveCustomDns(p, s)
                showDnsSheet = false
            },
            onDismiss = { showDnsSheet = false }
        )
    }
}

@Composable
private fun TelemetryMetricCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceCardBg)
            .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 17.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = color,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
