/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Tuple;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class MansionGrid
/*     */ {
/*     */   private static final int DEFAULT_SIZE = 11;
/*     */   private static final int CLEAR = 0;
/*     */   private static final int CORRIDOR = 1;
/*     */   private static final int ROOM = 2;
/*     */   private static final int START_ROOM = 3;
/*     */   private static final int TEST_ROOM = 4;
/*     */   private static final int BLOCKED = 5;
/*     */   private static final int ROOM_1x1 = 65536;
/*     */   private static final int ROOM_1x2 = 131072;
/*     */   private static final int ROOM_2x2 = 262144;
/*     */   private static final int ROOM_ORIGIN_FLAG = 1048576;
/*     */   private static final int ROOM_DOOR_FLAG = 2097152;
/*     */   private static final int ROOM_STAIRS_FLAG = 4194304;
/*     */   private static final int ROOM_CORRIDOR_FLAG = 8388608;
/*     */   private static final int ROOM_TYPE_MASK = 983040;
/*     */   private static final int ROOM_ID_MASK = 65535;
/*     */   private final RandomSource random;
/*     */   final WoodlandMansionPieces.SimpleGrid baseGrid;
/*     */   final WoodlandMansionPieces.SimpleGrid thirdFloorGrid;
/*     */   final WoodlandMansionPieces.SimpleGrid[] floorRooms;
/*     */   final int entranceX;
/*     */   final int entranceY;
/*     */   
/*     */   public MansionGrid(RandomSource paramRandomSource) {
/* 720 */     this.random = paramRandomSource;
/*     */     
/* 722 */     byte b = 11;
/* 723 */     this.entranceX = 7;
/* 724 */     this.entranceY = 4;
/*     */     
/* 726 */     this.baseGrid = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/* 727 */     this.baseGrid.set(this.entranceX, this.entranceY, this.entranceX + 1, this.entranceY + 1, 3);
/* 728 */     this.baseGrid.set(this.entranceX - 1, this.entranceY, this.entranceX - 1, this.entranceY + 1, 2);
/* 729 */     this.baseGrid.set(this.entranceX + 2, this.entranceY - 2, this.entranceX + 3, this.entranceY + 3, 5);
/* 730 */     this.baseGrid.set(this.entranceX + 1, this.entranceY - 2, this.entranceX + 1, this.entranceY - 1, 1);
/* 731 */     this.baseGrid.set(this.entranceX + 1, this.entranceY + 2, this.entranceX + 1, this.entranceY + 3, 1);
/* 732 */     this.baseGrid.set(this.entranceX - 1, this.entranceY - 1, 1);
/* 733 */     this.baseGrid.set(this.entranceX - 1, this.entranceY + 2, 1);
/*     */     
/* 735 */     this.baseGrid.set(0, 0, 11, 1, 5);
/* 736 */     this.baseGrid.set(0, 9, 11, 11, 5);
/*     */     
/* 738 */     recursiveCorridor(this.baseGrid, this.entranceX, this.entranceY - 2, Direction.WEST, 6);
/* 739 */     recursiveCorridor(this.baseGrid, this.entranceX, this.entranceY + 3, Direction.WEST, 6);
/* 740 */     recursiveCorridor(this.baseGrid, this.entranceX - 2, this.entranceY - 1, Direction.WEST, 3);
/* 741 */     recursiveCorridor(this.baseGrid, this.entranceX - 2, this.entranceY + 2, Direction.WEST, 3);
/* 742 */     while (cleanEdges(this.baseGrid));
/*     */ 
/*     */     
/* 745 */     this.floorRooms = new WoodlandMansionPieces.SimpleGrid[3];
/* 746 */     this.floorRooms[0] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/* 747 */     this.floorRooms[1] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/* 748 */     this.floorRooms[2] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/* 749 */     identifyRooms(this.baseGrid, this.floorRooms[0]);
/* 750 */     identifyRooms(this.baseGrid, this.floorRooms[1]);
/*     */ 
/*     */     
/* 753 */     this.floorRooms[0].set(this.entranceX + 1, this.entranceY, this.entranceX + 1, this.entranceY + 1, 8388608);
/* 754 */     this.floorRooms[1].set(this.entranceX + 1, this.entranceY, this.entranceX + 1, this.entranceY + 1, 8388608);
/*     */     
/* 756 */     this.thirdFloorGrid = new WoodlandMansionPieces.SimpleGrid(this.baseGrid.width, this.baseGrid.height, 5);
/* 757 */     setupThirdFloor();
/* 758 */     identifyRooms(this.thirdFloorGrid, this.floorRooms[2]);
/*     */   }
/*     */   
/*     */   public static boolean isHouse(WoodlandMansionPieces.SimpleGrid paramSimpleGrid, int paramInt1, int paramInt2) {
/* 762 */     int i = paramSimpleGrid.get(paramInt1, paramInt2);
/* 763 */     return (i == 1 || i == 2 || i == 3 || i == 4);
/*     */   }
/*     */   
/*     */   public boolean isRoomId(WoodlandMansionPieces.SimpleGrid paramSimpleGrid, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 767 */     return ((this.floorRooms[paramInt3].get(paramInt1, paramInt2) & 0xFFFF) == paramInt4);
/*     */   }
/*     */   
/*     */   public Direction get1x2RoomDirection(WoodlandMansionPieces.SimpleGrid paramSimpleGrid, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 771 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 772 */       if (isRoomId(paramSimpleGrid, paramInt1 + direction.getStepX(), paramInt2 + direction.getStepZ(), paramInt3, paramInt4)) {
/* 773 */         return direction;
/*     */       }
/*     */     } 
/* 776 */     return null;
/*     */   }
/*     */   
/*     */   private void recursiveCorridor(WoodlandMansionPieces.SimpleGrid paramSimpleGrid, int paramInt1, int paramInt2, Direction paramDirection, int paramInt3) {
/* 780 */     if (paramInt3 <= 0) {
/*     */       return;
/*     */     }
/*     */     
/* 784 */     paramSimpleGrid.set(paramInt1, paramInt2, 1);
/* 785 */     paramSimpleGrid.setif(paramInt1 + paramDirection.getStepX(), paramInt2 + paramDirection.getStepZ(), 0, 1);
/*     */     
/* 787 */     for (byte b = 0; b < 8; b++) {
/* 788 */       Direction direction = Direction.from2DDataValue(this.random.nextInt(4));
/* 789 */       if (direction != paramDirection.getOpposite())
/*     */       {
/*     */         
/* 792 */         if (direction != Direction.EAST || !this.random.nextBoolean()) {
/*     */ 
/*     */ 
/*     */           
/* 796 */           int i = paramInt1 + paramDirection.getStepX();
/* 797 */           int j = paramInt2 + paramDirection.getStepZ();
/* 798 */           if (paramSimpleGrid.get(i + direction.getStepX(), j + direction.getStepZ()) == 0 && paramSimpleGrid.get(i + direction.getStepX() * 2, j + direction.getStepZ() * 2) == 0) {
/* 799 */             recursiveCorridor(paramSimpleGrid, paramInt1 + paramDirection.getStepX() + direction.getStepX(), paramInt2 + paramDirection.getStepZ() + direction.getStepZ(), direction, paramInt3 - 1); break;
/*     */           } 
/*     */         }  } 
/*     */     } 
/* 803 */     Direction direction1 = paramDirection.getClockWise();
/* 804 */     Direction direction2 = paramDirection.getCounterClockWise();
/* 805 */     paramSimpleGrid.setif(paramInt1 + direction1.getStepX(), paramInt2 + direction1.getStepZ(), 0, 2);
/* 806 */     paramSimpleGrid.setif(paramInt1 + direction2.getStepX(), paramInt2 + direction2.getStepZ(), 0, 2);
/*     */     
/* 808 */     paramSimpleGrid.setif(paramInt1 + paramDirection.getStepX() + direction1.getStepX(), paramInt2 + paramDirection.getStepZ() + direction1.getStepZ(), 0, 2);
/* 809 */     paramSimpleGrid.setif(paramInt1 + paramDirection.getStepX() + direction2.getStepX(), paramInt2 + paramDirection.getStepZ() + direction2.getStepZ(), 0, 2);
/* 810 */     paramSimpleGrid.setif(paramInt1 + paramDirection.getStepX() * 2, paramInt2 + paramDirection.getStepZ() * 2, 0, 2);
/* 811 */     paramSimpleGrid.setif(paramInt1 + direction1.getStepX() * 2, paramInt2 + direction1.getStepZ() * 2, 0, 2);
/* 812 */     paramSimpleGrid.setif(paramInt1 + direction2.getStepX() * 2, paramInt2 + direction2.getStepZ() * 2, 0, 2);
/*     */   }
/*     */   
/*     */   private boolean cleanEdges(WoodlandMansionPieces.SimpleGrid paramSimpleGrid) {
/* 816 */     boolean bool = false;
/* 817 */     for (byte b = 0; b < paramSimpleGrid.height; b++) {
/* 818 */       for (byte b1 = 0; b1 < paramSimpleGrid.width; b1++) {
/* 819 */         if (paramSimpleGrid.get(b1, b) == 0) {
/* 820 */           int i = 0;
/* 821 */           i += isHouse(paramSimpleGrid, b1 + 1, b) ? 1 : 0;
/* 822 */           i += isHouse(paramSimpleGrid, b1 - 1, b) ? 1 : 0;
/* 823 */           i += isHouse(paramSimpleGrid, b1, b + 1) ? 1 : 0;
/* 824 */           i += isHouse(paramSimpleGrid, b1, b - 1) ? 1 : 0;
/*     */           
/* 826 */           if (i >= 3) {
/*     */             
/* 828 */             paramSimpleGrid.set(b1, b, 2);
/* 829 */             bool = true;
/* 830 */           } else if (i == 2) {
/*     */             
/* 832 */             int j = 0;
/* 833 */             j += isHouse(paramSimpleGrid, b1 + 1, b + 1) ? 1 : 0;
/* 834 */             j += isHouse(paramSimpleGrid, b1 - 1, b + 1) ? 1 : 0;
/* 835 */             j += isHouse(paramSimpleGrid, b1 + 1, b - 1) ? 1 : 0;
/* 836 */             j += isHouse(paramSimpleGrid, b1 - 1, b - 1) ? 1 : 0;
/* 837 */             if (j <= 1) {
/* 838 */               paramSimpleGrid.set(b1, b, 2);
/* 839 */               bool = true;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 845 */     return bool;
/*     */   }
/*     */ 
/*     */   
/*     */   private void setupThirdFloor() {
/* 850 */     ArrayList<Tuple> arrayList = Lists.newArrayList();
/* 851 */     WoodlandMansionPieces.SimpleGrid simpleGrid = this.floorRooms[1];
/* 852 */     for (byte b1 = 0; b1 < this.thirdFloorGrid.height; b1++) {
/* 853 */       for (byte b = 0; b < this.thirdFloorGrid.width; b++) {
/* 854 */         int m = simpleGrid.get(b, b1);
/* 855 */         int n = m & 0xF0000;
/* 856 */         if (n == 131072 && (m & 0x200000) == 2097152) {
/* 857 */           arrayList.add(new Tuple(Integer.valueOf(b), Integer.valueOf(b1)));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 862 */     if (arrayList.isEmpty()) {
/*     */       
/* 864 */       this.thirdFloorGrid.set(0, 0, this.thirdFloorGrid.width, this.thirdFloorGrid.height, 5);
/*     */       
/*     */       return;
/*     */     } 
/* 868 */     Tuple tuple = arrayList.get(this.random.nextInt(arrayList.size()));
/* 869 */     int i = simpleGrid.get(((Integer)tuple.getA()).intValue(), ((Integer)tuple.getB()).intValue());
/* 870 */     simpleGrid.set(((Integer)tuple.getA()).intValue(), ((Integer)tuple.getB()).intValue(), i | 0x400000);
/* 871 */     Direction direction1 = get1x2RoomDirection(this.baseGrid, ((Integer)tuple.getA()).intValue(), ((Integer)tuple.getB()).intValue(), 1, i & 0xFFFF);
/* 872 */     int j = ((Integer)tuple.getA()).intValue() + direction1.getStepX();
/* 873 */     int k = ((Integer)tuple.getB()).intValue() + direction1.getStepZ();
/*     */     
/* 875 */     for (byte b2 = 0; b2 < this.thirdFloorGrid.height; b2++) {
/* 876 */       for (byte b = 0; b < this.thirdFloorGrid.width; b++) {
/* 877 */         if (!isHouse(this.baseGrid, b, b2)) {
/* 878 */           this.thirdFloorGrid.set(b, b2, 5);
/* 879 */         } else if (b == ((Integer)tuple.getA()).intValue() && b2 == ((Integer)tuple.getB()).intValue()) {
/* 880 */           this.thirdFloorGrid.set(b, b2, 3);
/* 881 */         } else if (b == j && b2 == k) {
/* 882 */           this.thirdFloorGrid.set(b, b2, 3);
/* 883 */           this.floorRooms[2].set(b, b2, 8388608);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 888 */     ArrayList<Direction> arrayList1 = Lists.newArrayList();
/* 889 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 890 */       if (this.thirdFloorGrid.get(j + direction.getStepX(), k + direction.getStepZ()) == 0) {
/* 891 */         arrayList1.add(direction);
/*     */       }
/*     */     } 
/*     */     
/* 895 */     if (arrayList1.isEmpty()) {
/*     */       
/* 897 */       this.thirdFloorGrid.set(0, 0, this.thirdFloorGrid.width, this.thirdFloorGrid.height, 5);
/* 898 */       simpleGrid.set(((Integer)tuple.getA()).intValue(), ((Integer)tuple.getB()).intValue(), i);
/*     */       return;
/*     */     } 
/* 901 */     Direction direction2 = arrayList1.get(this.random.nextInt(arrayList1.size()));
/* 902 */     recursiveCorridor(this.thirdFloorGrid, j + direction2.getStepX(), k + direction2.getStepZ(), direction2, 4);
/* 903 */     while (cleanEdges(this.thirdFloorGrid));
/*     */   }
/*     */ 
/*     */   
/*     */   private void identifyRooms(WoodlandMansionPieces.SimpleGrid paramSimpleGrid1, WoodlandMansionPieces.SimpleGrid paramSimpleGrid2) {
/* 908 */     ObjectArrayList objectArrayList = new ObjectArrayList(); byte b;
/* 909 */     for (b = 0; b < paramSimpleGrid1.height; b++) {
/* 910 */       for (byte b1 = 0; b1 < paramSimpleGrid1.width; b1++) {
/* 911 */         if (paramSimpleGrid1.get(b1, b) == 2) {
/* 912 */           objectArrayList.add(new Tuple(Integer.valueOf(b1), Integer.valueOf(b)));
/*     */         }
/*     */       } 
/*     */     } 
/* 916 */     Util.shuffle((List)objectArrayList, this.random);
/*     */     
/* 918 */     b = 10;
/* 919 */     for (ObjectListIterator<Tuple> objectListIterator = objectArrayList.iterator(); objectListIterator.hasNext(); ) { Tuple tuple = objectListIterator.next();
/* 920 */       int i = ((Integer)tuple.getA()).intValue();
/* 921 */       int j = ((Integer)tuple.getB()).intValue();
/*     */       
/* 923 */       if (paramSimpleGrid2.get(i, j) == 0) {
/* 924 */         int k = i;
/* 925 */         int m = i;
/* 926 */         int n = j;
/* 927 */         int i1 = j;
/* 928 */         int i2 = 65536;
/* 929 */         if (paramSimpleGrid2.get(i + 1, j) == 0 && paramSimpleGrid2.get(i, j + 1) == 0 && paramSimpleGrid2.get(i + 1, j + 1) == 0 && paramSimpleGrid1
/* 930 */           .get(i + 1, j) == 2 && paramSimpleGrid1.get(i, j + 1) == 2 && paramSimpleGrid1.get(i + 1, j + 1) == 2) {
/*     */           
/* 932 */           m++;
/* 933 */           i1++;
/* 934 */           i2 = 262144;
/* 935 */         } else if (paramSimpleGrid2.get(i - 1, j) == 0 && paramSimpleGrid2.get(i, j + 1) == 0 && paramSimpleGrid2.get(i - 1, j + 1) == 0 && paramSimpleGrid1
/* 936 */           .get(i - 1, j) == 2 && paramSimpleGrid1.get(i, j + 1) == 2 && paramSimpleGrid1.get(i - 1, j + 1) == 2) {
/*     */           
/* 938 */           k--;
/* 939 */           i1++;
/* 940 */           i2 = 262144;
/* 941 */         } else if (paramSimpleGrid2.get(i - 1, j) == 0 && paramSimpleGrid2.get(i, j - 1) == 0 && paramSimpleGrid2.get(i - 1, j - 1) == 0 && paramSimpleGrid1
/* 942 */           .get(i - 1, j) == 2 && paramSimpleGrid1.get(i, j - 1) == 2 && paramSimpleGrid1.get(i - 1, j - 1) == 2) {
/*     */           
/* 944 */           k--;
/* 945 */           n--;
/* 946 */           i2 = 262144;
/* 947 */         } else if (paramSimpleGrid2.get(i + 1, j) == 0 && paramSimpleGrid1.get(i + 1, j) == 2) {
/* 948 */           m++;
/* 949 */           i2 = 131072;
/* 950 */         } else if (paramSimpleGrid2.get(i, j + 1) == 0 && paramSimpleGrid1.get(i, j + 1) == 2) {
/* 951 */           i1++;
/* 952 */           i2 = 131072;
/* 953 */         } else if (paramSimpleGrid2.get(i - 1, j) == 0 && paramSimpleGrid1.get(i - 1, j) == 2) {
/* 954 */           k--;
/* 955 */           i2 = 131072;
/* 956 */         } else if (paramSimpleGrid2.get(i, j - 1) == 0 && paramSimpleGrid1.get(i, j - 1) == 2) {
/* 957 */           n--;
/* 958 */           i2 = 131072;
/*     */         } 
/*     */ 
/*     */         
/* 962 */         int i3 = this.random.nextBoolean() ? k : m;
/* 963 */         int i4 = this.random.nextBoolean() ? n : i1;
/* 964 */         int i5 = 2097152;
/* 965 */         if (!paramSimpleGrid1.edgesTo(i3, i4, 1)) {
/* 966 */           i3 = (i3 == k) ? m : k;
/* 967 */           i4 = (i4 == n) ? i1 : n;
/* 968 */           if (!paramSimpleGrid1.edgesTo(i3, i4, 1)) {
/* 969 */             i4 = (i4 == n) ? i1 : n;
/* 970 */             if (!paramSimpleGrid1.edgesTo(i3, i4, 1)) {
/* 971 */               i3 = (i3 == k) ? m : k;
/* 972 */               i4 = (i4 == n) ? i1 : n;
/* 973 */               if (!paramSimpleGrid1.edgesTo(i3, i4, 1)) {
/*     */                 
/* 975 */                 i5 = 0;
/* 976 */                 i3 = k;
/* 977 */                 i4 = n;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/* 982 */         for (int i6 = n; i6 <= i1; i6++) {
/* 983 */           for (int i7 = k; i7 <= m; i7++) {
/* 984 */             if (i7 == i3 && i6 == i4) {
/* 985 */               paramSimpleGrid2.set(i7, i6, 0x100000 | i5 | i2 | b);
/*     */             } else {
/* 987 */               paramSimpleGrid2.set(i7, i6, i2 | b);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 992 */         b++;
/*     */       }  }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\WoodlandMansionPieces$MansionGrid.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */