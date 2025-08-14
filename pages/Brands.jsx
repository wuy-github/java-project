import React from "react";
import { Container, Typography, Box } from "@mui/material";

export default function Brands() {
  return (
    <Container sx={{ py: 6 }}>
      <Typography variant="h3" fontWeight="bold" gutterBottom>
        Thương hiệu
      </Typography>
      <Typography variant="body1">
        Chúng tôi hợp tác với nhiều thương hiệu đồng hồ nổi tiếng trên thế giới.
        Khám phá bộ sưu tập đa dạng từ các thương hiệu uy tín.
      </Typography>
      <Box sx={{ mt: 4 }}>
        {/* Bạn có thể thêm danh sách thương hiệu ở đây */}
      </Box>
    </Container>
  );
}
