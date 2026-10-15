import axios from "axios";

const api = axios.create({
  baseURL: `${import.meta.env.VITE_BACK_END_URL}/api`,
  withCredentials: true,
});

api.interceptors.request.use(
  (config) => {
    const auth = localStorage.getItem("auth");

    if (auth) {
      const user = JSON.parse(auth);

      if (user?.jwtToken) {
        const token = user.jwtToken.includes("=")
          ? user.jwtToken.substring(user.jwtToken.indexOf("=") + 1)
          : user.jwtToken;

        config.headers.Authorization = `Bearer ${token}`;
      }
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

export default api;
