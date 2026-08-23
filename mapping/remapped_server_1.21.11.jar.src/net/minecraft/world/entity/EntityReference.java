/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.server.players.OldUsersConverter;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.entity.UUIDLookup;
/*     */ import net.minecraft.world.level.entity.UniquelyIdentifyable;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public final class EntityReference<StoredEntityType extends UniquelyIdentifyable> {
/*  21 */   private static final Codec<? extends EntityReference<?>> CODEC = UUIDUtil.CODEC.xmap(EntityReference::new, EntityReference::getUUID);
/*  22 */   private static final StreamCodec<ByteBuf, ? extends EntityReference<?>> STREAM_CODEC = UUIDUtil.STREAM_CODEC.map(EntityReference::new, EntityReference::getUUID);
/*     */   private Either<UUID, StoredEntityType> entity;
/*     */   
/*     */   public static <Type extends UniquelyIdentifyable> Codec<EntityReference<Type>> codec() {
/*  26 */     return (Codec)CODEC;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <Type extends UniquelyIdentifyable> StreamCodec<ByteBuf, EntityReference<Type>> streamCodec() {
/*  31 */     return (StreamCodec)STREAM_CODEC;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private EntityReference(StoredEntityType paramStoredEntityType) {
/*  37 */     this.entity = Either.right(paramStoredEntityType);
/*     */   }
/*     */   
/*     */   private EntityReference(UUID paramUUID) {
/*  41 */     this.entity = Either.left(paramUUID);
/*     */   }
/*     */   
/*     */   public static <T extends UniquelyIdentifyable> EntityReference<T> of(T paramT) {
/*  45 */     return (paramT != null) ? new EntityReference<>(paramT) : null;
/*     */   }
/*     */   
/*     */   public static <T extends UniquelyIdentifyable> EntityReference<T> of(UUID paramUUID) {
/*  49 */     return new EntityReference<>(paramUUID);
/*     */   }
/*     */   
/*     */   public UUID getUUID() {
/*  53 */     return (UUID)this.entity.map(paramUUID -> paramUUID, UniquelyIdentifyable::getUUID);
/*     */   }
/*     */   
/*     */   public StoredEntityType getEntity(UUIDLookup<? extends UniquelyIdentifyable> paramUUIDLookup, Class<StoredEntityType> paramClass) {
/*  57 */     Optional<UniquelyIdentifyable> optional = this.entity.right();
/*  58 */     if (optional.isPresent()) {
/*  59 */       UniquelyIdentifyable uniquelyIdentifyable = optional.get();
/*  60 */       if (uniquelyIdentifyable.isRemoved()) {
/*     */         
/*  62 */         this.entity = Either.left(uniquelyIdentifyable.getUUID());
/*     */       } else {
/*  64 */         return (StoredEntityType)uniquelyIdentifyable;
/*     */       } 
/*     */     } 
/*     */     
/*  68 */     Optional<UUID> optional1 = this.entity.left();
/*  69 */     if (optional1.isPresent()) {
/*  70 */       StoredEntityType storedEntityType = resolve(paramUUIDLookup.lookup(optional1.get()), paramClass);
/*  71 */       if (storedEntityType != null && !storedEntityType.isRemoved()) {
/*  72 */         this.entity = Either.right(storedEntityType);
/*  73 */         return storedEntityType;
/*     */       } 
/*     */     } 
/*  76 */     return null;
/*     */   }
/*     */   
/*     */   public StoredEntityType getEntity(Level paramLevel, Class<StoredEntityType> paramClass) {
/*  80 */     if (Player.class.isAssignableFrom(paramClass)) {
/*  81 */       Objects.requireNonNull(paramLevel); return getEntity(paramLevel::getPlayerInAnyDimension, paramClass);
/*     */     } 
/*  83 */     Objects.requireNonNull(paramLevel); return getEntity(paramLevel::getEntityInAnyDimension, paramClass);
/*     */   }
/*     */   
/*     */   private StoredEntityType resolve(UniquelyIdentifyable paramUniquelyIdentifyable, Class<StoredEntityType> paramClass) {
/*  87 */     if (paramUniquelyIdentifyable != null && paramClass.isAssignableFrom(paramUniquelyIdentifyable.getClass())) {
/*  88 */       return paramClass.cast(paramUniquelyIdentifyable);
/*     */     }
/*  90 */     return null;
/*     */   }
/*     */   
/*     */   public boolean matches(StoredEntityType paramStoredEntityType) {
/*  94 */     return getUUID().equals(paramStoredEntityType.getUUID());
/*     */   }
/*     */   
/*     */   public void store(ValueOutput paramValueOutput, String paramString) {
/*  98 */     paramValueOutput.store(paramString, UUIDUtil.CODEC, getUUID());
/*     */   }
/*     */   
/*     */   public static void store(EntityReference<?> paramEntityReference, ValueOutput paramValueOutput, String paramString) {
/* 102 */     if (paramEntityReference != null) {
/* 103 */       paramEntityReference.store(paramValueOutput, paramString);
/*     */     }
/*     */   }
/*     */   
/*     */   public static <StoredEntityType extends UniquelyIdentifyable> StoredEntityType get(EntityReference<StoredEntityType> paramEntityReference, Level paramLevel, Class<StoredEntityType> paramClass) {
/* 108 */     return (paramEntityReference != null) ? paramEntityReference.getEntity(paramLevel, paramClass) : null;
/*     */   }
/*     */   
/*     */   public static Entity getEntity(EntityReference<Entity> paramEntityReference, Level paramLevel) {
/* 112 */     return get(paramEntityReference, paramLevel, Entity.class);
/*     */   }
/*     */   
/*     */   public static LivingEntity getLivingEntity(EntityReference<LivingEntity> paramEntityReference, Level paramLevel) {
/* 116 */     return get(paramEntityReference, paramLevel, LivingEntity.class);
/*     */   }
/*     */   
/*     */   public static Player getPlayer(EntityReference<Player> paramEntityReference, Level paramLevel) {
/* 120 */     return get(paramEntityReference, paramLevel, Player.class);
/*     */   }
/*     */   
/*     */   public static <StoredEntityType extends UniquelyIdentifyable> EntityReference<StoredEntityType> read(ValueInput paramValueInput, String paramString) {
/* 124 */     return paramValueInput.read(paramString, codec()).orElse(null);
/*     */   }
/*     */   
/*     */   public static <StoredEntityType extends UniquelyIdentifyable> EntityReference<StoredEntityType> readWithOldOwnerConversion(ValueInput paramValueInput, String paramString, Level paramLevel) {
/* 128 */     Optional<UUID> optional = paramValueInput.read(paramString, UUIDUtil.CODEC);
/* 129 */     if (optional.isPresent()) {
/* 130 */       return of(optional.get());
/*     */     }
/* 132 */     return paramValueInput.getString(paramString)
/* 133 */       .map(paramString -> OldUsersConverter.convertMobOwnerIfNecessary(paramLevel.getServer(), paramString))
/* 134 */       .map(EntityReference::new)
/* 135 */       .orElse(null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 141 */     if (paramObject == this) {
/* 142 */       return true;
/*     */     }
/* 144 */     if (paramObject instanceof EntityReference) { EntityReference entityReference = (EntityReference)paramObject; if (getUUID().equals(entityReference.getUUID())); }  return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 149 */     return getUUID().hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\EntityReference.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */