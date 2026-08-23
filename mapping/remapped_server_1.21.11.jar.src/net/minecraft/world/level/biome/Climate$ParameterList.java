/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.ExtraCodecs;
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
/*     */ public class ParameterList<T>
/*     */ {
/*     */   private final List<Pair<Climate.ParameterPoint, T>> values;
/*     */   private final Climate.RTree<T> index;
/*     */   
/*     */   public static <T> Codec<ParameterList<T>> codec(MapCodec<T> paramMapCodec) {
/* 282 */     return ExtraCodecs.nonEmptyList(RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)Climate.ParameterPoint.CODEC.fieldOf("parameters").forGetter(Pair::getFirst), (App)paramMapCodec.forGetter(Pair::getSecond)).apply((Applicative)paramInstance, Pair::of))
/*     */ 
/*     */         
/* 285 */         .listOf()).xmap(ParameterList::new, ParameterList::values);
/*     */   }
/*     */   
/*     */   public ParameterList(List<Pair<Climate.ParameterPoint, T>> paramList) {
/* 289 */     this.values = paramList;
/* 290 */     this.index = Climate.RTree.create(paramList);
/*     */   }
/*     */   
/*     */   public List<Pair<Climate.ParameterPoint, T>> values() {
/* 294 */     return this.values;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T findValue(Climate.TargetPoint paramTargetPoint) {
/* 301 */     return findValueIndex(paramTargetPoint);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public T findValueBruteForce(Climate.TargetPoint paramTargetPoint) {
/* 306 */     Iterator<Pair<Climate.ParameterPoint, T>> iterator = values().iterator();
/*     */ 
/*     */     
/* 309 */     Pair pair = iterator.next();
/* 310 */     long l = ((Climate.ParameterPoint)pair.getFirst()).fitness(paramTargetPoint);
/* 311 */     Object object = pair.getSecond();
/*     */     
/* 313 */     while (iterator.hasNext()) {
/* 314 */       Pair pair1 = iterator.next();
/* 315 */       long l1 = ((Climate.ParameterPoint)pair1.getFirst()).fitness(paramTargetPoint);
/* 316 */       if (l1 < l) {
/* 317 */         l = l1;
/* 318 */         object = pair1.getSecond();
/*     */       } 
/*     */     } 
/* 321 */     return (T)object;
/*     */   }
/*     */   
/*     */   public T findValueIndex(Climate.TargetPoint paramTargetPoint) {
/* 325 */     return findValueIndex(paramTargetPoint, Climate.RTree.Node::distance);
/*     */   }
/*     */   
/*     */   protected T findValueIndex(Climate.TargetPoint paramTargetPoint, Climate.DistanceMetric<T> paramDistanceMetric) {
/* 329 */     return this.index.search(paramTargetPoint, paramDistanceMetric);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\biome\Climate$ParameterList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */