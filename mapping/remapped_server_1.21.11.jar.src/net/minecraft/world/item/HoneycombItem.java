/*     */ package net.minecraft.world.item;
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.ImmutableBiMap;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.data.recipes.RecipeCategory;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ 
/*     */ public class HoneycombItem extends Item implements SignApplicator {
/*  30 */   public static final Supplier<BiMap<Block, Block>> WAXABLES = (Supplier<BiMap<Block, Block>>)Suppliers.memoize(() -> ImmutableBiMap.builder().put(Blocks.COPPER_BLOCK, Blocks.WAXED_COPPER_BLOCK).put(Blocks.EXPOSED_COPPER, Blocks.WAXED_EXPOSED_COPPER).put(Blocks.WEATHERED_COPPER, Blocks.WAXED_WEATHERED_COPPER).put(Blocks.OXIDIZED_COPPER, Blocks.WAXED_OXIDIZED_COPPER).put(Blocks.CUT_COPPER, Blocks.WAXED_CUT_COPPER).put(Blocks.EXPOSED_CUT_COPPER, Blocks.WAXED_EXPOSED_CUT_COPPER).put(Blocks.WEATHERED_CUT_COPPER, Blocks.WAXED_WEATHERED_CUT_COPPER).put(Blocks.OXIDIZED_CUT_COPPER, Blocks.WAXED_OXIDIZED_CUT_COPPER).put(Blocks.CUT_COPPER_SLAB, Blocks.WAXED_CUT_COPPER_SLAB).put(Blocks.EXPOSED_CUT_COPPER_SLAB, Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB).put(Blocks.WEATHERED_CUT_COPPER_SLAB, Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB).put(Blocks.OXIDIZED_CUT_COPPER_SLAB, Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB).put(Blocks.CUT_COPPER_STAIRS, Blocks.WAXED_CUT_COPPER_STAIRS).put(Blocks.EXPOSED_CUT_COPPER_STAIRS, Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS).put(Blocks.WEATHERED_CUT_COPPER_STAIRS, Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS).put(Blocks.OXIDIZED_CUT_COPPER_STAIRS, Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS).put(Blocks.CHISELED_COPPER, Blocks.WAXED_CHISELED_COPPER).put(Blocks.EXPOSED_CHISELED_COPPER, Blocks.WAXED_EXPOSED_CHISELED_COPPER).put(Blocks.WEATHERED_CHISELED_COPPER, Blocks.WAXED_WEATHERED_CHISELED_COPPER).put(Blocks.OXIDIZED_CHISELED_COPPER, Blocks.WAXED_OXIDIZED_CHISELED_COPPER).put(Blocks.COPPER_DOOR, Blocks.WAXED_COPPER_DOOR).put(Blocks.EXPOSED_COPPER_DOOR, Blocks.WAXED_EXPOSED_COPPER_DOOR).put(Blocks.WEATHERED_COPPER_DOOR, Blocks.WAXED_WEATHERED_COPPER_DOOR).put(Blocks.OXIDIZED_COPPER_DOOR, Blocks.WAXED_OXIDIZED_COPPER_DOOR).put(Blocks.COPPER_TRAPDOOR, Blocks.WAXED_COPPER_TRAPDOOR).put(Blocks.EXPOSED_COPPER_TRAPDOOR, Blocks.WAXED_EXPOSED_COPPER_TRAPDOOR).put(Blocks.WEATHERED_COPPER_TRAPDOOR, Blocks.WAXED_WEATHERED_COPPER_TRAPDOOR).put(Blocks.OXIDIZED_COPPER_TRAPDOOR, Blocks.WAXED_OXIDIZED_COPPER_TRAPDOOR).putAll((Map)Blocks.COPPER_BARS.waxedMapping()).put(Blocks.COPPER_GRATE, Blocks.WAXED_COPPER_GRATE).put(Blocks.EXPOSED_COPPER_GRATE, Blocks.WAXED_EXPOSED_COPPER_GRATE).put(Blocks.WEATHERED_COPPER_GRATE, Blocks.WAXED_WEATHERED_COPPER_GRATE).put(Blocks.OXIDIZED_COPPER_GRATE, Blocks.WAXED_OXIDIZED_COPPER_GRATE).put(Blocks.COPPER_BULB, Blocks.WAXED_COPPER_BULB).put(Blocks.EXPOSED_COPPER_BULB, Blocks.WAXED_EXPOSED_COPPER_BULB).put(Blocks.WEATHERED_COPPER_BULB, Blocks.WAXED_WEATHERED_COPPER_BULB).put(Blocks.OXIDIZED_COPPER_BULB, Blocks.WAXED_OXIDIZED_COPPER_BULB).put(Blocks.COPPER_CHEST, Blocks.WAXED_COPPER_CHEST).put(Blocks.EXPOSED_COPPER_CHEST, Blocks.WAXED_EXPOSED_COPPER_CHEST).put(Blocks.WEATHERED_COPPER_CHEST, Blocks.WAXED_WEATHERED_COPPER_CHEST).put(Blocks.OXIDIZED_COPPER_CHEST, Blocks.WAXED_OXIDIZED_COPPER_CHEST).put(Blocks.COPPER_GOLEM_STATUE, Blocks.WAXED_COPPER_GOLEM_STATUE).put(Blocks.EXPOSED_COPPER_GOLEM_STATUE, Blocks.WAXED_EXPOSED_COPPER_GOLEM_STATUE).put(Blocks.WEATHERED_COPPER_GOLEM_STATUE, Blocks.WAXED_WEATHERED_COPPER_GOLEM_STATUE).put(Blocks.OXIDIZED_COPPER_GOLEM_STATUE, Blocks.WAXED_OXIDIZED_COPPER_GOLEM_STATUE).put(Blocks.LIGHTNING_ROD, Blocks.WAXED_LIGHTNING_ROD).put(Blocks.EXPOSED_LIGHTNING_ROD, Blocks.WAXED_EXPOSED_LIGHTNING_ROD).put(Blocks.WEATHERED_LIGHTNING_ROD, Blocks.WAXED_WEATHERED_LIGHTNING_ROD).put(Blocks.OXIDIZED_LIGHTNING_ROD, Blocks.WAXED_OXIDIZED_LIGHTNING_ROD).putAll((Map)Blocks.COPPER_LANTERN.waxedMapping()).putAll((Map)Blocks.COPPER_CHAIN.waxedMapping()).build());
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
/*  99 */   public static final Supplier<BiMap<Block, Block>> WAX_OFF_BY_BLOCK = (Supplier<BiMap<Block, Block>>)Suppliers.memoize(() -> ((BiMap)WAXABLES.get()).inverse());
/*     */   
/*     */   private static final String WAXED_COPPER_DOOR = "waxed_copper_door";
/*     */   
/*     */   private static final String WAXED_COPPER_TRAPDOOR = "waxed_copper_trapdoor";
/*     */   private static final String WAXED_COPPER_GOLEM_STATUE = "waxed_copper_golem_statue";
/*     */   private static final String WAXED_COPPER_CHEST = "waxed_copper_chest";
/*     */   private static final String WAXED_LIGHTNING_ROD = "waxed_lightning_rod";
/*     */   private static final String WAXED_COPPER_BAR = "waxed_copper_bar";
/*     */   private static final String WAXED_COPPER_CHAIN = "waxed_copper_chain";
/*     */   private static final String WAXED_COPPER_LANTERN = "waxed_copper_lantern";
/*     */   private static final String WAXED_COPPER_BLOCK = "waxed_copper_block";
/* 111 */   public static final ImmutableMap<Block, Pair<RecipeCategory, String>> WAXED_RECIPES = ImmutableMap.builder()
/*     */     
/* 113 */     .put(Blocks.WAXED_COPPER_BULB, Pair.of(RecipeCategory.REDSTONE, "waxed_copper_bulb"))
/* 114 */     .put(Blocks.WAXED_WEATHERED_COPPER_BULB, Pair.of(RecipeCategory.REDSTONE, "waxed_weathered_copper_bulb"))
/* 115 */     .put(Blocks.WAXED_EXPOSED_COPPER_BULB, Pair.of(RecipeCategory.REDSTONE, "waxed_exposed_copper_bulb"))
/* 116 */     .put(Blocks.WAXED_OXIDIZED_COPPER_BULB, Pair.of(RecipeCategory.REDSTONE, "waxed_oxidized_copper_bulb"))
/*     */ 
/*     */     
/* 119 */     .put(Blocks.WAXED_COPPER_DOOR, Pair.of(RecipeCategory.REDSTONE, "waxed_copper_door"))
/* 120 */     .put(Blocks.WAXED_WEATHERED_COPPER_DOOR, Pair.of(RecipeCategory.REDSTONE, "waxed_copper_door"))
/* 121 */     .put(Blocks.WAXED_EXPOSED_COPPER_DOOR, Pair.of(RecipeCategory.REDSTONE, "waxed_copper_door"))
/* 122 */     .put(Blocks.WAXED_OXIDIZED_COPPER_DOOR, Pair.of(RecipeCategory.REDSTONE, "waxed_copper_door"))
/*     */ 
/*     */     
/* 125 */     .put(Blocks.WAXED_COPPER_TRAPDOOR, Pair.of(RecipeCategory.REDSTONE, "waxed_copper_trapdoor"))
/* 126 */     .put(Blocks.WAXED_WEATHERED_COPPER_TRAPDOOR, Pair.of(RecipeCategory.REDSTONE, "waxed_copper_trapdoor"))
/* 127 */     .put(Blocks.WAXED_EXPOSED_COPPER_TRAPDOOR, Pair.of(RecipeCategory.REDSTONE, "waxed_copper_trapdoor"))
/* 128 */     .put(Blocks.WAXED_OXIDIZED_COPPER_TRAPDOOR, Pair.of(RecipeCategory.REDSTONE, "waxed_copper_trapdoor"))
/*     */ 
/*     */     
/* 131 */     .put(Blocks.WAXED_COPPER_GOLEM_STATUE, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_golem_statue"))
/* 132 */     .put(Blocks.WAXED_WEATHERED_COPPER_GOLEM_STATUE, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_golem_statue"))
/* 133 */     .put(Blocks.WAXED_EXPOSED_COPPER_GOLEM_STATUE, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_golem_statue"))
/* 134 */     .put(Blocks.WAXED_OXIDIZED_COPPER_GOLEM_STATUE, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_golem_statue"))
/*     */ 
/*     */     
/* 137 */     .put(Blocks.WAXED_COPPER_CHEST, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_chest"))
/* 138 */     .put(Blocks.WAXED_WEATHERED_COPPER_CHEST, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_chest"))
/* 139 */     .put(Blocks.WAXED_EXPOSED_COPPER_CHEST, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_chest"))
/* 140 */     .put(Blocks.WAXED_OXIDIZED_COPPER_CHEST, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_chest"))
/*     */ 
/*     */     
/* 143 */     .put(Blocks.WAXED_LIGHTNING_ROD, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_lightning_rod"))
/* 144 */     .put(Blocks.WAXED_WEATHERED_LIGHTNING_ROD, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_lightning_rod"))
/* 145 */     .put(Blocks.WAXED_EXPOSED_LIGHTNING_ROD, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_lightning_rod"))
/* 146 */     .put(Blocks.WAXED_OXIDIZED_LIGHTNING_ROD, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_lightning_rod"))
/*     */ 
/*     */     
/* 149 */     .put(Blocks.COPPER_BARS.waxed(), Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_bar"))
/* 150 */     .put(Blocks.COPPER_BARS.waxedWeathered(), Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_bar"))
/* 151 */     .put(Blocks.COPPER_BARS.waxedExposed(), Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_bar"))
/* 152 */     .put(Blocks.COPPER_BARS.waxedOxidized(), Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_bar"))
/*     */ 
/*     */     
/* 155 */     .put(Blocks.COPPER_CHAIN.waxed(), Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_chain"))
/* 156 */     .put(Blocks.COPPER_CHAIN.waxedWeathered(), Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_chain"))
/* 157 */     .put(Blocks.COPPER_CHAIN.waxedExposed(), Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_chain"))
/* 158 */     .put(Blocks.COPPER_CHAIN.waxedOxidized(), Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_chain"))
/*     */ 
/*     */     
/* 161 */     .put(Blocks.COPPER_LANTERN.waxed(), Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_lantern"))
/* 162 */     .put(Blocks.COPPER_LANTERN.waxedWeathered(), Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_lantern"))
/* 163 */     .put(Blocks.COPPER_LANTERN.waxedExposed(), Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_lantern"))
/* 164 */     .put(Blocks.COPPER_LANTERN.waxedOxidized(), Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_lantern"))
/*     */ 
/*     */     
/* 167 */     .put(Blocks.WAXED_COPPER_BLOCK, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_block"))
/* 168 */     .put(Blocks.WAXED_WEATHERED_COPPER, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_block"))
/* 169 */     .put(Blocks.WAXED_EXPOSED_COPPER, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_block"))
/* 170 */     .put(Blocks.WAXED_OXIDIZED_COPPER, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_block"))
/* 171 */     .build();
/*     */   
/*     */   public HoneycombItem(Item.Properties paramProperties) {
/* 174 */     super(paramProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult useOn(UseOnContext paramUseOnContext) {
/* 179 */     Level level = paramUseOnContext.getLevel();
/* 180 */     BlockPos blockPos = paramUseOnContext.getClickedPos();
/* 181 */     BlockState blockState = level.getBlockState(blockPos);
/*     */     
/* 183 */     return (InteractionResult)getWaxed(blockState).map(paramBlockState2 -> {
/*     */           Player player = paramUseOnContext.getPlayer();
/*     */           
/*     */           ItemStack itemStack = paramUseOnContext.getItemInHand();
/*     */           
/*     */           if (player instanceof ServerPlayer) {
/*     */             ServerPlayer serverPlayer = (ServerPlayer)player;
/*     */             CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, paramBlockPos, itemStack);
/*     */           } 
/*     */           itemStack.shrink(1);
/*     */           paramLevel.setBlock(paramBlockPos, paramBlockState2, 11);
/*     */           paramLevel.gameEvent((Holder)GameEvent.BLOCK_CHANGE, paramBlockPos, GameEvent.Context.of((Entity)player, paramBlockState2));
/*     */           paramLevel.levelEvent((Entity)player, 3003, paramBlockPos, 0);
/*     */           if (paramBlockState1.getBlock() instanceof ChestBlock && paramBlockState1.getValue((Property)ChestBlock.TYPE) != ChestType.SINGLE) {
/*     */             BlockPos blockPos = ChestBlock.getConnectedBlockPos(paramBlockPos, paramBlockState1);
/*     */             paramLevel.gameEvent((Holder)GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of((Entity)player, paramLevel.getBlockState(blockPos)));
/*     */             paramLevel.levelEvent((Entity)player, 3003, blockPos, 0);
/*     */           } 
/*     */           return (InteractionResult)InteractionResult.SUCCESS;
/* 202 */         }).orElse(InteractionResult.PASS);
/*     */   }
/*     */   
/*     */   public static Optional<BlockState> getWaxed(BlockState paramBlockState) {
/* 206 */     return Optional.<Block>ofNullable((Block)((BiMap)WAXABLES.get()).get(paramBlockState.getBlock())).map(paramBlock -> paramBlock.withPropertiesOf(paramBlockState));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean tryApplyToSign(Level paramLevel, SignBlockEntity paramSignBlockEntity, boolean paramBoolean, Player paramPlayer) {
/* 211 */     if (paramSignBlockEntity.setWaxed(true)) {
/* 212 */       paramLevel.levelEvent(null, 3003, paramSignBlockEntity.getBlockPos(), 0);
/* 213 */       return true;
/*     */     } 
/* 215 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canApplyToSign(SignText paramSignText, Player paramPlayer) {
/* 220 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\HoneycombItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */