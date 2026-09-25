$root = "C:\Users\tian_\Desktop\CharunCore"
$libs = @(
  "$root\buildlibs\netty-buffer-4.1.101.Final.jar","$root\buildlibs\netty-codec-4.1.101.Final.jar",
  "$root\buildlibs\netty-common-4.1.101.Final.jar","$root\buildlibs\netty-handler-4.1.101.Final.jar",
  "$root\buildlibs\netty-resolver-4.1.101.Final.jar","$root\buildlibs\netty-transport-4.1.101.Final.jar",
  "$root\buildlibs\netty-transport-native-unix-common-4.1.101.Final.jar")
$m2 = "C:\Users\tian_\.m2\repository"
$deps = @("org\cloudburstmc\nbt\3.0.2.Final\nbt-3.0.2.Final.jar","com\google\code\gson\gson\2.13.2\gson-2.13.2.jar",
  "com\mojang\datafixerupper\9.0.19\datafixerupper-9.0.19.jar",
  "it\unimi\dsi\fastutil\8.5.18\fastutil-8.5.18.jar","com\google\guava\guava\32.1.3-jre\guava-32.1.3-jre.jar",
  "org\joml\joml\1.10.5\joml-1.10.5.jar","org\apache\commons\commons-lang3\3.18.0\commons-lang3-3.18.0.jar",
  "com\mojang\logging\1.6.11\logging-1.6.11.jar","com\mojang\authlib\7.0.61\authlib-7.0.61.jar",
  "com\mojang\brigadier\1.3.10\brigadier-1.3.10.jar","org\apache\logging\log4j\log4j-api\2.22.1\log4j-api-2.22.1.jar",
  "org\apache\logging\log4j\log4j-core\2.22.1\log4j-core-2.22.1.jar","at\yawk\lz4\lz4-java\1.8.1\lz4-java-1.8.1.jar")
$cp = ($libs + ($deps | ForEach-Object { Join-Path $m2 $_ })) -join ";"
# source list file with Windows paths
$srcs = (Get-ChildItem -Path "$root\src\main\java" -Filter *.java -Recurse).FullName
$srcs | Out-File -Encoding ascii "$root\srcs.txt"
# build a cmd that uses @srcs.txt
$cmd = 'javac -cp "' + $cp + '" -d "' + $root + '\target\classes" @"' + $root + '\srcs.txt"'
("=== FRESH BUILD " + (Get-Date -Format "yyyy-MM-dd HH:mm:ss") + " ===") | Out-File -Encoding utf8 C:\tmp\full.log
("cp entries: " + ($libs.Count + $deps.Count) + "  srcs: " + $srcs.Count) | Out-File -Append -Encoding utf8 C:\tmp\full.log
cmd /c $cmd 2>&1 | Out-File -Append -Encoding utf8 C:\tmp\full.log
("=== done ===") | Out-File -Append -Encoding utf8 C:\tmp\full.log
