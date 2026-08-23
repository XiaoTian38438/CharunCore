/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.jsonrpc.methods;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
import net.minecraft.server.jsonrpc.methods.ClientInfo;
import net.minecraft.server.jsonrpc.methods.InvalidParameterJsonRpcException;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleType;

public class GameRulesService {
    public static List<GameRuleUpdate<?>> get(MinecraftApi minecraftApi) {
        ArrayList arrayList = new ArrayList();
        minecraftApi.gameRuleService().getAvailableGameRules().forEach(gameRule -> GameRulesService.addGameRule(minecraftApi, gameRule, arrayList));
        return arrayList;
    }

    private static <T> void addGameRule(MinecraftApi minecraftApi, GameRule<T> gameRule, List<GameRuleUpdate<?>> list) {
        T t = minecraftApi.gameRuleService().getRuleValue(gameRule);
        list.add(GameRulesService.getTypedRule(minecraftApi, gameRule, Objects.requireNonNull(t)));
    }

    public static <T> GameRuleUpdate<T> getTypedRule(MinecraftApi minecraftApi, GameRule<T> gameRule, T t) {
        return minecraftApi.gameRuleService().getTypedRule(gameRule, t);
    }

    public static <T> GameRuleUpdate<T> update(MinecraftApi minecraftApi, GameRuleUpdate<T> gameRuleUpdate, ClientInfo clientInfo) {
        return minecraftApi.gameRuleService().updateGameRule(gameRuleUpdate, clientInfo);
    }

    public record GameRuleUpdate<T>(GameRule<T> gameRule, T value) {
        public static final Codec<GameRuleUpdate<?>> TYPED_CODEC = BuiltInRegistries.GAME_RULE.byNameCodec().dispatch("key", GameRuleUpdate::gameRule, GameRuleUpdate::getValueAndTypeCodec);
        public static final Codec<GameRuleUpdate<?>> CODEC = BuiltInRegistries.GAME_RULE.byNameCodec().dispatch("key", GameRuleUpdate::gameRule, GameRuleUpdate::getValueCodec);

        private static <T> MapCodec<? extends GameRuleUpdate<T>> getValueCodec(GameRule<T> gameRule) {
            return ((MapCodec)gameRule.valueCodec().fieldOf("value")).xmap(object -> new GameRuleUpdate<Object>(gameRule, object), GameRuleUpdate::value);
        }

        private static <T> MapCodec<? extends GameRuleUpdate<T>> getValueAndTypeCodec(GameRule<T> gameRule) {
            return RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)StringRepresentable.fromEnum(GameRuleType::values).fieldOf("type")).forGetter(gameRuleUpdate -> gameRuleUpdate.gameRule.gameRuleType()), ((MapCodec)gameRule.valueCodec().fieldOf("value")).forGetter(GameRuleUpdate::value)).apply((Applicative<GameRuleUpdate, ?>)instance, (gameRuleType, object) -> GameRuleUpdate.getUntypedRule(gameRule, gameRuleType, object)));
        }

        private static <T> GameRuleUpdate<T> getUntypedRule(GameRule<T> gameRule, GameRuleType gameRuleType, T t) {
            if (gameRule.gameRuleType() != gameRuleType) {
                throw new InvalidParameterJsonRpcException("Stated type \"" + String.valueOf(gameRuleType) + "\" mismatches with actual type \"" + String.valueOf(gameRule.gameRuleType()) + "\" of gamerule \"" + gameRule.id() + "\"");
            }
            return new GameRuleUpdate<T>(gameRule, t);
        }
    }
}

