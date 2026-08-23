/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class CarvingStepRemoveFix
/*    */   extends DataFix {
/*    */   public CarvingStepRemoveFix(Schema paramSchema) {
/* 14 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 19 */     return fixTypeEverywhereTyped("CarvingStepRemoveFix", getInputSchema().getType(References.CHUNK), CarvingStepRemoveFix::fixChunk);
/*    */   }
/*    */   
/*    */   private static Typed<?> fixChunk(Typed<?> paramTyped) {
/* 23 */     return paramTyped.update(DSL.remainderFinder(), paramDynamic -> {
/*    */           Dynamic dynamic = paramDynamic;
/*    */           Optional<Dynamic> optional = dynamic.get("CarvingMasks").result();
/*    */           if (optional.isPresent()) {
/*    */             Optional<Dynamic> optional1 = ((Dynamic)optional.get()).get("AIR").result();
/*    */             if (optional1.isPresent())
/*    */               dynamic = dynamic.set("carving_mask", optional1.get()); 
/*    */           } 
/*    */           return dynamic.remove("CarvingMasks");
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\CarvingStepRemoveFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */