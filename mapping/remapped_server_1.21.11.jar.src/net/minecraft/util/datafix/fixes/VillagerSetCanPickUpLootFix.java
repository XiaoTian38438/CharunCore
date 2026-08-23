/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VillagerSetCanPickUpLootFix
/*    */   extends NamedEntityFix
/*    */ {
/*    */   private static final String CAN_PICK_UP_LOOT = "CanPickUpLoot";
/*    */   
/*    */   public VillagerSetCanPickUpLootFix(Schema paramSchema) {
/* 16 */     super(paramSchema, true, "Villager CanPickUpLoot default value", References.ENTITY, "Villager");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 21 */     return paramTyped.update(DSL.remainderFinder(), VillagerSetCanPickUpLootFix::fixValue);
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixValue(Dynamic<?> paramDynamic) {
/* 25 */     return paramDynamic.set("CanPickUpLoot", paramDynamic.createBoolean(true));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\VillagerSetCanPickUpLootFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */