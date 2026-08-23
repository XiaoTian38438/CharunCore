/*    */ package net.minecraft.gametest.framework;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.TestBlock;
/*    */ import net.minecraft.world.level.block.entity.TestBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.block.state.properties.TestBlockMode;
/*    */ 
/*    */ public class BlockBasedTestInstance extends GameTestInstance {
/*    */   static {
/* 20 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)TestData.CODEC.forGetter(GameTestInstance::info)).apply((Applicative)paramInstance, BlockBasedTestInstance::new));
/*    */   }
/*    */   public static final MapCodec<BlockBasedTestInstance> CODEC;
/*    */   
/*    */   public BlockBasedTestInstance(TestData<Holder<TestEnvironmentDefinition>> paramTestData) {
/* 25 */     super(paramTestData);
/*    */   }
/*    */ 
/*    */   
/*    */   public void run(GameTestHelper paramGameTestHelper) {
/* 30 */     BlockPos blockPos = findStartBlock(paramGameTestHelper);
/* 31 */     TestBlockEntity testBlockEntity = paramGameTestHelper.<TestBlockEntity>getBlockEntity(blockPos, TestBlockEntity.class);
/* 32 */     testBlockEntity.trigger();
/*    */     
/* 34 */     paramGameTestHelper.onEachTick(() -> {
/*    */           List<BlockPos> list = findTestBlocks(paramGameTestHelper, TestBlockMode.ACCEPT);
/*    */           if (list.isEmpty()) {
/*    */             paramGameTestHelper.fail((Component)Component.translatable("test_block.error.missing", new Object[] { TestBlockMode.ACCEPT.getDisplayName() }));
/*    */           }
/*    */           boolean bool = list.stream().map(()).anyMatch(TestBlockEntity::hasTriggered);
/*    */           if (bool) {
/*    */             paramGameTestHelper.succeed();
/*    */           } else {
/*    */             forAllTriggeredTestBlocks(paramGameTestHelper, TestBlockMode.FAIL, ());
/*    */             forAllTriggeredTestBlocks(paramGameTestHelper, TestBlockMode.LOG, TestBlockEntity::trigger);
/*    */           } 
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private void forAllTriggeredTestBlocks(GameTestHelper paramGameTestHelper, TestBlockMode paramTestBlockMode, Consumer<TestBlockEntity> paramConsumer) {
/* 52 */     List<BlockPos> list = findTestBlocks(paramGameTestHelper, paramTestBlockMode);
/* 53 */     for (BlockPos blockPos : list) {
/* 54 */       TestBlockEntity testBlockEntity = paramGameTestHelper.<TestBlockEntity>getBlockEntity(blockPos, TestBlockEntity.class);
/* 55 */       if (testBlockEntity.hasTriggered()) {
/* 56 */         paramConsumer.accept(testBlockEntity);
/* 57 */         testBlockEntity.reset();
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private BlockPos findStartBlock(GameTestHelper paramGameTestHelper) {
/* 63 */     List<BlockPos> list = findTestBlocks(paramGameTestHelper, TestBlockMode.START);
/* 64 */     if (list.isEmpty()) {
/* 65 */       paramGameTestHelper.fail((Component)Component.translatable("test_block.error.missing", new Object[] { TestBlockMode.START.getDisplayName() }));
/*    */     }
/* 67 */     if (list.size() != 1) {
/* 68 */       paramGameTestHelper.fail((Component)Component.translatable("test_block.error.too_many", new Object[] { TestBlockMode.START.getDisplayName() }));
/*    */     }
/* 70 */     return list.getFirst();
/*    */   }
/*    */   
/*    */   private List<BlockPos> findTestBlocks(GameTestHelper paramGameTestHelper, TestBlockMode paramTestBlockMode) {
/* 74 */     ArrayList<BlockPos> arrayList = new ArrayList();
/* 75 */     paramGameTestHelper.forEveryBlockInStructure(paramBlockPos -> {
/*    */           BlockState blockState = paramGameTestHelper.getBlockState(paramBlockPos);
/*    */           if (blockState.is(Blocks.TEST_BLOCK) && blockState.getValue((Property)TestBlock.MODE) == paramTestBlockMode) {
/*    */             paramList.add(paramBlockPos.immutable());
/*    */           }
/*    */         });
/* 81 */     return arrayList;
/*    */   }
/*    */ 
/*    */   
/*    */   public MapCodec<BlockBasedTestInstance> codec() {
/* 86 */     return CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   protected MutableComponent typeDescription() {
/* 91 */     return Component.translatable("test_instance.type.block_based");
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\BlockBasedTestInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */