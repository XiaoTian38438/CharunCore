package com.CharunCore.server.worldgen.structure2;

import com.CharunCore.server.utils.BlockStateHelper;

public final class BlockTransform {

    private BlockTransform() {}

    private static final String[] FACING_ORDER = {"north", "east", "south", "west"};

    public static int transform(int stateId, Rotation rotation, Mirror mirror) {
        if (stateId == 0) return 0;
        if (rotation == Rotation.NONE && mirror == Mirror.NONE) return stateId;

        String name = BlockStateHelper.getName(stateId);
        if (name == null) return stateId;

        String facing = BlockStateHelper.getProp(stateId, "facing");
        if (facing != null) {
            int dirIndex = facingIndex(facing);
            if (dirIndex >= 0) {
                if (mirror == Mirror.LEFT_RIGHT) {
                    dirIndex = (dirIndex == 1) ? 3 : (dirIndex == 3) ? 1 : dirIndex;
                } else if (mirror == Mirror.FRONT_BACK) {
                    dirIndex = (dirIndex == 0) ? 2 : (dirIndex == 2) ? 0 : dirIndex;
                }
                dirIndex = (dirIndex + rotation.ordinal()) & 3;
                return BlockStateHelper.withProp(stateId, "facing", FACING_ORDER[dirIndex]);
            }
        }

        String axis = BlockStateHelper.getProp(stateId, "axis");
        if (axis != null) {
            if (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90) {
                String newAxis = axis.equals("x") ? "z" : axis.equals("z") ? "x" : axis;
                return BlockStateHelper.withProp(stateId, "axis", newAxis);
            }
            return stateId;
        }

        String rotationProp = BlockStateHelper.getProp(stateId, "rotation");
        if (rotationProp != null) {
            try {
                int r = Integer.parseInt(rotationProp);
                r = (r + rotation.ordinal() * 4) & 15;
                return BlockStateHelper.withProp(stateId, "rotation", String.valueOf(r));
            } catch (NumberFormatException e) {
                return stateId;
            }
        }

        String orientation = BlockStateHelper.getProp(stateId, "orientation");
        if (orientation != null) {
            return transformOrientation(stateId, orientation, rotation, mirror);
        }

        return stateId;
    }

    private static int facingIndex(String facing) {
        for (int i = 0; i < FACING_ORDER.length; i++) {
            if (FACING_ORDER[i].equals(facing)) return i;
        }
        return -1;
    }

    private static int transformOrientation(int stateId, String orientation, Rotation rotation, Mirror mirror) {
        int us = orientation.indexOf('_');
        if (us <= 0) return stateId;
        String front = orientation.substring(0, us);
        String top = orientation.substring(us + 1);
        // 原版 FrontAndTop.rotate(Rotation)/mirror(Mirror)：front 与 top *同时* 旋转、*同时* 镜像。
        // 此前只旋转 front、只镜像 top，导致含 crafter 且带旋转/镜像的模板中朝向错误。
        // 变换顺序采用 先 mirror 后 rotate，与 vanilla StructureTemplate（state.mirror(m).rotate(r)）
        // 以及本类 facing 分支的处理顺序保持一致，确保与原版语义等价。
        front = mirrorDir(front, mirror);
        top = mirrorDir(top, mirror);
        front = rotateDir(front, rotation);
        top = rotateDir(top, rotation);
        return BlockStateHelper.withProp(stateId, "orientation", front + "_" + top);
    }

    private static String rotateDir(String dir, Rotation rotation) {
        return switch (rotation) {
            case CLOCKWISE_90 -> switch (dir) {
                case "north" -> "east";
                case "east" -> "south";
                case "south" -> "west";
                case "west" -> "north";
                default -> dir;
            };
            case COUNTERCLOCKWISE_90 -> switch (dir) {
                case "north" -> "west";
                case "west" -> "south";
                case "south" -> "east";
                case "east" -> "north";
                default -> dir;
            };
            case CLOCKWISE_180 -> switch (dir) {
                case "north" -> "south";
                case "south" -> "north";
                case "east" -> "west";
                case "west" -> "east";
                default -> dir;
            };
            default -> dir;
        };
    }

    private static String mirrorDir(String dir, Mirror mirror) {
        return switch (mirror) {
            case LEFT_RIGHT -> switch (dir) {
                case "north" -> "south";
                case "south" -> "north";
                default -> dir;
            };
            case FRONT_BACK -> switch (dir) {
                case "east" -> "west";
                case "west" -> "east";
                default -> dir;
            };
            default -> dir;
        };
    }
}
