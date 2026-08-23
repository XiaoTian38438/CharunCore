/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.stream.IntStream;
/*    */ 
/*    */ public class ChunkTicketUnpackPosFix extends DataFix {
/*    */   private static final long CHUNK_COORD_BITS = 32L;
/*    */   
/*    */   public ChunkTicketUnpackPosFix(Schema paramSchema) {
/* 15 */     super(paramSchema, false);
/*    */   }
/*    */   private static final long CHUNK_COORD_MASK = 4294967295L;
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 20 */     return fixTypeEverywhereTyped("ChunkTicketUnpackPosFix", getInputSchema().getType(References.SAVED_DATA_TICKETS), paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ChunkTicketUnpackPosFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */