/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class RemoveEmptyItemInBrushableBlockFix
/*    */   extends NamedEntityWriteReadFix {
/*    */   public RemoveEmptyItemInBrushableBlockFix(Schema paramSchema) {
/* 11 */     super(paramSchema, false, "RemoveEmptyItemInSuspiciousBlockFix", References.BLOCK_ENTITY, "minecraft:brushable_block");
/*    */   }
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fix(Dynamic<T> paramDynamic) {
/* 16 */     Optional<Dynamic> optional = paramDynamic.get("item").result();
/* 17 */     if (optional.isPresent() && isEmptyStack(optional.get())) {
/* 18 */       return paramDynamic.remove("item");
/*    */     }
/* 20 */     return paramDynamic;
/*    */   }
/*    */   
/*    */   private static boolean isEmptyStack(Dynamic<?> paramDynamic) {
/* 24 */     String str = NamespacedSchema.ensureNamespaced(paramDynamic.get("id").asString("minecraft:air"));
/* 25 */     int i = paramDynamic.get("count").asInt(0);
/* 26 */     return (str.equals("minecraft:air") || i == 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\RemoveEmptyItemInBrushableBlockFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */