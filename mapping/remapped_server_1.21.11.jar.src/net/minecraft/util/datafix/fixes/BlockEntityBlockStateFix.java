/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class BlockEntityBlockStateFix extends NamedEntityFix {
/*    */   public BlockEntityBlockStateFix(Schema paramSchema, boolean paramBoolean) {
/* 12 */     super(paramSchema, paramBoolean, "BlockEntityBlockStateFix", References.BLOCK_ENTITY, "minecraft:piston");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 17 */     Type type1 = getOutputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:piston");
/*    */     
/* 19 */     Type type2 = type1.findFieldType("blockState");
/* 20 */     OpticFinder opticFinder = DSL.fieldFinder("blockState", type2);
/* 21 */     Dynamic dynamic = (Dynamic)paramTyped.get(DSL.remainderFinder());
/*    */     
/* 23 */     int i = dynamic.get("blockId").asInt(0);
/* 24 */     dynamic = dynamic.remove("blockId");
/* 25 */     int j = dynamic.get("blockData").asInt(0) & 0xF;
/* 26 */     dynamic = dynamic.remove("blockData");
/*    */     
/* 28 */     Dynamic<?> dynamic1 = BlockStateData.getTag(i << 4 | j);
/* 29 */     Typed typed = (Typed)type1.pointTyped(paramTyped.getOps()).orElseThrow(() -> new IllegalStateException("Could not create new piston block entity."));
/* 30 */     return typed.set(DSL.remainderFinder(), dynamic).set(opticFinder, (Typed)((Pair)type2.readTyped(dynamic1).result().orElseThrow(() -> new IllegalStateException("Could not parse newly created block state tag."))).getFirst());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\BlockEntityBlockStateFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */