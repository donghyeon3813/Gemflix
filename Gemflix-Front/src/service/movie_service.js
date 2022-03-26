import setupInterceptors from "./setup_interceptors";

class MovieService {
  constructor(httpClient) {
    this.server = setupInterceptors(httpClient);
  }
  async movies(data) {
    return await this.server
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
    return await this.server
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
    return await this.server
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
    return await this.server
      .get("/movie/reviews", { params: data })
      .then(function (success) {
        console.log(success);
        return success.data;
      })
      .catch(function (error) {
        return error.request.response;
      });
  }

  async reviewModify(data, accessToken) {
    return await this.server
      .put("/movie/review", data, {
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

  async reviewDelete(rvId, accessToken) {
    return await this.server
      .delete("/movie/review/" + rvId, null, {
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
}

export default MovieService;
