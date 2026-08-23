/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntityItemFrameDirectionFix extends NamedEntityFix {
/*    */   public EntityItemFrameDirectionFix(Schema paramSchema, boolean paramBoolean) {
/* 10 */     super(paramSchema, paramBoolean, "EntityItemFrameDirectionFix", References.ENTITY, "minecraft:item_frame");
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> paramDynamic) {
/* 14 */     return paramDynamic.set("Facing", paramDynamic.createByte(direction2dTo3d(paramDynamic.get("Facing").asByte((byte)0))));
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 19 */     return paramTyped.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */   
/*    */   private static byte direction2dTo3d(byte paramByte) {
/* 23 */     switch (paramByte)
/*    */     
/*    */     { default:
/* 26 */         return 2;
/*    */       case 0:
/* 28 */         return 3;
/*    */       case 1:
/* 30 */         return 4;
/*    */       case 3:
/* 32 */         break; }  return 5;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityItemFrameDirectionFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */