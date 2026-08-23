/*    */ package net.minecraft.world.waypoints;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.scores.PlayerTeam;
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
/*    */ public class Icon
/*    */ {
/*    */   public static final Codec<Icon> CODEC;
/*    */   public static final StreamCodec<ByteBuf, Icon> STREAM_CODEC;
/*    */   
/*    */   static {
/* 35 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)ResourceKey.codec(WaypointStyleAssets.ROOT_ID).fieldOf("style").forGetter(()), (App)ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf("color").forGetter(())).apply((Applicative)paramInstance, Icon::new));
/*    */ 
/*    */ 
/*    */     
/* 39 */     STREAM_CODEC = StreamCodec.composite(
/* 40 */         ResourceKey.streamCodec(WaypointStyleAssets.ROOT_ID), paramIcon -> paramIcon.style, 
/* 41 */         ByteBufCodecs.optional(ByteBufCodecs.RGB_COLOR), paramIcon -> paramIcon.color, Icon::new);
/*    */   }
/*    */ 
/*    */   
/* 45 */   public static final Icon NULL = new Icon();
/*    */   
/* 47 */   public ResourceKey<WaypointStyleAsset> style = WaypointStyleAssets.DEFAULT;
/* 48 */   public Optional<Integer> color = Optional.empty();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Icon(ResourceKey<WaypointStyleAsset> paramResourceKey, Optional<Integer> paramOptional) {
/* 54 */     this.style = paramResourceKey;
/* 55 */     this.color = paramOptional;
/*    */   }
/*    */   
/*    */   public boolean hasData() {
/* 59 */     return (this.style != WaypointStyleAssets.DEFAULT || this.color.isPresent());
/*    */   }
/*    */   
/*    */   public Icon cloneAndAssignStyle(LivingEntity paramLivingEntity) {
/* 63 */     ResourceKey<WaypointStyleAsset> resourceKey = getOverrideStyle();
/*    */     
/* 65 */     Optional<Integer> optional = this.color.or(() -> Optional.<PlayerTeam>ofNullable(paramLivingEntity.getTeam()).map(()).map(()));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 71 */     if (resourceKey == this.style && optional.isEmpty()) {
/* 72 */       return this;
/*    */     }
/*    */     
/* 75 */     return new Icon(resourceKey, optional);
/*    */   }
/*    */   
/*    */   public void copyFrom(Icon paramIcon) {
/* 79 */     this.color = paramIcon.color;
/* 80 */     this.style = paramIcon.style;
/*    */   }
/*    */   
/*    */   private ResourceKey<WaypointStyleAsset> getOverrideStyle() {
/* 84 */     return (this.style != WaypointStyleAssets.DEFAULT) ? this.style : WaypointStyleAssets.DEFAULT;
/*    */   }
/*    */   
/*    */   public Icon() {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\waypoints\Waypoint$Icon.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */