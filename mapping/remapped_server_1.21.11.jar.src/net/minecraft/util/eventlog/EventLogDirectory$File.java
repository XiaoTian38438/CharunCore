package net.minecraft.util.eventlog;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;

public interface File {
  Path path();
  
  EventLogDirectory.FileId id();
  
  Reader openReader() throws IOException;
  
  EventLogDirectory.CompressedFile compress() throws IOException;
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\eventlog\EventLogDirectory$File.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */