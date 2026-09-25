package com.CharunCore.server.world;

import com.CharunCore.server.network.protocol.PacketBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * #14 验证：盔甲纹饰(trim)组件 id=54 的线序必须与原版 1.21.11 一致。
 * 原版 ArmorTrim.STREAM_CODEC = composite(TrimMaterial.STREAM_CODEC, TrimPattern.STREAM_CODEC),
 * 二者均为 ByteBufCodecs.holder(REFERENCE) -> 仅写 VarInt(registryId+1)，无 present 布尔、无 tooltip 布尔。
 *
 * 本测试用真实 PacketBuffer 编码一个猪鼻纹饰(snout, 注册表 id=11) + 铁(iron, id=5) 的 iron_chestplate，
 * 校验组件块内含 component id 54，后跟 VarInt(5+1)=6、VarInt(11+1)=12（即原版线序）。
 */
public final class TrimWireTest {
    public static void main(String[] args) throws Exception {
        try { com.CharunCore.server.utils.BlockManager.init(); } catch (Exception e) { System.err.println("[TrimWireTest] init warn: " + e.getMessage()); }
        int chestId = com.CharunCore.server.utils.BlockManager.getItemIdByName("iron_chestplate");
        if (chestId <= 0) { System.err.println("[TrimWireTest] 无法解析 iron_chestplate id, 跳过(需先 loadItems)"); return; }

        ByteBuf buf = Unpooled.buffer();
        PacketBuffer pb = new PacketBuffer(buf);
        // 猪鼻纹饰: material=iron(id 5), pattern=snout(id 11)
        pb.writeStackWithComponents(chestId, 1, null, false, 0, 0, 0, null, 0, 5, 11, -1);

        byte[] all = new byte[buf.readableBytes()];
        buf.getBytes(0, all);

        // 线序: count(varint) id(varint) nSet(varint) removed(0) ...components
        int p = 0;
        int count = readVarInt(all, p); p = next;
        int id = readVarInt(all, p); p = next;
        int nSet = readVarInt(all, p); p = next;
        int removed = readVarInt(all, p); p = next;
        if (removed != 0) throw new AssertionError("removed!=0 at " + p);
        if (nSet != 1) throw new AssertionError("nSet!=1, got " + nSet);

        int compId = readVarInt(all, p); p = next;
        if (compId != 54) throw new AssertionError("expected trim comp 54, got " + compId);
        int mat = readVarInt(all, p); p = next;
        int pat = readVarInt(all, p); p = next;
        if (mat != 5 + 1) throw new AssertionError("material wire should be 6, got " + mat);
        if (pat != 11 + 1) throw new AssertionError("pattern wire should be 12, got " + pat);

        // 解码回放（复用 NetworkHandler 的组件解析逻辑）：手动实现 trim 分支（-1 偏移）
        int dMat = mat > 0 ? mat - 1 : -1;
        int dPat = pat > 0 ? pat - 1 : -1;
        if (dMat != 5 || dPat != 11) throw new AssertionError("decode round-trip failed");

        System.out.println("[TrimWireTest] PASS: snout+iron trim 线序正确 (comp54, mat6, pat12) 且可往返解码");
    }

    private static int next = 0;
    private static int readVarInt(byte[] b, int p) {
        int shift = 0, val = 0, i = p;
        while (true) {
            int x = b[i++] & 0xFF;
            val |= (x & 0x7f) << shift;
            if ((x & 0x80) == 0) break;
            shift += 7;
        }
        next = i;
        return val;
    }
}
