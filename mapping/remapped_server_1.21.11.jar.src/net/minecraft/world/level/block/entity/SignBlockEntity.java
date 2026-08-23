/*     */ package net.minecraft.world.level.block.entity;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.UnaryOperator;
/*     */ import net.minecraft.commands.CommandSource;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.network.FilteredText;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.SignBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class SignBlockEntity extends BlockEntity {
/*  39 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int MAX_TEXT_LINE_WIDTH = 90;
/*     */   private static final int TEXT_LINE_HEIGHT = 10;
/*     */   private static final boolean DEFAULT_IS_WAXED = false;
/*     */   private UUID playerWhoMayEdit;
/*     */   private SignText frontText;
/*     */   private SignText backText;
/*     */   private boolean isWaxed = false;
/*     */   
/*     */   public SignBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  50 */     this(BlockEntityType.SIGN, paramBlockPos, paramBlockState);
/*     */   }
/*     */   
/*     */   public SignBlockEntity(BlockEntityType<?> paramBlockEntityType, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  54 */     super(paramBlockEntityType, paramBlockPos, paramBlockState);
/*  55 */     this.frontText = createDefaultSignText();
/*  56 */     this.backText = createDefaultSignText();
/*     */   }
/*     */   
/*     */   protected SignText createDefaultSignText() {
/*  60 */     return new SignText();
/*     */   }
/*     */   
/*     */   public boolean isFacingFrontText(Player paramPlayer) {
/*  64 */     Block block = getBlockState().getBlock(); if (block instanceof SignBlock) { SignBlock signBlock = (SignBlock)block;
/*  65 */       Vec3 vec3 = signBlock.getSignHitboxCenterPosition(getBlockState());
/*  66 */       double d1 = paramPlayer.getX() - getBlockPos().getX() + vec3.x;
/*  67 */       double d2 = paramPlayer.getZ() - getBlockPos().getZ() + vec3.z;
/*     */       
/*  69 */       float f1 = signBlock.getYRotationDegrees(getBlockState());
/*  70 */       float f2 = (float)(Mth.atan2(d2, d1) * 57.2957763671875D) - 90.0F;
/*  71 */       return (Mth.degreesDifferenceAbs(f1, f2) <= 90.0F); }
/*     */     
/*  73 */     return false;
/*     */   }
/*     */   
/*     */   public SignText getText(boolean paramBoolean) {
/*  77 */     return paramBoolean ? this.frontText : this.backText;
/*     */   }
/*     */   
/*     */   public SignText getFrontText() {
/*  81 */     return this.frontText;
/*     */   }
/*     */   
/*     */   public SignText getBackText() {
/*  85 */     return this.backText;
/*     */   }
/*     */   
/*     */   public int getTextLineHeight() {
/*  89 */     return 10;
/*     */   }
/*     */   
/*     */   public int getMaxTextLineWidth() {
/*  93 */     return 90;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput paramValueOutput) {
/*  98 */     super.saveAdditional(paramValueOutput);
/*     */     
/* 100 */     paramValueOutput.store("front_text", SignText.DIRECT_CODEC, this.frontText);
/* 101 */     paramValueOutput.store("back_text", SignText.DIRECT_CODEC, this.backText);
/* 102 */     paramValueOutput.putBoolean("is_waxed", this.isWaxed);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput paramValueInput) {
/* 107 */     super.loadAdditional(paramValueInput);
/*     */     
/* 109 */     this.frontText = paramValueInput.read("front_text", SignText.DIRECT_CODEC).map(this::loadLines).orElseGet(SignText::new);
/* 110 */     this.backText = paramValueInput.read("back_text", SignText.DIRECT_CODEC).map(this::loadLines).orElseGet(SignText::new);
/* 111 */     this.isWaxed = paramValueInput.getBooleanOr("is_waxed", false);
/*     */   }
/*     */   
/*     */   private SignText loadLines(SignText paramSignText) {
/* 115 */     for (byte b = 0; b < 4; b++) {
/* 116 */       Component component1 = loadLine(paramSignText.getMessage(b, false));
/* 117 */       Component component2 = loadLine(paramSignText.getMessage(b, true));
/* 118 */       paramSignText = paramSignText.setMessage(b, component1, component2);
/*     */     } 
/* 120 */     return paramSignText;
/*     */   }
/*     */   
/*     */   private Component loadLine(Component paramComponent) {
/* 124 */     Level level = this.level; if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*     */       try {
/* 126 */         return (Component)ComponentUtils.updateForEntity(createCommandSourceStack((Player)null, serverLevel, this.worldPosition), paramComponent, null, 0);
/* 127 */       } catch (CommandSyntaxException commandSyntaxException) {} }
/*     */ 
/*     */     
/* 130 */     return paramComponent;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateSignText(Player paramPlayer, boolean paramBoolean, List<FilteredText> paramList) {
/* 135 */     if (isWaxed() || !paramPlayer.getUUID().equals(getPlayerWhoMayEdit()) || this.level == null) {
/* 136 */       LOGGER.warn("Player {} just tried to change non-editable sign", paramPlayer.getPlainTextName());
/*     */       
/*     */       return;
/*     */     } 
/* 140 */     updateText(paramSignText -> setMessages(paramPlayer, paramList, paramSignText), paramBoolean);
/* 141 */     setAllowedPlayerEditor((UUID)null);
/* 142 */     this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
/*     */   }
/*     */   
/*     */   public boolean updateText(UnaryOperator<SignText> paramUnaryOperator, boolean paramBoolean) {
/* 146 */     SignText signText = getText(paramBoolean);
/* 147 */     return setText(paramUnaryOperator.apply(signText), paramBoolean);
/*     */   }
/*     */   
/*     */   private SignText setMessages(Player paramPlayer, List<FilteredText> paramList, SignText paramSignText) {
/* 151 */     for (byte b = 0; b < paramList.size(); b++) {
/* 152 */       FilteredText filteredText = paramList.get(b);
/* 153 */       Style style = paramSignText.getMessage(b, paramPlayer.isTextFilteringEnabled()).getStyle();
/* 154 */       if (paramPlayer.isTextFilteringEnabled()) {
/*     */         
/* 156 */         paramSignText = paramSignText.setMessage(b, (Component)Component.literal(filteredText.filteredOrEmpty()).setStyle(style));
/*     */       } else {
/* 158 */         paramSignText = paramSignText.setMessage(b, (Component)Component.literal(filteredText.raw()).setStyle(style), (Component)Component.literal(filteredText.filteredOrEmpty()).setStyle(style));
/*     */       } 
/*     */     } 
/* 161 */     return paramSignText;
/*     */   }
/*     */   
/*     */   public boolean setText(SignText paramSignText, boolean paramBoolean) {
/* 165 */     return paramBoolean ? setFrontText(paramSignText) : setBackText(paramSignText);
/*     */   }
/*     */   
/*     */   private boolean setBackText(SignText paramSignText) {
/* 169 */     if (paramSignText != this.backText) {
/* 170 */       this.backText = paramSignText;
/* 171 */       markUpdated();
/* 172 */       return true;
/*     */     } 
/* 174 */     return false;
/*     */   }
/*     */   
/*     */   private boolean setFrontText(SignText paramSignText) {
/* 178 */     if (paramSignText != this.frontText) {
/* 179 */       this.frontText = paramSignText;
/* 180 */       markUpdated();
/* 181 */       return true;
/*     */     } 
/* 183 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canExecuteClickCommands(boolean paramBoolean, Player paramPlayer) {
/* 187 */     return (isWaxed() && getText(paramBoolean).hasAnyClickCommands(paramPlayer));
/*     */   }
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
/*     */   public boolean executeClickCommandsIfPresent(ServerLevel paramServerLevel, Player paramPlayer, BlockPos paramBlockPos, boolean paramBoolean) {
/*     */     // Byte code:
/*     */     //   0: iconst_0
/*     */     //   1: istore #5
/*     */     //   3: aload_0
/*     */     //   4: iload #4
/*     */     //   6: invokevirtual getText : (Z)Lnet/minecraft/world/level/block/entity/SignText;
/*     */     //   9: aload_2
/*     */     //   10: invokevirtual isTextFilteringEnabled : ()Z
/*     */     //   13: invokevirtual getMessages : (Z)[Lnet/minecraft/network/chat/Component;
/*     */     //   16: astore #6
/*     */     //   18: aload #6
/*     */     //   20: arraylength
/*     */     //   21: istore #7
/*     */     //   23: iconst_0
/*     */     //   24: istore #8
/*     */     //   26: iload #8
/*     */     //   28: iload #7
/*     */     //   30: if_icmpge -> 196
/*     */     //   33: aload #6
/*     */     //   35: iload #8
/*     */     //   37: aaload
/*     */     //   38: astore #9
/*     */     //   40: aload #9
/*     */     //   42: invokeinterface getStyle : ()Lnet/minecraft/network/chat/Style;
/*     */     //   47: astore #10
/*     */     //   49: aload #10
/*     */     //   51: invokevirtual getClickEvent : ()Lnet/minecraft/network/chat/ClickEvent;
/*     */     //   54: astore #11
/*     */     //   56: aload #11
/*     */     //   58: astore #12
/*     */     //   60: iconst_0
/*     */     //   61: istore #13
/*     */     //   63: aload #12
/*     */     //   65: iload #13
/*     */     //   67: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   72: tableswitch default -> 190, -1 -> 190, 0 -> 104, 1 -> 138, 2 -> 160
/*     */     //   104: aload #12
/*     */     //   106: checkcast net/minecraft/network/chat/ClickEvent$RunCommand
/*     */     //   109: astore #14
/*     */     //   111: aload_1
/*     */     //   112: invokevirtual getServer : ()Lnet/minecraft/server/MinecraftServer;
/*     */     //   115: invokevirtual getCommands : ()Lnet/minecraft/commands/Commands;
/*     */     //   118: aload_2
/*     */     //   119: aload_1
/*     */     //   120: aload_3
/*     */     //   121: invokestatic createCommandSourceStack : (Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/commands/CommandSourceStack;
/*     */     //   124: aload #14
/*     */     //   126: invokevirtual command : ()Ljava/lang/String;
/*     */     //   129: invokevirtual performPrefixedCommand : (Lnet/minecraft/commands/CommandSourceStack;Ljava/lang/String;)V
/*     */     //   132: iconst_1
/*     */     //   133: istore #5
/*     */     //   135: goto -> 190
/*     */     //   138: aload #12
/*     */     //   140: checkcast net/minecraft/network/chat/ClickEvent$ShowDialog
/*     */     //   143: astore #15
/*     */     //   145: aload_2
/*     */     //   146: aload #15
/*     */     //   148: invokevirtual dialog : ()Lnet/minecraft/core/Holder;
/*     */     //   151: invokevirtual openDialog : (Lnet/minecraft/core/Holder;)V
/*     */     //   154: iconst_1
/*     */     //   155: istore #5
/*     */     //   157: goto -> 190
/*     */     //   160: aload #12
/*     */     //   162: checkcast net/minecraft/network/chat/ClickEvent$Custom
/*     */     //   165: astore #16
/*     */     //   167: aload_1
/*     */     //   168: invokevirtual getServer : ()Lnet/minecraft/server/MinecraftServer;
/*     */     //   171: aload #16
/*     */     //   173: invokevirtual id : ()Lnet/minecraft/resources/Identifier;
/*     */     //   176: aload #16
/*     */     //   178: invokevirtual payload : ()Ljava/util/Optional;
/*     */     //   181: invokevirtual handleCustomClickAction : (Lnet/minecraft/resources/Identifier;Ljava/util/Optional;)V
/*     */     //   184: iconst_1
/*     */     //   185: istore #5
/*     */     //   187: goto -> 190
/*     */     //   190: iinc #8, 1
/*     */     //   193: goto -> 26
/*     */     //   196: iload #5
/*     */     //   198: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #191	-> 0
/*     */     //   #192	-> 3
/*     */     //   #193	-> 40
/*     */     //   #194	-> 49
/*     */     //   #195	-> 56
/*     */     //   #196	-> 104
/*     */     //   #197	-> 111
/*     */     //   #198	-> 132
/*     */     //   #199	-> 135
/*     */     //   #200	-> 138
/*     */     //   #201	-> 145
/*     */     //   #202	-> 154
/*     */     //   #203	-> 157
/*     */     //   #204	-> 160
/*     */     //   #205	-> 167
/*     */     //   #206	-> 184
/*     */     //   #207	-> 187
/*     */     //   #192	-> 190
/*     */     //   #212	-> 196
/*     */   }
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
/*     */   private static CommandSourceStack createCommandSourceStack(Player paramPlayer, ServerLevel paramServerLevel, BlockPos paramBlockPos) {
/* 216 */     String str = (paramPlayer == null) ? "Sign" : paramPlayer.getPlainTextName();
/* 217 */     Component component = (Component)((paramPlayer == null) ? Component.literal("Sign") : paramPlayer.getDisplayName());
/* 218 */     return new CommandSourceStack(CommandSource.NULL, Vec3.atCenterOf((Vec3i)paramBlockPos), Vec2.ZERO, paramServerLevel, (PermissionSet)LevelBasedPermissionSet.GAMEMASTER, str, component, paramServerLevel.getServer(), (Entity)paramPlayer);
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/* 223 */     return ClientboundBlockEntityDataPacket.create(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider paramProvider) {
/* 228 */     return saveCustomOnly(paramProvider);
/*     */   }
/*     */   
/*     */   public void setAllowedPlayerEditor(UUID paramUUID) {
/* 232 */     this.playerWhoMayEdit = paramUUID;
/*     */   }
/*     */   
/*     */   public UUID getPlayerWhoMayEdit() {
/* 236 */     return this.playerWhoMayEdit;
/*     */   }
/*     */   
/*     */   private void markUpdated() {
/* 240 */     setChanged();
/* 241 */     this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
/*     */   }
/*     */   
/*     */   public boolean isWaxed() {
/* 245 */     return this.isWaxed;
/*     */   }
/*     */   
/*     */   public boolean setWaxed(boolean paramBoolean) {
/* 249 */     if (this.isWaxed != paramBoolean) {
/* 250 */       this.isWaxed = paramBoolean;
/* 251 */       markUpdated();
/* 252 */       return true;
/*     */     } 
/* 254 */     return false;
/*     */   }
/*     */   
/*     */   public boolean playerIsTooFarAwayToEdit(UUID paramUUID) {
/* 258 */     Player player = this.level.getPlayerByUUID(paramUUID);
/* 259 */     return (player == null || !player.isWithinBlockInteractionRange(getBlockPos(), 4.0D));
/*     */   }
/*     */   
/*     */   public static void tick(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, SignBlockEntity paramSignBlockEntity) {
/* 263 */     UUID uUID = paramSignBlockEntity.getPlayerWhoMayEdit();
/* 264 */     if (uUID != null) {
/* 265 */       paramSignBlockEntity.clearInvalidPlayerWhoMayEdit(paramSignBlockEntity, paramLevel, uUID);
/*     */     }
/*     */   }
/*     */   
/*     */   private void clearInvalidPlayerWhoMayEdit(SignBlockEntity paramSignBlockEntity, Level paramLevel, UUID paramUUID) {
/* 270 */     if (paramSignBlockEntity.playerIsTooFarAwayToEdit(paramUUID)) {
/* 271 */       paramSignBlockEntity.setAllowedPlayerEditor((UUID)null);
/*     */     }
/*     */   }
/*     */   
/*     */   public SoundEvent getSignInteractionFailedSoundEvent() {
/* 276 */     return SoundEvents.WAXED_SIGN_INTERACT_FAIL;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\SignBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */