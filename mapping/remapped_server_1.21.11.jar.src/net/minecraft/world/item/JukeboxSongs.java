/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public interface JukeboxSongs {
/* 14 */   public static final ResourceKey<JukeboxSong> THIRTEEN = create("13");
/* 15 */   public static final ResourceKey<JukeboxSong> CAT = create("cat");
/* 16 */   public static final ResourceKey<JukeboxSong> BLOCKS = create("blocks");
/* 17 */   public static final ResourceKey<JukeboxSong> CHIRP = create("chirp");
/* 18 */   public static final ResourceKey<JukeboxSong> FAR = create("far");
/* 19 */   public static final ResourceKey<JukeboxSong> MALL = create("mall");
/* 20 */   public static final ResourceKey<JukeboxSong> MELLOHI = create("mellohi");
/* 21 */   public static final ResourceKey<JukeboxSong> STAL = create("stal");
/* 22 */   public static final ResourceKey<JukeboxSong> STRAD = create("strad");
/* 23 */   public static final ResourceKey<JukeboxSong> WARD = create("ward");
/* 24 */   public static final ResourceKey<JukeboxSong> ELEVEN = create("11");
/* 25 */   public static final ResourceKey<JukeboxSong> WAIT = create("wait");
/* 26 */   public static final ResourceKey<JukeboxSong> PIGSTEP = create("pigstep");
/* 27 */   public static final ResourceKey<JukeboxSong> OTHERSIDE = create("otherside");
/* 28 */   public static final ResourceKey<JukeboxSong> FIVE = create("5");
/* 29 */   public static final ResourceKey<JukeboxSong> RELIC = create("relic");
/* 30 */   public static final ResourceKey<JukeboxSong> PRECIPICE = create("precipice");
/* 31 */   public static final ResourceKey<JukeboxSong> CREATOR = create("creator");
/* 32 */   public static final ResourceKey<JukeboxSong> CREATOR_MUSIC_BOX = create("creator_music_box");
/* 33 */   public static final ResourceKey<JukeboxSong> TEARS = create("tears");
/* 34 */   public static final ResourceKey<JukeboxSong> LAVA_CHICKEN = create("lava_chicken");
/*    */   
/*    */   private static ResourceKey<JukeboxSong> create(String paramString) {
/* 37 */     return ResourceKey.create(Registries.JUKEBOX_SONG, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<JukeboxSong> paramBootstrapContext, ResourceKey<JukeboxSong> paramResourceKey, Holder.Reference<SoundEvent> paramReference, int paramInt1, int paramInt2) {
/* 41 */     paramBootstrapContext.register(paramResourceKey, new JukeboxSong((Holder<SoundEvent>)paramReference, (Component)Component.translatable(Util.makeDescriptionId("jukebox_song", paramResourceKey.identifier())), paramInt1, paramInt2));
/*    */   }
/*    */   
/*    */   static void bootstrap(BootstrapContext<JukeboxSong> paramBootstrapContext) {
/* 45 */     register(paramBootstrapContext, THIRTEEN, SoundEvents.MUSIC_DISC_13, 178, 1);
/* 46 */     register(paramBootstrapContext, CAT, SoundEvents.MUSIC_DISC_CAT, 185, 2);
/* 47 */     register(paramBootstrapContext, BLOCKS, SoundEvents.MUSIC_DISC_BLOCKS, 345, 3);
/* 48 */     register(paramBootstrapContext, CHIRP, SoundEvents.MUSIC_DISC_CHIRP, 185, 4);
/* 49 */     register(paramBootstrapContext, FAR, SoundEvents.MUSIC_DISC_FAR, 174, 5);
/* 50 */     register(paramBootstrapContext, MALL, SoundEvents.MUSIC_DISC_MALL, 197, 6);
/* 51 */     register(paramBootstrapContext, MELLOHI, SoundEvents.MUSIC_DISC_MELLOHI, 96, 7);
/* 52 */     register(paramBootstrapContext, STAL, SoundEvents.MUSIC_DISC_STAL, 150, 8);
/* 53 */     register(paramBootstrapContext, STRAD, SoundEvents.MUSIC_DISC_STRAD, 188, 9);
/* 54 */     register(paramBootstrapContext, WARD, SoundEvents.MUSIC_DISC_WARD, 251, 10);
/* 55 */     register(paramBootstrapContext, ELEVEN, SoundEvents.MUSIC_DISC_11, 71, 11);
/* 56 */     register(paramBootstrapContext, WAIT, SoundEvents.MUSIC_DISC_WAIT, 238, 12);
/* 57 */     register(paramBootstrapContext, PIGSTEP, SoundEvents.MUSIC_DISC_PIGSTEP, 149, 13);
/* 58 */     register(paramBootstrapContext, OTHERSIDE, SoundEvents.MUSIC_DISC_OTHERSIDE, 195, 14);
/* 59 */     register(paramBootstrapContext, FIVE, SoundEvents.MUSIC_DISC_5, 178, 15);
/* 60 */     register(paramBootstrapContext, RELIC, SoundEvents.MUSIC_DISC_RELIC, 218, 14);
/* 61 */     register(paramBootstrapContext, PRECIPICE, SoundEvents.MUSIC_DISC_PRECIPICE, 299, 13);
/* 62 */     register(paramBootstrapContext, CREATOR, SoundEvents.MUSIC_DISC_CREATOR, 176, 12);
/* 63 */     register(paramBootstrapContext, CREATOR_MUSIC_BOX, SoundEvents.MUSIC_DISC_CREATOR_MUSIC_BOX, 73, 11);
/* 64 */     register(paramBootstrapContext, TEARS, SoundEvents.MUSIC_DISC_TEARS, 175, 10);
/* 65 */     register(paramBootstrapContext, LAVA_CHICKEN, SoundEvents.MUSIC_DISC_LAVA_CHICKEN, 134, 9);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\JukeboxSongs.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */