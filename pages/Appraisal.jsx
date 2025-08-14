import React from "react";
import { Container, Typography, Box } from "@mui/material";

export default function Appraisal() {
  return (
    <Container sx={{ py: 6 }}>
      <Typography variant="h3" fontWeight="bold" gutterBottom>
        Thẩm định đồng hồ
      </Typography>
      <Typography variant="body1" sx={{ mb: 2 }}>
        Chúng tôi cung cấp dịch vụ thẩm định đồng hồ chính hãng, nhanh chóng và
        chính xác. Vui lòng liên hệ hoặc gửi thông tin sản phẩm để chúng tôi hỗ
        trợ.
      </Typography>
      <Box sx={{ mt: 4 }}>{/* Bạn có thể thêm form gửi thông tin ở đây */}</Box>
    </Container>
  );
}
