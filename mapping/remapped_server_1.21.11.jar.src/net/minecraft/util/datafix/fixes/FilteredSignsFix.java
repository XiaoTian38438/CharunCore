/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class FilteredSignsFix extends NamedEntityWriteReadFix {
/*    */   public FilteredSignsFix(Schema paramSchema) {
/*  8 */     super(paramSchema, false, "Remove filtered text from signs", References.BLOCK_ENTITY, "minecraft:sign");
/*    */   }
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fix(Dynamic<T> paramDynamic) {
/* 13 */     return paramDynamic.remove("FilteredText1").remove("FilteredText2").remove("FilteredText3").remove("FilteredText4");
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\FilteredSignsFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */