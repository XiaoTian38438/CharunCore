/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChargeCursor
/*     */ {
/*     */   private static final ObjectArrayList<Vec3i> NON_CORNER_NEIGHBOURS;
/*     */   public static final int MAX_CURSOR_DECAY_DELAY = 1;
/*     */   private BlockPos pos;
/*     */   int charge;
/*     */   private int updateDelay;
/*     */   private int decayDelay;
/*     */   private Set<Direction> facings;
/*     */   private static final Codec<Set<Direction>> DIRECTION_SET;
/*     */   public static final Codec<ChargeCursor> CODEC;
/*     */   
/*     */   static {
/* 134 */     NON_CORNER_NEIGHBOURS = (ObjectArrayList<Vec3i>)Util.make(new ObjectArrayList(18), paramObjectArrayList -> {
/*     */           Objects.requireNonNull(paramObjectArrayList);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           BlockPos.betweenClosedStream(new BlockPos(-1, -1, -1), new BlockPos(1, 1, 1)).filter(()).map(BlockPos::immutable).forEach(paramObjectArrayList::add);
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 151 */     DIRECTION_SET = Direction.CODEC.listOf().xmap(paramList -> Sets.newEnumSet(paramList, Direction.class), Lists::newArrayList);
/*     */     
/* 153 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)BlockPos.CODEC.fieldOf("pos").forGetter(ChargeCursor::getPos), (App)Codec.intRange(0, 1000).fieldOf("charge").orElse(Integer.valueOf(0)).forGetter(ChargeCursor::getCharge), (App)Codec.intRange(0, 1).fieldOf("decay_delay").orElse(Integer.valueOf(1)).forGetter(ChargeCursor::getDecayDelay), (App)Codec.intRange(0, 2147483647).fieldOf("update_delay").orElse(Integer.valueOf(0)).forGetter(()), (App)DIRECTION_SET.lenientOptionalFieldOf("facings").forGetter(())).apply((Applicative)paramInstance, ChargeCursor::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ChargeCursor(BlockPos paramBlockPos, int paramInt1, int paramInt2, int paramInt3, Optional<Set<Direction>> paramOptional) {
/* 162 */     this.pos = paramBlockPos;
/* 163 */     this.charge = paramInt1;
/* 164 */     this.decayDelay = paramInt2;
/* 165 */     this.updateDelay = paramInt3;
/* 166 */     this.facings = paramOptional.orElse(null);
/*     */   }
/*     */   
/*     */   public ChargeCursor(BlockPos paramBlockPos, int paramInt) {
/* 170 */     this(paramBlockPos, paramInt, 1, 0, Optional.empty());
/*     */   }
/*     */   
/*     */   public BlockPos getPos() {
/* 174 */     return this.pos;
/*     */   }
/*     */   
/*     */   boolean isPosUnreasonable(BlockPos paramBlockPos) {
/* 178 */     return (this.pos.distChessboard((Vec3i)paramBlockPos) > 1024);
/*     */   }
/*     */   
/*     */   public int getCharge() {
/* 182 */     return this.charge;
/*     */   }
/*     */   
/*     */   public int getDecayDelay() {
/* 186 */     return this.decayDelay;
/*     */   }
/*     */   
/*     */   public Set<Direction> getFacingData() {
/* 190 */     return this.facings;
/*     */   }
/*     */   
/*     */   private boolean shouldUpdate(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, boolean paramBoolean) {
/* 194 */     if (this.charge <= 0) {
/* 195 */       return false;
/*     */     }
/* 197 */     if (paramBoolean) {
/* 198 */       return true;
/*     */     }
/* 200 */     if (paramLevelAccessor instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevelAccessor;
/* 201 */       return serverLevel.shouldTickBlocksAt(paramBlockPos); }
/*     */     
/* 203 */     return false;
/*     */   }
/*     */   
/*     */   public void update(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, RandomSource paramRandomSource, SculkSpreader paramSculkSpreader, boolean paramBoolean) {
/* 207 */     if (!shouldUpdate(paramLevelAccessor, paramBlockPos, paramSculkSpreader.isWorldGeneration)) {
/*     */       return;
/*     */     }
/*     */     
/* 211 */     if (this.updateDelay > 0) {
/* 212 */       this.updateDelay--;
/*     */       
/*     */       return;
/*     */     } 
/* 216 */     BlockState blockState = paramLevelAccessor.getBlockState(this.pos);
/* 217 */     SculkBehaviour sculkBehaviour = getBlockBehaviour(blockState);
/*     */ 
/*     */     
/* 220 */     if (paramBoolean && sculkBehaviour.attemptSpreadVein(paramLevelAccessor, this.pos, blockState, this.facings, paramSculkSpreader.isWorldGeneration())) {
/* 221 */       if (sculkBehaviour.canChangeBlockStateOnSpread()) {
/* 222 */         blockState = paramLevelAccessor.getBlockState(this.pos);
/* 223 */         sculkBehaviour = getBlockBehaviour(blockState);
/*     */       } 
/* 225 */       paramLevelAccessor.playSound(null, this.pos, SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */     } 
/*     */ 
/*     */     
/* 229 */     this.charge = sculkBehaviour.attemptUseCharge(this, paramLevelAccessor, paramBlockPos, paramRandomSource, paramSculkSpreader, paramBoolean);
/*     */     
/* 231 */     if (this.charge <= 0) {
/* 232 */       sculkBehaviour.onDischarged(paramLevelAccessor, blockState, this.pos, paramRandomSource);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 237 */     BlockPos blockPos = getValidMovementPos(paramLevelAccessor, this.pos, paramRandomSource);
/* 238 */     if (blockPos != null) {
/* 239 */       sculkBehaviour.onDischarged(paramLevelAccessor, blockState, this.pos, paramRandomSource);
/* 240 */       this.pos = blockPos.immutable();
/* 241 */       if (paramSculkSpreader.isWorldGeneration() && !this.pos.closerThan(new Vec3i(paramBlockPos.getX(), this.pos.getY(), paramBlockPos.getZ()), 15.0D)) {
/* 242 */         this.charge = 0;
/*     */         return;
/*     */       } 
/* 245 */       blockState = paramLevelAccessor.getBlockState(blockPos);
/*     */     } 
/*     */     
/* 248 */     if (blockState.getBlock() instanceof SculkBehaviour) {
/* 249 */       this.facings = MultifaceBlock.availableFaces(blockState);
/*     */     }
/* 251 */     this.decayDelay = sculkBehaviour.updateDecayDelay(this.decayDelay);
/* 252 */     this.updateDelay = sculkBehaviour.getSculkSpreadDelay();
/*     */   }
/*     */   
/*     */   void mergeWith(ChargeCursor paramChargeCursor) {
/* 256 */     this.charge += paramChargeCursor.charge;
/* 257 */     paramChargeCursor.charge = 0;
/* 258 */     this.updateDelay = Math.min(this.updateDelay, paramChargeCursor.updateDelay);
/*     */   }
/*     */   
/*     */   private static SculkBehaviour getBlockBehaviour(BlockState paramBlockState) {
/* 262 */     Block block = paramBlockState.getBlock(); SculkBehaviour sculkBehaviour = (SculkBehaviour)block; return (block instanceof SculkBehaviour) ? sculkBehaviour : SculkBehaviour.DEFAULT;
/*     */   }
/*     */   
/*     */   private static List<Vec3i> getRandomizedNonCornerNeighbourOffsets(RandomSource paramRandomSource) {
/* 266 */     return Util.shuffledCopy(NON_CORNER_NEIGHBOURS, paramRandomSource);
/*     */   }
/*     */   
/*     */   private static BlockPos getValidMovementPos(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 270 */     BlockPos.MutableBlockPos mutableBlockPos1 = paramBlockPos.mutable();
/* 271 */     BlockPos.MutableBlockPos mutableBlockPos2 = paramBlockPos.mutable();
/*     */     
/* 273 */     for (Vec3i vec3i : getRandomizedNonCornerNeighbourOffsets(paramRandomSource)) {
/* 274 */       mutableBlockPos2.setWithOffset((Vec3i)paramBlockPos, vec3i);
/* 275 */       BlockState blockState = paramLevelAccessor.getBlockState((BlockPos)mutableBlockPos2);
/*     */       
/* 277 */       if (blockState.getBlock() instanceof SculkBehaviour && isMovementUnobstructed(paramLevelAccessor, paramBlockPos, (BlockPos)mutableBlockPos2)) {
/* 278 */         mutableBlockPos1.set((Vec3i)mutableBlockPos2);
/*     */         
/* 280 */         if (SculkVeinBlock.hasSubstrateAccess(paramLevelAccessor, blockState, (BlockPos)mutableBlockPos2)) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/* 285 */     return mutableBlockPos1.equals(paramBlockPos) ? null : (BlockPos)mutableBlockPos1;
/*     */   }
/*     */   
/*     */   private static boolean isMovementUnobstructed(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos1, BlockPos paramBlockPos2) {
/* 289 */     if (paramBlockPos1.distManhattan((Vec3i)paramBlockPos2) == 1) {
/* 290 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 294 */     BlockPos blockPos = paramBlockPos2.subtract((Vec3i)paramBlockPos1);
/* 295 */     Direction direction1 = Direction.fromAxisAndDirection(Direction.Axis.X, (blockPos.getX() < 0) ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE);
/* 296 */     Direction direction2 = Direction.fromAxisAndDirection(Direction.Axis.Y, (blockPos.getY() < 0) ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE);
/* 297 */     Direction direction3 = Direction.fromAxisAndDirection(Direction.Axis.Z, (blockPos.getZ() < 0) ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE);
/*     */     
/* 299 */     if (blockPos.getX() == 0)
/* 300 */       return (isUnobstructed(paramLevelAccessor, paramBlockPos1, direction2) || isUnobstructed(paramLevelAccessor, paramBlockPos1, direction3)); 
/* 301 */     if (blockPos.getY() == 0) {
/* 302 */       return (isUnobstructed(paramLevelAccessor, paramBlockPos1, direction1) || isUnobstructed(paramLevelAccessor, paramBlockPos1, direction3));
/*     */     }
/* 304 */     return (isUnobstructed(paramLevelAccessor, paramBlockPos1, direction1) || isUnobstructed(paramLevelAccessor, paramBlockPos1, direction2));
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isUnobstructed(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, Direction paramDirection) {
/* 309 */     BlockPos blockPos = paramBlockPos.relative(paramDirection);
/* 310 */     return !paramLevelAccessor.getBlockState(blockPos).isFaceSturdy((BlockGetter)paramLevelAccessor, blockPos, paramDirection.getOpposite());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SculkSpreader$ChargeCursor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */