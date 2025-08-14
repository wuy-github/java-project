import React from "react";
import {
  Container,
  Typography,
  List,
  ListItem,
  ListItemText,
} from "@mui/material";

export default function Support() {
  return (
    <Container sx={{ py: 6 }}>
      <Typography variant="h3" fontWeight="bold" gutterBottom>
        Hỗ trợ khách hàng
      </Typography>
      <Typography variant="body1" sx={{ mb: 2 }}>
        Chúng tôi luôn sẵn sàng hỗ trợ bạn 24/7. Các kênh hỗ trợ:
      </Typography>
      <List>
        <ListItem>
          <ListItemText primary="📞 Hotline: 0123 456 789" />
        </ListItem>
        <ListItem>
          <ListItemText primary="✉ Email: support@2goco.com" />
        </ListItem>
        <ListItem>
          <ListItemText primary="💬 Chat trực tuyến" />
        </ListItem>
      </List>
    </Container>
  );
}
