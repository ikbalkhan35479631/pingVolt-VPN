package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.VpnRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("PingVolt VPN", appName)
  }

  @Test
  fun `verify Bangladesh servers exist and are default`() {
    val repo = VpnRepository()
    val servers = repo.servers.value
    val bdServers = servers.filter { it.countryCode == "BD" }
    assertTrue(bdServers.isNotEmpty())
    assertEquals("Dhaka BDIX Express", repo.selectedServer.value.name)
    assertEquals(8, repo.selectedServer.value.currentPingMs)
  }

  @Test
  fun `verify over 50 servers exist`() {
    val repo = VpnRepository()
    assertTrue(repo.servers.value.size >= 50)
  }

  @Test
  fun `verify custom DNS configuration`() {
    val repo = VpnRepository()
    repo.setCustomDns("94.140.14.14", "94.140.15.15")
    val customDns = repo.presetDnsOptions.first { it.isCustom }
    repo.setDnsOption(customDns)
    val (primary, secondary) = repo.getEffectiveDnsIps()
    assertEquals("94.140.14.14", primary)
    assertEquals("94.140.15.15", secondary)
  }

  @Test
  fun `verify Telegram developer profile avatar configuration`() {
    val repo = VpnRepository()
    assertEquals("Tamim238966", VpnRepository.TELEGRAM_USERNAME)
    assertTrue(repo.developerAvatarUrl.value.contains("telesco.pe") || repo.developerAvatarUrl.value.contains("telegram"))
  }
}
