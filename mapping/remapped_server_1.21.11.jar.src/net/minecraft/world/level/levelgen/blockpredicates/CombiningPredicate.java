/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ abstract class CombiningPredicate
/*    */   implements BlockPredicate {
/*    */   protected CombiningPredicate(List<BlockPredicate> paramList) {
/* 13 */     this.predicates = paramList;
/*    */   }
/*    */   protected final List<BlockPredicate> predicates;
/*    */   public static <T extends CombiningPredicate> MapCodec<T> codec(Function<List<BlockPredicate>, T> paramFunction) {
/* 17 */     return RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)BlockPredicate.CODEC.listOf().fieldOf("predicates").forGetter(())).apply((Applicative)paramInstance, paramFunction));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\blockpredicates\CombiningPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */