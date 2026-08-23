/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  org.slf4j.Logger
 */
package net.minecraft.locale;

import com.google.gson.JsonElement;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import net.minecraft.locale.Language;
import net.minecraft.util.StrictJsonParser;
import org.slf4j.Logger;

public record DeprecatedTranslationsInfo(List<String> removed, Map<String, String> renamed) {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeprecatedTranslationsInfo EMPTY = new DeprecatedTranslationsInfo(List.of(), Map.of());
    public static final Codec<DeprecatedTranslationsInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)Codec.STRING.listOf().fieldOf("removed")).forGetter(DeprecatedTranslationsInfo::removed), ((MapCodec)Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("renamed")).forGetter(DeprecatedTranslationsInfo::renamed)).apply((Applicative<DeprecatedTranslationsInfo, ?>)instance, DeprecatedTranslationsInfo::new));

    public static DeprecatedTranslationsInfo loadFromJson(InputStream inputStream) {
        JsonElement jsonElement = StrictJsonParser.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        return (DeprecatedTranslationsInfo)CODEC.parse(JsonOps.INSTANCE, jsonElement).getOrThrow(string -> new IllegalStateException("Failed to parse deprecated language data: " + string));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static DeprecatedTranslationsInfo loadFromResource(String string) {
        try (InputStream inputStream = Language.class.getResourceAsStream(string);){
            if (inputStream == null) return EMPTY;
            DeprecatedTranslationsInfo deprecatedTranslationsInfo = DeprecatedTranslationsInfo.loadFromJson(inputStream);
            return deprecatedTranslationsInfo;
        }
        catch (Exception exception) {
            LOGGER.error("Failed to read {}", (Object)string, (Object)exception);
        }
        return EMPTY;
    }

    public static DeprecatedTranslationsInfo loadFromDefaultResource() {
        return DeprecatedTranslationsInfo.loadFromResource("/assets/minecraft/lang/deprecated.json");
    }

    public void applyToMap(Map<String, String> map) {
        for (String string3 : this.removed) {
            map.remove(string3);
        }
        this.renamed.forEach((string, string2) -> {
            String string3 = (String)map.remove(string);
            if (string3 == null) {
                LOGGER.warn("Missing translation key for rename: {}", string);
                map.remove(string2);
            } else {
                map.put((String)string2, string3);
            }
        });
    }
}

