import httpClient from "./axios_base";

class MovieService {
  async movies(data) {
    return await httpClient
      .get("/movie/list", { params: data })
      .then(function (success) {
        console.log(success);
        return success.data;
      })
      .catch(function (error) {
        return error.request.response;
      });
  }

  async movieDetail(data) {
    console.log(data);
    return await httpClient
      .get("movie/details", { params: data })
      .then(function (success) {
        console.log(success);
        return success.data;
      })
      .catch(function (error) {
        return error.request.response;
      });
  }

  async reviewRegister(data, accessToken) {
    return await httpClient
      .post("/movie/review", data, {
        headers: { Authorization: "Bearer " + accessToken },
      })
      .then(function (success) {
        console.log(success);
        return success.data;
      })
      .catch(function (error) {
        return JSON.parse(error.request.response);
      });
  }

  async reviews(data) {
    return await httpClient
      .get("/movie/reviews", { params: data })
      .then(function (success) {
        console.log(success);
        return success.data;
      })
      .catch(function (error) {
        return error.request.response;
      });
  }
}

export default MovieService;
