/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class BlockEntityFurnaceBurnTimeFix
/*    */   extends NamedEntityFix {
/*    */   public BlockEntityFurnaceBurnTimeFix(Schema paramSchema, String paramString) {
/* 11 */     super(paramSchema, false, "BlockEntityFurnaceBurnTimeFix" + paramString, References.BLOCK_ENTITY, paramString);
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixBurnTime(Dynamic<?> paramDynamic) {
/* 15 */     paramDynamic = paramDynamic.renameField("CookTime", "cooking_time_spent");
/* 16 */     paramDynamic = paramDynamic.renameField("CookTimeTotal", "cooking_total_time");
/* 17 */     paramDynamic = paramDynamic.renameField("BurnTime", "lit_time_remaining");
/*    */     
/* 19 */     paramDynamic = paramDynamic.setFieldIfPresent("lit_total_time", paramDynamic.get("lit_time_remaining").result());
/* 20 */     return paramDynamic;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 25 */     return paramTyped.update(DSL.remainderFinder(), this::fixBurnTime);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\BlockEntityFurnaceBurnTimeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */