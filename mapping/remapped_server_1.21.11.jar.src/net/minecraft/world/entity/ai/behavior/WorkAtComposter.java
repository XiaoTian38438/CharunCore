/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ComposterBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class WorkAtComposter extends WorkAtPoi {
/*  22 */   private static final List<Item> COMPOSTABLE_ITEMS = (List<Item>)ImmutableList.of(Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void useWorkstation(ServerLevel paramServerLevel, Villager paramVillager) {
/*  29 */     Optional<GlobalPos> optional = paramVillager.getBrain().getMemory(MemoryModuleType.JOB_SITE);
/*  30 */     if (optional.isEmpty()) {
/*     */       return;
/*     */     }
/*  33 */     GlobalPos globalPos = optional.get();
/*  34 */     BlockState blockState = paramServerLevel.getBlockState(globalPos.pos());
/*     */     
/*  36 */     if (blockState.is(Blocks.COMPOSTER)) {
/*  37 */       makeBread(paramServerLevel, paramVillager);
/*     */       
/*  39 */       compostItems(paramServerLevel, paramVillager, globalPos, blockState);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void compostItems(ServerLevel paramServerLevel, Villager paramVillager, GlobalPos paramGlobalPos, BlockState paramBlockState) {
/*  45 */     BlockPos blockPos = paramGlobalPos.pos();
/*  46 */     if (((Integer)paramBlockState.getValue((Property)ComposterBlock.LEVEL)).intValue() == 8) {
/*  47 */       paramBlockState = ComposterBlock.extractProduce((Entity)paramVillager, paramBlockState, (Level)paramServerLevel, blockPos);
/*     */     }
/*     */ 
/*     */     
/*  51 */     int i = 20;
/*  52 */     byte b = 10;
/*     */     
/*  54 */     int[] arrayOfInt = new int[COMPOSTABLE_ITEMS.size()];
/*     */     
/*  56 */     SimpleContainer simpleContainer = paramVillager.getInventory();
/*  57 */     int j = simpleContainer.getContainerSize();
/*     */     
/*  59 */     BlockState blockState = paramBlockState;
/*     */     
/*  61 */     for (int k = j - 1; k >= 0 && i > 0; k--) {
/*  62 */       ItemStack itemStack = simpleContainer.getItem(k);
/*  63 */       int m = COMPOSTABLE_ITEMS.indexOf(itemStack.getItem());
/*  64 */       if (m != -1) {
/*     */ 
/*     */ 
/*     */         
/*  68 */         int n = itemStack.getCount();
/*  69 */         int i1 = arrayOfInt[m] + n;
/*  70 */         arrayOfInt[m] = i1;
/*     */         
/*  72 */         int i2 = Math.min(Math.min(i1 - 10, i), n);
/*  73 */         if (i2 > 0) {
/*  74 */           i -= i2;
/*  75 */           for (byte b1 = 0; b1 < i2; b1++) {
/*  76 */             blockState = ComposterBlock.insertItem((Entity)paramVillager, blockState, paramServerLevel, itemStack, blockPos);
/*  77 */             if (((Integer)blockState.getValue((Property)ComposterBlock.LEVEL)).intValue() == 7) {
/*  78 */               spawnComposterFillEffects(paramServerLevel, paramBlockState, blockPos, blockState);
/*     */               return;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*  85 */     spawnComposterFillEffects(paramServerLevel, paramBlockState, blockPos, blockState);
/*     */   }
/*     */   
/*     */   private void spawnComposterFillEffects(ServerLevel paramServerLevel, BlockState paramBlockState1, BlockPos paramBlockPos, BlockState paramBlockState2) {
/*  89 */     paramServerLevel.levelEvent(1500, paramBlockPos, (paramBlockState2 != paramBlockState1) ? 1 : 0);
/*     */   }
/*     */   
/*     */   private void makeBread(ServerLevel paramServerLevel, Villager paramVillager) {
/*  93 */     SimpleContainer simpleContainer = paramVillager.getInventory();
/*  94 */     if (simpleContainer.countItem(Items.BREAD) > 36) {
/*     */       return;
/*     */     }
/*     */     
/*  98 */     int i = simpleContainer.countItem(Items.WHEAT);
/*  99 */     byte b1 = 3;
/* 100 */     byte b2 = 3;
/* 101 */     int j = Math.min(3, i / 3);
/* 102 */     if (j == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 106 */     int k = j * 3;
/* 107 */     simpleContainer.removeItemType(Items.WHEAT, k);
/* 108 */     ItemStack itemStack = simpleContainer.addItem(new ItemStack((ItemLike)Items.BREAD, j));
/* 109 */     if (!itemStack.isEmpty())
/* 110 */       paramVillager.spawnAtLocation(paramServerLevel, itemStack, 0.5F); 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\WorkAtComposter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */