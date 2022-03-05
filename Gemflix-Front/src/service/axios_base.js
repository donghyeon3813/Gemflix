import axios from "axios";

const httpClient = axios.create({
  baseURL: "http://localhost:9090",
  withCredentials: true,
  headers: { Authorization: null },
});

export default httpClient;
