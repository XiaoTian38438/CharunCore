/*     */ package net.minecraft.world.level.block.entity;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.features.EndFeatures;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TheEndGatewayBlockEntity extends TheEndPortalBlockEntity {
/*  34 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int SPAWN_TIME = 200;
/*     */   
/*     */   private static final int COOLDOWN_TIME = 40;
/*     */   private static final int ATTENTION_INTERVAL = 2400;
/*     */   private static final int EVENT_COOLDOWN = 1;
/*     */   private static final int GATEWAY_HEIGHT_ABOVE_SURFACE = 10;
/*     */   private static final long DEFAULT_AGE = 0L;
/*     */   private static final boolean DEFAULT_EXACT_TELEPORT = false;
/*  44 */   private long age = 0L;
/*     */   private int teleportCooldown;
/*     */   private BlockPos exitPortal;
/*     */   private boolean exactTeleport = false;
/*     */   
/*     */   public TheEndGatewayBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  50 */     super(BlockEntityType.END_GATEWAY, paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput paramValueOutput) {
/*  55 */     super.saveAdditional(paramValueOutput);
/*  56 */     paramValueOutput.putLong("Age", this.age);
/*  57 */     paramValueOutput.storeNullable("exit_portal", BlockPos.CODEC, this.exitPortal);
/*  58 */     if (this.exactTeleport) {
/*  59 */       paramValueOutput.putBoolean("ExactTeleport", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput paramValueInput) {
/*  65 */     super.loadAdditional(paramValueInput);
/*  66 */     this.age = paramValueInput.getLongOr("Age", 0L);
/*  67 */     this
/*     */       
/*  69 */       .exitPortal = paramValueInput.read("exit_portal", BlockPos.CODEC).filter(Level::isInSpawnableBounds).orElse(null);
/*  70 */     this.exactTeleport = paramValueInput.getBooleanOr("ExactTeleport", false);
/*     */   }
/*     */   
/*     */   public static void beamAnimationTick(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, TheEndGatewayBlockEntity paramTheEndGatewayBlockEntity) {
/*  74 */     paramTheEndGatewayBlockEntity.age++;
/*     */     
/*  76 */     if (paramTheEndGatewayBlockEntity.isCoolingDown()) {
/*  77 */       paramTheEndGatewayBlockEntity.teleportCooldown--;
/*     */     }
/*     */   }
/*     */   
/*     */   public static void portalTick(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, TheEndGatewayBlockEntity paramTheEndGatewayBlockEntity) {
/*  82 */     boolean bool1 = paramTheEndGatewayBlockEntity.isSpawning();
/*  83 */     boolean bool2 = paramTheEndGatewayBlockEntity.isCoolingDown();
/*  84 */     paramTheEndGatewayBlockEntity.age++;
/*     */     
/*  86 */     if (bool2) {
/*  87 */       paramTheEndGatewayBlockEntity.teleportCooldown--;
/*     */     }
/*  89 */     else if (paramTheEndGatewayBlockEntity.age % 2400L == 0L) {
/*  90 */       triggerCooldown(paramLevel, paramBlockPos, paramBlockState, paramTheEndGatewayBlockEntity);
/*     */     } 
/*     */ 
/*     */     
/*  94 */     if (bool1 != paramTheEndGatewayBlockEntity.isSpawning() || bool2 != paramTheEndGatewayBlockEntity.isCoolingDown()) {
/*  95 */       setChanged(paramLevel, paramBlockPos, paramBlockState);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isSpawning() {
/* 100 */     return (this.age < 200L);
/*     */   }
/*     */   
/*     */   public boolean isCoolingDown() {
/* 104 */     return (this.teleportCooldown > 0);
/*     */   }
/*     */   
/*     */   public float getSpawnPercent(float paramFloat) {
/* 108 */     return Mth.clamp(((float)this.age + paramFloat) / 200.0F, 0.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public float getCooldownPercent(float paramFloat) {
/* 112 */     return 1.0F - Mth.clamp((this.teleportCooldown - paramFloat) / 40.0F, 0.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/* 117 */     return ClientboundBlockEntityDataPacket.create(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider paramProvider) {
/* 122 */     return saveCustomOnly(paramProvider);
/*     */   }
/*     */   
/*     */   public static void triggerCooldown(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, TheEndGatewayBlockEntity paramTheEndGatewayBlockEntity) {
/* 126 */     if (!paramLevel.isClientSide()) {
/* 127 */       paramTheEndGatewayBlockEntity.teleportCooldown = 40;
/* 128 */       paramLevel.blockEvent(paramBlockPos, paramBlockState.getBlock(), 1, 0);
/* 129 */       setChanged(paramLevel, paramBlockPos, paramBlockState);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean triggerEvent(int paramInt1, int paramInt2) {
/* 135 */     if (paramInt1 == 1) {
/* 136 */       this.teleportCooldown = 40;
/* 137 */       return true;
/*     */     } 
/*     */     
/* 140 */     return super.triggerEvent(paramInt1, paramInt2);
/*     */   }
/*     */   
/*     */   public Vec3 getPortalPosition(ServerLevel paramServerLevel, BlockPos paramBlockPos) {
/* 144 */     if (this.exitPortal == null && paramServerLevel.dimension() == Level.END) {
/* 145 */       BlockPos blockPos = findOrCreateValidTeleportPos(paramServerLevel, paramBlockPos);
/* 146 */       blockPos = blockPos.above(10);
/* 147 */       LOGGER.debug("Creating portal at {}", blockPos);
/* 148 */       spawnGatewayPortal(paramServerLevel, blockPos, EndGatewayConfiguration.knownExit(paramBlockPos, false));
/* 149 */       setExitPosition(blockPos, this.exactTeleport);
/*     */     } 
/*     */     
/* 152 */     if (this.exitPortal != null) {
/* 153 */       BlockPos blockPos = this.exactTeleport ? this.exitPortal : findExitPosition((Level)paramServerLevel, this.exitPortal);
/* 154 */       return blockPos.getBottomCenter();
/*     */     } 
/* 156 */     return null;
/*     */   }
/*     */   
/*     */   private static BlockPos findExitPosition(Level paramLevel, BlockPos paramBlockPos) {
/* 160 */     BlockPos blockPos = findTallestBlock((BlockGetter)paramLevel, paramBlockPos.offset(0, 2, 0), 5, false);
/* 161 */     LOGGER.debug("Best exit position for portal at {} is {}", paramBlockPos, blockPos);
/* 162 */     return blockPos.above();
/*     */   }
/*     */   
/*     */   private static BlockPos findOrCreateValidTeleportPos(ServerLevel paramServerLevel, BlockPos paramBlockPos) {
/* 166 */     Vec3 vec3 = findExitPortalXZPosTentative(paramServerLevel, paramBlockPos);
/*     */     
/* 168 */     LevelChunk levelChunk = getChunk((Level)paramServerLevel, vec3);
/*     */     
/* 170 */     BlockPos blockPos = findValidSpawnInChunk(levelChunk);
/*     */     
/* 172 */     if (blockPos == null) {
/* 173 */       BlockPos blockPos1 = BlockPos.containing(vec3.x + 0.5D, 75.0D, vec3.z + 0.5D);
/* 174 */       LOGGER.debug("Failed to find a suitable block to teleport to, spawning an island on {}", blockPos1);
/* 175 */       paramServerLevel.registryAccess().lookup(Registries.CONFIGURED_FEATURE)
/* 176 */         .flatMap(paramRegistry -> paramRegistry.get(EndFeatures.END_ISLAND))
/* 177 */         .ifPresent(paramReference -> ((ConfiguredFeature)paramReference.value()).place((WorldGenLevel)paramServerLevel, paramServerLevel.getChunkSource().getGenerator(), RandomSource.create(paramBlockPos.asLong()), paramBlockPos));
/* 178 */       blockPos = blockPos1;
/*     */     } else {
/* 180 */       LOGGER.debug("Found suitable block to teleport to: {}", blockPos);
/*     */     } 
/*     */     
/* 183 */     return findTallestBlock((BlockGetter)paramServerLevel, blockPos, 16, true);
/*     */   }
/*     */   
/*     */   private static Vec3 findExitPortalXZPosTentative(ServerLevel paramServerLevel, BlockPos paramBlockPos) {
/* 187 */     Vec3 vec31 = (new Vec3(paramBlockPos.getX(), 0.0D, paramBlockPos.getZ())).normalize();
/* 188 */     char c = 'Ѐ';
/* 189 */     Vec3 vec32 = vec31.scale(1024.0D);
/*     */     
/* 191 */     byte b = 16;
/* 192 */     while (!isChunkEmpty(paramServerLevel, vec32) && b-- > 0) {
/* 193 */       LOGGER.debug("Skipping backwards past nonempty chunk at {}", vec32);
/* 194 */       vec32 = vec32.add(vec31.scale(-16.0D));
/*     */     } 
/*     */     
/* 197 */     b = 16;
/* 198 */     while (isChunkEmpty(paramServerLevel, vec32) && b-- > 0) {
/* 199 */       LOGGER.debug("Skipping forward past empty chunk at {}", vec32);
/* 200 */       vec32 = vec32.add(vec31.scale(16.0D));
/*     */     } 
/* 202 */     LOGGER.debug("Found chunk at {}", vec32);
/* 203 */     return vec32;
/*     */   }
/*     */   
/*     */   private static boolean isChunkEmpty(ServerLevel paramServerLevel, Vec3 paramVec3) {
/* 207 */     return (getChunk((Level)paramServerLevel, paramVec3).getHighestFilledSectionIndex() == -1);
/*     */   }
/*     */   
/*     */   private static BlockPos findTallestBlock(BlockGetter paramBlockGetter, BlockPos paramBlockPos, int paramInt, boolean paramBoolean) {
/* 211 */     BlockPos blockPos = null;
/*     */     
/* 213 */     for (int i = -paramInt; i <= paramInt; i++) {
/* 214 */       for (int j = -paramInt; j <= paramInt; j++) {
/* 215 */         if (i != 0 || j != 0 || paramBoolean)
/*     */         {
/*     */ 
/*     */           
/* 219 */           for (int k = paramBlockGetter.getMaxY(); k > ((blockPos == null) ? paramBlockGetter.getMinY() : blockPos.getY()); k--) {
/* 220 */             BlockPos blockPos1 = new BlockPos(paramBlockPos.getX() + i, k, paramBlockPos.getZ() + j);
/* 221 */             BlockState blockState = paramBlockGetter.getBlockState(blockPos1);
/* 222 */             if (blockState.isCollisionShapeFullBlock(paramBlockGetter, blockPos1) && (paramBoolean || !blockState.is(Blocks.BEDROCK))) {
/* 223 */               blockPos = blockPos1;
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 230 */     return (blockPos == null) ? paramBlockPos : blockPos;
/*     */   }
/*     */   
/*     */   private static LevelChunk getChunk(Level paramLevel, Vec3 paramVec3) {
/* 234 */     return paramLevel.getChunk(Mth.floor(paramVec3.x / 16.0D), Mth.floor(paramVec3.z / 16.0D));
/*     */   }
/*     */   
/*     */   private static BlockPos findValidSpawnInChunk(LevelChunk paramLevelChunk) {
/* 238 */     ChunkPos chunkPos = paramLevelChunk.getPos();
/* 239 */     BlockPos blockPos1 = new BlockPos(chunkPos.getMinBlockX(), 30, chunkPos.getMinBlockZ());
/* 240 */     int i = paramLevelChunk.getHighestSectionPosition() + 16 - 1;
/* 241 */     BlockPos blockPos2 = new BlockPos(chunkPos.getMaxBlockX(), i, chunkPos.getMaxBlockZ());
/* 242 */     BlockPos blockPos3 = null;
/* 243 */     double d = 0.0D;
/*     */ 
/*     */     
/* 246 */     for (BlockPos blockPos4 : BlockPos.betweenClosed(blockPos1, blockPos2)) {
/* 247 */       BlockState blockState = paramLevelChunk.getBlockState(blockPos4);
/*     */       
/* 249 */       BlockPos blockPos5 = blockPos4.above();
/* 250 */       BlockPos blockPos6 = blockPos4.above(2);
/* 251 */       if (blockState.is(Blocks.END_STONE) && !paramLevelChunk.getBlockState(blockPos5).isCollisionShapeFullBlock((BlockGetter)paramLevelChunk, blockPos5) && !paramLevelChunk.getBlockState(blockPos6).isCollisionShapeFullBlock((BlockGetter)paramLevelChunk, blockPos6)) {
/* 252 */         double d1 = blockPos4.distToCenterSqr(0.0D, 0.0D, 0.0D);
/* 253 */         if (blockPos3 == null || d1 < d) {
/* 254 */           blockPos3 = blockPos4;
/* 255 */           d = d1;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 260 */     return blockPos3;
/*     */   }
/*     */   
/*     */   private static void spawnGatewayPortal(ServerLevel paramServerLevel, BlockPos paramBlockPos, EndGatewayConfiguration paramEndGatewayConfiguration) {
/* 264 */     Feature.END_GATEWAY.place((FeatureConfiguration)paramEndGatewayConfiguration, (WorldGenLevel)paramServerLevel, paramServerLevel.getChunkSource().getGenerator(), RandomSource.create(), paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRenderFace(Direction paramDirection) {
/* 269 */     return Block.shouldRenderFace(getBlockState(), this.level.getBlockState(getBlockPos().relative(paramDirection)), paramDirection);
/*     */   }
/*     */   
/*     */   public int getParticleAmount() {
/* 273 */     int i = 0;
/* 274 */     for (Direction direction : Direction.values()) {
/* 275 */       i += shouldRenderFace(direction) ? 1 : 0;
/*     */     }
/* 277 */     return i;
/*     */   }
/*     */   
/*     */   public void setExitPosition(BlockPos paramBlockPos, boolean paramBoolean) {
/* 281 */     this.exactTeleport = paramBoolean;
/* 282 */     this.exitPortal = paramBlockPos;
/* 283 */     setChanged();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\TheEndGatewayBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */