/*     */ package net.minecraft.world.entity.decoration;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.world.entity.Avatar;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.player.PlayerModelPart;
/*     */ import net.minecraft.world.item.component.ResolvableProfile;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class Mannequin
/*     */   extends Avatar {
/*  29 */   protected static final EntityDataAccessor<ResolvableProfile> DATA_PROFILE = SynchedEntityData.defineId(Mannequin.class, EntityDataSerializers.RESOLVABLE_PROFILE);
/*  30 */   private static final EntityDataAccessor<Boolean> DATA_IMMOVABLE = SynchedEntityData.defineId(Mannequin.class, EntityDataSerializers.BOOLEAN); private static final byte ALL_LAYERS;
/*  31 */   private static final EntityDataAccessor<Optional<Component>> DATA_DESCRIPTION = SynchedEntityData.defineId(Mannequin.class, EntityDataSerializers.OPTIONAL_COMPONENT);
/*     */   static {
/*  33 */     ALL_LAYERS = (byte)Arrays.<PlayerModelPart>stream(PlayerModelPart.values()).mapToInt(PlayerModelPart::getMask).reduce(0, (paramInt1, paramInt2) -> paramInt1 | paramInt2);
/*     */   }
/*  35 */   private static final Set<Pose> VALID_POSES = Set.of(Pose.STANDING, Pose.CROUCHING, Pose.SWIMMING, Pose.FALL_FLYING, Pose.SLEEPING); static {
/*  36 */     POSE_CODEC = Pose.CODEC.validate(paramPose -> VALID_POSES.contains(paramPose) ? DataResult.success(paramPose) : DataResult.error(()));
/*     */     
/*  38 */     LAYERS_CODEC = PlayerModelPart.CODEC.listOf().xmap(paramList -> Byte.valueOf((byte)paramList.stream().mapToInt(PlayerModelPart::getMask).reduce(ALL_LAYERS, ())), paramByte -> Arrays.<PlayerModelPart>stream(PlayerModelPart.values()).filter(()).toList());
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Codec<Pose> POSE_CODEC;
/*     */   private static final Codec<Byte> LAYERS_CODEC;
/*  44 */   public static final ResolvableProfile DEFAULT_PROFILE = (ResolvableProfile)ResolvableProfile.Static.EMPTY;
/*     */   
/*  46 */   private static final Component DEFAULT_DESCRIPTION = (Component)Component.translatable("entity.minecraft.mannequin.label");
/*     */   
/*  48 */   protected static EntityType.EntityFactory<Mannequin> constructor = Mannequin::new;
/*     */   
/*     */   private static final String PROFILE_FIELD = "profile";
/*     */   
/*     */   private static final String HIDDEN_LAYERS_FIELD = "hidden_layers";
/*     */   private static final String MAIN_HAND_FIELD = "main_hand";
/*     */   private static final String POSE_FIELD = "pose";
/*     */   private static final String IMMOVABLE_FIELD = "immovable";
/*     */   private static final String DESCRIPTION_FIELD = "description";
/*     */   private static final String HIDE_DESCRIPTION_FIELD = "hide_description";
/*  58 */   private Component description = DEFAULT_DESCRIPTION;
/*     */   private boolean hideDescription = false;
/*     */   
/*     */   public Mannequin(EntityType<Mannequin> paramEntityType, Level paramLevel) {
/*  62 */     super(paramEntityType, paramLevel);
/*  63 */     this.entityData.set(DATA_PLAYER_MODE_CUSTOMISATION, Byte.valueOf(ALL_LAYERS));
/*     */   }
/*     */   
/*     */   protected Mannequin(Level paramLevel) {
/*  67 */     this(EntityType.MANNEQUIN, paramLevel);
/*     */   }
/*     */   
/*     */   public static Mannequin create(EntityType<Mannequin> paramEntityType, Level paramLevel) {
/*  71 */     return (Mannequin)constructor.create(paramEntityType, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  76 */     super.defineSynchedData(paramBuilder);
/*     */     
/*  78 */     paramBuilder.define(DATA_PROFILE, DEFAULT_PROFILE);
/*  79 */     paramBuilder.define(DATA_IMMOVABLE, Boolean.valueOf(false));
/*  80 */     paramBuilder.define(DATA_DESCRIPTION, Optional.of(DEFAULT_DESCRIPTION));
/*     */   }
/*     */   
/*     */   protected ResolvableProfile getProfile() {
/*  84 */     return (ResolvableProfile)this.entityData.get(DATA_PROFILE);
/*     */   }
/*     */   
/*     */   private void setProfile(ResolvableProfile paramResolvableProfile) {
/*  88 */     this.entityData.set(DATA_PROFILE, paramResolvableProfile);
/*     */   }
/*     */   
/*     */   private boolean getImmovable() {
/*  92 */     return ((Boolean)this.entityData.get(DATA_IMMOVABLE)).booleanValue();
/*     */   }
/*     */   
/*     */   private void setImmovable(boolean paramBoolean) {
/*  96 */     this.entityData.set(DATA_IMMOVABLE, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */   
/*     */   protected Component getDescription() {
/* 100 */     return ((Optional<Component>)this.entityData.get(DATA_DESCRIPTION)).orElse(null);
/*     */   }
/*     */   
/*     */   private void setDescription(Component paramComponent) {
/* 104 */     this.description = paramComponent;
/* 105 */     updateDescription();
/*     */   }
/*     */   
/*     */   private void setHideDescription(boolean paramBoolean) {
/* 109 */     this.hideDescription = paramBoolean;
/* 110 */     updateDescription();
/*     */   }
/*     */   
/*     */   private void updateDescription() {
/* 114 */     this.entityData.set(DATA_DESCRIPTION, this.hideDescription ? Optional.empty() : Optional.<Component>of(this.description));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isImmobile() {
/* 119 */     return (getImmovable() || super.isImmobile());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEffectiveAi() {
/* 124 */     return (!getImmovable() && super.isEffectiveAi());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 129 */     super.addAdditionalSaveData(paramValueOutput);
/*     */     
/* 131 */     paramValueOutput.store("profile", ResolvableProfile.CODEC, getProfile());
/* 132 */     paramValueOutput.store("hidden_layers", LAYERS_CODEC, this.entityData.get(DATA_PLAYER_MODE_CUSTOMISATION));
/* 133 */     paramValueOutput.store("main_hand", HumanoidArm.CODEC, getMainArm());
/* 134 */     paramValueOutput.store("pose", POSE_CODEC, getPose());
/* 135 */     paramValueOutput.putBoolean("immovable", getImmovable());
/* 136 */     Component component = getDescription();
/* 137 */     if (component != null) {
/* 138 */       if (!component.equals(DEFAULT_DESCRIPTION)) {
/* 139 */         paramValueOutput.store("description", ComponentSerialization.CODEC, component);
/*     */       }
/*     */     } else {
/* 142 */       paramValueOutput.putBoolean("hide_description", true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 148 */     super.readAdditionalSaveData(paramValueInput);
/*     */     
/* 150 */     paramValueInput.read("profile", ResolvableProfile.CODEC).ifPresent(this::setProfile);
/* 151 */     this.entityData.set(DATA_PLAYER_MODE_CUSTOMISATION, paramValueInput.read("hidden_layers", LAYERS_CODEC).orElse(Byte.valueOf(ALL_LAYERS)));
/* 152 */     setMainArm(paramValueInput.read("main_hand", HumanoidArm.CODEC).orElse(DEFAULT_MAIN_HAND));
/* 153 */     setPose(paramValueInput.read("pose", POSE_CODEC).orElse(Pose.STANDING));
/* 154 */     setImmovable(paramValueInput.getBooleanOr("immovable", false));
/* 155 */     setHideDescription(paramValueInput.getBooleanOr("hide_description", false));
/* 156 */     setDescription(paramValueInput.read("description", ComponentSerialization.CODEC).orElse(DEFAULT_DESCRIPTION));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> paramDataComponentType) {
/* 161 */     if (paramDataComponentType == DataComponents.PROFILE) {
/* 162 */       return (T)castComponentValue(paramDataComponentType, getProfile());
/*     */     }
/*     */     
/* 165 */     return (T)super.get(paramDataComponentType);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter paramDataComponentGetter) {
/* 170 */     applyImplicitComponentIfPresent(paramDataComponentGetter, DataComponents.PROFILE);
/* 171 */     super.applyImplicitComponents(paramDataComponentGetter);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> paramDataComponentType, T paramT) {
/* 176 */     if (paramDataComponentType == DataComponents.PROFILE) {
/* 177 */       setProfile((ResolvableProfile)castComponentValue(DataComponents.PROFILE, paramT));
/* 178 */       return true;
/*     */     } 
/*     */     
/* 181 */     return super.applyImplicitComponent(paramDataComponentType, paramT);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\decoration\Mannequin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */