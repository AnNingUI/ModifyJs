package com.anningui.modifyjs.util.js_long;

import dev.latvian.mods.kubejs.typings.Info;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@Info("""
        This class demonstrates the use of the SwitchMap utility to map various static constants to specific values.\s
        The class provides three examples of how to use SwitchMap with:
        \s
         1. A class containing static constants of different types (String, Integer, Double).
         2. A class with only one type of static constants (Integer).
         3. An Enum type that represents different display contexts (ItemDisplayContext).
        \s
         The examples showcase how to efficiently map these constants to their corresponding values using SwitchMap\s
         with methods like caseOf and defaultCase. This approach offers a concise, efficient, and maintainable\s
         alternative to traditional switch-case statements.
        \s
         It also demonstrates how to handle default cases when no matching constant is found, ensuring robustness\s
         in different use cases. This class leverages Java's powerful enum and constant handling to efficiently manage\s
         the mapping of static values to results.
        """)
public class SwitchMap<T, V> {
    private final Map<T, Function<T, V>> cases = new HashMap<>();
    private final Function<T, V> defaultCase;

    private SwitchMap(Map<T, Function<T, V>> cases, Function<T, V> defaultCase) {
        this.cases.putAll(cases);
        this.defaultCase = defaultCase;
    }

    // 创建 SwitchMap 实例，支持任何类型的常量
    @SafeVarargs
    public static <T, V> SwitchMap<T, V> of(Case<T, V> defaultCase, Case<T, V>... cases) {
        Map<T, Function<T, V>> caseMap = new HashMap<>();
        for (Case<T, V> caseItem : cases) {
            caseMap.put(caseItem.constantValue(), caseItem.value());
        }
        return new SwitchMap<>(caseMap, defaultCase.value());
    }

    // 专门针对枚举类型的优化版本
    @SafeVarargs
    public static <T extends Enum<T>, V> SwitchMap<T, V> ofEnum(Case<T, V> defaultCase, Case<T, V>... cases) {
        EnumMap<T, Function<T, V>> caseMap = new EnumMap<>((Class<T>) cases[0].constantValue().getClass());
        for (Case<T, V> caseItem : cases) {
            caseMap.put(caseItem.constantValue(), caseItem.value());
        }
        return new SwitchMap<>(caseMap, defaultCase.value());
    }

    // 获取对应的值
    public V get(T constantValue) {
        Function<T, V> result = cases.get(constantValue);
        if (result != null) {
            return result.apply(constantValue);
        }
        return defaultCase.apply(constantValue);
    }

    // Case 用于存储常量与对应的值
    public record Case<T, V>(T constantValue, Function<T, V> value) {
    }

    // 默认 case 方法
    public static <T, V> Case<T, V> defOf(Function<T, V> supplier) {
        return new Case<>(null, supplier);
    }

    // caseOf 用于构建 case 条目
    public static <T, V> Case<T, V> caseOf(T constantValue, V value) {
        return new Case<>(constantValue, (e) -> value);
    }
}