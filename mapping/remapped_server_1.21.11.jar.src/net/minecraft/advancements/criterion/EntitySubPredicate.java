/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface EntitySubPredicate
/*    */ {
/* 15 */   public static final Codec<EntitySubPredicate> CODEC = BuiltInRegistries.ENTITY_SUB_PREDICATE_TYPE.byNameCodec().dispatch(EntitySubPredicate::codec, Function.identity());
/*    */   
/*    */   MapCodec<? extends EntitySubPredicate> codec();
/*    */   
/*    */   boolean matches(Entity paramEntity, ServerLevel paramServerLevel, Vec3 paramVec3);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\criterion\EntitySubPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */