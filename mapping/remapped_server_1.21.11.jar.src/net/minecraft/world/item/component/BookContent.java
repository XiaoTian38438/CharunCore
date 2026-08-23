package net.minecraft.world.item.component;

import java.util.List;
import net.minecraft.server.network.Filterable;

public interface BookContent<T, C> {
  List<Filterable<T>> pages();
  
  C withReplacedPages(List<Filterable<T>> paramList);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\component\BookContent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */