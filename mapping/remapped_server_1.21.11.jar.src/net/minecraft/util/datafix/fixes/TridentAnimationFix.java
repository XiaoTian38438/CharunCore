/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class TridentAnimationFix
/*    */   extends DataComponentRemainderFix
/*    */ {
/*    */   public TridentAnimationFix(Schema paramSchema) {
/* 10 */     super(paramSchema, "TridentAnimationFix", "minecraft:consumable");
/*    */   }
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fixComponent(Dynamic<T> paramDynamic) {
/* 15 */     return paramDynamic.update("animation", paramDynamic -> {
/*    */           String str = paramDynamic.asString().result().orElse("");
/*    */           return "spear".equals(str) ? paramDynamic.createString("trident") : paramDynamic;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\TridentAnimationFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */