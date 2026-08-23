package com.CharunCore.server.worldgen.biome;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.CharunCore.server.worldgen.density.DensityFunction;

public final class Climate {

    public static final int PARAMETER_COUNT = 7;
    private static final float QUANTIZATION_FACTOR = 10000.0f;

    public static long quantizeCoord(float f) {
        return (long) (f * QUANTIZATION_FACTOR);
    }

    public static float unquantizeCoord(long l) {
        return (float) l / QUANTIZATION_FACTOR;
    }

    public static TargetPoint target(float t, float h, float c, float e, float d, float w) {
        return new TargetPoint(
            quantizeCoord(t), quantizeCoord(h), quantizeCoord(c),
            quantizeCoord(e), quantizeCoord(d), quantizeCoord(w)
        );
    }

    public record TargetPoint(long temperature, long humidity, long continentalness,
                              long erosion, long depth, long weirdness) {

        public long[] toParameterArray() {
            return new long[]{temperature, humidity, continentalness, erosion, depth, weirdness, 0L};
        }
    }

    public record Parameter(long min, long max) {

        public static Parameter point(float f) {
            return new Parameter(quantizeCoord(f), quantizeCoord(f));
        }

        public static Parameter span(float f1, float f2) {
            if (f1 > f2) throw new IllegalArgumentException("min > max: " + f1 + " " + f2);
            return new Parameter(quantizeCoord(f1), quantizeCoord(f2));
        }

        public static Parameter span(Parameter p1, Parameter p2) {
            if (p1.min() > p2.max()) throw new IllegalArgumentException("min > max: " + p1 + " " + p2);
            return new Parameter(p1.min(), p2.max());
        }

        public long distance(long l) {
            long d1 = l - this.max;
            long d2 = this.min - l;
            if (d1 > 0L) return d1;
            return Math.max(d2, 0L);
        }

        public long distance(Parameter p) {
            long d1 = p.min() - this.max;
            long d2 = this.min - p.max();
            if (d1 > 0L) return d1;
            return Math.max(d2, 0L);
        }

        public Parameter span(Parameter p) {
            if (p == null) return this;
            return new Parameter(Math.min(this.min, p.min()), Math.max(this.max, p.max()));
        }
    }

    public static ParameterPoint parameters(Parameter t, Parameter h, Parameter c, Parameter e, Parameter d, Parameter w, float offset) {
        return new ParameterPoint(t, h, c, e, d, w, quantizeCoord(offset));
    }

    public static ParameterPoint parameters(float t, float h, float c, float e, float d, float w, float offset) {
        return new ParameterPoint(Parameter.point(t), Parameter.point(h), Parameter.point(c),
            Parameter.point(e), Parameter.point(d), Parameter.point(w), quantizeCoord(offset));
    }

    public record ParameterPoint(Parameter temperature, Parameter humidity, Parameter continentalness,
                                  Parameter erosion, Parameter depth, Parameter weirdness, long offset) {

        public long fitness(TargetPoint target) {
            long sq = square(temperature.distance(target.temperature()));
            sq += square(humidity.distance(target.humidity()));
            sq += square(continentalness.distance(target.continentalness()));
            sq += square(erosion.distance(target.erosion()));
            sq += square(depth.distance(target.depth()));
            sq += square(weirdness.distance(target.weirdness()));
            sq += square(offset);
            return sq;
        }

        public List<Parameter> parameterSpace() {
            return List.of(temperature, humidity, continentalness, erosion, depth, weirdness, new Parameter(offset, offset));
        }

        private static long square(long v) { return v * v; }
    }

    public record Sampler(DensityFunction temperature, DensityFunction humidity,
                           DensityFunction continentalness, DensityFunction erosion,
                           DensityFunction depth, DensityFunction weirdness) {

        public TargetPoint sample(int quartX, int quartY, int quartZ) {
            int bx = quartX << 2;
            int by = quartY << 2;
            int bz = quartZ << 2;
            DensityFunction.SinglePointContext ctx = new DensityFunction.SinglePointContext(bx, by, bz);
            return target(
                (float) temperature.compute(ctx),
                (float) humidity.compute(ctx),
                (float) continentalness.compute(ctx),
                (float) erosion.compute(ctx),
                (float) depth.compute(ctx),
                (float) weirdness.compute(ctx)
            );
        }
    }

    public static class ParameterList<T> {
        private final List<SimpleEntry<ParameterPoint, T>> entries;
        private final RTree<T> index;

        public ParameterList(List<SimpleEntry<ParameterPoint, T>> entries) {
            this.entries = entries;
            this.index = RTree.create(entries);
        }

        public List<SimpleEntry<ParameterPoint, T>> entries() {
            return entries;
        }

        public T findValue(TargetPoint target) {
            // 原自研 RTree 最近邻存在剪枝 bug（会与暴力最近邻产生 0.02% 地表错判，
            // 导致雪原/海洋等边界错乱）。由于参数表仅约 1000 条且按列采样（非逐方块），
            // 暴力最近邻开销可忽略，且结果与原版 true-nearest 完全一致。故直接走暴力。
            return findValueBruteForce(target);
        }

        public T findValueBruteForce(TargetPoint target) {
            var it = entries.iterator();
            var first = it.next();
            long bestDist = first.getKey().fitness(target);
            T best = first.getValue();
            while (it.hasNext()) {
                var e = it.next();
                long d = e.getKey().fitness(target);
                if (d < bestDist) {
                    bestDist = d;
                    best = e.getValue();
                }
            }
            return best;
        }

        public T findValueIndex(TargetPoint target) {
            return index.search(target, RTree.Node::distance);
        }
    }

    @FunctionalInterface
    interface DistanceMetric<T> {
        long distance(RTree.Node<T> node, long[] query);
    }

    static final class RTree<T> {
        private static final int CHILDREN_PER_NODE = 6;
        private final Node<T> root;
        private final ThreadLocal<Leaf<T>> lastResult = new ThreadLocal<>();

        private RTree(Node<T> root) {
            this.root = root;
        }

        static <T> RTree<T> create(List<SimpleEntry<ParameterPoint, T>> list) {
            if (list.isEmpty()) {
                throw new IllegalArgumentException("Need at least one value to build the search tree.");
            }
            int n = list.get(0).getKey().parameterSpace().size();
            if (n != 7) {
                throw new IllegalStateException("Expecting parameter space to be 7, got " + n);
            }
            List<Leaf<T>> leaves = list.stream()
                .map(e -> new Leaf<>(e.getKey(), e.getValue()))
                .collect(Collectors.toCollection(ArrayList::new));
            return new RTree<>(build(n, leaves));
        }

        @SuppressWarnings("unchecked")
        private static <T> Node<T> build(int n, List<? extends Node<T>> list) {
            if (list.isEmpty()) {
                throw new IllegalStateException("Need at least one child to build a node");
            }
            if (list.size() == 1) {
                return list.get(0);
            }
            if (list.size() <= 6) {
                list.sort(Comparator.comparingLong(node -> {
                    long l = 0L;
                    for (int i = 0; i < n; i++) {
                        Parameter p = node.parameterSpace[i];
                        l += Math.abs((p.min() + p.max()) / 2L);
                    }
                    return l;
                }));
                return new SubTree<>(list);
            }
            long bestCost = Long.MAX_VALUE;
            int bestDim = -1;
            List<SubTree<T>> bestBuckets = null;
            for (int i = 0; i < n; i++) {
                sort(list, n, i, false);
                List<SubTree<T>> buckets = bucketize(list);
                long cost = 0L;
                for (SubTree<T> st : buckets) {
                    cost += cost(st.parameterSpace);
                }
                if (bestCost <= cost) continue;
                bestCost = cost;
                bestDim = i;
                bestBuckets = buckets;
            }
            sort(bestBuckets, n, bestDim, true);
            List<Node<T>> built = new ArrayList<>();
            for (SubTree<T> st : bestBuckets) {
                built.add(build(n, Arrays.asList(st.children)));
            }
            return new SubTree<>(built);
        }

