/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.TriState;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.BlockItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class FlowerPotBlock extends Block {
/*     */   static {
/*  33 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)BuiltInRegistries.BLOCK.byNameCodec().fieldOf("potted").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, FlowerPotBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<FlowerPotBlock> CODEC;
/*     */   
/*     */   public MapCodec<FlowerPotBlock> codec() {
/*  40 */     return CODEC;
/*     */   }
/*     */   
/*  43 */   private static final Map<Block, Block> POTTED_BY_CONTENT = Maps.newHashMap();
/*     */   
/*  45 */   private static final VoxelShape SHAPE = Block.column(6.0D, 0.0D, 6.0D);
/*     */   
/*     */   private final Block potted;
/*     */   
/*     */   public FlowerPotBlock(Block paramBlock, BlockBehaviour.Properties paramProperties) {
/*  50 */     super(paramProperties);
/*  51 */     this.potted = paramBlock;
/*     */     
/*  53 */     POTTED_BY_CONTENT.put(paramBlock, this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  58 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack paramItemStack, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/*  63 */     Item item = paramItemStack.getItem(); BlockItem blockItem = (BlockItem)item; BlockState blockState = ((item instanceof BlockItem) ? POTTED_BY_CONTENT.getOrDefault(blockItem.getBlock(), Blocks.AIR) : Blocks.AIR).defaultBlockState();
/*  64 */     if (blockState.isAir()) {
/*  65 */       return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */     }
/*     */     
/*  68 */     if (!isEmpty()) {
/*  69 */       return (InteractionResult)InteractionResult.CONSUME;
/*     */     }
/*     */     
/*  72 */     paramLevel.setBlock(paramBlockPos, blockState, 3);
/*  73 */     paramLevel.gameEvent((Entity)paramPlayer, (Holder)GameEvent.BLOCK_CHANGE, paramBlockPos);
/*  74 */     paramPlayer.awardStat(Stats.POT_FLOWER);
/*     */     
/*  76 */     paramItemStack.consume(1, (LivingEntity)paramPlayer);
/*  77 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*  82 */     if (isEmpty()) {
/*  83 */       return (InteractionResult)InteractionResult.CONSUME;
/*     */     }
/*     */     
/*  86 */     ItemStack itemStack = new ItemStack(this.potted);
/*     */     
/*  88 */     if (!paramPlayer.addItem(itemStack)) {
/*  89 */       paramPlayer.drop(itemStack, false);
/*     */     }
/*     */     
/*  92 */     paramLevel.setBlock(paramBlockPos, Blocks.FLOWER_POT.defaultBlockState(), 3);
/*  93 */     paramLevel.gameEvent((Entity)paramPlayer, (Holder)GameEvent.BLOCK_CHANGE, paramBlockPos);
/*     */     
/*  95 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 100 */     if (isEmpty()) {
/* 101 */       return super.getCloneItemStack(paramLevelReader, paramBlockPos, paramBlockState, paramBoolean);
/*     */     }
/* 103 */     return new ItemStack(this.potted);
/*     */   }
/*     */   
/*     */   private boolean isEmpty() {
/* 107 */     return (this.potted == Blocks.AIR);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 112 */     if (paramDirection == Direction.DOWN && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/* 113 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 116 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */   
/*     */   public Block getPotted() {
/* 120 */     return this.potted;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 125 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isRandomlyTicking(BlockState paramBlockState) {
/* 130 */     return (paramBlockState.is(Blocks.POTTED_OPEN_EYEBLOSSOM) || paramBlockState.is(Blocks.POTTED_CLOSED_EYEBLOSSOM));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 135 */     if (isRandomlyTicking(paramBlockState)) {
/* 136 */       boolean bool1 = (this.potted == Blocks.OPEN_EYEBLOSSOM);
/* 137 */       boolean bool2 = ((TriState)paramServerLevel.environmentAttributes().getValue(EnvironmentAttributes.EYEBLOSSOM_OPEN, paramBlockPos)).toBoolean(bool1);
/* 138 */       if (bool1 != bool2) {
/* 139 */         paramServerLevel.setBlock(paramBlockPos, opposite(paramBlockState), 3);
/* 140 */         EyeblossomBlock.Type type = EyeblossomBlock.Type.fromBoolean(bool1).transform();
/* 141 */         type.spawnTransformParticle(paramServerLevel, paramBlockPos, paramRandomSource);
/* 142 */         paramServerLevel.playSound(null, paramBlockPos, type.longSwitchSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */       } 
/*     */     } 
/* 145 */     super.randomTick(paramBlockState, paramServerLevel, paramBlockPos, paramRandomSource);
/*     */   }
/*     */   
/*     */   public BlockState opposite(BlockState paramBlockState) {
/* 149 */     if (paramBlockState.is(Blocks.POTTED_OPEN_EYEBLOSSOM)) {
/* 150 */       return Blocks.POTTED_CLOSED_EYEBLOSSOM.defaultBlockState();
/*     */     }
/* 152 */     if (paramBlockState.is(Blocks.POTTED_CLOSED_EYEBLOSSOM)) {
/* 153 */       return Blocks.POTTED_OPEN_EYEBLOSSOM.defaultBlockState();
/*     */     }
/* 155 */     return paramBlockState;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\FlowerPotBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */