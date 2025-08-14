// src/theme/theme.js (FIXED)
// – Change background to light, export SearchInput (unchanged)
// =============================================

import { createTheme, styled } from "@mui/material/styles";

export const theme = createTheme({
  palette: {
    primary: { main: "#8B0000" },
    secondary: { main: "#f50057" },
    background: {
      default: "#ffffff", // fixed: light app background
    },
  },
  typography: {
    fontFamily: "Roboto, Arial, sans-serif",
  },
});

export const SearchInput = styled("div")(({ theme }) => ({
  display: "flex",
  alignItems: "center",
  background: "#f5f5f5",
  borderRadius: 25,
  padding: "6px 10px",
  width: "100%",
  maxWidth: 320,
  [theme.breakpoints.down("md")]: {
    maxWidth: 200,
  },
}));
