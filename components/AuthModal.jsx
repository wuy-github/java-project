import React, { useState } from "react";
import { Dialog, Tabs, Tab, DialogContent } from "@mui/material";
import LoginForm from "./LoginForm";
import RegisterForm from "./RegisterForm";

const AuthModal = ({ open, onClose }) => {
  const [tab, setTab] = useState(0); // 0 = login, 1 = register

  return (
    <Dialog open={open} onClose={onClose} maxWidth="xs" fullWidth>
      <Tabs
        value={tab}
        onChange={(e, newValue) => setTab(newValue)}
        textColor="primary"
        indicatorColor="primary"
        centered
      >
        <Tab label="Đăng nhập" />
        <Tab label="Đăng ký" />
      </Tabs>

      <DialogContent>
        {tab === 0 ? (
          <LoginForm switchToRegister={() => setTab(1)} closeModal={onClose} />
        ) : (
          <RegisterForm switchToLogin={() => setTab(0)} closeModal={onClose} />
        )}
      </DialogContent>
    </Dialog>
  );
};

export default AuthModal;
