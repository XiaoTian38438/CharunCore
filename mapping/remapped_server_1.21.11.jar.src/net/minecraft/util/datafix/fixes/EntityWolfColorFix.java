/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntityWolfColorFix extends NamedEntityFix {
/*    */   public EntityWolfColorFix(Schema paramSchema, boolean paramBoolean) {
/* 10 */     super(paramSchema, paramBoolean, "EntityWolfColorFix", References.ENTITY, "minecraft:wolf");
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> paramDynamic) {
/* 14 */     return paramDynamic.update("CollarColor", paramDynamic -> paramDynamic.createByte((byte)(15 - paramDynamic.asInt(0))));
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 19 */     return paramTyped.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityWolfColorFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */