import React from "react";
import { Box, Pagination, Typography, Stack } from "@mui/material";

export default function PaginationBar({ page, totalPages, onChange }) {
  if (totalPages <= 1) return null;
  return (
    <Stack
      direction={{ xs: "column", sm: "row" }}
      spacing={2}
      alignItems="center"
      justifyContent="space-between"
      sx={{ mt: 3 }}
    >
      <Typography variant="body2">
        Trang <b>{page}</b> / <b>{totalPages}</b>
      </Typography>
      <Box>
        <Pagination
          page={page}
          count={totalPages}
          color="primary"
          shape="rounded"
          onChange={(_, p) => onChange(p)}
          showFirstButton
          showLastButton
        />
      </Box>
    </Stack>
  );
}
