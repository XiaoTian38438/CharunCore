/*    */ package net.minecraft.world.item;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.Map;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public class HoeItem extends Item {
/* 26 */   protected static final Map<Block, Pair<Predicate<UseOnContext>, Consumer<UseOnContext>>> TILLABLES = Maps.newHashMap((Map)ImmutableMap.of(Blocks.GRASS_BLOCK, 
/* 27 */         Pair.of(HoeItem::onlyIfAirAbove, changeIntoState(Blocks.FARMLAND.defaultBlockState())), Blocks.DIRT_PATH, 
/* 28 */         Pair.of(HoeItem::onlyIfAirAbove, changeIntoState(Blocks.FARMLAND.defaultBlockState())), Blocks.DIRT, 
/* 29 */         Pair.of(HoeItem::onlyIfAirAbove, changeIntoState(Blocks.FARMLAND.defaultBlockState())), Blocks.COARSE_DIRT, 
/* 30 */         Pair.of(HoeItem::onlyIfAirAbove, changeIntoState(Blocks.DIRT.defaultBlockState())), Blocks.ROOTED_DIRT, 
/* 31 */         Pair.of(paramUseOnContext -> true, changeIntoStateAndDropItem(Blocks.DIRT.defaultBlockState(), Items.HANGING_ROOTS))));
/*    */ 
/*    */   
/*    */   public HoeItem(ToolMaterial paramToolMaterial, float paramFloat1, float paramFloat2, Item.Properties paramProperties) {
/* 35 */     super(paramProperties.hoe(paramToolMaterial, paramFloat1, paramFloat2));
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext paramUseOnContext) {
/* 40 */     Level level = paramUseOnContext.getLevel();
/* 41 */     BlockPos blockPos = paramUseOnContext.getClickedPos();
/*    */     
/* 43 */     Pair pair = TILLABLES.get(level.getBlockState(blockPos).getBlock());
/*    */     
/* 45 */     if (pair == null) {
/* 46 */       return (InteractionResult)InteractionResult.PASS;
/*    */     }
/*    */     
/* 49 */     Predicate<UseOnContext> predicate = (Predicate)pair.getFirst();
/* 50 */     Consumer<UseOnContext> consumer = (Consumer)pair.getSecond();
/*    */     
/* 52 */     if (predicate.test(paramUseOnContext)) {
/* 53 */       Player player = paramUseOnContext.getPlayer();
/* 54 */       level.playSound((Entity)player, blockPos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
/*    */       
/* 56 */       if (!level.isClientSide()) {
/* 57 */         consumer.accept(paramUseOnContext);
/* 58 */         if (player != null) {
/* 59 */           paramUseOnContext.getItemInHand().hurtAndBreak(1, (LivingEntity)player, paramUseOnContext.getHand().asEquipmentSlot());
/*    */         }
/*    */       } 
/* 62 */       return (InteractionResult)InteractionResult.SUCCESS;
/*    */     } 
/*    */     
/* 65 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */   
/*    */   public static Consumer<UseOnContext> changeIntoState(BlockState paramBlockState) {
/* 69 */     return paramUseOnContext -> {
/*    */         paramUseOnContext.getLevel().setBlock(paramUseOnContext.getClickedPos(), paramBlockState, 11);
/*    */         paramUseOnContext.getLevel().gameEvent((Holder)GameEvent.BLOCK_CHANGE, paramUseOnContext.getClickedPos(), GameEvent.Context.of((Entity)paramUseOnContext.getPlayer(), paramBlockState));
/*    */       };
/*    */   }
/*    */   
/*    */   public static Consumer<UseOnContext> changeIntoStateAndDropItem(BlockState paramBlockState, ItemLike paramItemLike) {
/* 76 */     return paramUseOnContext -> {
/*    */         paramUseOnContext.getLevel().setBlock(paramUseOnContext.getClickedPos(), paramBlockState, 11);
/*    */         paramUseOnContext.getLevel().gameEvent((Holder)GameEvent.BLOCK_CHANGE, paramUseOnContext.getClickedPos(), GameEvent.Context.of((Entity)paramUseOnContext.getPlayer(), paramBlockState));
/*    */         Block.popResourceFromFace(paramUseOnContext.getLevel(), paramUseOnContext.getClickedPos(), paramUseOnContext.getClickedFace(), new ItemStack(paramItemLike));
/*    */       };
/*    */   }
/*    */   
/*    */   public static boolean onlyIfAirAbove(UseOnContext paramUseOnContext) {
/* 84 */     return (paramUseOnContext.getClickedFace() != Direction.DOWN && paramUseOnContext.getLevel().getBlockState(paramUseOnContext.getClickedPos().above()).isAir());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\HoeItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */