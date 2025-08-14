import React from "react";
import { Container, Typography } from "@mui/material";

export default function About() {
  return (
    <Container sx={{ py: 6 }}>
      <Typography variant="h3" fontWeight="bold" gutterBottom>
        Giới thiệu
      </Typography>
      <Typography variant="body1" sx={{ mb: 2 }}>
        2GOCO là trung tâm uy tín về thẩm định và mua bán đồng hồ cũ chính hãng.
        Chúng tôi cam kết mang lại sản phẩm chất lượng và dịch vụ tận tâm.
      </Typography>
    </Container>
  );
}
