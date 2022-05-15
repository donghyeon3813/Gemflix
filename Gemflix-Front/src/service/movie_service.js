import setupInterceptors from "./setup_interceptors";

class MovieService {
  constructor(httpClient) {
    this.server = setupInterceptors(httpClient);
  }
  async movies(data) {
    return await this.server
      .get("/movie/list", { params: data })
      .then(function (success) {
        return success.data;
      })
      .catch(function (error) {
        return error.request.response;
      });
  }

  async movieDetail(data) {
    return await this.server
      .get("movie/details", { params: data })
      .then(function (success) {
        return success.data;
      })
      .catch(function (error) {
        return error.request.response;
      });
  }

  async reviewRegister(data, accessToken) {
    return await this.server
      .post("/movie/review", data, {
        headers: {
          Authorization: "Bearer " + accessToken,
          "Content-Type": "application/json",
        },
      })
      .then(function (success) {
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
        return success.data;
      })
      .catch(function (error) {
        return JSON.parse(error.request.response);
      });
  }

  async filmography(data) {
    return await this.server
      .get("movie/filmographys/" + data, null)
      .then(function (success) {
        return success.data;
      })
      .catch(function (error) {
        return error.request.response;
      });
  }

  async place() {
    return await this.server
      .get("/reservation/places/", null)
      .then(function (success) {
        return success.data;
      })
      .catch(function (error) {
        return error.request.response;
      });
  }
  async theater(data) {
    return await this.server
      .get("/reservation/theaters/", { params: data })
      .then(function (success) {
        return success.data;
      })
      .catch(function (error) {
        return error.request.response;
      });
  }

  async showingMovie() {
    return await this.server
      .get("/reservation/movies/", null)
      .then(function (success) {
        return success.data;
      })
      .catch(function (error) {
        return error.request.response;
      });
  }
  async screens(data) {
    return await this.server
      .get("/reservation/screens/", { params: data })
      .then(function (success) {
        return success.data;
      })
      .catch(function (error) {
        return error.request.response;
      });
  }
  async screenInfo(data) {
    return await this.server
      .get("/reservation/screen/" + data, null)
      .then(function (success) {
        return success.data;
      })
      .catch(function (error) {
        return error.request.response;
      });
  }
  //결제 endpoint
  async completePayment(data, memberId) {
    return await this.server
      .post("/payments/complete/" + memberId, data, {
        headers: { "Content-Type": "application/json" },
      })
      .then(function (success) {
        return success.data;
      })
      .catch(function (error) {
        return JSON.parse(error.request.response);
      });
  }

  async profile() {
    return await this.server
      .get("/member/profile", {})
      .then(function (success) {
        return success.data;
      })
      .catch(function (error) {
        return JSON.parse(error.request.response);
      });
  }
}

export default MovieService;
