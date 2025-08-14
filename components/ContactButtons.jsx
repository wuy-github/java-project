import React from "react";
import { Box, Fab, Tooltip } from "@mui/material";
import PhoneIcon from "@mui/icons-material/Phone";
import ChatIcon from "@mui/icons-material/Chat"; // Tạm thay Zalo icon
import EventIcon from "@mui/icons-material/Event";

export default function ContactButtons() {
  return (
    <Box
      sx={{
        position: "fixed",
        bottom: 20,
        right: 20,
        display: "flex",
        flexDirection: "column",
        gap: 1.5,
        zIndex: 1500,
      }}
    >
      <Tooltip title="Gọi ngay" placement="left">
        <Fab
          color="error"
          href="tel:0909123456"
          sx={{
            backgroundColor: "#d32f2f",
            "&:hover": { backgroundColor: "#b71c1c" },
          }}
        >
          <PhoneIcon />
        </Fab>
      </Tooltip>

      <Tooltip title="Chat Zalo" placement="left">
        <Fab
          sx={{
            backgroundColor: "#039be5",
            color: "#fff",
            "&:hover": { backgroundColor: "#0277bd" },
          }}
          href="https://zalo.me/0589882106"
          target="_blank"
        >
          <ChatIcon />
        </Fab>
      </Tooltip>

      <Tooltip title="Đặt lịch" placement="left">
        <Fab
          sx={{
            backgroundColor: "#2e7d32",
            color: "#fff",
            "&:hover": { backgroundColor: "#1b5e20" },
          }}
          href="#booking"
        >
          <EventIcon />
        </Fab>
      </Tooltip>
    </Box>
  );
}
