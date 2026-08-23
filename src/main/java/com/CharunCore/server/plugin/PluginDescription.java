package com.CharunCore.server.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record PluginDescription(
        String name,
        String version,
        String main,
        String apiVersion,
        List<String> authors,
        List<String> depends,
        List<String> softDepends,
        Map<String, Object> commandsSection,
        Map<String, Object> permissionsSection) {

    @SuppressWarnings("unchecked")
    public static PluginDescription fromYaml(Map<String, Object> yml) {
        String name = str(yml.get("name"));
        String version = str(yml.getOrDefault("version", "1.0"));
        String main = str(yml.get("main"));
        String api = str(yml.getOrDefault("api-version", "1.0"));
        if (name == null || main == null) {
            throw new IllegalArgumentException("plugin.yml 缺少 name 或 main");
        }
        List<String> authors = new ArrayList<>();
        Object authorField = yml.get("author");
        if (authorField != null) authors.add(str(authorField));
        Object authorsField = yml.get("authors");
        if (authorsField instanceof List<?> l) l.forEach(a -> authors.add(str(a)));
        List<String> depends = listOf(yml.get("depend"));
        List<String> soft = listOf(yml.get("softdepend"));
        Map<String, Object> commands = yml.get("commands") instanceof Map<?, ?> m
                ? (Map<String, Object>) m : Map.of();
        Map<String, Object> permissions = yml.get("permissions") instanceof Map<?, ?> p
                ? (Map<String, Object>) p : Map.of();
        return new PluginDescription(name, version, main, api, authors, depends, soft, commands, permissions);
    }

    private static String str(Object o) {
        return o == null ? null : o.toString();
    }

    private static List<String> listOf(Object o) {
        List<String> result = new ArrayList<>();
        if (o instanceof List<?> l) l.forEach(e -> result.add(String.valueOf(e)));
        return result;
    }
}
