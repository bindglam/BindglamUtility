package com.bindglam.utility.pluginmessaging;

import com.bindglam.utility.BindglamUtility;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class PluginMessenger implements PluginMessageListener {
    private final List<MessageQueue> queue = new LinkedList<>();

    public void send(String cmd, List<String> args, Consumer<ByteArrayDataInput> consumer) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(cmd);
        args.forEach(out::writeUTF);

        BindglamUtility.getInstance().getServer().sendPluginMessage(BindglamUtility.getInstance(), "BungeeCord", out.toByteArray());

        queue.add(new MessageQueue(cmd, consumer));
    }

    public void send(String cmd, String... args) {
        send(cmd, Arrays.stream(args).toList(), (in) -> {});
    }

    public void send(String cmd, List<String> args) {
        send(cmd, args, (in) -> {});
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {
        if(!channel.equals("BungeeCord"))
            return;

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();

        Iterator<MessageQueue> iterator = queue.iterator();
        int i = 0;
        while(iterator.hasNext()) {
            MessageQueue msgData = iterator.next();

            if(msgData.cmd().equals(subChannel)) {
                msgData.consumer().accept(in);
                queue.remove(i);
                break;
            }
            i++;
        }
    }

    private record MessageQueue(String cmd, Consumer<ByteArrayDataInput> consumer) {
    }
}
