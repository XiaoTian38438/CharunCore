/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class AttributeIdPrefixFix
/*    */   extends AttributesRenameFix {
/*  9 */   private static final List<String> PREFIXES = List.of("generic.", "horse.", "player.", "zombie.");
/*    */   
/*    */   public AttributeIdPrefixFix(Schema paramSchema) {
/* 12 */     super(paramSchema, "AttributeIdPrefixFix", AttributeIdPrefixFix::replaceId);
/*    */   }
/*    */   
/*    */   private static String replaceId(String paramString) {
/* 16 */     String str = NamespacedSchema.ensureNamespaced(paramString);
/* 17 */     for (String str1 : PREFIXES) {
/* 18 */       String str2 = NamespacedSchema.ensureNamespaced(str1);
/* 19 */       if (str.startsWith(str2)) {
/* 20 */         return "minecraft:" + str.substring(str2.length());
/*    */       }
/*    */     } 
/* 23 */     return paramString;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\AttributeIdPrefixFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */