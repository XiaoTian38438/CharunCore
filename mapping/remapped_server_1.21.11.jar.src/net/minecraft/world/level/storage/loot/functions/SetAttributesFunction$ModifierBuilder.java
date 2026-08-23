/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*    */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModifierBuilder
/*    */ {
/*    */   private final Identifier id;
/*    */   private final Holder<Attribute> attribute;
/*    */   private final AttributeModifier.Operation operation;
/*    */   private final NumberProvider amount;
/* 83 */   private final Set<EquipmentSlotGroup> slots = EnumSet.noneOf(EquipmentSlotGroup.class);
/*    */   
/*    */   public ModifierBuilder(Identifier paramIdentifier, Holder<Attribute> paramHolder, AttributeModifier.Operation paramOperation, NumberProvider paramNumberProvider) {
/* 86 */     this.id = paramIdentifier;
/* 87 */     this.attribute = paramHolder;
/* 88 */     this.operation = paramOperation;
/* 89 */     this.amount = paramNumberProvider;
/*    */   }
/*    */   
/*    */   public ModifierBuilder forSlot(EquipmentSlotGroup paramEquipmentSlotGroup) {
/* 93 */     this.slots.add(paramEquipmentSlotGroup);
/* 94 */     return this;
/*    */   }
/*    */   
/*    */   public SetAttributesFunction.Modifier build() {
/* 98 */     return new SetAttributesFunction.Modifier(this.id, this.attribute, this.operation, this.amount, List.copyOf(this.slots));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\SetAttributesFunction$ModifierBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */