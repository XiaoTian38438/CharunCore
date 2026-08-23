/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.SimpleMenuProvider;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ContainerLevelAccess;
/*     */ import net.minecraft.world.inventory.EnchantmentMenu;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.EnchantingTableBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class EnchantingTableBlock extends BaseEntityBlock {
/*  31 */   public static final MapCodec<EnchantingTableBlock> CODEC = simpleCodec(EnchantingTableBlock::new);
/*     */   public static final List<BlockPos> BOOKSHELF_OFFSETS;
/*     */   
/*     */   public MapCodec<EnchantingTableBlock> codec() {
/*  35 */     return CODEC;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  41 */     BOOKSHELF_OFFSETS = BlockPos.betweenClosedStream(-2, 0, -2, 2, 1, 2).filter(paramBlockPos -> (Math.abs(paramBlockPos.getX()) == 2 || Math.abs(paramBlockPos.getZ()) == 2)).map(BlockPos::immutable).toList();
/*     */   }
/*  43 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 12.0D);
/*     */   
/*     */   protected EnchantingTableBlock(BlockBehaviour.Properties paramProperties) {
/*  46 */     super(paramProperties);
/*     */   }
/*     */   
/*     */   public static boolean isValidBookShelf(Level paramLevel, BlockPos paramBlockPos1, BlockPos paramBlockPos2) {
/*  50 */     return (paramLevel.getBlockState(paramBlockPos1.offset((Vec3i)paramBlockPos2)).is(BlockTags.ENCHANTMENT_POWER_PROVIDER) && paramLevel.getBlockState(paramBlockPos1.offset(paramBlockPos2.getX() / 2, paramBlockPos2.getY(), paramBlockPos2.getZ() / 2)).is(BlockTags.ENCHANTMENT_POWER_TRANSMITTER));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean useShapeForLightOcclusion(BlockState paramBlockState) {
/*  55 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  60 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  65 */     super.animateTick(paramBlockState, paramLevel, paramBlockPos, paramRandomSource);
/*     */     
/*  67 */     for (BlockPos blockPos : BOOKSHELF_OFFSETS) {
/*  68 */       if (paramRandomSource.nextInt(16) == 0 && isValidBookShelf(paramLevel, paramBlockPos, blockPos)) {
/*  69 */         paramLevel.addParticle((ParticleOptions)ParticleTypes.ENCHANT, paramBlockPos.getX() + 0.5D, paramBlockPos.getY() + 2.0D, paramBlockPos.getZ() + 0.5D, (blockPos.getX() + paramRandomSource.nextFloat()) - 0.5D, (blockPos.getY() - paramRandomSource.nextFloat() - 1.0F), (blockPos.getZ() + paramRandomSource.nextFloat()) - 0.5D);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  76 */     return (BlockEntity)new EnchantingTableBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/*  81 */     return paramLevel.isClientSide() ? createTickerHelper(paramBlockEntityType, BlockEntityType.ENCHANTING_TABLE, EnchantingTableBlockEntity::bookAnimationTick) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*  86 */     if (!paramLevel.isClientSide()) {
/*  87 */       paramPlayer.openMenu(paramBlockState.getMenuProvider(paramLevel, paramBlockPos));
/*     */     }
/*  89 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected MenuProvider getMenuProvider(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/*  94 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/*  95 */     if (blockEntity instanceof EnchantingTableBlockEntity) { EnchantingTableBlockEntity enchantingTableBlockEntity = (EnchantingTableBlockEntity)blockEntity;
/*  96 */       Component component = enchantingTableBlockEntity.getDisplayName();
/*     */       
/*  98 */       return (MenuProvider)new SimpleMenuProvider((paramInt, paramInventory, paramPlayer) -> new EnchantmentMenu(paramInt, paramInventory, ContainerLevelAccess.create(paramLevel, paramBlockPos)), component); }
/*     */     
/* 100 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 105 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\EnchantingTableBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */