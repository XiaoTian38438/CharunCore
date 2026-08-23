/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.List;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.EnchantmentTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.animal.bee.Bee;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.BlockItemStateProperties;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeehiveBlock
/*     */   extends BaseEntityBlock
/*     */ {
/*  64 */   public static final MapCodec<BeehiveBlock> CODEC = simpleCodec(BeehiveBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<BeehiveBlock> codec() {
/*  68 */     return CODEC;
/*     */   }
/*     */   
/*  71 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*  72 */   public static final IntegerProperty HONEY_LEVEL = BlockStateProperties.LEVEL_HONEY;
/*     */   
/*     */   public static final int MAX_HONEY_LEVELS = 5;
/*     */   
/*     */   public BeehiveBlock(BlockBehaviour.Properties paramProperties) {
/*  77 */     super(paramProperties);
/*  78 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)HONEY_LEVEL, Integer.valueOf(0))).setValue((Property)FACING, (Comparable)Direction.NORTH));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState paramBlockState) {
/*  83 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/*  88 */     return ((Integer)paramBlockState.getValue((Property)HONEY_LEVEL)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerDestroy(Level paramLevel, Player paramPlayer, BlockPos paramBlockPos, BlockState paramBlockState, BlockEntity paramBlockEntity, ItemStack paramItemStack) {
/*  93 */     super.playerDestroy(paramLevel, paramPlayer, paramBlockPos, paramBlockState, paramBlockEntity, paramItemStack);
/*     */     
/*  95 */     if (!paramLevel.isClientSide() && 
/*  96 */       paramBlockEntity instanceof BeehiveBlockEntity) { BeehiveBlockEntity beehiveBlockEntity = (BeehiveBlockEntity)paramBlockEntity;
/*  97 */       if (!EnchantmentHelper.hasTag(paramItemStack, EnchantmentTags.PREVENTS_BEE_SPAWNS_WHEN_MINING)) {
/*  98 */         beehiveBlockEntity.emptyAllLivingFromHive(paramPlayer, paramBlockState, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
/*     */         
/* 100 */         Containers.updateNeighboursAfterDestroy(paramBlockState, paramLevel, paramBlockPos);
/*     */         
/* 102 */         angerNearbyBees(paramLevel, paramBlockPos);
/*     */       } 
/*     */       
/* 105 */       CriteriaTriggers.BEE_NEST_DESTROYED.trigger((ServerPlayer)paramPlayer, paramBlockState, paramItemStack, beehiveBlockEntity.getOccupantCount()); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onExplosionHit(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, Explosion paramExplosion, BiConsumer<ItemStack, BlockPos> paramBiConsumer) {
/* 112 */     super.onExplosionHit(paramBlockState, paramServerLevel, paramBlockPos, paramExplosion, paramBiConsumer);
/* 113 */     angerNearbyBees((Level)paramServerLevel, paramBlockPos);
/*     */   }
/*     */   
/*     */   private void angerNearbyBees(Level paramLevel, BlockPos paramBlockPos) {
/* 117 */     AABB aABB = (new AABB(paramBlockPos)).inflate(8.0D, 6.0D, 8.0D);
/* 118 */     List list = paramLevel.getEntitiesOfClass(Bee.class, aABB);
/* 119 */     if (!list.isEmpty()) {
/* 120 */       List list1 = paramLevel.getEntitiesOfClass(Player.class, aABB);
/* 121 */       if (list1.isEmpty()) {
/*     */         return;
/*     */       }
/* 124 */       for (Bee bee : list) {
/* 125 */         if (bee.getTarget() == null) {
/* 126 */           Player player = (Player)Util.getRandom(list1, paramLevel.random);
/* 127 */           bee.setTarget((LivingEntity)player);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void dropHoneycomb(ServerLevel paramServerLevel, ItemStack paramItemStack, BlockState paramBlockState, BlockEntity paramBlockEntity, Entity paramEntity, BlockPos paramBlockPos) {
/* 134 */     dropFromBlockInteractLootTable(paramServerLevel, BuiltInLootTables.HARVEST_BEEHIVE, paramBlockState, paramBlockEntity, paramItemStack, paramEntity, (paramServerLevel, paramItemStack) -> popResource((Level)paramServerLevel, paramBlockPos, paramItemStack));
/*     */   }
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
/*     */   protected InteractionResult useItemOn(ItemStack paramItemStack, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/*     */     // Byte code:
/*     */     //   0: aload_2
/*     */     //   1: getstatic net/minecraft/world/level/block/BeehiveBlock.HONEY_LEVEL : Lnet/minecraft/world/level/block/state/properties/IntegerProperty;
/*     */     //   4: invokevirtual getValue : (Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;
/*     */     //   7: checkcast java/lang/Integer
/*     */     //   10: invokevirtual intValue : ()I
/*     */     //   13: istore #8
/*     */     //   15: iconst_0
/*     */     //   16: istore #9
/*     */     //   18: iload #8
/*     */     //   20: iconst_5
/*     */     //   21: if_icmplt -> 275
/*     */     //   24: aload_1
/*     */     //   25: invokevirtual getItem : ()Lnet/minecraft/world/item/Item;
/*     */     //   28: astore #10
/*     */     //   30: aload_3
/*     */     //   31: instanceof net/minecraft/server/level/ServerLevel
/*     */     //   34: ifeq -> 127
/*     */     //   37: aload_3
/*     */     //   38: checkcast net/minecraft/server/level/ServerLevel
/*     */     //   41: astore #11
/*     */     //   43: aload_1
/*     */     //   44: getstatic net/minecraft/world/item/Items.SHEARS : Lnet/minecraft/world/item/Item;
/*     */     //   47: invokevirtual is : (Lnet/minecraft/world/item/Item;)Z
/*     */     //   50: ifeq -> 127
/*     */     //   53: aload #11
/*     */     //   55: aload_1
/*     */     //   56: aload_2
/*     */     //   57: aload_3
/*     */     //   58: aload #4
/*     */     //   60: invokevirtual getBlockEntity : (Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;
/*     */     //   63: aload #5
/*     */     //   65: aload #4
/*     */     //   67: invokestatic dropHoneycomb : (Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;)V
/*     */     //   70: aload_3
/*     */     //   71: aconst_null
/*     */     //   72: aload #5
/*     */     //   74: invokevirtual getX : ()D
/*     */     //   77: aload #5
/*     */     //   79: invokevirtual getY : ()D
/*     */     //   82: aload #5
/*     */     //   84: invokevirtual getZ : ()D
/*     */     //   87: getstatic net/minecraft/sounds/SoundEvents.BEEHIVE_SHEAR : Lnet/minecraft/sounds/SoundEvent;
/*     */     //   90: getstatic net/minecraft/sounds/SoundSource.BLOCKS : Lnet/minecraft/sounds/SoundSource;
/*     */     //   93: fconst_1
/*     */     //   94: fconst_1
/*     */     //   95: invokevirtual playSound : (Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V
/*     */     //   98: aload_1
/*     */     //   99: iconst_1
/*     */     //   100: aload #5
/*     */     //   102: aload #6
/*     */     //   104: invokevirtual asEquipmentSlot : ()Lnet/minecraft/world/entity/EquipmentSlot;
/*     */     //   107: invokevirtual hurtAndBreak : (ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V
/*     */     //   110: iconst_1
/*     */     //   111: istore #9
/*     */     //   113: aload_3
/*     */     //   114: aload #5
/*     */     //   116: getstatic net/minecraft/world/level/gameevent/GameEvent.SHEAR : Lnet/minecraft/core/Holder$Reference;
/*     */     //   119: aload #4
/*     */     //   121: invokevirtual gameEvent : (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Holder;Lnet/minecraft/core/BlockPos;)V
/*     */     //   124: goto -> 250
/*     */     //   127: aload_1
/*     */     //   128: getstatic net/minecraft/world/item/Items.GLASS_BOTTLE : Lnet/minecraft/world/item/Item;
/*     */     //   131: invokevirtual is : (Lnet/minecraft/world/item/Item;)Z
/*     */     //   134: ifeq -> 250
/*     */     //   137: aload_1
/*     */     //   138: iconst_1
/*     */     //   139: invokevirtual shrink : (I)V
/*     */     //   142: aload_3
/*     */     //   143: aload #5
/*     */     //   145: aload #5
/*     */     //   147: invokevirtual getX : ()D
/*     */     //   150: aload #5
/*     */     //   152: invokevirtual getY : ()D
/*     */     //   155: aload #5
/*     */     //   157: invokevirtual getZ : ()D
/*     */     //   160: getstatic net/minecraft/sounds/SoundEvents.BOTTLE_FILL : Lnet/minecraft/sounds/SoundEvent;
/*     */     //   163: getstatic net/minecraft/sounds/SoundSource.BLOCKS : Lnet/minecraft/sounds/SoundSource;
/*     */     //   166: fconst_1
/*     */     //   167: fconst_1
/*     */     //   168: invokevirtual playSound : (Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V
/*     */     //   171: aload_1
/*     */     //   172: invokevirtual isEmpty : ()Z
/*     */     //   175: ifeq -> 198
/*     */     //   178: aload #5
/*     */     //   180: aload #6
/*     */     //   182: new net/minecraft/world/item/ItemStack
/*     */     //   185: dup
/*     */     //   186: getstatic net/minecraft/world/item/Items.HONEY_BOTTLE : Lnet/minecraft/world/item/Item;
/*     */     //   189: invokespecial <init> : (Lnet/minecraft/world/level/ItemLike;)V
/*     */     //   192: invokevirtual setItemInHand : (Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V
/*     */     //   195: goto -> 236
/*     */     //   198: aload #5
/*     */     //   200: invokevirtual getInventory : ()Lnet/minecraft/world/entity/player/Inventory;
/*     */     //   203: new net/minecraft/world/item/ItemStack
/*     */     //   206: dup
/*     */     //   207: getstatic net/minecraft/world/item/Items.HONEY_BOTTLE : Lnet/minecraft/world/item/Item;
/*     */     //   210: invokespecial <init> : (Lnet/minecraft/world/level/ItemLike;)V
/*     */     //   213: invokevirtual add : (Lnet/minecraft/world/item/ItemStack;)Z
/*     */     //   216: ifne -> 236
/*     */     //   219: aload #5
/*     */     //   221: new net/minecraft/world/item/ItemStack
/*     */     //   224: dup
/*     */     //   225: getstatic net/minecraft/world/item/Items.HONEY_BOTTLE : Lnet/minecraft/world/item/Item;
/*     */     //   228: invokespecial <init> : (Lnet/minecraft/world/level/ItemLike;)V
/*     */     //   231: iconst_0
/*     */     //   232: invokevirtual drop : (Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/item/ItemEntity;
/*     */     //   235: pop
/*     */     //   236: iconst_1
/*     */     //   237: istore #9
/*     */     //   239: aload_3
/*     */     //   240: aload #5
/*     */     //   242: getstatic net/minecraft/world/level/gameevent/GameEvent.FLUID_PICKUP : Lnet/minecraft/core/Holder$Reference;
/*     */     //   245: aload #4
/*     */     //   247: invokevirtual gameEvent : (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Holder;Lnet/minecraft/core/BlockPos;)V
/*     */     //   250: aload_3
/*     */     //   251: invokevirtual isClientSide : ()Z
/*     */     //   254: ifne -> 275
/*     */     //   257: iload #9
/*     */     //   259: ifeq -> 275
/*     */     //   262: aload #5
/*     */     //   264: getstatic net/minecraft/stats/Stats.ITEM_USED : Lnet/minecraft/stats/StatType;
/*     */     //   267: aload #10
/*     */     //   269: invokevirtual get : (Ljava/lang/Object;)Lnet/minecraft/stats/Stat;
/*     */     //   272: invokevirtual awardStat : (Lnet/minecraft/stats/Stat;)V
/*     */     //   275: iload #9
/*     */     //   277: ifeq -> 334
/*     */     //   280: aload_3
/*     */     //   281: aload #4
/*     */     //   283: invokestatic isSmokeyPos : (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z
/*     */     //   286: ifne -> 322
/*     */     //   289: aload_0
/*     */     //   290: aload_3
/*     */     //   291: aload #4
/*     */     //   293: invokevirtual hiveContainsBees : (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z
/*     */     //   296: ifeq -> 306
/*     */     //   299: aload_0
/*     */     //   300: aload_3
/*     */     //   301: aload #4
/*     */     //   303: invokevirtual angerNearbyBees : (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V
/*     */     //   306: aload_0
/*     */     //   307: aload_3
/*     */     //   308: aload_2
/*     */     //   309: aload #4
/*     */     //   311: aload #5
/*     */     //   313: getstatic net/minecraft/world/level/block/entity/BeehiveBlockEntity$BeeReleaseStatus.EMERGENCY : Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$BeeReleaseStatus;
/*     */     //   316: invokevirtual releaseBeesAndResetHoneyLevel : (Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$BeeReleaseStatus;)V
/*     */     //   319: goto -> 330
/*     */     //   322: aload_0
/*     */     //   323: aload_3
/*     */     //   324: aload_2
/*     */     //   325: aload #4
/*     */     //   327: invokevirtual resetHoneyLevel : (Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V
/*     */     //   330: getstatic net/minecraft/world/InteractionResult.SUCCESS : Lnet/minecraft/world/InteractionResult$Success;
/*     */     //   333: areturn
/*     */     //   334: aload_0
/*     */     //   335: aload_1
/*     */     //   336: aload_2
/*     */     //   337: aload_3
/*     */     //   338: aload #4
/*     */     //   340: aload #5
/*     */     //   342: aload #6
/*     */     //   344: aload #7
/*     */     //   346: invokespecial useItemOn : (Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;
/*     */     //   349: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #139	-> 0
/*     */     //   #140	-> 15
/*     */     //   #142	-> 18
/*     */     //   #143	-> 24
/*     */     //   #144	-> 30
/*     */     //   #145	-> 53
/*     */     //   #146	-> 70
/*     */     //   #147	-> 98
/*     */     //   #148	-> 110
/*     */     //   #149	-> 113
/*     */     //   #150	-> 127
/*     */     //   #151	-> 137
/*     */     //   #152	-> 142
/*     */     //   #153	-> 171
/*     */     //   #154	-> 178
/*     */     //   #155	-> 198
/*     */     //   #156	-> 219
/*     */     //   #158	-> 236
/*     */     //   #159	-> 239
/*     */     //   #161	-> 250
/*     */     //   #162	-> 262
/*     */     //   #166	-> 275
/*     */     //   #167	-> 280
/*     */     //   #169	-> 289
/*     */     //   #170	-> 299
/*     */     //   #172	-> 306
/*     */     //   #174	-> 322
/*     */     //   #176	-> 330
/*     */     //   #179	-> 334
/*     */   }
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
/*     */   private boolean hiveContainsBees(Level paramLevel, BlockPos paramBlockPos) {
/* 183 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/* 184 */     if (blockEntity instanceof BeehiveBlockEntity) { BeehiveBlockEntity beehiveBlockEntity = (BeehiveBlockEntity)blockEntity;
/* 185 */       return !beehiveBlockEntity.isEmpty(); }
/*     */ 
/*     */     
/* 188 */     return false;
/*     */   }
/*     */   
/*     */   public void releaseBeesAndResetHoneyLevel(Level paramLevel, BlockState paramBlockState, BlockPos paramBlockPos, Player paramPlayer, BeehiveBlockEntity.BeeReleaseStatus paramBeeReleaseStatus) {
/* 192 */     resetHoneyLevel(paramLevel, paramBlockState, paramBlockPos);
/*     */     
/* 194 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/* 195 */     if (blockEntity instanceof BeehiveBlockEntity) { BeehiveBlockEntity beehiveBlockEntity = (BeehiveBlockEntity)blockEntity;
/* 196 */       beehiveBlockEntity.emptyAllLivingFromHive(paramPlayer, paramBlockState, paramBeeReleaseStatus); }
/*     */   
/*     */   }
/*     */   
/*     */   public void resetHoneyLevel(Level paramLevel, BlockState paramBlockState, BlockPos paramBlockPos) {
/* 201 */     paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)HONEY_LEVEL, Integer.valueOf(0)), 3);
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 206 */     if (((Integer)paramBlockState.getValue((Property)HONEY_LEVEL)).intValue() >= 5) {
/* 207 */       for (byte b = 0; b < paramRandomSource.nextInt(1) + 1; b++) {
/* 208 */         trySpawnDripParticles(paramLevel, paramBlockPos, paramBlockState);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void trySpawnDripParticles(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 214 */     if (!paramBlockState.getFluidState().isEmpty() || paramLevel.random.nextFloat() < 0.3F) {
/*     */       return;
/*     */     }
/*     */     
/* 218 */     VoxelShape voxelShape = paramBlockState.getCollisionShape((BlockGetter)paramLevel, paramBlockPos);
/* 219 */     double d = voxelShape.max(Direction.Axis.Y);
/* 220 */     if (d >= 1.0D && !paramBlockState.is(BlockTags.IMPERMEABLE)) {
/* 221 */       double d1 = voxelShape.min(Direction.Axis.Y);
/* 222 */       if (d1 > 0.0D) {
/* 223 */         spawnParticle(paramLevel, paramBlockPos, voxelShape, paramBlockPos.getY() + d1 - 0.05D);
/*     */       } else {
/* 225 */         BlockPos blockPos = paramBlockPos.below();
/* 226 */         BlockState blockState = paramLevel.getBlockState(blockPos);
/* 227 */         VoxelShape voxelShape1 = blockState.getCollisionShape((BlockGetter)paramLevel, blockPos);
/* 228 */         double d2 = voxelShape1.max(Direction.Axis.Y);
/* 229 */         if ((d2 < 1.0D || !blockState.isCollisionShapeFullBlock((BlockGetter)paramLevel, blockPos)) && blockState.getFluidState().isEmpty()) {
/* 230 */           spawnParticle(paramLevel, paramBlockPos, voxelShape, paramBlockPos.getY() - 0.05D);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void spawnParticle(Level paramLevel, BlockPos paramBlockPos, VoxelShape paramVoxelShape, double paramDouble) {
/* 237 */     spawnFluidParticle(paramLevel, paramBlockPos.getX() + paramVoxelShape.min(Direction.Axis.X), paramBlockPos
/* 238 */         .getX() + paramVoxelShape.max(Direction.Axis.X), paramBlockPos
/* 239 */         .getZ() + paramVoxelShape.min(Direction.Axis.Z), paramBlockPos
/* 240 */         .getZ() + paramVoxelShape.max(Direction.Axis.Z), paramDouble);
/*     */   }
/*     */ 
/*     */   
/*     */   private void spawnFluidParticle(Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5) {
/* 245 */     paramLevel.addParticle((ParticleOptions)ParticleTypes.DRIPPING_HONEY, Mth.lerp(paramLevel.random.nextDouble(), paramDouble1, paramDouble2), paramDouble5, Mth.lerp(paramLevel.random.nextDouble(), paramDouble3, paramDouble4), 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 250 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getHorizontalDirection().getOpposite());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 255 */     paramBuilder.add(new Property[] { (Property)HONEY_LEVEL, (Property)FACING });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 260 */     return (BlockEntity)new BeehiveBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/* 265 */     return paramLevel.isClientSide() ? null : createTickerHelper(paramBlockEntityType, BlockEntityType.BEEHIVE, BeehiveBlockEntity::serverTick);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState playerWillDestroy(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, Player paramPlayer) {
/* 270 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel; if (paramPlayer.preventsBlockDrops() && ((Boolean)serverLevel.getGameRules().get(GameRules.BLOCK_DROPS)).booleanValue()) {
/* 271 */         BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/* 272 */         if (blockEntity instanceof BeehiveBlockEntity) { BeehiveBlockEntity beehiveBlockEntity = (BeehiveBlockEntity)blockEntity;
/* 273 */           int i = ((Integer)paramBlockState.getValue((Property)HONEY_LEVEL)).intValue();
/* 274 */           boolean bool = !beehiveBlockEntity.isEmpty() ? true : false;
/*     */ 
/*     */           
/* 277 */           if (bool || i > 0) {
/* 278 */             ItemStack itemStack = new ItemStack(this);
/* 279 */             itemStack.applyComponents(beehiveBlockEntity.collectComponents());
/*     */ 
/*     */             
/* 282 */             itemStack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with((Property)HONEY_LEVEL, Integer.valueOf(i)));
/*     */             
/* 284 */             ItemEntity itemEntity = new ItemEntity(paramLevel, paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ(), itemStack);
/* 285 */             itemEntity.setDefaultPickUpDelay();
/* 286 */             paramLevel.addFreshEntity((Entity)itemEntity);
/*     */           }  }
/*     */       
/*     */       }  }
/*     */     
/* 291 */     return super.playerWillDestroy(paramLevel, paramBlockPos, paramBlockState, paramPlayer);
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<ItemStack> getDrops(BlockState paramBlockState, LootParams.Builder paramBuilder) {
/* 296 */     Entity entity = (Entity)paramBuilder.getOptionalParameter(LootContextParams.THIS_ENTITY);
/*     */ 
/*     */     
/* 299 */     if (entity instanceof net.minecraft.world.entity.item.PrimedTnt || entity instanceof net.minecraft.world.entity.monster.Creeper || entity instanceof net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull || entity instanceof net.minecraft.world.entity.boss.wither.WitherBoss || entity instanceof net.minecraft.world.entity.vehicle.minecart.MinecartTNT) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 305 */       BlockEntity blockEntity = (BlockEntity)paramBuilder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
/* 306 */       if (blockEntity instanceof BeehiveBlockEntity) { BeehiveBlockEntity beehiveBlockEntity = (BeehiveBlockEntity)blockEntity;
/* 307 */         beehiveBlockEntity.emptyAllLivingFromHive(null, paramBlockState, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY); }
/*     */     
/*     */     } 
/* 310 */     return super.getDrops(paramBlockState, paramBuilder);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 315 */     ItemStack itemStack = super.getCloneItemStack(paramLevelReader, paramBlockPos, paramBlockState, paramBoolean);
/* 316 */     if (paramBoolean) {
/* 317 */       itemStack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with((Property)HONEY_LEVEL, paramBlockState.getValue((Property)HONEY_LEVEL)));
/*     */     }
/* 319 */     return itemStack;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 324 */     if (paramLevelReader.getBlockState(paramBlockPos2).getBlock() instanceof FireBlock) {
/*     */       
/* 326 */       BlockEntity blockEntity = paramLevelReader.getBlockEntity(paramBlockPos1);
/* 327 */       if (blockEntity instanceof BeehiveBlockEntity) { BeehiveBlockEntity beehiveBlockEntity = (BeehiveBlockEntity)blockEntity;
/* 328 */         beehiveBlockEntity.emptyAllLivingFromHive(null, paramBlockState1, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY); }
/*     */     
/*     */     } 
/* 331 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 336 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 341 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\BeehiveBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */