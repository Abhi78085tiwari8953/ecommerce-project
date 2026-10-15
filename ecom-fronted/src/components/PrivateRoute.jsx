import React from "react";
import { useSelector } from "react-redux";
import { Navigate, Outlet, useLocation } from "react-router-dom";

const PrivateRoute = ({
  publicPage = false,
  adminOnly = false,
}) => {
  const { user } = useSelector((state) => state.auth);

  const isAdmin =
    user?.roles?.includes("ROLE_ADMIN");

  const isSeller =
    user?.roles?.includes("ROLE_SELLER");

  const location = useLocation();

  // Login / Register pages
  if (publicPage) {
    return user ? <Navigate to="/" /> : <Outlet />;
  }

  // Admin/Seller dashboard routes
  if (adminOnly) {
    // Seller is not allowed to access other admin pages
    if (isSeller && !isAdmin) {
      const sellerAllowedPaths = [
        "/admin/orders",
        "/admin/products",
        "/admin/categories",
      ];

      const sellerAllowed = sellerAllowedPaths.some(
        (path) => location.pathname.startsWith(path)
      );

      if (!sellerAllowed) {
        return <Navigate to="/" replace />;
      }
    }
  }

  // Normal private routes
  return user ? (
    <Outlet />
  ) : (
    <Navigate to="/login" />
  );
};

export default PrivateRoute;