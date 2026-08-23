/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class BlockEntityKeepPacked extends NamedEntityFix {
/*    */   public BlockEntityKeepPacked(Schema paramSchema, boolean paramBoolean) {
/* 10 */     super(paramSchema, paramBoolean, "BlockEntityKeepPacked", References.BLOCK_ENTITY, "DUMMY");
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixTag(Dynamic<?> paramDynamic) {
/* 14 */     return paramDynamic.set("keepPacked", paramDynamic.createBoolean(true));
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 19 */     return paramTyped.update(DSL.remainderFinder(), BlockEntityKeepPacked::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\BlockEntityKeepPacked.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */