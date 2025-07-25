package com.bindglam.utility.playerdata;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.bindglam.utility.BindglamUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerData {
    private final UUID uuid;

    private final Map<String, Object> variables = new HashMap<>();

    private final AtomicBoolean isLoading = new AtomicBoolean(true);

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public void load() {
        Bukkit.getAsyncScheduler().runDelayed(BindglamUtility.getInstance(), (task) -> {
            Player player = getPlayer();

            try {
                Connection connection = BindglamUtility.database().getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM rpg_playerdata WHERE uuid = ?");
                statement.setString(1, uuid.toString());
                ResultSet rs = statement.executeQuery();

                while (rs.next()) {
                    JSONObject dataJson = (JSONObject) JSON.parse(rs.getString("data"));

                    dataJson.forEach((name, valueJson) -> variables.put(name, VariableParser.parseFromJSON(valueJson)));
                }

                rs.close();
                statement.close();
                BindglamUtility.database().evictConnection(connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            isLoading.set(false);
        }, 20*50L, TimeUnit.MILLISECONDS);
    }

    private void dispose() {
        try {
            JSONObject dataJson = new JSONObject();

            variables.forEach((name, value) -> dataJson.put(name, VariableParser.parseToJSON(value)));

            Connection connection = BindglamUtility.database().getConnection();
            PreparedStatement statement1 = connection.prepareStatement("SELECT * FROM rpg_playerdata WHERE uuid = ?");
            statement1.setString(1, uuid.toString());
            ResultSet rs1 = statement1.executeQuery();

            if (rs1.next()) {
                PreparedStatement statement2 = connection.prepareStatement("UPDATE rpg_playerdata SET data = ? WHERE uuid = ?");
                statement2.setString(1, dataJson.toString());
                statement2.setString(2, uuid.toString());
                statement2.executeUpdate();
                statement2.close();
            } else {
                PreparedStatement statement2 = connection.prepareStatement("INSERT INTO rpg_playerdata(uuid, data) VALUES (?, ?)");
                statement2.setString(1, uuid.toString());
                statement2.setString(2, dataJson.toString());
                statement2.executeUpdate();
                statement2.close();
            }

            rs1.close();
            statement1.close();
            BindglamUtility.database().evictConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dispose(boolean async) {
        if(async)
            Bukkit.getAsyncScheduler().runNow(BindglamUtility.getInstance(), (task) -> dispose());
        else
            dispose();
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public <T> @Nullable T getVariable(String name) {
        return (T) variables.get(name);
    }

    public void setVariable(String name, @Nullable Object value) {
        variables.put(name, value);
    }

    public boolean hasVariable(String name) {
        return variables.containsKey(name);
    }

    public Map<String, Object> getVariables() {
        return Map.copyOf(variables);
    }

    public boolean isLoading() {
        return isLoading.get();
    }

    public @Nullable Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}
