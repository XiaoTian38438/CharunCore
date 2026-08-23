/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class CopperGolemWeatherStateFix extends NamedEntityFix {
/*    */   public CopperGolemWeatherStateFix(Schema paramSchema) {
/* 10 */     super(paramSchema, false, "CopperGolemWeatherStateFix", References.ENTITY, "minecraft:copper_golem");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 15 */     return paramTyped.update(DSL.remainderFinder(), paramDynamic -> paramDynamic.update("weather_state", CopperGolemWeatherStateFix::fixWeatherState));
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixWeatherState(Dynamic<?> paramDynamic) {
/* 19 */     switch (paramDynamic.asInt(0)) { case 1: case 2: case 3:  }  return 
/*    */ 
/*    */ 
/*    */       
/* 23 */       paramDynamic.createString("unaffected");
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\CopperGolemWeatherStateFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */