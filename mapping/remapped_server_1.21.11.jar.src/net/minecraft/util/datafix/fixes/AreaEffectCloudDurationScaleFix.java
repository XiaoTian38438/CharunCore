/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class AreaEffectCloudDurationScaleFix extends NamedEntityFix {
/*    */   public AreaEffectCloudDurationScaleFix(Schema paramSchema) {
/*  9 */     super(paramSchema, false, "AreaEffectCloudDurationScaleFix", References.ENTITY, "minecraft:area_effect_cloud");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 14 */     return paramTyped.update(DSL.remainderFinder(), paramDynamic -> paramDynamic.set("potion_duration_scale", paramDynamic.createFloat(0.25F)));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\AreaEffectCloudDurationScaleFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */