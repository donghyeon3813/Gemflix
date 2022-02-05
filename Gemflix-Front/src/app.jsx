import { BrowserRouter, Link, Route, Routes } from "react-router-dom";
import "./app.css";
import MovieList from "./components/movie/list";

function App() {
  return (
    <>
      <BrowserRouter>
        <nav>
          <ul>
            <li>
              <Link to="/movies">movies</Link>
            </li>
          </ul>
        </nav>
        <Routes>
          <Route path="/movies" exact element={<MovieList />}></Route>
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;
