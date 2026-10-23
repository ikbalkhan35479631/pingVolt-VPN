package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ServerCategory
import com.example.model.VpnServer
import com.example.ui.VpnUiState
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
fun ServerListScreen(
    uiState: VpnUiState,
    onServerSelect: (VpnServer) -> Unit,
    onCategorySelect: (ServerCategory) -> Unit,
    onSearchChange: (String) -> Unit,
    onAutoPickFastest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DeepNavyBg)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // Title Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "SELECT NODE",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "${uiState.filteredServers.size} Active Nodes • BDIX & Global Gaming",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }

            // Quick Auto-Pick Button
            Button(
                onClick = onAutoPickFastest,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CyberCyan.copy(alpha = 0.15f),
                    contentColor = CyberCyan
                ),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.4f)),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.testTag("auto_pick_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "FASTEST",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Search bar
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Search location, country, or city…", color = TextMuted, fontSize = 13.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("server_search_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SurfaceCardBg,
                unfocusedContainerColor = SurfaceCardBg,
                focusedBorderColor = CyberCyan,
                unfocusedBorderColor = BorderSubtle,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Category Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(ServerCategory.values()) { category ->
                val isSelected = uiState.selectedCategory == category
                FilterChip(
                    selected = isSelected,
                    onClick = { onCategorySelect(category) },
                    label = {
                        Text(
                            text = category.label,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = SurfaceCardBg,
                        labelColor = TextSecondary,
                        selectedContainerColor = CyberCyan.copy(alpha = 0.2f),
                        selectedLabelColor = CyberCyan
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = BorderSubtle,
                        selectedBorderColor = CyberCyan,
                        enabled = true,
                        selected = isSelected
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.testTag("category_chip_${category.name}")
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Server List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .testTag("servers_list")
        ) {
            items(uiState.filteredServers, key = { it.id }) { server ->
                val isSelected = uiState.selectedServer.id == server.id
                ServerItemCard(
                    server = server,
                    isSelected = isSelected,
                    onClick = { onServerSelect(server) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun ServerItemCard(
    server: VpnServer,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .border(
                1.dp,
                if (isSelected) CyberCyan else BorderSubtle,
                RoundedCornerShape(16.dp)
            )
            .testTag("server_item_${server.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) SurfaceElevated else SurfaceCardBg
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = server.flagEmoji,
                        fontSize = 26.sp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = server.name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            if (server.isGamingOptimized) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.SportsEsports,
                                    contentDescription = "Gaming Optimized",
                                    tint = CyberCyan,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${server.city}, ${server.country}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Ping Badge
                    val pingColor = when {
                        server.currentPingMs < 25 -> NeonGreen
                        server.currentPingMs < 45 -> CyberCyan
                        else -> Color(0xFFFBBF24)
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(pingColor.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "${server.currentPingMs} ms",
                                color = pingColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Signal bars indicator (aligned to bottom so bars stand upright)
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            modifier = Modifier.height(15.dp)
                        ) {
                            SignalBar(height = 5, isActive = true, color = pingColor)
                            SignalBar(height = 8, isActive = true, color = pingColor)
                            SignalBar(height = 11, isActive = server.currentPingMs < 45, color = pingColor)
                            SignalBar(height = 15, isActive = server.currentPingMs < 25, color = pingColor)
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Selected",
                            tint = CyberCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Load indicator bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Server Load: ${server.serverLoad}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    fontSize = 10.sp
                )
                Text(
                    text = if (server.serverLoad < 50) "Optimum Bandwidth" else "Moderate Traffic",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (server.serverLoad < 50) NeonGreen else TextSecondary,
                    fontSize = 10.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { server.serverLoad / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = if (server.serverLoad < 50) CyberCyan else Color(0xFFF59E0B),
                trackColor = SurfaceElevated
            )
        }
    }
}

@Composable
private fun SignalBar(height: Int, isActive: Boolean, color: Color) {
    Box(
        modifier = Modifier
            .width(3.dp)
            .height(height.dp)
            .clip(RoundedCornerShape(1.dp))
            .background(if (isActive) color else TextMuted.copy(alpha = 0.3f))
    )
}
