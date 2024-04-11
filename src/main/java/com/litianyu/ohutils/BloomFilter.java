package com.litianyu.ohutils;

import java.util.BitSet;

public class BloomFilter {

    /**
     * 位数组大小
     */
    private static final int DEFAULT_SIZE = 2 << 24;

    /**
     * 不同的哈希函数
     */
    private static final int[] SEEDS = new int[]{3, 13, 46, 71, 91, 134};

    /**
     * 位数组
     */
    private BitSet bits = new BitSet(DEFAULT_SIZE);

    /**
     * 一批拥有 hash 方法的对象，用来做哈希运算
     */
    private SimpleHash[] hashers = new SimpleHash[SEEDS.length];

    /**
     * 构造方法
     */
    public BloomFilter() {
        for (int i = 0; i < SEEDS.length; i++) {
            hashers[i] = new SimpleHash(DEFAULT_SIZE, SEEDS[i]);
        }
    }

    public void put(Object value) {
        for (SimpleHash hasher : hashers) {
            bits.set(hasher.hash(value), true); // 将指定索引处的位设置为指定的值
        }
    }

    public boolean contains(Object value) {
        for (SimpleHash hasher : hashers) {
            if (!bits.get(hasher.hash(value))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 内部类，哈希计算
     */
    public static class SimpleHash{
        private int cap;
        private int seed;

        public SimpleHash(int cap, int seed) {
            this.cap = cap;
            this.seed = seed;
        }

        public int hash(Object value) {
            int h;
            // 返回一个介于 0 到 cap - 1 之间的随机数
            return (value == null) ? 0 : Math.abs(seed * (cap - 1) & ((h = value.hashCode()) ^ (h >>> 16)));
        }
    }


    public static void main(String[] args) {

        String value1 = "https://javaguide.cn/";
        String value2 = "https://github.com/Snailclimb";

        Integer value3 = 13423;
        Integer value4 = 22131;

        BloomFilter filter1 = new BloomFilter();

        System.out.println(filter1.contains(value1));
        System.out.println(filter1.contains(value2));
        filter1.put(value1);
        filter1.put(value2);
        System.out.println(filter1.contains(value1));
        System.out.println(filter1.contains(value2));

        System.out.println(filter1.contains(value3));
        System.out.println(filter1.contains(value4));
        filter1.put(value3);
        filter1.put(value4);
        System.out.println(filter1.contains(value3));
        System.out.println(filter1.contains(value4));
    }
}
