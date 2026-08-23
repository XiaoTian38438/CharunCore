/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Arrays;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.IntStream;
/*    */ 
/*    */ public class ChunkBiomeFix extends DataFix {
/*    */   public ChunkBiomeFix(Schema paramSchema, boolean paramBoolean) {
/* 16 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 21 */     Type type = getInputSchema().getType(References.CHUNK);
/* 22 */     OpticFinder opticFinder = type.findField("Level");
/*    */     
/* 24 */     return fixTypeEverywhereTyped("Leaves fix", type, paramTyped -> paramTyped.updateTyped(paramOpticFinder, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ChunkBiomeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */