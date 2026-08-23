/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.BucketPickup;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ 
/*     */ public class BucketItem
/*     */   extends Item
/*     */   implements DispensibleContainerItem
/*     */ {
/*     */   private final Fluid content;
/*     */   
/*     */   public BucketItem(Fluid paramFluid, Item.Properties paramProperties) {
/*  37 */     super(paramProperties);
/*  38 */     this.content = paramFluid;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand) {
/*  43 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/*  44 */     BlockHitResult blockHitResult = getPlayerPOVHitResult(paramLevel, paramPlayer, (this.content == Fluids.EMPTY) ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
/*  45 */     if (blockHitResult.getType() == HitResult.Type.MISS) {
/*  46 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/*  49 */     if (blockHitResult.getType() == HitResult.Type.BLOCK) {
/*  50 */       BlockPos blockPos1 = blockHitResult.getBlockPos();
/*  51 */       Direction direction = blockHitResult.getDirection();
/*  52 */       BlockPos blockPos2 = blockPos1.relative(direction);
/*     */       
/*  54 */       if (!paramLevel.mayInteract((Entity)paramPlayer, blockPos1) || !paramPlayer.mayUseItemAt(blockPos2, direction, itemStack)) {
/*  55 */         return (InteractionResult)InteractionResult.FAIL;
/*     */       }
/*     */       
/*  58 */       if (this.content == Fluids.EMPTY) {
/*  59 */         BlockState blockState1 = paramLevel.getBlockState(blockPos1);
/*     */         
/*  61 */         Block block = blockState1.getBlock(); if (block instanceof BucketPickup) { BucketPickup bucketPickup = (BucketPickup)block;
/*  62 */           ItemStack itemStack1 = bucketPickup.pickupBlock((LivingEntity)paramPlayer, (LevelAccessor)paramLevel, blockPos1, blockState1);
/*  63 */           if (!itemStack1.isEmpty()) {
/*  64 */             paramPlayer.awardStat(Stats.ITEM_USED.get(this));
/*  65 */             bucketPickup.getPickupSound().ifPresent(paramSoundEvent -> paramPlayer.playSound(paramSoundEvent, 1.0F, 1.0F));
/*  66 */             paramLevel.gameEvent((Entity)paramPlayer, (Holder)GameEvent.FLUID_PICKUP, blockPos1);
/*  67 */             ItemStack itemStack2 = ItemUtils.createFilledResult(itemStack, paramPlayer, itemStack1);
/*  68 */             if (!paramLevel.isClientSide()) {
/*  69 */               CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)paramPlayer, itemStack1);
/*     */             }
/*  71 */             return (InteractionResult)InteractionResult.SUCCESS.heldItemTransformedTo(itemStack2);
/*     */           }  }
/*     */ 
/*     */         
/*  75 */         return (InteractionResult)InteractionResult.FAIL;
/*     */       } 
/*  77 */       BlockState blockState = paramLevel.getBlockState(blockPos1);
/*  78 */       BlockPos blockPos3 = (blockState.getBlock() instanceof net.minecraft.world.level.block.LiquidBlockContainer && this.content == Fluids.WATER) ? blockPos1 : blockPos2;
/*     */       
/*  80 */       if (emptyContents((LivingEntity)paramPlayer, paramLevel, blockPos3, blockHitResult)) {
/*  81 */         checkExtraContent((LivingEntity)paramPlayer, paramLevel, itemStack, blockPos3);
/*  82 */         if (paramPlayer instanceof ServerPlayer) {
/*  83 */           CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)paramPlayer, blockPos3, itemStack);
/*     */         }
/*  85 */         paramPlayer.awardStat(Stats.ITEM_USED.get(this));
/*  86 */         ItemStack itemStack1 = ItemUtils.createFilledResult(itemStack, paramPlayer, getEmptySuccessItem(itemStack, paramPlayer));
/*  87 */         return (InteractionResult)InteractionResult.SUCCESS.heldItemTransformedTo(itemStack1);
/*     */       } 
/*  89 */       return (InteractionResult)InteractionResult.FAIL;
/*     */     } 
/*     */ 
/*     */     
/*  93 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   public static ItemStack getEmptySuccessItem(ItemStack paramItemStack, Player paramPlayer) {
/*  97 */     if (!paramPlayer.hasInfiniteMaterials()) {
/*  98 */       return new ItemStack(Items.BUCKET);
/*     */     }
/* 100 */     return paramItemStack;
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
/*     */   public void checkExtraContent(LivingEntity paramLivingEntity, Level paramLevel, ItemStack paramItemStack, BlockPos paramBlockPos) {}
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
/*     */   public boolean emptyContents(LivingEntity paramLivingEntity, Level paramLevel, BlockPos paramBlockPos, BlockHitResult paramBlockHitResult) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield content : Lnet/minecraft/world/level/material/Fluid;
/*     */     //   4: astore #6
/*     */     //   6: aload #6
/*     */     //   8: instanceof net/minecraft/world/level/material/FlowingFluid
/*     */     //   11: ifeq -> 24
/*     */     //   14: aload #6
/*     */     //   16: checkcast net/minecraft/world/level/material/FlowingFluid
/*     */     //   19: astore #5
/*     */     //   21: goto -> 26
/*     */     //   24: iconst_0
/*     */     //   25: ireturn
/*     */     //   26: aload_2
/*     */     //   27: aload_3
/*     */     //   28: invokevirtual getBlockState : (Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   31: astore #6
/*     */     //   33: aload #6
/*     */     //   35: invokevirtual getBlock : ()Lnet/minecraft/world/level/block/Block;
/*     */     //   38: astore #7
/*     */     //   40: aload #6
/*     */     //   42: aload_0
/*     */     //   43: getfield content : Lnet/minecraft/world/level/material/Fluid;
/*     */     //   46: invokevirtual canBeReplaced : (Lnet/minecraft/world/level/material/Fluid;)Z
/*     */     //   49: istore #8
/*     */     //   51: aload_1
/*     */     //   52: ifnull -> 66
/*     */     //   55: aload_1
/*     */     //   56: invokevirtual isShiftKeyDown : ()Z
/*     */     //   59: ifeq -> 66
/*     */     //   62: iconst_1
/*     */     //   63: goto -> 67
/*     */     //   66: iconst_0
/*     */     //   67: istore #9
/*     */     //   69: iload #8
/*     */     //   71: ifne -> 108
/*     */     //   74: aload #7
/*     */     //   76: instanceof net/minecraft/world/level/block/LiquidBlockContainer
/*     */     //   79: ifeq -> 112
/*     */     //   82: aload #7
/*     */     //   84: checkcast net/minecraft/world/level/block/LiquidBlockContainer
/*     */     //   87: astore #11
/*     */     //   89: aload #11
/*     */     //   91: aload_1
/*     */     //   92: aload_2
/*     */     //   93: aload_3
/*     */     //   94: aload #6
/*     */     //   96: aload_0
/*     */     //   97: getfield content : Lnet/minecraft/world/level/material/Fluid;
/*     */     //   100: invokeinterface canPlaceLiquid : (Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/Fluid;)Z
/*     */     //   105: ifeq -> 112
/*     */     //   108: iconst_1
/*     */     //   109: goto -> 113
/*     */     //   112: iconst_0
/*     */     //   113: istore #10
/*     */     //   115: aload #6
/*     */     //   117: invokevirtual isAir : ()Z
/*     */     //   120: ifne -> 138
/*     */     //   123: iload #10
/*     */     //   125: ifeq -> 142
/*     */     //   128: iload #9
/*     */     //   130: ifeq -> 138
/*     */     //   133: aload #4
/*     */     //   135: ifnonnull -> 142
/*     */     //   138: iconst_1
/*     */     //   139: goto -> 143
/*     */     //   142: iconst_0
/*     */     //   143: istore #11
/*     */     //   145: iload #11
/*     */     //   147: ifne -> 184
/*     */     //   150: aload #4
/*     */     //   152: ifnull -> 182
/*     */     //   155: aload_0
/*     */     //   156: aload_1
/*     */     //   157: aload_2
/*     */     //   158: aload #4
/*     */     //   160: invokevirtual getBlockPos : ()Lnet/minecraft/core/BlockPos;
/*     */     //   163: aload #4
/*     */     //   165: invokevirtual getDirection : ()Lnet/minecraft/core/Direction;
/*     */     //   168: invokevirtual relative : (Lnet/minecraft/core/Direction;)Lnet/minecraft/core/BlockPos;
/*     */     //   171: aconst_null
/*     */     //   172: invokevirtual emptyContents : (Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/BlockHitResult;)Z
/*     */     //   175: ifeq -> 182
/*     */     //   178: iconst_1
/*     */     //   179: goto -> 183
/*     */     //   182: iconst_0
/*     */     //   183: ireturn
/*     */     //   184: aload_2
/*     */     //   185: invokevirtual environmentAttributes : ()Lnet/minecraft/world/attribute/EnvironmentAttributeSystem;
/*     */     //   188: getstatic net/minecraft/world/attribute/EnvironmentAttributes.WATER_EVAPORATES : Lnet/minecraft/world/attribute/EnvironmentAttribute;
/*     */     //   191: aload_3
/*     */     //   192: invokevirtual getValue : (Lnet/minecraft/world/attribute/EnvironmentAttribute;Lnet/minecraft/core/BlockPos;)Ljava/lang/Object;
/*     */     //   195: checkcast java/lang/Boolean
/*     */     //   198: invokevirtual booleanValue : ()Z
/*     */     //   201: ifeq -> 347
/*     */     //   204: aload_0
/*     */     //   205: getfield content : Lnet/minecraft/world/level/material/Fluid;
/*     */     //   208: getstatic net/minecraft/tags/FluidTags.WATER : Lnet/minecraft/tags/TagKey;
/*     */     //   211: invokevirtual is : (Lnet/minecraft/tags/TagKey;)Z
/*     */     //   214: ifeq -> 347
/*     */     //   217: aload_3
/*     */     //   218: invokevirtual getX : ()I
/*     */     //   221: istore #12
/*     */     //   223: aload_3
/*     */     //   224: invokevirtual getY : ()I
/*     */     //   227: istore #13
/*     */     //   229: aload_3
/*     */     //   230: invokevirtual getZ : ()I
/*     */     //   233: istore #14
/*     */     //   235: aload_2
/*     */     //   236: aload_1
/*     */     //   237: aload_3
/*     */     //   238: getstatic net/minecraft/sounds/SoundEvents.FIRE_EXTINGUISH : Lnet/minecraft/sounds/SoundEvent;
/*     */     //   241: getstatic net/minecraft/sounds/SoundSource.BLOCKS : Lnet/minecraft/sounds/SoundSource;
/*     */     //   244: ldc_w 0.5
/*     */     //   247: ldc_w 2.6
/*     */     //   250: aload_2
/*     */     //   251: getfield random : Lnet/minecraft/util/RandomSource;
/*     */     //   254: invokeinterface nextFloat : ()F
/*     */     //   259: aload_2
/*     */     //   260: getfield random : Lnet/minecraft/util/RandomSource;
/*     */     //   263: invokeinterface nextFloat : ()F
/*     */     //   268: fsub
/*     */     //   269: ldc_w 0.8
/*     */     //   272: fmul
/*     */     //   273: fadd
/*     */     //   274: invokevirtual playSound : (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V
/*     */     //   277: iconst_0
/*     */     //   278: istore #15
/*     */     //   280: iload #15
/*     */     //   282: bipush #8
/*     */     //   284: if_icmpge -> 345
/*     */     //   287: aload_2
/*     */     //   288: getstatic net/minecraft/core/particles/ParticleTypes.LARGE_SMOKE : Lnet/minecraft/core/particles/SimpleParticleType;
/*     */     //   291: iload #12
/*     */     //   293: i2f
/*     */     //   294: aload_2
/*     */     //   295: getfield random : Lnet/minecraft/util/RandomSource;
/*     */     //   298: invokeinterface nextFloat : ()F
/*     */     //   303: fadd
/*     */     //   304: f2d
/*     */     //   305: iload #13
/*     */     //   307: i2f
/*     */     //   308: aload_2
/*     */     //   309: getfield random : Lnet/minecraft/util/RandomSource;
/*     */     //   312: invokeinterface nextFloat : ()F
/*     */     //   317: fadd
/*     */     //   318: f2d
/*     */     //   319: iload #14
/*     */     //   321: i2f
/*     */     //   322: aload_2
/*     */     //   323: getfield random : Lnet/minecraft/util/RandomSource;
/*     */     //   326: invokeinterface nextFloat : ()F
/*     */     //   331: fadd
/*     */     //   332: f2d
/*     */     //   333: dconst_0
/*     */     //   334: dconst_0
/*     */     //   335: dconst_0
/*     */     //   336: invokevirtual addParticle : (Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V
/*     */     //   339: iinc #15, 1
/*     */     //   342: goto -> 280
/*     */     //   345: iconst_1
/*     */     //   346: ireturn
/*     */     //   347: aload #7
/*     */     //   349: instanceof net/minecraft/world/level/block/LiquidBlockContainer
/*     */     //   352: ifeq -> 399
/*     */     //   355: aload #7
/*     */     //   357: checkcast net/minecraft/world/level/block/LiquidBlockContainer
/*     */     //   360: astore #12
/*     */     //   362: aload_0
/*     */     //   363: getfield content : Lnet/minecraft/world/level/material/Fluid;
/*     */     //   366: getstatic net/minecraft/world/level/material/Fluids.WATER : Lnet/minecraft/world/level/material/FlowingFluid;
/*     */     //   369: if_acmpne -> 399
/*     */     //   372: aload #12
/*     */     //   374: aload_2
/*     */     //   375: aload_3
/*     */     //   376: aload #6
/*     */     //   378: aload #5
/*     */     //   380: iconst_0
/*     */     //   381: invokevirtual getSource : (Z)Lnet/minecraft/world/level/material/FluidState;
/*     */     //   384: invokeinterface placeLiquid : (Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/FluidState;)Z
/*     */     //   389: pop
/*     */     //   390: aload_0
/*     */     //   391: aload_1
/*     */     //   392: aload_2
/*     */     //   393: aload_3
/*     */     //   394: invokevirtual playEmptySound : (Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;)V
/*     */     //   397: iconst_1
/*     */     //   398: ireturn
/*     */     //   399: aload_2
/*     */     //   400: invokevirtual isClientSide : ()Z
/*     */     //   403: ifne -> 426
/*     */     //   406: iload #8
/*     */     //   408: ifeq -> 426
/*     */     //   411: aload #6
/*     */     //   413: invokevirtual liquid : ()Z
/*     */     //   416: ifne -> 426
/*     */     //   419: aload_2
/*     */     //   420: aload_3
/*     */     //   421: iconst_1
/*     */     //   422: invokevirtual destroyBlock : (Lnet/minecraft/core/BlockPos;Z)Z
/*     */     //   425: pop
/*     */     //   426: aload_2
/*     */     //   427: aload_3
/*     */     //   428: aload_0
/*     */     //   429: getfield content : Lnet/minecraft/world/level/material/Fluid;
/*     */     //   432: invokevirtual defaultFluidState : ()Lnet/minecraft/world/level/material/FluidState;
/*     */     //   435: invokevirtual createLegacyBlock : ()Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   438: bipush #11
/*     */     //   440: invokevirtual setBlock : (Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z
/*     */     //   443: ifne -> 457
/*     */     //   446: aload #6
/*     */     //   448: invokevirtual getFluidState : ()Lnet/minecraft/world/level/material/FluidState;
/*     */     //   451: invokevirtual isSource : ()Z
/*     */     //   454: ifeq -> 466
/*     */     //   457: aload_0
/*     */     //   458: aload_1
/*     */     //   459: aload_2
/*     */     //   460: aload_3
/*     */     //   461: invokevirtual playEmptySound : (Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;)V
/*     */     //   464: iconst_1
/*     */     //   465: ireturn
/*     */     //   466: iconst_0
/*     */     //   467: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #109	-> 0
/*     */     //   #110	-> 24
/*     */     //   #113	-> 26
/*     */     //   #114	-> 33
/*     */     //   #115	-> 40
/*     */     //   #117	-> 51
/*     */     //   #118	-> 69
/*     */     //   #119	-> 115
/*     */     //   #121	-> 145
/*     */     //   #124	-> 150
/*     */     //   #127	-> 184
/*     */     //   #128	-> 217
/*     */     //   #129	-> 223
/*     */     //   #130	-> 229
/*     */     //   #132	-> 235
/*     */     //   #134	-> 277
/*     */     //   #135	-> 287
/*     */     //   #134	-> 339
/*     */     //   #137	-> 345
/*     */     //   #140	-> 347
/*     */     //   #141	-> 372
/*     */     //   #142	-> 390
/*     */     //   #143	-> 397
/*     */     //   #147	-> 399
/*     */     //   #148	-> 419
/*     */     //   #152	-> 426
/*     */     //   #153	-> 457
/*     */     //   #154	-> 464
/*     */     //   #157	-> 466
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
/*     */   protected void playEmptySound(LivingEntity paramLivingEntity, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos) {
/* 161 */     SoundEvent soundEvent = this.content.is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
/* 162 */     paramLevelAccessor.playSound((Entity)paramLivingEntity, paramBlockPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 163 */     paramLevelAccessor.gameEvent((Entity)paramLivingEntity, (Holder)GameEvent.FLUID_PLACE, paramBlockPos);
/*     */   }
/*     */   
/*     */   public Fluid getContent() {
/* 167 */     return this.content;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\BucketItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */