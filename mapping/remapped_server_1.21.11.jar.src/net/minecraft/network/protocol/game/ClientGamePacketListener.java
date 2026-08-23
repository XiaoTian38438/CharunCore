/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.ConnectionProtocol;
/*    */ import net.minecraft.network.protocol.common.ClientCommonPacketListener;
/*    */ import net.minecraft.network.protocol.ping.ClientPongPacketListener;
/*    */ 
/*    */ public interface ClientGamePacketListener
/*    */   extends ClientCommonPacketListener, ClientPongPacketListener {
/*    */   default ConnectionProtocol protocol() {
/* 10 */     return ConnectionProtocol.PLAY;
/*    */   }
/*    */   
/*    */   void handleAddEntity(ClientboundAddEntityPacket paramClientboundAddEntityPacket);
/*    */   
/*    */   void handleAddObjective(ClientboundSetObjectivePacket paramClientboundSetObjectivePacket);
/*    */   
/*    */   void handleAnimate(ClientboundAnimatePacket paramClientboundAnimatePacket);
/*    */   
/*    */   void handleHurtAnimation(ClientboundHurtAnimationPacket paramClientboundHurtAnimationPacket);
/*    */   
/*    */   void handleAwardStats(ClientboundAwardStatsPacket paramClientboundAwardStatsPacket);
/*    */   
/*    */   void handleRecipeBookAdd(ClientboundRecipeBookAddPacket paramClientboundRecipeBookAddPacket);
/*    */   
/*    */   void handleRecipeBookRemove(ClientboundRecipeBookRemovePacket paramClientboundRecipeBookRemovePacket);
/*    */   
/*    */   void handleRecipeBookSettings(ClientboundRecipeBookSettingsPacket paramClientboundRecipeBookSettingsPacket);
/*    */   
/*    */   void handleBlockDestruction(ClientboundBlockDestructionPacket paramClientboundBlockDestructionPacket);
/*    */   
/*    */   void handleOpenSignEditor(ClientboundOpenSignEditorPacket paramClientboundOpenSignEditorPacket);
/*    */   
/*    */   void handleBlockEntityData(ClientboundBlockEntityDataPacket paramClientboundBlockEntityDataPacket);
/*    */   
/*    */   void handleBlockEvent(ClientboundBlockEventPacket paramClientboundBlockEventPacket);
/*    */   
/*    */   void handleBlockUpdate(ClientboundBlockUpdatePacket paramClientboundBlockUpdatePacket);
/*    */   
/*    */   void handleSystemChat(ClientboundSystemChatPacket paramClientboundSystemChatPacket);
/*    */   
/*    */   void handlePlayerChat(ClientboundPlayerChatPacket paramClientboundPlayerChatPacket);
/*    */   
/*    */   void handleDisguisedChat(ClientboundDisguisedChatPacket paramClientboundDisguisedChatPacket);
/*    */   
/*    */   void handleDeleteChat(ClientboundDeleteChatPacket paramClientboundDeleteChatPacket);
/*    */   
/*    */   void handleChunkBlocksUpdate(ClientboundSectionBlocksUpdatePacket paramClientboundSectionBlocksUpdatePacket);
/*    */   
/*    */   void handleMapItemData(ClientboundMapItemDataPacket paramClientboundMapItemDataPacket);
/*    */   
/*    */   void handleContainerClose(ClientboundContainerClosePacket paramClientboundContainerClosePacket);
/*    */   
/*    */   void handleContainerContent(ClientboundContainerSetContentPacket paramClientboundContainerSetContentPacket);
/*    */   
/*    */   void handleMountScreenOpen(ClientboundMountScreenOpenPacket paramClientboundMountScreenOpenPacket);
/*    */   
/*    */   void handleContainerSetData(ClientboundContainerSetDataPacket paramClientboundContainerSetDataPacket);
/*    */   
/*    */   void handleContainerSetSlot(ClientboundContainerSetSlotPacket paramClientboundContainerSetSlotPacket);
/*    */   
/*    */   void handleEntityEvent(ClientboundEntityEventPacket paramClientboundEntityEventPacket);
/*    */   
/*    */   void handleEntityLinkPacket(ClientboundSetEntityLinkPacket paramClientboundSetEntityLinkPacket);
/*    */   
/*    */   void handleSetEntityPassengersPacket(ClientboundSetPassengersPacket paramClientboundSetPassengersPacket);
/*    */   
/*    */   void handleExplosion(ClientboundExplodePacket paramClientboundExplodePacket);
/*    */   
/*    */   void handleGameEvent(ClientboundGameEventPacket paramClientboundGameEventPacket);
/*    */   
/*    */   void handleLevelChunkWithLight(ClientboundLevelChunkWithLightPacket paramClientboundLevelChunkWithLightPacket);
/*    */   
/*    */   void handleChunksBiomes(ClientboundChunksBiomesPacket paramClientboundChunksBiomesPacket);
/*    */   
/*    */   void handleForgetLevelChunk(ClientboundForgetLevelChunkPacket paramClientboundForgetLevelChunkPacket);
/*    */   
/*    */   void handleLevelEvent(ClientboundLevelEventPacket paramClientboundLevelEventPacket);
/*    */   
/*    */   void handleLogin(ClientboundLoginPacket paramClientboundLoginPacket);
/*    */   
/*    */   void handleMoveEntity(ClientboundMoveEntityPacket paramClientboundMoveEntityPacket);
/*    */   
/*    */   void handleMinecartAlongTrack(ClientboundMoveMinecartPacket paramClientboundMoveMinecartPacket);
/*    */   
/*    */   void handleMovePlayer(ClientboundPlayerPositionPacket paramClientboundPlayerPositionPacket);
/*    */   
/*    */   void handleRotatePlayer(ClientboundPlayerRotationPacket paramClientboundPlayerRotationPacket);
/*    */   
/*    */   void handleParticleEvent(ClientboundLevelParticlesPacket paramClientboundLevelParticlesPacket);
/*    */   
/*    */   void handlePlayerAbilities(ClientboundPlayerAbilitiesPacket paramClientboundPlayerAbilitiesPacket);
/*    */   
/*    */   void handlePlayerInfoRemove(ClientboundPlayerInfoRemovePacket paramClientboundPlayerInfoRemovePacket);
/*    */   
/*    */   void handlePlayerInfoUpdate(ClientboundPlayerInfoUpdatePacket paramClientboundPlayerInfoUpdatePacket);
/*    */   
/*    */   void handleRemoveEntities(ClientboundRemoveEntitiesPacket paramClientboundRemoveEntitiesPacket);
/*    */   
/*    */   void handleRemoveMobEffect(ClientboundRemoveMobEffectPacket paramClientboundRemoveMobEffectPacket);
/*    */   
/*    */   void handleRespawn(ClientboundRespawnPacket paramClientboundRespawnPacket);
/*    */   
/*    */   void handleRotateMob(ClientboundRotateHeadPacket paramClientboundRotateHeadPacket);
/*    */   
/*    */   void handleSetHeldSlot(ClientboundSetHeldSlotPacket paramClientboundSetHeldSlotPacket);
/*    */   
/*    */   void handleSetDisplayObjective(ClientboundSetDisplayObjectivePacket paramClientboundSetDisplayObjectivePacket);
/*    */   
/*    */   void handleSetEntityData(ClientboundSetEntityDataPacket paramClientboundSetEntityDataPacket);
/*    */   
/*    */   void handleSetEntityMotion(ClientboundSetEntityMotionPacket paramClientboundSetEntityMotionPacket);
/*    */   
/*    */   void handleSetEquipment(ClientboundSetEquipmentPacket paramClientboundSetEquipmentPacket);
/*    */   
/*    */   void handleSetExperience(ClientboundSetExperiencePacket paramClientboundSetExperiencePacket);
/*    */   
/*    */   void handleSetHealth(ClientboundSetHealthPacket paramClientboundSetHealthPacket);
/*    */   
/*    */   void handleSetPlayerTeamPacket(ClientboundSetPlayerTeamPacket paramClientboundSetPlayerTeamPacket);
/*    */   
/*    */   void handleSetScore(ClientboundSetScorePacket paramClientboundSetScorePacket);
/*    */   
/*    */   void handleResetScore(ClientboundResetScorePacket paramClientboundResetScorePacket);
/*    */   
/*    */   void handleSetSpawn(ClientboundSetDefaultSpawnPositionPacket paramClientboundSetDefaultSpawnPositionPacket);
/*    */   
/*    */   void handleSetTime(ClientboundSetTimePacket paramClientboundSetTimePacket);
/*    */   
/*    */   void handleSoundEvent(ClientboundSoundPacket paramClientboundSoundPacket);
/*    */   
/*    */   void handleSoundEntityEvent(ClientboundSoundEntityPacket paramClientboundSoundEntityPacket);
/*    */   
/*    */   void handleTakeItemEntity(ClientboundTakeItemEntityPacket paramClientboundTakeItemEntityPacket);
/*    */   
/*    */   void handleEntityPositionSync(ClientboundEntityPositionSyncPacket paramClientboundEntityPositionSyncPacket);
/*    */   
/*    */   void handleTeleportEntity(ClientboundTeleportEntityPacket paramClientboundTeleportEntityPacket);
/*    */   
/*    */   void handleTickingState(ClientboundTickingStatePacket paramClientboundTickingStatePacket);
/*    */   
/*    */   void handleTickingStep(ClientboundTickingStepPacket paramClientboundTickingStepPacket);
/*    */   
/*    */   void handleUpdateAttributes(ClientboundUpdateAttributesPacket paramClientboundUpdateAttributesPacket);
/*    */   
/*    */   void handleUpdateMobEffect(ClientboundUpdateMobEffectPacket paramClientboundUpdateMobEffectPacket);
/*    */   
/*    */   void handlePlayerCombatEnd(ClientboundPlayerCombatEndPacket paramClientboundPlayerCombatEndPacket);
/*    */   
/*    */   void handlePlayerCombatEnter(ClientboundPlayerCombatEnterPacket paramClientboundPlayerCombatEnterPacket);
/*    */   
/*    */   void handlePlayerCombatKill(ClientboundPlayerCombatKillPacket paramClientboundPlayerCombatKillPacket);
/*    */   
/*    */   void handleChangeDifficulty(ClientboundChangeDifficultyPacket paramClientboundChangeDifficultyPacket);
/*    */   
/*    */   void handleSetCamera(ClientboundSetCameraPacket paramClientboundSetCameraPacket);
/*    */   
/*    */   void handleInitializeBorder(ClientboundInitializeBorderPacket paramClientboundInitializeBorderPacket);
/*    */   
/*    */   void handleSetBorderLerpSize(ClientboundSetBorderLerpSizePacket paramClientboundSetBorderLerpSizePacket);
/*    */   
/*    */   void handleSetBorderSize(ClientboundSetBorderSizePacket paramClientboundSetBorderSizePacket);
/*    */   
/*    */   void handleSetBorderWarningDelay(ClientboundSetBorderWarningDelayPacket paramClientboundSetBorderWarningDelayPacket);
/*    */   
/*    */   void handleSetBorderWarningDistance(ClientboundSetBorderWarningDistancePacket paramClientboundSetBorderWarningDistancePacket);
/*    */   
/*    */   void handleSetBorderCenter(ClientboundSetBorderCenterPacket paramClientboundSetBorderCenterPacket);
/*    */   
/*    */   void handleTabListCustomisation(ClientboundTabListPacket paramClientboundTabListPacket);
/*    */   
/*    */   void handleBossUpdate(ClientboundBossEventPacket paramClientboundBossEventPacket);
/*    */   
/*    */   void handleItemCooldown(ClientboundCooldownPacket paramClientboundCooldownPacket);
/*    */   
/*    */   void handleMoveVehicle(ClientboundMoveVehiclePacket paramClientboundMoveVehiclePacket);
/*    */   
/*    */   void handleUpdateAdvancementsPacket(ClientboundUpdateAdvancementsPacket paramClientboundUpdateAdvancementsPacket);
/*    */   
/*    */   void handleSelectAdvancementsTab(ClientboundSelectAdvancementsTabPacket paramClientboundSelectAdvancementsTabPacket);
/*    */   
/*    */   void handlePlaceRecipe(ClientboundPlaceGhostRecipePacket paramClientboundPlaceGhostRecipePacket);
/*    */   
/*    */   void handleCommands(ClientboundCommandsPacket paramClientboundCommandsPacket);
/*    */   
/*    */   void handleStopSoundEvent(ClientboundStopSoundPacket paramClientboundStopSoundPacket);
/*    */   
/*    */   void handleCommandSuggestions(ClientboundCommandSuggestionsPacket paramClientboundCommandSuggestionsPacket);
/*    */   
/*    */   void handleUpdateRecipes(ClientboundUpdateRecipesPacket paramClientboundUpdateRecipesPacket);
/*    */   
/*    */   void handleLookAt(ClientboundPlayerLookAtPacket paramClientboundPlayerLookAtPacket);
/*    */   
/*    */   void handleTagQueryPacket(ClientboundTagQueryPacket paramClientboundTagQueryPacket);
/*    */   
/*    */   void handleLightUpdatePacket(ClientboundLightUpdatePacket paramClientboundLightUpdatePacket);
/*    */   
/*    */   void handleOpenBook(ClientboundOpenBookPacket paramClientboundOpenBookPacket);
/*    */   
/*    */   void handleOpenScreen(ClientboundOpenScreenPacket paramClientboundOpenScreenPacket);
/*    */   
/*    */   void handleMerchantOffers(ClientboundMerchantOffersPacket paramClientboundMerchantOffersPacket);
/*    */   
/*    */   void handleSetChunkCacheRadius(ClientboundSetChunkCacheRadiusPacket paramClientboundSetChunkCacheRadiusPacket);
/*    */   
/*    */   void handleSetSimulationDistance(ClientboundSetSimulationDistancePacket paramClientboundSetSimulationDistancePacket);
/*    */   
/*    */   void handleSetChunkCacheCenter(ClientboundSetChunkCacheCenterPacket paramClientboundSetChunkCacheCenterPacket);
/*    */   
/*    */   void handleBlockChangedAck(ClientboundBlockChangedAckPacket paramClientboundBlockChangedAckPacket);
/*    */   
/*    */   void setActionBarText(ClientboundSetActionBarTextPacket paramClientboundSetActionBarTextPacket);
/*    */   
/*    */   void setSubtitleText(ClientboundSetSubtitleTextPacket paramClientboundSetSubtitleTextPacket);
/*    */   
/*    */   void setTitleText(ClientboundSetTitleTextPacket paramClientboundSetTitleTextPacket);
/*    */   
/*    */   void setTitlesAnimation(ClientboundSetTitlesAnimationPacket paramClientboundSetTitlesAnimationPacket);
/*    */   
/*    */   void handleTitlesClear(ClientboundClearTitlesPacket paramClientboundClearTitlesPacket);
/*    */   
/*    */   void handleServerData(ClientboundServerDataPacket paramClientboundServerDataPacket);
/*    */   
/*    */   void handleCustomChatCompletions(ClientboundCustomChatCompletionsPacket paramClientboundCustomChatCompletionsPacket);
/*    */   
/*    */   void handleBundlePacket(ClientboundBundlePacket paramClientboundBundlePacket);
/*    */   
/*    */   void handleDamageEvent(ClientboundDamageEventPacket paramClientboundDamageEventPacket);
/*    */   
/*    */   void handleConfigurationStart(ClientboundStartConfigurationPacket paramClientboundStartConfigurationPacket);
/*    */   
/*    */   void handleChunkBatchStart(ClientboundChunkBatchStartPacket paramClientboundChunkBatchStartPacket);
/*    */   
/*    */   void handleChunkBatchFinished(ClientboundChunkBatchFinishedPacket paramClientboundChunkBatchFinishedPacket);
/*    */   
/*    */   void handleDebugSample(ClientboundDebugSamplePacket paramClientboundDebugSamplePacket);
/*    */   
/*    */   void handleProjectilePowerPacket(ClientboundProjectilePowerPacket paramClientboundProjectilePowerPacket);
/*    */   
/*    */   void handleSetCursorItem(ClientboundSetCursorItemPacket paramClientboundSetCursorItemPacket);
/*    */   
/*    */   void handleSetPlayerInventory(ClientboundSetPlayerInventoryPacket paramClientboundSetPlayerInventoryPacket);
/*    */   
/*    */   void handleTestInstanceBlockStatus(ClientboundTestInstanceBlockStatus paramClientboundTestInstanceBlockStatus);
/*    */   
/*    */   void handleWaypoint(ClientboundTrackedWaypointPacket paramClientboundTrackedWaypointPacket);
/*    */   
/*    */   void handleDebugChunkValue(ClientboundDebugChunkValuePacket paramClientboundDebugChunkValuePacket);
/*    */   
/*    */   void handleDebugBlockValue(ClientboundDebugBlockValuePacket paramClientboundDebugBlockValuePacket);
/*    */   
/*    */   void handleDebugEntityValue(ClientboundDebugEntityValuePacket paramClientboundDebugEntityValuePacket);
/*    */   
/*    */   void handleDebugEvent(ClientboundDebugEventPacket paramClientboundDebugEventPacket);
/*    */   
/*    */   void handleGameTestHighlightPos(ClientboundGameTestHighlightPosPacket paramClientboundGameTestHighlightPosPacket);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\game\ClientGamePacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */