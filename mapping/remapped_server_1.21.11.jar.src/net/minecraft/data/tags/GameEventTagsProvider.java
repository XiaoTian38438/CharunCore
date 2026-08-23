/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.GameEventTags;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*    */ 
/*    */ public class GameEventTagsProvider
/*    */   extends KeyTagProvider<GameEvent>
/*    */ {
/*    */   @VisibleForTesting
/* 18 */   static final List<ResourceKey<GameEvent>> VIBRATIONS_EXCEPT_FLAP = List.of((ResourceKey<GameEvent>[])new ResourceKey[] { GameEvent.BLOCK_ATTACH
/* 19 */         .key(), GameEvent.BLOCK_CHANGE
/* 20 */         .key(), GameEvent.BLOCK_CLOSE
/* 21 */         .key(), GameEvent.BLOCK_DESTROY
/* 22 */         .key(), GameEvent.BLOCK_DETACH
/* 23 */         .key(), GameEvent.BLOCK_OPEN
/* 24 */         .key(), GameEvent.BLOCK_PLACE
/* 25 */         .key(), GameEvent.BLOCK_ACTIVATE
/* 26 */         .key(), GameEvent.BLOCK_DEACTIVATE
/* 27 */         .key(), GameEvent.CONTAINER_CLOSE
/* 28 */         .key(), GameEvent.CONTAINER_OPEN
/* 29 */         .key(), GameEvent.DRINK
/* 30 */         .key(), GameEvent.EAT
/* 31 */         .key(), GameEvent.ELYTRA_GLIDE
/* 32 */         .key(), GameEvent.ENTITY_DAMAGE
/* 33 */         .key(), GameEvent.ENTITY_DIE
/* 34 */         .key(), GameEvent.ENTITY_DISMOUNT
/* 35 */         .key(), GameEvent.ENTITY_INTERACT
/* 36 */         .key(), GameEvent.ENTITY_MOUNT
/* 37 */         .key(), GameEvent.ENTITY_PLACE
/* 38 */         .key(), GameEvent.ENTITY_ACTION
/* 39 */         .key(), GameEvent.EQUIP
/* 40 */         .key(), GameEvent.EXPLODE
/* 41 */         .key(), GameEvent.FLUID_PICKUP
/*    */         
/* 43 */         .key(), GameEvent.FLUID_PLACE
/* 44 */         .key(), GameEvent.HIT_GROUND
/* 45 */         .key(), GameEvent.INSTRUMENT_PLAY
/* 46 */         .key(), GameEvent.ITEM_INTERACT_FINISH
/* 47 */         .key(), GameEvent.LIGHTNING_STRIKE
/* 48 */         .key(), GameEvent.NOTE_BLOCK_PLAY
/* 49 */         .key(), GameEvent.PRIME_FUSE
/* 50 */         .key(), GameEvent.PROJECTILE_LAND
/* 51 */         .key(), GameEvent.PROJECTILE_SHOOT
/* 52 */         .key(), GameEvent.SHEAR
/* 53 */         .key(), GameEvent.SPLASH
/* 54 */         .key(), GameEvent.STEP
/* 55 */         .key(), GameEvent.SWIM
/* 56 */         .key(), GameEvent.TELEPORT
/* 57 */         .key(), GameEvent.UNEQUIP
/* 58 */         .key() });
/*    */ 
/*    */   
/*    */   public GameEventTagsProvider(PackOutput paramPackOutput, CompletableFuture<HolderLookup.Provider> paramCompletableFuture) {
/* 62 */     super(paramPackOutput, Registries.GAME_EVENT, paramCompletableFuture);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addTags(HolderLookup.Provider paramProvider) {
/* 67 */     tag(GameEventTags.VIBRATIONS).addAll(VIBRATIONS_EXCEPT_FLAP).addAll(VibrationSystem.RESONANCE_EVENTS).add(GameEvent.FLAP.key());
/*    */     
/* 69 */     tag(GameEventTags.SHRIEKER_CAN_LISTEN).add(GameEvent.SCULK_SENSOR_TENDRILS_CLICKING.key());
/*    */     
/* 71 */     tag(GameEventTags.WARDEN_CAN_LISTEN).addAll(VIBRATIONS_EXCEPT_FLAP).addAll(VibrationSystem.RESONANCE_EVENTS).add(GameEvent.SHRIEK.key()).addTag(GameEventTags.SHRIEKER_CAN_LISTEN);
/*    */     
/* 73 */     tag(GameEventTags.IGNORE_VIBRATIONS_SNEAKING).add((ResourceKey<GameEvent>[])new ResourceKey[] { GameEvent.HIT_GROUND
/* 74 */           .key(), GameEvent.PROJECTILE_SHOOT
/* 75 */           .key(), GameEvent.STEP
/* 76 */           .key(), GameEvent.SWIM
/* 77 */           .key(), GameEvent.ITEM_INTERACT_START
/* 78 */           .key(), GameEvent.ITEM_INTERACT_FINISH
/* 79 */           .key() });
/*    */ 
/*    */     
/* 82 */     tag(GameEventTags.ALLAY_CAN_LISTEN).add(GameEvent.NOTE_BLOCK_PLAY.key());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\tags\GameEventTagsProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */