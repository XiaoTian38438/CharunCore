/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class LodestoneCompassComponentFix
/*    */   extends DataComponentRemainderFix {
/*    */   public LodestoneCompassComponentFix(Schema paramSchema) {
/* 10 */     super(paramSchema, "LodestoneCompassComponentFix", "minecraft:lodestone_target", "minecraft:lodestone_tracker");
/*    */   }
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fixComponent(Dynamic<T> paramDynamic) {
/* 15 */     Optional<Dynamic> optional1 = paramDynamic.get("pos").result();
/* 16 */     Optional<Dynamic> optional2 = paramDynamic.get("dimension").result();
/* 17 */     paramDynamic = paramDynamic.remove("pos").remove("dimension");
/* 18 */     if (optional1.isPresent() && optional2.isPresent()) {
/* 19 */       paramDynamic = paramDynamic.set("target", paramDynamic.emptyMap()
/* 20 */           .set("pos", optional1.get())
/* 21 */           .set("dimension", optional2.get()));
/*    */     }
/*    */     
/* 24 */     return paramDynamic;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\LodestoneCompassComponentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */