/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntityPaintingItemFrameDirectionFix extends DataFix {
/* 12 */   private static final int[][] DIRECTIONS = new int[][] { { 0, 0, 1 }, { -1, 0, 0 }, { 0, 0, -1 }, { 1, 0, 0 } };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public EntityPaintingItemFrameDirectionFix(Schema paramSchema, boolean paramBoolean) {
/* 20 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */   
/*    */   private Dynamic<?> doFix(Dynamic<?> paramDynamic, boolean paramBoolean1, boolean paramBoolean2) {
/* 24 */     if ((paramBoolean1 || paramBoolean2) && paramDynamic.get("Facing").asNumber().result().isEmpty()) {
/*    */       int i;
/* 26 */       if (paramDynamic.get("Direction").asNumber().result().isPresent()) {
/* 27 */         i = paramDynamic.get("Direction").asByte((byte)0) % DIRECTIONS.length;
/* 28 */         int[] arrayOfInt = DIRECTIONS[i];
/*    */         
/* 30 */         paramDynamic = paramDynamic.set("TileX", paramDynamic.createInt(paramDynamic.get("TileX").asInt(0) + arrayOfInt[0]));
/* 31 */         paramDynamic = paramDynamic.set("TileY", paramDynamic.createInt(paramDynamic.get("TileY").asInt(0) + arrayOfInt[1]));
/* 32 */         paramDynamic = paramDynamic.set("TileZ", paramDynamic.createInt(paramDynamic.get("TileZ").asInt(0) + arrayOfInt[2]));
/*    */         
/* 34 */         paramDynamic = paramDynamic.remove("Direction");
/*    */         
/* 36 */         if (paramBoolean2 && paramDynamic.get("ItemRotation").asNumber().result().isPresent()) {
/* 37 */           paramDynamic = paramDynamic.set("ItemRotation", paramDynamic.createByte((byte)(paramDynamic.get("ItemRotation").asByte((byte)0) * 2)));
/*    */         }
/*    */       } else {
/* 40 */         i = paramDynamic.get("Dir").asByte((byte)0) % DIRECTIONS.length;
/* 41 */         paramDynamic = paramDynamic.remove("Dir");
/*    */       } 
/* 43 */       paramDynamic = paramDynamic.set("Facing", paramDynamic.createByte((byte)i));
/*    */     } 
/*    */     
/* 46 */     return paramDynamic;
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 51 */     Type type1 = getInputSchema().getChoiceType(References.ENTITY, "Painting");
/* 52 */     OpticFinder opticFinder1 = DSL.namedChoice("Painting", type1);
/*    */     
/* 54 */     Type type2 = getInputSchema().getChoiceType(References.ENTITY, "ItemFrame");
/* 55 */     OpticFinder opticFinder2 = DSL.namedChoice("ItemFrame", type2);
/*    */     
/* 57 */     Type type3 = getInputSchema().getType(References.ENTITY);
/*    */     
/* 59 */     TypeRewriteRule typeRewriteRule1 = fixTypeEverywhereTyped("EntityPaintingFix", type3, paramTyped -> paramTyped.updateTyped(paramOpticFinder, paramType, ()));
/*    */ 
/*    */     
/* 62 */     TypeRewriteRule typeRewriteRule2 = fixTypeEverywhereTyped("EntityItemFrameFix", type3, paramTyped -> paramTyped.updateTyped(paramOpticFinder, paramType, ()));
/*    */ 
/*    */ 
/*    */     
/* 66 */     return TypeRewriteRule.seq(typeRewriteRule1, typeRewriteRule2);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityPaintingItemFrameDirectionFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */