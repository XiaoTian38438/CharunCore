/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.StructureBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.StructureMode;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class StructureBlock extends BaseEntityBlock implements GameMasterBlock {
/*  23 */   public static final MapCodec<StructureBlock> CODEC = simpleCodec(StructureBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<StructureBlock> codec() {
/*  27 */     return CODEC;
/*     */   }
/*     */   
/*  30 */   public static final EnumProperty<StructureMode> MODE = BlockStateProperties.STRUCTUREBLOCK_MODE;
/*     */   
/*     */   protected StructureBlock(BlockBehaviour.Properties paramProperties) {
/*  33 */     super(paramProperties);
/*     */     
/*  35 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)MODE, (Comparable)StructureMode.LOAD));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  40 */     return (BlockEntity)new StructureBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*  45 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/*  46 */     if (blockEntity instanceof StructureBlockEntity) {
/*  47 */       return ((StructureBlockEntity)blockEntity).usedBy(paramPlayer) ? (InteractionResult)InteractionResult.SUCCESS : (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/*  50 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/*  55 */     if (paramLevel.isClientSide()) {
/*     */       return;
/*     */     }
/*  58 */     if (paramLivingEntity != null) {
/*  59 */       BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/*  60 */       if (blockEntity instanceof StructureBlockEntity) {
/*  61 */         ((StructureBlockEntity)blockEntity).createdBy(paramLivingEntity);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/*  68 */     paramBuilder.add(new Property[] { (Property)MODE });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/*  73 */     if (!(paramLevel instanceof ServerLevel)) {
/*     */       return;
/*     */     }
/*     */     
/*  77 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/*  78 */     if (!(blockEntity instanceof StructureBlockEntity)) {
/*     */       return;
/*     */     }
/*     */     
/*  82 */     StructureBlockEntity structureBlockEntity = (StructureBlockEntity)blockEntity;
/*     */     
/*  84 */     boolean bool1 = paramLevel.hasNeighborSignal(paramBlockPos);
/*  85 */     boolean bool2 = structureBlockEntity.isPowered();
/*     */     
/*  87 */     if (bool1 && !bool2) {
/*  88 */       structureBlockEntity.setPowered(true);
/*  89 */       trigger((ServerLevel)paramLevel, structureBlockEntity);
/*  90 */     } else if (!bool1 && bool2) {
/*  91 */       structureBlockEntity.setPowered(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void trigger(ServerLevel paramServerLevel, StructureBlockEntity paramStructureBlockEntity) {
/*  96 */     switch (paramStructureBlockEntity.getMode()) {
/*     */       case SAVE:
/*  98 */         paramStructureBlockEntity.saveStructure(false);
/*     */         break;
/*     */       case LOAD:
/* 101 */         paramStructureBlockEntity.placeStructure(paramServerLevel);
/*     */         break;
/*     */       case CORNER:
/* 104 */         paramStructureBlockEntity.unloadStructure();
/*     */         break;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\StructureBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */