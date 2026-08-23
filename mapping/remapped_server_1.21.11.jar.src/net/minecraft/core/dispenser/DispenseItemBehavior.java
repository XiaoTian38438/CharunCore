/*     */ package net.minecraft.core.dispenser;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.animal.armadillo.Armadillo;
/*     */ import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
/*     */ import net.minecraft.world.entity.decoration.ArmorStand;
/*     */ import net.minecraft.world.entity.item.PrimedTnt;
/*     */ import net.minecraft.world.item.BoneMealItem;
/*     */ import net.minecraft.world.item.DispensibleContainerItem;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.HoneycombItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.SpawnEggItem;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.BaseFireBlock;
/*     */ import net.minecraft.world.level.block.BeehiveBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.BucketPickup;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.block.CandleCakeBlock;
/*     */ import net.minecraft.world.level.block.CarvedPumpkinBlock;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ import net.minecraft.world.level.block.RespawnAnchorBlock;
/*     */ import net.minecraft.world.level.block.SkullBlock;
/*     */ import net.minecraft.world.level.block.TntBlock;
/*     */ import net.minecraft.world.level.block.WitherSkullBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SkullBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RotationSegment;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public interface DispenseItemBehavior {
/*  65 */   public static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final DispenseItemBehavior NOOP;
/*     */   
/*     */   static {
/*  70 */     NOOP = ((paramBlockSource, paramItemStack) -> paramItemStack);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void bootStrap() {
/*  80 */     DispenserBlock.registerProjectileBehavior((ItemLike)Items.ARROW);
/*  81 */     DispenserBlock.registerProjectileBehavior((ItemLike)Items.TIPPED_ARROW);
/*  82 */     DispenserBlock.registerProjectileBehavior((ItemLike)Items.SPECTRAL_ARROW);
/*  83 */     DispenserBlock.registerProjectileBehavior((ItemLike)Items.EGG);
/*  84 */     DispenserBlock.registerProjectileBehavior((ItemLike)Items.BLUE_EGG);
/*  85 */     DispenserBlock.registerProjectileBehavior((ItemLike)Items.BROWN_EGG);
/*  86 */     DispenserBlock.registerProjectileBehavior((ItemLike)Items.SNOWBALL);
/*  87 */     DispenserBlock.registerProjectileBehavior((ItemLike)Items.EXPERIENCE_BOTTLE);
/*  88 */     DispenserBlock.registerProjectileBehavior((ItemLike)Items.SPLASH_POTION);
/*  89 */     DispenserBlock.registerProjectileBehavior((ItemLike)Items.LINGERING_POTION);
/*  90 */     DispenserBlock.registerProjectileBehavior((ItemLike)Items.FIREWORK_ROCKET);
/*  91 */     DispenserBlock.registerProjectileBehavior((ItemLike)Items.FIRE_CHARGE);
/*  92 */     DispenserBlock.registerProjectileBehavior((ItemLike)Items.WIND_CHARGE);
/*     */     
/*  94 */     DefaultDispenseItemBehavior defaultDispenseItemBehavior1 = new DefaultDispenseItemBehavior()
/*     */       {
/*     */         public ItemStack execute(BlockSource param1BlockSource, ItemStack param1ItemStack) {
/*  97 */           Direction direction = (Direction)param1BlockSource.state().getValue((Property)DispenserBlock.FACING);
/*     */           
/*  99 */           EntityType entityType = ((SpawnEggItem)param1ItemStack.getItem()).getType(param1ItemStack);
/* 100 */           if (entityType == null) {
/* 101 */             return param1ItemStack;
/*     */           }
/*     */           try {
/* 104 */             entityType.spawn(param1BlockSource.level(), param1ItemStack, null, param1BlockSource.pos().relative(direction), EntitySpawnReason.DISPENSER, (direction != Direction.UP), false);
/* 105 */           } catch (Exception exception) {
/* 106 */             LOGGER.error("Error while dispensing spawn egg from dispenser at {}", param1BlockSource.pos(), exception);
/* 107 */             return ItemStack.EMPTY;
/*     */           } 
/* 109 */           param1ItemStack.shrink(1);
/* 110 */           param1BlockSource.level().gameEvent(null, (Holder)GameEvent.ENTITY_PLACE, param1BlockSource.pos());
/* 111 */           return param1ItemStack;
/*     */         }
/*     */       };
/*     */     
/* 115 */     for (SpawnEggItem spawnEggItem : SpawnEggItem.eggs()) {
/* 116 */       DispenserBlock.registerBehavior((ItemLike)spawnEggItem, defaultDispenseItemBehavior1);
/*     */     }
/*     */     
/* 119 */     DispenserBlock.registerBehavior((ItemLike)Items.ARMOR_STAND, new DefaultDispenseItemBehavior()
/*     */         {
/*     */           public ItemStack execute(BlockSource param1BlockSource, ItemStack param1ItemStack) {
/* 122 */             Direction direction = (Direction)param1BlockSource.state().getValue((Property)DispenserBlock.FACING);
/* 123 */             BlockPos blockPos = param1BlockSource.pos().relative(direction);
/* 124 */             ServerLevel serverLevel = param1BlockSource.level();
/* 125 */             Consumer consumer = EntityType.appendDefaultStackConfig(param1ArmorStand -> param1ArmorStand.setYRot(param1Direction.toYRot()), (Level)serverLevel, param1ItemStack, null);
/* 126 */             ArmorStand armorStand = (ArmorStand)EntityType.ARMOR_STAND.spawn(serverLevel, consumer, blockPos, EntitySpawnReason.DISPENSER, false, false);
/* 127 */             if (armorStand != null) {
/* 128 */               param1ItemStack.shrink(1);
/*     */             }
/* 130 */             return param1ItemStack;
/*     */           }
/*     */         });
/*     */     
/* 134 */     DispenserBlock.registerBehavior((ItemLike)Items.CHEST, new OptionalDispenseItemBehavior()
/*     */         {
/*     */           public ItemStack execute(BlockSource param1BlockSource, ItemStack param1ItemStack) {
/* 137 */             BlockPos blockPos = param1BlockSource.pos().relative((Direction)param1BlockSource.state().getValue((Property)DispenserBlock.FACING));
/* 138 */             List list = param1BlockSource.level().getEntitiesOfClass(AbstractChestedHorse.class, new AABB(blockPos), param1AbstractChestedHorse -> (param1AbstractChestedHorse.isAlive() && !param1AbstractChestedHorse.hasChest()));
/*     */             
/* 140 */             for (AbstractChestedHorse abstractChestedHorse : list) {
/* 141 */               if (abstractChestedHorse.isTamed()) {
/* 142 */                 SlotAccess slotAccess = abstractChestedHorse.getSlot(499);
/* 143 */                 if (slotAccess != null && slotAccess.set(param1ItemStack)) {
/* 144 */                   param1ItemStack.shrink(1);
/* 145 */                   setSuccess(true);
/* 146 */                   return param1ItemStack;
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */             
/* 151 */             return super.execute(param1BlockSource, param1ItemStack);
/*     */           }
/*     */         });
/*     */     
/* 155 */     DispenserBlock.registerBehavior((ItemLike)Items.OAK_BOAT, new BoatDispenseItemBehavior(EntityType.OAK_BOAT));
/* 156 */     DispenserBlock.registerBehavior((ItemLike)Items.SPRUCE_BOAT, new BoatDispenseItemBehavior(EntityType.SPRUCE_BOAT));
/* 157 */     DispenserBlock.registerBehavior((ItemLike)Items.BIRCH_BOAT, new BoatDispenseItemBehavior(EntityType.BIRCH_BOAT));
/* 158 */     DispenserBlock.registerBehavior((ItemLike)Items.JUNGLE_BOAT, new BoatDispenseItemBehavior(EntityType.JUNGLE_BOAT));
/* 159 */     DispenserBlock.registerBehavior((ItemLike)Items.DARK_OAK_BOAT, new BoatDispenseItemBehavior(EntityType.DARK_OAK_BOAT));
/* 160 */     DispenserBlock.registerBehavior((ItemLike)Items.ACACIA_BOAT, new BoatDispenseItemBehavior(EntityType.ACACIA_BOAT));
/* 161 */     DispenserBlock.registerBehavior((ItemLike)Items.CHERRY_BOAT, new BoatDispenseItemBehavior(EntityType.CHERRY_BOAT));
/* 162 */     DispenserBlock.registerBehavior((ItemLike)Items.MANGROVE_BOAT, new BoatDispenseItemBehavior(EntityType.MANGROVE_BOAT));
/* 163 */     DispenserBlock.registerBehavior((ItemLike)Items.PALE_OAK_BOAT, new BoatDispenseItemBehavior(EntityType.PALE_OAK_BOAT));
/* 164 */     DispenserBlock.registerBehavior((ItemLike)Items.BAMBOO_RAFT, new BoatDispenseItemBehavior(EntityType.BAMBOO_RAFT));
/*     */     
/* 166 */     DispenserBlock.registerBehavior((ItemLike)Items.OAK_CHEST_BOAT, new BoatDispenseItemBehavior(EntityType.OAK_CHEST_BOAT));
/* 167 */     DispenserBlock.registerBehavior((ItemLike)Items.SPRUCE_CHEST_BOAT, new BoatDispenseItemBehavior(EntityType.SPRUCE_CHEST_BOAT));
/* 168 */     DispenserBlock.registerBehavior((ItemLike)Items.BIRCH_CHEST_BOAT, new BoatDispenseItemBehavior(EntityType.BIRCH_CHEST_BOAT));
/* 169 */     DispenserBlock.registerBehavior((ItemLike)Items.JUNGLE_CHEST_BOAT, new BoatDispenseItemBehavior(EntityType.JUNGLE_CHEST_BOAT));
/* 170 */     DispenserBlock.registerBehavior((ItemLike)Items.DARK_OAK_CHEST_BOAT, new BoatDispenseItemBehavior(EntityType.DARK_OAK_CHEST_BOAT));
/* 171 */     DispenserBlock.registerBehavior((ItemLike)Items.ACACIA_CHEST_BOAT, new BoatDispenseItemBehavior(EntityType.ACACIA_CHEST_BOAT));
/* 172 */     DispenserBlock.registerBehavior((ItemLike)Items.CHERRY_CHEST_BOAT, new BoatDispenseItemBehavior(EntityType.CHERRY_CHEST_BOAT));
/* 173 */     DispenserBlock.registerBehavior((ItemLike)Items.MANGROVE_CHEST_BOAT, new BoatDispenseItemBehavior(EntityType.MANGROVE_CHEST_BOAT));
/* 174 */     DispenserBlock.registerBehavior((ItemLike)Items.PALE_OAK_CHEST_BOAT, new BoatDispenseItemBehavior(EntityType.PALE_OAK_CHEST_BOAT));
/* 175 */     DispenserBlock.registerBehavior((ItemLike)Items.BAMBOO_CHEST_RAFT, new BoatDispenseItemBehavior(EntityType.BAMBOO_CHEST_RAFT));
/*     */     
/* 177 */     DefaultDispenseItemBehavior defaultDispenseItemBehavior2 = new DefaultDispenseItemBehavior() {
/* 178 */         private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
/*     */ 
/*     */         
/*     */         public ItemStack execute(BlockSource param1BlockSource, ItemStack param1ItemStack) {
/* 182 */           DispensibleContainerItem dispensibleContainerItem = (DispensibleContainerItem)param1ItemStack.getItem();
/* 183 */           BlockPos blockPos = param1BlockSource.pos().relative((Direction)param1BlockSource.state().getValue((Property)DispenserBlock.FACING));
/*     */           
/* 185 */           ServerLevel serverLevel = param1BlockSource.level();
/* 186 */           if (dispensibleContainerItem.emptyContents(null, (Level)serverLevel, blockPos, null)) {
/* 187 */             dispensibleContainerItem.checkExtraContent(null, (Level)serverLevel, param1ItemStack, blockPos);
/* 188 */             return consumeWithRemainder(param1BlockSource, param1ItemStack, new ItemStack((ItemLike)Items.BUCKET));
/*     */           } 
/*     */           
/* 191 */           return this.defaultDispenseItemBehavior.dispense(param1BlockSource, param1ItemStack);
/*     */         }
/*     */       };
/* 194 */     DispenserBlock.registerBehavior((ItemLike)Items.LAVA_BUCKET, defaultDispenseItemBehavior2);
/* 195 */     DispenserBlock.registerBehavior((ItemLike)Items.WATER_BUCKET, defaultDispenseItemBehavior2);
/* 196 */     DispenserBlock.registerBehavior((ItemLike)Items.POWDER_SNOW_BUCKET, defaultDispenseItemBehavior2);
/* 197 */     DispenserBlock.registerBehavior((ItemLike)Items.SALMON_BUCKET, defaultDispenseItemBehavior2);
/* 198 */     DispenserBlock.registerBehavior((ItemLike)Items.COD_BUCKET, defaultDispenseItemBehavior2);
/* 199 */     DispenserBlock.registerBehavior((ItemLike)Items.PUFFERFISH_BUCKET, defaultDispenseItemBehavior2);
/* 200 */     DispenserBlock.registerBehavior((ItemLike)Items.TROPICAL_FISH_BUCKET, defaultDispenseItemBehavior2);
/* 201 */     DispenserBlock.registerBehavior((ItemLike)Items.AXOLOTL_BUCKET, defaultDispenseItemBehavior2);
/* 202 */     DispenserBlock.registerBehavior((ItemLike)Items.TADPOLE_BUCKET, defaultDispenseItemBehavior2);
/*     */     
/* 204 */     DispenserBlock.registerBehavior((ItemLike)Items.BUCKET, new DefaultDispenseItemBehavior() {
/*     */           public ItemStack execute(BlockSource param1BlockSource, ItemStack param1ItemStack) {
/*     */             Item item;
/* 207 */             ServerLevel serverLevel = param1BlockSource.level();
/*     */             
/* 209 */             BlockPos blockPos = param1BlockSource.pos().relative((Direction)param1BlockSource.state().getValue((Property)DispenserBlock.FACING));
/*     */             
/* 211 */             BlockState blockState = serverLevel.getBlockState(blockPos);
/* 212 */             Block block = blockState.getBlock();
/*     */ 
/*     */ 
/*     */             
/* 216 */             if (block instanceof BucketPickup) { BucketPickup bucketPickup = (BucketPickup)block;
/* 217 */               ItemStack itemStack = bucketPickup.pickupBlock(null, (LevelAccessor)serverLevel, blockPos, blockState);
/* 218 */               if (itemStack.isEmpty()) {
/* 219 */                 return super.execute(param1BlockSource, param1ItemStack);
/*     */               }
/* 221 */               serverLevel.gameEvent(null, (Holder)GameEvent.FLUID_PICKUP, blockPos);
/* 222 */               item = itemStack.getItem(); }
/*     */             else
/* 224 */             { return super.execute(param1BlockSource, param1ItemStack); }
/*     */ 
/*     */             
/* 227 */             return consumeWithRemainder(param1BlockSource, param1ItemStack, new ItemStack((ItemLike)item));
/*     */           }
/*     */         });
/*     */     
/* 231 */     DispenserBlock.registerBehavior((ItemLike)Items.FLINT_AND_STEEL, new OptionalDispenseItemBehavior()
/*     */         {
/*     */           protected ItemStack execute(BlockSource param1BlockSource, ItemStack param1ItemStack) {
/* 234 */             ServerLevel serverLevel = param1BlockSource.level();
/*     */             
/* 236 */             setSuccess(true);
/*     */             
/* 238 */             Direction direction = (Direction)param1BlockSource.state().getValue((Property)DispenserBlock.FACING);
/* 239 */             BlockPos blockPos = param1BlockSource.pos().relative(direction);
/* 240 */             BlockState blockState = serverLevel.getBlockState(blockPos);
/* 241 */             if (BaseFireBlock.canBePlacedAt((Level)serverLevel, blockPos, direction)) {
/* 242 */               serverLevel.setBlockAndUpdate(blockPos, BaseFireBlock.getState((BlockGetter)serverLevel, blockPos));
/* 243 */               serverLevel.gameEvent(null, (Holder)GameEvent.BLOCK_PLACE, blockPos);
/* 244 */             } else if (CampfireBlock.canLight(blockState) || CandleBlock.canLight(blockState) || CandleCakeBlock.canLight(blockState)) {
/* 245 */               serverLevel.setBlockAndUpdate(blockPos, (BlockState)blockState.setValue((Property)BlockStateProperties.LIT, Boolean.valueOf(true)));
/* 246 */               serverLevel.gameEvent(null, (Holder)GameEvent.BLOCK_CHANGE, blockPos);
/* 247 */             } else if (blockState.getBlock() instanceof TntBlock) {
/* 248 */               if (TntBlock.prime((Level)serverLevel, blockPos)) {
/* 249 */                 serverLevel.removeBlock(blockPos, false);
/*     */               } else {
/* 251 */                 setSuccess(false);
/*     */               } 
/*     */             } else {
/* 254 */               setSuccess(false);
/*     */             } 
/*     */             
/* 257 */             if (isSuccess())
/* 258 */               param1ItemStack.hurtAndBreak(1, serverLevel, null, param1Item -> {
/*     */                   
/*     */                   }); 
/* 261 */             return param1ItemStack;
/*     */           }
/*     */         });
/*     */     
/* 265 */     DispenserBlock.registerBehavior((ItemLike)Items.BONE_MEAL, new OptionalDispenseItemBehavior()
/*     */         {
/*     */           protected ItemStack execute(BlockSource param1BlockSource, ItemStack param1ItemStack) {
/* 268 */             setSuccess(true);
/* 269 */             ServerLevel serverLevel = param1BlockSource.level();
/*     */             
/* 271 */             BlockPos blockPos = param1BlockSource.pos().relative((Direction)param1BlockSource.state().getValue((Property)DispenserBlock.FACING));
/* 272 */             if (BoneMealItem.growCrop(param1ItemStack, (Level)serverLevel, blockPos) || BoneMealItem.growWaterPlant(param1ItemStack, (Level)serverLevel, blockPos, null)) {
/* 273 */               if (!serverLevel.isClientSide()) {
/* 274 */                 serverLevel.levelEvent(1505, blockPos, 15);
/*     */               }
/*     */             } else {
/* 277 */               setSuccess(false);
/*     */             } 
/*     */             
/* 280 */             return param1ItemStack;
/*     */           }
/*     */         });
/*     */     
/* 284 */     DispenserBlock.registerBehavior((ItemLike)Blocks.TNT, new OptionalDispenseItemBehavior()
/*     */         {
/*     */           protected ItemStack execute(BlockSource param1BlockSource, ItemStack param1ItemStack) {
/* 287 */             ServerLevel serverLevel = param1BlockSource.level();
/* 288 */             if (!((Boolean)serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)).booleanValue()) {
/* 289 */               setSuccess(false);
/* 290 */               return param1ItemStack;
/*     */             } 
/*     */             
/* 293 */             BlockPos blockPos = param1BlockSource.pos().relative((Direction)param1BlockSource.state().getValue((Property)DispenserBlock.FACING));
/*     */             
/* 295 */             PrimedTnt primedTnt = new PrimedTnt((Level)serverLevel, blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, null);
/* 296 */             serverLevel.addFreshEntity((Entity)primedTnt);
/* 297 */             serverLevel.playSound(null, primedTnt.getX(), primedTnt.getY(), primedTnt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 298 */             serverLevel.gameEvent(null, (Holder)GameEvent.ENTITY_PLACE, blockPos);
/*     */             
/* 300 */             param1ItemStack.shrink(1);
/* 301 */             setSuccess(true);
/* 302 */             return param1ItemStack;
/*     */           }
/*     */         });
/*     */     
/* 306 */     DispenserBlock.registerBehavior((ItemLike)Items.WITHER_SKELETON_SKULL, new OptionalDispenseItemBehavior()
/*     */         {
/*     */           protected ItemStack execute(BlockSource param1BlockSource, ItemStack param1ItemStack) {
/* 309 */             ServerLevel serverLevel = param1BlockSource.level();
/* 310 */             Direction direction = (Direction)param1BlockSource.state().getValue((Property)DispenserBlock.FACING);
/* 311 */             BlockPos blockPos = param1BlockSource.pos().relative(direction);
/*     */             
/* 313 */             if (serverLevel.isEmptyBlock(blockPos) && WitherSkullBlock.canSpawnMob((Level)serverLevel, blockPos, param1ItemStack)) {
/* 314 */               serverLevel.setBlock(blockPos, (BlockState)Blocks.WITHER_SKELETON_SKULL.defaultBlockState().setValue((Property)SkullBlock.ROTATION, Integer.valueOf(RotationSegment.convertToSegment(direction))), 3);
/* 315 */               serverLevel.gameEvent(null, (Holder)GameEvent.BLOCK_PLACE, blockPos);
/* 316 */               BlockEntity blockEntity = serverLevel.getBlockEntity(blockPos);
/* 317 */               if (blockEntity instanceof SkullBlockEntity) {
/* 318 */                 WitherSkullBlock.checkSpawn((Level)serverLevel, blockPos, (SkullBlockEntity)blockEntity);
/*     */               }
/* 320 */               param1ItemStack.shrink(1);
/* 321 */               setSuccess(true);
/*     */             } else {
/* 323 */               setSuccess(EquipmentDispenseItemBehavior.dispenseEquipment(param1BlockSource, param1ItemStack));
/*     */             } 
/* 325 */             return param1ItemStack;
/*     */           }
/*     */         });
/*     */     
/* 329 */     DispenserBlock.registerBehavior((ItemLike)Blocks.CARVED_PUMPKIN, new OptionalDispenseItemBehavior()
/*     */         {
/*     */           protected ItemStack execute(BlockSource param1BlockSource, ItemStack param1ItemStack) {
/* 332 */             ServerLevel serverLevel = param1BlockSource.level();
/* 333 */             BlockPos blockPos = param1BlockSource.pos().relative((Direction)param1BlockSource.state().getValue((Property)DispenserBlock.FACING));
/* 334 */             CarvedPumpkinBlock carvedPumpkinBlock = (CarvedPumpkinBlock)Blocks.CARVED_PUMPKIN;
/*     */             
/* 336 */             if (serverLevel.isEmptyBlock(blockPos) && carvedPumpkinBlock.canSpawnGolem((LevelReader)serverLevel, blockPos)) {
/* 337 */               if (!serverLevel.isClientSide()) {
/* 338 */                 serverLevel.setBlock(blockPos, carvedPumpkinBlock.defaultBlockState(), 3);
/* 339 */                 serverLevel.gameEvent(null, (Holder)GameEvent.BLOCK_PLACE, blockPos);
/*     */               } 
/* 341 */               param1ItemStack.shrink(1);
/* 342 */               setSuccess(true);
/*     */             } else {
/* 344 */               setSuccess(EquipmentDispenseItemBehavior.dispenseEquipment(param1BlockSource, param1ItemStack));
/*     */             } 
/* 346 */             return param1ItemStack;
/*     */           }
/*     */         });
/*     */     
/* 350 */     DispenserBlock.registerBehavior((ItemLike)Blocks.SHULKER_BOX.asItem(), new ShulkerBoxDispenseBehavior());
/* 351 */     for (DyeColor dyeColor : DyeColor.values()) {
/* 352 */       DispenserBlock.registerBehavior((ItemLike)ShulkerBoxBlock.getBlockByColor(dyeColor).asItem(), new ShulkerBoxDispenseBehavior());
/*     */     }
/*     */     
/* 355 */     DispenserBlock.registerBehavior((ItemLike)Items.GLASS_BOTTLE.asItem(), new OptionalDispenseItemBehavior() {
/*     */           private ItemStack takeLiquid(BlockSource param1BlockSource, ItemStack param1ItemStack1, ItemStack param1ItemStack2) {
/* 357 */             param1BlockSource.level().gameEvent(null, (Holder)GameEvent.FLUID_PICKUP, param1BlockSource.pos());
/* 358 */             return consumeWithRemainder(param1BlockSource, param1ItemStack1, param1ItemStack2);
/*     */           }
/*     */ 
/*     */           
/*     */           public ItemStack execute(BlockSource param1BlockSource, ItemStack param1ItemStack) {
/* 363 */             setSuccess(false);
/* 364 */             ServerLevel serverLevel = param1BlockSource.level();
/*     */             
/* 366 */             BlockPos blockPos = param1BlockSource.pos().relative((Direction)param1BlockSource.state().getValue((Property)DispenserBlock.FACING));
/*     */             
/* 368 */             BlockState blockState = serverLevel.getBlockState(blockPos);
/*     */             
/* 370 */             if (blockState.is(BlockTags.BEEHIVES, param1BlockStateBase -> (param1BlockStateBase.hasProperty((Property)BeehiveBlock.HONEY_LEVEL) && param1BlockStateBase.getBlock() instanceof BeehiveBlock)) && ((Integer)blockState.getValue((Property)BeehiveBlock.HONEY_LEVEL)).intValue() >= 5) {
/* 371 */               ((BeehiveBlock)blockState.getBlock()).releaseBeesAndResetHoneyLevel((Level)serverLevel, blockState, blockPos, null, BeehiveBlockEntity.BeeReleaseStatus.BEE_RELEASED);
/* 372 */               setSuccess(true);
/* 373 */               return takeLiquid(param1BlockSource, param1ItemStack, new ItemStack((ItemLike)Items.HONEY_BOTTLE));
/* 374 */             }  if (serverLevel.getFluidState(blockPos).is(FluidTags.WATER)) {
/* 375 */               setSuccess(true);
/* 376 */               return takeLiquid(param1BlockSource, param1ItemStack, PotionContents.createItemStack(Items.POTION, Potions.WATER));
/*     */             } 
/* 378 */             return super.execute(param1BlockSource, param1ItemStack);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 383 */     DispenserBlock.registerBehavior((ItemLike)Items.GLOWSTONE, new OptionalDispenseItemBehavior()
/*     */         {
/*     */           public ItemStack execute(BlockSource param1BlockSource, ItemStack param1ItemStack) {
/* 386 */             Direction direction = (Direction)param1BlockSource.state().getValue((Property)DispenserBlock.FACING);
/* 387 */             BlockPos blockPos = param1BlockSource.pos().relative(direction);
/* 388 */             ServerLevel serverLevel = param1BlockSource.level();
/* 389 */             BlockState blockState = serverLevel.getBlockState(blockPos);
/* 390 */             setSuccess(true);
/* 391 */             if (blockState.is(Blocks.RESPAWN_ANCHOR)) {
/* 392 */               if (((Integer)blockState.getValue((Property)RespawnAnchorBlock.CHARGE)).intValue() != 4) {
/* 393 */                 RespawnAnchorBlock.charge(null, (Level)serverLevel, blockPos, blockState);
/* 394 */                 param1ItemStack.shrink(1);
/*     */               } else {
/* 396 */                 setSuccess(false);
/*     */               } 
/*     */               
/* 399 */               return param1ItemStack;
/*     */             } 
/* 401 */             return super.execute(param1BlockSource, param1ItemStack);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 406 */     DispenserBlock.registerBehavior((ItemLike)Items.SHEARS.asItem(), new ShearsDispenseItemBehavior());
/*     */     
/* 408 */     DispenserBlock.registerBehavior((ItemLike)Items.BRUSH.asItem(), new OptionalDispenseItemBehavior()
/*     */         {
/*     */           protected ItemStack execute(BlockSource param1BlockSource, ItemStack param1ItemStack) {
/* 411 */             ServerLevel serverLevel = param1BlockSource.level();
/*     */             
/* 413 */             BlockPos blockPos = param1BlockSource.pos().relative((Direction)param1BlockSource.state().getValue((Property)DispenserBlock.FACING));
/*     */             
/* 415 */             List list = serverLevel.getEntitiesOfClass(Armadillo.class, new AABB(blockPos), EntitySelector.NO_SPECTATORS);
/* 416 */             if (list.isEmpty()) {
/* 417 */               setSuccess(false);
/* 418 */               return param1ItemStack;
/*     */             } 
/* 420 */             for (Armadillo armadillo : list) {
/* 421 */               if (armadillo.brushOffScute(null, param1ItemStack)) {
/* 422 */                 param1ItemStack.hurtAndBreak(16, serverLevel, null, param1Item -> { 
/* 423 */                     }); return param1ItemStack;
/*     */               } 
/*     */             } 
/* 426 */             setSuccess(false);
/* 427 */             return param1ItemStack;
/*     */           }
/*     */         });
/*     */     
/* 431 */     DispenserBlock.registerBehavior((ItemLike)Items.HONEYCOMB, new OptionalDispenseItemBehavior()
/*     */         {
/*     */           public ItemStack execute(BlockSource param1BlockSource, ItemStack param1ItemStack) {
/* 434 */             BlockPos blockPos = param1BlockSource.pos().relative((Direction)param1BlockSource.state().getValue((Property)DispenserBlock.FACING));
/* 435 */             ServerLevel serverLevel = param1BlockSource.level();
/* 436 */             BlockState blockState = serverLevel.getBlockState(blockPos);
/*     */             
/* 438 */             Optional<BlockState> optional = HoneycombItem.getWaxed(blockState);
/* 439 */             if (optional.isPresent()) {
/* 440 */               serverLevel.setBlockAndUpdate(blockPos, optional.get());
/* 441 */               serverLevel.levelEvent(3003, blockPos, 0);
/* 442 */               param1ItemStack.shrink(1);
/* 443 */               setSuccess(true);
/*     */               
/* 445 */               return param1ItemStack;
/*     */             } 
/* 447 */             return super.execute(param1BlockSource, param1ItemStack);
/*     */           }
/*     */         });
/*     */     
/* 451 */     DispenserBlock.registerBehavior((ItemLike)Items.POTION, new DefaultDispenseItemBehavior() {
/* 452 */           private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
/*     */ 
/*     */           
/*     */           public ItemStack execute(BlockSource param1BlockSource, ItemStack param1ItemStack) {
/* 456 */             PotionContents potionContents = (PotionContents)param1ItemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
/* 457 */             if (!potionContents.is(Potions.WATER)) {
/* 458 */               return this.defaultDispenseItemBehavior.dispense(param1BlockSource, param1ItemStack);
/*     */             }
/*     */             
/* 461 */             ServerLevel serverLevel = param1BlockSource.level();
/* 462 */             BlockPos blockPos1 = param1BlockSource.pos();
/*     */             
/* 464 */             BlockPos blockPos2 = param1BlockSource.pos().relative((Direction)param1BlockSource.state().getValue((Property)DispenserBlock.FACING));
/* 465 */             if (serverLevel.getBlockState(blockPos2).is(BlockTags.CONVERTABLE_TO_MUD)) {
/* 466 */               if (!serverLevel.isClientSide()) {
/* 467 */                 for (byte b = 0; b < 5; b++) {
/* 468 */                   serverLevel.sendParticles((ParticleOptions)ParticleTypes.SPLASH, blockPos1.getX() + serverLevel.random.nextDouble(), (blockPos1.getY() + 1), blockPos1.getZ() + serverLevel.random.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 1.0D);
/*     */                 }
/*     */               }
/*     */               
/* 472 */               serverLevel.playSound(null, blockPos1, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 473 */               serverLevel.gameEvent(null, (Holder)GameEvent.FLUID_PLACE, blockPos1);
/*     */               
/* 475 */               serverLevel.setBlockAndUpdate(blockPos2, Blocks.MUD.defaultBlockState());
/*     */               
/* 477 */               return consumeWithRemainder(param1BlockSource, param1ItemStack, new ItemStack((ItemLike)Items.GLASS_BOTTLE));
/*     */             } 
/*     */             
/* 480 */             return this.defaultDispenseItemBehavior.dispense(param1BlockSource, param1ItemStack);
/*     */           }
/*     */         });
/*     */     
/* 484 */     DispenserBlock.registerBehavior((ItemLike)Items.MINECART, new MinecartDispenseItemBehavior(EntityType.MINECART));
/* 485 */     DispenserBlock.registerBehavior((ItemLike)Items.CHEST_MINECART, new MinecartDispenseItemBehavior(EntityType.CHEST_MINECART));
/* 486 */     DispenserBlock.registerBehavior((ItemLike)Items.FURNACE_MINECART, new MinecartDispenseItemBehavior(EntityType.FURNACE_MINECART));
/* 487 */     DispenserBlock.registerBehavior((ItemLike)Items.TNT_MINECART, new MinecartDispenseItemBehavior(EntityType.TNT_MINECART));
/* 488 */     DispenserBlock.registerBehavior((ItemLike)Items.HOPPER_MINECART, new MinecartDispenseItemBehavior(EntityType.HOPPER_MINECART));
/* 489 */     DispenserBlock.registerBehavior((ItemLike)Items.COMMAND_BLOCK_MINECART, new MinecartDispenseItemBehavior(EntityType.COMMAND_BLOCK_MINECART));
/*     */   }
/*     */   
/*     */   ItemStack dispense(BlockSource paramBlockSource, ItemStack paramItemStack);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\dispenser\DispenseItemBehavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */