/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SculkSpreader
/*     */ {
/*     */   public static final int MAX_GROWTH_RATE_RADIUS = 24;
/*     */   public static final int MAX_CHARGE = 1000;
/*     */   public static final float MAX_DECAY_FACTOR = 0.5F;
/*     */   private static final int MAX_CURSORS = 32;
/*     */   public static final int SHRIEKER_PLACEMENT_RATE = 11;
/*     */   public static final int MAX_CURSOR_DISTANCE = 1024;
/*     */   final boolean isWorldGeneration;
/*     */   private final TagKey<Block> replaceableBlocks;
/*     */   private final int growthSpawnCost;
/*     */   private final int noGrowthRadius;
/*     */   private final int chargeDecayRate;
/*     */   private final int additionalDecayRate;
/*  61 */   private List<ChargeCursor> cursors = new ArrayList<>();
/*     */   
/*     */   public SculkSpreader(boolean paramBoolean, TagKey<Block> paramTagKey, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  64 */     this.isWorldGeneration = paramBoolean;
/*  65 */     this.replaceableBlocks = paramTagKey;
/*  66 */     this.growthSpawnCost = paramInt1;
/*  67 */     this.noGrowthRadius = paramInt2;
/*  68 */     this.chargeDecayRate = paramInt3;
/*  69 */     this.additionalDecayRate = paramInt4;
/*     */   }
/*     */   
/*     */   public static SculkSpreader createLevelSpreader() {
/*  73 */     return new SculkSpreader(false, BlockTags.SCULK_REPLACEABLE, 10, 4, 10, 5);
/*     */   }
/*     */   
/*     */   public static SculkSpreader createWorldGenSpreader() {
/*  77 */     return new SculkSpreader(true, BlockTags.SCULK_REPLACEABLE_WORLD_GEN, 50, 1, 5, 10);
/*     */   }
/*     */   
/*     */   public TagKey<Block> replaceableBlocks() {
/*  81 */     return this.replaceableBlocks;
/*     */   }
/*     */   
/*     */   public int growthSpawnCost() {
/*  85 */     return this.growthSpawnCost;
/*     */   }
/*     */   
/*     */   public int noGrowthRadius() {
/*  89 */     return this.noGrowthRadius;
/*     */   }
/*     */   
/*     */   public int chargeDecayRate() {
/*  93 */     return this.chargeDecayRate;
/*     */   }
/*     */   
/*     */   public int additionalDecayRate() {
/*  97 */     return this.additionalDecayRate;
/*     */   }
/*     */   
/*     */   public boolean isWorldGeneration() {
/* 101 */     return this.isWorldGeneration;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public List<ChargeCursor> getCursors() {
/* 106 */     return this.cursors;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 110 */     this.cursors.clear();
/*     */   }
/*     */   
/*     */   public void load(ValueInput paramValueInput) {
/* 114 */     this.cursors.clear();
/* 115 */     ((List)paramValueInput.read("cursors", ChargeCursor.CODEC.sizeLimitedListOf(32)).orElse(List.of()))
/* 116 */       .forEach(this::addCursor);
/*     */   }
/*     */   
/*     */   public void save(ValueOutput paramValueOutput) {
/* 120 */     paramValueOutput.store("cursors", ChargeCursor.CODEC.listOf(), this.cursors);
/*     */     
/* 122 */     if (SharedConstants.DEBUG_SCULK_CATALYST) {
/* 123 */       int i = ((Integer)getCursors().stream().map(ChargeCursor::getCharge).reduce(Integer.valueOf(0), Integer::sum)).intValue();
/* 124 */       int j = ((Integer)getCursors().stream().map(paramChargeCursor -> Integer.valueOf(1)).reduce(Integer.valueOf(0), Integer::sum)).intValue();
/* 125 */       int k = ((Integer)getCursors().stream().map(ChargeCursor::getCharge).reduce(Integer.valueOf(0), Math::max)).intValue();
/* 126 */       paramValueOutput.putInt("stats.total", i);
/* 127 */       paramValueOutput.putInt("stats.count", j);
/* 128 */       paramValueOutput.putInt("stats.max", k);
/* 129 */       paramValueOutput.putInt("stats.avg", i / (j + 1));
/*     */     } 
/*     */   }
/*     */   public static class ChargeCursor { private static final ObjectArrayList<Vec3i> NON_CORNER_NEIGHBOURS; public static final int MAX_CURSOR_DECAY_DELAY = 1;
/*     */     static {
/* 134 */       NON_CORNER_NEIGHBOURS = (ObjectArrayList<Vec3i>)Util.make(new ObjectArrayList(18), param1ObjectArrayList -> {
/*     */             Objects.requireNonNull(param1ObjectArrayList);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             BlockPos.betweenClosedStream(new BlockPos(-1, -1, -1), new BlockPos(1, 1, 1)).filter(()).map(BlockPos::immutable).forEach(param1ObjectArrayList::add);
/*     */           });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 151 */       DIRECTION_SET = Direction.CODEC.listOf().xmap(param1List -> Sets.newEnumSet(param1List, Direction.class), Lists::newArrayList);
/*     */       
/* 153 */       CODEC = RecordCodecBuilder.create(param1Instance -> param1Instance.group((App)BlockPos.CODEC.fieldOf("pos").forGetter(ChargeCursor::getPos), (App)Codec.intRange(0, 1000).fieldOf("charge").orElse(Integer.valueOf(0)).forGetter(ChargeCursor::getCharge), (App)Codec.intRange(0, 1).fieldOf("decay_delay").orElse(Integer.valueOf(1)).forGetter(ChargeCursor::getDecayDelay), (App)Codec.intRange(0, 2147483647).fieldOf("update_delay").orElse(Integer.valueOf(0)).forGetter(()), (App)DIRECTION_SET.lenientOptionalFieldOf("facings").forGetter(())).apply((Applicative)param1Instance, ChargeCursor::new));
/*     */     }
/*     */     private BlockPos pos; int charge; private int updateDelay;
/*     */     private int decayDelay;
/*     */     private Set<Direction> facings;
/*     */     private static final Codec<Set<Direction>> DIRECTION_SET;
/*     */     public static final Codec<ChargeCursor> CODEC;
/*     */     
/*     */     private ChargeCursor(BlockPos param1BlockPos, int param1Int1, int param1Int2, int param1Int3, Optional<Set<Direction>> param1Optional) {
/* 162 */       this.pos = param1BlockPos;
/* 163 */       this.charge = param1Int1;
/* 164 */       this.decayDelay = param1Int2;
/* 165 */       this.updateDelay = param1Int3;
/* 166 */       this.facings = param1Optional.orElse(null);
/*     */     }
/*     */     
/*     */     public ChargeCursor(BlockPos param1BlockPos, int param1Int) {
/* 170 */       this(param1BlockPos, param1Int, 1, 0, Optional.empty());
/*     */     }
/*     */     
/*     */     public BlockPos getPos() {
/* 174 */       return this.pos;
/*     */     }
/*     */     
/*     */     boolean isPosUnreasonable(BlockPos param1BlockPos) {
/* 178 */       return (this.pos.distChessboard((Vec3i)param1BlockPos) > 1024);
/*     */     }
/*     */     
/*     */     public int getCharge() {
/* 182 */       return this.charge;
/*     */     }
/*     */     
/*     */     public int getDecayDelay() {
/* 186 */       return this.decayDelay;
/*     */     }
/*     */     
/*     */     public Set<Direction> getFacingData() {
/* 190 */       return this.facings;
/*     */     }
/*     */     
/*     */     private boolean shouldUpdate(LevelAccessor param1LevelAccessor, BlockPos param1BlockPos, boolean param1Boolean) {
/* 194 */       if (this.charge <= 0) {
/* 195 */         return false;
/*     */       }
/* 197 */       if (param1Boolean) {
/* 198 */         return true;
/*     */       }
/* 200 */       if (param1LevelAccessor instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)param1LevelAccessor;
/* 201 */         return serverLevel.shouldTickBlocksAt(param1BlockPos); }
/*     */       
/* 203 */       return false;
/*     */     }
/*     */     
/*     */     public void update(LevelAccessor param1LevelAccessor, BlockPos param1BlockPos, RandomSource param1RandomSource, SculkSpreader param1SculkSpreader, boolean param1Boolean) {
/* 207 */       if (!shouldUpdate(param1LevelAccessor, param1BlockPos, param1SculkSpreader.isWorldGeneration)) {
/*     */         return;
/*     */       }
/*     */       
/* 211 */       if (this.updateDelay > 0) {
/* 212 */         this.updateDelay--;
/*     */         
/*     */         return;
/*     */       } 
/* 216 */       BlockState blockState = param1LevelAccessor.getBlockState(this.pos);
/* 217 */       SculkBehaviour sculkBehaviour = getBlockBehaviour(blockState);
/*     */ 
/*     */       
/* 220 */       if (param1Boolean && sculkBehaviour.attemptSpreadVein(param1LevelAccessor, this.pos, blockState, this.facings, param1SculkSpreader.isWorldGeneration())) {
/* 221 */         if (sculkBehaviour.canChangeBlockStateOnSpread()) {
/* 222 */           blockState = param1LevelAccessor.getBlockState(this.pos);
/* 223 */           sculkBehaviour = getBlockBehaviour(blockState);
/*     */         } 
/* 225 */         param1LevelAccessor.playSound(null, this.pos, SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */       } 
/*     */ 
/*     */       
/* 229 */       this.charge = sculkBehaviour.attemptUseCharge(this, param1LevelAccessor, param1BlockPos, param1RandomSource, param1SculkSpreader, param1Boolean);
/*     */       
/* 231 */       if (this.charge <= 0) {
/* 232 */         sculkBehaviour.onDischarged(param1LevelAccessor, blockState, this.pos, param1RandomSource);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 237 */       BlockPos blockPos = getValidMovementPos(param1LevelAccessor, this.pos, param1RandomSource);
/* 238 */       if (blockPos != null) {
/* 239 */         sculkBehaviour.onDischarged(param1LevelAccessor, blockState, this.pos, param1RandomSource);
/* 240 */         this.pos = blockPos.immutable();
/* 241 */         if (param1SculkSpreader.isWorldGeneration() && !this.pos.closerThan(new Vec3i(param1BlockPos.getX(), this.pos.getY(), param1BlockPos.getZ()), 15.0D)) {
/* 242 */           this.charge = 0;
/*     */           return;
/*     */         } 
/* 245 */         blockState = param1LevelAccessor.getBlockState(blockPos);
/*     */       } 
/*     */       
/* 248 */       if (blockState.getBlock() instanceof SculkBehaviour) {
/* 249 */         this.facings = MultifaceBlock.availableFaces(blockState);
/*     */       }
/* 251 */       this.decayDelay = sculkBehaviour.updateDecayDelay(this.decayDelay);
/* 252 */       this.updateDelay = sculkBehaviour.getSculkSpreadDelay();
/*     */     }
/*     */     
/*     */     void mergeWith(ChargeCursor param1ChargeCursor) {
/* 256 */       this.charge += param1ChargeCursor.charge;
/* 257 */       param1ChargeCursor.charge = 0;
/* 258 */       this.updateDelay = Math.min(this.updateDelay, param1ChargeCursor.updateDelay);
/*     */     }
/*     */     
/*     */     private static SculkBehaviour getBlockBehaviour(BlockState param1BlockState) {
/* 262 */       Block block = param1BlockState.getBlock(); SculkBehaviour sculkBehaviour = (SculkBehaviour)block; return (block instanceof SculkBehaviour) ? sculkBehaviour : SculkBehaviour.DEFAULT;
/*     */     }
/*     */     
/*     */     private static List<Vec3i> getRandomizedNonCornerNeighbourOffsets(RandomSource param1RandomSource) {
/* 266 */       return Util.shuffledCopy(NON_CORNER_NEIGHBOURS, param1RandomSource);
/*     */     }
/*     */     
/*     */     private static BlockPos getValidMovementPos(LevelAccessor param1LevelAccessor, BlockPos param1BlockPos, RandomSource param1RandomSource) {
/* 270 */       BlockPos.MutableBlockPos mutableBlockPos1 = param1BlockPos.mutable();
/* 271 */       BlockPos.MutableBlockPos mutableBlockPos2 = param1BlockPos.mutable();
/*     */       
/* 273 */       for (Vec3i vec3i : getRandomizedNonCornerNeighbourOffsets(param1RandomSource)) {
/* 274 */         mutableBlockPos2.setWithOffset((Vec3i)param1BlockPos, vec3i);
/* 275 */         BlockState blockState = param1LevelAccessor.getBlockState((BlockPos)mutableBlockPos2);
/*     */         
/* 277 */         if (blockState.getBlock() instanceof SculkBehaviour && isMovementUnobstructed(param1LevelAccessor, param1BlockPos, (BlockPos)mutableBlockPos2)) {
/* 278 */           mutableBlockPos1.set((Vec3i)mutableBlockPos2);
/*     */           
/* 280 */           if (SculkVeinBlock.hasSubstrateAccess(param1LevelAccessor, blockState, (BlockPos)mutableBlockPos2)) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/* 285 */       return mutableBlockPos1.equals(param1BlockPos) ? null : (BlockPos)mutableBlockPos1;
/*     */     }
/*     */     
/*     */     private static boolean isMovementUnobstructed(LevelAccessor param1LevelAccessor, BlockPos param1BlockPos1, BlockPos param1BlockPos2) {
/* 289 */       if (param1BlockPos1.distManhattan((Vec3i)param1BlockPos2) == 1) {
/* 290 */         return true;
/*     */       }
/*     */ 
/*     */       
/* 294 */       BlockPos blockPos = param1BlockPos2.subtract((Vec3i)param1BlockPos1);
/* 295 */       Direction direction1 = Direction.fromAxisAndDirection(Direction.Axis.X, (blockPos.getX() < 0) ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE);
/* 296 */       Direction direction2 = Direction.fromAxisAndDirection(Direction.Axis.Y, (blockPos.getY() < 0) ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE);
/* 297 */       Direction direction3 = Direction.fromAxisAndDirection(Direction.Axis.Z, (blockPos.getZ() < 0) ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE);
/*     */       
/* 299 */       if (blockPos.getX() == 0)
/* 300 */         return (isUnobstructed(param1LevelAccessor, param1BlockPos1, direction2) || isUnobstructed(param1LevelAccessor, param1BlockPos1, direction3)); 
/* 301 */       if (blockPos.getY() == 0) {
/* 302 */         return (isUnobstructed(param1LevelAccessor, param1BlockPos1, direction1) || isUnobstructed(param1LevelAccessor, param1BlockPos1, direction3));
/*     */       }
/* 304 */       return (isUnobstructed(param1LevelAccessor, param1BlockPos1, direction1) || isUnobstructed(param1LevelAccessor, param1BlockPos1, direction2));
/*     */     }
/*     */ 
/*     */     
/*     */     private static boolean isUnobstructed(LevelAccessor param1LevelAccessor, BlockPos param1BlockPos, Direction param1Direction) {
/* 309 */       BlockPos blockPos = param1BlockPos.relative(param1Direction);
/* 310 */       return !param1LevelAccessor.getBlockState(blockPos).isFaceSturdy((BlockGetter)param1LevelAccessor, blockPos, param1Direction.getOpposite());
/*     */     } }
/*     */ 
/*     */   
/*     */   public void addCursors(BlockPos paramBlockPos, int paramInt) {
/* 315 */     while (paramInt > 0) {
/* 316 */       int i = Math.min(paramInt, 1000);
/* 317 */       addCursor(new ChargeCursor(paramBlockPos, i));
/* 318 */       paramInt -= i;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addCursor(ChargeCursor paramChargeCursor) {
/* 323 */     if (this.cursors.size() >= 32) {
/*     */       return;
/*     */     }
/* 326 */     this.cursors.add(paramChargeCursor);
/*     */   }
/*     */   
/*     */   public void updateCursors(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, RandomSource paramRandomSource, boolean paramBoolean) {
/* 330 */     if (this.cursors.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 334 */     ArrayList<ChargeCursor> arrayList = new ArrayList();
/* 335 */     HashMap<Object, Object> hashMap = new HashMap<>();
/* 336 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/*     */     
/* 338 */     for (ChargeCursor chargeCursor1 : this.cursors) {
/* 339 */       if (chargeCursor1.isPosUnreasonable(paramBlockPos)) {
/*     */         continue;
/*     */       }
/* 342 */       chargeCursor1.update(paramLevelAccessor, paramBlockPos, paramRandomSource, this, paramBoolean);
/*     */       
/* 344 */       if (chargeCursor1.charge <= 0) {
/* 345 */         paramLevelAccessor.levelEvent(3006, chargeCursor1.getPos(), 0);
/*     */         
/*     */         continue;
/*     */       } 
/* 349 */       BlockPos blockPos = chargeCursor1.getPos();
/* 350 */       object2IntOpenHashMap.computeInt(blockPos, (paramBlockPos, paramInteger) -> Integer.valueOf(((paramInteger == null) ? 0 : paramInteger.intValue()) + paramChargeCursor.charge));
/*     */       
/* 352 */       ChargeCursor chargeCursor2 = (ChargeCursor)hashMap.get(blockPos);
/* 353 */       if (chargeCursor2 == null) {
/* 354 */         hashMap.put(blockPos, chargeCursor1);
/* 355 */         arrayList.add(chargeCursor1);
/*     */         
/*     */         continue;
/*     */       } 
/* 359 */       if (!isWorldGeneration() && chargeCursor1.charge + chargeCursor2.charge <= 1000) {
/* 360 */         chargeCursor2.mergeWith(chargeCursor1);
/*     */         
/*     */         continue;
/*     */       } 
/* 364 */       arrayList.add(chargeCursor1);
/*     */       
/* 366 */       if (chargeCursor1.charge < chargeCursor2.charge) {
/* 367 */         hashMap.put(blockPos, chargeCursor1);
/*     */       }
/*     */     } 
/*     */     
/* 371 */     for (ObjectIterator<Object2IntMap.Entry> objectIterator = object2IntOpenHashMap.object2IntEntrySet().iterator(); objectIterator.hasNext(); ) { Object2IntMap.Entry entry = objectIterator.next();
/* 372 */       BlockPos blockPos = (BlockPos)entry.getKey();
/* 373 */       int i = entry.getIntValue();
/*     */       
/* 375 */       ChargeCursor chargeCursor = (ChargeCursor)hashMap.get(blockPos);
/* 376 */       Set<Direction> set = (chargeCursor == null) ? null : chargeCursor.getFacingData();
/*     */       
/* 378 */       if (i > 0 && set != null) {
/* 379 */         int j = (int)(Math.log1p(i) / 2.299999952316284D) + 1;
/* 380 */         int k = (j << 6) + MultifaceBlock.pack(set);
/* 381 */         paramLevelAccessor.levelEvent(3006, blockPos, k);
/*     */       }  }
/*     */     
/* 384 */     this.cursors = arrayList;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SculkSpreader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */