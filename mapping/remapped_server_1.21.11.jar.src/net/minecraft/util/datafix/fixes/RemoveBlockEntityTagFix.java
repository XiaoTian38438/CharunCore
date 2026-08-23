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
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class RemoveBlockEntityTagFix
/*    */   extends DataFix {
/*    */   private final Set<String> blockEntityIdsToDrop;
/*    */   
/*    */   public RemoveBlockEntityTagFix(Schema paramSchema, Set<String> paramSet) {
/* 22 */     super(paramSchema, true);
/* 23 */     this.blockEntityIdsToDrop = paramSet;
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 28 */     Type type1 = getInputSchema().getType(References.ITEM_STACK);
/* 29 */     OpticFinder opticFinder1 = type1.findField("tag");
/* 30 */     OpticFinder opticFinder2 = opticFinder1.type().findField("BlockEntityTag");
/*    */     
/* 32 */     Type type2 = getInputSchema().getType(References.ENTITY);
/* 33 */     OpticFinder opticFinder3 = DSL.namedChoice("minecraft:falling_block", getInputSchema().getChoiceType(References.ENTITY, "minecraft:falling_block"));
/* 34 */     OpticFinder opticFinder4 = opticFinder3.type().findField("TileEntityData");
/*    */     
/* 36 */     Type type3 = getInputSchema().getType(References.STRUCTURE);
/* 37 */     OpticFinder opticFinder5 = type3.findField("blocks");
/* 38 */     OpticFinder opticFinder6 = DSL.typeFinder(((List.ListType)opticFinder5.type()).getElement());
/* 39 */     OpticFinder opticFinder7 = opticFinder6.type().findField("nbt");
/*    */     
/* 41 */     OpticFinder opticFinder8 = DSL.fieldFinder("id", NamespacedSchema.namespacedString());
/*    */     
/* 43 */     return TypeRewriteRule.seq(
/* 44 */         fixTypeEverywhereTyped("ItemRemoveBlockEntityTagFix", type1, paramTyped -> paramTyped.updateTyped(paramOpticFinder1, ())), new TypeRewriteRule[] {
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 49 */           fixTypeEverywhereTyped("FallingBlockEntityRemoveBlockEntityTagFix", type2, paramTyped -> paramTyped.updateTyped(paramOpticFinder1, ())), 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 54 */           fixTypeEverywhereTyped("StructureRemoveBlockEntityTagFix", type3, paramTyped -> paramTyped.updateTyped(paramOpticFinder1, ())), 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 63 */           convertUnchecked("ItemRemoveBlockEntityTagFix - update block entity type", getInputSchema().getType(References.BLOCK_ENTITY), getOutputSchema().getType(References.BLOCK_ENTITY))
/*    */         });
/*    */   }
/*    */   
/*    */   private Typed<?> removeBlockEntity(Typed<?> paramTyped, OpticFinder<?> paramOpticFinder, OpticFinder<String> paramOpticFinder1, String paramString) {
/* 68 */     Optional<Typed> optional = paramTyped.getOptionalTyped(paramOpticFinder);
/* 69 */     if (optional.isEmpty()) {
/* 70 */       return paramTyped;
/*    */     }
/* 72 */     String str = ((Typed)optional.get()).getOptional(paramOpticFinder1).orElse("");
/* 73 */     if (!this.blockEntityIdsToDrop.contains(str)) {
/* 74 */       return paramTyped;
/*    */     }
/* 76 */     return Util.writeAndReadTypedOrThrow(paramTyped, paramTyped.getType(), paramDynamic -> paramDynamic.remove(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\RemoveBlockEntityTagFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */