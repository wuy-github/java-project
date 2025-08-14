import React from "react";
import { Box, Container, Grid, Typography } from "@mui/material";
import { services } from "../data/services";

export default function Services() {
  return (
    <Box sx={{ py: 3, bgcolor: "#fbfbfb" }}>
      <Container>
        <Grid container spacing={2}>
          {services.map((s, i) => (
            <Grid item xs={6} md={2} key={i} textAlign="center">
              <Typography sx={{ fontSize: 28 }}>{s.icon}</Typography>
              <Typography sx={{ mt: 1 }}>{s.title}</Typography>
            </Grid>
          ))}
        </Grid>
      </Container>
    </Box>
  );
}
