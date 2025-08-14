import React from "react";
import { Container, Typography, TextField, Button, Box } from "@mui/material";

export default function Contact() {
  return (
    <Container sx={{ py: 6 }}>
      <Typography variant="h3" fontWeight="bold" gutterBottom>
        Liên hệ với chúng tôi
      </Typography>
      <Typography variant="body1" sx={{ mb: 4 }}>
        Hãy để lại lời nhắn, chúng tôi sẽ phản hồi trong thời gian sớm nhất.
      </Typography>
      <Box
        component="form"
        sx={{ display: "flex", flexDirection: "column", gap: 2, maxWidth: 500 }}
      >
        <TextField label="Họ và tên" variant="outlined" fullWidth />
        <TextField label="Email" type="email" variant="outlined" fullWidth />
        <TextField
          label="Nội dung"
          multiline
          rows={4}
          variant="outlined"
          fullWidth
        />
        <Button variant="contained" color="primary">
          Gửi
        </Button>
      </Box>
    </Container>
  );
}
