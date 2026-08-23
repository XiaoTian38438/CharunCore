/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*     */ 
/*     */ public class InlineBlockPosFormatFix extends DataFix {
/*     */   public InlineBlockPosFormatFix(Schema paramSchema) {
/*  18 */     super(paramSchema, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/*  23 */     OpticFinder<?> opticFinder1 = entityFinder("minecraft:vex");
/*  24 */     OpticFinder<?> opticFinder2 = entityFinder("minecraft:phantom");
/*  25 */     OpticFinder<?> opticFinder3 = entityFinder("minecraft:turtle");
/*  26 */     List<OpticFinder<?>> list = List.of(
/*  27 */         entityFinder("minecraft:item_frame"), 
/*  28 */         entityFinder("minecraft:glow_item_frame"), 
/*  29 */         entityFinder("minecraft:painting"), 
/*  30 */         entityFinder("minecraft:leash_knot"));
/*     */     
/*  32 */     return TypeRewriteRule.seq(
/*  33 */         fixTypeEverywhereTyped("InlineBlockPosFormatFix - player", getInputSchema().getType(References.PLAYER), paramTyped -> paramTyped.update(DSL.remainderFinder(), this::fixPlayer)), 
/*     */ 
/*     */         
/*  36 */         fixTypeEverywhereTyped("InlineBlockPosFormatFix - entity", getInputSchema().getType(References.ENTITY), paramTyped -> {
/*     */             paramTyped = paramTyped.update(DSL.remainderFinder(), this::fixLivingEntity).updateTyped(paramOpticFinder1, ()).updateTyped(paramOpticFinder2, ()).updateTyped(paramOpticFinder3, ());
/*     */             for (OpticFinder opticFinder : paramList) {
/*     */               paramTyped = paramTyped.updateTyped(opticFinder, ());
/*     */             }
/*     */             return paramTyped;
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private OpticFinder<?> entityFinder(String paramString) {
/*  50 */     return DSL.namedChoice(paramString, getInputSchema().getChoiceType(References.ENTITY, paramString));
/*     */   }
/*     */   
/*     */   private Dynamic<?> fixPlayer(Dynamic<?> paramDynamic) {
/*  54 */     paramDynamic = fixLivingEntity(paramDynamic);
/*  55 */     Optional<Number> optional1 = paramDynamic.get("SpawnX").asNumber().result();
/*  56 */     Optional<Number> optional2 = paramDynamic.get("SpawnY").asNumber().result();
/*  57 */     Optional<Number> optional3 = paramDynamic.get("SpawnZ").asNumber().result();
/*  58 */     if (optional1.isPresent() && optional2.isPresent() && optional3.isPresent()) {
/*  59 */       Dynamic dynamic = paramDynamic.createMap(Map.of(paramDynamic
/*  60 */             .createString("pos"), ExtraDataFixUtils.createBlockPos(paramDynamic, ((Number)optional1.get()).intValue(), ((Number)optional2.get()).intValue(), ((Number)optional3.get()).intValue())));
/*     */       
/*  62 */       dynamic = Dynamic.copyField(paramDynamic, "SpawnAngle", dynamic, "angle");
/*  63 */       dynamic = Dynamic.copyField(paramDynamic, "SpawnDimension", dynamic, "dimension");
/*  64 */       dynamic = Dynamic.copyField(paramDynamic, "SpawnForced", dynamic, "forced");
/*  65 */       paramDynamic = paramDynamic.remove("SpawnX").remove("SpawnY").remove("SpawnZ").remove("SpawnAngle").remove("SpawnDimension").remove("SpawnForced");
/*  66 */       paramDynamic = paramDynamic.set("respawn", dynamic);
/*     */     } 
/*  68 */     Optional<Dynamic> optional = paramDynamic.get("enteredNetherPosition").result();
/*  69 */     if (optional.isPresent()) {
/*  70 */       paramDynamic = paramDynamic.remove("enteredNetherPosition").set("entered_nether_pos", paramDynamic.createList(Stream.of(new Dynamic[] { paramDynamic
/*  71 */                 .createDouble(((Dynamic)optional.get()).get("x").asDouble(0.0D)), paramDynamic
/*  72 */                 .createDouble(((Dynamic)optional.get()).get("y").asDouble(0.0D)), paramDynamic
/*  73 */                 .createDouble(((Dynamic)optional.get()).get("z").asDouble(0.0D)) })));
/*     */     }
/*     */     
/*  76 */     return paramDynamic;
/*     */   }
/*     */   
/*     */   private Dynamic<?> fixLivingEntity(Dynamic<?> paramDynamic) {
/*  80 */     return ExtraDataFixUtils.fixInlineBlockPos(paramDynamic, "SleepingX", "SleepingY", "SleepingZ", "sleeping_pos");
/*     */   }
/*     */   
/*     */   private Dynamic<?> fixVex(Dynamic<?> paramDynamic) {
/*  84 */     return ExtraDataFixUtils.fixInlineBlockPos(paramDynamic
/*  85 */         .renameField("LifeTicks", "life_ticks"), "BoundX", "BoundY", "BoundZ", "bound_pos");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Dynamic<?> fixPhantom(Dynamic<?> paramDynamic) {
/*  92 */     return ExtraDataFixUtils.fixInlineBlockPos(paramDynamic
/*  93 */         .renameField("Size", "size"), "AX", "AY", "AZ", "anchor_pos");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Dynamic<?> fixTurtle(Dynamic<?> paramDynamic) {
/* 100 */     paramDynamic = paramDynamic.remove("TravelPosX").remove("TravelPosY").remove("TravelPosZ");
/* 101 */     paramDynamic = ExtraDataFixUtils.fixInlineBlockPos(paramDynamic, "HomePosX", "HomePosY", "HomePosZ", "home_pos");
/* 102 */     return paramDynamic.renameField("HasEgg", "has_egg");
/*     */   }
/*     */   
/*     */   private Dynamic<?> fixBlockAttached(Dynamic<?> paramDynamic) {
/* 106 */     return ExtraDataFixUtils.fixInlineBlockPos(paramDynamic, "TileX", "TileY", "TileZ", "block_pos");
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\InlineBlockPosFormatFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */