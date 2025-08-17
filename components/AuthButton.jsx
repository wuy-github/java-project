//file path: src/components/AuthButton.jsx
import React, { useState } from "react";
import {
  Button,
  Avatar,
  Menu,
  MenuItem,
  Divider,
  ListItemIcon,
} from "@mui/material";
import { Logout, AccountCircle } from "@mui/icons-material";
import AuthModal from "./AuthModal";
import { useAuth } from "../api/useAuth";

const AuthButton = () => {
  const { user, logout } = useAuth();
  const [modalOpen, setModalOpen] = useState(false);
  const [anchorEl, setAnchorEl] = useState(null);

  const handleMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  return (
    <>
      {!user ? (
        <>
          <Button
            variant="contained"
            color="primary"
            onClick={() => setModalOpen(true)}
            sx={{ borderRadius: 25, px: 3 }}
          >
            Đăng nhập / Đăng ký
          </Button>

          <AuthModal open={modalOpen} onClose={() => setModalOpen(false)} />
        </>
      ) : (
        <>
          <Avatar
            onClick={handleMenuOpen}
            sx={{ bgcolor: "primary.main", cursor: "pointer" }}
          >
            {user.fullName ? user.fullName[0].toUpperCase() : "U"}
          </Avatar>

          <Menu
            anchorEl={anchorEl}
            open={Boolean(anchorEl)}
            onClose={handleMenuClose}
          >
            <MenuItem disabled>
              <ListItemIcon>
                <AccountCircle fontSize="small" />
              </ListItemIcon>
              {user.fullName || user.email}
            </MenuItem>
            <Divider />
            <MenuItem
              onClick={() => {
                logout();
                handleMenuClose();
              }}
            >
              <ListItemIcon>
                <Logout fontSize="small" />
              </ListItemIcon>
              Đăng xuất
            </MenuItem>
          </Menu>
        </>
      )}
    </>
  );
};

export default AuthButton;
