/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VillagerFollowRangeFix
/*    */   extends NamedEntityFix
/*    */ {
/*    */   private static final double ORIGINAL_VALUE = 16.0D;
/*    */   private static final double NEW_BASE_VALUE = 48.0D;
/*    */   
/*    */   public VillagerFollowRangeFix(Schema paramSchema) {
/* 17 */     super(paramSchema, false, "Villager Follow Range Fix", References.ENTITY, "minecraft:villager");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 22 */     return paramTyped.update(DSL.remainderFinder(), VillagerFollowRangeFix::fixValue);
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixValue(Dynamic<?> paramDynamic) {
/* 26 */     return paramDynamic.update("Attributes", paramDynamic2 -> paramDynamic1.createList(paramDynamic2.asStream().map(())));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\VillagerFollowRangeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */