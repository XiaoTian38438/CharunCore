/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.advancements;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.criterion.CriterionValidator;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.HolderGetter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;

public record Advancement(Optional<Identifier> parent, Optional<DisplayInfo> display, AdvancementRewards rewards, Map<String, Criterion<?>> criteria, AdvancementRequirements requirements, boolean sendsTelemetryEvent, Optional<Component> name) {
    private static final Codec<Map<String, Criterion<?>>> CRITERIA_CODEC = Codec.unboundedMap(Codec.STRING, Criterion.CODEC).validate(map -> map.isEmpty() ? DataResult.error(() -> "Advancement criteria cannot be empty") : DataResult.success(map));
    public static final Codec<Advancement> CODEC = RecordCodecBuilder.create(instance -> instance.group(Identifier.CODEC.optionalFieldOf("parent").forGetter(Advancement::parent), DisplayInfo.CODEC.optionalFieldOf("display").forGetter(Advancement::display), AdvancementRewards.CODEC.optionalFieldOf("rewards", AdvancementRewards.EMPTY).forGetter(Advancement::rewards), ((MapCodec)CRITERIA_CODEC.fieldOf("criteria")).forGetter(Advancement::criteria), AdvancementRequirements.CODEC.optionalFieldOf("requirements").forGetter(advancement -> Optional.of(advancement.requirements())), Codec.BOOL.optionalFieldOf("sends_telemetry_event", false).forGetter(Advancement::sendsTelemetryEvent)).apply((Applicative<Advancement, ?>)instance, (optional, optional2, advancementRewards, map, optional3, bl) -> {
        AdvancementRequirements advancementRequirements = optional3.orElseGet(() -> AdvancementRequirements.allOf(map.keySet()));
        return new Advancement((Optional<Identifier>)optional, (Optional<DisplayInfo>)optional2, (AdvancementRewards)advancementRewards, (Map<String, Criterion<?>>)map, advancementRequirements, (boolean)bl);
    })).validate(Advancement::validate);
    public static final StreamCodec<RegistryFriendlyByteBuf, Advancement> STREAM_CODEC = StreamCodec.ofMember(Advancement::write, Advancement::read);

    public Advancement(Optional<Identifier> optional, Optional<DisplayInfo> optional2, AdvancementRewards advancementRewards, Map<String, Criterion<?>> map, AdvancementRequirements advancementRequirements, boolean bl) {
        this(optional, optional2, advancementRewards, Map.copyOf(map), advancementRequirements, bl, optional2.map(Advancement::decorateName));
    }

    private static DataResult<Advancement> validate(Advancement advancement) {
        return advancement.requirements().validate(advancement.criteria().keySet()).map(advancementRequirements -> advancement);
    }

    private static Component decorateName(DisplayInfo displayInfo) {
        Component component = displayInfo.getTitle();
        ChatFormatting chatFormatting = displayInfo.getType().getChatColor();
        MutableComponent mutableComponent = ComponentUtils.mergeStyles(component.copy(), Style.EMPTY.withColor(chatFormatting)).append("\n").append(displayInfo.getDescription());
        MutableComponent mutableComponent2 = component.copy().withStyle(style -> style.withHoverEvent(new HoverEvent.ShowText(mutableComponent)));
        return ComponentUtils.wrapInSquareBrackets(mutableComponent2).withStyle(chatFormatting);
    }

    public static Component name(AdvancementHolder advancementHolder) {
        return advancementHolder.value().name().orElseGet(() -> Component.literal(advancementHolder.id().toString()));
    }

    private void write(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        registryFriendlyByteBuf.writeOptional(this.parent, FriendlyByteBuf::writeIdentifier);
        DisplayInfo.STREAM_CODEC.apply(ByteBufCodecs::optional).encode(registryFriendlyByteBuf, this.display);
        this.requirements.write(registryFriendlyByteBuf);
        registryFriendlyByteBuf.writeBoolean(this.sendsTelemetryEvent);
    }

    private static Advancement read(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        return new Advancement(registryFriendlyByteBuf.readOptional(FriendlyByteBuf::readIdentifier), (Optional)DisplayInfo.STREAM_CODEC.apply(ByteBufCodecs::optional).decode(registryFriendlyByteBuf), AdvancementRewards.EMPTY, Map.of(), new AdvancementRequirements(registryFriendlyByteBuf), registryFriendlyByteBuf.readBoolean());
    }

