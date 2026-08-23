/*    */ package net.minecraft.world.waypoints;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*    */ import net.minecraft.world.scores.PlayerTeam;
/*    */ 
/*    */ public interface Waypoint {
/* 24 */   public static final AttributeModifier WAYPOINT_TRANSMIT_RANGE_HIDE_MODIFIER = new AttributeModifier(
/* 25 */       Identifier.withDefaultNamespace("waypoint_transmit_range_hide"), -1.0D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
/*    */   
/*    */   public static final int MAX_RANGE = 60000000;
/*    */ 
/*    */   
/*    */   static Item.Properties addHideAttribute(Item.Properties paramProperties) {
/* 31 */     return paramProperties.component(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder().add(Attributes.WAYPOINT_TRANSMIT_RANGE, WAYPOINT_TRANSMIT_RANGE_HIDE_MODIFIER, EquipmentSlotGroup.HEAD, ItemAttributeModifiers.Display.hidden()).build());
/*    */   }
/*    */   
/*    */   public static class Icon { static {
/* 35 */       CODEC = RecordCodecBuilder.create(param1Instance -> param1Instance.group((App)ResourceKey.codec(WaypointStyleAssets.ROOT_ID).fieldOf("style").forGetter(()), (App)ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf("color").forGetter(())).apply((Applicative)param1Instance, Icon::new));
/*    */ 
/*    */ 
/*    */       
/* 39 */       STREAM_CODEC = StreamCodec.composite(
/* 40 */           ResourceKey.streamCodec(WaypointStyleAssets.ROOT_ID), param1Icon -> param1Icon.style, 
/* 41 */           ByteBufCodecs.optional(ByteBufCodecs.RGB_COLOR), param1Icon -> param1Icon.color, Icon::new);
/*    */     }
/*    */     public static final Codec<Icon> CODEC;
/*    */     public static final StreamCodec<ByteBuf, Icon> STREAM_CODEC;
/* 45 */     public static final Icon NULL = new Icon();
/*    */     
/* 47 */     public ResourceKey<WaypointStyleAsset> style = WaypointStyleAssets.DEFAULT;
/* 48 */     public Optional<Integer> color = Optional.empty();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     private Icon(ResourceKey<WaypointStyleAsset> param1ResourceKey, Optional<Integer> param1Optional) {
/* 54 */       this.style = param1ResourceKey;
/* 55 */       this.color = param1Optional;
/*    */     }
/*    */     
/*    */     public boolean hasData() {
/* 59 */       return (this.style != WaypointStyleAssets.DEFAULT || this.color.isPresent());
/*    */     }
/*    */     
/*    */     public Icon cloneAndAssignStyle(LivingEntity param1LivingEntity) {
/* 63 */       ResourceKey<WaypointStyleAsset> resourceKey = getOverrideStyle();
/*    */       
/* 65 */       Optional<Integer> optional = this.color.or(() -> Optional.<PlayerTeam>ofNullable(param1LivingEntity.getTeam()).map(()).map(()));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 71 */       if (resourceKey == this.style && optional.isEmpty()) {
/* 72 */         return this;
/*    */       }
/*    */       
/* 75 */       return new Icon(resourceKey, optional);
/*    */     }
/*    */     
/*    */     public void copyFrom(Icon param1Icon) {
/* 79 */       this.color = param1Icon.color;
/* 80 */       this.style = param1Icon.style;
/*    */     }
/*    */     
/*    */     private ResourceKey<WaypointStyleAsset> getOverrideStyle() {
/* 84 */       return (this.style != WaypointStyleAssets.DEFAULT) ? this.style : WaypointStyleAssets.DEFAULT;
/*    */     }
/*    */     
/*    */     public Icon() {} }
/*    */ 
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\waypoints\Waypoint.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */