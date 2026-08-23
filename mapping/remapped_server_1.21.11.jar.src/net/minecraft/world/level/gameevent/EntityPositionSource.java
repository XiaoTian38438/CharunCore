/*    */ package net.minecraft.world.level.gameevent;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class EntityPositionSource implements PositionSource {
/*    */   static {
/* 21 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)UUIDUtil.CODEC.fieldOf("source_entity").forGetter(EntityPositionSource::getUuid), (App)Codec.FLOAT.fieldOf("y_offset").orElse(Float.valueOf(0.0F)).forGetter(())).apply((Applicative)paramInstance, ()));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 26 */     STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, EntityPositionSource::getId, ByteBufCodecs.FLOAT, paramEntityPositionSource -> Float.valueOf(paramEntityPositionSource.yOffset), (paramInteger, paramFloat) -> new EntityPositionSource(Either.right(Either.right(paramInteger)), paramFloat.floatValue()));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<EntityPositionSource> CODEC;
/*    */   public static final StreamCodec<ByteBuf, EntityPositionSource> STREAM_CODEC;
/*    */   private Either<Entity, Either<UUID, Integer>> entityOrUuidOrId;
/*    */   private final float yOffset;
/*    */   
/*    */   public EntityPositionSource(Entity paramEntity, float paramFloat) {
/* 36 */     this(Either.left(paramEntity), paramFloat);
/*    */   }
/*    */   
/*    */   private EntityPositionSource(Either<Entity, Either<UUID, Integer>> paramEither, float paramFloat) {
/* 40 */     this.entityOrUuidOrId = paramEither;
/* 41 */     this.yOffset = paramFloat;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Vec3> getPosition(Level paramLevel) {
/* 46 */     if (this.entityOrUuidOrId.left().isEmpty()) {
/* 47 */       resolveEntity(paramLevel);
/*    */     }
/* 49 */     return this.entityOrUuidOrId.left().map(paramEntity -> paramEntity.position().add(0.0D, this.yOffset, 0.0D));
/*    */   }
/*    */   
/*    */   private void resolveEntity(Level paramLevel) {
/* 53 */     ((Optional)this.entityOrUuidOrId.map(Optional::of, paramEither -> {
/*    */           Objects.requireNonNull(paramLevel);
/*    */ 
/*    */ 
/*    */           
/*    */           return Optional.ofNullable((Entity)paramEither.map((), paramLevel::getEntity));
/* 59 */         })).ifPresent(paramEntity -> this.entityOrUuidOrId = Either.left(paramEntity));
/*    */   }
/*    */   
/*    */   public UUID getUuid() {
/* 63 */     return (UUID)this.entityOrUuidOrId.map(Entity::getUUID, paramEither -> (UUID)paramEither.map(Function.identity(), ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private int getId() {
/* 75 */     return ((Integer)this.entityOrUuidOrId.map(Entity::getId, paramEither -> (Integer)paramEither.map((), Function.identity()))).intValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PositionSourceType<EntityPositionSource> getType() {
/* 88 */     return PositionSourceType.ENTITY;
/*    */   }
/*    */   
/*    */   public static class Type
/*    */     implements PositionSourceType<EntityPositionSource> {
/*    */     public MapCodec<EntityPositionSource> codec() {
/* 94 */       return EntityPositionSource.CODEC;
/*    */     }
/*    */ 
/*    */     
/*    */     public StreamCodec<ByteBuf, EntityPositionSource> streamCodec() {
/* 99 */       return EntityPositionSource.STREAM_CODEC;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\gameevent\EntityPositionSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */