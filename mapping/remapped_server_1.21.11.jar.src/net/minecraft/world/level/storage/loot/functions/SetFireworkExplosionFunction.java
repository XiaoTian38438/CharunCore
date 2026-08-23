/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.FireworkExplosion;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetFireworkExplosionFunction extends LootItemConditionalFunction {
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and(paramInstance.group((App)FireworkExplosion.Shape.CODEC.optionalFieldOf("shape").forGetter(()), (App)FireworkExplosion.COLOR_LIST_CODEC.optionalFieldOf("colors").forGetter(()), (App)FireworkExplosion.COLOR_LIST_CODEC.optionalFieldOf("fade_colors").forGetter(()), (App)Codec.BOOL.optionalFieldOf("trail").forGetter(()), (App)Codec.BOOL.optionalFieldOf("twinkle").forGetter(()))).apply((Applicative)paramInstance, SetFireworkExplosionFunction::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final MapCodec<SetFireworkExplosionFunction> CODEC;
/*    */ 
/*    */   
/* 25 */   public static final FireworkExplosion DEFAULT_VALUE = new FireworkExplosion(FireworkExplosion.Shape.SMALL_BALL, IntList.of(), IntList.of(), false, false);
/*    */   
/*    */   final Optional<FireworkExplosion.Shape> shape;
/*    */   final Optional<IntList> colors;
/*    */   final Optional<IntList> fadeColors;
/*    */   final Optional<Boolean> trail;
/*    */   final Optional<Boolean> twinkle;
/*    */   
/*    */   public SetFireworkExplosionFunction(List<LootItemCondition> paramList, Optional<FireworkExplosion.Shape> paramOptional, Optional<IntList> paramOptional1, Optional<IntList> paramOptional2, Optional<Boolean> paramOptional3, Optional<Boolean> paramOptional4) {
/* 34 */     super(paramList);
/* 35 */     this.shape = paramOptional;
/* 36 */     this.colors = paramOptional1;
/* 37 */     this.fadeColors = paramOptional2;
/* 38 */     this.trail = paramOptional3;
/* 39 */     this.twinkle = paramOptional4;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 44 */     paramItemStack.update(DataComponents.FIREWORK_EXPLOSION, DEFAULT_VALUE, this::apply);
/* 45 */     return paramItemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   private FireworkExplosion apply(FireworkExplosion paramFireworkExplosion) {
/* 50 */     Objects.requireNonNull(paramFireworkExplosion);
/* 51 */     Objects.requireNonNull(paramFireworkExplosion);
/* 52 */     Objects.requireNonNull(paramFireworkExplosion);
/* 53 */     Objects.requireNonNull(paramFireworkExplosion);
/* 54 */     Objects.requireNonNull(paramFireworkExplosion); return new FireworkExplosion(this.shape.orElseGet(paramFireworkExplosion::shape), this.colors.orElseGet(paramFireworkExplosion::colors), this.fadeColors.orElseGet(paramFireworkExplosion::fadeColors), ((Boolean)this.trail.orElseGet(paramFireworkExplosion::hasTrail)).booleanValue(), ((Boolean)this.twinkle.orElseGet(paramFireworkExplosion::hasTwinkle)).booleanValue());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetFireworkExplosionFunction> getType() {
/* 60 */     return LootItemFunctions.SET_FIREWORK_EXPLOSION;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\SetFireworkExplosionFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */