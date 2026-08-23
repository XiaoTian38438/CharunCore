package com.CharunCore.server.worldgen.structure2;

import com.CharunCore.server.utils.BlockStateHelper;
import org.cloudburstmc.nbt.NbtMap;

public record JigsawBlockInfo(int x, int y, int z, int blockStateId, NbtMap nbt,
                                String pool, String name, String target,
                                boolean rollable, int placementPriority, int selectionPriority) {

    public String frontFacing() {
        String orientation = getOrientation();
        if (orientation == null) return "up";
        int underscore = orientation.indexOf('_');
        return underscore > 0 ? orientation.substring(0, underscore) : orientation;
    }

    public String topFacing() {
        String orientation = getOrientation();
        if (orientation == null) return "up";
        int underscore = orientation.indexOf('_');
        return underscore > 0 ? orientation.substring(underscore + 1) : orientation;
    }

    private String getOrientation() {
        return BlockStateHelper.getProp(blockStateId, "orientation");
    }

    public static String opposite(String direction) {
        return switch (direction) {
            case "down" -> "up";
            case "up" -> "down";
            case "north" -> "south";
            case "south" -> "north";
            case "east" -> "west";
            case "west" -> "east";
            default -> direction;
        };
    }

    public static boolean canAttach(JigsawBlockInfo parent, JigsawBlockInfo child) {
        String parentFront = parent.frontFacing();
        String childFront = child.frontFacing();
        if (!parentFront.equals(opposite(childFront))) return false;
        if (!parent.rollable() && !parent.topFacing().equals(child.topFacing())) return false;
        return parent.target().equals(child.name());
    }
}
