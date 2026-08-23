/*     */ package net.minecraft.world.level.block.entity.vault;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class VaultServerData {
/*     */   static {
/*  21 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)UUIDUtil.CODEC_LINKED_SET.lenientOptionalFieldOf("rewarded_players", Set.of()).forGetter(()), (App)Codec.LONG.lenientOptionalFieldOf("state_updating_resumes_at", Long.valueOf(0L)).forGetter(()), (App)ItemStack.CODEC.listOf().lenientOptionalFieldOf("items_to_eject", List.of()).forGetter(()), (App)Codec.INT.lenientOptionalFieldOf("total_ejections_needed", Integer.valueOf(0)).forGetter(())).apply((Applicative)paramInstance, VaultServerData::new));
/*     */   }
/*     */ 
/*     */   
/*     */   static final String TAG_NAME = "server_data";
/*     */   
/*     */   static Codec<VaultServerData> CODEC;
/*     */   
/*     */   private static final int MAX_REWARD_PLAYERS = 128;
/*  30 */   private final Set<UUID> rewardedPlayers = (Set<UUID>)new ObjectLinkedOpenHashSet();
/*     */   private long stateUpdatingResumesAt;
/*  32 */   private final List<ItemStack> itemsToEject = (List<ItemStack>)new ObjectArrayList();
/*     */   private long lastInsertFailTimestamp;
/*     */   private int totalEjectionsNeeded;
/*     */   boolean isDirty;
/*     */   
/*     */   VaultServerData(Set<UUID> paramSet, long paramLong, List<ItemStack> paramList, int paramInt) {
/*  38 */     this.rewardedPlayers.addAll(paramSet);
/*  39 */     this.stateUpdatingResumesAt = paramLong;
/*  40 */     this.itemsToEject.addAll(paramList);
/*  41 */     this.totalEjectionsNeeded = paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setLastInsertFailTimestamp(long paramLong) {
/*  48 */     this.lastInsertFailTimestamp = paramLong;
/*     */   }
/*     */   
/*     */   long getLastInsertFailTimestamp() {
/*  52 */     return this.lastInsertFailTimestamp;
/*     */   }
/*     */   
/*     */   Set<UUID> getRewardedPlayers() {
/*  56 */     return this.rewardedPlayers;
/*     */   }
/*     */   
/*     */   boolean hasRewardedPlayer(Player paramPlayer) {
/*  60 */     return this.rewardedPlayers.contains(paramPlayer.getUUID());
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void addToRewardedPlayers(Player paramPlayer) {
/*  65 */     this.rewardedPlayers.add(paramPlayer.getUUID());
/*     */     
/*  67 */     if (this.rewardedPlayers.size() > 128) {
/*  68 */       Iterator<UUID> iterator = this.rewardedPlayers.iterator();
/*  69 */       if (iterator.hasNext()) {
/*  70 */         iterator.next();
/*  71 */         iterator.remove();
/*     */       } 
/*     */     } 
/*     */     
/*  75 */     markChanged();
/*     */   }
/*     */   
/*     */   long stateUpdatingResumesAt() {
/*  79 */     return this.stateUpdatingResumesAt;
/*     */   }
/*     */   
/*     */   void pauseStateUpdatingUntil(long paramLong) {
/*  83 */     this.stateUpdatingResumesAt = paramLong;
/*  84 */     markChanged();
/*     */   }
/*     */   
/*     */   List<ItemStack> getItemsToEject() {
/*  88 */     return this.itemsToEject;
/*     */   }
/*     */   
/*     */   void markEjectionFinished() {
/*  92 */     this.totalEjectionsNeeded = 0;
/*  93 */     markChanged();
/*     */   }
/*     */   
/*     */   void setItemsToEject(List<ItemStack> paramList) {
/*  97 */     this.itemsToEject.clear();
/*  98 */     this.itemsToEject.addAll(paramList);
/*  99 */     this.totalEjectionsNeeded = this.itemsToEject.size();
/* 100 */     markChanged();
/*     */   }
/*     */   
/*     */   ItemStack getNextItemToEject() {
/* 104 */     if (this.itemsToEject.isEmpty()) {
/* 105 */       return ItemStack.EMPTY;
/*     */     }
/*     */     
/* 108 */     return Objects.<ItemStack>requireNonNullElse(this.itemsToEject.get(this.itemsToEject.size() - 1), ItemStack.EMPTY);
/*     */   }
/*     */   
/*     */   ItemStack popNextItemToEject() {
/* 112 */     if (this.itemsToEject.isEmpty()) {
/* 113 */       return ItemStack.EMPTY;
/*     */     }
/* 115 */     markChanged();
/*     */     
/* 117 */     return Objects.<ItemStack>requireNonNullElse(this.itemsToEject.remove(this.itemsToEject.size() - 1), ItemStack.EMPTY);
/*     */   }
/*     */   
/*     */   void set(VaultServerData paramVaultServerData) {
/* 121 */     this.stateUpdatingResumesAt = paramVaultServerData.stateUpdatingResumesAt();
/* 122 */     this.itemsToEject.clear();
/* 123 */     this.itemsToEject.addAll(paramVaultServerData.itemsToEject);
/* 124 */     this.rewardedPlayers.clear();
/* 125 */     this.rewardedPlayers.addAll(paramVaultServerData.rewardedPlayers);
/*     */   }
/*     */   
/*     */   private void markChanged() {
/* 129 */     this.isDirty = true;
/*     */   }
/*     */   
/*     */   public float ejectionProgress() {
/* 133 */     if (this.totalEjectionsNeeded == 1) {
/* 134 */       return 1.0F;
/*     */     }
/*     */     
/* 137 */     return 1.0F - Mth.inverseLerp(getItemsToEject().size(), 1.0F, this.totalEjectionsNeeded);
/*     */   }
/*     */   
/*     */   VaultServerData() {}
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\vault\VaultServerData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */