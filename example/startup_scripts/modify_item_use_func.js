/**
 * This example makes all item use methods that have not had a use method overridden into the use method of the ender pearl.
 *
 * Note: If you want to modify a specific item,
 *       you need to check that its class overrides the method that needs to be modified.
 */

// 这个例子可以让所有没有重写过use方法的物品use方法都变成ender pearl的use方法。
// 注意: 如果要修改指定的物品，需要检查它的类是否重写了需要修改的方法。

ItemEvents.modification((event) => {
    event.modifyItemBuilder("*", (itemBuilder) => {
        itemBuilder
        .use((l, p, h) => {
            Item.of("ender_pearl").use(l,p,h);
            return true;
        })
        return itemBuilder;
    });
});