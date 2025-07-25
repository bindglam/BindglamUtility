package com.bindglam.utility.managers;

import com.bindglam.utility.BindglamUtility;
import com.bindglam.utility.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class PlayerDataManager {
    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    public void init() {
        try {
            Connection connection = BindglamUtility.database().getConnection();
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS bu_playerdata(uuid VARCHAR(36), data JSON)");
            statement.close();
            BindglamUtility.database().evictConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Bukkit.getOnlinePlayers().forEach(this::load);
    }

    public void load(UUID uuid) {
        if(playerDataMap.containsKey(uuid))
            return;

        PlayerData playerData = new PlayerData(uuid);
        playerData.load();
        playerDataMap.put(uuid, playerData);
    }

    public void dispose(UUID uuid, boolean async) {
        if(!playerDataMap.containsKey(uuid))
            return;

        PlayerData playerData = playerDataMap.get(uuid);
        playerData.dispose(async);
        playerDataMap.remove(uuid);
    }

    public void load(Player player) {
        load(player.getUniqueId());
    }

    public void dispose(Player player, boolean async) {
        dispose(player.getUniqueId(), async);
    }

    public void disposeAll(boolean async) {
        Bukkit.getOnlinePlayers().forEach((player) -> dispose(player, async));
    }

    public @NotNull PlayerData getPlayerData(UUID uuid) {
        return Objects.requireNonNull(playerDataMap.get(uuid));
    }

    public @NotNull PlayerData getPlayerData(Player player) {
        return getPlayerData(player.getUniqueId());
    }
}
