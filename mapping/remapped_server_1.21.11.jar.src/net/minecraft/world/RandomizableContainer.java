/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ import net.minecraft.world.level.storage.loot.LootParams;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public interface RandomizableContainer extends Container {
/*    */   public static final String LOOT_TABLE_TAG = "LootTable";
/*    */   public static final String LOOT_TABLE_SEED_TAG = "LootTableSeed";
/*    */   
/*    */   ResourceKey<LootTable> getLootTable();
/*    */   
/*    */   void setLootTable(ResourceKey<LootTable> paramResourceKey);
/*    */   
/*    */   default void setLootTable(ResourceKey<LootTable> paramResourceKey, long paramLong) {
/* 31 */     setLootTable(paramResourceKey);
/* 32 */     setLootTableSeed(paramLong);
/*    */   }
/*    */   
/*    */   long getLootTableSeed();
/*    */   
/*    */   void setLootTableSeed(long paramLong);
/*    */   
/*    */   BlockPos getBlockPos();
/*    */   
/*    */   Level getLevel();
/*    */   
/*    */   static void setBlockEntityLootTable(BlockGetter paramBlockGetter, RandomSource paramRandomSource, BlockPos paramBlockPos, ResourceKey<LootTable> paramResourceKey) {
/* 44 */     BlockEntity blockEntity = paramBlockGetter.getBlockEntity(paramBlockPos);
/* 45 */     if (blockEntity instanceof RandomizableContainer) { RandomizableContainer randomizableContainer = (RandomizableContainer)blockEntity;
/* 46 */       randomizableContainer.setLootTable(paramResourceKey, paramRandomSource.nextLong()); }
/*    */   
/*    */   }
/*    */   
/*    */   default boolean tryLoadLootTable(ValueInput paramValueInput) {
/* 51 */     ResourceKey<LootTable> resourceKey = paramValueInput.read("LootTable", LootTable.KEY_CODEC).orElse(null);
/* 52 */     setLootTable(resourceKey);
/* 53 */     setLootTableSeed(paramValueInput.getLongOr("LootTableSeed", 0L));
/* 54 */     return (resourceKey != null);
/*    */   }
/*    */   
/*    */   default boolean trySaveLootTable(ValueOutput paramValueOutput) {
/* 58 */     ResourceKey<LootTable> resourceKey = getLootTable();
/* 59 */     if (resourceKey == null) {
/* 60 */       return false;
/*    */     }
/*    */     
/* 63 */     paramValueOutput.store("LootTable", LootTable.KEY_CODEC, resourceKey);
/* 64 */     long l = getLootTableSeed();
/* 65 */     if (l != 0L) {
/* 66 */       paramValueOutput.putLong("LootTableSeed", l);
/*    */     }
/* 68 */     return true;
/*    */   }
/*    */   
/*    */   default void unpackLootTable(Player paramPlayer) {
/* 72 */     Level level = getLevel();
/* 73 */     BlockPos blockPos = getBlockPos();
/* 74 */     ResourceKey<LootTable> resourceKey = getLootTable();
/*    */     
/* 76 */     if (resourceKey != null && level != null && level.getServer() != null) {
/* 77 */       LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(resourceKey);
/* 78 */       if (paramPlayer instanceof ServerPlayer) {
/* 79 */         CriteriaTriggers.GENERATE_LOOT.trigger((ServerPlayer)paramPlayer, resourceKey);
/*    */       }
/* 81 */       setLootTable(null);
/*    */       
/* 83 */       LootParams.Builder builder = (new LootParams.Builder((ServerLevel)level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf((Vec3i)blockPos));
/*    */       
/* 85 */       if (paramPlayer != null) {
/* 86 */         builder.withLuck(paramPlayer.getLuck()).withParameter(LootContextParams.THIS_ENTITY, paramPlayer);
/*    */       }
/*    */       
/* 89 */       lootTable.fill(this, builder.create(LootContextParamSets.CHEST), getLootTableSeed());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\RandomizableContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */