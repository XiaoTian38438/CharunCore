/*    */ package net.minecraft.world.entity.variant;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public interface SpawnCondition
/*    */   extends PriorityProvider.SelectorCondition<SpawnContext> {
/*    */   public static final Codec<SpawnCondition> CODEC;
/*    */   
/*    */   static {
/* 12 */     CODEC = BuiltInRegistries.SPAWN_CONDITION_TYPE.byNameCodec().dispatch(SpawnCondition::codec, paramMapCodec -> paramMapCodec);
/*    */   }
/*    */   
/*    */   MapCodec<? extends SpawnCondition> codec();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\variant\SpawnCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */