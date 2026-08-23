package com.CharunCore.server.worldgen.structure2;

import com.CharunCore.server.worldgen.WorldGenLevel;

import java.util.ArrayList;
import java.util.List;

public class PoolElementStructurePiece {

    private final StructurePoolElement element;
    private int positionX, positionY, positionZ;
    private final int groundLevelDelta;
    private final Rotation rotation;
    private BoundingBox boundingBox;
    private final List<JigsawJunction> junctions = new ArrayList<>();

    public PoolElementStructurePiece(StructurePoolElement element,
            int posX, int posY, int posZ, int groundLevelDelta,
            Rotation rotation, BoundingBox boundingBox) {
        this.element = element;
        this.positionX = posX;
        this.positionY = posY;
        this.positionZ = posZ;
        this.groundLevelDelta = groundLevelDelta;
        this.rotation = rotation;
        this.boundingBox = boundingBox;
    }

    public StructurePoolElement getElement() { return element; }
    public int getPositionX() { return positionX; }
    public int getPositionY() { return positionY; }
    public int getPositionZ() { return positionZ; }
    public int getGroundLevelDelta() { return groundLevelDelta; }
    public Rotation getRotation() { return rotation; }
    public BoundingBox getBoundingBox() { return boundingBox; }
    public List<JigsawJunction> getJunctions() { return junctions; }

    public void addJunction(JigsawJunction junction) {
        junctions.add(junction);
    }

    public void move(int dx, int dy, int dz) {
        this.positionX += dx;
        this.positionY += dy;
        this.positionZ += dz;
        this.boundingBox = new BoundingBox(
            boundingBox.minX + dx, boundingBox.minY + dy, boundingBox.minZ + dz,
            boundingBox.maxX + dx, boundingBox.maxY + dy, boundingBox.maxZ + dz);
    }

    public void place(WorldGenLevel level, StructureTemplateManager manager) {
        element.place(level, manager, positionX, positionY, positionZ, rotation, Mirror.NONE);
    }
}
