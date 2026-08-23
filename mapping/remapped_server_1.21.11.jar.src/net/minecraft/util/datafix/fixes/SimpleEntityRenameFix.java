/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public abstract class SimpleEntityRenameFix extends EntityRenameFix {
/*    */   public SimpleEntityRenameFix(String paramString, Schema paramSchema, boolean paramBoolean) {
/* 11 */     super(paramString, paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Pair<String, Typed<?>> fix(String paramString, Typed<?> paramTyped) {
/* 16 */     Pair<String, Dynamic<?>> pair = getNewNameAndTag(paramString, (Dynamic)paramTyped.getOrCreate(DSL.remainderFinder()));
/* 17 */     return Pair.of(pair.getFirst(), paramTyped.set(DSL.remainderFinder(), pair.getSecond()));
/*    */   }
/*    */   
/*    */   protected abstract Pair<String, Dynamic<?>> getNewNameAndTag(String paramString, Dynamic<?> paramDynamic);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\SimpleEntityRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */