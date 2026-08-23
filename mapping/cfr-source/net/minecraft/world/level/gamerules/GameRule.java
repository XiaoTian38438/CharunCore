/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.gamerules;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.Objects;
import java.util.function.ToIntFunction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
import net.minecraft.world.level.gamerules.GameRules;

public final class GameRule<T>
implements FeatureElement {
    private final GameRuleCategory category;
    private final GameRuleType gameRuleType;
    private final ArgumentType<T> argument;
    private final GameRules.VisitorCaller<T> visitorCaller;
    private final Codec<T> valueCodec;
    private final ToIntFunction<T> commandResultFunction;
    private final T defaultValue;
    private final FeatureFlagSet requiredFeatures;

    public GameRule(GameRuleCategory gameRuleCategory, GameRuleType gameRuleType, ArgumentType<T> argumentType, GameRules.VisitorCaller<T> visitorCaller, Codec<T> codec, ToIntFunction<T> toIntFunction, T t, FeatureFlagSet featureFlagSet) {
        this.category = gameRuleCategory;
        this.gameRuleType = gameRuleType;
        this.argument = argumentType;
        this.visitorCaller = visitorCaller;
        this.valueCodec = codec;
        this.commandResultFunction = toIntFunction;
        this.defaultValue = t;
        this.requiredFeatures = featureFlagSet;
    }

    public String toString() {
        return this.id();
    }

    public String id() {
        return this.getIdentifier().toShortString();
    }

    public Identifier getIdentifier() {
        return Objects.requireNonNull(BuiltInRegistries.GAME_RULE.getKey(this));
    }

    public String getDescriptionId() {
        return Util.makeDescriptionId("gamerule", this.getIdentifier());
    }

    public String serialize(T t) {
        return t.toString();
    }

    public DataResult<T> deserialize(String string) {
        try {
            StringReader stringReader = new StringReader(string);
            T t = this.argument.parse(stringReader);
            if (stringReader.canRead()) {
                return DataResult.error(() -> "Failed to deserialize; trailing characters", t);
            }
            return DataResult.success(t);
        }
        catch (CommandSyntaxException commandSyntaxException) {
            return DataResult.error(() -> "Failed to deserialize");
        }
    }

    public Class<T> valueClass() {
        return this.defaultValue.getClass();
    }

    public void callVisitor(GameRuleTypeVisitor gameRuleTypeVisitor) {
        this.visitorCaller.call(gameRuleTypeVisitor, this);
    }

    public int getCommandResult(T t) {
        return this.commandResultFunction.applyAsInt(t);
    }

    public GameRuleCategory category() {
        return this.category;
    }

    public GameRuleType gameRuleType() {
        return this.gameRuleType;
    }

    public ArgumentType<T> argument() {
        return this.argument;
    }

    public Codec<T> valueCodec() {
        return this.valueCodec;
    }

    public T defaultValue() {
        return this.defaultValue;
    }

    @Override
    public FeatureFlagSet requiredFeatures() {
        return this.requiredFeatures;
    }
}

