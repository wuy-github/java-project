import React from "react";
import {
  Card,
  CardMedia,
  CardContent,
  CardActions,
  Typography,
  IconButton,
  Tooltip,
  Box,
} from "@mui/material";
import FavoriteIcon from "@mui/icons-material/Favorite";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import VisibilityIcon from "@mui/icons-material/Visibility";

const formatPrice = (price) =>
  new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(price);

export default function ProductCardMUI({
  product,
  onToggleFavorite,
  onAddToCart,
  onViewDetails,
}) {
  return (
    <Card
      sx={{
        borderRadius: 2,
        boxShadow: 3,
        height: "100%",
        display: "flex",
        flexDirection: "column",
      }}
    >
      {/* Image */}
      <Box
        sx={{ position: "relative", paddingTop: "100%", overflow: "hidden" }}
      >
        <CardMedia
          component="img"
          image={product.image_url}
          alt={product.name}
          sx={{
            position: "absolute",
            top: 0,
            left: 0,
            width: "100%",
            height: "100%",
            objectFit: "cover",
            transition: "transform 0.3s",
            "&:hover": { transform: "scale(1.05)" },
          }}
        />
      </Box>

      {/* Content */}
      <CardContent sx={{ flexGrow: 1 }}>
        <Typography
          variant="subtitle1"
          fontWeight={600}
          noWrap
          title={product.name}
        >
          {product.name}
        </Typography>
        <Typography variant="body2" color="text.secondary">
          {product.name_brand} • {product.name_category}
        </Typography>
        <Typography
          variant="h6"
          color="primary"
          sx={{ mt: 1, fontWeight: 700 }}
        >
          {formatPrice(product.price)}
        </Typography>
      </CardContent>

      {/* Actions */}
      <CardActions disableSpacing sx={{ justifyContent: "space-between" }}>
        <Box>
          <Tooltip title={product.is_favorite ? "Bỏ yêu thích" : "Yêu thích"}>
            <IconButton
              color={product.is_favorite ? "error" : "default"}
              onClick={() => onToggleFavorite(product.id)}
            >
              {product.is_favorite ? <FavoriteIcon /> : <FavoriteBorderIcon />}
            </IconButton>
          </Tooltip>

          <Tooltip title="Thêm vào giỏ hàng">
            <IconButton color="primary" onClick={() => onAddToCart(product.id)}>
              <ShoppingCartIcon />
            </IconButton>
          </Tooltip>
        </Box>

        <Tooltip title="Xem chi tiết">
          <IconButton
            color="secondary"
            onClick={() => onViewDetails(product.id)}
          >
            <VisibilityIcon />
          </IconButton>
        </Tooltip>
      </CardActions>
    </Card>
  );
}
