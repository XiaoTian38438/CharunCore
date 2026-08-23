/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class FireResistantToDamageResistantComponentFix extends DataComponentRemainderFix {
/*    */   public FireResistantToDamageResistantComponentFix(Schema paramSchema) {
/*  8 */     super(paramSchema, "FireResistantToDamageResistantComponentFix", "minecraft:fire_resistant", "minecraft:damage_resistant");
/*    */   }
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fixComponent(Dynamic<T> paramDynamic) {
/* 13 */     return paramDynamic.emptyMap().set("types", paramDynamic.createString("#minecraft:is_fire"));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\FireResistantToDamageResistantComponentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */