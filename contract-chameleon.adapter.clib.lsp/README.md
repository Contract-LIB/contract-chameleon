# Language Server for Contract-LIB

## Lazyvim

### Features

- [ ] (Working example in `nvim`)

### Contract-LIB File Type

Add the following file type definition to `.config/nvim/lua/config/options.lua`:

```lua

vim.filetype.add({
    extension = {
        clib = "contract-lib",
    },
})
```

### Add Language Server

Add the following file type definition to `.config/nvim/lua/plugins/ls_clib.lua`:
Be sure that you fix the path to the jar.

```lua
return {
  "neovim/nvim-lspconfig",
  opts = {
    servers = {
      chameleon = {
        filetypes = { "contract-lib" },
        cmd = {
          "java",
          "-jar",
          "<your_path>/contract-chameleon-1.0.0-SNAPSHOT-all.jar",
          "clib-lsp",
        },
      },
    },
  },
}
```
