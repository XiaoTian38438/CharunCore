/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2ByteMap;
/*    */ import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.TicketStorage;
/*    */ 
/*    */ public class SimulationChunkTracker
/*    */   extends ChunkTracker
/*    */ {
/*    */   public static final int MAX_LEVEL = 33;
/* 12 */   protected final Long2ByteMap chunks = (Long2ByteMap)new Long2ByteOpenHashMap();
/*    */   
/*    */   private final TicketStorage ticketStorage;
/*    */   
/*    */   public SimulationChunkTracker(TicketStorage paramTicketStorage) {
/* 17 */     super(34, 16, 256);
/* 18 */     this.ticketStorage = paramTicketStorage;
/* 19 */     paramTicketStorage.setSimulationChunkUpdatedListener(this::update);
/* 20 */     this.chunks.defaultReturnValue((byte)33);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getLevelFromSource(long paramLong) {
/* 25 */     return this.ticketStorage.getTicketLevelAt(paramLong, true);
/*    */   }
/*    */   
/*    */   public int getLevel(ChunkPos paramChunkPos) {
/* 29 */     return getLevel(paramChunkPos.toLong());
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getLevel(long paramLong) {
/* 34 */     return this.chunks.get(paramLong);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setLevel(long paramLong, int paramInt) {
/* 39 */     if (paramInt >= 33) {
/* 40 */       this.chunks.remove(paramLong);
/*    */     } else {
/* 42 */       this.chunks.put(paramLong, (byte)paramInt);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void runAllUpdates() {
/* 47 */     runUpdates(2147483647);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\SimulationChunkTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */