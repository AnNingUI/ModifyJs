> This English Readme is translated by `GPT-4o`, please understand if there is any incomprehension.

---

<p align="center">
<a href="./README_ZH.md"> 简体中文 </a> | <span> English </span>
</p>

This is a simple mod that initially started as an addition for some features not implemented by the KJS community. It currently implements some basic item rendering, which can be considered a simple extension to[RenderJS](https://github.com/ch1335/RenderJS). And an Item type that can be used to create [MEK Module Item](https://wiki.aidancbrady.com/wiki/Modules)

### TODE LIST
- [x] Currently only the support for item renderByItem has been added
- [x] Add an Item type that can be used to create [MEK Module Item](https://wiki.aidancbrady.com/wiki/Modules)
- [ ] ...(still being improved)

### Attention
<span style="color: red;">
This mod overrides the <code style="color: red; font-size: 16px">createObject</code> method of <code style="color: red; font-size: 16px">BlockItemBuilder</code>, which may cause issues during block registration.</span>

## Example
Look [This](./example/)