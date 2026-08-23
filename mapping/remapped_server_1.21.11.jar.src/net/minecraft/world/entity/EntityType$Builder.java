/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.DependantName;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.fixes.References;
/*     */ import net.minecraft.world.flag.FeatureFlag;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder<T extends Entity>
/*     */ {
/*     */   private final EntityType.EntityFactory<T> factory;
/*     */   private final MobCategory category;
/* 777 */   private ImmutableSet<Block> immuneTo = ImmutableSet.of();
/*     */   private boolean serialize = true;
/*     */   private boolean summon = true;
/*     */   private boolean fireImmune;
/*     */   private boolean canSpawnFarFromPlayer;
/* 782 */   private int clientTrackingRange = 5;
/* 783 */   private int updateInterval = 3;
/* 784 */   private EntityDimensions dimensions = EntityDimensions.scalable(0.6F, 1.8F);
/* 785 */   private float spawnDimensionsScale = 1.0F;
/* 786 */   private EntityAttachments.Builder attachments = EntityAttachments.builder();
/* 787 */   private FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET; private DependantName<EntityType<?>, Optional<ResourceKey<LootTable>>> lootTable; private Builder(EntityType.EntityFactory<T> paramEntityFactory, MobCategory paramMobCategory) {
/* 788 */     this.lootTable = (paramResourceKey -> Optional.of(ResourceKey.create(Registries.LOOT_TABLE, paramResourceKey.identifier().withPrefix("entities/"))));
/* 789 */     this.descriptionId = (paramResourceKey -> Util.makeDescriptionId("entity", paramResourceKey.identifier()));
/* 790 */     this.allowedInPeaceful = true;
/*     */ 
/*     */     
/* 793 */     this.factory = paramEntityFactory;
/* 794 */     this.category = paramMobCategory;
/* 795 */     this.canSpawnFarFromPlayer = (paramMobCategory == MobCategory.CREATURE || paramMobCategory == MobCategory.MISC);
/*     */   }
/*     */   private final DependantName<EntityType<?>, String> descriptionId; private boolean allowedInPeaceful;
/*     */   public static <T extends Entity> Builder<T> of(EntityType.EntityFactory<T> paramEntityFactory, MobCategory paramMobCategory) {
/* 799 */     return new Builder<>(paramEntityFactory, paramMobCategory);
/*     */   }
/*     */   
/*     */   public static <T extends Entity> Builder<T> createNothing(MobCategory paramMobCategory) {
/* 803 */     return new Builder<>((paramEntityType, paramLevel) -> null, paramMobCategory);
/*     */   }
/*     */   
/*     */   public Builder<T> sized(float paramFloat1, float paramFloat2) {
/* 807 */     this.dimensions = EntityDimensions.scalable(paramFloat1, paramFloat2);
/* 808 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> spawnDimensionsScale(float paramFloat) {
/* 812 */     this.spawnDimensionsScale = paramFloat;
/* 813 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> eyeHeight(float paramFloat) {
/* 817 */     this.dimensions = this.dimensions.withEyeHeight(paramFloat);
/* 818 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> passengerAttachments(float... paramVarArgs) {
/* 822 */     for (float f : paramVarArgs) {
/* 823 */       this.attachments = this.attachments.attach(EntityAttachment.PASSENGER, 0.0F, f, 0.0F);
/*     */     }
/* 825 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> passengerAttachments(Vec3... paramVarArgs) {
/* 829 */     for (Vec3 vec3 : paramVarArgs) {
/* 830 */       this.attachments = this.attachments.attach(EntityAttachment.PASSENGER, vec3);
/*     */     }
/* 832 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> vehicleAttachment(Vec3 paramVec3) {
/* 836 */     return attach(EntityAttachment.VEHICLE, paramVec3);
/*     */   }
/*     */   
/*     */   public Builder<T> ridingOffset(float paramFloat) {
/* 840 */     return attach(EntityAttachment.VEHICLE, 0.0F, -paramFloat, 0.0F);
/*     */   }
/*     */   
/*     */   public Builder<T> nameTagOffset(float paramFloat) {
/* 844 */     return attach(EntityAttachment.NAME_TAG, 0.0F, paramFloat, 0.0F);
/*     */   }
/*     */   
/*     */   public Builder<T> attach(EntityAttachment paramEntityAttachment, float paramFloat1, float paramFloat2, float paramFloat3) {
/* 848 */     this.attachments = this.attachments.attach(paramEntityAttachment, paramFloat1, paramFloat2, paramFloat3);
/* 849 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> attach(EntityAttachment paramEntityAttachment, Vec3 paramVec3) {
/* 853 */     this.attachments = this.attachments.attach(paramEntityAttachment, paramVec3);
/* 854 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> noSummon() {
/* 858 */     this.summon = false;
/* 859 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> noSave() {
/* 863 */     this.serialize = false;
/* 864 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> fireImmune() {
/* 868 */     this.fireImmune = true;
/* 869 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> immuneTo(Block... paramVarArgs) {
/* 873 */     this.immuneTo = ImmutableSet.copyOf((Object[])paramVarArgs);
/* 874 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> canSpawnFarFromPlayer() {
/* 878 */     this.canSpawnFarFromPlayer = true;
/* 879 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> clientTrackingRange(int paramInt) {
/* 883 */     this.clientTrackingRange = paramInt;
/* 884 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> updateInterval(int paramInt) {
/* 888 */     this.updateInterval = paramInt;
/* 889 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> requiredFeatures(FeatureFlag... paramVarArgs) {
/* 893 */     this.requiredFeatures = FeatureFlags.REGISTRY.subset(paramVarArgs);
/* 894 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> noLootTable() {
/* 898 */     this.lootTable = DependantName.fixed(Optional.empty());
/* 899 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> notInPeaceful() {
/* 903 */     this.allowedInPeaceful = false;
/* 904 */     return this;
/*     */   }
/*     */   
/*     */   public EntityType<T> build(ResourceKey<EntityType<?>> paramResourceKey) {
/* 908 */     if (this.serialize) {
/* 909 */       Util.fetchChoiceType(References.ENTITY_TREE, paramResourceKey.identifier().toString());
/*     */     }
/*     */     
/* 912 */     return new EntityType<>(this.factory, this.category, this.serialize, this.summon, this.fireImmune, this.canSpawnFarFromPlayer, this.immuneTo, this.dimensions.withAttachments(this.attachments), this.spawnDimensionsScale, this.clientTrackingRange, this.updateInterval, (String)this.descriptionId.get(paramResourceKey), (Optional<ResourceKey<LootTable>>)this.lootTable.get(paramResourceKey), this.requiredFeatures, this.allowedInPeaceful);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\EntityType$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */