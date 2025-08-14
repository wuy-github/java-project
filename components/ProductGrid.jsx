import React from "react";
import { Box, Container, Typography, Grid, Button } from "@mui/material";
import { useNavigate } from "react-router-dom";
import ProductCard from "./ProductCard";

export default function ProductGrid({ products, onToggleFavorite, title }) {
  const navigate = useNavigate();

  return (
    <Box sx={{ py: 6 }}>
      <Container>
        <Typography variant="h4" align="center" sx={{ fontWeight: 800, mb: 4 }}>
          {title}
        </Typography>

        <Grid container spacing={3}>
          {products.map((p) => (
            <Grid item xs={12} sm={6} md={4} lg={3} key={p.id}>
              <ProductCard product={p} onToggleFavorite={onToggleFavorite} />
            </Grid>
          ))}
        </Grid>

        <Box textAlign="center" sx={{ mt: 4 }}>
          <Button
            variant="contained"
            sx={{ bgcolor: "primary.main" }}
            onClick={() => navigate("/shop")}
          >
            Xem thêm
          </Button>
        </Box>
      </Container>
    </Box>
  );
}
