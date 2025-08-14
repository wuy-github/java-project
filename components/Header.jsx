import React from "react";
import {
  AppBar,
  Toolbar,
  Box,
  Typography,
  IconButton,
  InputBase,
  Badge,
  Avatar,
} from "@mui/material";
import SearchIcon from "@mui/icons-material/Search";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import FavoriteIcon from "@mui/icons-material/Favorite";
import PersonIcon from "@mui/icons-material/Person";
import { Link as RouterLink, useNavigate } from "react-router-dom";
import { SearchInput } from "../theme/theme";

export default function Header({
  user,
  onLogin,
  cartCount,
  searchQuery,
  setSearchQuery,
  onSearch,
}) {
  const navigate = useNavigate();

  const handleSearch = () => {
    if (onSearch) onSearch();
    navigate(`/shop?search=${encodeURIComponent(searchQuery)}`);
  };

  return (
    <AppBar
      position="sticky"
      color="default"
      elevation={1}
      sx={{ bgcolor: "white" }}
    >
      <Toolbar sx={{ justifyContent: "space-between" }}>
        <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
          <Typography
            variant="h5"
            sx={{ color: "primary.main", fontWeight: "bold" }}
            component={RouterLink}
            to="/"
            style={{ textDecoration: "none" }}
          >
            2GOCO
          </Typography>
        </Box>

        {/* Menu */}
        <Box sx={{ display: { xs: "none", md: "flex" }, gap: 4 }}>
          <RouterLink
            to="/"
            style={{ textDecoration: "none", color: "inherit" }}
          >
            Trang chủ
          </RouterLink>
          <RouterLink
            to="/shop"
            style={{ textDecoration: "none", color: "inherit" }}
          >
            Cửa hàng
          </RouterLink>
          <RouterLink
            to="/appraisal"
            style={{ textDecoration: "none", color: "inherit" }}
          >
            Thẩm định
          </RouterLink>
          <RouterLink
            to="/brands"
            style={{ textDecoration: "none", color: "inherit" }}
          >
            Thương hiệu
          </RouterLink>
          <RouterLink
            to="/about"
            style={{ textDecoration: "none", color: "inherit" }}
          >
            Giới thiệu
          </RouterLink>
          <RouterLink
            to="/support"
            style={{ textDecoration: "none", color: "inherit" }}
          >
            Hỗ trợ
          </RouterLink>
          <RouterLink
            to="/contact"
            style={{ textDecoration: "none", color: "inherit" }}
          >
            Liên hệ
          </RouterLink>
        </Box>

        {/* Actions */}
        <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
          <SearchInput>
            <InputBase
              placeholder="Tìm kiếm..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              onKeyDown={(e) => e.key === "Enter" && handleSearch()}
              sx={{ ml: 1, flex: 1 }}
            />
            <IconButton onClick={handleSearch} size="small">
              <SearchIcon />
            </IconButton>
          </SearchInput>

          <IconButton sx={{ ml: 1 }} onClick={() => navigate("/cart")}>
            <Badge badgeContent={cartCount} color="error">
              <ShoppingCartIcon />
            </Badge>
          </IconButton>

          <IconButton onClick={() => navigate("/favorites")}>
            <FavoriteIcon />
          </IconButton>

          {user ? (
            <Avatar
              sx={{
                bgcolor: "primary.main",
                width: 36,
                height: 36,
                cursor: "pointer",
              }}
              onClick={() => navigate("/profile")}
            >
              {user.fullName.charAt(0)}
            </Avatar>
          ) : (
            <IconButton onClick={onLogin}>
              <PersonIcon />
            </IconButton>
          )}
        </Box>
      </Toolbar>
    </AppBar>
  );
}
