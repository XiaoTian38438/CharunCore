/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.List;
/*     */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TrappedChestBlockEntityFix
/*     */   extends DataFix
/*     */ {
/*  27 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int SIZE = 4096;
/*     */   private static final short SIZE_BITS = 12;
/*     */   
/*     */   public TrappedChestBlockEntityFix(Schema paramSchema, boolean paramBoolean) {
/*  33 */     super(paramSchema, paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/*  38 */     Type type1 = getOutputSchema().getType(References.CHUNK);
/*  39 */     Type type2 = type1.findFieldType("Level");
/*  40 */     Type type3 = type2.findFieldType("TileEntities");
/*  41 */     if (!(type3 instanceof List.ListType)) {
/*  42 */       throw new IllegalStateException("Tile entity type is not a list type.");
/*     */     }
/*  44 */     List.ListType listType = (List.ListType)type3;
/*     */     
/*  46 */     OpticFinder opticFinder1 = DSL.fieldFinder("TileEntities", (Type)listType);
/*     */     
/*  48 */     Type type4 = getInputSchema().getType(References.CHUNK);
/*     */     
/*  50 */     OpticFinder opticFinder2 = type4.findField("Level");
/*  51 */     OpticFinder opticFinder3 = opticFinder2.type().findField("Sections");
/*  52 */     Type type5 = opticFinder3.type();
/*  53 */     if (!(type5 instanceof List.ListType)) {
/*  54 */       throw new IllegalStateException("Expecting sections to be a list.");
/*     */     }
/*  56 */     Type type6 = ((List.ListType)type5).getElement();
/*  57 */     OpticFinder opticFinder4 = DSL.typeFinder(type6);
/*     */     
/*  59 */     return TypeRewriteRule.seq((new AddNewChoices(
/*  60 */           getOutputSchema(), "AddTrappedChestFix", References.BLOCK_ENTITY)).makeRule(), 
/*  61 */         fixTypeEverywhereTyped("Trapped Chest fix", type4, paramTyped -> paramTyped.updateTyped(paramOpticFinder1, ())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class TrappedChestSection
/*     */     extends LeavesFix.Section
/*     */   {
/*     */     private IntSet chestIds;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public TrappedChestSection(Typed<?> param1Typed, Schema param1Schema) {
/* 116 */       super(param1Typed, param1Schema);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean skippable() {
/* 121 */       this.chestIds = (IntSet)new IntOpenHashSet();
/*     */       
/* 123 */       for (byte b = 0; b < this.palette.size(); b++) {
/* 124 */         Dynamic dynamic = this.palette.get(b);
/* 125 */         String str = dynamic.get("Name").asString("");
/* 126 */         if (Objects.equals(str, "minecraft:trapped_chest")) {
/* 127 */           this.chestIds.add(b);
/*     */         }
/*     */       } 
/*     */       
/* 131 */       return this.chestIds.isEmpty();
/*     */     }
/*     */     
/*     */     public boolean isTrappedChest(int param1Int) {
/* 135 */       return this.chestIds.contains(param1Int);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\TrappedChestBlockEntityFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */