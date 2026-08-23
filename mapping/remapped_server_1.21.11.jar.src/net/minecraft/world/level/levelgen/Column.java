/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Column
/*     */ {
/*     */   public static Range around(int paramInt1, int paramInt2) {
/*  23 */     return new Range(paramInt1 - 1, paramInt2 + 1);
/*     */   }
/*     */   
/*     */   public static Range inside(int paramInt1, int paramInt2) {
/*  27 */     return new Range(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Column below(int paramInt) {
/*  34 */     return new Ray(paramInt, false);
/*     */   }
/*     */   
/*     */   public static Column fromHighest(int paramInt) {
/*  38 */     return new Ray(paramInt + 1, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Column above(int paramInt) {
/*  45 */     return new Ray(paramInt, true);
/*     */   }
/*     */   
/*     */   public static Column fromLowest(int paramInt) {
/*  49 */     return new Ray(paramInt - 1, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Column line() {
/*  56 */     return Line.INSTANCE;
/*     */   }
/*     */   
/*     */   public static Column create(OptionalInt paramOptionalInt1, OptionalInt paramOptionalInt2) {
/*  60 */     if (paramOptionalInt1.isPresent() && paramOptionalInt2.isPresent()) {
/*  61 */       return inside(paramOptionalInt1.getAsInt(), paramOptionalInt2.getAsInt());
/*     */     }
/*     */     
/*  64 */     if (paramOptionalInt1.isPresent()) {
/*  65 */       return above(paramOptionalInt1.getAsInt());
/*     */     }
/*     */     
/*  68 */     if (paramOptionalInt2.isPresent()) {
/*  69 */       return below(paramOptionalInt2.getAsInt());
/*     */     }
/*     */     
/*  72 */     return line();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract OptionalInt getCeiling();
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract OptionalInt getFloor();
/*     */ 
/*     */   
/*     */   public abstract OptionalInt getHeight();
/*     */ 
/*     */   
/*     */   public Column withFloor(OptionalInt paramOptionalInt) {
/*  88 */     return create(paramOptionalInt, getCeiling());
/*     */   }
/*     */   
/*     */   public Column withCeiling(OptionalInt paramOptionalInt) {
/*  92 */     return create(getFloor(), paramOptionalInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Optional<Column> scan(LevelSimulatedReader paramLevelSimulatedReader, BlockPos paramBlockPos, int paramInt, Predicate<BlockState> paramPredicate1, Predicate<BlockState> paramPredicate2) {
/* 102 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable();
/* 103 */     if (!paramLevelSimulatedReader.isStateAtPosition(paramBlockPos, paramPredicate1)) {
/* 104 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/* 108 */     int i = paramBlockPos.getY();
/* 109 */     OptionalInt optionalInt1 = scanDirection(paramLevelSimulatedReader, paramInt, paramPredicate1, paramPredicate2, mutableBlockPos, i, Direction.UP);
/* 110 */     OptionalInt optionalInt2 = scanDirection(paramLevelSimulatedReader, paramInt, paramPredicate1, paramPredicate2, mutableBlockPos, i, Direction.DOWN);
/*     */     
/* 112 */     return Optional.of(create(optionalInt2, optionalInt1));
/*     */   }
/*     */   
/*     */   private static OptionalInt scanDirection(LevelSimulatedReader paramLevelSimulatedReader, int paramInt1, Predicate<BlockState> paramPredicate1, Predicate<BlockState> paramPredicate2, BlockPos.MutableBlockPos paramMutableBlockPos, int paramInt2, Direction paramDirection) {
/* 116 */     paramMutableBlockPos.setY(paramInt2);
/* 117 */     for (byte b = 1; b < paramInt1 && 
/* 118 */       paramLevelSimulatedReader.isStateAtPosition((BlockPos)paramMutableBlockPos, paramPredicate1); b++) {
/* 119 */       paramMutableBlockPos.move(paramDirection);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     return paramLevelSimulatedReader.isStateAtPosition((BlockPos)paramMutableBlockPos, paramPredicate2) ? OptionalInt.of(paramMutableBlockPos.getY()) : OptionalInt.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class Range
/*     */     extends Column
/*     */   {
/*     */     private final int floor;
/*     */     private final int ceiling;
/*     */     
/*     */     protected Range(int param1Int1, int param1Int2) {
/* 136 */       this.floor = param1Int1;
/* 137 */       this.ceiling = param1Int2;
/* 138 */       if (height() < 0) {
/* 139 */         throw new IllegalArgumentException("Column of negative height: " + String.valueOf(this));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public OptionalInt getCeiling() {
/* 145 */       return OptionalInt.of(this.ceiling);
/*     */     }
/*     */ 
/*     */     
/*     */     public OptionalInt getFloor() {
/* 150 */       return OptionalInt.of(this.floor);
/*     */     }
/*     */ 
/*     */     
/*     */     public OptionalInt getHeight() {
/* 155 */       return OptionalInt.of(height());
/*     */     }
/*     */     
/*     */     public int ceiling() {
/* 159 */       return this.ceiling;
/*     */     }
/*     */     
/*     */     public int floor() {
/* 163 */       return this.floor;
/*     */     }
/*     */     
/*     */     public int height() {
/* 167 */       return this.ceiling - this.floor - 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 172 */       return "C(" + this.ceiling + "-" + this.floor + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class Line
/*     */     extends Column
/*     */   {
/* 180 */     static final Line INSTANCE = new Line();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public OptionalInt getCeiling() {
/* 187 */       return OptionalInt.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public OptionalInt getFloor() {
/* 192 */       return OptionalInt.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public OptionalInt getHeight() {
/* 197 */       return OptionalInt.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 202 */       return "C(-)";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class Ray
/*     */     extends Column
/*     */   {
/*     */     private final int edge;
/*     */     private final boolean pointingUp;
/*     */     
/*     */     public Ray(int param1Int, boolean param1Boolean) {
/* 214 */       this.edge = param1Int;
/* 215 */       this.pointingUp = param1Boolean;
/*     */     }
/*     */ 
/*     */     
/*     */     public OptionalInt getCeiling() {
/* 220 */       return this.pointingUp ? OptionalInt.empty() : OptionalInt.of(this.edge);
/*     */     }
/*     */ 
/*     */     
/*     */     public OptionalInt getFloor() {
/* 225 */       return this.pointingUp ? OptionalInt.of(this.edge) : OptionalInt.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public OptionalInt getHeight() {
/* 230 */       return OptionalInt.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 235 */       return this.pointingUp ? ("C(" + this.edge + "-)") : ("C(-" + this.edge + ")");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\Column.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */