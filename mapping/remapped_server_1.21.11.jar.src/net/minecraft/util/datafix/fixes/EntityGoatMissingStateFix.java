/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntityGoatMissingStateFix extends NamedEntityFix {
/*    */   public EntityGoatMissingStateFix(Schema paramSchema) {
/* 10 */     super(paramSchema, false, "EntityGoatMissingStateFix", References.ENTITY, "minecraft:goat");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 15 */     return paramTyped.update(DSL.remainderFinder(), paramDynamic -> paramDynamic.set("HasLeftHorn", paramDynamic.createBoolean(true)).set("HasRightHorn", paramDynamic.createBoolean(true)));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityGoatMissingStateFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */