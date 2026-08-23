/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ 
/*     */ public class ItemPotionFix
/*     */   extends DataFix {
/*     */   private static final int SPLASH = 16384;
/*     */   private static final String[] POTIONS;
/*     */   public static final String DEFAULT = "minecraft:water";
/*     */   
/*     */   public ItemPotionFix(Schema paramSchema, boolean paramBoolean) {
/*  24 */     super(paramSchema, paramBoolean);
/*     */   }
/*     */   
/*     */   static {
/*  28 */     POTIONS = (String[])DataFixUtils.make(new String[128], paramArrayOfString -> {
/*     */           paramArrayOfString[0] = "minecraft:water";
/*     */           paramArrayOfString[1] = "minecraft:regeneration";
/*     */           paramArrayOfString[2] = "minecraft:swiftness";
/*     */           paramArrayOfString[3] = "minecraft:fire_resistance";
/*     */           paramArrayOfString[4] = "minecraft:poison";
/*     */           paramArrayOfString[5] = "minecraft:healing";
/*     */           paramArrayOfString[6] = "minecraft:night_vision";
/*     */           paramArrayOfString[7] = null;
/*     */           paramArrayOfString[8] = "minecraft:weakness";
/*     */           paramArrayOfString[9] = "minecraft:strength";
/*     */           paramArrayOfString[10] = "minecraft:slowness";
/*     */           paramArrayOfString[11] = "minecraft:leaping";
/*     */           paramArrayOfString[12] = "minecraft:harming";
/*     */           paramArrayOfString[13] = "minecraft:water_breathing";
/*     */           paramArrayOfString[14] = "minecraft:invisibility";
/*     */           paramArrayOfString[15] = null;
/*     */           paramArrayOfString[16] = "minecraft:awkward";
/*     */           paramArrayOfString[17] = "minecraft:regeneration";
/*     */           paramArrayOfString[18] = "minecraft:swiftness";
/*     */           paramArrayOfString[19] = "minecraft:fire_resistance";
/*     */           paramArrayOfString[20] = "minecraft:poison";
/*     */           paramArrayOfString[21] = "minecraft:healing";
/*     */           paramArrayOfString[22] = "minecraft:night_vision";
/*     */           paramArrayOfString[23] = null;
/*     */           paramArrayOfString[24] = "minecraft:weakness";
/*     */           paramArrayOfString[25] = "minecraft:strength";
/*     */           paramArrayOfString[26] = "minecraft:slowness";
/*     */           paramArrayOfString[27] = "minecraft:leaping";
/*     */           paramArrayOfString[28] = "minecraft:harming";
/*     */           paramArrayOfString[29] = "minecraft:water_breathing";
/*     */           paramArrayOfString[30] = "minecraft:invisibility";
/*     */           paramArrayOfString[31] = null;
/*     */           paramArrayOfString[32] = "minecraft:thick";
/*     */           paramArrayOfString[33] = "minecraft:strong_regeneration";
/*     */           paramArrayOfString[34] = "minecraft:strong_swiftness";
/*     */           paramArrayOfString[35] = "minecraft:fire_resistance";
/*     */           paramArrayOfString[36] = "minecraft:strong_poison";
/*     */           paramArrayOfString[37] = "minecraft:strong_healing";
/*     */           paramArrayOfString[38] = "minecraft:night_vision";
/*     */           paramArrayOfString[39] = null;
/*     */           paramArrayOfString[40] = "minecraft:weakness";
/*     */           paramArrayOfString[41] = "minecraft:strong_strength";
/*     */           paramArrayOfString[42] = "minecraft:slowness";
/*     */           paramArrayOfString[43] = "minecraft:strong_leaping";
/*     */           paramArrayOfString[44] = "minecraft:strong_harming";
/*     */           paramArrayOfString[45] = "minecraft:water_breathing";
/*     */           paramArrayOfString[46] = "minecraft:invisibility";
/*     */           paramArrayOfString[47] = null;
/*     */           paramArrayOfString[48] = null;
/*     */           paramArrayOfString[49] = "minecraft:strong_regeneration";
/*     */           paramArrayOfString[50] = "minecraft:strong_swiftness";
/*     */           paramArrayOfString[51] = "minecraft:fire_resistance";
/*     */           paramArrayOfString[52] = "minecraft:strong_poison";
/*     */           paramArrayOfString[53] = "minecraft:strong_healing";
/*     */           paramArrayOfString[54] = "minecraft:night_vision";
/*     */           paramArrayOfString[55] = null;
/*     */           paramArrayOfString[56] = "minecraft:weakness";
/*     */           paramArrayOfString[57] = "minecraft:strong_strength";
/*     */           paramArrayOfString[58] = "minecraft:slowness";
/*     */           paramArrayOfString[59] = "minecraft:strong_leaping";
/*     */           paramArrayOfString[60] = "minecraft:strong_harming";
/*     */           paramArrayOfString[61] = "minecraft:water_breathing";
/*     */           paramArrayOfString[62] = "minecraft:invisibility";
/*     */           paramArrayOfString[63] = null;
/*     */           paramArrayOfString[64] = "minecraft:mundane";
/*     */           paramArrayOfString[65] = "minecraft:long_regeneration";
/*     */           paramArrayOfString[66] = "minecraft:long_swiftness";
/*     */           paramArrayOfString[67] = "minecraft:long_fire_resistance";
/*     */           paramArrayOfString[68] = "minecraft:long_poison";
/*     */           paramArrayOfString[69] = "minecraft:healing";
/*     */           paramArrayOfString[70] = "minecraft:long_night_vision";
/*     */           paramArrayOfString[71] = null;
/*     */           paramArrayOfString[72] = "minecraft:long_weakness";
/*     */           paramArrayOfString[73] = "minecraft:long_strength";
/*     */           paramArrayOfString[74] = "minecraft:long_slowness";
/*     */           paramArrayOfString[75] = "minecraft:long_leaping";
/*     */           paramArrayOfString[76] = "minecraft:harming";
/*     */           paramArrayOfString[77] = "minecraft:long_water_breathing";
/*     */           paramArrayOfString[78] = "minecraft:long_invisibility";
/*     */           paramArrayOfString[79] = null;
/*     */           paramArrayOfString[80] = "minecraft:awkward";
/*     */           paramArrayOfString[81] = "minecraft:long_regeneration";
/*     */           paramArrayOfString[82] = "minecraft:long_swiftness";
/*     */           paramArrayOfString[83] = "minecraft:long_fire_resistance";
/*     */           paramArrayOfString[84] = "minecraft:long_poison";
/*     */           paramArrayOfString[85] = "minecraft:healing";
/*     */           paramArrayOfString[86] = "minecraft:long_night_vision";
/*     */           paramArrayOfString[87] = null;
/*     */           paramArrayOfString[88] = "minecraft:long_weakness";
/*     */           paramArrayOfString[89] = "minecraft:long_strength";
/*     */           paramArrayOfString[90] = "minecraft:long_slowness";
/*     */           paramArrayOfString[91] = "minecraft:long_leaping";
/*     */           paramArrayOfString[92] = "minecraft:harming";
/*     */           paramArrayOfString[93] = "minecraft:long_water_breathing";
/*     */           paramArrayOfString[94] = "minecraft:long_invisibility";
/*     */           paramArrayOfString[95] = null;
/*     */           paramArrayOfString[96] = "minecraft:thick";
/*     */           paramArrayOfString[97] = "minecraft:regeneration";
/*     */           paramArrayOfString[98] = "minecraft:swiftness";
/*     */           paramArrayOfString[99] = "minecraft:long_fire_resistance";
/*     */           paramArrayOfString[100] = "minecraft:poison";
/*     */           paramArrayOfString[101] = "minecraft:strong_healing";
/*     */           paramArrayOfString[102] = "minecraft:long_night_vision";
/*     */           paramArrayOfString[103] = null;
/*     */           paramArrayOfString[104] = "minecraft:long_weakness";
/*     */           paramArrayOfString[105] = "minecraft:strength";
/*     */           paramArrayOfString[106] = "minecraft:long_slowness";
/*     */           paramArrayOfString[107] = "minecraft:leaping";
/*     */           paramArrayOfString[108] = "minecraft:strong_harming";
/*     */           paramArrayOfString[109] = "minecraft:long_water_breathing";
/*     */           paramArrayOfString[110] = "minecraft:long_invisibility";
/*     */           paramArrayOfString[111] = null;
/*     */           paramArrayOfString[112] = null;
/*     */           paramArrayOfString[113] = "minecraft:regeneration";
/*     */           paramArrayOfString[114] = "minecraft:swiftness";
/*     */           paramArrayOfString[115] = "minecraft:long_fire_resistance";
/*     */           paramArrayOfString[116] = "minecraft:poison";
/*     */           paramArrayOfString[117] = "minecraft:strong_healing";
/*     */           paramArrayOfString[118] = "minecraft:long_night_vision";
/*     */           paramArrayOfString[119] = null;
/*     */           paramArrayOfString[120] = "minecraft:long_weakness";
/*     */           paramArrayOfString[121] = "minecraft:strength";
/*     */           paramArrayOfString[122] = "minecraft:long_slowness";
/*     */           paramArrayOfString[123] = "minecraft:leaping";
/*     */           paramArrayOfString[124] = "minecraft:strong_harming";
/*     */           paramArrayOfString[125] = "minecraft:long_water_breathing";
/*     */           paramArrayOfString[126] = "minecraft:long_invisibility";
/*     */           paramArrayOfString[127] = null;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/* 163 */     Type type = getInputSchema().getType(References.ITEM_STACK);
/* 164 */     OpticFinder opticFinder1 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 165 */     OpticFinder opticFinder2 = type.findField("tag");
/*     */     
/* 167 */     return fixTypeEverywhereTyped("ItemPotionFix", type, paramTyped -> {
/*     */           Optional<Pair> optional = paramTyped.getOptional(paramOpticFinder1);
/*     */           if (optional.isPresent() && Objects.equals(((Pair)optional.get()).getSecond(), "minecraft:potion")) {
/*     */             Dynamic dynamic = (Dynamic)paramTyped.get(DSL.remainderFinder());
/*     */             Optional<Typed> optional1 = paramTyped.getOptionalTyped(paramOpticFinder2);
/*     */             short s = dynamic.get("Damage").asShort((short)0);
/*     */             if (optional1.isPresent()) {
/*     */               Typed typed = paramTyped;
/*     */               Dynamic dynamic1 = (Dynamic)((Typed)optional1.get()).get(DSL.remainderFinder());
/*     */               Optional optional2 = dynamic1.get("Potion").asString().result();
/*     */               if (optional2.isEmpty()) {
/*     */                 String str = POTIONS[s & 0x7F];
/*     */                 Typed typed1 = ((Typed)optional1.get()).set(DSL.remainderFinder(), dynamic1.set("Potion", dynamic1.createString((str == null) ? "minecraft:water" : str)));
/*     */                 typed = typed.set(paramOpticFinder2, typed1);
/*     */                 if ((s & 0x4000) == 16384)
/*     */                   typed = typed.set(paramOpticFinder1, Pair.of(References.ITEM_NAME.typeName(), "minecraft:splash_potion")); 
/*     */               } 
/*     */               if (s != 0)
/*     */                 dynamic = dynamic.set("Damage", dynamic.createShort((short)0)); 
/*     */               return typed.set(DSL.remainderFinder(), dynamic);
/*     */             } 
/*     */           } 
/*     */           return paramTyped;
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ItemPotionFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */