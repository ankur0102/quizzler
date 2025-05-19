import axios from "axios";

const backendUrl = process.env.REACT_APP_BACKEND_URL || "http://localhost:8080";
console.log("Backend url is ", backendUrl);

const api = axios.create({
    baseURL: `${backendUrl}/api-server/v1`,
});

export default api;