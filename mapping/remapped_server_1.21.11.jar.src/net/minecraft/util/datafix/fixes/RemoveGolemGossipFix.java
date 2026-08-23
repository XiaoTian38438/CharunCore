/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class RemoveGolemGossipFix extends NamedEntityFix {
/*    */   public RemoveGolemGossipFix(Schema paramSchema, boolean paramBoolean) {
/* 10 */     super(paramSchema, paramBoolean, "Remove Golem Gossip Fix", References.ENTITY, "minecraft:villager");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 15 */     return paramTyped.update(DSL.remainderFinder(), RemoveGolemGossipFix::fixValue);
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixValue(Dynamic<?> paramDynamic) {
/* 19 */     return paramDynamic.update("Gossips", paramDynamic2 -> paramDynamic1.createList(paramDynamic2.asStream().filter(())));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\RemoveGolemGossipFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */