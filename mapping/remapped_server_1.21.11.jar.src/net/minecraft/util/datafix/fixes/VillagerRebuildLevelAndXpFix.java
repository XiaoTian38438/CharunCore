/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.List;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class VillagerRebuildLevelAndXpFix
/*    */   extends DataFix {
/*    */   private static final int TRADES_PER_LEVEL = 2;
/* 18 */   private static final int[] LEVEL_XP_THRESHOLDS = new int[] { 0, 10, 50, 100, 150 };
/*    */   
/*    */   public static int getMinXpPerLevel(int paramInt) {
/* 21 */     return LEVEL_XP_THRESHOLDS[Mth.clamp(paramInt - 1, 0, LEVEL_XP_THRESHOLDS.length - 1)];
/*    */   }
/*    */   
/*    */   public VillagerRebuildLevelAndXpFix(Schema paramSchema, boolean paramBoolean) {
/* 25 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 30 */     Type type1 = getInputSchema().getChoiceType(References.ENTITY, "minecraft:villager");
/* 31 */     OpticFinder opticFinder1 = DSL.namedChoice("minecraft:villager", type1);
/*    */     
/* 33 */     OpticFinder opticFinder2 = type1.findField("Offers");
/* 34 */     Type type2 = opticFinder2.type();
/* 35 */     OpticFinder opticFinder3 = type2.findField("Recipes");
/* 36 */     List.ListType listType = (List.ListType)opticFinder3.type();
/* 37 */     OpticFinder opticFinder4 = listType.getElement().finder();
/*    */     
/* 39 */     return fixTypeEverywhereTyped("Villager level and xp rebuild", getInputSchema().getType(References.ENTITY), paramTyped -> paramTyped.updateTyped(paramOpticFinder1, paramType, ()));
/*    */   }
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
/*    */   private static Typed<?> addLevel(Typed<?> paramTyped, int paramInt) {
/* 72 */     return paramTyped.update(DSL.remainderFinder(), paramDynamic -> paramDynamic.update("VillagerData", ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Typed<?> addXpFromLevel(Typed<?> paramTyped, int paramInt) {
/* 79 */     int i = getMinXpPerLevel(paramInt);
/* 80 */     return paramTyped.update(DSL.remainderFinder(), paramDynamic -> paramDynamic.set("Xp", paramDynamic.createInt(paramInt)));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\VillagerRebuildLevelAndXpFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */