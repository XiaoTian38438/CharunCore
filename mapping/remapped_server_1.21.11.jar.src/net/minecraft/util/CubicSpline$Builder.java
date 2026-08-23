/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.floats.FloatArrayList;
/*     */ import it.unimi.dsi.fastutil.floats.FloatList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Builder<C, I extends BoundedFloatFunction<C>>
/*     */ {
/*     */   private final I coordinate;
/*     */   private final BoundedFloatFunction<Float> valueTransformer;
/* 299 */   private final FloatList locations = (FloatList)new FloatArrayList();
/* 300 */   private final List<CubicSpline<C, I>> values = Lists.newArrayList();
/* 301 */   private final FloatList derivatives = (FloatList)new FloatArrayList();
/*     */   
/*     */   protected Builder(I paramI) {
/* 304 */     this(paramI, BoundedFloatFunction.IDENTITY);
/*     */   }
/*     */   
/*     */   protected Builder(I paramI, BoundedFloatFunction<Float> paramBoundedFloatFunction) {
/* 308 */     this.coordinate = paramI;
/* 309 */     this.valueTransformer = paramBoundedFloatFunction;
/*     */   }
/*     */   
/*     */   public Builder<C, I> addPoint(float paramFloat1, float paramFloat2) {
/* 313 */     return addPoint(paramFloat1, new CubicSpline.Constant<>(this.valueTransformer.apply(Float.valueOf(paramFloat2))), 0.0F);
/*     */   }
/*     */   
/*     */   public Builder<C, I> addPoint(float paramFloat1, float paramFloat2, float paramFloat3) {
/* 317 */     return addPoint(paramFloat1, new CubicSpline.Constant<>(this.valueTransformer.apply(Float.valueOf(paramFloat2))), paramFloat3);
/*     */   }
/*     */   
/*     */   public Builder<C, I> addPoint(float paramFloat, CubicSpline<C, I> paramCubicSpline) {
/* 321 */     return addPoint(paramFloat, paramCubicSpline, 0.0F);
/*     */   }
/*     */   
/*     */   private Builder<C, I> addPoint(float paramFloat1, CubicSpline<C, I> paramCubicSpline, float paramFloat2) {
/* 325 */     if (!this.locations.isEmpty() && paramFloat1 <= this.locations.getFloat(this.locations.size() - 1)) {
/* 326 */       throw new IllegalArgumentException("Please register points in ascending order");
/*     */     }
/* 328 */     this.locations.add(paramFloat1);
/* 329 */     this.values.add(paramCubicSpline);
/* 330 */     this.derivatives.add(paramFloat2);
/* 331 */     return this;
/*     */   }
/*     */   
/*     */   public CubicSpline<C, I> build() {
/* 335 */     if (this.locations.isEmpty()) {
/* 336 */       throw new IllegalStateException("No elements added");
/*     */     }
/* 338 */     return CubicSpline.Multipoint.create(this.coordinate, this.locations.toFloatArray(), (List<CubicSpline<C, I>>)ImmutableList.copyOf(this.values), this.derivatives.toFloatArray());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\CubicSpline$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */