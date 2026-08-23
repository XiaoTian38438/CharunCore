/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class PlayerUUIDFix extends AbstractUUIDFix {
/*    */   public PlayerUUIDFix(Schema paramSchema) {
/* 11 */     super(paramSchema, References.PLAYER);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 16 */     return fixTypeEverywhereTyped("PlayerUUIDFix", getInputSchema().getType(this.typeReference), paramTyped -> {
/*    */           OpticFinder opticFinder = paramTyped.getType().findField("RootVehicle");
/*    */           return paramTyped.updateTyped(opticFinder, opticFinder.type(), ()).update(DSL.remainderFinder(), ());
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\PlayerUUIDFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */