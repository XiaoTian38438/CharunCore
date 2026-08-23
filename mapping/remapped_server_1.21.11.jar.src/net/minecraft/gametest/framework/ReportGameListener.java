/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import java.util.Locale;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
/*     */ import org.apache.commons.lang3.exception.ExceptionUtils;
/*     */ 
/*     */ 
/*     */ class ReportGameListener
/*     */   implements GameTestListener
/*     */ {
/*  21 */   private int attempts = 0;
/*  22 */   private int successes = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   public void testStructureLoaded(GameTestInfo paramGameTestInfo) {
/*  27 */     this.attempts++;
/*     */   }
/*     */   
/*     */   private void handleRetry(GameTestInfo paramGameTestInfo, GameTestRunner paramGameTestRunner, boolean paramBoolean) {
/*  31 */     RetryOptions retryOptions = paramGameTestInfo.retryOptions();
/*  32 */     String str1 = String.format(Locale.ROOT, "[Run: %4d, Ok: %4d, Fail: %4d", new Object[] { Integer.valueOf(this.attempts), Integer.valueOf(this.successes), Integer.valueOf(this.attempts - this.successes) });
/*  33 */     if (!retryOptions.unlimitedTries()) {
/*  34 */       str1 = str1 + str1;
/*     */     }
/*  36 */     str1 = str1 + "]";
/*  37 */     String str2 = String.valueOf(paramGameTestInfo.id()) + " " + String.valueOf(paramGameTestInfo.id()) + "! " + (paramBoolean ? "passed" : "failed") + "ms";
/*  38 */     String str3 = String.format(Locale.ROOT, "%-53s%s", new Object[] { str1, str2 });
/*  39 */     if (paramBoolean) {
/*  40 */       reportPassed(paramGameTestInfo, str3);
/*     */     } else {
/*  42 */       say(paramGameTestInfo.getLevel(), ChatFormatting.RED, str3);
/*     */     } 
/*     */     
/*  45 */     if (retryOptions.hasTriesLeft(this.attempts, this.successes)) {
/*  46 */       paramGameTestRunner.rerunTest(paramGameTestInfo);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void testPassed(GameTestInfo paramGameTestInfo, GameTestRunner paramGameTestRunner) {
/*  52 */     this.successes++;
/*  53 */     if (paramGameTestInfo.retryOptions().hasRetries()) {
/*  54 */       handleRetry(paramGameTestInfo, paramGameTestRunner, true);
/*     */       return;
/*     */     } 
/*  57 */     if (!paramGameTestInfo.isFlaky()) {
/*  58 */       reportPassed(paramGameTestInfo, String.valueOf(paramGameTestInfo.id()) + " passed! (" + String.valueOf(paramGameTestInfo.id()) + "ms / " + paramGameTestInfo.getRunTime() + "gameticks)");
/*     */       
/*     */       return;
/*     */     } 
/*  62 */     if (this.successes >= paramGameTestInfo.requiredSuccesses()) {
/*  63 */       reportPassed(paramGameTestInfo, String.valueOf(paramGameTestInfo) + " passed " + String.valueOf(paramGameTestInfo) + " times of " + this.successes + " attempts.");
/*     */     } else {
/*  65 */       say(paramGameTestInfo.getLevel(), ChatFormatting.GREEN, "Flaky test " + String.valueOf(paramGameTestInfo) + " succeeded, attempt: " + this.attempts + " successes: " + this.successes);
/*  66 */       paramGameTestRunner.rerunTest(paramGameTestInfo);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void testFailed(GameTestInfo paramGameTestInfo, GameTestRunner paramGameTestRunner) {
/*  72 */     if (!paramGameTestInfo.isFlaky()) {
/*  73 */       reportFailure(paramGameTestInfo, paramGameTestInfo.getError());
/*  74 */       if (paramGameTestInfo.retryOptions().hasRetries()) {
/*  75 */         handleRetry(paramGameTestInfo, paramGameTestRunner, false);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*  80 */     GameTestInstance gameTestInstance = paramGameTestInfo.getTest();
/*  81 */     String str = "Flaky test " + String.valueOf(paramGameTestInfo) + " failed, attempt: " + this.attempts + "/" + gameTestInstance.maxAttempts();
/*  82 */     if (gameTestInstance.requiredSuccesses() > 1) {
/*  83 */       str = str + ", successes: " + str + " (" + this.successes + " required)";
/*     */     }
/*  85 */     say(paramGameTestInfo.getLevel(), ChatFormatting.YELLOW, str);
/*  86 */     if (paramGameTestInfo.maxAttempts() - this.attempts + this.successes >= paramGameTestInfo.requiredSuccesses()) {
/*  87 */       paramGameTestRunner.rerunTest(paramGameTestInfo);
/*     */     } else {
/*  89 */       reportFailure(paramGameTestInfo, new ExhaustedAttemptsException(this.attempts, this.successes, paramGameTestInfo));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void testAddedForRerun(GameTestInfo paramGameTestInfo1, GameTestInfo paramGameTestInfo2, GameTestRunner paramGameTestRunner) {
/*  95 */     paramGameTestInfo2.addListener(this);
/*     */   }
/*     */   
/*     */   public static void reportPassed(GameTestInfo paramGameTestInfo, String paramString) {
/*  99 */     getTestInstanceBlockEntity(paramGameTestInfo).ifPresent(paramTestInstanceBlockEntity -> paramTestInstanceBlockEntity.setSuccess());
/* 100 */     visualizePassedTest(paramGameTestInfo, paramString);
/*     */   }
/*     */   
/*     */   private static void visualizePassedTest(GameTestInfo paramGameTestInfo, String paramString) {
/* 104 */     say(paramGameTestInfo.getLevel(), ChatFormatting.GREEN, paramString);
/*     */     
/* 106 */     GlobalTestReporter.onTestSuccess(paramGameTestInfo);
/*     */   }
/*     */   
/*     */   protected static void reportFailure(GameTestInfo paramGameTestInfo, Throwable paramThrowable) {
/*     */     MutableComponent mutableComponent;
/* 111 */     if (paramThrowable instanceof GameTestAssertException) { GameTestAssertException gameTestAssertException = (GameTestAssertException)paramThrowable;
/* 112 */       Component component = gameTestAssertException.getDescription(); }
/*     */     else
/* 114 */     { mutableComponent = Component.literal(Util.describeError(paramThrowable)); }
/*     */     
/* 116 */     getTestInstanceBlockEntity(paramGameTestInfo).ifPresent(paramTestInstanceBlockEntity -> paramTestInstanceBlockEntity.setErrorMessage(paramComponent));
/* 117 */     visualizeFailedTest(paramGameTestInfo, paramThrowable);
/*     */   }
/*     */   
/*     */   protected static void visualizeFailedTest(GameTestInfo paramGameTestInfo, Throwable paramThrowable) {
/* 121 */     String str1 = paramThrowable.getMessage() + paramThrowable.getMessage();
/* 122 */     String str2 = (paramGameTestInfo.isRequired() ? "" : "(optional) ") + (paramGameTestInfo.isRequired() ? "" : "(optional) ") + " failed! " + String.valueOf(paramGameTestInfo.id());
/*     */     
/* 124 */     say(paramGameTestInfo.getLevel(), paramGameTestInfo.isRequired() ? ChatFormatting.RED : ChatFormatting.YELLOW, str2);
/*     */     
/* 126 */     Throwable throwable = (Throwable)MoreObjects.firstNonNull(ExceptionUtils.getRootCause(paramThrowable), paramThrowable);
/* 127 */     if (throwable instanceof GameTestAssertPosException) { GameTestAssertPosException gameTestAssertPosException = (GameTestAssertPosException)throwable;
/* 128 */       paramGameTestInfo.getTestInstanceBlockEntity().markError(gameTestAssertPosException.getAbsolutePos(), gameTestAssertPosException.getMessageToShowAtBlock()); }
/*     */ 
/*     */     
/* 131 */     GlobalTestReporter.onTestFailed(paramGameTestInfo);
/*     */   }
/*     */   
/*     */   private static Optional<TestInstanceBlockEntity> getTestInstanceBlockEntity(GameTestInfo paramGameTestInfo) {
/* 135 */     ServerLevel serverLevel = paramGameTestInfo.getLevel();
/* 136 */     Optional<BlockPos> optional = Optional.ofNullable(paramGameTestInfo.getTestBlockPos());
/* 137 */     return (Optional)optional.flatMap(paramBlockPos -> paramServerLevel.getBlockEntity(paramBlockPos, BlockEntityType.TEST_INSTANCE_BLOCK));
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void say(ServerLevel paramServerLevel, ChatFormatting paramChatFormatting, String paramString) {
/* 142 */     paramServerLevel.getPlayers(paramServerPlayer -> true).forEach(paramServerPlayer -> paramServerPlayer.sendSystemMessage((Component)Component.literal(paramString).withStyle(paramChatFormatting)));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\ReportGameListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */