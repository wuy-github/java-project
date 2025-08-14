import React from "react";
import {
  Card,
  CardMedia,
  CardContent,
  CardActions,
  IconButton,
  Typography,
  Stack,
  Chip,
  Button,
} from "@mui/material";
import FavoriteIcon from "@mui/icons-material/Favorite";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import VisibilityIcon from "@mui/icons-material/Visibility";

const formatPrice = (price) =>
  new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(
    price
  );

export default function ProductCard({
  product,
  onToggleFavorite,
  onAddToCart,
  onViewDetails,
}) {
  return (
    <Card
      sx={{
        borderRadius: 2,
        position: "relative",
        display: "flex",
        flexDirection: "column",
        height: "100%",
      }}
    >
      {/* Hình ảnh */}
      <CardMedia
        component="img"
        height="180"
        image={product.image_url}
        alt={product.name}
      />

      {/* Nút yêu thích */}
      <IconButton
        onClick={() => onToggleFavorite(product.id)}
        sx={{ position: "absolute", top: 12, right: 12, bgcolor: "white" }}
        size="small"
      >
        {product.is_favorite ? (
          <FavoriteIcon sx={{ color: "error.main" }} />
        ) : (
          <FavoriteBorderIcon sx={{ color: "grey.400" }} />
        )}
      </IconButton>

      {/* Thông tin */}
      <CardContent sx={{ flexGrow: 1 }}>
        <Typography
          variant="subtitle2"
          sx={{ fontWeight: 700, height: 40, overflow: "hidden" }}
        >
          {product.name}
        </Typography>
        <Typography sx={{ color: "primary.main", fontWeight: 700, mt: 1 }}>
          {formatPrice(product.price)}
        </Typography>
        <Stack direction="row" spacing={1} sx={{ mt: 1 }}>
          <Chip
            label={
              typeof product.name_brand === "string"
                ? product.name_brand
                : product.name_brand?.name || ""
            }
            size="small"
          />
          <Chip
            label={
              typeof product.name_category === "string"
                ? product.name_category
                : product.name_category?.name || ""
            }
            size="small"
          />
        </Stack>
      </CardContent>

      {/* Nút hành động */}
      <CardActions sx={{ justifyContent: "space-between", px: 2, pb: 2 }}>
        <Button
          variant="contained"
          color="error"
          size="small"
          startIcon={<ShoppingCartIcon />}
          onClick={() => onAddToCart(product.id)}
        >
          Thêm giỏ
        </Button>
        <Button
          variant="outlined"
          color="error"
          size="small"
          startIcon={<VisibilityIcon />}
          onClick={() => onViewDetails(product.id)}
        >
          Xem
        </Button>
      </CardActions>
    </Card>
  );
}
