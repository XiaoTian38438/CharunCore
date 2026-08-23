/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Collection;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class RandomizedIntStateProvider extends BlockStateProvider {
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)BlockStateProvider.CODEC.fieldOf("source").forGetter(()), (App)Codec.STRING.fieldOf("property").forGetter(()), (App)IntProvider.CODEC.fieldOf("values").forGetter(())).apply((Applicative)paramInstance, RandomizedIntStateProvider::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<RandomizedIntStateProvider> CODEC;
/*    */   
/*    */   private final BlockStateProvider source;
/*    */   private final String propertyName;
/*    */   private IntegerProperty property;
/*    */   private final IntProvider values;
/*    */   
/*    */   public RandomizedIntStateProvider(BlockStateProvider paramBlockStateProvider, IntegerProperty paramIntegerProperty, IntProvider paramIntProvider) {
/* 30 */     this.source = paramBlockStateProvider;
/* 31 */     this.property = paramIntegerProperty;
/* 32 */     this.propertyName = paramIntegerProperty.getName();
/* 33 */     this.values = paramIntProvider;
/*    */     
/* 35 */     List list = paramIntegerProperty.getPossibleValues();
/* 36 */     for (int i = paramIntProvider.getMinValue(); i <= paramIntProvider.getMaxValue(); i++) {
/* 37 */       if (!list.contains(Integer.valueOf(i))) {
/* 38 */         throw new IllegalArgumentException("Property value out of range: " + paramIntegerProperty.getName() + ": " + i);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public RandomizedIntStateProvider(BlockStateProvider paramBlockStateProvider, String paramString, IntProvider paramIntProvider) {
/* 44 */     this.source = paramBlockStateProvider;
/* 45 */     this.propertyName = paramString;
/* 46 */     this.values = paramIntProvider;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockStateProviderType<?> type() {
/* 51 */     return BlockStateProviderType.RANDOMIZED_INT_STATE_PROVIDER;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getState(RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 56 */     BlockState blockState = this.source.getState(paramRandomSource, paramBlockPos);
/* 57 */     if (this.property == null || !blockState.hasProperty((Property)this.property)) {
/* 58 */       IntegerProperty integerProperty = findProperty(blockState, this.propertyName);
/* 59 */       if (integerProperty == null) {
/* 60 */         return blockState;
/*    */       }
/* 62 */       this.property = integerProperty;
/*    */     } 
/* 64 */     return (BlockState)blockState.setValue((Property)this.property, Integer.valueOf(this.values.sample(paramRandomSource)));
/*    */   }
/*    */   
/*    */   private static IntegerProperty findProperty(BlockState paramBlockState, String paramString) {
/* 68 */     Collection collection = paramBlockState.getProperties();
/*    */ 
/*    */ 
/*    */     
/* 72 */     Optional<IntegerProperty> optional = collection.stream().filter(paramProperty -> paramProperty.getName().equals(paramString)).filter(paramProperty -> paramProperty instanceof IntegerProperty).map(paramProperty -> (IntegerProperty)paramProperty).findAny();
/* 73 */     return optional.orElse(null);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\RandomizedIntStateProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */