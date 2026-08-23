/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class DropExperienceBlock extends Block {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)IntProvider.codec(0, 10).fieldOf("experience").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, DropExperienceBlock::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<DropExperienceBlock> CODEC;
/*    */   private final IntProvider xpRange;
/*    */   
/*    */   public MapCodec<? extends DropExperienceBlock> codec() {
/* 19 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public DropExperienceBlock(IntProvider paramIntProvider, BlockBehaviour.Properties paramProperties) {
/* 25 */     super(paramProperties);
/* 26 */     this.xpRange = paramIntProvider;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void spawnAfterBreak(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, ItemStack paramItemStack, boolean paramBoolean) {
/* 31 */     super.spawnAfterBreak(paramBlockState, paramServerLevel, paramBlockPos, paramItemStack, paramBoolean);
/* 32 */     if (paramBoolean)
/* 33 */       tryDropExperience(paramServerLevel, paramBlockPos, paramItemStack, this.xpRange); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\DropExperienceBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */