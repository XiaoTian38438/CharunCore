/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MemoryExpiryDataFix
/*    */   extends NamedEntityFix
/*    */ {
/*    */   public MemoryExpiryDataFix(Schema paramSchema, String paramString) {
/* 30 */     super(paramSchema, false, "Memory expiry data fix (" + paramString + ")", References.ENTITY, paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 35 */     return paramTyped.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> paramDynamic) {
/* 39 */     return paramDynamic.update("Brain", this::updateBrain);
/*    */   }
/*    */   
/*    */   private Dynamic<?> updateBrain(Dynamic<?> paramDynamic) {
/* 43 */     return paramDynamic.update("memories", this::updateMemories);
/*    */   }
/*    */   
/*    */   private Dynamic<?> updateMemories(Dynamic<?> paramDynamic) {
/* 47 */     return paramDynamic.updateMapValues(this::updateMemoryEntry);
/*    */   }
/*    */   
/*    */   private Pair<Dynamic<?>, Dynamic<?>> updateMemoryEntry(Pair<Dynamic<?>, Dynamic<?>> paramPair) {
/* 51 */     return paramPair.mapSecond(this::wrapMemoryValue);
/*    */   }
/*    */   
/*    */   private Dynamic<?> wrapMemoryValue(Dynamic<?> paramDynamic) {
/* 55 */     return paramDynamic.createMap((Map)ImmutableMap.of(paramDynamic
/* 56 */           .createString("value"), paramDynamic));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\MemoryExpiryDataFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */