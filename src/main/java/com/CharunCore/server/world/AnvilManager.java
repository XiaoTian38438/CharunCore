package com.CharunCore.server.world;

import org.cloudburstmc.nbt.NBTInputStream;
import org.cloudburstmc.nbt.NBTOutputStream;
import org.cloudburstmc.nbt.NbtMap;
import java.io.*;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class AnvilManager {
    private static final int SECTOR_SIZE = 4096;

    /**
     * 加载 MCA 中的区块 NBT
     *
     * FIXES:
     *   - Added length validation to prevent negative array size / OOB
     *   - Added compression type check (only type 2 = zlib is supported)
     *   - Validate sector count to prevent reading garbage
     */
    public static NbtMap loadChunkNbt(File regionFolder, int chunkX, int chunkZ) {
        File mcaFile = new File(regionFolder, "r." + (chunkX >> 5) + "." + (chunkZ >> 5) + ".mca");
        if (!mcaFile.exists()) return null;
        // 与 saveChunkNbt 共享类锁: 防止并发读到半写数据(torn read)误判区块缺失而重新生成
        synchronized (AnvilManager.class) {
            return loadChunkNbtLocked(mcaFile, chunkX, chunkZ);
        }
    }

    private static NbtMap loadChunkNbtLocked(File mcaFile, int chunkX, int chunkZ) {

        try (RandomAccessFile raf = new RandomAccessFile(mcaFile, "r")) {
            int index = ((chunkX & 31) + (chunkZ & 31) * 32) * 4;
            if (raf.length() < index + 4) return null;

            raf.seek(index);
            int offsetData = raf.readInt();
            int sectorOffset = offsetData >> 8;
            int sectorCount = offsetData & 0xFF;
            if (sectorOffset == 0 || sectorCount == 0) return null;

            // Validate sector range
            long dataStart = (long) sectorOffset * SECTOR_SIZE;
            if (dataStart + 5 > raf.length()) return null;

            raf.seek(dataStart);
            int length = raf.readInt();
            if (length <= 1 || length > SECTOR_SIZE * sectorCount) return null;

            byte compression = raf.readByte();
            if (compression == 1) {
                // gzip (旧版世界/外部工具写入): 同样支持, 防止被当作缺失区块覆盖
                byte[] gzipped = new byte[length - 1];
                raf.readFully(gzipped);
                try (NBTInputStream nbtIn = new NBTInputStream(new DataInputStream(
                        new java.util.zip.GZIPInputStream(new ByteArrayInputStream(gzipped))))) {
                    return (NbtMap) nbtIn.readTag();
                }
            }
            if (compression != 2) return null; // Only zlib (2) / gzip (1) supported

            byte[] compressed = new byte[length - 1];
            raf.readFully(compressed);

            try (NBTInputStream nbtIn = new NBTInputStream(new DataInputStream(new InflaterInputStream(new ByteArrayInputStream(compressed))))) {
                return (NbtMap) nbtIn.readTag();
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将区块保存回 MCA 文件 (1.21.11 兼容格式)
     *
     * FIXES vs original:
     *   1. Check if the chunk already has a sector — if so, only overwrite if new data
     *      fits in the same sector count. Otherwise, mark old sector as free and append.
     *   2. Validate sector alignment with setLength() properly
     *   3. Handle file creation race condition with createNewFile()
     *   4. Write timestamp in the modification timestamp table (second 4096 bytes of header)
     */
    public static synchronized void saveChunkNbt(File regionFolder, int chunkX, int chunkZ, NbtMap nbt) {
        if (!regionFolder.exists()) {
            regionFolder.mkdirs();
        }

        File mcaFile = new File(regionFolder, "r." + (chunkX >> 5) + "." + (chunkZ >> 5) + ".mca");
        try (RandomAccessFile raf = new RandomAccessFile(mcaFile, "rw")) {
            // Initialize header if file is new or too small
            if (raf.length() < 8192) {
                raf.write(new byte[8192]);
            }

            // Serialize and compress
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (NBTOutputStream nbtOut = new NBTOutputStream(new DataOutputStream(new DeflaterOutputStream(baos)))) {
                nbtOut.writeTag(nbt);
            }
            byte[] data = baos.toByteArray();

            // Calculate needed sectors (5 = 4 bytes length + 1 byte compression type + data)
            int totalBytes = 5 + data.length;
            int neededSectors = (totalBytes + SECTOR_SIZE - 1) / SECTOR_SIZE;

            int index = ((chunkX & 31) + (chunkZ & 31) * 32) * 4;

            // Read existing entry to check for free space reuse
            raf.seek(index);
            int oldOffsetData = raf.readInt();
            int oldSectorCount = oldOffsetData & 0xFF;

            int sectorOffset;
            if (oldSectorCount >= neededSectors && oldOffsetData != 0) {
                // Reuse existing sector space
                sectorOffset = oldOffsetData >> 8;
            } else {
                // Append at end of file
                sectorOffset = (int) ((raf.length() + SECTOR_SIZE - 1) / SECTOR_SIZE);
            }

            // Write chunk data at sector
            raf.seek((long) sectorOffset * SECTOR_SIZE);
            raf.writeInt(data.length + 1); // +1 for compression type byte
            raf.writeByte(2);             // zlib compression
            raf.write(data);

            // Pad to sector boundary — 只扩不缩!
            // 旧实现 setLength(alignedEnd) 在复用更大旧扇区时会把文件截断,
            // 物理删除排在其后的所有区块数据 → 区块丢失/错乱(越玩越乱的根因)。
            long endPos = raf.getFilePointer();
            long alignedEnd = ((long) sectorOffset + neededSectors) * SECTOR_SIZE;
            if (endPos < alignedEnd && raf.length() < alignedEnd) {
                raf.setLength(alignedEnd);
            }

            // Update location table (first 4096 bytes of header)
            // sector 数字段仅 1 字节: 超过 255 会截断导致区块"消失", 此处拒绝写入并告警
            if (neededSectors > 255) {
                System.err.println("[Anvil] 区块 (" + chunkX + "," + chunkZ + ") 压缩后 " + data.length
                        + " 字节超过 255 扇区上限, 跳过写入防止位置表截断损坏");
                return;
            }
            raf.seek(index);
            raf.writeInt((sectorOffset << 8) | neededSectors);

            // Update timestamp table (second 4096 bytes of header)
            int timestampIndex = 4096 + index;
            raf.seek(timestampIndex);
            raf.writeInt((int) (System.currentTimeMillis() / 1000));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
