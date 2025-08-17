// file path: src/api/index.js
// Centralized API helpers
export const baseURL = "http://localhost:8080";

// Generate headers with Authorization token if available
const authHeaders = () => {
  const token = localStorage.getItem("jwt_token");
  return token
    ? { Authorization: `Bearer ${token}`, "Content-Type": "application/json" }
    : { "Content-Type": "application/json" };
};

// Build query string from object
const q = (obj = {}) =>
  Object.entries(obj)
    .filter(([, v]) => v !== undefined && v !== null && v !== "")
    .map(([k, v]) => `${encodeURIComponent(k)}=${encodeURIComponent(v)}`)
    .join("&");

// === Watch-related APIs ===
export async function getWatches({
  page = 0,
  limit = 12,
  brandName,
  categoryName,
  search,
  sortBy = "updatedAt",
  order = "desc",
}) {
  const query = q({
    page,
    limit,
    brandName,
    categoryName,
    search,
    sortBy,
    order,
  });
  const res = await fetch(`${baseURL}/api/v1/watch?${query}`, {
    headers: authHeaders(),
  });
  if (!res.ok) throw new Error("Failed to fetch watches");
  return res.json(); // { watch: [...], totalPage: number }
}

// === Brand & Category APIs ===
export async function getBrands() {
  const res = await fetch(`${baseURL}/api/v1/brand/get-all`, {
    headers: authHeaders(),
  });
  if (!res.ok) throw new Error("Failed to fetch brands");
  return res.json(); // → [{id, name}, ...]
}

export async function getCategories() {
  const res = await fetch(`${baseURL}/api/v1/category/get-all`, {
    headers: authHeaders(),
  });
  if (!res.ok) throw new Error("Failed to fetch categories");
  return res.json(); // → [{id, name}, ...]
}

// === Favorite API ===
export async function toggleFavorite(watchId) {
  const res = await fetch(`${baseURL}/api/v1/favorite/toggle`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify({ watch_id: watchId }),
  });
  if (!res.ok) throw new Error("Failed to toggle favorite");
  return res.json();
}

// === Cart API ===
export async function addToCart({ userId, watchId, quantity = 1 }) {
  const res = await fetch(`${baseURL}/api/v1/cart/add-cart`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify({ user_id: userId, watch_id: watchId, quantity }),
  });
  if (!res.ok) throw new Error("Failed to add to cart");
  return res.json();
}

// === Auth APIs (Mocked) ===
export const sendOTP = async (email) => {
  await new Promise((resolve) => setTimeout(resolve, 1500));
  console.log("Sending OTP to:", email);
  return { success: true };
};

export const verifyOTP = async (email, otp) => {
  await new Promise((resolve) => setTimeout(resolve, 1000));
  console.log("Verifying OTP:", email, otp);
  return { success: true };
};

export const register = async (userData) => {
  await new Promise((resolve) => setTimeout(resolve, 2000));
  console.log("Registering user:", userData);
  return { success: true, message: "Registration successful" };
};

// eslint-disable-next-line no-unused-vars
export const login = async (email, password) => {
  await new Promise((resolve) => setTimeout(resolve, 1500));
  console.log("Logging in:", email);
  return {
    success: true,
    data: {
      token:
        "eyJhbGciOiJIUzI1NiJ9.eyJwaG9uZU51bWJlciI6IjA4NjU2ODY1NDAiLCJ1c2VySWQiOjUsInN1YiI6IjA4NjU2ODY1NDAiLCJleHAiOjE3NTY2NTcwMzF9.r_PqngR8CYFP_jbuJfH0B-E8qMCcIfWuaKdcPFpREw0",
      userId: 5,
      // cspell:disable-next-line
      fullName: "Nguyễn Văn A",
      roleId: 2,
      imageUrl: null,
      email: email,
    },
  };
};
