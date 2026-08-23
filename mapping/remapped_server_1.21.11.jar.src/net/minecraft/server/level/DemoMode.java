/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class DemoMode
/*     */   extends ServerPlayerGameMode {
/*     */   public static final int DEMO_DAYS = 5;
/*     */   public static final int TOTAL_PLAY_TICKS = 120500;
/*     */   private boolean displayedIntro;
/*     */   private boolean demoHasEnded;
/*     */   private int demoEndedReminder;
/*     */   private int gameModeTicks;
/*     */   
/*     */   public DemoMode(ServerPlayer paramServerPlayer) {
/*  25 */     super(paramServerPlayer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  30 */     super.tick();
/*  31 */     this.gameModeTicks++;
/*     */     
/*  33 */     long l1 = this.level.getGameTime();
/*  34 */     long l2 = l1 / 24000L + 1L;
/*     */     
/*  36 */     if (!this.displayedIntro && this.gameModeTicks > 20) {
/*  37 */       this.displayedIntro = true;
/*  38 */       this.player.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 0.0F));
/*     */     } 
/*     */     
/*  41 */     this.demoHasEnded = (l1 > 120500L);
/*  42 */     if (this.demoHasEnded) {
/*  43 */       this.demoEndedReminder++;
/*     */     }
/*     */     
/*  46 */     if (l1 % 24000L == 500L) {
/*  47 */       if (l2 <= 6L) {
/*  48 */         if (l2 == 6L) {
/*  49 */           this.player.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 104.0F));
/*     */         } else {
/*  51 */           this.player.sendSystemMessage((Component)Component.translatable("demo.day." + l2));
/*     */         } 
/*     */       }
/*  54 */     } else if (l2 == 1L) {
/*  55 */       if (l1 == 100L) {
/*  56 */         this.player.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 101.0F));
/*  57 */       } else if (l1 == 175L) {
/*  58 */         this.player.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 102.0F));
/*  59 */       } else if (l1 == 250L) {
/*  60 */         this.player.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 103.0F));
/*     */       } 
/*  62 */     } else if (l2 == 5L && 
/*  63 */       l1 % 24000L == 22000L) {
/*  64 */       this.player.sendSystemMessage((Component)Component.translatable("demo.day.warning"));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void outputDemoReminder() {
/*  70 */     if (this.demoEndedReminder > 100) {
/*  71 */       this.player.sendSystemMessage((Component)Component.translatable("demo.reminder"));
/*  72 */       this.demoEndedReminder = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleBlockBreakAction(BlockPos paramBlockPos, ServerboundPlayerActionPacket.Action paramAction, Direction paramDirection, int paramInt1, int paramInt2) {
/*  78 */     if (this.demoHasEnded) {
/*  79 */       outputDemoReminder();
/*     */       return;
/*     */     } 
/*  82 */     super.handleBlockBreakAction(paramBlockPos, paramAction, paramDirection, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult useItem(ServerPlayer paramServerPlayer, Level paramLevel, ItemStack paramItemStack, InteractionHand paramInteractionHand) {
/*  87 */     if (this.demoHasEnded) {
/*  88 */       outputDemoReminder();
/*  89 */       return (InteractionResult)InteractionResult.PASS;
/*     */     } 
/*  91 */     return super.useItem(paramServerPlayer, paramLevel, paramItemStack, paramInteractionHand);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult useItemOn(ServerPlayer paramServerPlayer, Level paramLevel, ItemStack paramItemStack, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/*  96 */     if (this.demoHasEnded) {
/*  97 */       outputDemoReminder();
/*  98 */       return (InteractionResult)InteractionResult.PASS;
/*     */     } 
/* 100 */     return super.useItemOn(paramServerPlayer, paramLevel, paramItemStack, paramInteractionHand, paramBlockHitResult);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\DemoMode.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */