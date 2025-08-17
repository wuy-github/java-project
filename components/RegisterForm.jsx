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
  Stepper,
  Step,
  StepLabel,
  Grid,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Fade,
} from "@mui/material";
import { Visibility, VisibilityOff, CheckCircle } from "@mui/icons-material";
import OTPTimer from "./OTPTimer";
import * as api from "../api";

const RegisterForm = ({ switchToLogin, closeModal }) => {
  const [formData, setFormData] = useState({
    email: "",
    otp: "",
    fullname: "",
    phoneNumber: "",
    password: "",
    retypePassword: "",
    address: "",
    job: "",
    sex: "nam",
  });

  const [otpSent, setOtpSent] = useState(false);
  const [otpVerified, setOtpVerified] = useState(false);
  const [currentStep, setCurrentStep] = useState(0);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [showRetypePassword, setShowRetypePassword] = useState(false);
  const [isRegistering, setIsRegistering] = useState(false);

  const steps = ["Xác thực OTP", "Thông tin"];

  const handleInputChange = (field, value) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
    setError("");
  };

  const validateEmail = (email) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  const validatePassword = (password) =>
    password.length >= 8 &&
    /[A-Z]/.test(password) &&
    /[a-z]/.test(password) &&
    /[^A-Za-z0-9]/.test(password);

  const handleSendOTP = async () => {
    if (!formData.email || !validateEmail(formData.email)) {
      setError("Vui lòng nhập email hợp lệ");
      return;
    }
    setLoading(true);
    try {
      await api.sendOTP(formData.email);
      setOtpSent(true);
      setSuccess("Mã OTP đã được gửi đến email của bạn");
    } catch (err) {
      setError(err.message || "Không thể gửi OTP. Vui lòng thử lại.");
    } finally {
      setLoading(false);
    }
  };

  const handleVerifyOTP = async () => {
    if (!formData.otp || formData.otp.length !== 6) {
      setError("Vui lòng nhập mã OTP 6 số");
      return;
    }
    setLoading(true);
    try {
      await api.verifyOTP(formData.email, formData.otp);
      setOtpVerified(true);
      setCurrentStep(1);
      setSuccess("Xác thực OTP thành công");
    } catch (err) {
      setError(err.message || "Mã OTP không chính xác");
    } finally {
      setLoading(false);
    }
  };

  const handleRegister = async () => {
    if (!otpVerified) {
      setError("Vui lòng xác thực OTP trước khi đăng ký");
      return;
    }
    if (!formData.fullname || !formData.phoneNumber || !formData.password) {
      setError("Vui lòng điền đầy đủ thông tin");
      return;
    }
    if (!validatePassword(formData.password)) {
      setError(
        "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường và ký tự đặc biệt"
      );
      return;
    }
    if (formData.password !== formData.retypePassword) {
      setError("Mật khẩu nhập lại không khớp");
      return;
    }

    setLoading(true);
    setIsRegistering(true);
    try {
      await api.register(formData);
      setSuccess("Đăng ký thành công!");

      // Hiển thị thông báo thành công trong 2 giây, sau đó chuyển sang login
      setTimeout(() => {
        setSuccess("Chuyển sang trang đăng nhập...");
        setTimeout(() => {
          switchToLogin();
          // Reset form state
          setFormData({
            email: formData.email, // Giữ lại email để user không phải nhập lại
            otp: "",
            fullname: "",
            phoneNumber: "",
            password: "",
            retypePassword: "",
            address: "",
            job: "",
            sex: "nam",
          });
          setOtpSent(false);
          setOtpVerified(false);
          setCurrentStep(0);
          setError("");
          setSuccess("");
          setIsRegistering(false);
        }, 1000);
      }, 1500);
    } catch (err) {
      setError(err.message || "Đăng ký thất bại. Vui lòng thử lại.");
      setIsRegistering(false);
    } finally {
      setLoading(false);
    }
  };

  // Hiển thị màn hình thành công
  if (isRegistering && success && !error) {
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
            Đăng ký thành công!
          </Typography>
          <Typography variant="body1" align="center" color="text.secondary">
            Tài khoản của bạn đã được tạo thành công.
            <br />
            Đang chuyển sang trang đăng nhập...
          </Typography>
          <CircularProgress color="success" />
        </Box>
      </Fade>
    );
  }

  return (
    <Box>
      {/* Stepper */}
      <Stepper activeStep={currentStep} sx={{ mb: 4 }}>
        {steps.map((label) => (
          <Step key={label}>
            <StepLabel>{label}</StepLabel>
          </Step>
        ))}
      </Stepper>

      {/* Error and Success Messages */}
      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}
      {success && !isRegistering && (
        <Alert severity="success" sx={{ mb: 2 }}>
          {success}
        </Alert>
      )}

      {/* Step 1: OTP Verification */}
      {currentStep === 0 && (
        <Box display="flex" flexDirection="column" gap={2}>
          <TextField
            label="Email"
            type="email"
            fullWidth
            value={formData.email}
            onChange={(e) => handleInputChange("email", e.target.value)}
            disabled={otpSent}
            placeholder="Nhập email của bạn"
          />

          {!otpSent ? (
            <Button
              variant="contained"
              color="primary"
              fullWidth
              onClick={handleSendOTP}
              disabled={loading}
              sx={{ borderRadius: 25, py: 1.5 }}
            >
              {loading ? (
                <CircularProgress size={24} color="inherit" />
              ) : (
                "Gửi mã OTP"
              )}
            </Button>
          ) : (
            <>
              <TextField
                label="Mã OTP"
                type="text"
                fullWidth
                value={formData.otp}
                onChange={(e) => handleInputChange("otp", e.target.value)}
                inputProps={{ maxLength: 6 }}
                placeholder="Nhập 6 số OTP"
              />

              <OTPTimer
                duration={300}
                isActive={otpSent && !otpVerified}
                onExpire={() => {
                  setOtpSent(false);
                  handleInputChange("otp", "");
                }}
              />

              <Button
                variant="contained"
                color="primary"
                fullWidth
                onClick={handleVerifyOTP}
                disabled={loading || otpVerified}
                sx={{ borderRadius: 25, py: 1.5 }}
              >
                {loading ? (
                  <CircularProgress size={24} color="inherit" />
                ) : (
                  "Xác thực OTP"
                )}
              </Button>

              {!otpVerified && (
                <Button
                  variant="outlined"
                  color="primary"
                  fullWidth
                  onClick={() => {
                    setOtpSent(false);
                    handleInputChange("otp", "");
                    setSuccess("");
                  }}
                  sx={{ borderRadius: 25, py: 1.2 }}
                >
                  Gửi lại mã OTP
                </Button>
              )}
            </>
          )}

          <Typography align="center" variant="body2">
            Đã có tài khoản?{" "}
            <Button onClick={switchToLogin} color="primary">
              Đăng nhập
            </Button>
          </Typography>
        </Box>
      )}

      {/* Step 2: User Information */}
      {currentStep === 1 && (
        <Box display="flex" flexDirection="column" gap={2}>
          <Grid container spacing={2}>
            <Grid item xs={12}>
              <TextField
                label="Họ và tên"
                fullWidth
                value={formData.fullname}
                onChange={(e) => handleInputChange("fullname", e.target.value)}
                placeholder="Nhập họ và tên"
              />
            </Grid>

            <Grid item xs={12}>
              <TextField
                label="Số điện thoại"
                type="tel"
                fullWidth
                value={formData.phoneNumber}
                onChange={(e) =>
                  handleInputChange("phoneNumber", e.target.value)
                }
                placeholder="Nhập số điện thoại"
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                label="Mật khẩu"
                type={showPassword ? "text" : "password"}
                fullWidth
                value={formData.password}
                onChange={(e) => handleInputChange("password", e.target.value)}
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton
                        onClick={() => setShowPassword(!showPassword)}
                      >
                        {showPassword ? <VisibilityOff /> : <Visibility />}
                      </IconButton>
                    </InputAdornment>
                  ),
                }}
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                label="Nhập lại mật khẩu"
                type={showRetypePassword ? "text" : "password"}
                fullWidth
                value={formData.retypePassword}
                onChange={(e) =>
                  handleInputChange("retypePassword", e.target.value)
                }
                placeholder="Nhập lại mật khẩu"
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton
                        onClick={() =>
                          setShowRetypePassword(!showRetypePassword)
                        }
                      >
                        {showRetypePassword ? (
                          <VisibilityOff />
                        ) : (
                          <Visibility />
                        )}
                      </IconButton>
                    </InputAdornment>
                  ),
                }}
              />
            </Grid>

            <Grid item xs={12}>
              <TextField
                label="Địa chỉ"
                fullWidth
                value={formData.address}
                onChange={(e) => handleInputChange("address", e.target.value)}
                placeholder="Nhập địa chỉ"
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                label="Nghề nghiệp"
                fullWidth
                value={formData.job}
                onChange={(e) => handleInputChange("job", e.target.value)}
                placeholder="Nhập nghề nghiệp"
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <FormControl fullWidth>
                <InputLabel>Giới tính</InputLabel>
                <Select
                  value={formData.sex}
                  label="Giới tính"
                  onChange={(e) => handleInputChange("sex", e.target.value)}
                >
                  <MenuItem value="nam">Nam</MenuItem>
                  <MenuItem value="nu">Nữ</MenuItem>
                </Select>
              </FormControl>
            </Grid>
          </Grid>

          <Box display="flex" gap={2} mt={2}>
            <Button
              variant="outlined"
              color="primary"
              fullWidth
              onClick={() => setCurrentStep(0)}
              sx={{ borderRadius: 25, py: 1.2 }}
              disabled={loading}
            >
              Quay lại
            </Button>
            <Button
              variant="contained"
              color="primary"
              fullWidth
              onClick={handleRegister}
              disabled={loading}
              sx={{ borderRadius: 25, py: 1.2 }}
            >
              {loading ? (
                <CircularProgress size={24} color="inherit" />
              ) : (
                "Đăng ký"
              )}
            </Button>
          </Box>
        </Box>
      )}
    </Box>
  );
};

export default RegisterForm;
