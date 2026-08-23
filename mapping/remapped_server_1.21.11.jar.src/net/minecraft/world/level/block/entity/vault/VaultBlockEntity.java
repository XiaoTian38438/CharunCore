/*     */ package net.minecraft.world.level.block.entity.vault;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.VaultBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class VaultBlockEntity extends BlockEntity {
/*     */   private final VaultServerData serverData;
/*     */   private final VaultSharedData sharedData;
/*     */   
/*     */   public VaultBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  51 */     super(BlockEntityType.VAULT, paramBlockPos, paramBlockState);
/*  52 */     this.serverData = new VaultServerData();
/*  53 */     this.sharedData = new VaultSharedData();
/*  54 */     this.clientData = new VaultClientData();
/*  55 */     this.config = VaultConfig.DEFAULT;
/*     */   }
/*     */   private final VaultClientData clientData; private VaultConfig config;
/*     */   
/*     */   public Packet<ClientGamePacketListener> getUpdatePacket() {
/*  60 */     return (Packet<ClientGamePacketListener>)ClientboundBlockEntityDataPacket.create(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider paramProvider) {
/*  65 */     return (CompoundTag)Util.make(new CompoundTag(), paramCompoundTag -> paramCompoundTag.store("shared_data", VaultSharedData.CODEC, (DynamicOps)paramProvider.createSerializationContext((DynamicOps)NbtOps.INSTANCE), this.sharedData));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput paramValueOutput) {
/*  70 */     super.saveAdditional(paramValueOutput);
/*     */     
/*  72 */     paramValueOutput.store("config", VaultConfig.CODEC, this.config);
/*  73 */     paramValueOutput.store("shared_data", VaultSharedData.CODEC, this.sharedData);
/*  74 */     paramValueOutput.store("server_data", VaultServerData.CODEC, this.serverData);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput paramValueInput) {
/*  79 */     super.loadAdditional(paramValueInput);
/*     */     
/*  81 */     Objects.requireNonNull(this.serverData); paramValueInput.read("server_data", VaultServerData.CODEC).ifPresent(this.serverData::set);
/*  82 */     this.config = paramValueInput.read("config", VaultConfig.CODEC).orElse(VaultConfig.DEFAULT);
/*  83 */     Objects.requireNonNull(this.sharedData); paramValueInput.read("shared_data", VaultSharedData.CODEC).ifPresent(this.sharedData::set);
/*     */   }
/*     */   
/*     */   public VaultServerData getServerData() {
/*  87 */     return (this.level == null || this.level.isClientSide()) ? null : this.serverData;
/*     */   }
/*     */   
/*     */   public VaultSharedData getSharedData() {
/*  91 */     return this.sharedData;
/*     */   }
/*     */   
/*     */   public VaultClientData getClientData() {
/*  95 */     return this.clientData;
/*     */   }
/*     */   
/*     */   public VaultConfig getConfig() {
/*  99 */     return this.config;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void setConfig(VaultConfig paramVaultConfig) {
/* 104 */     this.config = paramVaultConfig;
/*     */   }
/*     */   
/*     */   public static final class Server {
/*     */     private static final int UNLOCKING_DELAY_TICKS = 14;
/*     */     private static final int DISPLAY_CYCLE_TICK_RATE = 20;
/*     */     private static final int INSERT_FAIL_SOUND_BUFFER_TICKS = 15;
/*     */     
/*     */     public static void tick(ServerLevel param1ServerLevel, BlockPos param1BlockPos, BlockState param1BlockState, VaultConfig param1VaultConfig, VaultServerData param1VaultServerData, VaultSharedData param1VaultSharedData) {
/* 113 */       VaultState vaultState = (VaultState)param1BlockState.getValue(VaultBlock.STATE);
/*     */       
/* 115 */       if (shouldCycleDisplayItem(param1ServerLevel.getGameTime(), vaultState)) {
/* 116 */         cycleDisplayItemFromLootTable(param1ServerLevel, vaultState, param1VaultConfig, param1VaultSharedData, param1BlockPos);
/*     */       }
/*     */       
/* 119 */       BlockState blockState = param1BlockState;
/* 120 */       if (param1ServerLevel.getGameTime() >= param1VaultServerData.stateUpdatingResumesAt()) {
/* 121 */         blockState = (BlockState)blockState.setValue(VaultBlock.STATE, vaultState.tickAndGetNext(param1ServerLevel, param1BlockPos, param1VaultConfig, param1VaultServerData, param1VaultSharedData));
/*     */         
/* 123 */         if (param1BlockState != blockState) {
/* 124 */           setVaultState(param1ServerLevel, param1BlockPos, param1BlockState, blockState, param1VaultConfig, param1VaultSharedData);
/*     */         }
/*     */       } 
/*     */       
/* 128 */       if (param1VaultServerData.isDirty || param1VaultSharedData.isDirty) {
/*     */         
/* 130 */         VaultBlockEntity.setChanged((Level)param1ServerLevel, param1BlockPos, param1BlockState);
/*     */ 
/*     */         
/* 133 */         if (param1VaultSharedData.isDirty) {
/* 134 */           param1ServerLevel.sendBlockUpdated(param1BlockPos, param1BlockState, blockState, 2);
/*     */         }
/* 136 */         param1VaultServerData.isDirty = false;
/* 137 */         param1VaultSharedData.isDirty = false;
/*     */       } 
/*     */     }
/*     */     
/*     */     public static void tryInsertKey(ServerLevel param1ServerLevel, BlockPos param1BlockPos, BlockState param1BlockState, VaultConfig param1VaultConfig, VaultServerData param1VaultServerData, VaultSharedData param1VaultSharedData, Player param1Player, ItemStack param1ItemStack) {
/* 142 */       VaultState vaultState = (VaultState)param1BlockState.getValue(VaultBlock.STATE);
/*     */       
/* 144 */       if (!canEjectReward(param1VaultConfig, vaultState)) {
/*     */         return;
/*     */       }
/*     */       
/* 148 */       if (!isValidToInsert(param1VaultConfig, param1ItemStack)) {
/* 149 */         playInsertFailSound(param1ServerLevel, param1VaultServerData, param1BlockPos, SoundEvents.VAULT_INSERT_ITEM_FAIL);
/*     */         
/*     */         return;
/*     */       } 
/* 153 */       if (param1VaultServerData.hasRewardedPlayer(param1Player)) {
/* 154 */         playInsertFailSound(param1ServerLevel, param1VaultServerData, param1BlockPos, SoundEvents.VAULT_REJECT_REWARDED_PLAYER);
/*     */         
/*     */         return;
/*     */       } 
/* 158 */       List<ItemStack> list = resolveItemsToEject(param1ServerLevel, param1VaultConfig, param1BlockPos, param1Player, param1ItemStack);
/* 159 */       if (list.isEmpty()) {
/*     */         return;
/*     */       }
/*     */       
/* 163 */       param1Player.awardStat(Stats.ITEM_USED.get(param1ItemStack.getItem()));
/* 164 */       param1ItemStack.consume(param1VaultConfig.keyItem().getCount(), (LivingEntity)param1Player);
/*     */       
/* 166 */       unlock(param1ServerLevel, param1BlockState, param1BlockPos, param1VaultConfig, param1VaultServerData, param1VaultSharedData, list);
/* 167 */       param1VaultServerData.addToRewardedPlayers(param1Player);
/* 168 */       param1VaultSharedData.updateConnectedPlayersWithinRange(param1ServerLevel, param1BlockPos, param1VaultServerData, param1VaultConfig, param1VaultConfig.deactivationRange());
/*     */     }
/*     */     
/*     */     static void setVaultState(ServerLevel param1ServerLevel, BlockPos param1BlockPos, BlockState param1BlockState1, BlockState param1BlockState2, VaultConfig param1VaultConfig, VaultSharedData param1VaultSharedData) {
/* 172 */       VaultState vaultState1 = (VaultState)param1BlockState1.getValue(VaultBlock.STATE);
/* 173 */       VaultState vaultState2 = (VaultState)param1BlockState2.getValue(VaultBlock.STATE);
/*     */       
/* 175 */       param1ServerLevel.setBlock(param1BlockPos, param1BlockState2, 3);
/* 176 */       vaultState1.onTransition(param1ServerLevel, param1BlockPos, vaultState2, param1VaultConfig, param1VaultSharedData, ((Boolean)param1BlockState2.getValue((Property)VaultBlock.OMINOUS)).booleanValue());
/*     */     }
/*     */     
/*     */     static void cycleDisplayItemFromLootTable(ServerLevel param1ServerLevel, VaultState param1VaultState, VaultConfig param1VaultConfig, VaultSharedData param1VaultSharedData, BlockPos param1BlockPos) {
/* 180 */       if (!canEjectReward(param1VaultConfig, param1VaultState)) {
/* 181 */         param1VaultSharedData.setDisplayItem(ItemStack.EMPTY);
/*     */         
/*     */         return;
/*     */       } 
/* 185 */       ItemStack itemStack = getRandomDisplayItemFromLootTable(param1ServerLevel, param1BlockPos, param1VaultConfig.overrideLootTableToDisplay().orElse(param1VaultConfig.lootTable()));
/* 186 */       param1VaultSharedData.setDisplayItem(itemStack);
/*     */     }
/*     */     
/*     */     private static ItemStack getRandomDisplayItemFromLootTable(ServerLevel param1ServerLevel, BlockPos param1BlockPos, ResourceKey<LootTable> param1ResourceKey) {
/* 190 */       LootTable lootTable = param1ServerLevel.getServer().reloadableRegistries().getLootTable(param1ResourceKey);
/*     */ 
/*     */       
/* 193 */       LootParams lootParams = (new LootParams.Builder(param1ServerLevel)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf((Vec3i)param1BlockPos)).create(LootContextParamSets.VAULT);
/* 194 */       ObjectArrayList objectArrayList = lootTable.getRandomItems(lootParams, param1ServerLevel.getRandom());
/*     */       
/* 196 */       if (objectArrayList.isEmpty()) {
/* 197 */         return ItemStack.EMPTY;
/*     */       }
/*     */       
/* 200 */       return (ItemStack)Util.getRandom((List)objectArrayList, param1ServerLevel.getRandom());
/*     */     }
/*     */     
/*     */     private static void unlock(ServerLevel param1ServerLevel, BlockState param1BlockState, BlockPos param1BlockPos, VaultConfig param1VaultConfig, VaultServerData param1VaultServerData, VaultSharedData param1VaultSharedData, List<ItemStack> param1List) {
/* 204 */       param1VaultServerData.setItemsToEject(param1List);
/* 205 */       param1VaultSharedData.setDisplayItem(param1VaultServerData.getNextItemToEject());
/* 206 */       param1VaultServerData.pauseStateUpdatingUntil(param1ServerLevel.getGameTime() + 14L);
/* 207 */       setVaultState(param1ServerLevel, param1BlockPos, param1BlockState, (BlockState)param1BlockState.setValue(VaultBlock.STATE, VaultState.UNLOCKING), param1VaultConfig, param1VaultSharedData);
/*     */     }
/*     */     
/*     */     private static List<ItemStack> resolveItemsToEject(ServerLevel param1ServerLevel, VaultConfig param1VaultConfig, BlockPos param1BlockPos, Player param1Player, ItemStack param1ItemStack) {
/* 211 */       LootTable lootTable = param1ServerLevel.getServer().reloadableRegistries().getLootTable(param1VaultConfig.lootTable());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 217 */       LootParams lootParams = (new LootParams.Builder(param1ServerLevel)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf((Vec3i)param1BlockPos)).withLuck(param1Player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, param1Player).withParameter(LootContextParams.TOOL, param1ItemStack).create(LootContextParamSets.VAULT);
/*     */       
/* 219 */       return (List<ItemStack>)lootTable.getRandomItems(lootParams);
/*     */     }
/*     */     
/*     */     private static boolean canEjectReward(VaultConfig param1VaultConfig, VaultState param1VaultState) {
/* 223 */       return (!param1VaultConfig.keyItem().isEmpty() && param1VaultState != VaultState.INACTIVE);
/*     */     }
/*     */     
/*     */     private static boolean isValidToInsert(VaultConfig param1VaultConfig, ItemStack param1ItemStack) {
/* 227 */       return (ItemStack.isSameItemSameComponents(param1ItemStack, param1VaultConfig.keyItem()) && param1ItemStack.getCount() >= param1VaultConfig.keyItem().getCount());
/*     */     }
/*     */     
/*     */     private static boolean shouldCycleDisplayItem(long param1Long, VaultState param1VaultState) {
/* 231 */       return (param1Long % 20L == 0L && param1VaultState == VaultState.ACTIVE);
/*     */     }
/*     */     
/*     */     private static void playInsertFailSound(ServerLevel param1ServerLevel, VaultServerData param1VaultServerData, BlockPos param1BlockPos, SoundEvent param1SoundEvent) {
/* 235 */       if (param1ServerLevel.getGameTime() >= param1VaultServerData.getLastInsertFailTimestamp() + 15L) {
/* 236 */         param1ServerLevel.playSound(null, param1BlockPos, param1SoundEvent, SoundSource.BLOCKS);
/* 237 */         param1VaultServerData.setLastInsertFailTimestamp(param1ServerLevel.getGameTime());
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Client {
/*     */     private static final int PARTICLE_TICK_RATE = 20;
/*     */     private static final float IDLE_PARTICLE_CHANCE = 0.5F;
/*     */     private static final float AMBIENT_SOUND_CHANCE = 0.02F;
/*     */     private static final int ACTIVATION_PARTICLE_COUNT = 20;
/*     */     private static final int DEACTIVATION_PARTICLE_COUNT = 20;
/*     */     
/*     */     public static void tick(Level param1Level, BlockPos param1BlockPos, BlockState param1BlockState, VaultClientData param1VaultClientData, VaultSharedData param1VaultSharedData) {
/* 250 */       param1VaultClientData.updateDisplayItemSpin();
/*     */       
/* 252 */       if (param1Level.getGameTime() % 20L == 0L) {
/* 253 */         emitConnectionParticlesForNearbyPlayers(param1Level, param1BlockPos, param1BlockState, param1VaultSharedData);
/*     */       }
/*     */       
/* 256 */       emitIdleParticles(param1Level, param1BlockPos, param1VaultSharedData, ((Boolean)param1BlockState.getValue((Property)VaultBlock.OMINOUS)).booleanValue() ? (ParticleOptions)ParticleTypes.SOUL_FIRE_FLAME : (ParticleOptions)ParticleTypes.SMALL_FLAME);
/* 257 */       playIdleSounds(param1Level, param1BlockPos, param1VaultSharedData);
/*     */     }
/*     */     
/*     */     public static void emitActivationParticles(Level param1Level, BlockPos param1BlockPos, BlockState param1BlockState, VaultSharedData param1VaultSharedData, ParticleOptions param1ParticleOptions) {
/* 261 */       emitConnectionParticlesForNearbyPlayers(param1Level, param1BlockPos, param1BlockState, param1VaultSharedData);
/* 262 */       RandomSource randomSource = param1Level.random;
/* 263 */       for (byte b = 0; b < 20; b++) {
/* 264 */         Vec3 vec3 = randomPosInsideCage(param1BlockPos, randomSource);
/* 265 */         param1Level.addParticle((ParticleOptions)ParticleTypes.SMOKE, vec3.x(), vec3.y(), vec3.z(), 0.0D, 0.0D, 0.0D);
/* 266 */         param1Level.addParticle(param1ParticleOptions, vec3.x(), vec3.y(), vec3.z(), 0.0D, 0.0D, 0.0D);
/*     */       } 
/*     */     }
/*     */     
/*     */     public static void emitDeactivationParticles(Level param1Level, BlockPos param1BlockPos, ParticleOptions param1ParticleOptions) {
/* 271 */       RandomSource randomSource = param1Level.random;
/* 272 */       for (byte b = 0; b < 20; b++) {
/* 273 */         Vec3 vec31 = randomPosCenterOfCage(param1BlockPos, randomSource);
/* 274 */         Vec3 vec32 = new Vec3(randomSource.nextGaussian() * 0.02D, randomSource.nextGaussian() * 0.02D, randomSource.nextGaussian() * 0.02D);
/* 275 */         param1Level.addParticle(param1ParticleOptions, vec31.x(), vec31.y(), vec31.z(), vec32.x(), vec32.y(), vec32.z());
/*     */       } 
/*     */     }
/*     */     
/*     */     private static void emitIdleParticles(Level param1Level, BlockPos param1BlockPos, VaultSharedData param1VaultSharedData, ParticleOptions param1ParticleOptions) {
/* 280 */       RandomSource randomSource = param1Level.getRandom();
/* 281 */       if (randomSource.nextFloat() <= 0.5F) {
/* 282 */         Vec3 vec3 = randomPosInsideCage(param1BlockPos, randomSource);
/* 283 */         param1Level.addParticle((ParticleOptions)ParticleTypes.SMOKE, vec3.x(), vec3.y(), vec3.z(), 0.0D, 0.0D, 0.0D);
/* 284 */         if (shouldDisplayActiveEffects(param1VaultSharedData)) {
/* 285 */           param1Level.addParticle(param1ParticleOptions, vec3.x(), vec3.y(), vec3.z(), 0.0D, 0.0D, 0.0D);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     private static void emitConnectionParticlesForPlayer(Level param1Level, Vec3 param1Vec3, Player param1Player) {
/* 291 */       RandomSource randomSource = param1Level.random;
/* 292 */       Vec3 vec3 = param1Vec3.vectorTo(param1Player.position().add(0.0D, (param1Player.getBbHeight() / 2.0F), 0.0D));
/* 293 */       int i = Mth.nextInt(randomSource, 2, 5);
/* 294 */       for (byte b = 0; b < i; b++) {
/* 295 */         Vec3 vec31 = vec3.offsetRandom(randomSource, 1.0F);
/* 296 */         param1Level.addParticle((ParticleOptions)ParticleTypes.VAULT_CONNECTION, param1Vec3.x(), param1Vec3.y(), param1Vec3.z(), vec31.x(), vec31.y(), vec31.z());
/*     */       } 
/*     */     }
/*     */     
/*     */     private static void emitConnectionParticlesForNearbyPlayers(Level param1Level, BlockPos param1BlockPos, BlockState param1BlockState, VaultSharedData param1VaultSharedData) {
/* 301 */       Set<UUID> set = param1VaultSharedData.getConnectedPlayers();
/*     */       
/* 303 */       if (set.isEmpty()) {
/*     */         return;
/*     */       }
/*     */       
/* 307 */       Vec3 vec3 = keyholePos(param1BlockPos, (Direction)param1BlockState.getValue((Property)VaultBlock.FACING));
/*     */       
/* 309 */       for (UUID uUID : set) {
/* 310 */         Player player = param1Level.getPlayerByUUID(uUID);
/* 311 */         if (player == null || !isWithinConnectionRange(param1BlockPos, param1VaultSharedData, player)) {
/*     */           continue;
/*     */         }
/*     */         
/* 315 */         emitConnectionParticlesForPlayer(param1Level, vec3, player);
/*     */       } 
/*     */     }
/*     */     
/*     */     private static boolean isWithinConnectionRange(BlockPos param1BlockPos, VaultSharedData param1VaultSharedData, Player param1Player) {
/* 320 */       return (param1Player.blockPosition().distSqr((Vec3i)param1BlockPos) <= Mth.square(param1VaultSharedData.connectedParticlesRange()));
/*     */     }
/*     */     
/*     */     private static void playIdleSounds(Level param1Level, BlockPos param1BlockPos, VaultSharedData param1VaultSharedData) {
/* 324 */       if (!shouldDisplayActiveEffects(param1VaultSharedData)) {
/*     */         return;
/*     */       }
/*     */       
/* 328 */       RandomSource randomSource = param1Level.getRandom();
/* 329 */       if (randomSource.nextFloat() <= 0.02F) {
/* 330 */         param1Level.playLocalSound(param1BlockPos, SoundEvents.VAULT_AMBIENT, SoundSource.BLOCKS, randomSource.nextFloat() * 0.25F + 0.75F, randomSource.nextFloat() + 0.5F, false);
/*     */       }
/*     */     }
/*     */     
/*     */     public static boolean shouldDisplayActiveEffects(VaultSharedData param1VaultSharedData) {
/* 335 */       return param1VaultSharedData.hasDisplayItem();
/*     */     }
/*     */     
/*     */     private static Vec3 randomPosCenterOfCage(BlockPos param1BlockPos, RandomSource param1RandomSource) {
/* 339 */       return Vec3.atLowerCornerOf((Vec3i)param1BlockPos).add(Mth.nextDouble(param1RandomSource, 0.4D, 0.6D), Mth.nextDouble(param1RandomSource, 0.4D, 0.6D), Mth.nextDouble(param1RandomSource, 0.4D, 0.6D));
/*     */     }
/*     */     
/*     */     private static Vec3 randomPosInsideCage(BlockPos param1BlockPos, RandomSource param1RandomSource) {
/* 343 */       return Vec3.atLowerCornerOf((Vec3i)param1BlockPos).add(Mth.nextDouble(param1RandomSource, 0.1D, 0.9D), Mth.nextDouble(param1RandomSource, 0.25D, 0.75D), Mth.nextDouble(param1RandomSource, 0.1D, 0.9D));
/*     */     }
/*     */     
/*     */     private static Vec3 keyholePos(BlockPos param1BlockPos, Direction param1Direction) {
/* 347 */       return Vec3.atBottomCenterOf((Vec3i)param1BlockPos).add(param1Direction.getStepX() * 0.5D, 1.75D, param1Direction.getStepZ() * 0.5D);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\vault\VaultBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */