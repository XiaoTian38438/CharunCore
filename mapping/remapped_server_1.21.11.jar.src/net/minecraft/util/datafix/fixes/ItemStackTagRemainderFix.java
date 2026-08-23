/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.function.Predicate;
/*    */ 
/*    */ public abstract class ItemStackTagRemainderFix
/*    */   extends ItemStackTagFix {
/*    */   public ItemStackTagRemainderFix(Schema paramSchema, String paramString, Predicate<String> paramPredicate) {
/* 12 */     super(paramSchema, paramString, paramPredicate);
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract <T> Dynamic<T> fixItemStackTag(Dynamic<T> paramDynamic);
/*    */   
/*    */   protected final Typed<?> fixItemStackTag(Typed<?> paramTyped) {
/* 19 */     return paramTyped.update(DSL.remainderFinder(), this::fixItemStackTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ItemStackTagRemainderFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */