/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.BlockUtil;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Relative;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.portal.PortalShape;
/*     */ import net.minecraft.world.level.portal.TeleportTransition;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class NetherPortalBlock extends Block implements Portal {
/*  47 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  48 */   public static final MapCodec<NetherPortalBlock> CODEC = simpleCodec(NetherPortalBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<NetherPortalBlock> codec() {
/*  52 */     return CODEC;
/*     */   }
/*     */   
/*  55 */   public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
/*     */   
/*  57 */   private static final Map<Direction.Axis, VoxelShape> SHAPES = Shapes.rotateHorizontalAxis(Block.column(4.0D, 16.0D, 0.0D, 16.0D));
/*     */   
/*     */   public NetherPortalBlock(BlockBehaviour.Properties paramProperties) {
/*  60 */     super(paramProperties);
/*  61 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AXIS, (Comparable)Direction.Axis.X));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  66 */     return SHAPES.get(paramBlockState.getValue((Property)AXIS));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  71 */     if (paramServerLevel.isSpawningMonsters() && ((Boolean)paramServerLevel.environmentAttributes().getValue(EnvironmentAttributes.NETHER_PORTAL_SPAWNS_PIGLINS, paramBlockPos)).booleanValue() && paramRandomSource.nextInt(2000) < paramServerLevel.getDifficulty().getId() && paramServerLevel.anyPlayerCloseEnoughForSpawning(paramBlockPos)) {
/*     */       
/*  73 */       while (paramServerLevel.getBlockState(paramBlockPos).is(this)) {
/*  74 */         paramBlockPos = paramBlockPos.below();
/*     */       }
/*  76 */       if (paramServerLevel.getBlockState(paramBlockPos).isValidSpawn((BlockGetter)paramServerLevel, paramBlockPos, EntityType.ZOMBIFIED_PIGLIN)) {
/*  77 */         Entity entity = EntityType.ZOMBIFIED_PIGLIN.spawn(paramServerLevel, paramBlockPos.above(), EntitySpawnReason.STRUCTURE);
/*  78 */         if (entity != null) {
/*  79 */           entity.setPortalCooldown();
/*  80 */           Entity entity1 = entity.getVehicle();
/*  81 */           if (entity1 != null) {
/*  82 */             entity1.setPortalCooldown();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  91 */     Direction.Axis axis1 = paramDirection.getAxis();
/*  92 */     Direction.Axis axis2 = (Direction.Axis)paramBlockState1.getValue((Property)AXIS);
/*     */     
/*  94 */     boolean bool = (axis2 != axis1 && axis1.isHorizontal()) ? true : false;
/*  95 */     if (bool || paramBlockState2.is(this) || PortalShape.findAnyShape((BlockGetter)paramLevelReader, paramBlockPos1, axis2).isComplete()) {
/*  96 */       return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */     }
/*     */     
/*  99 */     return Blocks.AIR.defaultBlockState();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier, boolean paramBoolean) {
/* 104 */     if (paramEntity.canUsePortal(false)) {
/* 105 */       paramEntity.setAsInsidePortal(this, paramBlockPos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPortalTransitionTime(ServerLevel paramServerLevel, Entity paramEntity) {
/* 111 */     if (paramEntity instanceof Player) { Player player = (Player)paramEntity;
/* 112 */       return Math.max(0, ((Integer)paramServerLevel.getGameRules().get(
/* 113 */             (player.getAbilities()).invulnerable ? 
/* 114 */             GameRules.PLAYERS_NETHER_PORTAL_CREATIVE_DELAY : 
/* 115 */             GameRules.PLAYERS_NETHER_PORTAL_DEFAULT_DELAY)).intValue()); }
/*     */     
/* 117 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public TeleportTransition getPortalDestination(ServerLevel paramServerLevel, Entity paramEntity, BlockPos paramBlockPos) {
/* 122 */     ResourceKey resourceKey = (paramServerLevel.dimension() == Level.NETHER) ? Level.OVERWORLD : Level.NETHER;
/* 123 */     ServerLevel serverLevel = paramServerLevel.getServer().getLevel(resourceKey);
/* 124 */     if (serverLevel == null) {
/* 125 */       return null;
/*     */     }
/*     */     
/* 128 */     boolean bool = (serverLevel.dimension() == Level.NETHER) ? true : false;
/*     */     
/* 130 */     WorldBorder worldBorder = serverLevel.getWorldBorder();
/* 131 */     double d = DimensionType.getTeleportationScale(paramServerLevel.dimensionType(), serverLevel.dimensionType());
/* 132 */     BlockPos blockPos = worldBorder.clampToBounds(paramEntity
/* 133 */         .getX() * d, paramEntity
/* 134 */         .getY(), paramEntity
/* 135 */         .getZ() * d);
/*     */ 
/*     */     
/* 138 */     return getExitPortal(serverLevel, paramEntity, paramBlockPos, blockPos, bool, worldBorder);
/*     */   } private TeleportTransition getExitPortal(ServerLevel paramServerLevel, Entity paramEntity, BlockPos paramBlockPos1, BlockPos paramBlockPos2, boolean paramBoolean, WorldBorder paramWorldBorder) {
/*     */     BlockUtil.FoundRectangle foundRectangle;
/*     */     TeleportTransition.PostTeleportTransition postTeleportTransition;
/* 142 */     Optional<BlockPos> optional = paramServerLevel.getPortalForcer().findClosestPortalPosition(paramBlockPos2, paramBoolean, paramWorldBorder);
/*     */ 
/*     */ 
/*     */     
/* 146 */     if (optional.isPresent()) {
/* 147 */       BlockPos blockPos = optional.get();
/* 148 */       BlockState blockState = paramServerLevel.getBlockState(blockPos);
/*     */       
/* 150 */       foundRectangle = BlockUtil.getLargestRectangleAround(blockPos, (Direction.Axis)blockState.getValue((Property)BlockStateProperties.HORIZONTAL_AXIS), 21, Direction.Axis.Y, 21, paramBlockPos -> (paramServerLevel.getBlockState(paramBlockPos) == paramBlockState));
/* 151 */       postTeleportTransition = TeleportTransition.PLAY_PORTAL_SOUND.then(paramEntity -> paramEntity.placePortalTicket(paramBlockPos));
/*     */     } else {
/* 153 */       Direction.Axis axis = paramEntity.level().getBlockState(paramBlockPos1).getOptionalValue((Property)AXIS).orElse(Direction.Axis.X);
/* 154 */       Optional<BlockUtil.FoundRectangle> optional1 = paramServerLevel.getPortalForcer().createPortal(paramBlockPos2, axis);
/* 155 */       if (optional1.isEmpty()) {
/* 156 */         LOGGER.error("Unable to create a portal, likely target out of worldborder");
/* 157 */         return null;
/*     */       } 
/*     */       
/* 160 */       foundRectangle = optional1.get();
/* 161 */       postTeleportTransition = TeleportTransition.PLAY_PORTAL_SOUND.then(TeleportTransition.PLACE_PORTAL_TICKET);
/*     */     } 
/*     */     
/* 164 */     return getDimensionTransitionFromExit(paramEntity, paramBlockPos1, foundRectangle, paramServerLevel, postTeleportTransition);
/*     */   }
/*     */ 
/*     */   
/*     */   private static TeleportTransition getDimensionTransitionFromExit(Entity paramEntity, BlockPos paramBlockPos, BlockUtil.FoundRectangle paramFoundRectangle, ServerLevel paramServerLevel, TeleportTransition.PostTeleportTransition paramPostTeleportTransition) {
/*     */     Direction.Axis axis;
/*     */     Vec3 vec3;
/* 171 */     BlockState blockState = paramEntity.level().getBlockState(paramBlockPos);
/* 172 */     if (blockState.hasProperty((Property)BlockStateProperties.HORIZONTAL_AXIS)) {
/* 173 */       axis = (Direction.Axis)blockState.getValue((Property)BlockStateProperties.HORIZONTAL_AXIS);
/*     */ 
/*     */       
/* 176 */       BlockUtil.FoundRectangle foundRectangle = BlockUtil.getLargestRectangleAround(paramBlockPos, axis, 21, Direction.Axis.Y, 21, paramBlockPos -> (paramEntity.level().getBlockState(paramBlockPos) == paramBlockState));
/* 177 */       vec3 = paramEntity.getRelativePortalPosition(axis, foundRectangle);
/*     */     } else {
/*     */       
/* 180 */       axis = Direction.Axis.X;
/* 181 */       vec3 = new Vec3(0.5D, 0.0D, 0.0D);
/*     */     } 
/* 183 */     return createDimensionTransition(paramServerLevel, paramFoundRectangle, axis, vec3, paramEntity, paramPostTeleportTransition);
/*     */   }
/*     */   
/*     */   private static TeleportTransition createDimensionTransition(ServerLevel paramServerLevel, BlockUtil.FoundRectangle paramFoundRectangle, Direction.Axis paramAxis, Vec3 paramVec3, Entity paramEntity, TeleportTransition.PostTeleportTransition paramPostTeleportTransition) {
/* 187 */     BlockPos blockPos = paramFoundRectangle.minCorner;
/* 188 */     BlockState blockState = paramServerLevel.getBlockState(blockPos);
/* 189 */     Direction.Axis axis = blockState.getOptionalValue((Property)BlockStateProperties.HORIZONTAL_AXIS).orElse(Direction.Axis.X);
/* 190 */     double d1 = paramFoundRectangle.axis1Size;
/* 191 */     double d2 = paramFoundRectangle.axis2Size;
/* 192 */     EntityDimensions entityDimensions = paramEntity.getDimensions(paramEntity.getPose());
/*     */     
/* 194 */     boolean bool1 = (paramAxis == axis) ? false : true;
/*     */     
/* 196 */     double d3 = entityDimensions.width() / 2.0D + (d1 - entityDimensions.width()) * paramVec3.x();
/* 197 */     double d4 = (d2 - entityDimensions.height()) * paramVec3.y();
/* 198 */     double d5 = 0.5D + paramVec3.z();
/*     */     
/* 200 */     boolean bool2 = (axis == Direction.Axis.X) ? true : false;
/*     */ 
/*     */ 
/*     */     
/* 204 */     Vec3 vec31 = new Vec3(blockPos.getX() + (bool2 ? d3 : d5), blockPos.getY() + d4, blockPos.getZ() + (bool2 ? d5 : d3));
/*     */ 
/*     */     
/* 207 */     Vec3 vec32 = PortalShape.findCollisionFreePosition(vec31, paramServerLevel, paramEntity, entityDimensions);
/* 208 */     return new TeleportTransition(paramServerLevel, vec32, Vec3.ZERO, bool1, 0.0F, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 214 */         Relative.union(new Set[] { Relative.DELTA, Relative.ROTATION }, ), paramPostTeleportTransition);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Portal.Transition getLocalTransition() {
/* 220 */     return Portal.Transition.CONFUSION;
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 225 */     if (paramRandomSource.nextInt(100) == 0) {
/* 226 */       paramLevel.playLocalSound(paramBlockPos.getX() + 0.5D, paramBlockPos.getY() + 0.5D, paramBlockPos.getZ() + 0.5D, SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 0.5F, paramRandomSource.nextFloat() * 0.4F + 0.8F, false);
/*     */     }
/*     */     
/* 229 */     for (byte b = 0; b < 4; b++) {
/* 230 */       double d1 = paramBlockPos.getX() + paramRandomSource.nextDouble();
/* 231 */       double d2 = paramBlockPos.getY() + paramRandomSource.nextDouble();
/* 232 */       double d3 = paramBlockPos.getZ() + paramRandomSource.nextDouble();
/* 233 */       double d4 = (paramRandomSource.nextFloat() - 0.5D) * 0.5D;
/* 234 */       double d5 = (paramRandomSource.nextFloat() - 0.5D) * 0.5D;
/* 235 */       double d6 = (paramRandomSource.nextFloat() - 0.5D) * 0.5D;
/*     */       
/* 237 */       int i = paramRandomSource.nextInt(2) * 2 - 1;
/* 238 */       if (paramLevel.getBlockState(paramBlockPos.west()).is(this) || paramLevel.getBlockState(paramBlockPos.east()).is(this)) {
/* 239 */         d3 = paramBlockPos.getZ() + 0.5D + 0.25D * i;
/* 240 */         d6 = (paramRandomSource.nextFloat() * 2.0F * i);
/*     */       } else {
/* 242 */         d1 = paramBlockPos.getX() + 0.5D + 0.25D * i;
/* 243 */         d4 = (paramRandomSource.nextFloat() * 2.0F * i);
/*     */       } 
/*     */       
/* 246 */       paramLevel.addParticle((ParticleOptions)ParticleTypes.PORTAL, d1, d2, d3, d4, d5, d6);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 252 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 257 */     switch (paramRotation) {
/*     */       case COUNTERCLOCKWISE_90:
/*     */       case CLOCKWISE_90:
/* 260 */         switch ((Direction.Axis)paramBlockState.getValue((Property)AXIS)) {
/*     */           case COUNTERCLOCKWISE_90:
/* 262 */             return (BlockState)paramBlockState.setValue((Property)AXIS, (Comparable)Direction.Axis.Z);
/*     */           case CLOCKWISE_90:
/* 264 */             return (BlockState)paramBlockState.setValue((Property)AXIS, (Comparable)Direction.Axis.X);
/*     */         } 
/* 266 */         return paramBlockState;
/*     */     } 
/*     */     
/* 269 */     return paramBlockState;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 275 */     paramBuilder.add(new Property[] { (Property)AXIS });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\NetherPortalBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */