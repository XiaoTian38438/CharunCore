/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public abstract class DataComponentRemainderFix
/*    */   extends DataFix {
/*    */   private final String name;
/*    */   private final String componentId;
/*    */   private final String newComponentId;
/*    */   
/*    */   public DataComponentRemainderFix(Schema paramSchema, String paramString1, String paramString2) {
/* 19 */     this(paramSchema, paramString1, paramString2, paramString2);
/*    */   }
/*    */   
/*    */   public DataComponentRemainderFix(Schema paramSchema, String paramString1, String paramString2, String paramString3) {
/* 23 */     super(paramSchema, false);
/* 24 */     this.name = paramString1;
/* 25 */     this.componentId = paramString2;
/* 26 */     this.newComponentId = paramString3;
/*    */   }
/*    */ 
/*    */   
/*    */   public final TypeRewriteRule makeRule() {
/* 31 */     Type type = getInputSchema().getType(References.DATA_COMPONENTS);
/* 32 */     return fixTypeEverywhereTyped(this.name, type, paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */   
/*    */   protected abstract <T> Dynamic<T> fixComponent(Dynamic<T> paramDynamic);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\DataComponentRemainderFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */