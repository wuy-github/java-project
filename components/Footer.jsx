import React from "react";
import {
  Box,
  Grid,
  Typography,
  Link,
  IconButton,
  Stack,
  Divider,
} from "@mui/material";
import LocationOnIcon from "@mui/icons-material/LocationOn";
import PhoneIcon from "@mui/icons-material/Phone";
import FacebookIcon from "@mui/icons-material/Facebook";
import InstagramIcon from "@mui/icons-material/Instagram";
import YouTubeIcon from "@mui/icons-material/YouTube";

export default function Footer() {
  return (
    <Box
      component="footer"
      sx={{
        backgroundColor: "#8B0000",
        color: "#fff",
        pt: 6,
        pb: 3,
        fontSize: 14,
      }}
    >
      <Grid
        container
        spacing={4}
        sx={{
          maxWidth: 1200,
          mx: "auto",
          px: 2,
        }}
      >
        {/* Cột 1: Logo + Mô tả + Mạng xã hội */}
        <Grid item xs={12} md={4}>
          <Stack direction="row" spacing={1.5} alignItems="center" mb={2}>
            <img src="/logo.png" alt="Logo" style={{ height: 48 }} />
            <Typography variant="h6" fontWeight="bold">
              HỆ THỐNG ĐỒNG HỒ CŨ UTH
            </Typography>
          </Stack>

          <Typography
            variant="body2"
            sx={{ lineHeight: 1.8, textAlign: "justify", mb: 2 }}
          >
            Chuyên sửa chữa, thu mua đồng hồ cũ, bảo dưỡng, thay linh kiện và
            thẩm định bởi các chuyên gia hàng đầu.
          </Typography>

          <Typography variant="subtitle2" sx={{ mb: 1, fontWeight: "bold" }}>
            Mạng xã hội
          </Typography>
          <Stack direction="row" spacing={1.5}>
            {[FacebookIcon, InstagramIcon, YouTubeIcon].map((Icon, idx) => (
              <IconButton
                key={idx}
                color="inherit"
                size="small"
                sx={{
                  border: "1px solid rgba(255,255,255,0.4)",
                  borderRadius: "8px",
                  transition: "all 0.3s ease",
                  "&:hover": {
                    backgroundColor: "rgba(255,255,255,0.15)",
                    borderColor: "#fff",
                    transform: "translateY(-2px)",
                  },
                }}
              >
                <Icon />
              </IconButton>
            ))}
          </Stack>
        </Grid>

        {/* Cột 2: Địa chỉ + Hotline */}
        <Grid item xs={12} md={4}>
          <Typography variant="subtitle1" fontWeight="bold" mb={1}>
            Liên hệ
          </Typography>
          <Stack spacing={1.5}>
            <Stack direction="row" spacing={1.5} alignItems="flex-start">
              <LocationOnIcon />
              <Box>
                <Typography variant="body2" fontWeight="bold">
                  VP TP. Hồ Chí Minh
                </Typography>
                <Typography variant="body2">
                  02 Võ Oanh, P.25 – Q.Bình Thạnh
                </Typography>
              </Box>
            </Stack>
            <Stack direction="row" spacing={1.5} alignItems="flex-start">
              <PhoneIcon />
              <Box>
                <Typography variant="body2" fontWeight="bold">
                  Hotline dịch vụ
                </Typography>
                <Typography variant="body2">0589 882 106</Typography>
              </Box>
            </Stack>
          </Stack>
        </Grid>

        {/* Cột 3: Liên kết */}
        <Grid item xs={12} md={4}>
          <Grid container spacing={6}>
            <Grid item xs={6}>
              <Typography
                variant="subtitle2"
                sx={{ mb: 5, fontWeight: "bold", fontSize: "15px" }}
              >
                Hỗ trợ khách hàng
              </Typography>
              <Link
                href="#"
                color="inherit"
                underline="hover"
                display="block"
                mb={2}
                fontSize="16px"
              >
                Tra cứu bảo hành
              </Link>
              <Link
                href="#"
                color="inherit"
                underline="hover"
                display="block"
                mb={2}
                fontSize="16px"
              >
                Chính sách bảo mật
              </Link>
              <Link
                href="#"
                color="inherit"
                underline="hover"
                display="block"
                fontSize="16px"
              >
                Giải quyết khiếu nại
              </Link>
            </Grid>
            <Grid item xs={6}>
              <Typography
                variant="subtitle2"
                sx={{ mb: 2, fontWeight: "bold", fontSize: "15px" }}
              >
                Chính sách & Thanh toán
              </Typography>
              <Link
                href="#"
                color="inherit"
                underline="hover"
                display="block"
                mb={2}
                fontSize="16px"
              >
                Hướng dẫn thanh toán
              </Link>
              <Link
                href="#"
                color="inherit"
                underline="hover"
                display="block"
                mb={2}
                fontSize="16px"
              >
                Chính sách bảo hành
              </Link>
              <Link
                href="#"
                color="inherit"
                underline="hover"
                display="block"
                fontSize="16px"
              >
                Chính sách kiểm hàng
              </Link>
            </Grid>
          </Grid>
        </Grid>
      </Grid>

      {/* Bản quyền */}
      <Divider sx={{ borderColor: "rgba(255,255,255,0.2)", my: 3 }} />
      <Typography variant="body2" align="center">
        Copyright © 2025 HỆ THỐNG ĐỒNG HỒ CŨ UTH.vn. All rights reserved
      </Typography>
    </Box>
  );
}
