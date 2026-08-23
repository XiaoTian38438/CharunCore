/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class PrimedTntBlockStateFixer
/*    */   extends NamedEntityWriteReadFix
/*    */ {
/*    */   public PrimedTntBlockStateFixer(Schema paramSchema) {
/* 12 */     super(paramSchema, true, "PrimedTnt BlockState fixer", References.ENTITY, "minecraft:tnt");
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> renameFuse(Dynamic<T> paramDynamic) {
/* 16 */     Optional<Dynamic> optional = paramDynamic.get("Fuse").get().result();
/* 17 */     if (optional.isPresent()) {
/* 18 */       return paramDynamic.set("fuse", optional.get());
/*    */     }
/* 20 */     return paramDynamic;
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> insertBlockState(Dynamic<T> paramDynamic) {
/* 24 */     return paramDynamic.set("block_state", paramDynamic.createMap(Map.of(paramDynamic
/* 25 */             .createString("Name"), paramDynamic.createString("minecraft:tnt"))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fix(Dynamic<T> paramDynamic) {
/* 31 */     return renameFuse(insertBlockState(paramDynamic));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\PrimedTntBlockStateFixer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */