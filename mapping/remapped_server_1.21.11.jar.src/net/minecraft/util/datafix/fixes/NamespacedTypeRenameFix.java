/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class NamespacedTypeRenameFix
/*    */   extends DataFix {
/*    */   private final String name;
/*    */   private final DSL.TypeReference type;
/*    */   private final UnaryOperator<String> renamer;
/*    */   
/*    */   public NamespacedTypeRenameFix(Schema paramSchema, String paramString, DSL.TypeReference paramTypeReference, UnaryOperator<String> paramUnaryOperator) {
/* 22 */     super(paramSchema, false);
/* 23 */     this.name = paramString;
/* 24 */     this.type = paramTypeReference;
/* 25 */     this.renamer = paramUnaryOperator;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 30 */     Type type = DSL.named(this.type.typeName(), NamespacedSchema.namespacedString());
/* 31 */     if (!Objects.equals(type, getInputSchema().getType(this.type))) {
/* 32 */       throw new IllegalStateException("\"" + this.type.typeName() + "\" is not what was expected.");
/*    */     }
/* 34 */     return fixTypeEverywhere(this.name, type, paramDynamicOps -> ());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\NamespacedTypeRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */