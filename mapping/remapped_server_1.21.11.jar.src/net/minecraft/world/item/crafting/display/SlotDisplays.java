/*    */ package net.minecraft.world.item.crafting.display;
/*    */ 
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class SlotDisplays {
/*    */   public static SlotDisplay.Type<?> bootstrap(Registry<SlotDisplay.Type<?>> paramRegistry) {
/*  7 */     Registry.register(paramRegistry, "empty", SlotDisplay.Empty.TYPE);
/*  8 */     Registry.register(paramRegistry, "any_fuel", SlotDisplay.AnyFuel.TYPE);
/*  9 */     Registry.register(paramRegistry, "item", SlotDisplay.ItemSlotDisplay.TYPE);
/* 10 */     Registry.register(paramRegistry, "item_stack", SlotDisplay.ItemStackSlotDisplay.TYPE);
/* 11 */     Registry.register(paramRegistry, "tag", SlotDisplay.TagSlotDisplay.TYPE);
/* 12 */     Registry.register(paramRegistry, "smithing_trim", SlotDisplay.SmithingTrimDemoSlotDisplay.TYPE);
/* 13 */     Registry.register(paramRegistry, "with_remainder", SlotDisplay.WithRemainder.TYPE);
/* 14 */     return (SlotDisplay.Type)Registry.register(paramRegistry, "composite", SlotDisplay.Composite.TYPE);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\display\SlotDisplays.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */