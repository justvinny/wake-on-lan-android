package com.example.wake_on_lan_android

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

private const val BROADCAST_ADDRESS = "255.255.255.255"
private const val PORT = 9
private const val RADIX = 16
private const val ARRAY_SIZE = 6
private const val INET_ADDRESS_OCTET_BYTE = 0xFF.toByte()
private val INET_ADDRESS = InetAddress.getByName(BROADCAST_ADDRESS)

suspend fun sendMagicPacket(macAddress: String) {
    withContext(Dispatchers.IO) {
        val packetData = macAddress.split(":")
            .map { it.toInt(RADIX).toByte() }
            .toByteArray()
            .toPacketData()
        val packet = DatagramPacket(packetData, packetData.size, INET_ADDRESS, PORT)
        val socket = DatagramSocket()
        socket.send(packet)
        socket.close()
    }
}

private fun ByteArray.toPacketData(): ByteArray {
    val bytes = ByteArray(ARRAY_SIZE + RADIX * this.size)
    for (i in 0 until ARRAY_SIZE) {
        bytes[i] = INET_ADDRESS_OCTET_BYTE
    }
    for (i in ARRAY_SIZE until bytes.size) {
        bytes[i] = this[i % this.size]
    }
    return bytes
}

private const val MAC_ADDRESS_REGEX_PATTERN = "^([\\dA-Fa-f]{2}:){5}([\\dA-Fa-f]{2})$"

fun isValidMacAddress(macAddress: String): Boolean {
    return macAddress.matches(MAC_ADDRESS_REGEX_PATTERN.toRegex())
}
