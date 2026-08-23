/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class ContainerBlockEntityLockPredicateFix extends DataFix {
/*    */   public ContainerBlockEntityLockPredicateFix(Schema paramSchema) {
/* 11 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 16 */     return fixTypeEverywhereTyped("ContainerBlockEntityLockPredicateFix", (Type)getInputSchema().findChoiceType(References.BLOCK_ENTITY), ContainerBlockEntityLockPredicateFix::fixBlockEntity);
/*    */   }
/*    */ 
/*    */   
/*    */   private static Typed<?> fixBlockEntity(Typed<?> paramTyped) {
/* 21 */     return paramTyped.update(DSL.remainderFinder(), paramDynamic -> paramDynamic.renameAndFixField("Lock", "lock", LockComponentPredicateFix::fixLock));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ContainerBlockEntityLockPredicateFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */