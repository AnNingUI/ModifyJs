package com.anningui.modifyjs.util.js_long;

import java.util.function.Consumer;
import java.util.function.Function;

public class TryCatchPipe {
    // 成功的逻辑，返回类型为 T
    private final Function<Void, ?> successAction;
    // 异常捕获的逻辑，返回类型为 T
    private final Function<Exception, ?> errorAction;
    // finally块的逻辑，返回类型为 void
    private final Function<Void, ?> finallyAction;

    private final boolean hasReturnValue;
    private final boolean hasFinally;

    // 构造方法：用于支持有返回值和没有返回值的情况
    public TryCatchPipe(Function<Void, ?> successAction, Function<Exception, ?> errorAction, Function<Void, ?> finallyAction, boolean hasReturnValue, boolean hasFinally) {
        this.successAction = successAction;
        this.errorAction = errorAction;
        this.finallyAction = finallyAction;
        this.hasReturnValue = hasReturnValue;
        this.hasFinally = hasFinally;
    }

    // 执行操作，带有 try-catch，返回值为 T
    public Object run() {
        if (hasFinally) {
            try {
                if (hasReturnValue) {
                    return successAction.apply(null); // 执行并返回值
                } else {
                    successAction.apply(null); // 执行，没有返回值
                    return null;
                }
            } catch (Exception e) {
                if (hasReturnValue) {
                    return errorAction.apply(e); // 返回异常处理结果
                } else {
                    errorAction.apply(e); // 异常处理
                    return null;
                }
            } finally {
                finallyAction.apply(null); // 执行 finally 块
            }
        } else {
            try {
                if (hasReturnValue) {
                    return successAction.apply(null); // 执行并返回值
                } else {
                    successAction.apply(null); // 执行，没有返回值
                    return null;
                }
            } catch (Exception e) {
                if (hasReturnValue) {
                    return errorAction.apply(e); // 返回异常处理结果
                } else {
                    errorAction.apply(e); // 异常处理
                    return null;
                }
            }
        }
    }

    // 静态方法用于无返回值的操作
    public static TryCatchPipe pipeWithVoid(Runnable successAction, Consumer<Exception> errorAction) {
        return new TryCatchPipe(v -> {
            successAction.run();
            return null;
        }, e -> {
            errorAction.accept(e);
            return null;
        }, v -> null, false, false);
    }

    // 静态方法用于有返回值的操作
    public static <T> TryCatchPipe pipeWithReturnValue(Function<Void, T> successAction, Function<Exception, T> errorAction) {
        return new TryCatchPipe(successAction, errorAction, v -> null, true, false);
    }

    // 静态方法用于有返回值的操作，并支持 finally 块
    public static <T> TryCatchPipe pipeWithReturnValueAndFinally(Function<Void, T> successAction, Function<Exception, T> errorAction, Runnable finallyAction) {
        return new TryCatchPipe(successAction, errorAction, v -> {
            finallyAction.run();
            return null;
        }, true, true);
    }

    // 静态方法用于无返回值的操作，并支持 finally 块
    public static TryCatchPipe pipeWithVoidAndFinally(Runnable successAction, Consumer<Exception> errorAction, Runnable finallyAction) {
        return new TryCatchPipe(v -> {
            successAction.run();
            return null;
        }, e -> {
            errorAction.accept(e);
            return null;
        }, v -> {
            finallyAction.run();
            return null;
        }, false, true);
    }

    public static void tryCatchBBV(boolean b, Runnable successAction, Runnable superAction) {
        if (b) {
            TryCatchPipe.pipeWithVoid(successAction, (e) -> superAction.run()).run();
        } else {
            superAction.run();
        }
    }

    public static <T> T tryCatchBBR(boolean b, Function<Void, T> successAction, Function<Exception, T> superAction) {
        if (b) {
            return (T) TryCatchPipe.pipeWithReturnValue(successAction, superAction).run();
        } else {
            return superAction.apply(null);
        }
    }
}
