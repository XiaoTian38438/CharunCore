/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.sniffer.Sniffer;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SnifferEggBlock extends Block {
/*  28 */   public static final MapCodec<SnifferEggBlock> CODEC = simpleCodec(SnifferEggBlock::new);
/*     */   public static final int MAX_HATCH_LEVEL = 2;
/*     */   
/*     */   public MapCodec<SnifferEggBlock> codec() {
/*  32 */     return CODEC;
/*     */   }
/*     */ 
/*     */   
/*  36 */   public static final IntegerProperty HATCH = BlockStateProperties.HATCH;
/*     */   
/*     */   private static final int REGULAR_HATCH_TIME_TICKS = 24000;
/*     */   
/*     */   private static final int BOOSTED_HATCH_TIME_TICKS = 12000;
/*     */   private static final int RANDOM_HATCH_OFFSET_TICKS = 300;
/*  42 */   private static final VoxelShape SHAPE = Block.column(14.0D, 12.0D, 0.0D, 16.0D);
/*     */   
/*     */   public SnifferEggBlock(BlockBehaviour.Properties paramProperties) {
/*  45 */     super(paramProperties);
/*  46 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)HATCH, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/*  51 */     paramBuilder.add(new Property[] { (Property)HATCH });
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  56 */     return SHAPE;
/*     */   }
/*     */   
/*     */   public int getHatchLevel(BlockState paramBlockState) {
/*  60 */     return ((Integer)paramBlockState.getValue((Property)HATCH)).intValue();
/*     */   }
/*     */   
/*     */   private boolean isReadyToHatch(BlockState paramBlockState) {
/*  64 */     return (getHatchLevel(paramBlockState) == 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  69 */     if (!isReadyToHatch(paramBlockState)) {
/*  70 */       paramServerLevel.playSound(null, paramBlockPos, SoundEvents.SNIFFER_EGG_CRACK, SoundSource.BLOCKS, 0.7F, 0.9F + paramRandomSource.nextFloat() * 0.2F);
/*  71 */       paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)HATCH, Integer.valueOf(getHatchLevel(paramBlockState) + 1)), 2);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  76 */     paramServerLevel.playSound(null, paramBlockPos, SoundEvents.SNIFFER_EGG_HATCH, SoundSource.BLOCKS, 0.7F, 0.9F + paramRandomSource.nextFloat() * 0.2F);
/*  77 */     paramServerLevel.destroyBlock(paramBlockPos, false);
/*     */     
/*  79 */     Sniffer sniffer = (Sniffer)EntityType.SNIFFER.create((Level)paramServerLevel, EntitySpawnReason.BREEDING);
/*  80 */     if (sniffer != null) {
/*  81 */       Vec3 vec3 = paramBlockPos.getCenter();
/*     */       
/*  83 */       sniffer.setBaby(true);
/*  84 */       sniffer.snapTo(vec3.x(), vec3.y(), vec3.z(), Mth.wrapDegrees(paramServerLevel.random.nextFloat() * 360.0F), 0.0F);
/*     */       
/*  86 */       paramServerLevel.addFreshEntity((Entity)sniffer);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/*  92 */     boolean bool = hatchBoost((BlockGetter)paramLevel, paramBlockPos);
/*     */     
/*  94 */     if (!paramLevel.isClientSide() && bool) {
/*  95 */       paramLevel.levelEvent(3009, paramBlockPos, 0);
/*     */     }
/*     */     
/*  98 */     char c = bool ? '⻠' : '巀';
/*  99 */     int i = c / 3;
/*     */     
/* 101 */     paramLevel.gameEvent((Holder)GameEvent.BLOCK_PLACE, paramBlockPos, GameEvent.Context.of(paramBlockState1));
/* 102 */     paramLevel.scheduleTick(paramBlockPos, this, i + paramLevel.random.nextInt(300));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 107 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean hatchBoost(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 111 */     return paramBlockGetter.getBlockState(paramBlockPos.below()).is(BlockTags.SNIFFER_EGG_HATCH_BOOST);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SnifferEggBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */