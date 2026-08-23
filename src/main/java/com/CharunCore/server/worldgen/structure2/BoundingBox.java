package com.CharunCore.server.worldgen.structure2;

public final class BoundingBox {
    public final int minX, minY, minZ, maxX, maxY, maxZ;

    public BoundingBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.minX = minX; this.minY = minY; this.minZ = minZ;
        this.maxX = maxX; this.maxY = maxY; this.maxZ = maxZ;
    }

    public boolean intersects(BoundingBox other) {
        return this.maxX >= other.minX && this.minX <= other.maxX
            && this.maxY >= other.minY && this.minY <= other.maxY
            && this.maxZ >= other.minZ && this.minZ <= other.maxZ;
    }

    public int getSpanX() { return maxX - minX + 1; }
    public int getSpanY() { return maxY - minY + 1; }
    public int getSpanZ() { return maxZ - minZ + 1; }

    public BoundingBox inflate(int n) {
        return new BoundingBox(minX - n, minY - n, minZ - n, maxX + n, maxY + n, maxZ + n);
    }
}
