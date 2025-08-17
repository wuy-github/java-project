import React, { useState } from "react";
import {
  TextField,
  Button,
  Typography,
  InputAdornment,
  IconButton,
  CircularProgress,
  Box,
  Alert,
  Fade,
} from "@mui/material";
import { Visibility, VisibilityOff, CheckCircle } from "@mui/icons-material";
import * as api from "../api";
import { useAuth } from "../api/useAuth";

const LoginForm = ({ switchToRegister, closeModal }) => {
  const { login } = useAuth();
  const [formData, setFormData] = useState({ email: "", password: "" });
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [isLoggingIn, setIsLoggingIn] = useState(false);

  const handleChange = (field, value) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
    setError("");
  };

  const togglePasswordVisibility = () => {
    setShowPassword((prev) => !prev);
  };

  const handleLogin = async () => {
    if (!formData.email || !formData.password) {
      setError("Vui lòng nhập email và mật khẩu");
      return;
    }

    setLoading(true);
    setIsLoggingIn(true);

    try {
      const response = await api.login(formData.email, formData.password);
      if (response.success) {
        login(response.data, response.data.token);
        setSuccess("Bạn đã đăng nhập thành công!");

        // Hiển thị thông báo thành công trong 2 giây, sau đó đóng modal
        setTimeout(() => {
          setSuccess("Chào mừng bạn trở lại!");
          setTimeout(() => {
            closeModal();
            // Reset form state
            setFormData({ email: "", password: "" });
            setError("");
            setSuccess("");
            setIsLoggingIn(false);
          }, 1000);
        }, 1500);
      }
    } catch (err) {
      setError(err.message || "Đăng nhập thất bại. Vui lòng thử lại.");
      setIsLoggingIn(false);
    } finally {
      setLoading(false);
    }
  };

  // Hiển thị màn hình thành công
  if (isLoggingIn && success && !error) {
    return (
      <Fade in={true}>
        <Box
          display="flex"
          flexDirection="column"
          alignItems="center"
          gap={3}
          sx={{ py: 4 }}
        >
          <CheckCircle
            sx={{
              fontSize: 80,
              color: "success.main",
              animation: "pulse 1.5s ease-in-out infinite",
            }}
          />
          <Typography variant="h5" align="center" color="success.main">
            Bạn đã đăng nhập thành công!
          </Typography>
          <Typography variant="body1" align="center" color="text.secondary">
            {success === "Chào mừng bạn trở lại!"
              ? "Chào mừng bạn trở lại!"
              : "Bạn đã đăng nhập thành công vào hệ thống."}
          </Typography>
          <CircularProgress color="success" />
        </Box>
      </Fade>
    );
  }

  return (
    <Box display="flex" flexDirection="column" gap={2}>
      <Typography variant="h5" align="center" sx={{ mb: 2, fontWeight: 600 }}>
        Đăng nhập
      </Typography>

      {/* Error and Success Messages */}
      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}
      {success && !isLoggingIn && (
        <Alert severity="success" sx={{ mb: 2 }}>
          {success}
        </Alert>
      )}

      <TextField
        label="Email"
        type="email"
        fullWidth
        value={formData.email}
        onChange={(e) => handleChange("email", e.target.value)}
        placeholder="Nhập email của bạn"
        disabled={loading}
        sx={{
          "& .MuiOutlinedInput-root": {
            borderRadius: 2,
          },
        }}
      />

      <TextField
        label="Mật khẩu"
        type={showPassword ? "text" : "password"}
        fullWidth
        value={formData.password}
        onChange={(e) => handleChange("password", e.target.value)}
        placeholder="Nhập mật khẩu của bạn"
        disabled={loading}
        sx={{
          "& .MuiOutlinedInput-root": {
            borderRadius: 2,
          },
        }}
        InputProps={{
          endAdornment: (
            <InputAdornment position="end">
              <IconButton
                aria-label="toggle password visibility"
                onClick={togglePasswordVisibility}
                onMouseDown={(e) => e.preventDefault()}
                disabled={loading}
                edge="end"
                size="small"
                sx={{
                  color: "action.active",
                  "&:hover": {
                    backgroundColor: "action.hover",
                  },
                }}
              >
                {showPassword ? (
                  <VisibilityOff fontSize="small" />
                ) : (
                  <Visibility fontSize="small" />
                )}
              </IconButton>
            </InputAdornment>
          ),
        }}
      />

      <Button
        variant="contained"
        color="primary"
        fullWidth
        onClick={handleLogin}
        disabled={loading}
        sx={{
          borderRadius: 25,
          py: 1.5,
          fontSize: "1rem",
          fontWeight: 600,
          textTransform: "none",
          boxShadow: 2,
          "&:hover": {
            boxShadow: 4,
          },
        }}
      >
        {loading ? (
          <Box display="flex" alignItems="center" gap={1}>
            <CircularProgress size={20} color="inherit" />
            <span>Đang đăng nhập...</span>
          </Box>
        ) : (
          "Đăng nhập"
        )}
      </Button>

      {/* Divider */}
      <Box display="flex" alignItems="center" gap={2} sx={{ my: 2 }}>
        <Box flex={1} height={1} bgcolor="divider" />
        <Typography variant="body2" color="text.secondary">
          hoặc
        </Typography>
        <Box flex={1} height={1} bgcolor="divider" />
      </Box>

      {/* Switch to Register */}
      <Typography align="center" variant="body2" color="text.secondary">
        Chưa có tài khoản?{" "}
        <Button
          onClick={switchToRegister}
          color="primary"
          disabled={loading}
          sx={{
            textTransform: "none",
            fontWeight: 600,
            "&:hover": { textDecoration: "underline" },
          }}
        >
          Đăng ký ngay
        </Button>
      </Typography>
    </Box>
  );
};

export default LoginForm;
