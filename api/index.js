// Centralized API helpers
export const baseURL = "http://localhost:8080";

const authHeaders = () => {
  const token = localStorage.getItem("jwt_token");
  return token
    ? { Authorization: `Bearer ${token}`, "Content-Type": "application/json" }
    : { "Content-Type": "application/json" };
};

const q = (obj = {}) =>
  Object.entries(obj)
    .filter(([, v]) => v !== undefined && v !== null && v !== "")
    .map(([k, v]) => `${encodeURIComponent(k)}=${encodeURIComponent(v)}`)
    .join("&");

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

export async function toggleFavorite(watchId) {
  const res = await fetch(`${baseURL}/api/v1/favorite/toggle`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify({ watch_id: watchId }),
  });
  if (!res.ok) throw new Error("Failed to toggle favorite");
  return res.json();
}

export async function addToCart({ userId, watchId, quantity = 1 }) {
  const res = await fetch(`${baseURL}/api/v1/cart/add-cart`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify({ user_id: userId, watch_id: watchId, quantity }),
  });
  if (!res.ok) throw new Error("Failed to add to cart");
  return res.json();
}
