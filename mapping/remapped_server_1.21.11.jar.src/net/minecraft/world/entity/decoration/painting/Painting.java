/*     */ package net.minecraft.world.entity.decoration.painting;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerEntity;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.PaintingVariantTags;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.decoration.HangingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.variant.VariantUtils;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Painting extends HangingEntity {
/*  41 */   private static final EntityDataAccessor<Holder<PaintingVariant>> DATA_PAINTING_VARIANT_ID = SynchedEntityData.defineId(Painting.class, EntityDataSerializers.PAINTING_VARIANT);
/*     */   
/*     */   public static final float DEPTH = 0.0625F;
/*     */   
/*     */   public Painting(EntityType<? extends Painting> paramEntityType, Level paramLevel) {
/*  46 */     super(paramEntityType, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  51 */     super.defineSynchedData(paramBuilder);
/*  52 */     paramBuilder.define(DATA_PAINTING_VARIANT_ID, VariantUtils.getAny(registryAccess(), Registries.PAINTING_VARIANT));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> paramEntityDataAccessor) {
/*  57 */     super.onSyncedDataUpdated(paramEntityDataAccessor);
/*  58 */     if (DATA_PAINTING_VARIANT_ID.equals(paramEntityDataAccessor)) {
/*  59 */       recalculateBoundingBox();
/*     */     }
/*     */   }
/*     */   
/*     */   private void setVariant(Holder<PaintingVariant> paramHolder) {
/*  64 */     this.entityData.set(DATA_PAINTING_VARIANT_ID, paramHolder);
/*     */   }
/*     */   
/*     */   public Holder<PaintingVariant> getVariant() {
/*  68 */     return (Holder<PaintingVariant>)this.entityData.get(DATA_PAINTING_VARIANT_ID);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> paramDataComponentType) {
/*  73 */     if (paramDataComponentType == DataComponents.PAINTING_VARIANT) {
/*  74 */       return (T)castComponentValue(paramDataComponentType, getVariant());
/*     */     }
/*     */     
/*  77 */     return (T)super.get(paramDataComponentType);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter paramDataComponentGetter) {
/*  82 */     applyImplicitComponentIfPresent(paramDataComponentGetter, DataComponents.PAINTING_VARIANT);
/*  83 */     super.applyImplicitComponents(paramDataComponentGetter);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> paramDataComponentType, T paramT) {
/*  88 */     if (paramDataComponentType == DataComponents.PAINTING_VARIANT) {
/*  89 */       setVariant((Holder<PaintingVariant>)castComponentValue(DataComponents.PAINTING_VARIANT, paramT));
/*  90 */       return true;
/*     */     } 
/*     */     
/*  93 */     return super.applyImplicitComponent(paramDataComponentType, paramT);
/*     */   }
/*     */   
/*     */   public static Optional<Painting> create(Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/*  97 */     Painting painting = new Painting(paramLevel, paramBlockPos);
/*     */     
/*  99 */     ArrayList arrayList = new ArrayList();
/* 100 */     Objects.requireNonNull(arrayList); paramLevel.registryAccess().lookupOrThrow(Registries.PAINTING_VARIANT).getTagOrEmpty(PaintingVariantTags.PLACEABLE).forEach(arrayList::add);
/* 101 */     if (arrayList.isEmpty()) {
/* 102 */       return Optional.empty();
/*     */     }
/*     */     
/* 105 */     painting.setDirection(paramDirection);
/* 106 */     arrayList.removeIf(paramHolder -> {
/*     */           paramPainting.setVariant(paramHolder);
/*     */           
/*     */           return !paramPainting.survives();
/*     */         });
/* 111 */     if (arrayList.isEmpty()) {
/* 112 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 118 */     int i = arrayList.stream().mapToInt(Painting::variantArea).max().orElse(0);
/*     */     
/* 120 */     arrayList.removeIf(paramHolder -> (variantArea(paramHolder) < paramInt));
/* 121 */     Optional<Holder<PaintingVariant>> optional = Util.getRandomSafe(arrayList, painting.random);
/* 122 */     if (optional.isEmpty()) {
/* 123 */       return Optional.empty();
/*     */     }
/* 125 */     painting.setVariant(optional.get());
/* 126 */     painting.setDirection(paramDirection);
/* 127 */     return Optional.of(painting);
/*     */   }
/*     */   
/*     */   private static int variantArea(Holder<PaintingVariant> paramHolder) {
/* 131 */     return ((PaintingVariant)paramHolder.value()).area();
/*     */   }
/*     */   
/*     */   private Painting(Level paramLevel, BlockPos paramBlockPos) {
/* 135 */     super(EntityType.PAINTING, paramLevel, paramBlockPos);
/*     */   }
/*     */   
/*     */   public Painting(Level paramLevel, BlockPos paramBlockPos, Direction paramDirection, Holder<PaintingVariant> paramHolder) {
/* 139 */     this(paramLevel, paramBlockPos);
/* 140 */     setVariant(paramHolder);
/* 141 */     setDirection(paramDirection);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 146 */     paramValueOutput.store("facing", Direction.LEGACY_ID_CODEC_2D, getDirection());
/* 147 */     super.addAdditionalSaveData(paramValueOutput);
/*     */     
/* 149 */     VariantUtils.writeVariant(paramValueOutput, getVariant());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 154 */     Direction direction = paramValueInput.read("facing", Direction.LEGACY_ID_CODEC_2D).orElse(Direction.SOUTH);
/* 155 */     super.readAdditionalSaveData(paramValueInput);
/* 156 */     setDirection(direction);
/*     */     
/* 158 */     VariantUtils.readVariant(paramValueInput, Registries.PAINTING_VARIANT).ifPresent(this::setVariant);
/*     */   }
/*     */ 
/*     */   
/*     */   protected AABB calculateBoundingBox(BlockPos paramBlockPos, Direction paramDirection) {
/* 163 */     float f = 0.46875F;
/* 164 */     Vec3 vec31 = Vec3.atCenterOf((Vec3i)paramBlockPos).relative(paramDirection, -0.46875D);
/*     */     
/* 166 */     PaintingVariant paintingVariant = (PaintingVariant)getVariant().value();
/* 167 */     double d1 = offsetForPaintingSize(paintingVariant.width());
/* 168 */     double d2 = offsetForPaintingSize(paintingVariant.height());
/*     */     
/* 170 */     Direction direction = paramDirection.getCounterClockWise();
/* 171 */     Vec3 vec32 = vec31.relative(direction, d1).relative(Direction.UP, d2);
/*     */     
/* 173 */     Direction.Axis axis = paramDirection.getAxis();
/* 174 */     double d3 = (axis == Direction.Axis.X) ? 0.0625D : paintingVariant.width();
/* 175 */     double d4 = paintingVariant.height();
/* 176 */     double d5 = (axis == Direction.Axis.Z) ? 0.0625D : paintingVariant.width();
/*     */     
/* 178 */     return AABB.ofSize(vec32, d3, d4, d5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double offsetForPaintingSize(int paramInt) {
/* 185 */     return (paramInt % 2 == 0) ? 0.5D : 0.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dropItem(ServerLevel paramServerLevel, Entity paramEntity) {
/* 190 */     if (!((Boolean)paramServerLevel.getGameRules().get(GameRules.ENTITY_DROPS)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/* 194 */     playSound(SoundEvents.PAINTING_BREAK, 1.0F, 1.0F);
/*     */     
/* 196 */     if (paramEntity instanceof Player) { Player player = (Player)paramEntity;
/* 197 */       if (player.hasInfiniteMaterials()) {
/*     */         return;
/*     */       } }
/*     */ 
/*     */     
/* 202 */     spawnAtLocation(paramServerLevel, (ItemLike)Items.PAINTING);
/*     */   }
/*     */ 
/*     */   
/*     */   public void playPlacementSound() {
/* 207 */     playSound(SoundEvents.PAINTING_PLACE, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void snapTo(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2) {
/* 212 */     setPos(paramDouble1, paramDouble2, paramDouble3);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 trackingPosition() {
/* 217 */     return Vec3.atLowerCornerOf((Vec3i)this.pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity paramServerEntity) {
/* 222 */     return (Packet<ClientGamePacketListener>)new ClientboundAddEntityPacket((Entity)this, getDirection().get3DDataValue(), getPos());
/*     */   }
/*     */ 
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket paramClientboundAddEntityPacket) {
/* 227 */     super.recreateFromPacket(paramClientboundAddEntityPacket);
/* 228 */     setDirection(Direction.from3DDataValue(paramClientboundAddEntityPacket.getData()));
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getPickResult() {
/* 233 */     return new ItemStack((ItemLike)Items.PAINTING);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\decoration\painting\Painting.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */