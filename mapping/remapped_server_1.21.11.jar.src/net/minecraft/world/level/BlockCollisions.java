/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Cursor3D;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class BlockCollisions<T>
/*     */   extends AbstractIterator<T>
/*     */ {
/*     */   private final AABB box;
/*     */   private final CollisionContext context;
/*     */   private final Cursor3D cursor;
/*     */   private final BlockPos.MutableBlockPos pos;
/*     */   private final VoxelShape entityShape;
/*     */   private final CollisionGetter collisionGetter;
/*     */   private final boolean onlySuffocatingBlocks;
/*     */   private BlockGetter cachedBlockGetter;
/*     */   private long cachedBlockGetterPos;
/*     */   private final BiFunction<BlockPos.MutableBlockPos, VoxelShape, T> resultProvider;
/*     */   
/*     */   public BlockCollisions(CollisionGetter paramCollisionGetter, Entity paramEntity, AABB paramAABB, boolean paramBoolean, BiFunction<BlockPos.MutableBlockPos, VoxelShape, T> paramBiFunction) {
/*  34 */     this(paramCollisionGetter, 
/*     */         
/*  36 */         (paramEntity == null) ? CollisionContext.empty() : CollisionContext.of(paramEntity), paramAABB, paramBoolean, paramBiFunction);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockCollisions(CollisionGetter paramCollisionGetter, CollisionContext paramCollisionContext, AABB paramAABB, boolean paramBoolean, BiFunction<BlockPos.MutableBlockPos, VoxelShape, T> paramBiFunction) {
/*  44 */     this.context = paramCollisionContext;
/*  45 */     this.pos = new BlockPos.MutableBlockPos();
/*  46 */     this.entityShape = Shapes.create(paramAABB);
/*  47 */     this.collisionGetter = paramCollisionGetter;
/*  48 */     this.box = paramAABB;
/*  49 */     this.onlySuffocatingBlocks = paramBoolean;
/*  50 */     this.resultProvider = paramBiFunction;
/*     */ 
/*     */     
/*  53 */     int i = Mth.floor(paramAABB.minX - 1.0E-7D) - 1;
/*  54 */     int j = Mth.floor(paramAABB.maxX + 1.0E-7D) + 1;
/*  55 */     int k = Mth.floor(paramAABB.minY - 1.0E-7D) - 1;
/*  56 */     int m = Mth.floor(paramAABB.maxY + 1.0E-7D) + 1;
/*  57 */     int n = Mth.floor(paramAABB.minZ - 1.0E-7D) - 1;
/*  58 */     int i1 = Mth.floor(paramAABB.maxZ + 1.0E-7D) + 1;
/*  59 */     this.cursor = new Cursor3D(i, k, n, j, m, i1);
/*     */   }
/*     */   
/*     */   private BlockGetter getChunk(int paramInt1, int paramInt2) {
/*  63 */     int i = SectionPos.blockToSectionCoord(paramInt1);
/*  64 */     int j = SectionPos.blockToSectionCoord(paramInt2);
/*     */     
/*  66 */     long l = ChunkPos.asLong(i, j);
/*  67 */     if (this.cachedBlockGetter != null && this.cachedBlockGetterPos == l) {
/*  68 */       return this.cachedBlockGetter;
/*     */     }
/*  70 */     BlockGetter blockGetter = this.collisionGetter.getChunkForCollisions(i, j);
/*  71 */     this.cachedBlockGetter = blockGetter;
/*  72 */     this.cachedBlockGetterPos = l;
/*  73 */     return blockGetter;
/*     */   }
/*     */ 
/*     */   
/*     */   protected T computeNext() {
/*  78 */     while (this.cursor.advance()) {
/*  79 */       int i = this.cursor.nextX();
/*  80 */       int j = this.cursor.nextY();
/*  81 */       int k = this.cursor.nextZ();
/*     */       
/*  83 */       int m = this.cursor.getNextType();
/*     */       
/*  85 */       if (m == 3) {
/*     */         continue;
/*     */       }
/*     */       
/*  89 */       BlockGetter blockGetter = getChunk(i, k);
/*     */       
/*  91 */       if (blockGetter == null) {
/*     */         continue;
/*     */       }
/*     */       
/*  95 */       this.pos.set(i, j, k);
/*  96 */       BlockState blockState = blockGetter.getBlockState((BlockPos)this.pos);
/*     */       
/*  98 */       if (this.onlySuffocatingBlocks && !blockState.isSuffocating(blockGetter, (BlockPos)this.pos)) {
/*     */         continue;
/*     */       }
/*     */       
/* 102 */       if (m == 1 && !blockState.hasLargeCollisionShape()) {
/*     */         continue;
/*     */       }
/* 105 */       if (m == 2 && !blockState.is(Blocks.MOVING_PISTON)) {
/*     */         continue;
/*     */       }
/*     */       
/* 109 */       VoxelShape voxelShape1 = this.context.getCollisionShape(blockState, this.collisionGetter, (BlockPos)this.pos);
/*     */       
/* 111 */       if (voxelShape1 == Shapes.block()) {
/* 112 */         if (this.box.intersects(i, j, k, i + 1.0D, j + 1.0D, k + 1.0D))
/* 113 */           return this.resultProvider.apply(this.pos, voxelShape1.move((Vec3i)this.pos)); 
/*     */         continue;
/*     */       } 
/* 116 */       VoxelShape voxelShape2 = voxelShape1.move((Vec3i)this.pos);
/* 117 */       if (!voxelShape2.isEmpty() && Shapes.joinIsNotEmpty(voxelShape2, this.entityShape, BooleanOp.AND)) {
/* 118 */         return this.resultProvider.apply(this.pos, voxelShape2);
/*     */       }
/*     */     } 
/*     */     
/* 122 */     return (T)endOfData();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\BlockCollisions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */