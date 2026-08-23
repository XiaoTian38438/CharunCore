/*     */ package net.minecraft.world.item.crafting.display;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.level.block.entity.FuelValues;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnyFuel
/*     */   implements SlotDisplay
/*     */ {
/*  97 */   public static final AnyFuel INSTANCE = new AnyFuel();
/*     */   
/*  99 */   public static final MapCodec<AnyFuel> MAP_CODEC = MapCodec.unit(INSTANCE);
/*     */   
/* 101 */   public static final StreamCodec<RegistryFriendlyByteBuf, AnyFuel> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*     */   
/* 103 */   public static final SlotDisplay.Type<AnyFuel> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SlotDisplay.Type<AnyFuel> type() {
/* 110 */     return TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 115 */     return "<any fuel>";
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Stream<T> resolve(ContextMap paramContextMap, DisplayContentsFactory<T> paramDisplayContentsFactory) {
/* 120 */     if (paramDisplayContentsFactory instanceof DisplayContentsFactory.ForStacks) { DisplayContentsFactory.ForStacks forStacks = (DisplayContentsFactory.ForStacks)paramDisplayContentsFactory;
/* 121 */       FuelValues fuelValues = (FuelValues)paramContextMap.getOptional(SlotDisplayContext.FUEL_VALUES);
/* 122 */       if (fuelValues != null) {
/* 123 */         Objects.requireNonNull(forStacks); return fuelValues.fuelItems().stream().map(forStacks::forStack);
/*     */       }  }
/*     */     
/* 126 */     return Stream.empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\display\SlotDisplay$AnyFuel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */