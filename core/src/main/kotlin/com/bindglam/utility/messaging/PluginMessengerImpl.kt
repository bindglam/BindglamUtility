package com.bindglam.utility.messaging

import com.google.common.io.ByteArrayDataInput
import com.google.common.io.ByteStreams
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.messaging.PluginMessageListener
import java.util.*
import java.util.function.Consumer

class PluginMessengerImpl(private val plugin: Plugin) : PluginMessenger, PluginMessageListener {
    private val queue: MutableList<Packet> = LinkedList()

    override fun send(cmd: String, args: List<String>, consumer: Consumer<ByteArrayDataInput>) {
        val out = ByteStreams.newDataOutput()
        out.writeUTF(cmd)
        args.forEach { s -> out.writeUTF(s) }

        Bukkit.getServer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray())

        queue.add(Packet(cmd, consumer))
    }

    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        if (channel != "BungeeCord") return

        val `in` = ByteStreams.newDataInput(message)
        val subChannel = `in`.readUTF()

        val iterator: Iterator<Packet> = queue.iterator()
        var i = 0
        while (iterator.hasNext()) {
            val msgData = iterator.next()

            if (msgData.cmd == subChannel) {
                queue.removeAt(i)
                msgData.consumer.accept(`in`)
                break
            }
            i++
        }
    }

    private data class Packet(val cmd: String, val consumer: Consumer<ByteArrayDataInput>)
}