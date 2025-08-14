import React from "react";
import {
  Box,
  Button,
  MenuItem,
  TextField,
  InputAdornment,
} from "@mui/material";
import SearchIcon from "@mui/icons-material/Search";
import ClearAllIcon from "@mui/icons-material/ClearAll";

export default function FiltersBar({
  brands,
  categories,
  selectedBrand,
  setSelectedBrand,
  selectedCategory,
  setSelectedCategory,
  search,
  setSearch,
  onSearch,
  onClear,
}) {
  return (
    <Box
      sx={{
        display: "flex",
        flexWrap: "wrap",
        gap: 2,
        mb: 3,
        alignItems: "center",
      }}
    >
      {/* Brand select */}
      <TextField
        select
        label="Thương hiệu"
        value={selectedBrand}
        onChange={(e) => setSelectedBrand(e.target.value)}
        size="small"
        sx={{ minWidth: 220 }}
      >
        <MenuItem value="">Tất cả</MenuItem>
        {brands.map((b) => (
          <MenuItem key={b.id} value={b.id}>
            {`${b.id} - ${b.name}`}
          </MenuItem>
        ))}
      </TextField>

      {/* Category select */}
      <TextField
        select
        label="Danh mục"
        value={selectedCategory}
        onChange={(e) => setSelectedCategory(e.target.value)}
        size="small"
        sx={{ minWidth: 220 }}
      >
        <MenuItem value="">Tất cả</MenuItem>
        {categories.map((c) => (
          <MenuItem key={c.id} value={c.id}>
            {`${c.id} - ${c.name}`}
          </MenuItem>
        ))}
      </TextField>

      {/* Search input */}
      <TextField
        placeholder="Tìm kiếm sản phẩm..."
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        onKeyDown={(e) => e.key === "Enter" && onSearch()}
        size="small"
        sx={{ flex: 1, minWidth: 220 }}
        InputProps={{
          startAdornment: (
            <InputAdornment position="start">
              <SearchIcon fontSize="small" />
            </InputAdornment>
          ),
        }}
      />

      {/* Buttons */}
      <Button variant="contained" color="primary" onClick={onSearch}>
        Tìm kiếm
      </Button>
      <Button
        variant="outlined"
        color="secondary"
        startIcon={<ClearAllIcon />}
        onClick={onClear}
      >
        Xóa lọc
      </Button>
    </Box>
  );
}
