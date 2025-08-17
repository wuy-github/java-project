import React, { useState } from "react";
import {
  AppBar,
  Toolbar,
  Box,
  Typography,
  IconButton,
  InputBase,
  Badge,
  Drawer,
  List,
  ListItem,
  ListItemText,
  Divider,
} from "@mui/material";
import SearchIcon from "@mui/icons-material/Search";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import FavoriteIcon from "@mui/icons-material/Favorite";
import MenuIcon from "@mui/icons-material/Menu";
import { Link as RouterLink, useNavigate } from "react-router-dom";
import { SearchInput } from "../theme/theme";
import AuthButton from "./AuthButton";

export default function Header({
  cartCount,
  searchQuery,
  setSearchQuery,
  onSearch,
}) {
  const navigate = useNavigate();
  const [mobileOpen, setMobileOpen] = useState(false);

  const handleSearch = () => {
    if (onSearch) onSearch();
    navigate(`/shop?search=${encodeURIComponent(searchQuery)}`);
  };

  const toggleDrawer = (open) => () => {
    setMobileOpen(open);
  };

  const menuItems = [
    { label: "Trang chủ", path: "/" },
    { label: "Cửa hàng", path: "/shop" },
    { label: "Thẩm định", path: "/appraisal" },
    { label: "Thương hiệu", path: "/brands" },
    { label: "Giới thiệu", path: "/about" },
    { label: "Hỗ trợ", path: "/support" },
    { label: "Liên hệ", path: "/contact" },
  ];

  return (
    <>
      <AppBar
        position="sticky"
        color="default"
        elevation={1}
        sx={{ bgcolor: "white" }}
      >
        <Toolbar sx={{ justifyContent: "space-between" }}>
          {/* Logo */}
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

          {/* Menu desktop */}
          <Box sx={{ display: { xs: "none", md: "flex" }, gap: 4 }}>
            {menuItems.map((item) => (
              <RouterLink
                key={item.path}
                to={item.path}
                style={{ textDecoration: "none", color: "inherit" }}
              >
                {item.label}
              </RouterLink>
            ))}
          </Box>

          {/* Actions */}
          <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
            {/* Search */}
            <SearchInput sx={{ display: { xs: "none", sm: "flex" } }}>
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

            {/* Cart */}
            <IconButton sx={{ ml: 1 }} onClick={() => navigate("/cart")}>
              <Badge badgeContent={cartCount} color="error">
                <ShoppingCartIcon />
              </Badge>
            </IconButton>

            {/* Favorites */}
            <IconButton onClick={() => navigate("/favorites")}>
              <FavoriteIcon />
            </IconButton>

            {/* Auth */}
            <AuthButton />

            {/* Mobile menu toggle */}
            <IconButton
              sx={{ display: { md: "none" } }}
              onClick={toggleDrawer(true)}
            >
              <MenuIcon />
            </IconButton>
          </Box>
        </Toolbar>
      </AppBar>

      {/* Drawer mobile */}
      <Drawer anchor="right" open={mobileOpen} onClose={toggleDrawer(false)}>
        <Box sx={{ width: 250, p: 2 }} role="presentation">
          <Typography variant="h6" sx={{ mb: 2, color: "primary.main" }}>
            Menu
          </Typography>
          <Divider sx={{ mb: 2 }} />
          <List>
            {menuItems.map((item) => (
              <ListItem
                button
                key={item.path}
                component={RouterLink}
                to={item.path}
                onClick={toggleDrawer(false)}
              >
                <ListItemText primary={item.label} />
              </ListItem>
            ))}
          </List>
        </Box>
      </Drawer>
    </>
  );
}
