/*     */ package net.minecraft.world.item;
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.RotatedPillarBlock;
/*     */ import net.minecraft.world.level.block.WeatheringCopper;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ 
/*     */ public class AxeItem extends Item {
/*  32 */   protected static final Map<Block, Block> STRIPPABLES = (Map<Block, Block>)(new ImmutableMap.Builder())
/*  33 */     .put(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD)
/*  34 */     .put(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG)
/*  35 */     .put(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD)
/*  36 */     .put(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG)
/*  37 */     .put(Blocks.PALE_OAK_WOOD, Blocks.STRIPPED_PALE_OAK_WOOD)
/*  38 */     .put(Blocks.PALE_OAK_LOG, Blocks.STRIPPED_PALE_OAK_LOG)
/*  39 */     .put(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD)
/*  40 */     .put(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG)
/*  41 */     .put(Blocks.CHERRY_WOOD, Blocks.STRIPPED_CHERRY_WOOD)
/*  42 */     .put(Blocks.CHERRY_LOG, Blocks.STRIPPED_CHERRY_LOG)
/*  43 */     .put(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD)
/*  44 */     .put(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG)
/*  45 */     .put(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD)
/*  46 */     .put(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG)
/*  47 */     .put(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD)
/*  48 */     .put(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG)
/*  49 */     .put(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM)
/*  50 */     .put(Blocks.WARPED_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE)
/*  51 */     .put(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM)
/*  52 */     .put(Blocks.CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_HYPHAE)
/*  53 */     .put(Blocks.MANGROVE_WOOD, Blocks.STRIPPED_MANGROVE_WOOD)
/*  54 */     .put(Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG)
/*  55 */     .put(Blocks.BAMBOO_BLOCK, Blocks.STRIPPED_BAMBOO_BLOCK)
/*  56 */     .build();
/*     */   
/*     */   public AxeItem(ToolMaterial paramToolMaterial, float paramFloat1, float paramFloat2, Item.Properties paramProperties) {
/*  59 */     super(paramProperties.axe(paramToolMaterial, paramFloat1, paramFloat2));
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult useOn(UseOnContext paramUseOnContext) {
/*  64 */     Level level = paramUseOnContext.getLevel();
/*  65 */     BlockPos blockPos = paramUseOnContext.getClickedPos();
/*  66 */     Player player = paramUseOnContext.getPlayer();
/*     */     
/*  68 */     if (playerHasBlockingItemUseIntent(paramUseOnContext))
/*     */     {
/*  70 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/*  73 */     Optional<BlockState> optional = evaluateNewBlockState(level, blockPos, player, level.getBlockState(blockPos));
/*  74 */     if (optional.isEmpty()) {
/*  75 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/*  78 */     ItemStack itemStack = paramUseOnContext.getItemInHand();
/*     */     
/*  80 */     if (player instanceof ServerPlayer) {
/*  81 */       CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, blockPos, itemStack);
/*     */     }
/*     */     
/*  84 */     level.setBlock(blockPos, optional.get(), 11);
/*  85 */     level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of((Entity)player, optional.get()));
/*     */     
/*  87 */     if (player != null) {
/*  88 */       itemStack.hurtAndBreak(1, (LivingEntity)player, paramUseOnContext.getHand().asEquipmentSlot());
/*     */     }
/*     */     
/*  91 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   private static boolean playerHasBlockingItemUseIntent(UseOnContext paramUseOnContext) {
/*  95 */     Player player = paramUseOnContext.getPlayer();
/*  96 */     return (paramUseOnContext.getHand().equals(InteractionHand.MAIN_HAND) && player
/*  97 */       .getOffhandItem().has(DataComponents.BLOCKS_ATTACKS) && 
/*  98 */       !player.isSecondaryUseActive());
/*     */   }
/*     */   
/*     */   private Optional<BlockState> evaluateNewBlockState(Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockState paramBlockState) {
/* 102 */     Optional<BlockState> optional1 = getStripped(paramBlockState);
/* 103 */     if (optional1.isPresent()) {
/* 104 */       paramLevel.playSound((Entity)paramPlayer, paramBlockPos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 105 */       return optional1;
/*     */     } 
/*     */     
/* 108 */     Optional<BlockState> optional2 = WeatheringCopper.getPrevious(paramBlockState);
/* 109 */     if (optional2.isPresent()) {
/* 110 */       spawnSoundAndParticle(paramLevel, paramBlockPos, paramPlayer, paramBlockState, SoundEvents.AXE_SCRAPE, 3005);
/* 111 */       return optional2;
/*     */     } 
/*     */     
/* 114 */     Optional<BlockState> optional3 = Optional.<Block>ofNullable((Block)((BiMap)HoneycombItem.WAX_OFF_BY_BLOCK.get()).get(paramBlockState.getBlock())).map(paramBlock -> paramBlock.withPropertiesOf(paramBlockState));
/* 115 */     if (optional3.isPresent()) {
/* 116 */       spawnSoundAndParticle(paramLevel, paramBlockPos, paramPlayer, paramBlockState, SoundEvents.AXE_WAX_OFF, 3004);
/* 117 */       return optional3;
/*     */     } 
/*     */     
/* 120 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   private static void spawnSoundAndParticle(Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockState paramBlockState, SoundEvent paramSoundEvent, int paramInt) {
/* 124 */     paramLevel.playSound((Entity)paramPlayer, paramBlockPos, paramSoundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 125 */     paramLevel.levelEvent((Entity)paramPlayer, paramInt, paramBlockPos, 0);
/*     */     
/* 127 */     if (paramBlockState.getBlock() instanceof ChestBlock && paramBlockState.getValue((Property)ChestBlock.TYPE) != ChestType.SINGLE) {
/* 128 */       BlockPos blockPos = ChestBlock.getConnectedBlockPos(paramBlockPos, paramBlockState);
/* 129 */       paramLevel.gameEvent((Holder)GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of((Entity)paramPlayer, paramLevel.getBlockState(blockPos)));
/* 130 */       paramLevel.levelEvent((Entity)paramPlayer, paramInt, blockPos, 0);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Optional<BlockState> getStripped(BlockState paramBlockState) {
/* 135 */     return Optional.<Block>ofNullable(STRIPPABLES.get(paramBlockState.getBlock())).map(paramBlock -> (BlockState)paramBlock.defaultBlockState().setValue((Property)RotatedPillarBlock.AXIS, paramBlockState.getValue((Property)RotatedPillarBlock.AXIS)));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\AxeItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */