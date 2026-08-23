/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class WallPropertyFix extends DataFix {
/* 14 */   private static final Set<String> WALL_BLOCKS = (Set<String>)ImmutableSet.of("minecraft:andesite_wall", "minecraft:brick_wall", "minecraft:cobblestone_wall", "minecraft:diorite_wall", "minecraft:end_stone_brick_wall", "minecraft:granite_wall", (Object[])new String[] { "minecraft:mossy_cobblestone_wall", "minecraft:mossy_stone_brick_wall", "minecraft:nether_brick_wall", "minecraft:prismarine_wall", "minecraft:red_nether_brick_wall", "minecraft:red_sandstone_wall", "minecraft:sandstone_wall", "minecraft:stone_brick_wall" });
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
/*    */   public WallPropertyFix(Schema paramSchema, boolean paramBoolean) {
/* 32 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 37 */     return fixTypeEverywhereTyped("WallPropertyFix", getInputSchema().getType(References.BLOCK_STATE), paramTyped -> paramTyped.update(DSL.remainderFinder(), WallPropertyFix::upgradeBlockStateTag));
/*    */   }
/*    */   
/*    */   private static String mapProperty(String paramString) {
/* 41 */     return "true".equals(paramString) ? "low" : "none";
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> fixWallProperty(Dynamic<T> paramDynamic, String paramString) {
/* 45 */     return paramDynamic.update(paramString, paramDynamic -> {
/*    */           Objects.requireNonNull(paramDynamic);
/*    */           return (Dynamic)DataFixUtils.orElse(paramDynamic.asString().result().map(WallPropertyFix::mapProperty).map(paramDynamic::createString), paramDynamic);
/*    */         }); } private static <T> Dynamic<T> upgradeBlockStateTag(Dynamic<T> paramDynamic) {
/* 49 */     Objects.requireNonNull(WALL_BLOCKS); boolean bool = paramDynamic.get("Name").asString().result().filter(WALL_BLOCKS::contains).isPresent();
/* 50 */     if (!bool) {
/* 51 */       return paramDynamic;
/*    */     }
/*    */     
/* 54 */     return paramDynamic.update("Properties", paramDynamic -> {
/*    */           Dynamic<?> dynamic = fixWallProperty(paramDynamic, "east");
/*    */           dynamic = fixWallProperty(dynamic, "west");
/*    */           dynamic = fixWallProperty(dynamic, "north");
/*    */           return fixWallProperty(dynamic, "south");
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\WallPropertyFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */