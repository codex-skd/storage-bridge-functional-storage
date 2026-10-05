# Storage Bridge (Functional Storage)

Right-click a Functional Storage Controller to send enchanted books to an Apothic-Enchanting Library, gems to an Apotheosis Gem Case and matching items to Sophisticated Storage chests next to its linked drawers — no hoppers, no pipes.

Storage Bridge (Functional Storage) hooks into the click Functional Storage already uses to insert items into a Controller, and adds targets that Functional Storage can't reach on its own. It uses each mod's public `IItemHandler` capability; no mixins, no extra blocks.

## Features

- **Apothic-Enchanting Library integration** — right-clicking a Functional Storage Controller (plain or framed) or a Controller Extension linked to one (main hand, not sneaking, any face) moves every `minecraft:enchanted_book` stack in the player's inventory into an Apothic-Enchanting Library (`apothic_enchanting:library` "Enchantment Library" or `apothic_enchanting:ender_library` "Library of Alexandria") reachable from the Controller. One-directional: the Library consumes every book it accepts and cannot return them.
- **Sophisticated Storage integration** — the same click also moves items into a reachable Sophisticated Storage chest or barrel, but only items the chest/barrel already contains a matching stack of (never dumps unrelated items into an empty container).
- **Apotheosis Gem Case integration** — the same click also moves unsocketed gem stacks into a reachable Apotheosis Gem Case or Ender Gem Case; the Gem Case's own capability rejects anything that isn't a valid gem.
- **Reach** — Functional Storage links drawers wirelessly with its Linking Tool, so the target block can touch the Controller or any drawer or extension linked to it.
- Functional Storage's own insertion still happens on the same click.

## Requirements

| | |
|---|---|
| Minecraft | 1.21.1 |
| NeoForge | 21.1.249+ |
| Functional Storage (+ Titanium) | Required |
| Sophisticated Storage (+ Sophisticated Core) | Required (chest/barrel integration) |
| Apothic-Enchanting | Required (Library integration) |
| Apotheosis + Placebo | Required (Gem Case integration) |

All of them are declared as `required` dependencies in the mod metadata (`neoforge.mods.toml`).

## Building from source

Place the official `functionalstorage-1.21.1-1.5.8.jar` in `libs/`, then:

```
./gradlew build
```

The built jar is placed in `build/libs/`.
