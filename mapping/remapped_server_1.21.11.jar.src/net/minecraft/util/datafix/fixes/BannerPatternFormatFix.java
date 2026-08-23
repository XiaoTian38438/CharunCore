/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ public class BannerPatternFormatFix extends NamedEntityFix {
/* 13 */   private static final Map<String, String> PATTERN_ID_MAP = Map.ofEntries((Map.Entry<? extends String, ? extends String>[])new Map.Entry[] { 
/* 14 */         Map.entry("b", "minecraft:base"), 
/* 15 */         Map.entry("bl", "minecraft:square_bottom_left"), 
/* 16 */         Map.entry("br", "minecraft:square_bottom_right"), 
/* 17 */         Map.entry("tl", "minecraft:square_top_left"), 
/* 18 */         Map.entry("tr", "minecraft:square_top_right"), 
/* 19 */         Map.entry("bs", "minecraft:stripe_bottom"), 
/* 20 */         Map.entry("ts", "minecraft:stripe_top"), 
/* 21 */         Map.entry("ls", "minecraft:stripe_left"), 
/* 22 */         Map.entry("rs", "minecraft:stripe_right"), 
/* 23 */         Map.entry("cs", "minecraft:stripe_center"), 
/* 24 */         Map.entry("ms", "minecraft:stripe_middle"), 
/* 25 */         Map.entry("drs", "minecraft:stripe_downright"), 
/* 26 */         Map.entry("dls", "minecraft:stripe_downleft"), 
/* 27 */         Map.entry("ss", "minecraft:small_stripes"), 
/* 28 */         Map.entry("cr", "minecraft:cross"), 
/* 29 */         Map.entry("sc", "minecraft:straight_cross"), 
/* 30 */         Map.entry("bt", "minecraft:triangle_bottom"), 
/* 31 */         Map.entry("tt", "minecraft:triangle_top"), 
/* 32 */         Map.entry("bts", "minecraft:triangles_bottom"), 
/* 33 */         Map.entry("tts", "minecraft:triangles_top"), 
/* 34 */         Map.entry("ld", "minecraft:diagonal_left"), 
/* 35 */         Map.entry("rd", "minecraft:diagonal_up_right"), 
/* 36 */         Map.entry("lud", "minecraft:diagonal_up_left"), 
/* 37 */         Map.entry("rud", "minecraft:diagonal_right"), 
/* 38 */         Map.entry("mc", "minecraft:circle"), 
/* 39 */         Map.entry("mr", "minecraft:rhombus"), 
/* 40 */         Map.entry("vh", "minecraft:half_vertical"), 
/* 41 */         Map.entry("hh", "minecraft:half_horizontal"), 
/* 42 */         Map.entry("vhr", "minecraft:half_vertical_right"), 
/* 43 */         Map.entry("hhb", "minecraft:half_horizontal_bottom"), 
/* 44 */         Map.entry("bo", "minecraft:border"), 
/* 45 */         Map.entry("cbo", "minecraft:curly_border"), 
/* 46 */         Map.entry("gra", "minecraft:gradient"), 
/* 47 */         Map.entry("gru", "minecraft:gradient_up"), 
/* 48 */         Map.entry("bri", "minecraft:bricks"), 
/* 49 */         Map.entry("glb", "minecraft:globe"), 
/* 50 */         Map.entry("cre", "minecraft:creeper"), 
/* 51 */         Map.entry("sku", "minecraft:skull"), 
/* 52 */         Map.entry("flo", "minecraft:flower"), 
/* 53 */         Map.entry("moj", "minecraft:mojang"), 
/* 54 */         Map.entry("pig", "minecraft:piglin") });
/*    */ 
/*    */   
/*    */   public BannerPatternFormatFix(Schema paramSchema) {
/* 58 */     super(paramSchema, false, "BannerPatternFormatFix", References.BLOCK_ENTITY, "minecraft:banner");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 63 */     return paramTyped.update(DSL.remainderFinder(), BannerPatternFormatFix::fixTag);
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixTag(Dynamic<?> paramDynamic) {
/* 67 */     return paramDynamic.renameAndFixField("Patterns", "patterns", paramDynamic -> paramDynamic.createList(paramDynamic.asStream().map(BannerPatternFormatFix::fixLayer)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static Dynamic<?> fixLayer(Dynamic<?> paramDynamic) {
/* 73 */     paramDynamic = paramDynamic.renameAndFixField("Pattern", "pattern", paramDynamic -> {
/*    */           Objects.requireNonNull(paramDynamic); return (Dynamic)DataFixUtils.orElse(paramDynamic.asString().map(()).map(paramDynamic::createString).result(), paramDynamic);
/*    */         });
/* 76 */     paramDynamic = paramDynamic.set("color", paramDynamic.createString(ExtraDataFixUtils.dyeColorIdToName(paramDynamic.get("Color").asInt(0))));
/* 77 */     paramDynamic = paramDynamic.remove("Color");
/* 78 */     return paramDynamic;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\BannerPatternFormatFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */