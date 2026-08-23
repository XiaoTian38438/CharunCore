/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class BlockEntityShulkerBoxColorFix extends NamedEntityFix {
/*    */   public BlockEntityShulkerBoxColorFix(Schema paramSchema, boolean paramBoolean) {
/*  9 */     super(paramSchema, paramBoolean, "BlockEntityShulkerBoxColorFix", References.BLOCK_ENTITY, "minecraft:shulker_box");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 14 */     return paramTyped.update(DSL.remainderFinder(), paramDynamic -> paramDynamic.remove("Color"));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\BlockEntityShulkerBoxColorFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */