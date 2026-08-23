/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class GossipUUIDFix extends NamedEntityFix {
/*    */   public GossipUUIDFix(Schema paramSchema, String paramString) {
/* 10 */     super(paramSchema, false, "Gossip for for " + paramString, References.ENTITY, paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 15 */     return paramTyped.update(DSL.remainderFinder(), paramDynamic -> paramDynamic.update("Gossips", ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\GossipUUIDFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */