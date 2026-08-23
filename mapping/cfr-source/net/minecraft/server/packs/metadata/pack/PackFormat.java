/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  org.jspecify.annotations.Nullable
 *  org.slf4j.Logger
 */
package net.minecraft.server.packs.metadata.pack;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.BiFunction;
import net.minecraft.server.packs.PackType;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.InclusiveRange;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public record PackFormat(int major, int minor) implements Comparable<PackFormat>
{
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final Codec<PackFormat> BOTTOM_CODEC = PackFormat.fullCodec(0);
    public static final Codec<PackFormat> TOP_CODEC = PackFormat.fullCodec(Integer.MAX_VALUE);

    private static Codec<PackFormat> fullCodec(int n) {
        return ExtraCodecs.compactListCodec(ExtraCodecs.NON_NEGATIVE_INT, ExtraCodecs.NON_NEGATIVE_INT.listOf(1, 256)).xmap(list -> list.size() > 1 ? PackFormat.of((Integer)list.getFirst(), (Integer)list.get(1)) : PackFormat.of((Integer)list.getFirst(), n), packFormat -> packFormat.minor != n ? List.of(Integer.valueOf(packFormat.major()), Integer.valueOf(packFormat.minor())) : List.of(Integer.valueOf(packFormat.major())));
    }

    public static <ResultType, HolderType extends IntermediaryFormatHolder> DataResult<List<ResultType>> validateHolderList(List<HolderType> list, int n, BiFunction<HolderType, InclusiveRange<PackFormat>, ResultType> biFunction) {
        int n2 = list.stream().map(IntermediaryFormatHolder::format).mapToInt(IntermediaryFormat::effectiveMinMajorVersion).min().orElse(Integer.MAX_VALUE);
        ArrayList<ResultType> arrayList = new ArrayList<ResultType>(list.size());
        for (IntermediaryFormatHolder intermediaryFormatHolder : list) {
            IntermediaryFormat intermediaryFormat = intermediaryFormatHolder.format();
            if (intermediaryFormat.min().isEmpty() && intermediaryFormat.max().isEmpty() && intermediaryFormat.supported().isEmpty()) {
                LOGGER.warn("Unknown or broken overlay entry {}", (Object)intermediaryFormatHolder);
                continue;
            }
            DataResult<InclusiveRange<PackFormat>> dataResult = intermediaryFormat.validate(n, false, n2 <= n, "Overlay \"" + String.valueOf(intermediaryFormatHolder) + "\"", "formats");
            if (dataResult.isSuccess()) {
                arrayList.add(biFunction.apply(intermediaryFormatHolder, dataResult.getOrThrow()));
                continue;
            }
            return DataResult.error(dataResult.error().get()::message);
        }
        return DataResult.success(List.copyOf(arrayList));
    }

    @VisibleForTesting
    public static int lastPreMinorVersion(PackType packType) {
        return switch (packType) {
            default -> throw new MatchException(null, null);
            case PackType.CLIENT_RESOURCES -> 64;
            case PackType.SERVER_DATA -> 81;
        };
    }

    public static MapCodec<InclusiveRange<PackFormat>> packCodec(PackType packType) {
        int n = PackFormat.lastPreMinorVersion(packType);
        return IntermediaryFormat.PACK_CODEC.flatXmap(intermediaryFormat -> intermediaryFormat.validate(n, true, false, "Pack", "supported_formats"), inclusiveRange -> DataResult.success(IntermediaryFormat.fromRange(inclusiveRange, n)));
    }

    public static PackFormat of(int n, int n2) {
        return new PackFormat(n, n2);
    }

    public static PackFormat of(int n) {
        return new PackFormat(n, 0);
    }

    public InclusiveRange<PackFormat> minorRange() {
        return new InclusiveRange<PackFormat>(this, PackFormat.of(this.major, Integer.MAX_VALUE));
    }

    @Override
    public int compareTo(PackFormat packFormat) {
        int n = Integer.compare(this.major(), packFormat.major());
        if (n != 0) {
            return n;
        }
        return Integer.compare(this.minor(), packFormat.minor());
    }

    @Override
    public String toString() {
        if (this.minor == Integer.MAX_VALUE) {
            return String.format(Locale.ROOT, "%d.*", this.major());
        }
        return String.format(Locale.ROOT, "%d.%d", this.major(), this.minor());
    }

    @Override
    public /* synthetic */ int compareTo(Object object) {
        return this.compareTo((PackFormat)object);
    }

    public static interface IntermediaryFormatHolder {
        public IntermediaryFormat format();
    }

    public record IntermediaryFormat(Optional<PackFormat> min, Optional<PackFormat> max, Optional<Integer> format, Optional<InclusiveRange<Integer>> supported) {
        static final MapCodec<IntermediaryFormat> PACK_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(BOTTOM_CODEC.optionalFieldOf("min_format").forGetter(IntermediaryFormat::min), TOP_CODEC.optionalFieldOf("max_format").forGetter(IntermediaryFormat::max), Codec.INT.optionalFieldOf("pack_format").forGetter(IntermediaryFormat::format), InclusiveRange.codec(Codec.INT).optionalFieldOf("supported_formats").forGetter(IntermediaryFormat::supported)).apply((Applicative<IntermediaryFormat, ?>)instance, IntermediaryFormat::new));
        public static final MapCodec<IntermediaryFormat> OVERLAY_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(BOTTOM_CODEC.optionalFieldOf("min_format").forGetter(IntermediaryFormat::min), TOP_CODEC.optionalFieldOf("max_format").forGetter(IntermediaryFormat::max), InclusiveRange.codec(Codec.INT).optionalFieldOf("formats").forGetter(IntermediaryFormat::supported)).apply((Applicative<IntermediaryFormat, ?>)instance, (optional, optional2, optional3) -> new IntermediaryFormat((Optional<PackFormat>)optional, (Optional<PackFormat>)optional2, optional.map(PackFormat::major), (Optional<InclusiveRange<Integer>>)optional3)));

        public static IntermediaryFormat fromRange(InclusiveRange<PackFormat> inclusiveRange, int n) {
            InclusiveRange<Integer> inclusiveRange2 = inclusiveRange.map(PackFormat::major);
            return new IntermediaryFormat(Optional.of(inclusiveRange.minInclusive()), Optional.of(inclusiveRange.maxInclusive()), inclusiveRange2.isValueInRange(n) ? Optional.of(inclusiveRange2.minInclusive()) : Optional.empty(), inclusiveRange2.isValueInRange(n) ? Optional.of(new InclusiveRange<Integer>(inclusiveRange2.minInclusive(), inclusiveRange2.maxInclusive())) : Optional.empty());
        }

        public int effectiveMinMajorVersion() {
            if (this.min.isPresent()) {
                if (this.supported.isPresent()) {
                    return Math.min(this.min.get().major(), this.supported.get().minInclusive());
                }
                return this.min.get().major();
            }
            if (this.supported.isPresent()) {
                return this.supported.get().minInclusive();
            }
            return Integer.MAX_VALUE;
        }

        public DataResult<InclusiveRange<PackFormat>> validate(int n, boolean bl, boolean bl2, String string, String string2) {
            if (this.min.isPresent() != this.max.isPresent()) {
                return DataResult.error(() -> string + " missing field, must declare both min_format and max_format");
            }
            if (bl2 && this.supported.isEmpty()) {
                return DataResult.error(() -> string + " missing required field " + string2 + ", must be present in all overlays for any overlays to work across game versions");
            }
            if (this.min.isPresent()) {
                return this.validateNewFormat(n, bl, bl2, string, string2);
            }
            if (this.supported.isPresent()) {
                return this.validateOldFormat(n, bl, string, string2);
            }
            if (bl && this.format.isPresent()) {
                int n2 = this.format.get();
                if (n2 > n) {
                    return DataResult.error(() -> string + " declares support for version newer than " + n + ", but is missing mandatory fields min_format and max_format");
                }
                return DataResult.success(new InclusiveRange<PackFormat>(PackFormat.of(n2)));
            }
            return DataResult.error(() -> string + " could not be parsed, missing format version information");
        }

        private DataResult<InclusiveRange<PackFormat>> validateNewFormat(int n, boolean bl, boolean bl2, String string, String string2) {
            int n2 = this.min.get().major();
            int n3 = this.max.get().major();
            if (this.min.get().compareTo(this.max.get()) > 0) {
                return DataResult.error(() -> string + " min_format (" + String.valueOf(this.min.get()) + ") is greater than max_format (" + String.valueOf(this.max.get()) + ")");
            }
            if (n2 > n && !bl2) {
                String string3;
                if (this.supported.isPresent()) {
                    return DataResult.error(() -> string + " key " + string2 + " is deprecated starting from pack format " + (n + 1) + ". Remove " + string2 + " from your pack.mcmeta.");
                }
                if (bl && this.format.isPresent() && (string3 = this.validatePackFormatForRange(n2, n3)) != null) {
                    return DataResult.error(() -> string3);
                }
            } else {
                Object object;
                if (this.supported.isPresent()) {
                    object = this.supported.get();
                    if (((InclusiveRange)object).minInclusive() != n2) {
                        return DataResult.error(() -> string + " version declaration mismatch between " + string2 + " (from " + String.valueOf(object.minInclusive()) + ") and min_format (" + String.valueOf(this.min.get()) + ")");
                    }
                    if (((InclusiveRange)object).maxInclusive() != n3 && ((InclusiveRange)object).maxInclusive() != n) {
                        return DataResult.error(() -> string + " version declaration mismatch between " + string2 + " (up to " + String.valueOf(object.maxInclusive()) + ") and max_format (" + String.valueOf(this.max.get()) + ")");
                    }
                } else {
                    return DataResult.error(() -> string + " declares support for format " + n2 + ", but game versions supporting formats 17 to " + n + " require a " + string2 + " field. Add \"" + string2 + "\": [" + n2 + ", " + n + "] or require a version greater or equal to " + (n + 1) + ".0.");
                }
                if (bl) {
                    if (this.format.isPresent()) {
                        object = this.validatePackFormatForRange(n2, n3);
                        if (object != null) {
                            return DataResult.error(() -> IntermediaryFormat.lambda$validateNewFormat$13((String)object));
                        }
                    } else {
                        return DataResult.error(() -> string + " declares support for formats up to " + n + ", but game versions supporting formats 17 to " + n + " require a pack_format field. Add \"pack_format\": " + n2 + " or require a version greater or equal to " + (n + 1) + ".0.");
                    }
                }
            }
            return DataResult.success(new InclusiveRange<PackFormat>(this.min.get(), this.max.get()));
        }

        private DataResult<InclusiveRange<PackFormat>> validateOldFormat(int n, boolean bl, String string, String string2) {
            InclusiveRange<Integer> inclusiveRange = this.supported.get();
            int n2 = inclusiveRange.minInclusive();
            int n3 = inclusiveRange.maxInclusive();
            if (n3 > n) {
                return DataResult.error(() -> string + " declares support for version newer than " + n + ", but is missing mandatory fields min_format and max_format");
            }
            if (bl) {
                if (this.format.isPresent()) {
                    String string3 = this.validatePackFormatForRange(n2, n3);
                    if (string3 != null) {
                        return DataResult.error(() -> string3);
                    }
                } else {
                    return DataResult.error(() -> string + " declares support for formats up to " + n + ", but game versions supporting formats 17 to " + n + " require a pack_format field. Add \"pack_format\": " + n2 + " or require a version greater or equal to " + (n + 1) + ".0.");
                }
            }
            return DataResult.success(new InclusiveRange<Integer>(n2, n3).map(PackFormat::of));
        }

        private @Nullable String validatePackFormatForRange(int n, int n2) {
            int n3 = this.format.get();
            if (n3 < n || n3 > n2) {
                return "Pack declared support for versions " + n + " to " + n2 + " but declared main format is " + n3;
            }
            if (n3 < 15) {
                return "Multi-version packs cannot support minimum version of less than 15, since this will leave versions in range unable to load pack.";
            }
            return null;
        }

        private static /* synthetic */ String lambda$validateNewFormat$13(String string) {
            return string;
        }
    }
}

