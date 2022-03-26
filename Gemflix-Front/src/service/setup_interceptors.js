import axios from "axios";
import { userLogin } from "../store/actions";
import store from "../store/store";

const setupInterceptors = (httpClient) => {
  const { dispatch } = store;

  // Add a request interceptor
  httpClient.interceptors.request.use(
    function (config) {
      const token = store.getState().userReducer.token;
      const accessToken = token ? token : null;
      config.headers = { Authorization: "Bearer " + accessToken };
      return config;
    },
    function (error) {
      return Promise.reject(error);
    }
  );

  // Add a response interceptor
  httpClient.interceptors.response.use(
    function (response) {
      return response;
    },
    function (error) {
      const {
        config,
        response: { status },
      } = error;
      const originalRequest = config;

      console.log(status);
      if (status === 401) {
        const response = error.response.data;
        const code = response.code;

        if (code === 1007) {
          //accessToken 만료 -> 재발급
          return axios
            .post("/auth/refresh")
            .then(function (success) {
              const response = success.data;
              const code = response.code;

              if (code === 1000) {
                const accessToken = response.data.accessToken;
                const memberId = response.data.memberId;
                const memberRole = response.data.memberRole;

                dispatch(userLogin(accessToken, memberId, memberRole));
                originalRequest.headers = {
                  Authorization: "Bearer " + accessToken,
                };
                return axios(originalRequest);
              }
            })
            .catch(function (error) {
              console.log("refresh requset fail : " + error);
              return JSON.parse(error.request.response);
            })
            .finally(() => {
              console.log("refresh request end");
            });
        }
      }
      return Promise.reject(error);
    }
  );
  return httpClient;
};

export default setupInterceptors;
