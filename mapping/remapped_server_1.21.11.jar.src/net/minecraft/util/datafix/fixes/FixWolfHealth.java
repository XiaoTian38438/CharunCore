/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*    */ 
/*    */ public class FixWolfHealth extends NamedEntityFix {
/*    */   private static final String WOLF_ID = "minecraft:wolf";
/*    */   private static final String WOLF_HEALTH = "minecraft:generic.max_health";
/*    */   
/*    */   public FixWolfHealth(Schema paramSchema) {
/* 15 */     super(paramSchema, false, "FixWolfHealth", References.ENTITY, "minecraft:wolf");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 20 */     return paramTyped.update(DSL.remainderFinder(), paramDynamic -> {
/*    */           MutableBoolean mutableBoolean = new MutableBoolean(false);
/*    */           paramDynamic = paramDynamic.update("Attributes", ());
/*    */           if (mutableBoolean.isTrue())
/*    */             paramDynamic = paramDynamic.update("Health", ()); 
/*    */           return paramDynamic;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\FixWolfHealth.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */