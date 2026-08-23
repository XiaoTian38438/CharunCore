/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.ConnectionProtocol;
/*    */ import net.minecraft.network.protocol.common.ServerCommonPacketListener;
/*    */ import net.minecraft.network.protocol.ping.ServerPingPacketListener;
/*    */ 
/*    */ public interface ServerGamePacketListener
/*    */   extends ServerCommonPacketListener, ServerPingPacketListener {
/*    */   default ConnectionProtocol protocol() {
/* 10 */     return ConnectionProtocol.PLAY;
/*    */   }
/*    */   
/*    */   void handleAnimate(ServerboundSwingPacket paramServerboundSwingPacket);
/*    */   
/*    */   void handleChat(ServerboundChatPacket paramServerboundChatPacket);
/*    */   
/*    */   void handleChatCommand(ServerboundChatCommandPacket paramServerboundChatCommandPacket);
/*    */   
/*    */   void handleSignedChatCommand(ServerboundChatCommandSignedPacket paramServerboundChatCommandSignedPacket);
/*    */   
/*    */   void handleChatAck(ServerboundChatAckPacket paramServerboundChatAckPacket);
/*    */   
/*    */   void handleClientCommand(ServerboundClientCommandPacket paramServerboundClientCommandPacket);
/*    */   
/*    */   void handleContainerButtonClick(ServerboundContainerButtonClickPacket paramServerboundContainerButtonClickPacket);
/*    */   
/*    */   void handleContainerClick(ServerboundContainerClickPacket paramServerboundContainerClickPacket);
/*    */   
/*    */   void handlePlaceRecipe(ServerboundPlaceRecipePacket paramServerboundPlaceRecipePacket);
/*    */   
/*    */   void handleContainerClose(ServerboundContainerClosePacket paramServerboundContainerClosePacket);
/*    */   
/*    */   void handleInteract(ServerboundInteractPacket paramServerboundInteractPacket);
/*    */   
/*    */   void handleMovePlayer(ServerboundMovePlayerPacket paramServerboundMovePlayerPacket);
/*    */   
/*    */   void handlePlayerAbilities(ServerboundPlayerAbilitiesPacket paramServerboundPlayerAbilitiesPacket);
/*    */   
/*    */   void handlePlayerAction(ServerboundPlayerActionPacket paramServerboundPlayerActionPacket);
/*    */   
/*    */   void handlePlayerCommand(ServerboundPlayerCommandPacket paramServerboundPlayerCommandPacket);
/*    */   
/*    */   void handlePlayerInput(ServerboundPlayerInputPacket paramServerboundPlayerInputPacket);
/*    */   
/*    */   void handleSetCarriedItem(ServerboundSetCarriedItemPacket paramServerboundSetCarriedItemPacket);
/*    */   
/*    */   void handleSetCreativeModeSlot(ServerboundSetCreativeModeSlotPacket paramServerboundSetCreativeModeSlotPacket);
/*    */   
/*    */   void handleSignUpdate(ServerboundSignUpdatePacket paramServerboundSignUpdatePacket);
/*    */   
/*    */   void handleUseItemOn(ServerboundUseItemOnPacket paramServerboundUseItemOnPacket);
/*    */   
/*    */   void handleUseItem(ServerboundUseItemPacket paramServerboundUseItemPacket);
/*    */   
/*    */   void handleTeleportToEntityPacket(ServerboundTeleportToEntityPacket paramServerboundTeleportToEntityPacket);
/*    */   
/*    */   void handlePaddleBoat(ServerboundPaddleBoatPacket paramServerboundPaddleBoatPacket);
/*    */   
/*    */   void handleMoveVehicle(ServerboundMoveVehiclePacket paramServerboundMoveVehiclePacket);
/*    */   
/*    */   void handleAcceptTeleportPacket(ServerboundAcceptTeleportationPacket paramServerboundAcceptTeleportationPacket);
/*    */   
/*    */   void handleAcceptPlayerLoad(ServerboundPlayerLoadedPacket paramServerboundPlayerLoadedPacket);
/*    */   
/*    */   void handleRecipeBookSeenRecipePacket(ServerboundRecipeBookSeenRecipePacket paramServerboundRecipeBookSeenRecipePacket);
/*    */   
/*    */   void handleBundleItemSelectedPacket(ServerboundSelectBundleItemPacket paramServerboundSelectBundleItemPacket);
/*    */   
/*    */   void handleRecipeBookChangeSettingsPacket(ServerboundRecipeBookChangeSettingsPacket paramServerboundRecipeBookChangeSettingsPacket);
/*    */   
/*    */   void handleSeenAdvancements(ServerboundSeenAdvancementsPacket paramServerboundSeenAdvancementsPacket);
/*    */   
/*    */   void handleCustomCommandSuggestions(ServerboundCommandSuggestionPacket paramServerboundCommandSuggestionPacket);
/*    */   
/*    */   void handleSetCommandBlock(ServerboundSetCommandBlockPacket paramServerboundSetCommandBlockPacket);
/*    */   
/*    */   void handleSetCommandMinecart(ServerboundSetCommandMinecartPacket paramServerboundSetCommandMinecartPacket);
/*    */   
/*    */   void handlePickItemFromBlock(ServerboundPickItemFromBlockPacket paramServerboundPickItemFromBlockPacket);
/*    */   
/*    */   void handlePickItemFromEntity(ServerboundPickItemFromEntityPacket paramServerboundPickItemFromEntityPacket);
/*    */   
/*    */   void handleRenameItem(ServerboundRenameItemPacket paramServerboundRenameItemPacket);
/*    */   
/*    */   void handleSetBeaconPacket(ServerboundSetBeaconPacket paramServerboundSetBeaconPacket);
/*    */   
/*    */   void handleSetStructureBlock(ServerboundSetStructureBlockPacket paramServerboundSetStructureBlockPacket);
/*    */   
/*    */   void handleSetTestBlock(ServerboundSetTestBlockPacket paramServerboundSetTestBlockPacket);
/*    */   
/*    */   void handleTestInstanceBlockAction(ServerboundTestInstanceBlockActionPacket paramServerboundTestInstanceBlockActionPacket);
/*    */   
/*    */   void handleSelectTrade(ServerboundSelectTradePacket paramServerboundSelectTradePacket);
/*    */   
/*    */   void handleEditBook(ServerboundEditBookPacket paramServerboundEditBookPacket);
/*    */   
/*    */   void handleEntityTagQuery(ServerboundEntityTagQueryPacket paramServerboundEntityTagQueryPacket);
/*    */   
/*    */   void handleContainerSlotStateChanged(ServerboundContainerSlotStateChangedPacket paramServerboundContainerSlotStateChangedPacket);
/*    */   
/*    */   void handleBlockEntityTagQuery(ServerboundBlockEntityTagQueryPacket paramServerboundBlockEntityTagQueryPacket);
/*    */   
/*    */   void handleSetJigsawBlock(ServerboundSetJigsawBlockPacket paramServerboundSetJigsawBlockPacket);
/*    */   
/*    */   void handleJigsawGenerate(ServerboundJigsawGeneratePacket paramServerboundJigsawGeneratePacket);
/*    */   
/*    */   void handleChangeDifficulty(ServerboundChangeDifficultyPacket paramServerboundChangeDifficultyPacket);
/*    */   
/*    */   void handleChangeGameMode(ServerboundChangeGameModePacket paramServerboundChangeGameModePacket);
/*    */   
/*    */   void handleLockDifficulty(ServerboundLockDifficultyPacket paramServerboundLockDifficultyPacket);
/*    */   
/*    */   void handleChatSessionUpdate(ServerboundChatSessionUpdatePacket paramServerboundChatSessionUpdatePacket);
/*    */   
/*    */   void handleConfigurationAcknowledged(ServerboundConfigurationAcknowledgedPacket paramServerboundConfigurationAcknowledgedPacket);
/*    */   
/*    */   void handleChunkBatchReceived(ServerboundChunkBatchReceivedPacket paramServerboundChunkBatchReceivedPacket);
/*    */   
/*    */   void handleDebugSubscriptionRequest(ServerboundDebugSubscriptionRequestPacket paramServerboundDebugSubscriptionRequestPacket);
/*    */   
/*    */   void handleClientTickEnd(ServerboundClientTickEndPacket paramServerboundClientTickEndPacket);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\game\ServerGamePacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */