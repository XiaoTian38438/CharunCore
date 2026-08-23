package com.CharunCore.server.network;

import io.netty.buffer.ByteBuf;

import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public final class Compression {

    private static final int ZLIB_LEVEL = 6;
    private static final ThreadLocal<Deflater> DEFLATER =
            ThreadLocal.withInitial(() -> new Deflater(ZLIB_LEVEL));
    private static final ThreadLocal<Inflater> INFLATER =
            ThreadLocal.withInitial(Inflater::new);
    private static final ThreadLocal<byte[]> COPY_BUF = ThreadLocal.withInitial(() -> new byte[8192]);

    private Compression() {}

    public static byte[] compress(ByteBuf input) {
        Deflater deflater = DEFLATER.get();
        deflater.reset();
        byte[] src = new byte[input.readableBytes()];
        input.readBytes(src);
        deflater.setInput(src);
        deflater.finish();
        io.netty.buffer.ByteBuf out = io.netty.buffer.Unpooled.buffer(src.length / 2 + 16);
        byte[] buf = COPY_BUF.get();
        try {
            while (!deflater.finished()) {
                int n = deflater.deflate(buf);
                if (n > 0) out.writeBytes(buf, 0, n);
            }
            byte[] result = new byte[out.readableBytes()];
            out.readBytes(result);
            return result;
        } finally {
            out.release();
        }
    }

    public static ByteBuf decompress(ByteBuf input, int expectedSize) throws DataFormatException {
        Inflater inflater = INFLATER.get();
        inflater.reset();
        byte[] src = new byte[input.readableBytes()];
        input.readBytes(src);
        inflater.setInput(src);
        ByteBuf out = io.netty.buffer.Unpooled.buffer(expectedSize);
        byte[] buf = COPY_BUF.get();
        int produced = 0;
        try {
            while (!inflater.finished()) {
                int n = inflater.inflate(buf);
                if (n == 0 && inflater.needsInput()) break;
                produced += n;
                if (produced > expectedSize) throw new DataFormatException("decompressed overrun");
                if (n > 0) out.writeBytes(buf, 0, n);
            }
            if (produced != expectedSize) throw new DataFormatException("size mismatch " + produced + " != " + expectedSize);
            return out;
        } catch (DataFormatException | RuntimeException e) {
            out.release();
            throw e;
        }
    }
}
