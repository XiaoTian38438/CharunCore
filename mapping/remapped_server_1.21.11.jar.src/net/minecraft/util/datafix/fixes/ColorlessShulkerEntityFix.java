/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class ColorlessShulkerEntityFix extends NamedEntityFix {
/*    */   public ColorlessShulkerEntityFix(Schema paramSchema, boolean paramBoolean) {
/*  9 */     super(paramSchema, paramBoolean, "Colorless shulker entity fix", References.ENTITY, "minecraft:shulker");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 14 */     return paramTyped.update(DSL.remainderFinder(), paramDynamic -> (paramDynamic.get("Color").asInt(0) == 10) ? paramDynamic.set("Color", paramDynamic.createByte((byte)16)) : paramDynamic);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ColorlessShulkerEntityFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */