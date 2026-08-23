/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class OptionsRenameFieldFix extends DataFix {
/*    */   private final String fixName;
/*    */   
/*    */   public OptionsRenameFieldFix(Schema paramSchema, boolean paramBoolean, String paramString1, String paramString2, String paramString3) {
/* 14 */     super(paramSchema, paramBoolean);
/* 15 */     this.fixName = paramString1;
/* 16 */     this.fieldFrom = paramString2;
/* 17 */     this.fieldTo = paramString3;
/*    */   }
/*    */   private final String fieldFrom; private final String fieldTo;
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 22 */     return fixTypeEverywhereTyped(this.fixName, getInputSchema().getType(References.OPTIONS), paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\OptionsRenameFieldFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */