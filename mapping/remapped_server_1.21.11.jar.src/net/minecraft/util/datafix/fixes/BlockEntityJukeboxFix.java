/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class BlockEntityJukeboxFix extends NamedEntityFix {
/*    */   public BlockEntityJukeboxFix(Schema paramSchema, boolean paramBoolean) {
/* 12 */     super(paramSchema, paramBoolean, "BlockEntityJukeboxFix", References.BLOCK_ENTITY, "minecraft:jukebox");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 17 */     Type type1 = getInputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:jukebox");
/* 18 */     Type type2 = type1.findFieldType("RecordItem");
/* 19 */     OpticFinder opticFinder = DSL.fieldFinder("RecordItem", type2);
/* 20 */     Dynamic dynamic = (Dynamic)paramTyped.get(DSL.remainderFinder());
/* 21 */     int i = dynamic.get("Record").asInt(0);
/* 22 */     if (i > 0) {
/* 23 */       dynamic.remove("Record");
/*    */       
/* 25 */       String str = ItemStackTheFlatteningFix.updateItem(ItemIdFix.getItem(i), 0);
/* 26 */       if (str != null) {
/* 27 */         Dynamic dynamic1 = dynamic.emptyMap();
/* 28 */         dynamic1 = dynamic1.set("id", dynamic1.createString(str));
/* 29 */         dynamic1 = dynamic1.set("Count", dynamic1.createByte((byte)1));
/* 30 */         return paramTyped.set(opticFinder, (Typed)((Pair)type2.readTyped(dynamic1).result().orElseThrow(() -> new IllegalStateException("Could not create record item stack."))).getFirst()).set(DSL.remainderFinder(), dynamic);
/*    */       } 
/*    */     } 
/* 33 */     return paramTyped;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\BlockEntityJukeboxFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */