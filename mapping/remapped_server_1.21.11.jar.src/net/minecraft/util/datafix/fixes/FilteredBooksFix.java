/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class FilteredBooksFix extends ItemStackTagFix {
/*    */   public FilteredBooksFix(Schema paramSchema) {
/*  9 */     super(paramSchema, "Remove filtered text from books", paramString -> (paramString.equals("minecraft:writable_book") || paramString.equals("minecraft:written_book")));
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fixItemStackTag(Typed<?> paramTyped) {
/* 14 */     return Util.writeAndReadTypedOrThrow(paramTyped, paramTyped.getType(), paramDynamic -> paramDynamic.remove("filtered_title").remove("filtered_pages"));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\FilteredBooksFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */