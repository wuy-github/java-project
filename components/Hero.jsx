import React from "react";
import { Box, Container, Typography, Button } from "@mui/material";
import { useNavigate } from "react-router-dom";

export default function Hero() {
  const navigate = useNavigate();

  return (
    <Box
      id="home"
      sx={{
        height: { xs: 360, md: 500 },
        backgroundImage:
          "linear-gradient(rgba(0,0,0,0.5), rgba(0,0,0,0.5)), url(https://images.unsplash.com/photo-1609587312208-cea54be969e7?w=1600&h=800&fit=crop)",
        backgroundSize: "cover",
        backgroundPosition: "center",
        color: "white",
        display: "flex",
        alignItems: "center",
      }}
    >
      <Container>
        <Typography variant="h3" sx={{ fontWeight: "bold", lineHeight: 1.1 }}>
          TRUNG TÂM UY TÍN
          <br />
          DÀNH CHO ĐỒNG HỒ
          <br />
          CŨ CHÍNH HÃNG
        </Typography>
        <Typography variant="h6" sx={{ mt: 2, maxWidth: 680, opacity: 0.95 }}>
          Chào mừng bạn đến với Công ty Cổ phần Bình Viên Đồng Hồ — Công ty uy
          tín thẩm định đồng hồ hàng đầu tại Việt Nam
        </Typography>
        <Button
          variant="contained"
          sx={{ mt: 3, bgcolor: "primary.main" }}
          onClick={() => navigate("/shop")}
        >
          Xem thêm
        </Button>
      </Container>
    </Box>
  );
}
