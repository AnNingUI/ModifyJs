package com.anningui.modifyjs.util.js_long;

import dev.latvian.mods.kubejs.typings.Info;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
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
public class SwitchMap<T> {
    private final Map<T, Supplier<Object>> cases = new HashMap<>();
    private final Supplier<Object> defaultCase;

    private SwitchMap(Map<T, Supplier<Object>> cases, Supplier<Object> defaultCase) {
        this.cases.putAll(cases);
        this.defaultCase = defaultCase;
    }

    // 创建 SwitchMap 实例，支持任何类型的常量
    public static <T> SwitchMap<T> of(Case<T> defaultCase, Case<T>... cases) {
        Map<T, Supplier<Object>> caseMap = new HashMap<>();
        for (Case<T> caseItem : cases) {
            caseMap.put(caseItem.constantValue(), caseItem.value());
        }
        return new SwitchMap<>(caseMap, defaultCase.value());
    }

    // 专门针对枚举类型的优化版本
    public static <T extends Enum<T>> SwitchMap<T> ofEnum(Case<T> defaultCase, Case<T>... cases) {
        EnumMap<T, Supplier<Object>> caseMap = new EnumMap<>((Class<T>) cases[0].constantValue().getClass());
        for (Case<T> caseItem : cases) {
            caseMap.put(caseItem.constantValue(), caseItem.value());
        }
        return new SwitchMap<>(caseMap, defaultCase.value());
    }

    // 获取对应的值
    public Object get(T constantValue) {
        Supplier<Object> result = cases.get(constantValue);
        if (result != null) {
            return result.get();
        }
        return defaultCase.get();
    }

    // Case 用于存储常量与对应的值
    public record Case<T>(T constantValue, Supplier<Object> value) {
    }

    // 默认 case 方法
    public static <T> Case<T> defOf(Supplier<Object> supplier) {
        return new Case<>(null, supplier);
    }

    // caseOf 用于构建 case 条目
    public static <T> Case<T> caseOf(T constantValue, Object value) {
        return new Case<>(constantValue, () -> value);
    }
}