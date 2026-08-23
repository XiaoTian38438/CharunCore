/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.jtracy;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

class Loader {
    private final String name;

    Loader() {
        String string = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        String string2 = System.getProperty("os.arch").toLowerCase(Locale.ROOT);
        String string3 = "";
        String string4 = "jtracy-jni";
        String string5 = "";
        switch (string2) {
            case "amd64": 
            case "x86_64": 
            case "x86-64": {
                if (string.contains("win")) {
                    string5 = "-windows.dll";
                    break;
                }
                if (string.contains("mac") || string.contains("darwin")) {
                    string3 = "lib";
                    string5 = "-macos.dylib";
                    break;
                }
                if (string.contains("linux") || string.contains("unix")) {
                    string3 = "lib";
                    string5 = "-linux.so";
                    break;
                }
                throw new UnsatisfiedLinkError("Unsupported OS name: " + string + " / " + string2);
            }
            case "aarch64": {
                if (string.contains("mac") || string.contains("darwin")) {
                    string3 = "lib";
                    string5 = "-macos-arm64.dylib";
                    break;
                }
                throw new UnsatisfiedLinkError("Unsupported OS name: " + string + " / " + string2);
            }
            default: {
                throw new UnsatisfiedLinkError("Unsupported OS arch: " + string + " / " + string2);
            }
        }
        this.name = string3 + "jtracy-jni" + string5;
    }

    private Path createUnpackRoot() {
        Path path = Path.of(System.getProperty("java.io.tmpdir"), new String[0]).resolve("jtracy-" + String.valueOf(UUID.randomUUID()));
        try {
            Files.createDirectory(path, new FileAttribute[0]);
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return path;
    }

    public void load() {
        Path path = this.createUnpackRoot();
        try {
            Path path2 = this.unpackLibrary(path);
            System.load(path2.toAbsolutePath().toString());
        }
        catch (Throwable throwable) {
            try {
                Files.walkFileTree(path, Set.of(), 1, (FileVisitor<? super Path>)new SimpleFileVisitor<Path>(this){

                    @Override
                    public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
                        Files.delete(path);
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
            catch (IOException iOException) {
                // empty catch block
            }
            try {
                Files.deleteIfExists(path);
            }
            catch (IOException iOException) {
                // empty catch block
            }
            throw throwable;
        }
        try {
            Files.walkFileTree(path, Set.of(), 1, (FileVisitor<? super Path>)new /* invalid duplicate definition of identical inner class */);
        }
        catch (IOException iOException) {
            // empty catch block
        }
        try {
            Files.deleteIfExists(path);
        }
        catch (IOException iOException) {}
    }

    private Path unpackLibrary(Path path) {
        Path path2;
        block9: {
            InputStream inputStream = Loader.class.getClassLoader().getResourceAsStream(this.name);
            try {
                if (inputStream == null) {
                    throw new UnsatisfiedLinkError("Could not find jtracy natives at " + this.name);
                }
                Path path3 = Files.createTempFile(path, this.name, null, new FileAttribute[0]);
                Files.copy(inputStream, path3, StandardCopyOption.REPLACE_EXISTING);
                path2 = path3;
                if (inputStream == null) break block9;
            }
            catch (Throwable throwable) {
                try {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        }
                        catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                    }
                    throw throwable;
                }
                catch (IOException iOException) {
                    throw new LinkageError("Can't unpack jtracy natives found at " + this.name + " to " + String.valueOf(path), iOException);
                }
            }
            inputStream.close();
        }
        return path2;
    }
}

