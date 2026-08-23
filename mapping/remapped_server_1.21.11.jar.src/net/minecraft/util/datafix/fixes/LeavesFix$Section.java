/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.util.datafix.PackedBitStorage;
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
/*     */ 
/*     */ 
/*     */ public abstract class Section
/*     */ {
/*     */   protected static final String BLOCK_STATES_TAG = "BlockStates";
/*     */   protected static final String NAME_TAG = "Name";
/*     */   protected static final String PROPERTIES_TAG = "Properties";
/* 193 */   private final Type<Pair<String, Dynamic<?>>> blockStateType = DSL.named(References.BLOCK_STATE.typeName(), DSL.remainderType());
/* 194 */   protected final OpticFinder<List<Pair<String, Dynamic<?>>>> paletteFinder = DSL.fieldFinder("Palette", (Type)DSL.list(this.blockStateType));
/*     */   
/*     */   protected final List<Dynamic<?>> palette;
/*     */   protected final int index;
/*     */   protected PackedBitStorage storage;
/*     */   
/*     */   public Section(Typed<?> paramTyped, Schema paramSchema) {
/* 201 */     if (!Objects.equals(paramSchema.getType(References.BLOCK_STATE), this.blockStateType)) {
/* 202 */       throw new IllegalStateException("Block state type is not what was expected.");
/*     */     }
/*     */     
/* 205 */     Optional optional = paramTyped.getOptional(this.paletteFinder);
/*     */     
/* 207 */     this.palette = (List<Dynamic<?>>)optional.map(paramList -> (List)paramList.stream().map(Pair::getSecond).collect(Collectors.toList())).orElse(ImmutableList.of());
/*     */     
/* 209 */     Dynamic<?> dynamic = (Dynamic)paramTyped.get(DSL.remainderFinder());
/* 210 */     this.index = dynamic.get("Y").asInt(0);
/*     */     
/* 212 */     readStorage(dynamic);
/*     */   }
/*     */   
/*     */   protected void readStorage(Dynamic<?> paramDynamic) {
/* 216 */     if (skippable()) {
/* 217 */       this.storage = null;
/*     */     } else {
/* 219 */       long[] arrayOfLong = paramDynamic.get("BlockStates").asLongStream().toArray();
/* 220 */       int i = Math.max(4, DataFixUtils.ceillog2(this.palette.size()));
/* 221 */       this.storage = new PackedBitStorage(i, 4096, arrayOfLong);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Typed<?> write(Typed<?> paramTyped) {
/* 226 */     if (isSkippable()) {
/* 227 */       return paramTyped;
/*     */     }
/* 229 */     return paramTyped
/* 230 */       .update(DSL.remainderFinder(), paramDynamic -> paramDynamic.set("BlockStates", paramDynamic.createLongList(Arrays.stream(this.storage.getRaw()))))
/* 231 */       .set(this.paletteFinder, this.palette.stream().map(paramDynamic -> Pair.of(References.BLOCK_STATE.typeName(), paramDynamic)).collect(Collectors.toList()));
/*     */   }
/*     */   
/*     */   public boolean isSkippable() {
/* 235 */     return (this.storage == null);
/*     */   }
/*     */   
/*     */   public int getBlock(int paramInt) {
/* 239 */     return this.storage.get(paramInt);
/*     */   }
/*     */   
/*     */   protected int getStateId(String paramString, boolean paramBoolean, int paramInt) {
/* 243 */     return LeavesFix.LEAVES.get(paramString).intValue() << 5 | (paramBoolean ? 16 : 0) | paramInt;
/*     */   }
/*     */   
/*     */   int getIndex() {
/* 247 */     return this.index;
/*     */   }
/*     */   
/*     */   protected abstract boolean skippable();
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\LeavesFix$Section.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */