/*     */ package net.minecraft.world.level.block.entity.vault;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.VaultBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Server
/*     */ {
/*     */   private static final int UNLOCKING_DELAY_TICKS = 14;
/*     */   private static final int DISPLAY_CYCLE_TICK_RATE = 20;
/*     */   private static final int INSERT_FAIL_SOUND_BUFFER_TICKS = 15;
/*     */   
/*     */   public static void tick(ServerLevel paramServerLevel, BlockPos paramBlockPos, BlockState paramBlockState, VaultConfig paramVaultConfig, VaultServerData paramVaultServerData, VaultSharedData paramVaultSharedData) {
/* 113 */     VaultState vaultState = (VaultState)paramBlockState.getValue(VaultBlock.STATE);
/*     */     
/* 115 */     if (shouldCycleDisplayItem(paramServerLevel.getGameTime(), vaultState)) {
/* 116 */       cycleDisplayItemFromLootTable(paramServerLevel, vaultState, paramVaultConfig, paramVaultSharedData, paramBlockPos);
/*     */     }
/*     */     
/* 119 */     BlockState blockState = paramBlockState;
/* 120 */     if (paramServerLevel.getGameTime() >= paramVaultServerData.stateUpdatingResumesAt()) {
/* 121 */       blockState = (BlockState)blockState.setValue(VaultBlock.STATE, vaultState.tickAndGetNext(paramServerLevel, paramBlockPos, paramVaultConfig, paramVaultServerData, paramVaultSharedData));
/*     */       
/* 123 */       if (paramBlockState != blockState) {
/* 124 */         setVaultState(paramServerLevel, paramBlockPos, paramBlockState, blockState, paramVaultConfig, paramVaultSharedData);
/*     */       }
/*     */     } 
/*     */     
/* 128 */     if (paramVaultServerData.isDirty || paramVaultSharedData.isDirty) {
/*     */       
/* 130 */       VaultBlockEntity.access$000((Level)paramServerLevel, paramBlockPos, paramBlockState);
/*     */ 
/*     */       
/* 133 */       if (paramVaultSharedData.isDirty) {
/* 134 */         paramServerLevel.sendBlockUpdated(paramBlockPos, paramBlockState, blockState, 2);
/*     */       }
/* 136 */       paramVaultServerData.isDirty = false;
/* 137 */       paramVaultSharedData.isDirty = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void tryInsertKey(ServerLevel paramServerLevel, BlockPos paramBlockPos, BlockState paramBlockState, VaultConfig paramVaultConfig, VaultServerData paramVaultServerData, VaultSharedData paramVaultSharedData, Player paramPlayer, ItemStack paramItemStack) {
/* 142 */     VaultState vaultState = (VaultState)paramBlockState.getValue(VaultBlock.STATE);
/*     */     
/* 144 */     if (!canEjectReward(paramVaultConfig, vaultState)) {
/*     */       return;
/*     */     }
/*     */     
/* 148 */     if (!isValidToInsert(paramVaultConfig, paramItemStack)) {
/* 149 */       playInsertFailSound(paramServerLevel, paramVaultServerData, paramBlockPos, SoundEvents.VAULT_INSERT_ITEM_FAIL);
/*     */       
/*     */       return;
/*     */     } 
/* 153 */     if (paramVaultServerData.hasRewardedPlayer(paramPlayer)) {
/* 154 */       playInsertFailSound(paramServerLevel, paramVaultServerData, paramBlockPos, SoundEvents.VAULT_REJECT_REWARDED_PLAYER);
/*     */       
/*     */       return;
/*     */     } 
/* 158 */     List<ItemStack> list = resolveItemsToEject(paramServerLevel, paramVaultConfig, paramBlockPos, paramPlayer, paramItemStack);
/* 159 */     if (list.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 163 */     paramPlayer.awardStat(Stats.ITEM_USED.get(paramItemStack.getItem()));
/* 164 */     paramItemStack.consume(paramVaultConfig.keyItem().getCount(), (LivingEntity)paramPlayer);
/*     */     
/* 166 */     unlock(paramServerLevel, paramBlockState, paramBlockPos, paramVaultConfig, paramVaultServerData, paramVaultSharedData, list);
/* 167 */     paramVaultServerData.addToRewardedPlayers(paramPlayer);
/* 168 */     paramVaultSharedData.updateConnectedPlayersWithinRange(paramServerLevel, paramBlockPos, paramVaultServerData, paramVaultConfig, paramVaultConfig.deactivationRange());
/*     */   }
/*     */   
/*     */   static void setVaultState(ServerLevel paramServerLevel, BlockPos paramBlockPos, BlockState paramBlockState1, BlockState paramBlockState2, VaultConfig paramVaultConfig, VaultSharedData paramVaultSharedData) {
/* 172 */     VaultState vaultState1 = (VaultState)paramBlockState1.getValue(VaultBlock.STATE);
/* 173 */     VaultState vaultState2 = (VaultState)paramBlockState2.getValue(VaultBlock.STATE);
/*     */     
/* 175 */     paramServerLevel.setBlock(paramBlockPos, paramBlockState2, 3);
/* 176 */     vaultState1.onTransition(paramServerLevel, paramBlockPos, vaultState2, paramVaultConfig, paramVaultSharedData, ((Boolean)paramBlockState2.getValue((Property)VaultBlock.OMINOUS)).booleanValue());
/*     */   }
/*     */   
/*     */   static void cycleDisplayItemFromLootTable(ServerLevel paramServerLevel, VaultState paramVaultState, VaultConfig paramVaultConfig, VaultSharedData paramVaultSharedData, BlockPos paramBlockPos) {
/* 180 */     if (!canEjectReward(paramVaultConfig, paramVaultState)) {
/* 181 */       paramVaultSharedData.setDisplayItem(ItemStack.EMPTY);
/*     */       
/*     */       return;
/*     */     } 
/* 185 */     ItemStack itemStack = getRandomDisplayItemFromLootTable(paramServerLevel, paramBlockPos, paramVaultConfig.overrideLootTableToDisplay().orElse(paramVaultConfig.lootTable()));
/* 186 */     paramVaultSharedData.setDisplayItem(itemStack);
/*     */   }
/*     */   
/*     */   private static ItemStack getRandomDisplayItemFromLootTable(ServerLevel paramServerLevel, BlockPos paramBlockPos, ResourceKey<LootTable> paramResourceKey) {
/* 190 */     LootTable lootTable = paramServerLevel.getServer().reloadableRegistries().getLootTable(paramResourceKey);
/*     */ 
/*     */     
/* 193 */     LootParams lootParams = (new LootParams.Builder(paramServerLevel)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf((Vec3i)paramBlockPos)).create(LootContextParamSets.VAULT);
/* 194 */     ObjectArrayList objectArrayList = lootTable.getRandomItems(lootParams, paramServerLevel.getRandom());
/*     */     
/* 196 */     if (objectArrayList.isEmpty()) {
/* 197 */       return ItemStack.EMPTY;
/*     */     }
/*     */     
/* 200 */     return (ItemStack)Util.getRandom((List)objectArrayList, paramServerLevel.getRandom());
/*     */   }
/*     */   
/*     */   private static void unlock(ServerLevel paramServerLevel, BlockState paramBlockState, BlockPos paramBlockPos, VaultConfig paramVaultConfig, VaultServerData paramVaultServerData, VaultSharedData paramVaultSharedData, List<ItemStack> paramList) {
/* 204 */     paramVaultServerData.setItemsToEject(paramList);
/* 205 */     paramVaultSharedData.setDisplayItem(paramVaultServerData.getNextItemToEject());
/* 206 */     paramVaultServerData.pauseStateUpdatingUntil(paramServerLevel.getGameTime() + 14L);
/* 207 */     setVaultState(paramServerLevel, paramBlockPos, paramBlockState, (BlockState)paramBlockState.setValue(VaultBlock.STATE, VaultState.UNLOCKING), paramVaultConfig, paramVaultSharedData);
/*     */   }
/*     */   
/*     */   private static List<ItemStack> resolveItemsToEject(ServerLevel paramServerLevel, VaultConfig paramVaultConfig, BlockPos paramBlockPos, Player paramPlayer, ItemStack paramItemStack) {
/* 211 */     LootTable lootTable = paramServerLevel.getServer().reloadableRegistries().getLootTable(paramVaultConfig.lootTable());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 217 */     LootParams lootParams = (new LootParams.Builder(paramServerLevel)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf((Vec3i)paramBlockPos)).withLuck(paramPlayer.getLuck()).withParameter(LootContextParams.THIS_ENTITY, paramPlayer).withParameter(LootContextParams.TOOL, paramItemStack).create(LootContextParamSets.VAULT);
/*     */     
/* 219 */     return (List<ItemStack>)lootTable.getRandomItems(lootParams);
/*     */   }
/*     */   
/*     */   private static boolean canEjectReward(VaultConfig paramVaultConfig, VaultState paramVaultState) {
/* 223 */     return (!paramVaultConfig.keyItem().isEmpty() && paramVaultState != VaultState.INACTIVE);
/*     */   }
/*     */   
/*     */   private static boolean isValidToInsert(VaultConfig paramVaultConfig, ItemStack paramItemStack) {
/* 227 */     return (ItemStack.isSameItemSameComponents(paramItemStack, paramVaultConfig.keyItem()) && paramItemStack.getCount() >= paramVaultConfig.keyItem().getCount());
/*     */   }
/*     */   
/*     */   private static boolean shouldCycleDisplayItem(long paramLong, VaultState paramVaultState) {
/* 231 */     return (paramLong % 20L == 0L && paramVaultState == VaultState.ACTIVE);
/*     */   }
/*     */   
/*     */   private static void playInsertFailSound(ServerLevel paramServerLevel, VaultServerData paramVaultServerData, BlockPos paramBlockPos, SoundEvent paramSoundEvent) {
/* 235 */     if (paramServerLevel.getGameTime() >= paramVaultServerData.getLastInsertFailTimestamp() + 15L) {
/* 236 */       paramServerLevel.playSound(null, paramBlockPos, paramSoundEvent, SoundSource.BLOCKS);
/* 237 */       paramVaultServerData.setLastInsertFailTimestamp(paramServerLevel.getGameTime());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\vault\VaultBlockEntity$Server.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */