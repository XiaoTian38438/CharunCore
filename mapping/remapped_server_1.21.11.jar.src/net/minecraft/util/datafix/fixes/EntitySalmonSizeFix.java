/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntitySalmonSizeFix extends NamedEntityFix {
/*    */   public EntitySalmonSizeFix(Schema paramSchema) {
/* 10 */     super(paramSchema, false, "EntitySalmonSizeFix", References.ENTITY, "minecraft:salmon");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 15 */     return paramTyped.update(DSL.remainderFinder(), paramDynamic -> {
/*    */           String str = paramDynamic.get("type").asString("medium");
/*    */           return str.equals("large") ? paramDynamic : paramDynamic.set("type", paramDynamic.createString("medium"));
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntitySalmonSizeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */