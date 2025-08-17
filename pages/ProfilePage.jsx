// src/pages/ProfilePage.jsx
import React from "react";
import { useAuth } from "../api/useAuth"; // ✅ lấy từ hooks/useAuth.js

const ProfilePage = () => {
  const { user, logout } = useAuth();

  return (
    <div className="max-w-4xl mx-auto px-4 py-8">
      <div className="bg-white rounded-lg shadow-md p-8">
        <h1 className="text-3xl font-bold text-gray-800 mb-6">
          Thông tin cá nhân
        </h1>
        {user ? (
          <>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Họ và tên
                </label>
                <input
                  type="text"
                  value={user.fullName || ""}
                  disabled
                  className="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-100"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Email
                </label>
                <input
                  type="email"
                  value={user.email || ""}
                  disabled
                  className="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-100"
                />
              </div>
              <div className="md:col-span-2">
                <div className="text-sm text-gray-600 bg-gray-50 p-4 rounded-md">
                  <p>
                    <strong>User ID:</strong> {user.userId}
                  </p>
                  <p>
                    <strong>Role ID:</strong> {user.roleId}
                  </p>
                  <p>
                    <strong>Token được lưu trong localStorage</strong>
                  </p>
                </div>
              </div>
            </div>

            {/* 🔹 Nút đăng xuất */}
            <div className="mt-8">
              <button
                onClick={logout}
                className="px-4 py-2 bg-red-500 text-white rounded-md hover:bg-red-600 transition"
              >
                Đăng xuất
              </button>
            </div>
          </>
        ) : (
          <p className="text-gray-600">Bạn chưa đăng nhập.</p>
        )}
      </div>
    </div>
  );
};

export default ProfilePage;
