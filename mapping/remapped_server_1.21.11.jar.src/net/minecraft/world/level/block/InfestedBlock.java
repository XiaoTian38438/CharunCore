/*    */ package net.minecraft.world.level.block;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Map;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.EnchantmentTags;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.monster.Silverfish;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class InfestedBlock extends Block {
/*    */   static {
/* 23 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)BuiltInRegistries.BLOCK.byNameCodec().fieldOf("host").forGetter(InfestedBlock::getHostBlock), (App)propertiesCodec()).apply((Applicative)paramInstance, InfestedBlock::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<InfestedBlock> CODEC;
/*    */   private final Block hostBlock;
/*    */   
/*    */   public MapCodec<? extends InfestedBlock> codec() {
/* 30 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 35 */   private static final Map<Block, Block> BLOCK_BY_HOST_BLOCK = Maps.newIdentityHashMap();
/*    */   
/* 37 */   private static final Map<BlockState, BlockState> HOST_TO_INFESTED_STATES = Maps.newIdentityHashMap();
/* 38 */   private static final Map<BlockState, BlockState> INFESTED_TO_HOST_STATES = Maps.newIdentityHashMap();
/*    */   
/*    */   public InfestedBlock(Block paramBlock, BlockBehaviour.Properties paramProperties) {
/* 41 */     super(paramProperties.destroyTime(paramBlock.defaultDestroyTime() / 2.0F).explosionResistance(0.75F));
/* 42 */     this.hostBlock = paramBlock;
/* 43 */     BLOCK_BY_HOST_BLOCK.put(paramBlock, this);
/*    */   }
/*    */   
/*    */   public Block getHostBlock() {
/* 47 */     return this.hostBlock;
/*    */   }
/*    */   
/*    */   public static boolean isCompatibleHostBlock(BlockState paramBlockState) {
/* 51 */     return BLOCK_BY_HOST_BLOCK.containsKey(paramBlockState.getBlock());
/*    */   }
/*    */   
/*    */   private void spawnInfestation(ServerLevel paramServerLevel, BlockPos paramBlockPos) {
/* 55 */     Silverfish silverfish = (Silverfish)EntityType.SILVERFISH.create((Level)paramServerLevel, EntitySpawnReason.TRIGGERED);
/* 56 */     if (silverfish != null) {
/* 57 */       silverfish.snapTo(paramBlockPos.getX() + 0.5D, paramBlockPos.getY(), paramBlockPos.getZ() + 0.5D, 0.0F, 0.0F);
/* 58 */       paramServerLevel.addFreshEntity((Entity)silverfish);
/*    */       
/* 60 */       silverfish.spawnAnim();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void spawnAfterBreak(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, ItemStack paramItemStack, boolean paramBoolean) {
/* 66 */     super.spawnAfterBreak(paramBlockState, paramServerLevel, paramBlockPos, paramItemStack, paramBoolean);
/*    */     
/* 68 */     if (((Boolean)paramServerLevel.getGameRules().get(GameRules.BLOCK_DROPS)).booleanValue() && 
/* 69 */       !EnchantmentHelper.hasTag(paramItemStack, EnchantmentTags.PREVENTS_INFESTED_SPAWNS)) {
/* 70 */       spawnInfestation(paramServerLevel, paramBlockPos);
/*    */     }
/*    */   }
/*    */   
/*    */   public static BlockState infestedStateByHost(BlockState paramBlockState) {
/* 75 */     return getNewStateWithProperties(HOST_TO_INFESTED_STATES, paramBlockState, () -> ((Block)BLOCK_BY_HOST_BLOCK.get(paramBlockState.getBlock())).defaultBlockState());
/*    */   }
/*    */   
/*    */   public BlockState hostStateByInfested(BlockState paramBlockState) {
/* 79 */     return getNewStateWithProperties(INFESTED_TO_HOST_STATES, paramBlockState, () -> getHostBlock().defaultBlockState());
/*    */   }
/*    */   
/*    */   private static BlockState getNewStateWithProperties(Map<BlockState, BlockState> paramMap, BlockState paramBlockState, Supplier<BlockState> paramSupplier) {
/* 83 */     return paramMap.computeIfAbsent(paramBlockState, paramBlockState -> {
/*    */           BlockState blockState = paramSupplier.get();
/*    */           for (Property property : paramBlockState.getProperties())
/*    */             blockState = blockState.hasProperty(property) ? (BlockState)blockState.setValue(property, paramBlockState.getValue(property)) : blockState; 
/*    */           return blockState;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\InfestedBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */