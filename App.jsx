import React, { useState, useEffect } from "react";
import { ThemeProvider } from "@mui/material/styles";
import { Routes, Route, useNavigate } from "react-router-dom";
import {
  Box,
  Container,
  Typography,
  Paper,
  Button,
  Grid,
  Fab,
  Tooltip,
} from "@mui/material";
import { CalendarMonth, Phone, Chat } from "@mui/icons-material";

import Cart from "./pages/Cart";
import Favorites from "./pages/Favorites";
import Profile from "./pages/Profile";

import Header from "./components/Header";
import Hero from "./components/Hero";
import Categories from "./components/Categories";
import Services from "./components/Services";
import ProductGrid from "./components/ProductGrid";
import ProductCard from "./components/ProductCard";
import Footer from "./components/Footer";
import Store from "./pages/Store";

import { theme } from "./theme/theme";
import { sampleProducts } from "./data/products";
import { tabCategories } from "./data/tabCategories";

export default function App() {
  const [user, setUser] = useState(null);
  const [hotWatches, setHotWatches] = useState([]);
  const [searchQuery, setSearchQuery] = useState("");
  const [cartCount, setCartCount] = useState(0);
  const [tabIndex, setTabIndex] = useState(0);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("jwt_token");
    if (token) {
      setUser({ id: 1, fullName: "Nguyen Van A", avatar: null });
      setCartCount(3);
    }
    setHotWatches(sampleProducts);
  }, []);

  const handleLogin = () => {
    console.log("redirect to login");
  };

  const handleSearch = () => {
    console.log("search for", searchQuery);
  };

  const handleToggleFavorite = (id) => {
    if (!user) return handleLogin();
    setHotWatches((prev) =>
      prev.map((w) => (w.id === id ? { ...w, is_favorite: !w.is_favorite } : w))
    );
  };

  const filtered = hotWatches.filter((w) =>
    !searchQuery
      ? true
      : w.name.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <ThemeProvider theme={theme}>
      <Header
        user={user}
        onLogin={handleLogin}
        cartCount={cartCount}
        searchQuery={searchQuery}
        setSearchQuery={setSearchQuery}
        onSearch={handleSearch}
      />

      <main>
        <Routes>
          {/* Trang chủ */}
          <Route
            path="/"
            element={
              <>
                <Hero />
                <Categories />
                <Services />
                <ProductGrid
                  products={filtered}
                  onToggleFavorite={handleToggleFavorite}
                  title="THƯ VIỆN ĐỒNG HỒ ĐÃ SỬA"
                />
                <Box sx={{ py: 4 }}>
                  <Container>
                    <Typography
                      variant="h4"
                      align="center"
                      sx={{ fontWeight: 800, mb: 2 }}
                    >
                      SẢN PHẨM{" "}
                      <Box component="span" sx={{ color: "primary.main" }}>
                        BÁN CHẠY
                      </Box>
                    </Typography>

                    {/* Tabs Categories */}
                    <Box
                      sx={{
                        display: "flex",
                        justifyContent: "center",
                        mb: 3,
                      }}
                    >
                      <Paper sx={{ p: 0.5, display: "flex" }} elevation={1}>
                        {tabCategories.map((t, i) => (
                          <Button
                            key={t.id || i}
                            onClick={() => setTabIndex(i)}
                            variant={tabIndex === i ? "contained" : "text"}
                            sx={{
                              textTransform: "none",
                              bgcolor:
                                tabIndex === i ? "primary.main" : "transparent",
                              color: tabIndex === i ? "white" : "text.primary",
                              borderRadius: 2,
                            }}
                          >
                            {t.name || t}
                          </Button>
                        ))}
                      </Paper>
                    </Box>

                    {/* Grid */}
                    <Grid container spacing={3}>
                      {hotWatches.slice(0, 5).map((p) => (
                        <Grid
                          key={`bs-${p.id}`}
                          item
                          xs={12}
                          sm={6}
                          md={4}
                          lg={2}
                        >
                          <ProductCard
                            product={p}
                            onToggleFavorite={handleToggleFavorite}
                          />
                        </Grid>
                      ))}
                    </Grid>

                    {/* Nút xem thêm → sang /shop */}
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
              </>
            }
          />

          {/* Các trang khác */}
          <Route path="/shop" element={<Store />} />
          <Route path="/cart" element={<Cart />} />
          <Route path="/favorites" element={<Favorites />} />
          <Route path="/profile" element={<Profile />} />
        </Routes>
      </main>

      <Footer />

      {/* Floating Contact Buttons */}
      <Box
        sx={{
          position: "fixed",
          right: 16,
          bottom: 16,
          display: "flex",
          flexDirection: "column",
          gap: 1.5,
          zIndex: 9999,
        }}
      >
        <Tooltip title="Đặt lịch hẹn" placement="left">
          <Fab color="primary" size="medium" href="#booking">
            <CalendarMonth />
          </Fab>
        </Tooltip>

        <Tooltip title="Gọi điện" placement="left">
          <Fab color="secondary" size="medium" href="tel:0123456789">
            <Phone />
          </Fab>
        </Tooltip>

        <Tooltip title="Chat Zalo" placement="left">
          <Fab
            sx={{ bgcolor: "#0068ff", "&:hover": { bgcolor: "#0055cc" } }}
            size="medium"
            href="https://zalo.me/0123456789"
            target="_blank"
          >
            <Chat />
          </Fab>
        </Tooltip>
      </Box>
    </ThemeProvider>
  );
}
