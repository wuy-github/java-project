import React, { useEffect, useMemo, useState } from "react";
import {
  Box,
  Container,
  Grid,
  Typography,
  Alert,
  Snackbar,
  Skeleton,
} from "@mui/material";
import FiltersBar from "../components/FiltersBar.jsx";
import ProductCardMUI from "../components/ProductCardMUI.jsx";
import PaginationBar from "../components/PaginationBar.jsx";
import { addToCart, getBrands, getWatches, toggleFavorite } from "../api";
import { tabCategories } from "../data/tabCategories";
import { brands as localBrands } from "../data/brands"; // fallback local

export default function Store() {
  const [products, setProducts] = useState([]);
  const [brands, setBrands] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const [selectedBrand, setSelectedBrand] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("");
  const [search, setSearch] = useState("");

  const [page, setPage] = useState(1);
  const limit = 12;
  const [totalPages, setTotalPages] = useState(0);

  const [toast, setToast] = useState({ open: false, message: "" });

  const user = useMemo(() => {
    const token = localStorage.getItem("jwt_token");
    return token ? { id: 1, fullName: "Nguyen Van A" } : null;
  }, []);

  useEffect(() => {
    (async () => {
      try {
        const b = await getBrands().catch(() => []);
        const brandList = Array.isArray(b) ? b : b?.data || [];
        setBrands(brandList.length ? brandList : localBrands);
      } catch {
        setBrands(localBrands);
      }
    })();
  }, []);

  useEffect(() => {
    loadProducts();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [page, selectedBrand, selectedCategory]);

  const loadProducts = async () => {
    setLoading(true);
    setError("");
    try {
      const { watch, totalPage } = await getWatches({
        page: page - 1,
        limit,
        brandId: selectedBrand || undefined,
        categoryId: selectedCategory || undefined,
        search: search?.trim() || undefined,
      });
      setProducts(watch || []);
      setTotalPages(totalPage || 0);
    } catch {
      setError("Có lỗi xảy ra khi tải sản phẩm. Vui lòng thử lại.");
    } finally {
      setLoading(false);
      window.scrollTo({ top: 0, behavior: "smooth" });
    }
  };

  const handleSearch = () => {
    setPage(1);
    loadProducts();
  };

  const clearFilters = () => {
    setSelectedBrand("");
    setSelectedCategory("");
    setSearch("");
    setPage(1);
    loadProducts();
  };

  const handleToggleFavorite = async (watchId) => {
    if (!user)
      return setError("Vui lòng đăng nhập để sử dụng tính năng yêu thích");
    try {
      await toggleFavorite(watchId);
      setProducts((prev) =>
        prev.map((p) =>
          p.id === watchId ? { ...p, is_favorite: !p.is_favorite } : p
        )
      );
    } catch {
      setError("Không thể cập nhật yêu thích. Vui lòng thử lại.");
    }
  };

  const handleAddToCart = async (watchId) => {
    if (!user) return setError("Vui lòng đăng nhập để thêm vào giỏ hàng");
    try {
      await addToCart({ userId: user.id, watchId });
      setToast({ open: true, message: "Đã thêm vào giỏ hàng" });
    } catch {
      setError("Không thể thêm vào giỏ hàng. Vui lòng thử lại.");
    }
  };

  const handleViewDetails = (watchId) => {
    window.location.href = `/product/${watchId}`;
  };

  return (
    <Box id="shop" sx={{ py: 6 }}>
      <Container>
        <Typography variant="h4" align="center" sx={{ fontWeight: 800, mb: 3 }}>
          CỬA HÀNG ĐỒNG HỒ
        </Typography>

        <FiltersBar
          brands={brands}
          categories={tabCategories}
          selectedBrand={selectedBrand}
          setSelectedBrand={(v) => {
            setSelectedBrand(v);
            setPage(1);
          }}
          selectedCategory={selectedCategory}
          setSelectedCategory={(v) => {
            setSelectedCategory(v);
            setPage(1);
          }}
          search={search}
          setSearch={setSearch}
          onSearch={handleSearch}
          onClear={clearFilters}
        />

        {error && (
          <Alert severity="error" sx={{ mb: 2 }} onClose={() => setError("")}>
            {error}
          </Alert>
        )}

        {loading ? (
          <Grid container spacing={3}>
            {Array.from({ length: 8 }).map((_, i) => (
              <Grid key={`sk-${i}`} size={{ xs: 12, sm: 6, md: 4, lg: 3 }}>
                <Skeleton
                  variant="rectangular"
                  height={200}
                  sx={{ borderRadius: 2 }}
                />
                <Skeleton sx={{ mt: 1 }} width="80%" />
                <Skeleton width="40%" />
                <Skeleton sx={{ mt: 1 }} width="60%" />
              </Grid>
            ))}
          </Grid>
        ) : (
          <>
            {products.length === 0 ? (
              <Alert severity="info" sx={{ my: 4 }}>
                Không tìm thấy sản phẩm nào. Hãy thử thay đổi bộ lọc hoặc từ
                khóa tìm kiếm.
              </Alert>
            ) : (
              <Grid container spacing={3}>
                {products.map((p) => (
                  <Grid key={p.id} size={{ xs: 12, sm: 6, md: 4, lg: 3 }}>
                    <ProductCardMUI
                      product={p}
                      onToggleFavorite={handleToggleFavorite}
                      onAddToCart={handleAddToCart}
                      onViewDetails={handleViewDetails}
                    />
                  </Grid>
                ))}
              </Grid>
            )}

            {totalPages > 1 && (
              <PaginationBar
                page={page}
                totalPages={totalPages}
                onChange={(p) => setPage(p)}
              />
            )}
          </>
        )}

        <Snackbar
          open={toast.open}
          autoHideDuration={2000}
          onClose={() => setToast({ open: false, message: "" })}
          message={toast.message}
        />
      </Container>
    </Box>
  );
}
