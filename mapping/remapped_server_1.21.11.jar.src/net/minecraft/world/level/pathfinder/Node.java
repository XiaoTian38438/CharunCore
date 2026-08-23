/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class Node
/*     */ {
/*     */   public final int x;
/*     */   public final int y;
/*     */   public final int z;
/*     */   private final int hash;
/*  15 */   public int heapIdx = -1;
/*     */   
/*     */   public float g;
/*     */   
/*     */   public float h;
/*     */   public float f;
/*     */   public Node cameFrom;
/*     */   public boolean closed;
/*     */   public float walkedDistance;
/*     */   public float costMalus;
/*  25 */   public PathType type = PathType.BLOCKED;
/*     */   
/*     */   public Node(int paramInt1, int paramInt2, int paramInt3) {
/*  28 */     this.x = paramInt1;
/*  29 */     this.y = paramInt2;
/*  30 */     this.z = paramInt3;
/*     */     
/*  32 */     this.hash = createHash(paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */   
/*     */   public Node cloneAndMove(int paramInt1, int paramInt2, int paramInt3) {
/*  36 */     Node node = new Node(paramInt1, paramInt2, paramInt3);
/*  37 */     node.heapIdx = this.heapIdx;
/*  38 */     node.g = this.g;
/*  39 */     node.h = this.h;
/*  40 */     node.f = this.f;
/*  41 */     node.cameFrom = this.cameFrom;
/*  42 */     node.closed = this.closed;
/*  43 */     node.walkedDistance = this.walkedDistance;
/*  44 */     node.costMalus = this.costMalus;
/*  45 */     node.type = this.type;
/*  46 */     return node;
/*     */   }
/*     */   
/*     */   public static int createHash(int paramInt1, int paramInt2, int paramInt3) {
/*  50 */     return paramInt2 & 0xFF | (paramInt1 & 0x7FFF) << 8 | (paramInt3 & 0x7FFF) << 24 | ((paramInt1 < 0) ? Integer.MIN_VALUE : 0) | ((paramInt3 < 0) ? 32768 : 0);
/*     */   }
/*     */   
/*     */   public float distanceTo(Node paramNode) {
/*  54 */     float f1 = (paramNode.x - this.x);
/*  55 */     float f2 = (paramNode.y - this.y);
/*  56 */     float f3 = (paramNode.z - this.z);
/*  57 */     return Mth.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
/*     */   }
/*     */   
/*     */   public float distanceToXZ(Node paramNode) {
/*  61 */     float f1 = (paramNode.x - this.x);
/*  62 */     float f2 = (paramNode.z - this.z);
/*  63 */     return Mth.sqrt(f1 * f1 + f2 * f2);
/*     */   }
/*     */   
/*     */   public float distanceTo(BlockPos paramBlockPos) {
/*  67 */     float f1 = (paramBlockPos.getX() - this.x);
/*  68 */     float f2 = (paramBlockPos.getY() - this.y);
/*  69 */     float f3 = (paramBlockPos.getZ() - this.z);
/*  70 */     return Mth.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
/*     */   }
/*     */   
/*     */   public float distanceToSqr(Node paramNode) {
/*  74 */     float f1 = (paramNode.x - this.x);
/*  75 */     float f2 = (paramNode.y - this.y);
/*  76 */     float f3 = (paramNode.z - this.z);
/*  77 */     return f1 * f1 + f2 * f2 + f3 * f3;
/*     */   }
/*     */   
/*     */   public float distanceToSqr(BlockPos paramBlockPos) {
/*  81 */     float f1 = (paramBlockPos.getX() - this.x);
/*  82 */     float f2 = (paramBlockPos.getY() - this.y);
/*  83 */     float f3 = (paramBlockPos.getZ() - this.z);
/*  84 */     return f1 * f1 + f2 * f2 + f3 * f3;
/*     */   }
/*     */   
/*     */   public float distanceManhattan(Node paramNode) {
/*  88 */     float f1 = Math.abs(paramNode.x - this.x);
/*  89 */     float f2 = Math.abs(paramNode.y - this.y);
/*  90 */     float f3 = Math.abs(paramNode.z - this.z);
/*  91 */     return f1 + f2 + f3;
/*     */   }
/*     */   
/*     */   public float distanceManhattan(BlockPos paramBlockPos) {
/*  95 */     float f1 = Math.abs(paramBlockPos.getX() - this.x);
/*  96 */     float f2 = Math.abs(paramBlockPos.getY() - this.y);
/*  97 */     float f3 = Math.abs(paramBlockPos.getZ() - this.z);
/*  98 */     return f1 + f2 + f3;
/*     */   }
/*     */   
/*     */   public BlockPos asBlockPos() {
/* 102 */     return new BlockPos(this.x, this.y, this.z);
/*     */   }
/*     */   
/*     */   public Vec3 asVec3() {
/* 106 */     return new Vec3(this.x, this.y, this.z);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 111 */     if (paramObject instanceof Node) { Node node = (Node)paramObject;
/* 112 */       return (this.hash == node.hash && this.x == node.x && this.y == node.y && this.z == node.z); }
/*     */     
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 119 */     return this.hash;
/*     */   }
/*     */   
/*     */   public boolean inOpenSet() {
/* 123 */     return (this.heapIdx >= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 128 */     return "Node{x=" + this.x + ", y=" + this.y + ", z=" + this.z + "}";
/*     */   }
/*     */   
/*     */   public void writeToStream(FriendlyByteBuf paramFriendlyByteBuf) {
/* 132 */     paramFriendlyByteBuf.writeInt(this.x);
/* 133 */     paramFriendlyByteBuf.writeInt(this.y);
/* 134 */     paramFriendlyByteBuf.writeInt(this.z);
/* 135 */     paramFriendlyByteBuf.writeFloat(this.walkedDistance);
/* 136 */     paramFriendlyByteBuf.writeFloat(this.costMalus);
/* 137 */     paramFriendlyByteBuf.writeBoolean(this.closed);
/* 138 */     paramFriendlyByteBuf.writeEnum(this.type);
/* 139 */     paramFriendlyByteBuf.writeFloat(this.f);
/*     */   }
/*     */   
/*     */   public static Node createFromStream(FriendlyByteBuf paramFriendlyByteBuf) {
/* 143 */     Node node = new Node(paramFriendlyByteBuf.readInt(), paramFriendlyByteBuf.readInt(), paramFriendlyByteBuf.readInt());
/* 144 */     readContents(paramFriendlyByteBuf, node);
/* 145 */     return node;
/*     */   }
/*     */   
/*     */   protected static void readContents(FriendlyByteBuf paramFriendlyByteBuf, Node paramNode) {
/* 149 */     paramNode.walkedDistance = paramFriendlyByteBuf.readFloat();
/* 150 */     paramNode.costMalus = paramFriendlyByteBuf.readFloat();
/* 151 */     paramNode.closed = paramFriendlyByteBuf.readBoolean();
/* 152 */     paramNode.type = (PathType)paramFriendlyByteBuf.readEnum(PathType.class);
/* 153 */     paramNode.f = paramFriendlyByteBuf.readFloat();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\pathfinder\Node.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */