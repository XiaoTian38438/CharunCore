/*     */ package net.minecraft.world.level.block.state;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.DependantName;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.flag.FeatureFlag;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.SoundType;
/*     */ import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
/*     */ import net.minecraft.world.level.material.MapColor;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.phys.Vec3;
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
/*     */ public class Properties
/*     */ {
/* 464 */   public static final Codec<Properties> CODEC = MapCodec.unitCodec(() -> of());
/*     */   
/*     */   Function<BlockState, MapColor> mapColor = paramBlockState -> MapColor.NONE;
/*     */   
/*     */   boolean hasCollision = true;
/* 469 */   SoundType soundType = SoundType.STONE;
/*     */   ToIntFunction<BlockState> lightEmission = paramBlockState -> 0;
/*     */   float explosionResistance;
/*     */   float destroyTime;
/*     */   boolean requiresCorrectToolForDrops;
/*     */   boolean isRandomlyTicking;
/* 475 */   float friction = 0.6F;
/* 476 */   float speedFactor = 1.0F; private ResourceKey<Block> id; private DependantName<Block, Optional<ResourceKey<LootTable>>> drops; private DependantName<Block, String> descriptionId; boolean canOcclude; boolean isAir; boolean ignitedByLava;
/* 477 */   float jumpFactor = 1.0F; @Deprecated
/*     */   boolean liquid; @Deprecated
/*     */   boolean forceSolidOff; boolean forceSolidOn; PushReaction pushReaction; boolean spawnTerrainParticles;
/*     */   
/*     */   private Properties() {
/* 482 */     this.drops = (paramResourceKey -> Optional.of(ResourceKey.create(Registries.LOOT_TABLE, paramResourceKey.identifier().withPrefix("blocks/"))));
/* 483 */     this.descriptionId = (paramResourceKey -> Util.makeDescriptionId("block", paramResourceKey.identifier()));
/* 484 */     this.canOcclude = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 492 */     this.pushReaction = PushReaction.NORMAL;
/* 493 */     this.spawnTerrainParticles = true;
/* 494 */     this.instrument = NoteBlockInstrument.HARP;
/*     */ 
/*     */     
/* 497 */     this.isValidSpawn = ((paramBlockState, paramBlockGetter, paramBlockPos, paramEntityType) -> 
/* 498 */       (paramBlockState.isFaceSturdy(paramBlockGetter, paramBlockPos, Direction.UP) && paramBlockState.getLightEmission() < 14));
/*     */     
/* 500 */     this.isRedstoneConductor = ((paramBlockState, paramBlockGetter, paramBlockPos) -> paramBlockState.isCollisionShapeFullBlock(paramBlockGetter, paramBlockPos));
/*     */ 
/*     */     
/* 503 */     this.isSuffocating = ((paramBlockState, paramBlockGetter, paramBlockPos) -> 
/* 504 */       (paramBlockState.blocksMotion() && paramBlockState.isCollisionShapeFullBlock(paramBlockGetter, paramBlockPos)));
/*     */     
/* 506 */     this.isViewBlocking = this.isSuffocating;
/* 507 */     this.hasPostProcess = ((paramBlockState, paramBlockGetter, paramBlockPos) -> false);
/* 508 */     this.emissiveRendering = ((paramBlockState, paramBlockGetter, paramBlockPos) -> false);
/*     */ 
/*     */     
/* 511 */     this.requiredFeatures = FeatureFlags.VANILLA_SET;
/*     */   }
/*     */   NoteBlockInstrument instrument; boolean replaceable; BlockBehaviour.StateArgumentPredicate<EntityType<?>> isValidSpawn; BlockBehaviour.StatePredicate isRedstoneConductor; BlockBehaviour.StatePredicate isSuffocating; BlockBehaviour.StatePredicate isViewBlocking; BlockBehaviour.StatePredicate hasPostProcess; BlockBehaviour.StatePredicate emissiveRendering;
/*     */   boolean dynamicShape;
/*     */   FeatureFlagSet requiredFeatures;
/*     */   BlockBehaviour.OffsetFunction offsetFunction;
/*     */   
/*     */   public static Properties of() {
/* 519 */     return new Properties();
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
/*     */   public static Properties ofFullCopy(BlockBehaviour paramBlockBehaviour) {
/* 531 */     Properties properties1 = ofLegacyCopy(paramBlockBehaviour);
/* 532 */     Properties properties2 = paramBlockBehaviour.properties;
/*     */     
/* 534 */     properties1.jumpFactor = properties2.jumpFactor;
/* 535 */     properties1.isRedstoneConductor = properties2.isRedstoneConductor;
/* 536 */     properties1.isValidSpawn = properties2.isValidSpawn;
/* 537 */     properties1.hasPostProcess = properties2.hasPostProcess;
/* 538 */     properties1.isSuffocating = properties2.isSuffocating;
/* 539 */     properties1.isViewBlocking = properties2.isViewBlocking;
/* 540 */     properties1.drops = properties2.drops;
/* 541 */     properties1.descriptionId = properties2.descriptionId;
/*     */     
/* 543 */     return properties1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static Properties ofLegacyCopy(BlockBehaviour paramBlockBehaviour) {
/* 552 */     Properties properties1 = new Properties();
/* 553 */     Properties properties2 = paramBlockBehaviour.properties;
/*     */     
/* 555 */     properties1.destroyTime = properties2.destroyTime;
/* 556 */     properties1.explosionResistance = properties2.explosionResistance;
/* 557 */     properties1.hasCollision = properties2.hasCollision;
/* 558 */     properties1.isRandomlyTicking = properties2.isRandomlyTicking;
/* 559 */     properties1.lightEmission = properties2.lightEmission;
/* 560 */     properties1.mapColor = properties2.mapColor;
/* 561 */     properties1.soundType = properties2.soundType;
/* 562 */     properties1.friction = properties2.friction;
/* 563 */     properties1.speedFactor = properties2.speedFactor;
/* 564 */     properties1.dynamicShape = properties2.dynamicShape;
/* 565 */     properties1.canOcclude = properties2.canOcclude;
/* 566 */     properties1.isAir = properties2.isAir;
/* 567 */     properties1.ignitedByLava = properties2.ignitedByLava;
/* 568 */     properties1.liquid = properties2.liquid;
/* 569 */     properties1.forceSolidOff = properties2.forceSolidOff;
/* 570 */     properties1.forceSolidOn = properties2.forceSolidOn;
/* 571 */     properties1.pushReaction = properties2.pushReaction;
/* 572 */     properties1.requiresCorrectToolForDrops = properties2.requiresCorrectToolForDrops;
/* 573 */     properties1.offsetFunction = properties2.offsetFunction;
/* 574 */     properties1.spawnTerrainParticles = properties2.spawnTerrainParticles;
/* 575 */     properties1.requiredFeatures = properties2.requiredFeatures;
/* 576 */     properties1.emissiveRendering = properties2.emissiveRendering;
/* 577 */     properties1.instrument = properties2.instrument;
/* 578 */     properties1.replaceable = properties2.replaceable;
/*     */     
/* 580 */     return properties1;
/*     */   }
/*     */   
/*     */   public Properties mapColor(DyeColor paramDyeColor) {
/* 584 */     this.mapColor = (paramBlockState -> paramDyeColor.getMapColor());
/* 585 */     return this;
/*     */   }
/*     */   
/*     */   public Properties mapColor(MapColor paramMapColor) {
/* 589 */     this.mapColor = (paramBlockState -> paramMapColor);
/* 590 */     return this;
/*     */   }
/*     */   
/*     */   public Properties mapColor(Function<BlockState, MapColor> paramFunction) {
/* 594 */     this.mapColor = paramFunction;
/* 595 */     return this;
/*     */   }
/*     */   
/*     */   public Properties noCollision() {
/* 599 */     this.hasCollision = false;
/* 600 */     this.canOcclude = false;
/* 601 */     return this;
/*     */   }
/*     */   
/*     */   public Properties noOcclusion() {
/* 605 */     this.canOcclude = false;
/* 606 */     return this;
/*     */   }
/*     */   
/*     */   public Properties friction(float paramFloat) {
/* 610 */     this.friction = paramFloat;
/* 611 */     return this;
/*     */   }
/*     */   
/*     */   public Properties speedFactor(float paramFloat) {
/* 615 */     this.speedFactor = paramFloat;
/* 616 */     return this;
/*     */   }
/*     */   
/*     */   public Properties jumpFactor(float paramFloat) {
/* 620 */     this.jumpFactor = paramFloat;
/* 621 */     return this;
/*     */   }
/*     */   
/*     */   public Properties sound(SoundType paramSoundType) {
/* 625 */     this.soundType = paramSoundType;
/* 626 */     return this;
/*     */   }
/*     */   
/*     */   public Properties lightLevel(ToIntFunction<BlockState> paramToIntFunction) {
/* 630 */     this.lightEmission = paramToIntFunction;
/* 631 */     return this;
/*     */   }
/*     */   
/*     */   public Properties strength(float paramFloat1, float paramFloat2) {
/* 635 */     return destroyTime(paramFloat1).explosionResistance(paramFloat2);
/*     */   }
/*     */   
/*     */   public Properties instabreak() {
/* 639 */     return strength(0.0F);
/*     */   }
/*     */   
/*     */   public Properties strength(float paramFloat) {
/* 643 */     strength(paramFloat, paramFloat);
/* 644 */     return this;
/*     */   }
/*     */   
/*     */   public Properties randomTicks() {
/* 648 */     this.isRandomlyTicking = true;
/* 649 */     return this;
/*     */   }
/*     */   
/*     */   public Properties dynamicShape() {
/* 653 */     this.dynamicShape = true;
/* 654 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Properties noLootTable() {
/* 662 */     this.drops = DependantName.fixed(Optional.empty());
/* 663 */     return this;
/*     */   }
/*     */   
/*     */   public Properties overrideLootTable(Optional<ResourceKey<LootTable>> paramOptional) {
/* 667 */     this.drops = DependantName.fixed(paramOptional);
/* 668 */     return this;
/*     */   }
/*     */   
/*     */   protected Optional<ResourceKey<LootTable>> effectiveDrops() {
/* 672 */     return (Optional<ResourceKey<LootTable>>)this.drops.get(Objects.<ResourceKey>requireNonNull(this.id, "Block id not set"));
/*     */   }
/*     */   
/*     */   public Properties ignitedByLava() {
/* 676 */     this.ignitedByLava = true;
/* 677 */     return this;
/*     */   }
/*     */   
/*     */   public Properties liquid() {
/* 681 */     this.liquid = true;
/* 682 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Properties forceSolidOn() {
/* 689 */     this.forceSolidOn = true;
/* 690 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Properties forceSolidOff() {
/* 699 */     this.forceSolidOff = true;
/* 700 */     return this;
/*     */   }
/*     */   
/*     */   public Properties pushReaction(PushReaction paramPushReaction) {
/* 704 */     this.pushReaction = paramPushReaction;
/* 705 */     return this;
/*     */   }
/*     */   
/*     */   public Properties air() {
/* 709 */     this.isAir = true;
/* 710 */     return this;
/*     */   }
/*     */   
/*     */   public Properties isValidSpawn(BlockBehaviour.StateArgumentPredicate<EntityType<?>> paramStateArgumentPredicate) {
/* 714 */     this.isValidSpawn = paramStateArgumentPredicate;
/* 715 */     return this;
/*     */   }
/*     */   
/*     */   public Properties isRedstoneConductor(BlockBehaviour.StatePredicate paramStatePredicate) {
/* 719 */     this.isRedstoneConductor = paramStatePredicate;
/* 720 */     return this;
/*     */   }
/*     */   
/*     */   public Properties isSuffocating(BlockBehaviour.StatePredicate paramStatePredicate) {
/* 724 */     this.isSuffocating = paramStatePredicate;
/* 725 */     return this;
/*     */   }
/*     */   
/*     */   public Properties isViewBlocking(BlockBehaviour.StatePredicate paramStatePredicate) {
/* 729 */     this.isViewBlocking = paramStatePredicate;
/* 730 */     return this;
/*     */   }
/*     */   
/*     */   public Properties hasPostProcess(BlockBehaviour.StatePredicate paramStatePredicate) {
/* 734 */     this.hasPostProcess = paramStatePredicate;
/* 735 */     return this;
/*     */   }
/*     */   
/*     */   public Properties emissiveRendering(BlockBehaviour.StatePredicate paramStatePredicate) {
/* 739 */     this.emissiveRendering = paramStatePredicate;
/* 740 */     return this;
/*     */   }
/*     */   
/*     */   public Properties requiresCorrectToolForDrops() {
/* 744 */     this.requiresCorrectToolForDrops = true;
/* 745 */     return this;
/*     */   }
/*     */   
/*     */   public Properties destroyTime(float paramFloat) {
/* 749 */     this.destroyTime = paramFloat;
/* 750 */     return this;
/*     */   }
/*     */   
/*     */   public Properties explosionResistance(float paramFloat) {
/* 754 */     this.explosionResistance = Math.max(0.0F, paramFloat);
/* 755 */     return this;
/*     */   }
/*     */   
/*     */   public Properties offsetType(BlockBehaviour.OffsetType paramOffsetType) {
/* 759 */     switch (paramOffsetType.ordinal()) { default: throw new MatchException(null, null);case 0: case 2: case 1: break; }  this
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
/* 771 */       .offsetFunction = ((paramBlockState, paramBlockPos) -> {
/*     */         Block block = paramBlockState.getBlock();
/*     */         
/*     */         long l = Mth.getSeed(paramBlockPos.getX(), 0, paramBlockPos.getZ());
/*     */         
/*     */         float f = block.getMaxHorizontalOffset();
/*     */         double d1 = Mth.clamp((((float)(l & 0xFL) / 15.0F) - 0.5D) * 0.5D, -f, f);
/*     */         double d2 = Mth.clamp((((float)(l >> 8L & 0xFL) / 15.0F) - 0.5D) * 0.5D, -f, f);
/*     */         return new Vec3(d1, 0.0D, d2);
/*     */       });
/* 781 */     return this;
/*     */   }
/*     */   
/*     */   public Properties noTerrainParticles() {
/* 785 */     this.spawnTerrainParticles = false;
/* 786 */     return this;
/*     */   }
/*     */   
/*     */   public Properties requiredFeatures(FeatureFlag... paramVarArgs) {
/* 790 */     this.requiredFeatures = FeatureFlags.REGISTRY.subset(paramVarArgs);
/* 791 */     return this;
/*     */   }
/*     */   
/*     */   public Properties instrument(NoteBlockInstrument paramNoteBlockInstrument) {
/* 795 */     this.instrument = paramNoteBlockInstrument;
/* 796 */     return this;
/*     */   }
/*     */   
/*     */   public Properties replaceable() {
/* 800 */     this.replaceable = true;
/* 801 */     return this;
/*     */   }
/*     */   
/*     */   public Properties setId(ResourceKey<Block> paramResourceKey) {
/* 805 */     this.id = paramResourceKey;
/* 806 */     return this;
/*     */   }
/*     */   
/*     */   public Properties overrideDescription(String paramString) {
/* 810 */     this.descriptionId = DependantName.fixed(paramString);
/* 811 */     return this;
/*     */   }
/*     */   
/*     */   protected String effectiveDescriptionId() {
/* 815 */     return (String)this.descriptionId.get(Objects.<ResourceKey>requireNonNull(this.id, "Block id not set"));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\state\BlockBehaviour$Properties.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */