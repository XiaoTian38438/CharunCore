/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.base.Suppliers;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class EntityZombieSplitFix
/*    */   extends EntityRenameFix {
/*    */   private final Supplier<Type<?>> zombieVillagerType;
/*    */   
/*    */   public EntityZombieSplitFix(Schema paramSchema) {
/* 18 */     super("EntityZombieSplitFix", paramSchema, true);
/*    */     
/* 20 */     this.zombieVillagerType = (Supplier<Type<?>>)Suppliers.memoize(() -> getOutputSchema().getChoiceType(References.ENTITY, "ZombieVillager"));
/*    */   }
/*    */ 
/*    */   
/*    */   protected Pair<String, Typed<?>> fix(String paramString, Typed<?> paramTyped) {
/* 25 */     if (!paramString.equals("Zombie")) {
/* 26 */       return Pair.of(paramString, paramTyped);
/*    */     }
/*    */     
/* 29 */     Dynamic dynamic = paramTyped.getOptional(DSL.remainderFinder()).orElseThrow();
/* 30 */     int i = dynamic.get("ZombieType").asInt(0);
/*    */ 
/*    */ 
/*    */     
/* 34 */     switch (i)
/*    */     { default:
/* 36 */         str = "Zombie";
/* 37 */         typed = paramTyped;
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
/* 49 */         return Pair.of(str, typed.update(DSL.remainderFinder(), paramDynamic -> paramDynamic.remove("ZombieType")));case 1: case 2: case 3: case 4: case 5: str = "ZombieVillager"; typed = changeSchemaToZombieVillager(paramTyped, i - 1); return Pair.of(str, typed.update(DSL.remainderFinder(), paramDynamic -> paramDynamic.remove("ZombieType")));case 6: break; }  String str = "Husk"; Typed<?> typed = paramTyped; return Pair.of(str, typed.update(DSL.remainderFinder(), paramDynamic -> paramDynamic.remove("ZombieType")));
/*    */   }
/*    */ 
/*    */   
/*    */   private Typed<?> changeSchemaToZombieVillager(Typed<?> paramTyped, int paramInt) {
/* 54 */     return Util.writeAndReadTypedOrThrow(paramTyped, this.zombieVillagerType.get(), paramDynamic -> paramDynamic.set("Profession", paramDynamic.createInt(paramInt)));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityZombieSplitFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */