/*     */ package net.minecraft.util.eventlog;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.time.LocalDate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileList
/*     */   implements Iterable<EventLogDirectory.File>
/*     */ {
/*     */   private final List<EventLogDirectory.File> files;
/*     */   
/*     */   FileList(List<EventLogDirectory.File> paramList) {
/* 130 */     this.files = new ArrayList<>(paramList);
/*     */   }
/*     */   
/*     */   public FileList prune(LocalDate paramLocalDate, int paramInt) {
/* 134 */     this.files.removeIf(paramFile -> {
/*     */           EventLogDirectory.FileId fileId = paramFile.id();
/*     */           LocalDate localDate = fileId.date().plusDays(paramInt);
/*     */           if (!paramLocalDate.isBefore(localDate)) {
/*     */             try {
/*     */               Files.delete(paramFile.path());
/*     */               return true;
/* 141 */             } catch (IOException iOException) {
/*     */               EventLogDirectory.LOGGER.warn("Failed to delete expired event log file: {}", paramFile.path(), iOException);
/*     */             } 
/*     */           }
/*     */           return false;
/*     */         });
/* 147 */     return this;
/*     */   }
/*     */   
/*     */   public FileList compressAll() {
/* 151 */     ListIterator<EventLogDirectory.File> listIterator = this.files.listIterator();
/* 152 */     while (listIterator.hasNext()) {
/* 153 */       EventLogDirectory.File file = listIterator.next();
/*     */       try {
/* 155 */         listIterator.set(file.compress());
/* 156 */       } catch (IOException iOException) {
/* 157 */         EventLogDirectory.LOGGER.warn("Failed to compress event log file: {}", file.path(), iOException);
/*     */       } 
/*     */     } 
/* 160 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<EventLogDirectory.File> iterator() {
/* 165 */     return this.files.iterator();
/*     */   }
/*     */   
/*     */   public Stream<EventLogDirectory.File> stream() {
/* 169 */     return this.files.stream();
/*     */   }
/*     */   
/*     */   public Set<EventLogDirectory.FileId> ids() {
/* 173 */     return (Set<EventLogDirectory.FileId>)this.files.stream().map(EventLogDirectory.File::id).collect(Collectors.toSet());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\eventlog\EventLogDirectory$FileList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */