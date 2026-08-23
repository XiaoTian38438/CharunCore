/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 *  org.slf4j.Logger
 */
package net.minecraft.network.chat.contents;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.data.DataSource;
import net.minecraft.network.chat.contents.data.DataSources;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class NbtContents
implements ComponentContents {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final MapCodec<NbtContents> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codec.STRING.fieldOf("nbt")).forGetter(NbtContents::getNbtPath), Codec.BOOL.lenientOptionalFieldOf("interpret", false).forGetter(NbtContents::isInterpreting), ComponentSerialization.CODEC.lenientOptionalFieldOf("separator").forGetter(NbtContents::getSeparator), DataSources.CODEC.forGetter(NbtContents::getDataSource)).apply((Applicative<NbtContents, ?>)instance, NbtContents::new));
    private final boolean interpreting;
    private final Optional<Component> separator;
    private final String nbtPathPattern;
    private final DataSource dataSource;
    protected final @Nullable NbtPathArgument.NbtPath compiledNbtPath;

    public NbtContents(String string, boolean bl, Optional<Component> optional, DataSource dataSource) {
        this(string, NbtContents.compileNbtPath(string), bl, optional, dataSource);
    }

    private NbtContents(String string, @Nullable NbtPathArgument.NbtPath nbtPath, boolean bl, Optional<Component> optional, DataSource dataSource) {
        this.nbtPathPattern = string;
        this.compiledNbtPath = nbtPath;
        this.interpreting = bl;
        this.separator = optional;
        this.dataSource = dataSource;
    }

    private static @Nullable NbtPathArgument.NbtPath compileNbtPath(String string) {
        try {
            return new NbtPathArgument().parse(new StringReader(string));
        }
        catch (CommandSyntaxException commandSyntaxException) {
            return null;
        }
    }

    public String getNbtPath() {
        return this.nbtPathPattern;
    }

    public boolean isInterpreting() {
        return this.interpreting;
    }

    public Optional<Component> getSeparator() {
        return this.separator;
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof NbtContents)) return false;
        NbtContents nbtContents = (NbtContents)object;
        if (!this.dataSource.equals(nbtContents.dataSource)) return false;
        if (!this.separator.equals(nbtContents.separator)) return false;
        if (this.interpreting != nbtContents.interpreting) return false;
        if (!this.nbtPathPattern.equals(nbtContents.nbtPathPattern)) return false;
        return true;
    }

    public int hashCode() {
        int n = this.interpreting ? 1 : 0;
        n = 31 * n + this.separator.hashCode();
        n = 31 * n + this.nbtPathPattern.hashCode();
        n = 31 * n + this.dataSource.hashCode();
        return n;
    }

    public String toString() {
        return "nbt{" + String.valueOf(this.dataSource) + ", interpreting=" + this.interpreting + ", separator=" + String.valueOf(this.separator) + "}";
    }

    @Override
    public MutableComponent resolve(@Nullable CommandSourceStack commandSourceStack, @Nullable Entity entity, int n) throws CommandSyntaxException {
        if (commandSourceStack == null || this.compiledNbtPath == null) {
            return Component.empty();
        }
        Stream<String> stream = this.dataSource.getData(commandSourceStack).flatMap(compoundTag -> {
            try {
                return this.compiledNbtPath.get((Tag)compoundTag).stream();
            }
            catch (CommandSyntaxException commandSyntaxException) {
                return Stream.empty();
            }
        });
        if (this.interpreting) {
            RegistryOps<Tag> registryOps = commandSourceStack.registryAccess().createSerializationContext(NbtOps.INSTANCE);
            Component component = DataFixUtils.orElse(ComponentUtils.updateForEntity(commandSourceStack, this.separator, entity, n), ComponentUtils.DEFAULT_NO_STYLE_SEPARATOR);
            return stream.flatMap(tag -> {
                try {
                    Component component = (Component)ComponentSerialization.CODEC.parse(registryOps, tag).getOrThrow();
                    return Stream.of(ComponentUtils.updateForEntity(commandSourceStack, component, entity, n));
                }
                catch (Exception exception) {
                    LOGGER.warn("Failed to parse component: {}", tag, (Object)exception);
                    return Stream.of(new MutableComponent[0]);
                }
            }).reduce((mutableComponent, mutableComponent2) -> mutableComponent.append(component).append((Component)mutableComponent2)).orElseGet(Component::empty);
        }
        Stream<String> stream2 = stream.map(NbtContents::asString);
        return ComponentUtils.updateForEntity(commandSourceStack, this.separator, entity, n).map(mutableComponent -> stream2.map(Component::literal).reduce((mutableComponent2, mutableComponent3) -> mutableComponent2.append((Component)mutableComponent).append((Component)mutableComponent3)).orElseGet(Component::empty)).orElseGet(() -> Component.literal(stream2.collect(Collectors.joining(", "))));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static String asString(Tag tag) {
        if (!(tag instanceof StringTag)) return tag.toString();
        StringTag stringTag = (StringTag)tag;
        try {
            String string = stringTag.value();
            return string;
        }
        catch (Throwable throwable) {
            throw new MatchException(throwable.toString(), throwable);
        }
    }

    public MapCodec<NbtContents> codec() {
        return MAP_CODEC;
    }
}

