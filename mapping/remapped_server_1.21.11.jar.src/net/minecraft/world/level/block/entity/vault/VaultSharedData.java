/*    */ package net.minecraft.world.level.block.entity.vault;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Set;
/*    */ import java.util.UUID;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.UUIDUtil;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class VaultSharedData {
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)ItemStack.lenientOptionalFieldOf("display_item").forGetter(()), (App)UUIDUtil.CODEC_LINKED_SET.lenientOptionalFieldOf("connected_players", Set.of()).forGetter(()), (App)Codec.DOUBLE.lenientOptionalFieldOf("connected_particles_range", Double.valueOf(VaultConfig.DEFAULT.deactivationRange())).forGetter(())).apply((Applicative)paramInstance, VaultSharedData::new));
/*    */   }
/*    */ 
/*    */   
/*    */   static final String TAG_NAME = "shared_data";
/*    */   static Codec<VaultSharedData> CODEC;
/* 23 */   private ItemStack displayItem = ItemStack.EMPTY;
/* 24 */   private Set<UUID> connectedPlayers = (Set<UUID>)new ObjectLinkedOpenHashSet();
/* 25 */   private double connectedParticlesRange = VaultConfig.DEFAULT.deactivationRange();
/*    */   
/*    */   boolean isDirty;
/*    */   
/*    */   VaultSharedData(ItemStack paramItemStack, Set<UUID> paramSet, double paramDouble) {
/* 30 */     this.displayItem = paramItemStack;
/* 31 */     this.connectedPlayers.addAll(paramSet);
/* 32 */     this.connectedParticlesRange = paramDouble;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack getDisplayItem() {
/* 39 */     return this.displayItem;
/*    */   }
/*    */   
/*    */   public boolean hasDisplayItem() {
/* 43 */     return !this.displayItem.isEmpty();
/*    */   }
/*    */   
/*    */   public void setDisplayItem(ItemStack paramItemStack) {
/* 47 */     if (ItemStack.matches(this.displayItem, paramItemStack)) {
/*    */       return;
/*    */     }
/*    */     
/* 51 */     this.displayItem = paramItemStack.copy();
/* 52 */     markDirty();
/*    */   }
/*    */   
/*    */   boolean hasConnectedPlayers() {
/* 56 */     return !this.connectedPlayers.isEmpty();
/*    */   }
/*    */   
/*    */   Set<UUID> getConnectedPlayers() {
/* 60 */     return this.connectedPlayers;
/*    */   }
/*    */   
/*    */   double connectedParticlesRange() {
/* 64 */     return this.connectedParticlesRange;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void updateConnectedPlayersWithinRange(ServerLevel paramServerLevel, BlockPos paramBlockPos, VaultServerData paramVaultServerData, VaultConfig paramVaultConfig, double paramDouble) {
/* 71 */     Set<UUID> set = (Set)paramVaultConfig.playerDetector().detect(paramServerLevel, paramVaultConfig.entitySelector(), paramBlockPos, paramDouble, false).stream().filter(paramUUID -> !paramVaultServerData.getRewardedPlayers().contains(paramUUID)).collect(Collectors.toSet());
/*    */     
/* 73 */     if (!this.connectedPlayers.equals(set)) {
/* 74 */       this.connectedPlayers = set;
/* 75 */       markDirty();
/*    */     } 
/*    */   }
/*    */   
/*    */   private void markDirty() {
/* 80 */     this.isDirty = true;
/*    */   }
/*    */   
/*    */   void set(VaultSharedData paramVaultSharedData) {
/* 84 */     this.displayItem = paramVaultSharedData.displayItem;
/* 85 */     this.connectedPlayers = paramVaultSharedData.connectedPlayers;
/* 86 */     this.connectedParticlesRange = paramVaultSharedData.connectedParticlesRange;
/*    */   }
/*    */   
/*    */   VaultSharedData() {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\vault\VaultSharedData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */