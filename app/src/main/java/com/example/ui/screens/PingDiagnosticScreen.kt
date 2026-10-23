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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DiagnosticStatus
import com.example.model.DnsOption
import com.example.model.PingTarget
import com.example.model.VpnProtocol
import com.example.ui.VpnUiState
import com.example.ui.components.DnsSelectorBottomSheet
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

@Composable
fun PingDiagnosticScreen(
    uiState: VpnUiState,
    onRunDiagnostics: () -> Unit,
    onProtocolSelect: (VpnProtocol) -> Unit,
    onKillSwitchChange: (Boolean) -> Unit,
    onSelectDns: (DnsOption) -> Unit = {},
    onSaveCustomDns: (String, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var showDnsSheet by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DeepNavyBg)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "PING DIAGNOSTICS",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "Measure live latency & optimize protocols",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }

            Button(
                onClick = onRunDiagnostics,
                enabled = !uiState.isDiagnosing,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CyberCyan,
                    contentColor = Color(0xFF00382E)
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.testTag("run_diagnostics_button")
            ) {
                if (uiState.isDiagnosing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color(0xFF00382E),
                        strokeWidth = 2.dp
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Run",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "START TEST",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Targets Diagnostic Matrix
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            uiState.diagnosticTargets.forEach { target ->
                DiagnosticTargetItem(target = target)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // DEDICATED DNS CHANGER SECTION
        Text(
            text = "PHONE DNS ROUTER & RESOLVER (ডিএনএস পরিবর্তন)",
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, CyberCyan.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                .testTag("diagnostic_dns_card"),
            colors = CardDefaults.cardColors(containerColor = SurfaceCardBg)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
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
                                .size(36.dp)
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
                                    text = uiState.selectedDnsOption.name,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(NeonGreen.copy(alpha = 0.2f))
                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "ACTIVE",
                                        color = NeonGreen,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                            val dnsIps = if (uiState.selectedDnsOption.isCustom) {
                                "${uiState.customPrimaryDns} • ${uiState.customSecondaryDns}"
                            } else {
                                "${uiState.selectedDnsOption.primaryIp} • ${uiState.selectedDnsOption.secondaryIp}"
                            }
                            Text(
                                text = "IP: $dnsIps",
                                style = MaterialTheme.typography.bodySmall,
                                color = CyberCyan,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceElevated)
                            .border(1.dp, BorderSubtle, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "CUSTOMIZABLE",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // One-tap quick switch DNS pills
                Text(
                    text = "SELECT ANY DNS SERVER (যেকোনো DNS বেছে নিন):",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
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
                                        else -> "✏️ Custom IP"
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

                Button(
                    onClick = { showDnsSheet = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("diagnostic_change_dns_button"),
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
                        text = "CHANGE DNS / কাস্টম ডিএনএস বসান",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = CyberCyan
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Protocol Selector Card
        Text(
            text = "TUNNEL PROTOCOL (LATENCY PROFILE)",
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            VpnProtocol.values().forEach { protocol ->
                val isSelected = uiState.selectedProtocol == protocol
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onProtocolSelect(protocol) }
                        .border(
                            1.dp,
                            if (isSelected) CyberCyan else BorderSubtle,
                            RoundedCornerShape(14.dp)
                        )
                        .testTag("protocol_${protocol.name}"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) SurfaceElevated else SurfaceCardBg
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { onProtocolSelect(protocol) },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = CyberCyan,
                                    unselectedColor = TextMuted
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = protocol.displayName,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                if (isSelected) CyberCyan.copy(alpha = 0.2f) else SurfaceCardBg
                                            )
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = protocol.badge,
                                            color = if (isSelected) CyberCyan else TextSecondary,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }
                                Text(
                                    text = protocol.subtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Advanced Network Tweaks
        Text(
            text = "PING & SECURITY OPTIMIZATIONS",
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = SurfaceCardBg)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Kill-Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Kill Switch",
                            tint = CyberCyan,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Kill-Switch (Zero Leak)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Cut internet if VPN unexpectedly drops",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                    Switch(
                        checked = uiState.isKillSwitch,
                        onCheckedChange = onKillSwitchChange,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF00382E),
                            checkedTrackColor = CyberCyan,
                            uncheckedThumbColor = TextMuted,
                            uncheckedTrackColor = SurfaceElevated
                        ),
                        modifier = Modifier.testTag("kill_switch_toggle")
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Gaming MTU Optimization Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "MTU Tuning",
                            tint = NeonGreen,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "1420 Dynamic MTU Pacing",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Eliminates IP packet fragmentation for smooth gaming",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
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
private fun DiagnosticTargetItem(target: PingTarget) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = SurfaceCardBg)
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
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceElevated),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (target.category) {
                            "Gaming" -> Icons.Default.SportsEsports
                            "Voice" -> Icons.Default.GraphicEq
                            "DNS" -> Icons.Default.Dns
                            else -> Icons.Default.Cloud
                        },
                        contentDescription = target.name,
                        tint = CyberCyan,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = target.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "${target.host}:${target.port} • ${target.category}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            // Ping Result / Status
            when (target.status) {
                DiagnosticStatus.TESTING -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = CyberCyan,
                        strokeWidth = 2.dp
                    )
                }
                DiagnosticStatus.SUCCESS -> {
                    val ping = target.pingMs ?: 18
                    val pingColor = when {
                        ping < 25 -> NeonGreen
                        ping < 45 -> CyberCyan
                        else -> Color(0xFFFBBF24)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "$ping ms",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = pingColor
                        )
                        target.jitterMs?.let { jitter ->
                            Text(
                                text = "±$jitter ms jitter",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted,
                                fontSize = 9.sp
                            )
                        }
                    }
                }
                else -> {
                    Text(
                        text = "READY",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