        private static <T> void sort(List<? extends Node<T>> list, int n, int dim, boolean abs) {
            Comparator<Node<T>> cmp = comparator(dim, abs);
            for (int i = 1; i < n; i++) {
                cmp = cmp.thenComparing(comparator((dim + i) % n, abs));
            }
            list.sort(cmp);
        }

        private static <T> Comparator<Node<T>> comparator(int dim, boolean abs) {
            return Comparator.comparingLong(node -> {
                Parameter p = node.parameterSpace[dim];
                long mid = (p.min() + p.max()) / 2L;
                return abs ? Math.abs(mid) : mid;
            });
        }

        private static <T> List<SubTree<T>> bucketize(List<? extends Node<T>> list) {
            List<SubTree<T>> result = new ArrayList<>();
            List<Node<T>> current = new ArrayList<>();
            int bucketSize = (int) Math.pow(6.0, Math.floor(Math.log(list.size() - 0.01) / Math.log(6.0)));
            for (Node<T> node : list) {
                current.add(node);
                if (current.size() >= bucketSize) {
                    result.add(new SubTree<>(current));
                    current = new ArrayList<>();
                }
            }
            if (!current.isEmpty()) {
                result.add(new SubTree<>(current));
            }
            return result;
        }

        private static long cost(Parameter[] space) {
            long l = 0L;
            for (Parameter p : space) {
                l += Math.abs(p.max() - p.min());
            }
            return l;
        }

        static <T> Parameter[] buildParameterSpace(List<? extends Node<T>> list) {
            if (list.isEmpty()) {
                throw new IllegalArgumentException("SubTree needs at least one child");
            }
            Parameter[] result = new Parameter[7];
            for (int i = 0; i < 7; i++) {
                result[i] = null;
            }
            for (Node<T> node : list) {
                for (int i = 0; i < 7; i++) {
                    result[i] = node.parameterSpace[i].span(result[i]);
                }
            }
            return result;
        }

        public T search(TargetPoint target, DistanceMetric<T> metric) {
            long[] arr = target.toParameterArray();
            Leaf<T> leaf = root.search(arr, lastResult.get(), metric);
            lastResult.set(leaf);
            return leaf.value;
        }

        static abstract class Node<T> {
            final Parameter[] parameterSpace;

            Node(List<Parameter> list) {
                this.parameterSpace = list.toArray(new Parameter[0]);
            }

            protected abstract Leaf<T> search(long[] query, Leaf<T> best, DistanceMetric<T> metric);

            protected long distance(long[] query) {
                long l = 0L;
                for (int i = 0; i < 7; i++) {
                    long d = this.parameterSpace[i].distance(query[i]);
                    l += d * d;
                }
                return l;
            }
        }

        static final class SubTree<T> extends Node<T> {
            final Node<T>[] children;

            @SuppressWarnings("unchecked")
            SubTree(List<? extends Node<T>> list) {
                this(buildParameterSpace(list), list);
            }

            @SuppressWarnings("unchecked")
            SubTree(Parameter[] space, List<? extends Node<T>> list) {
                super(Arrays.asList(space));
                this.children = list.toArray(new Node[0]);
            }

            @Override
            protected Leaf<T> search(long[] query, Leaf<T> best, DistanceMetric<T> metric) {
                long bestDist = best == null ? Long.MAX_VALUE : metric.distance(best, query);
                Leaf<T> bestLeaf = best;
                for (Node<T> child : this.children) {
                    long childDist = metric.distance(child, query);
                    if (bestDist <= childDist) continue;
                    Leaf<T> result = child.search(query, bestLeaf, metric);
                    long resultDist = child == result ? childDist : metric.distance(result, query);
                    if (bestDist > resultDist) {
                        bestDist = resultDist;
                        bestLeaf = result;
                    }
                }
                return bestLeaf;
            }
        }

        static final class Leaf<T> extends Node<T> {
            final T value;

            Leaf(ParameterPoint point, T value) {
                super(point.parameterSpace());
                this.value = value;
            }

            @Override
            protected Leaf<T> search(long[] query, Leaf<T> best, DistanceMetric<T> metric) {
                return this;
            }
        }
    }
}
