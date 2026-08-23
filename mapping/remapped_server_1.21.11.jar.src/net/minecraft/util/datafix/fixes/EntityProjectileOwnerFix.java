/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.OptionalDynamic;
/*    */ import java.util.Arrays;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ public class EntityProjectileOwnerFix
/*    */   extends DataFix
/*    */ {
/*    */   public EntityProjectileOwnerFix(Schema paramSchema) {
/* 19 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 24 */     Schema schema = getInputSchema();
/* 25 */     return fixTypeEverywhereTyped("EntityProjectileOwner", schema.getType(References.ENTITY), this::updateProjectiles);
/*    */   }
/*    */   
/*    */   private Typed<?> updateProjectiles(Typed<?> paramTyped) {
/* 29 */     paramTyped = updateEntity(paramTyped, "minecraft:egg", this::updateOwnerThrowable);
/* 30 */     paramTyped = updateEntity(paramTyped, "minecraft:ender_pearl", this::updateOwnerThrowable);
/* 31 */     paramTyped = updateEntity(paramTyped, "minecraft:experience_bottle", this::updateOwnerThrowable);
/* 32 */     paramTyped = updateEntity(paramTyped, "minecraft:snowball", this::updateOwnerThrowable);
/* 33 */     paramTyped = updateEntity(paramTyped, "minecraft:potion", this::updateOwnerThrowable);
/* 34 */     paramTyped = updateEntity(paramTyped, "minecraft:llama_spit", this::updateOwnerLlamaSpit);
/* 35 */     paramTyped = updateEntity(paramTyped, "minecraft:arrow", this::updateOwnerArrow);
/* 36 */     paramTyped = updateEntity(paramTyped, "minecraft:spectral_arrow", this::updateOwnerArrow);
/* 37 */     paramTyped = updateEntity(paramTyped, "minecraft:trident", this::updateOwnerArrow);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 47 */     return paramTyped;
/*    */   }
/*    */   
/*    */   private Dynamic<?> updateOwnerArrow(Dynamic<?> paramDynamic) {
/* 51 */     long l1 = paramDynamic.get("OwnerUUIDMost").asLong(0L);
/* 52 */     long l2 = paramDynamic.get("OwnerUUIDLeast").asLong(0L);
/*    */     
/* 54 */     return setUUID(paramDynamic, l1, l2).remove("OwnerUUIDMost").remove("OwnerUUIDLeast");
/*    */   }
/*    */   
/*    */   private Dynamic<?> updateOwnerLlamaSpit(Dynamic<?> paramDynamic) {
/* 58 */     OptionalDynamic optionalDynamic = paramDynamic.get("Owner");
/* 59 */     long l1 = optionalDynamic.get("OwnerUUIDMost").asLong(0L);
/* 60 */     long l2 = optionalDynamic.get("OwnerUUIDLeast").asLong(0L);
/*    */     
/* 62 */     return setUUID(paramDynamic, l1, l2).remove("Owner");
/*    */   }
/*    */   
/*    */   private Dynamic<?> updateOwnerThrowable(Dynamic<?> paramDynamic) {
/* 66 */     String str = "owner";
/* 67 */     OptionalDynamic optionalDynamic = paramDynamic.get("owner");
/* 68 */     long l1 = optionalDynamic.get("M").asLong(0L);
/* 69 */     long l2 = optionalDynamic.get("L").asLong(0L);
/*    */     
/* 71 */     return setUUID(paramDynamic, l1, l2).remove("owner");
/*    */   }
/*    */   
/*    */   private Dynamic<?> setUUID(Dynamic<?> paramDynamic, long paramLong1, long paramLong2) {
/* 75 */     String str = "OwnerUUID";
/* 76 */     if (paramLong1 != 0L && paramLong2 != 0L) {
/* 77 */       return paramDynamic.set("OwnerUUID", paramDynamic.createIntList(Arrays.stream(createUUIDArray(paramLong1, paramLong2))));
/*    */     }
/* 79 */     return paramDynamic;
/*    */   }
/*    */   
/*    */   private static int[] createUUIDArray(long paramLong1, long paramLong2) {
/* 83 */     return new int[] { (int)(paramLong1 >> 32L), (int)paramLong1, (int)(paramLong2 >> 32L), (int)paramLong2 };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Typed<?> updateEntity(Typed<?> paramTyped, String paramString, Function<Dynamic<?>, Dynamic<?>> paramFunction) {
/* 92 */     Type type1 = getInputSchema().getChoiceType(References.ENTITY, paramString);
/* 93 */     Type type2 = getOutputSchema().getChoiceType(References.ENTITY, paramString);
/* 94 */     return paramTyped.updateTyped(DSL.namedChoice(paramString, type1), type2, paramTyped -> paramTyped.update(DSL.remainderFinder(), paramFunction));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityProjectileOwnerFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */