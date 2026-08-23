/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntityArmorStandSilentFix extends NamedEntityFix {
/*    */   public EntityArmorStandSilentFix(Schema paramSchema, boolean paramBoolean) {
/* 10 */     super(paramSchema, paramBoolean, "EntityArmorStandSilentFix", References.ENTITY, "ArmorStand");
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> paramDynamic) {
/* 14 */     if (paramDynamic.get("Silent").asBoolean(false) && !paramDynamic.get("Marker").asBoolean(false)) {
/* 15 */       return paramDynamic.remove("Silent");
/*    */     }
/* 17 */     return paramDynamic;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 22 */     return paramTyped.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityArmorStandSilentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */