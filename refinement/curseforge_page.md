# Refinement
Refinement is a library mod which allows for upgradable and data pack configurable blocks.

This means that a server can have its own data/resource pack filled with custom
blocks tiers which are then synced to the client through vanilla's own server pack feature.
This also means that mod pack creators can remove default block tiers and substitute in their own.
## Data Pack - For mod authors and data pack creators
It is highly recommended that you look at the default data pack included in mods
that use this library as there will be entries specific to each mod.
### Tiers
Below is an example of the json entries required by all tiers.
```json
{
  "type": "torcherino:torcherino", // Unique identifier for the type of block being targeted. This is set by the mod.
  "values": {
    // Each mod will have their own custom values here.
  },
  "friendlyName": "Normal",
  "default": true // Declares this tier as the default for any block using the same type,
                  // only one tier can be default for a type and there must be one,
                  // if this entry is missing it will default to false.
}
```
The id for the tier itself is generated from the file path: `data/torcherino/tiers/default` -> `torcherino:default`.
### Recipes
Just like vanilla the upgrade and downgrade crafting recipes have shaped and shapeless variants.

Unlike vanilla however, the `result` entry is the identifier of the new tier, the `group` entry is unsupported, and there is a new entry `input` for the required tier for the recipe.