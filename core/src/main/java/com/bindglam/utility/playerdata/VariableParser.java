package com.bindglam.utility.playerdata;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class VariableParser {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Object parseFromJSON(Object value) {
        if(value instanceof JSONObject valueJson) {
            return switch ((String) valueJson.get("__type__")) {
                case "itemstack" -> parseItemStackFromJSON(valueJson);
                case "date" -> parseDateFromJSON(valueJson);
                case null -> {
                    Map<Object, Object> result = new HashMap<>();

                    valueJson.forEach((name, valueJsonValue) -> result.put(name, parseFromJSON(valueJsonValue)));

                    yield result;
                }
                default -> throw new IllegalStateException("Unexpected value: " + valueJson.get("__type__"));
            };
        } else if(value instanceof JSONArray valueJson) {
            List<Object> result = new ArrayList<>();

            valueJson.forEach((valueJsonValue) -> result.add(parseFromJSON(valueJsonValue)));

            return result;
        }
        return value;
    }

    public static Object parseToJSON(Object value) {
        if(value instanceof ItemStack) {
            return parseItemStackToJSON((ItemStack) value);
        } else if(value instanceof LocalDateTime) {
            return parseDateToJSON((LocalDateTime) value);
        } else if(value instanceof Map<?, ?> map) {
            JSONObject json = new JSONObject();

            map.forEach((mapName, mapValue) -> json.put((String) mapName, parseToJSON(mapValue)));

            return json;
        } else if(value instanceof List<?> list) {
            JSONArray json = new JSONArray();

            list.forEach((mapValue) -> json.add(parseToJSON(mapValue)));

            return json;
        }

        //if(value == null)
        //    return new JSONObject();
        return value;
    }


    private static ItemStack parseItemStackFromJSON(JSONObject valueJson) {
        return ItemStack.deserializeBytes(Base64.getDecoder().decode((String) valueJson.get("data")));
    }

    private static JSONObject parseItemStackToJSON(ItemStack itemStack) {
        JSONObject json = new JSONObject();

        json.put("__type__", "itemstack");
        json.put("data", Base64.getEncoder().encodeToString(itemStack.serializeAsBytes()));

        return json;
    }

    private static LocalDateTime parseDateFromJSON(JSONObject valueJson) {
        return LocalDateTime.parse((String) valueJson.get("data"), DATE_TIME_FORMATTER);
    }

    private static JSONObject parseDateToJSON(LocalDateTime date) {
        JSONObject json = new JSONObject();

        json.put("__type__", "date");
        json.put("data", date.format(DATE_TIME_FORMATTER));

        return json;
    }
}
