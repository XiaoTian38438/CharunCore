/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.TooltipDisplay;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class ToggleTooltips extends LootItemConditionalFunction {
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and((App)Codec.unboundedMap(DataComponentType.CODEC, (Codec)Codec.BOOL).fieldOf("toggles").forGetter(())).apply((Applicative)paramInstance, ToggleTooltips::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<ToggleTooltips> CODEC;
/*    */   private final Map<DataComponentType<?>, Boolean> values;
/*    */   
/*    */   private ToggleTooltips(List<LootItemCondition> paramList, Map<DataComponentType<?>, Boolean> paramMap) {
/* 24 */     super(paramList);
/* 25 */     this.values = paramMap;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 30 */     paramItemStack.update(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT, paramTooltipDisplay -> {
/*    */           for (Map.Entry<DataComponentType<?>, Boolean> entry : this.values.entrySet()) {
/*    */             boolean bool = ((Boolean)entry.getValue()).booleanValue();
/*    */             paramTooltipDisplay = paramTooltipDisplay.withHidden((DataComponentType)entry.getKey(), !bool);
/*    */           } 
/*    */           return paramTooltipDisplay;
/*    */         });
/* 37 */     return paramItemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<ToggleTooltips> getType() {
/* 42 */     return LootItemFunctions.TOGGLE_TOOLTIPS;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\ToggleTooltips.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */