/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ 
/*    */ public class OminousBannerBlockEntityRenameFix extends NamedEntityFix {
/*    */   public OminousBannerBlockEntityRenameFix(Schema paramSchema, boolean paramBoolean) {
/* 11 */     super(paramSchema, paramBoolean, "OminousBannerBlockEntityRenameFix", References.BLOCK_ENTITY, "minecraft:banner");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 16 */     OpticFinder opticFinder1 = paramTyped.getType().findField("CustomName");
/*    */     
/* 18 */     OpticFinder opticFinder2 = DSL.typeFinder(getInputSchema().getType(References.TEXT_COMPONENT));
/* 19 */     return paramTyped.updateTyped(opticFinder1, paramTyped -> paramTyped.update(paramOpticFinder, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\OminousBannerBlockEntityRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */