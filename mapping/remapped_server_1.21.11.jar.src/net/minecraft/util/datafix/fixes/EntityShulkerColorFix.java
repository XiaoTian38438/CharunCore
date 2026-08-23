/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntityShulkerColorFix extends NamedEntityFix {
/*    */   public EntityShulkerColorFix(Schema paramSchema, boolean paramBoolean) {
/* 10 */     super(paramSchema, paramBoolean, "EntityShulkerColorFix", References.ENTITY, "minecraft:shulker");
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> paramDynamic) {
/* 14 */     if (paramDynamic.get("Color").map(Dynamic::asNumber).result().isEmpty()) {
/* 15 */       return paramDynamic.set("Color", paramDynamic.createByte((byte)10));
/*    */     }
/* 17 */     return paramDynamic;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 22 */     return paramTyped.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityShulkerColorFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */