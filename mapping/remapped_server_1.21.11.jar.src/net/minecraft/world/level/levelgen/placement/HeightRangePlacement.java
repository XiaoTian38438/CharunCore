/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
/*    */ import net.minecraft.world.level.levelgen.heightproviders.TrapezoidHeight;
/*    */ import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
/*    */ 
/*    */ public class HeightRangePlacement extends PlacementModifier {
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)HeightProvider.CODEC.fieldOf("height").forGetter(())).apply((Applicative)paramInstance, HeightRangePlacement::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<HeightRangePlacement> CODEC;
/*    */   private final HeightProvider height;
/*    */   
/*    */   private HeightRangePlacement(HeightProvider paramHeightProvider) {
/* 25 */     this.height = paramHeightProvider;
/*    */   }
/*    */   
/*    */   public static HeightRangePlacement of(HeightProvider paramHeightProvider) {
/* 29 */     return new HeightRangePlacement(paramHeightProvider);
/*    */   }
/*    */   
/*    */   public static HeightRangePlacement uniform(VerticalAnchor paramVerticalAnchor1, VerticalAnchor paramVerticalAnchor2) {
/* 33 */     return of((HeightProvider)UniformHeight.of(paramVerticalAnchor1, paramVerticalAnchor2));
/*    */   }
/*    */   
/*    */   public static HeightRangePlacement triangle(VerticalAnchor paramVerticalAnchor1, VerticalAnchor paramVerticalAnchor2) {
/* 37 */     return of((HeightProvider)TrapezoidHeight.of(paramVerticalAnchor1, paramVerticalAnchor2));
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> getPositions(PlacementContext paramPlacementContext, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 42 */     return Stream.of(paramBlockPos.atY(this.height.sample(paramRandomSource, paramPlacementContext)));
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 47 */     return PlacementModifierType.HEIGHT_RANGE;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\placement\HeightRangePlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */