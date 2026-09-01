package com.CharunCore.server.network.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import org.cloudburstmc.nbt.NBTOutputStream;
import org.cloudburstmc.nbt.NbtMap;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PacketBuffer {
    private final ByteBuf buffer;

    public PacketBuffer(ByteBuf buffer) { this.buffer = buffer; }

    // ─────────────────────────────────────────────────────────────────────────
    // NBT WRITING
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Named NBT: writes byte(10) + short(0) + compound_payload + TAG_End.
     * writeValue(tag, 10) already writes the compound entries AND TAG_End,
     * so we don't add another byte(0) after it.
     *
     * Used for protocol fields that expect "full NBT" with type+name prefix.
     * Note: chunk heightmaps in 1.21.4+ use a different format (see ChunkEncoder).
     */
    public void writeNbt(NbtMap tag) {
        if (tag == null) { buffer.writeByte(0); return; }
        try {
            buffer.writeByte(10);
            //buffer.writeShort(0); // empty name
            ByteBufOutputStream bbos = new ByteBufOutputStream(buffer);
            NBTOutputStream nbtOut = new NBTOutputStream(new DataOutputStream(bbos));
            nbtOut.writeValue((Object) tag,10);
            buffer.writeByte(0);
            bbos.flush();
        } catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * Anonymous NBT: writes ONLY the compound payload, as expected by protocol 774
     * for Text Component fields (system_chat, profileless_chat, etc.).
     *
     * The client reads anonymousNbt fields without a leading type byte or name.
     * cloudburstmc's writeValue(tag) writes compound entries + TAG_End(0x00)
     * automatically — exactly what we need.
     *
     * IMPORTANT: For a null/empty tag, writes byte(0) = TAG_End (empty compound).
     */
    public void writeAnonymousNbt(NbtMap tag) {
        if (tag == null) { buffer.writeByte(0); return; }
        try {
            buffer.writeByte(10);
            ByteBufOutputStream bbos = new ByteBufOutputStream(buffer);
            NBTOutputStream nbtOut = new NBTOutputStream(new DataOutputStream(bbos));
            nbtOut.writeValue((Object) tag);
            bbos.flush();
        } catch (IOException e) { e.printStackTrace(); }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // EXISTING METHODS — unchanged
    // ─────────────────────────────────────────────────────────────────────────

    public int readByte() { return buffer.readByte(); }
    public int readShort() { return buffer.readShort(); }

    public void writeVarInt(int value) {
        while ((value & -128) != 0) { buffer.writeByte(value & 127 | 128); value >>>= 7; }
        buffer.writeByte(value);
    }

    public static int varIntSize(int value) {
        int size = 1;
        while ((value & -128) != 0) { size++; value >>>= 7; }
        return size;
    }

    public void writeByteArray(byte[] data) { writeVarInt(data.length); buffer.writeBytes(data); }

    public int readInt() { return buffer.readInt(); }

    public void writeSlot(int id, int count) {
        if (count <= 0 || id <= 0) {
            writeVarInt(0);
        } else {
            writeVarInt(count);
            writeVarInt(id);
            writeVarInt(0);
            writeVarInt(0);
        }
    }

    /**
     * Item stack with a DataComponentPatch (1.21.11 wire format).
     * Layout: count VarInt, itemId VarInt, then DataComponentPatch =
     *   VarInt(nSet), VarInt(nRemoved), [for each set: componentId VarInt, value].
     * Empty patch = (0, 0) which matches the legacy writeSlot trailing pair.
     *
     * Component ids (data_component_type 注册表, 按 DataComponents 声明顺序):
     *   3  = minecraft:damage (物品已受损耐久, VarInt)
     *   6  = minecraft:custom_name (Optional<Component>)
     *   13 = minecraft:enchantments (ItemEnchantments: size + (id,level)* + showInTooltip)
     *   41 = minecraft:stored_enchantments (附魔书用)
     *   49 = minecraft:potion_contents
     *
     * enchants: map of (enchantmentRegistryId -> level); book=true writes
     *   stored_enchantments (id 41), else enchantments (id 13).
     * potion: (mobEffectId, amplifier, durationTicks); component id 49 = potion_contents.
     * damage: 已损失耐久值 (>0 才写); component id 3.
     */
    public void writeStackWithComponents(int id, int count,
            java.util.Map<Integer, Integer> enchants, boolean book,
            int potionEffectId, int potionAmp, int potionDuration) {
        writeStackWithComponents(id, count, enchants, book, potionEffectId, potionAmp, potionDuration, null, 0);
    }

    public void writeStackWithComponents(int id, int count,
            java.util.Map<Integer, Integer> enchants, boolean book,
            int potionEffectId, int potionAmp, int potionDuration, String customName) {
        writeStackWithComponents(id, count, enchants, book, potionEffectId, potionAmp, potionDuration, customName, 0);
    }

    public void writeStackWithComponents(int id, int count,
            java.util.Map<Integer, Integer> enchants, boolean book,
            int potionEffectId, int potionAmp, int potionDuration, String customName, int damage) {
        writeStackWithComponents(id, count, enchants, book, potionEffectId, potionAmp, potionDuration, customName, damage, -1, -1, -1);
    }

    /** #19 扩展: trimMaterial/trimPattern 为盔甲纹饰组件 (minecraft:trim, component id 54),
     *  值 = ArmorTrim(material holder VarInt, pattern holder VarInt); -1 表示无。
     *  potionRegistryId: minecraft:potion 注册表 holder id (水=0/凡品=1/浓稠=2/粗制=3 等), -1=无。
     *  用于水瓶等"无效果但有药水类型"的物品, 避免客户端渲染成"不可合成的药水"。 */
    public void writeStackWithComponents(int id, int count,
            java.util.Map<Integer, Integer> enchants, boolean book,
            int potionEffectId, int potionAmp, int potionDuration, String customName, int damage,
            int trimMaterial, int trimPattern) {
        writeStackWithComponents(id, count, enchants, book, potionEffectId, potionAmp, potionDuration, customName, damage, trimMaterial, trimPattern, -1);
    }

    public void writeStackWithComponents(int id, int count,
            java.util.Map<Integer, Integer> enchants, boolean book,
            int potionEffectId, int potionAmp, int potionDuration, String customName, int damage,
            int trimMaterial, int trimPattern, int potionRegistryId) {
        if (count <= 0 || id <= 0) { writeVarInt(0); return; }
        boolean hasEnch = enchants != null && !enchants.isEmpty();
        boolean hasPotion = potionEffectId > 0 || potionRegistryId >= 0;
        boolean hasName = customName != null && !customName.isEmpty();
        boolean hasDamage = damage > 0;
        boolean hasTrim = trimMaterial >= 0 && trimPattern >= 0;
        int nSet = (hasDamage ? 1 : 0) + (hasEnch ? 1 : 0) + (hasPotion ? 1 : 0) + (hasName ? 1 : 0) + (hasTrim ? 1 : 0);
        writeVarInt(count);
        writeVarInt(id);
        writeVarInt(nSet);
        writeVarInt(0); // removed count
        if (hasDamage) {
            // minecraft:damage (id 3): 已损失耐久值, VarInt。
            // 不写则客户端永远显示满耐久 -> 玩家误以为耐久不消耗。
            writeVarInt(3);
            writeVarInt(damage);
        }
        if (hasEnch) {
            // ItemEnchantments 组件值 (1.21.11): size VarInt + (id VarInt, level VarInt)* 。
            // 注意: 1.21.10+ 已移除 trailing show_in_tooltip boolean; 曾多写 1 字节
            // -> 客户端读下一个槽位时错位, 报 "No value with id 116" / "found N bytes extra"。
            writeVarInt(book ? 41 : 13);
            writeVarInt(enchants.size());
            for (java.util.Map.Entry<Integer, Integer> e : enchants.entrySet()) {
                writeVarInt(e.getKey());
                writeVarInt(e.getValue());
            }
        }
        if (hasPotion) {
            writeVarInt(49); // minecraft:potion_contents
            // PotionContents 组件值 (1.21.11): Optional<Holder<Potion>> + Optional<Integer> customColor
            //   + List<MobEffectInstance> + Optional<String> customName。
            if (potionRegistryId >= 0) {
                writeBoolean(true);  // potion holder 存在
                writeVarInt(potionRegistryId);
            } else {
                writeBoolean(false); // potion (optional Holder<Potion>) absent
            }
            writeBoolean(false); // customColor (optional Int) absent
            if (potionEffectId > 0) {
                // customEffects: list of MobEffectInstance
                writeVarInt(1);
                writeVarInt(potionEffectId);   // MobEffect holder registry id
                writeVarInt(potionAmp);        // amplifier
                writeVarInt(potionDuration);   // duration ticks
                writeBoolean(false);           // ambient
                writeBoolean(true);            // showParticles
                writeBoolean(true);            // showIcon
                writeBoolean(false);           // hiddenEffect (optional MobEffectInstance) absent
            } else {
                writeVarInt(0);                // 无 custom_effects
            }
            writeBoolean(false);           // customName (optional String) absent
        }
        if (hasName) {
            writeVarInt(6);  // minecraft:custom_name (声明顺序 id=6, 曾误用 4=UNBREAKABLE)
            // 组件值是 Component (NBT 标签), 不是 Optional<Component>。
            // custom_name 的 StreamCodec 直接写一个 NBT tag, 无 boolean 前缀;
            // 曾多写 writeBoolean(true) -> 客户端按 NBT 读到 0x01(TAG_BYTE) 而非 TAG_COMPOUND -> 解析失败踢出。
            writeAnonymousNbt(org.cloudburstmc.nbt.NbtMap.builder().putString("text", customName).build());
        }
        if (hasTrim) {
            // #19 minecraft:trim (component id 54): ArmorTrim = material(TrimMaterial holder) + pattern(TrimPattern holder)。
            // 原版 ArmorTrim.STREAM_CODEC = StreamCodec.composite(TrimMaterial.STREAM_CODEC, TrimPattern.STREAM_CODEC),
            // 二者均为 ByteBufCodecs.holder(registry, direct) —— 对 REFERENCE holder 仅写 VarInt(registryId+1), 无 present 布尔、无 tooltip 布尔。
            // trimMaterial/trimPattern 已是原版注册表 id（见 NetworkHandler.trimMaterialId/trimPatternId，取自 dumped_registries reg_4/reg_3.bin）。
            writeVarInt(54);
            writeVarInt(trimMaterial + 1);
            writeVarInt(trimPattern + 1);
        }
    }

    private static double lpSanitize(double d) {
        if (Double.isNaN(d)) return 0.0;
        return Math.max(-1.7179869183E10, Math.min(1.7179869183E10, d));
    }

    private static long lpPack(double d) {
        return Math.round((d * 0.5 + 0.5) * 32766.0);
    }

    public void writeVelocity(double vX, double vY, double vZ) {
        int sx = (int) Math.max(-32768, Math.min(32767, Math.round(vX * 8000.0)));
        int sy = (int) Math.max(-32768, Math.min(32767, Math.round(vY * 8000.0)));
        int sz = (int) Math.max(-32768, Math.min(32767, Math.round(vZ * 8000.0)));
        buffer.writeShort(sx);
        buffer.writeShort(sy);
        buffer.writeShort(sz);
    }

    // LpVec3 velocity encoding for the spawn_entity (add_entity) packet (1.21.11).
    // Mirrors Mojang net.minecraft.network.LpVec3.write exactly.
    // Zero magnitude writes a single 0 byte; non-zero writes 6 bytes (+ optional VarInt).
    public void writeLpVec3(double vX, double vY, double vZ) {
        double x = sanitizeLp(vX);
        double y = sanitizeLp(vY);
        double z = sanitizeLp(vZ);
        double max = Math.max(Math.abs(x), Math.max(Math.abs(y), Math.abs(z)));
        if (max < 3.051944088384301E-5) {
            buffer.writeByte(0);
            return;
        }
        long l1 = (long) Math.ceil(max);
        boolean continuation = ((l1 & 3L) != l1);
        long l2 = continuation ? (l1 & 3L | 4L) : l1;
        long l3 = packLp(x / l1) << 3L;
        long l4 = packLp(y / l1) << 18L;
        long l5 = packLp(z / l1) << 33L;
        long l6 = l2 | l3 | l4 | l5;
        buffer.writeByte((byte) (int) l6);
        buffer.writeByte((byte) (int) (l6 >> 8L));
        buffer.writeInt((int) (l6 >> 16L));
        if (continuation) {
            writeVarInt((int) (l1 >> 2L));
        }
    }

    private static double sanitizeLp(double d) {
        return Double.isNaN(d) ? 0.0 : Math.max(-1.7179869183E10, Math.min(1.7179869183E10, d));
    }

    private static long packLp(double d) {
        return Math.round((d * 0.5 + 0.5) * 32766.0);
    }

    public void writeAngle(float degrees) {
        byte angle = (byte) (Math.floor(degrees * 256.0F / 360.0F) % 256);
        buffer.writeByte(angle);
    }

    public void writeTextComponent(String text, String color) {
        NbtMap component = NbtMap.builder().putString("text", text).putString("color", color).build();
        writeNbt(component);
    }

    public void writePlayerMetadata(byte flags, byte skinParts) {
        buffer.writeByte(0); writeVarInt(0); buffer.writeByte(flags);
        buffer.writeByte(17); writeVarInt(0); buffer.writeByte(skinParts);
        buffer.writeByte(0xFF);
    }

    public void writePosition(int x, int y, int z) {
        long packed = ((long)(x & 0x3FFFFFF) << 38) | ((long)(z & 0x3FFFFFF) << 12) | (long)(y & 0xFFF);
        buffer.writeLong(packed);
    }

    public int[] readPosition() {
        long val = buffer.readLong();
        int x = (int)(val >> 38);          // 26-bit signed
        int z = (int)(val << 26 >> 38);    // 26-bit signed (shift left 26 to discard x, then right 38)
        int y = (int)(val << 52 >> 52);    // 12-bit signed (lowest 12 bits)
        return new int[]{x, y, z};
    }

    public int readVarInt() {
        int value = 0, size = 0; byte b;
        while (true) {
            if (!buffer.isReadable()) return -1;
            b = buffer.readByte();
            value |= (b & 127) << (size++ * 7);
            if ((b & 128) != 128) break;
        }
        return value;
    }

    public String readString() {
        int len = readVarInt(); byte[] bytes = new byte[len]; buffer.readBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public int readUnsignedShort() { return buffer.readUnsignedShort(); }
    public long readLong() { return buffer.readLong(); }

    public void writeString(String s) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        writeVarInt(bytes.length); buffer.writeBytes(bytes);
    }

    public void writeBitSet(long[] longs) {
        writeVarInt(longs.length);
        for (long l : longs) buffer.writeLong(l);
    }

    public void writeLongArray(long[] array) {
        writeVarInt(array.length);
        for (long l : array) buffer.writeLong(l);
    }

    public void writeUUID(UUID uuid) {
        buffer.writeLong(uuid.getMostSignificantBits());
        buffer.writeLong(uuid.getLeastSignificantBits());
    }

    public void writeShort(int v) { buffer.writeShort(v); }
    public void writeInt(int i) { buffer.writeInt(i); }
    public void writeLong(long l) { buffer.writeLong(l); }
    public void writeDouble(double d) { buffer.writeDouble(d); }
    public void writeFloat(float f) { buffer.writeFloat(f); }
    public void writeBoolean(boolean b) { buffer.writeByte(b ? 1 : 0); }
    public void writeByte(int b) { buffer.writeByte(b); }
    public void writeRawBytes(byte[] bytes) { buffer.writeBytes(bytes); }
    public ByteBuf getBuffer() { return buffer; }
}