    public boolean isRoot() {
        return this.parent.isEmpty();
    }

    public void validate(ProblemReporter problemReporter, HolderGetter.Provider provider) {
        this.criteria.forEach((string, criterion) -> {
            CriterionValidator criterionValidator = new CriterionValidator(problemReporter.forChild(new ProblemReporter.RootFieldPathElement((String)string)), provider);
            criterion.triggerInstance().validate(criterionValidator);
        });
    }

    public static class Builder {
        private Optional<Identifier> parent = Optional.empty();
        private Optional<DisplayInfo> display = Optional.empty();
        private AdvancementRewards rewards = AdvancementRewards.EMPTY;
        private final ImmutableMap.Builder<String, Criterion<?>> criteria = ImmutableMap.builder();
        private Optional<AdvancementRequirements> requirements = Optional.empty();
        private AdvancementRequirements.Strategy requirementsStrategy = AdvancementRequirements.Strategy.AND;
        private boolean sendsTelemetryEvent;

        public static Builder advancement() {
            return new Builder().sendsTelemetryEvent();
        }

        public static Builder recipeAdvancement() {
            return new Builder();
        }

        public Builder parent(AdvancementHolder advancementHolder) {
            this.parent = Optional.of(advancementHolder.id());
            return this;
        }

        @Deprecated(forRemoval=true)
        public Builder parent(Identifier identifier) {
            this.parent = Optional.of(identifier);
            return this;
        }

        public Builder display(ItemStack itemStack, Component component, Component component2, @Nullable Identifier identifier, AdvancementType advancementType, boolean bl, boolean bl2, boolean bl3) {
            return this.display(new DisplayInfo(itemStack, component, component2, Optional.ofNullable(identifier).map(ClientAsset.ResourceTexture::new), advancementType, bl, bl2, bl3));
        }

        public Builder display(ItemLike itemLike, Component component, Component component2, @Nullable Identifier identifier, AdvancementType advancementType, boolean bl, boolean bl2, boolean bl3) {
            return this.display(new DisplayInfo(new ItemStack(itemLike.asItem()), component, component2, Optional.ofNullable(identifier).map(ClientAsset.ResourceTexture::new), advancementType, bl, bl2, bl3));
        }

        public Builder display(DisplayInfo displayInfo) {
            this.display = Optional.of(displayInfo);
            return this;
        }

        public Builder rewards(AdvancementRewards.Builder builder) {
            return this.rewards(builder.build());
        }

        public Builder rewards(AdvancementRewards advancementRewards) {
            this.rewards = advancementRewards;
            return this;
        }

        public Builder addCriterion(String string, Criterion<?> criterion) {
            this.criteria.put((Object)string, criterion);
            return this;
        }

        public Builder requirements(AdvancementRequirements.Strategy strategy) {
            this.requirementsStrategy = strategy;
            return this;
        }

        public Builder requirements(AdvancementRequirements advancementRequirements) {
            this.requirements = Optional.of(advancementRequirements);
            return this;
        }

        public Builder sendsTelemetryEvent() {
            this.sendsTelemetryEvent = true;
            return this;
        }

        public AdvancementHolder build(Identifier identifier) {
            ImmutableMap immutableMap = this.criteria.buildOrThrow();
            AdvancementRequirements advancementRequirements = this.requirements.orElseGet(() -> this.lambda$build$0((Map)immutableMap));
            return new AdvancementHolder(identifier, new Advancement(this.parent, this.display, this.rewards, (Map<String, Criterion<?>>)immutableMap, advancementRequirements, this.sendsTelemetryEvent));
        }

        public AdvancementHolder save(Consumer<AdvancementHolder> consumer, String string) {
            AdvancementHolder advancementHolder = this.build(Identifier.parse(string));
            consumer.accept(advancementHolder);
            return advancementHolder;
        }

        private /* synthetic */ AdvancementRequirements lambda$build$0(Map map) {
            return this.requirementsStrategy.create(map.keySet());
        }
    }
}

