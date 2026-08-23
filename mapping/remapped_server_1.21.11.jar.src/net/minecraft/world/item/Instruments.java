/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public interface Instruments
/*    */ {
/*    */   public static final int GOAT_HORN_RANGE_BLOCKS = 256;
/*    */   public static final float GOAT_HORN_DURATION = 7.0F;
/* 18 */   public static final ResourceKey<Instrument> PONDER_GOAT_HORN = create("ponder_goat_horn");
/* 19 */   public static final ResourceKey<Instrument> SING_GOAT_HORN = create("sing_goat_horn");
/* 20 */   public static final ResourceKey<Instrument> SEEK_GOAT_HORN = create("seek_goat_horn");
/* 21 */   public static final ResourceKey<Instrument> FEEL_GOAT_HORN = create("feel_goat_horn");
/* 22 */   public static final ResourceKey<Instrument> ADMIRE_GOAT_HORN = create("admire_goat_horn");
/* 23 */   public static final ResourceKey<Instrument> CALL_GOAT_HORN = create("call_goat_horn");
/* 24 */   public static final ResourceKey<Instrument> YEARN_GOAT_HORN = create("yearn_goat_horn");
/* 25 */   public static final ResourceKey<Instrument> DREAM_GOAT_HORN = create("dream_goat_horn");
/*    */   
/*    */   private static ResourceKey<Instrument> create(String paramString) {
/* 28 */     return ResourceKey.create(Registries.INSTRUMENT, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */   
/*    */   static void bootstrap(BootstrapContext<Instrument> paramBootstrapContext) {
/* 32 */     register(paramBootstrapContext, PONDER_GOAT_HORN, (Holder<SoundEvent>)SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(0), 7.0F, 256.0F);
/* 33 */     register(paramBootstrapContext, SING_GOAT_HORN, (Holder<SoundEvent>)SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(1), 7.0F, 256.0F);
/* 34 */     register(paramBootstrapContext, SEEK_GOAT_HORN, (Holder<SoundEvent>)SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(2), 7.0F, 256.0F);
/* 35 */     register(paramBootstrapContext, FEEL_GOAT_HORN, (Holder<SoundEvent>)SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(3), 7.0F, 256.0F);
/* 36 */     register(paramBootstrapContext, ADMIRE_GOAT_HORN, (Holder<SoundEvent>)SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(4), 7.0F, 256.0F);
/* 37 */     register(paramBootstrapContext, CALL_GOAT_HORN, (Holder<SoundEvent>)SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(5), 7.0F, 256.0F);
/* 38 */     register(paramBootstrapContext, YEARN_GOAT_HORN, (Holder<SoundEvent>)SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(6), 7.0F, 256.0F);
/* 39 */     register(paramBootstrapContext, DREAM_GOAT_HORN, (Holder<SoundEvent>)SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(7), 7.0F, 256.0F);
/*    */   }
/*    */   
/*    */   static void register(BootstrapContext<Instrument> paramBootstrapContext, ResourceKey<Instrument> paramResourceKey, Holder<SoundEvent> paramHolder, float paramFloat1, float paramFloat2) {
/* 43 */     MutableComponent mutableComponent = Component.translatable(Util.makeDescriptionId("instrument", paramResourceKey.identifier()));
/* 44 */     paramBootstrapContext.register(paramResourceKey, new Instrument(paramHolder, paramFloat1, paramFloat2, (Component)mutableComponent));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\Instruments.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */