/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 */
package net.minecraft.server.packs;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.regex.Pattern;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.metadata.pack.PackFormat;
import net.minecraft.util.InclusiveRange;

public record OverlayMetadataSection(List<OverlayEntry> overlays) {
    private static final Pattern DIR_VALIDATOR = Pattern.compile("[-_a-zA-Z0-9.]+");
    public static final MetadataSectionType<OverlayMetadataSection> CLIENT_TYPE = new MetadataSectionType<OverlayMetadataSection>("overlays", OverlayMetadataSection.codecForPackType(PackType.CLIENT_RESOURCES));
    public static final MetadataSectionType<OverlayMetadataSection> SERVER_TYPE = new MetadataSectionType<OverlayMetadataSection>("overlays", OverlayMetadataSection.codecForPackType(PackType.SERVER_DATA));

    private static DataResult<String> validateOverlayDir(String string) {
        if (!DIR_VALIDATOR.matcher(string).matches()) {
            return DataResult.error(() -> string + " is not accepted directory name");
        }
        return DataResult.success(string);
    }

    @VisibleForTesting
    public static Codec<OverlayMetadataSection> codecForPackType(PackType packType) {
        return RecordCodecBuilder.create(instance -> instance.group(((MapCodec)OverlayEntry.listCodecForPackType(packType).fieldOf("entries")).forGetter(OverlayMetadataSection::overlays)).apply((Applicative<OverlayMetadataSection, ?>)instance, OverlayMetadataSection::new));
    }

    public static MetadataSectionType<OverlayMetadataSection> forPackType(PackType packType) {
        return switch (packType) {
            default -> throw new MatchException(null, null);
            case PackType.CLIENT_RESOURCES -> CLIENT_TYPE;
            case PackType.SERVER_DATA -> SERVER_TYPE;
        };
    }

    public List<String> overlaysForVersion(PackFormat packFormat) {
        return this.overlays.stream().filter(overlayEntry -> overlayEntry.isApplicable(packFormat)).map(OverlayEntry::overlay).toList();
    }

    public record OverlayEntry(InclusiveRange<PackFormat> format, String overlay) {
        static Codec<List<OverlayEntry>> listCodecForPackType(PackType packType) {
            int n = PackFormat.lastPreMinorVersion(packType);
            return IntermediateEntry.CODEC.listOf().flatXmap(list -> PackFormat.validateHolderList(list, n, (intermediateEntry, inclusiveRange) -> new OverlayEntry((InclusiveRange<PackFormat>)inclusiveRange, intermediateEntry.overlay())), list -> DataResult.success(list.stream().map(overlayEntry -> new IntermediateEntry(PackFormat.IntermediaryFormat.fromRange(overlayEntry.format(), n), overlayEntry.overlay())).toList()));
        }

        public boolean isApplicable(PackFormat packFormat) {
            return this.format.isValueInRange(packFormat);
        }

        record IntermediateEntry(PackFormat.IntermediaryFormat format, String overlay) implements PackFormat.IntermediaryFormatHolder
        {
            static final Codec<IntermediateEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(PackFormat.IntermediaryFormat.OVERLAY_CODEC.forGetter(IntermediateEntry::format), ((MapCodec)Codec.STRING.validate(OverlayMetadataSection::validateOverlayDir).fieldOf("directory")).forGetter(IntermediateEntry::overlay)).apply((Applicative<IntermediateEntry, ?>)instance, IntermediateEntry::new));

            @Override
            public String toString() {
                return this.overlay;
            }
        }
    }
}

