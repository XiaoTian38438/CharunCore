/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class HeightmapRenamingFix extends DataFix {
/*    */   public HeightmapRenamingFix(Schema paramSchema, boolean paramBoolean) {
/* 15 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 20 */     Type type = getInputSchema().getType(References.CHUNK);
/* 21 */     OpticFinder opticFinder = type.findField("Level");
/* 22 */     return fixTypeEverywhereTyped("HeightmapRenamingFix", type, paramTyped -> paramTyped.updateTyped(paramOpticFinder, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private Dynamic<?> fix(Dynamic<?> paramDynamic) {
/* 28 */     Optional<Dynamic> optional1 = paramDynamic.get("Heightmaps").result();
/* 29 */     if (optional1.isEmpty()) {
/* 30 */       return paramDynamic;
/*    */     }
/*    */     
/* 33 */     Dynamic dynamic = optional1.get();
/*    */     
/* 35 */     Optional<Dynamic> optional2 = dynamic.get("LIQUID").result();
/* 36 */     if (optional2.isPresent()) {
/* 37 */       dynamic = dynamic.remove("LIQUID");
/* 38 */       dynamic = dynamic.set("WORLD_SURFACE_WG", optional2.get());
/*    */     } 
/*    */     
/* 41 */     Optional<Dynamic> optional3 = dynamic.get("SOLID").result();
/* 42 */     if (optional3.isPresent()) {
/* 43 */       dynamic = dynamic.remove("SOLID");
/* 44 */       dynamic = dynamic.set("OCEAN_FLOOR_WG", optional3.get());
/* 45 */       dynamic = dynamic.set("OCEAN_FLOOR", optional3.get());
/*    */     } 
/*    */     
/* 48 */     Optional<Dynamic> optional4 = dynamic.get("LIGHT").result();
/* 49 */     if (optional4.isPresent()) {
/* 50 */       dynamic = dynamic.remove("LIGHT");
/* 51 */       dynamic = dynamic.set("LIGHT_BLOCKING", optional4.get());
/*    */     } 
/*    */     
/* 54 */     Optional<Dynamic> optional5 = dynamic.get("RAIN").result();
/* 55 */     if (optional5.isPresent()) {
/* 56 */       dynamic = dynamic.remove("RAIN");
/* 57 */       dynamic = dynamic.set("MOTION_BLOCKING", optional5.get());
/* 58 */       dynamic = dynamic.set("MOTION_BLOCKING_NO_LEAVES", optional5.get());
/*    */     } 
/*    */     
/* 61 */     return paramDynamic.set("Heightmaps", dynamic);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\HeightmapRenamingFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */