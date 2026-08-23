/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.longs.LongCollection;
/*    */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*    */ import it.unimi.dsi.fastutil.longs.LongSet;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.world.level.saveddata.SavedDataType;
/*    */ 
/*    */ public class StructureFeatureIndexSavedData extends SavedData {
/*    */   private final LongSet all;
/* 16 */   private static final Codec<LongSet> LONG_SET = Codec.LONG_STREAM.xmap(LongOpenHashSet::toSet, LongCollection::longStream); private final LongSet remaining; public static final Codec<StructureFeatureIndexSavedData> CODEC;
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)LONG_SET.fieldOf("All").forGetter(()), (App)LONG_SET.fieldOf("Remaining").forGetter(())).apply((Applicative)paramInstance, StructureFeatureIndexSavedData::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static SavedDataType<StructureFeatureIndexSavedData> type(String paramString) {
/* 24 */     return new SavedDataType(paramString, StructureFeatureIndexSavedData::new, CODEC, DataFixTypes.SAVED_DATA_STRUCTURE_FEATURE_INDICES);
/*    */   }
/*    */   
/*    */   private StructureFeatureIndexSavedData(LongSet paramLongSet1, LongSet paramLongSet2) {
/* 28 */     this.all = paramLongSet1;
/* 29 */     this.remaining = paramLongSet2;
/*    */   }
/*    */   
/*    */   public StructureFeatureIndexSavedData() {
/* 33 */     this((LongSet)new LongOpenHashSet(), (LongSet)new LongOpenHashSet());
/*    */   }
/*    */   
/*    */   public void addIndex(long paramLong) {
/* 37 */     this.all.add(paramLong);
/* 38 */     this.remaining.add(paramLong);
/* 39 */     setDirty();
/*    */   }
/*    */   
/*    */   public boolean hasStartIndex(long paramLong) {
/* 43 */     return this.all.contains(paramLong);
/*    */   }
/*    */   
/*    */   public boolean hasUnhandledIndex(long paramLong) {
/* 47 */     return this.remaining.contains(paramLong);
/*    */   }
/*    */   
/*    */   public void removeIndex(long paramLong) {
/* 51 */     if (this.remaining.remove(paramLong)) {
/* 52 */       setDirty();
/*    */     }
/*    */   }
/*    */   
/*    */   public LongSet getAll() {
/* 57 */     return this.all;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\StructureFeatureIndexSavedData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */