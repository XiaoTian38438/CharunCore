/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class AdvancementsRenameFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public AdvancementsRenameFix(Schema paramSchema, boolean paramBoolean, String paramString, Function<String, String> paramFunction) {
/* 15 */     super(paramSchema, paramBoolean);
/* 16 */     this.name = paramString;
/* 17 */     this.renamer = paramFunction;
/*    */   }
/*    */   private final Function<String, String> renamer;
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 22 */     return fixTypeEverywhereTyped(this.name, getInputSchema().getType(References.ADVANCEMENTS), paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\AdvancementsRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */