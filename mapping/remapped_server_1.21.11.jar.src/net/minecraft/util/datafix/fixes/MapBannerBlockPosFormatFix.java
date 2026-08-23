/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.List;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ public class MapBannerBlockPosFormatFix extends DataFix {
/*    */   public MapBannerBlockPosFormatFix(Schema paramSchema) {
/* 14 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 19 */     Type type = getInputSchema().getType(References.SAVED_DATA_MAP_DATA);
/* 20 */     OpticFinder opticFinder1 = type.findField("data");
/* 21 */     OpticFinder opticFinder2 = opticFinder1.type().findField("banners");
/* 22 */     OpticFinder opticFinder3 = DSL.typeFinder(((List.ListType)opticFinder2.type()).getElement());
/* 23 */     return fixTypeEverywhereTyped("MapBannerBlockPosFormatFix", type, paramTyped -> paramTyped.updateTyped(paramOpticFinder1, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\MapBannerBlockPosFormatFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */