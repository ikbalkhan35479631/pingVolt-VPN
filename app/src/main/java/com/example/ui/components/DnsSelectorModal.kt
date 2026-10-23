package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.model.DnsOption
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DnsSelectorBottomSheet(
    selectedDns: DnsOption,
    presetList: List<DnsOption>,
    customPrimary: String,
    customSecondary: String,
    onSelectDns: (DnsOption) -> Unit,
    onSaveCustomDns: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var inputPrimary by remember { mutableStateOf(customPrimary.ifEmpty { "1.1.1.1" }) }
    var inputSecondary by remember { mutableStateOf(customSecondary.ifEmpty { "1.0.0.1" }) }
    var showCustomInputs by remember { mutableStateOf(selectedDns.isCustom) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceElevated,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(CyberCyan.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Dns,
                            contentDescription = "DNS",
                            tint = CyberCyan,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "DNS CHANGER",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = TextPrimary,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(NeonGreen.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "ডিএনএস পরিবর্তন",
                                    color = NeonGreen,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Text(
                            text = "Select any DNS resolver or set custom IP on phone",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_dns_sheet_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Presets and Custom list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(presetList) { dns ->
                    val isSelected = selectedDns.id == dns.id
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) CyberCyan else BorderSubtle,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable {
                                onSelectDns(dns)
                                if (dns.isCustom) {
                                    showCustomInputs = true
                                } else {
                                    showCustomInputs = false
                                    onDismiss()
                                }
                            }
                            .testTag("dns_item_${dns.id}"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) SurfaceCardBg else DeepNavyBg
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
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
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isSelected) CyberCyan.copy(alpha = 0.2f) else SurfaceElevated
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = when {
                                                dns.isCustom -> Icons.Default.Edit
                                                dns.id.contains("ag") -> Icons.Default.Shield
                                                dns.id.contains("q9") -> Icons.Default.Security
                                                else -> Icons.Default.Speed
                                            },
                                            contentDescription = null,
                                            tint = if (isSelected) CyberCyan else TextMuted,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = dns.name,
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary
                                            )
                                            if (isSelected) {
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(4.dp))
                                                        .background(NeonGreen.copy(alpha = 0.2f))
                                                        .padding(horizontal = 6.dp, vertical = 1.dp)
                                                ) {
                                                    Text(
                                                        text = "ACTIVE",
                                                        color = NeonGreen,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Black
                                                    )
                                                }
                                            }
                                        }
                                        if (!dns.isCustom) {
                                            Text(
                                                text = "${dns.primaryIp} • ${dns.secondaryIp}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = if (isSelected) NeonGreen else TextSecondary,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        } else {
                                            Text(
                                                text = "Current: $inputPrimary • $inputSecondary",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = CyberCyan,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    val latencyTag = when {
                                        dns.id.contains("cf") -> "8 ms"
                                        dns.id.contains("gg") -> "14 ms"
                                        dns.id.contains("ag") -> "AD-BLOCK"
                                        dns.id.contains("q9") -> "SECURE"
                                        dns.id.contains("od") -> "16 ms"
                                        else -> "CUSTOM"
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(
                                                if (isSelected) CyberCyan.copy(alpha = 0.15f) else SurfaceCardBg
                                            )
                                            .border(
                                                0.5.dp,
                                                if (isSelected) CyberCyan else BorderSubtle,
                                                RoundedCornerShape(6.dp)
                                            )
                                            .padding(horizontal = 7.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = latencyTag,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (isSelected) CyberCyan else TextSecondary
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = NeonGreen,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = dns.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // If Custom is selected, show Primary & Secondary IP input boxes + presets
                if (showCustomInputs || selectedDns.isCustom) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .border(1.5.dp, CyberCyan, RoundedCornerShape(14.dp))
                                .padding(top = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceCardBg)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "ENTER CUSTOM DNS ADDRESSES (কাস্টম ডিএনএস)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CyberCyan,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "Tap any quick preset IP below or enter your own ISP DNS:",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Quick IP Fill Chips
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    val quickIps = listOf(
                                        Pair("Cloudflare", Pair("1.1.1.1", "1.0.0.1")),
                                        Pair("Google", Pair("8.8.8.8", "8.8.4.4")),
                                        Pair("AdGuard", Pair("94.140.14.14", "94.140.15.15")),
                                        Pair("Quad9", Pair("9.9.9.9", "149.112.112.112")),
                                        Pair("OpenDNS", Pair("208.67.222.222", "208.67.220.220"))
                                    )
                                    items(quickIps) { (name, ips) ->
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(SurfaceElevated)
                                                .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
                                                .clickable {
                                                    inputPrimary = ips.first
                                                    inputSecondary = ips.second
                                                }
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = "${ips.first} ($name)",
                                                fontSize = 10.sp,
                                                color = CyberCyan,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                OutlinedTextField(
                                    value = inputPrimary,
                                    onValueChange = { inputPrimary = it },
                                    label = { Text("Primary DNS IP (e.g. 1.1.1.1)") },
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("custom_primary_dns_input"),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = DeepNavyBg,
                                        unfocusedContainerColor = DeepNavyBg,
                                        focusedBorderColor = CyberCyan,
                                        unfocusedBorderColor = BorderSubtle,
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedTextField(
                                    value = inputSecondary,
                                    onValueChange = { inputSecondary = it },
                                    label = { Text("Secondary DNS IP (e.g. 8.8.8.8)") },
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("custom_secondary_dns_input"),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = DeepNavyBg,
                                        unfocusedContainerColor = DeepNavyBg,
                                        focusedBorderColor = CyberCyan,
                                        unfocusedBorderColor = BorderSubtle,
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                Button(
                                    onClick = {
                                        val p = inputPrimary.trim().ifEmpty { "1.1.1.1" }
                                        val s = inputSecondary.trim().ifEmpty { "8.8.8.8" }
                                        onSaveCustomDns(p, s)
                                        val customOpt = presetList.find { it.isCustom }
                                        if (customOpt != null) {
                                            onSelectDns(customOpt)
                                        }
                                        onDismiss()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("save_custom_dns_button"),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CyberCyan,
                                        contentColor = Color(0xFF00382E)
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = "APPLY DNS TO PHONE (ডিএনএস পরিবর্তন সেভ করুন)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
