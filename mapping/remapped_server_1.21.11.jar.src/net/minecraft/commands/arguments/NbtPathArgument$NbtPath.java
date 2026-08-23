/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.nbt.CollectionTag;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
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
/*     */ public class NbtPath
/*     */ {
/*     */   private final String original;
/*     */   private final Object2IntMap<NbtPathArgument.Node> nodeToOriginalPosition;
/*     */   private final NbtPathArgument.Node[] nodes;
/*     */   public static final Codec<NbtPath> CODEC;
/*     */   
/*     */   static {
/* 148 */     CODEC = Codec.STRING.comapFlatMap(paramString -> {
/*     */           try {
/*     */             NbtPath nbtPath = (new NbtPathArgument()).parse(new StringReader(paramString));
/*     */             
/*     */             return DataResult.success(nbtPath);
/* 153 */           } catch (CommandSyntaxException commandSyntaxException) {
/*     */             return DataResult.error(());
/*     */           } 
/*     */         }NbtPath::asString);
/*     */   }
/*     */ 
/*     */   
/*     */   public static NbtPath of(String paramString) throws CommandSyntaxException {
/* 161 */     return (new NbtPathArgument()).parse(new StringReader(paramString));
/*     */   }
/*     */   
/*     */   public NbtPath(String paramString, NbtPathArgument.Node[] paramArrayOfNode, Object2IntMap<NbtPathArgument.Node> paramObject2IntMap) {
/* 165 */     this.original = paramString;
/* 166 */     this.nodes = paramArrayOfNode;
/* 167 */     this.nodeToOriginalPosition = paramObject2IntMap;
/*     */   }
/*     */   
/*     */   public List<Tag> get(Tag paramTag) throws CommandSyntaxException {
/* 171 */     List<Tag> list = Collections.singletonList(paramTag);
/* 172 */     for (NbtPathArgument.Node node : this.nodes) {
/* 173 */       list = node.get(list);
/* 174 */       if (list.isEmpty()) {
/* 175 */         throw createNotFoundException(node);
/*     */       }
/*     */     } 
/* 178 */     return list;
/*     */   }
/*     */   
/*     */   public int countMatching(Tag paramTag) {
/* 182 */     List<Tag> list = Collections.singletonList(paramTag);
/* 183 */     for (NbtPathArgument.Node node : this.nodes) {
/* 184 */       list = node.get(list);
/* 185 */       if (list.isEmpty()) {
/* 186 */         return 0;
/*     */       }
/*     */     } 
/* 189 */     return list.size();
/*     */   }
/*     */   
/*     */   private List<Tag> getOrCreateParents(Tag paramTag) throws CommandSyntaxException {
/* 193 */     List<Tag> list = Collections.singletonList(paramTag);
/*     */     
/* 195 */     for (byte b = 0; b < this.nodes.length - 1; b++) {
/* 196 */       NbtPathArgument.Node node = this.nodes[b];
/* 197 */       int i = b + 1;
/* 198 */       Objects.requireNonNull(this.nodes[i]); list = node.getOrCreate(list, this.nodes[i]::createPreferredParentTag);
/* 199 */       if (list.isEmpty()) {
/* 200 */         throw createNotFoundException(node);
/*     */       }
/*     */     } 
/* 203 */     return list;
/*     */   }
/*     */   
/*     */   public List<Tag> getOrCreate(Tag paramTag, Supplier<Tag> paramSupplier) throws CommandSyntaxException {
/* 207 */     List<Tag> list = getOrCreateParents(paramTag);
/*     */     
/* 209 */     NbtPathArgument.Node node = this.nodes[this.nodes.length - 1];
/* 210 */     return node.getOrCreate(list, paramSupplier);
/*     */   }
/*     */   
/*     */   private static int apply(List<Tag> paramList, Function<Tag, Integer> paramFunction) {
/* 214 */     return ((Integer)paramList.stream().<Integer>map(paramFunction).reduce(Integer.valueOf(0), (paramInteger1, paramInteger2) -> Integer.valueOf(paramInteger1.intValue() + paramInteger2.intValue()))).intValue();
/*     */   }
/*     */   
/*     */   public static boolean isTooDeep(Tag paramTag, int paramInt) {
/* 218 */     if (paramInt >= 512) {
/* 219 */       return true;
/*     */     }
/* 221 */     if (paramTag instanceof CompoundTag) { CompoundTag compoundTag = (CompoundTag)paramTag;
/* 222 */       for (Tag tag : compoundTag.values()) {
/* 223 */         if (isTooDeep(tag, paramInt + 1)) {
/* 224 */           return true;
/*     */         }
/*     */       }  }
/* 227 */     else if (paramTag instanceof ListTag) { ListTag listTag = (ListTag)paramTag;
/* 228 */       for (Tag tag : listTag) {
/* 229 */         if (isTooDeep(tag, paramInt + 1)) {
/* 230 */           return true;
/*     */         }
/*     */       }  }
/*     */     
/* 234 */     return false;
/*     */   }
/*     */   
/*     */   public int set(Tag paramTag1, Tag paramTag2) throws CommandSyntaxException {
/* 238 */     if (isTooDeep(paramTag2, estimatePathDepth())) {
/* 239 */       throw NbtPathArgument.ERROR_DATA_TOO_DEEP.create();
/*     */     }
/* 241 */     Tag tag = paramTag2.copy();
/* 242 */     List<Tag> list = getOrCreateParents(paramTag1);
/* 243 */     if (list.isEmpty()) {
/* 244 */       return 0;
/*     */     }
/*     */     
/* 247 */     NbtPathArgument.Node node = this.nodes[this.nodes.length - 1];
/* 248 */     MutableBoolean mutableBoolean = new MutableBoolean(false);
/* 249 */     return apply(list, paramTag2 -> Integer.valueOf(paramNode.setTag(paramTag2, ())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int estimatePathDepth() {
/* 260 */     return this.nodes.length;
/*     */   }
/*     */   
/*     */   public int insert(int paramInt, CompoundTag paramCompoundTag, List<Tag> paramList) throws CommandSyntaxException {
/* 264 */     ArrayList<Tag> arrayList = new ArrayList(paramList.size());
/* 265 */     for (Tag tag1 : paramList) {
/* 266 */       Tag tag2 = tag1.copy();
/* 267 */       arrayList.add(tag2);
/* 268 */       if (isTooDeep(tag2, estimatePathDepth())) {
/* 269 */         throw NbtPathArgument.ERROR_DATA_TOO_DEEP.create();
/*     */       }
/*     */     } 
/* 272 */     List<Tag> list = getOrCreate((Tag)paramCompoundTag, ListTag::new);
/*     */     
/* 274 */     int i = 0;
/* 275 */     boolean bool = false;
/* 276 */     for (Tag tag : list) {
/* 277 */       CollectionTag collectionTag; if (tag instanceof CollectionTag) { collectionTag = (CollectionTag)tag; }
/* 278 */       else { throw NbtPathArgument.ERROR_EXPECTED_LIST.create(tag); }
/*     */ 
/*     */       
/* 281 */       boolean bool1 = false;
/* 282 */       int j = (paramInt < 0) ? (collectionTag.size() + paramInt + 1) : paramInt;
/* 283 */       for (Tag tag1 : arrayList) {
/*     */         try {
/* 285 */           if (collectionTag.addTag(j, bool ? tag1.copy() : tag1)) {
/* 286 */             j++;
/* 287 */             bool1 = true;
/*     */           } 
/* 289 */         } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
/* 290 */           throw NbtPathArgument.ERROR_INVALID_INDEX.create(Integer.valueOf(j));
/*     */         } 
/*     */       } 
/* 293 */       bool = true;
/* 294 */       i += bool1 ? 1 : 0;
/*     */     } 
/*     */     
/* 297 */     return i;
/*     */   }
/*     */   
/*     */   public int remove(Tag paramTag) {
/* 301 */     List<Tag> list = Collections.singletonList(paramTag);
/*     */     
/* 303 */     for (byte b = 0; b < this.nodes.length - 1; b++) {
/* 304 */       list = this.nodes[b].get(list);
/*     */     }
/*     */     
/* 307 */     NbtPathArgument.Node node = this.nodes[this.nodes.length - 1];
/* 308 */     Objects.requireNonNull(node); return apply(list, node::removeTag);
/*     */   }
/*     */   
/*     */   private CommandSyntaxException createNotFoundException(NbtPathArgument.Node paramNode) {
/* 312 */     int i = this.nodeToOriginalPosition.getInt(paramNode);
/* 313 */     return NbtPathArgument.ERROR_NOTHING_FOUND.create(this.original.substring(0, i));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 318 */     return this.original;
/*     */   }
/*     */   
/*     */   public String asString() {
/* 322 */     return this.original;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\arguments\NbtPathArgument$NbtPath.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */