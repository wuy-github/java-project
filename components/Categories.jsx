import React from "react";
import { Box, Container, Grid, Paper, Typography } from "@mui/material";
import { categories } from "../data/categories";

export default function Categories() {
  return (
    <Box sx={{ py: 6 }}>
      <Container>
        <Grid container spacing={2}>
          {categories.map((c, i) => (
            <Grid item xs={6} md={2} key={i}>
              <Paper
                elevation={1}
                sx={{ p: 3, textAlign: "center", borderRadius: 2 }}
              >
                <Typography sx={{ fontSize: 32 }}>{c.icon}</Typography>
                <Typography sx={{ mt: 1, fontWeight: 500 }}>
                  {c.label}
                </Typography>
              </Paper>
            </Grid>
          ))}
        </Grid>
      </Container>
    </Box>
  );
}
