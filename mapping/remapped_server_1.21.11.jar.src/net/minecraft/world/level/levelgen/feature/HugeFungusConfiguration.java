/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ 
/*    */ public class HugeFungusConfiguration implements FeatureConfiguration {
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)BlockState.CODEC.fieldOf("valid_base_block").forGetter(()), (App)BlockState.CODEC.fieldOf("stem_state").forGetter(()), (App)BlockState.CODEC.fieldOf("hat_state").forGetter(()), (App)BlockState.CODEC.fieldOf("decor_state").forGetter(()), (App)BlockPredicate.CODEC.fieldOf("replaceable_blocks").forGetter(()), (App)Codec.BOOL.fieldOf("planted").orElse(Boolean.valueOf(false)).forGetter(())).apply((Applicative)paramInstance, HugeFungusConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<HugeFungusConfiguration> CODEC;
/*    */   
/*    */   public final BlockState validBaseState;
/*    */   
/*    */   public final BlockState stemState;
/*    */   
/*    */   public final BlockState hatState;
/*    */   
/*    */   public final BlockState decorState;
/*    */   
/*    */   public final BlockPredicate replaceableBlocks;
/*    */   public final boolean planted;
/*    */   
/*    */   public HugeFungusConfiguration(BlockState paramBlockState1, BlockState paramBlockState2, BlockState paramBlockState3, BlockState paramBlockState4, BlockPredicate paramBlockPredicate, boolean paramBoolean) {
/* 28 */     this.validBaseState = paramBlockState1;
/* 29 */     this.stemState = paramBlockState2;
/* 30 */     this.hatState = paramBlockState3;
/* 31 */     this.decorState = paramBlockState4;
/* 32 */     this.replaceableBlocks = paramBlockPredicate;
/* 33 */     this.planted = paramBoolean;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\HugeFungusConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */