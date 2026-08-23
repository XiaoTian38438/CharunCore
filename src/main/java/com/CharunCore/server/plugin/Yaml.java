package com.CharunCore.server.plugin;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 极简 YAML 子集解析器: 支持嵌套 map、列表、标量、注释。
 * 覆盖 plugin.yml / 常见 config.yml 的写法。
 */
public final class Yaml {

    private Yaml() {}

    public static Map<String, Object> parse(String text) {
        List<String[]> lines = new ArrayList<>();
        for (String raw : text.split("\n")) {
            String noComment = stripComment(raw);
            if (noComment.isBlank()) continue;
            int indent = indentOf(noComment);
            lines.add(new String[]{" ".repeat(indent), noComment.trim()});
        }
        Parser parser = new Parser(lines);
        return parser.parseMap(0);
    }

    private static String stripComment(String line) {
        StringBuilder sb = new StringBuilder();
        boolean inSingle = false, inDouble = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\'' && !inDouble) inSingle = !inSingle;
            else if (c == '"' && !inSingle) inDouble = !inDouble;
            else if (c == '#' && !inSingle && !inDouble
                    && (i == 0 || Character.isWhitespace(line.charAt(i - 1)))) {
                break;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    private static int indentOf(String line) {
        int n = 0;
        while (n < line.length() && (line.charAt(n) == ' ' || line.charAt(n) == '\t')) n++;
        if (n == 0 || line.charAt(n - 1) != '\t') return n;
        return (n / 4 + 1) * 4;
    }

    private static final class Parser {
        private final List<String[]> lines;
        private int pos = 0;

        Parser(List<String[]> lines) {
            this.lines = lines;
        }

        Map<String, Object> parseMap(int minIndent) {
            Map<String, Object> result = new LinkedHashMap<>();
            while (pos < lines.size()) {
                String[] line = lines.get(pos);
                int indent = line[0].length();
                if (indent < minIndent) break;
                String content = line[1];
                if (content.startsWith("- ")) continue;
                int colon = findColon(content);
                if (colon <= 0) { pos++; continue; }
                String key = unquote(content.substring(0, colon).trim());
                String rest = content.substring(colon + 1).trim();
                pos++;
                if (rest.isEmpty()) {
                    if (pos < lines.size()) {
                        String[] next = lines.get(pos);
                        if (next[0].length() > indent) {
                            if (next[1].startsWith("- ")) {
                                result.put(key, parseList(indent + 1));
                            } else {
                                result.put(key, parseMap(indent + 1));
                            }
                            continue;
                        }
                    }
                    result.put(key, new LinkedHashMap<String, Object>());
                } else {
                    result.put(key, scalar(rest));
                }
            }
            return result;
        }

        List<Object> parseList(int minIndent) {
            List<Object> result = new ArrayList<>();
            while (pos < lines.size()) {
                String[] line = lines.get(pos);
                int indent = line[0].length();
                if (indent < minIndent) break;
                String content = line[1];
                if (!content.startsWith("- ") && !content.equals("-")) break;
                String rest = content.equals("-") ? "" : content.substring(2).trim();
                pos++;
                if (rest.isEmpty()) {
                    if (pos < lines.size() && lines.get(pos)[0].length() > indent) {
                        String[] next = lines.get(pos);
                        if (next[1].startsWith("- ")) {
                            result.add(parseList(indent + 1));
                        } else {
                            result.add(parseMap(indent + 1));
                        }
                    } else {
                        result.add(null);
                    }
                } else {
                    int colon = findColon(rest);
                    if (colon > 0) {
                        Map<String, Object> inlineMap = new LinkedHashMap<>();
                        String key = unquote(rest.substring(0, colon).trim());
                        String value = rest.substring(colon + 1).trim();
                        if (value.isEmpty() && pos < lines.size() && lines.get(pos)[0].length() > indent
                                && !lines.get(pos)[1].startsWith("- ")) {
                            inlineMap.put(key, parseMap(indent + 1));
                        } else {
                            inlineMap.put(key, scalar(value));
                        }
                        while (pos < lines.size() && lines.get(pos)[0].length() > indent
                                && !lines.get(pos)[1].startsWith("- ")) {
                            String[] sub = lines.get(pos);
                            int c2 = findColon(sub[1]);
                            if (c2 > 0) {
                                String k2 = unquote(sub[1].substring(0, c2).trim());
                                String v2 = sub[1].substring(c2 + 1).trim();
                                pos++;
                                if (v2.isEmpty() && pos < lines.size() && lines.get(pos)[0].length() > sub[0].length()) {
                                    String[] n2 = lines.get(pos);
                                    if (n2[1].startsWith("- ")) inlineMap.put(k2, parseList(sub[0].length() + 1));
                                    else inlineMap.put(k2, parseMap(sub[0].length() + 1));
                                } else {
                                    inlineMap.put(k2, scalar(v2));
                                }
                            } else {
                                pos++;
                            }
                        }
                        result.add(inlineMap);
                    } else {
                        result.add(scalar(rest));
                    }
                }
            }
            return result;
        }

        private int findColon(String s) {
            boolean inSingle = false, inDouble = false;
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c == '\'' && !inDouble) inSingle = !inSingle;
                else if (c == '"' && !inSingle) inDouble = !inDouble;
                else if (c == ':' && !inSingle && !inDouble
                        && (i + 1 >= s.length() || Character.isWhitespace(s.charAt(i + 1)))) {
                    return i;
                }
            }
            return -1;
        }

        private static String unquote(String s) {
            return scalar(s).toString();
        }

        private static Object scalar(String s) {
            if (s.length() >= 2 && ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'")))) {
                return s.substring(1, s.length() - 1);
            }
            if (s.equals("true") || s.equals("yes")) return Boolean.TRUE;
            if (s.equals("false") || s.equals("no")) return Boolean.FALSE;
            if (s.equals("null") || s.equals("~")) return null;
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException ignored) {}
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException ignored) {}
            return s;
        }
    }

    public static String dump(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        dumpMap(sb, map, 0);
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private static void dumpMap(StringBuilder sb, Map<String, Object> map, int indent) {
        for (Map.Entry<String, Object> e : map.entrySet()) {
            sb.append("  ".repeat(indent)).append(e.getKey()).append(":");
            if (e.getValue() instanceof Map) {
                sb.append("\n");
                dumpMap(sb, (Map<String, Object>) e.getValue(), indent + 1);
            } else if (e.getValue() instanceof List<?> list) {
                if (list.isEmpty()) {
                    sb.append(" []\n");
                } else {
                    sb.append("\n");
                    dumpList(sb, list, indent + 1);
                }
            } else {
                sb.append(" ").append(formatScalar(e.getValue())).append("\n");
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void dumpList(StringBuilder sb, List<?> list, int indent) {
        for (Object o : list) {
            sb.append("  ".repeat(indent)).append("-");
            if (o instanceof Map) {
                Map<String, Object> m = (Map<String, Object>) o;
                if (m.isEmpty()) {
                    sb.append(" {}\n");
                    continue;
                }
                sb.append("\n");
                dumpMap(sb, m, indent + 1);
            } else if (o instanceof List<?> l) {
                sb.append("\n");
                dumpList(sb, l, indent + 1);
            } else {
                sb.append(" ").append(formatScalar(o)).append("\n");
            }
        }
    }

    private static String formatScalar(Object o) {
        if (o == null) return "null";
        if (o instanceof Boolean || o instanceof Number) return o.toString();
        String s = o.toString();
        if (s.contains(":") || s.contains("#") || s.contains("\n") || s.isBlank()) {
            return "'" + s.replace("'", "''") + "'";
        }
        return s;
    }
}
