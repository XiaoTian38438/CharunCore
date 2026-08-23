/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class EntityHorseSplitFix
/*    */   extends EntityRenameFix {
/*    */   public EntityHorseSplitFix(Schema paramSchema, boolean paramBoolean) {
/* 15 */     super("EntityHorseSplitFix", paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Pair<String, Typed<?>> fix(String paramString, Typed<?> paramTyped) {
/* 20 */     if (Objects.equals("EntityHorse", paramString)) {
/* 21 */       Dynamic dynamic = (Dynamic)paramTyped.get(DSL.remainderFinder());
/* 22 */       int i = dynamic.get("Type").asInt(0);
/* 23 */       switch (i) { default: 
/*    */         case 1: 
/*    */         case 2: 
/*    */         case 3: 
/*    */         case 4:
/* 28 */           break; }  String str = "SkeletonHorse";
/*    */ 
/*    */       
/* 31 */       Type type = (Type)getOutputSchema().findChoiceType(References.ENTITY).types().get(str);
/* 32 */       return Pair.of(str, Util.writeAndReadTypedOrThrow(paramTyped, type, paramDynamic -> paramDynamic.remove("Type")));
/*    */     } 
/* 34 */     return Pair.of(paramString, paramTyped);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityHorseSplitFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */