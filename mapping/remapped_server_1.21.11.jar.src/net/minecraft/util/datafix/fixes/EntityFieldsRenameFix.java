/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class EntityFieldsRenameFix
/*    */   extends NamedEntityFix {
/*    */   private final Map<String, String> renames;
/*    */   
/*    */   public EntityFieldsRenameFix(Schema paramSchema, String paramString1, String paramString2, Map<String, String> paramMap) {
/* 14 */     super(paramSchema, false, paramString1, References.ENTITY, paramString2);
/* 15 */     this.renames = paramMap;
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> paramDynamic) {
/* 19 */     for (Map.Entry<String, String> entry : this.renames.entrySet()) {
/* 20 */       paramDynamic = paramDynamic.renameField((String)entry.getKey(), (String)entry.getValue());
/*    */     }
/* 22 */     return paramDynamic;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 27 */     return paramTyped.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityFieldsRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */