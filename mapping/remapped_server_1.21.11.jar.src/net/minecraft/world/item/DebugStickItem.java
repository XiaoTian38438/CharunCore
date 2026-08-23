/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.component.DebugStickState;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class DebugStickItem
/*     */   extends Item
/*     */ {
/*     */   public DebugStickItem(Item.Properties paramProperties) {
/*  26 */     super(paramProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canDestroyBlock(ItemStack paramItemStack, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, LivingEntity paramLivingEntity) {
/*  31 */     if (!paramLevel.isClientSide() && paramLivingEntity instanceof Player) { Player player = (Player)paramLivingEntity;
/*  32 */       handleInteraction(player, paramBlockState, (LevelAccessor)paramLevel, paramBlockPos, false, paramItemStack); }
/*     */ 
/*     */     
/*  35 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult useOn(UseOnContext paramUseOnContext) {
/*  40 */     Player player = paramUseOnContext.getPlayer();
/*  41 */     Level level = paramUseOnContext.getLevel();
/*     */     
/*  43 */     if (!level.isClientSide() && player != null) {
/*  44 */       BlockPos blockPos = paramUseOnContext.getClickedPos();
/*  45 */       if (!handleInteraction(player, level.getBlockState(blockPos), (LevelAccessor)level, blockPos, true, paramUseOnContext.getItemInHand())) {
/*  46 */         return (InteractionResult)InteractionResult.FAIL;
/*     */       }
/*     */     } 
/*     */     
/*  50 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   private boolean handleInteraction(Player paramPlayer, BlockState paramBlockState, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, boolean paramBoolean, ItemStack paramItemStack) {
/*  54 */     if (!paramPlayer.canUseGameMasterBlocks()) {
/*  55 */       return false;
/*     */     }
/*     */     
/*  58 */     Holder holder = paramBlockState.getBlockHolder();
/*  59 */     StateDefinition stateDefinition = ((Block)holder.value()).getStateDefinition();
/*  60 */     Collection<Property> collection = stateDefinition.getProperties();
/*     */     
/*  62 */     if (collection.isEmpty()) {
/*  63 */       message(paramPlayer, (Component)Component.translatable(this.descriptionId + ".empty", new Object[] { holder.getRegisteredName() }));
/*  64 */       return false;
/*     */     } 
/*     */     
/*  67 */     DebugStickState debugStickState = (DebugStickState)paramItemStack.get(DataComponents.DEBUG_STICK_STATE);
/*  68 */     if (debugStickState == null) {
/*  69 */       return false;
/*     */     }
/*     */     
/*  72 */     Property<Comparable> property = (Property)debugStickState.properties().get(holder);
/*  73 */     if (paramBoolean) {
/*  74 */       if (property == null) {
/*  75 */         property = collection.iterator().next();
/*     */       }
/*     */       
/*  78 */       BlockState blockState = cycleState(paramBlockState, property, paramPlayer.isSecondaryUseActive());
/*  79 */       paramLevelAccessor.setBlock(paramBlockPos, blockState, 18);
/*  80 */       message(paramPlayer, (Component)Component.translatable(this.descriptionId + ".update", new Object[] { property.getName(), getNameHelper(blockState, property) }));
/*     */     } else {
/*  82 */       property = getRelative((Iterable)collection, property, paramPlayer.isSecondaryUseActive());
/*  83 */       paramItemStack.set(DataComponents.DEBUG_STICK_STATE, debugStickState.withProperty(holder, property));
/*  84 */       message(paramPlayer, (Component)Component.translatable(this.descriptionId + ".select", new Object[] { property.getName(), getNameHelper(paramBlockState, property) }));
/*     */     } 
/*  86 */     return true;
/*     */   }
/*     */   
/*     */   private static <T extends Comparable<T>> BlockState cycleState(BlockState paramBlockState, Property<T> paramProperty, boolean paramBoolean) {
/*  90 */     return (BlockState)paramBlockState.setValue(paramProperty, getRelative(paramProperty.getPossibleValues(), paramBlockState.getValue(paramProperty), paramBoolean));
/*     */   }
/*     */   
/*     */   private static <T> T getRelative(Iterable<T> paramIterable, T paramT, boolean paramBoolean) {
/*  94 */     return paramBoolean ? (T)Util.findPreviousInIterable(paramIterable, paramT) : (T)Util.findNextInIterable(paramIterable, paramT);
/*     */   }
/*     */   
/*     */   private static void message(Player paramPlayer, Component paramComponent) {
/*  98 */     ((ServerPlayer)paramPlayer).sendSystemMessage(paramComponent, true);
/*     */   }
/*     */   
/*     */   private static <T extends Comparable<T>> String getNameHelper(BlockState paramBlockState, Property<T> paramProperty) {
/* 102 */     return paramProperty.getName(paramBlockState.getValue(paramProperty));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\DebugStickItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */