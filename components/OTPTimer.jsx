// file path: src/components/OTPTimer.jsx
import React, { useState, useEffect } from "react";

const OTPTimer = ({ duration, onExpire, isActive }) => {
  const [timeLeft, setTimeLeft] = useState(duration);

  useEffect(() => {
    if (!isActive) {
      setTimeLeft(duration);
      return;
    }

    const timer = setInterval(() => {
      setTimeLeft((prev) => {
        if (prev <= 1) {
          onExpire();
          return duration;
        }
        return prev - 1;
      });
    }, 1000);

    return () => clearInterval(timer);
  }, [isActive, onExpire, duration]);

  const minutes = Math.floor(timeLeft / 60);
  const seconds = timeLeft % 60;

  return (
    <div className="text-sm text-gray-500 mt-2">
      {isActive && timeLeft > 0
        ? `Mã OTP sẽ hết hạn sau ${minutes}:${seconds
            .toString()
            .padStart(2, "0")}`
        : "Mã OTP đã hết hạn"}
    </div>
  );
};

export default OTPTimer;
