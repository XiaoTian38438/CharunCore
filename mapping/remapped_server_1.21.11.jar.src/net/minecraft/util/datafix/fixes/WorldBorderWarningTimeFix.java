/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class WorldBorderWarningTimeFix extends DataFix {
/*    */   public WorldBorderWarningTimeFix(Schema paramSchema) {
/* 10 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 15 */     return writeFixAndRead("WorldBorderWarningTimeFix", getInputSchema().getType(References.SAVED_DATA_WORLD_BORDER), getOutputSchema().getType(References.SAVED_DATA_WORLD_BORDER), paramDynamic -> paramDynamic.update("data", ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\WorldBorderWarningTimeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */