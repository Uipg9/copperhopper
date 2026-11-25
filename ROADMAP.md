# Copper Hopper roadmap

## Done (verified by `scripts/verify.ps1`, 2026-11-25)
- 1.0.1: recipe unlock advancement so the recipe shows in the recipe book (unit test guards every recipe).
- Block + item `copperhopper:copper_hopper` (a vanilla `HopperBlock` using the vanilla hopper block
  entity type; block added via Fabric `FabricBlockEntityType.addValidBlock`).
- Half-speed transfer: `HopperBlockEntityMixin` doubles every positive `setCooldown` value for copper
  hoppers (`CooldownRule`, unit-tested). Client gametest: 160 ticks, copper moved 10, vanilla moved 20.
- Menu title "Copper Hopper" (gametest).
- Item-entity pickup from above (gametest), recipe (gametest checks 6 ingredients), textures render (screenshot).
- Lang, blockstate/models (down + side), loot table (drops itself, keeps custom name), pickaxe tag, creative tab.

## Untested
- Comparator output, redstone lock, pushing sideways.
- Fabric Transfer API: should work through Fabric's vanilla-Container fallback, not tested.
- Mining with a pickaxe actually drops the item in survival.

## Not done / ideas
- Hopper minecart variant.
