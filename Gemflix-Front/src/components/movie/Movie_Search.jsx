import { AiOutlineSearch } from "react-icons/ai";
const MovieSearch = (props) => {
  const setTitle = props.setTitle;
  return (
    <>
      <div>
        <div className="movie-search-group">
          <input
            className="movie-search"
            type={"text"}
            onChange={(e) => {
              setTitle(e.target.value);
            }}
            placeholder="search"
          />
          <AiOutlineSearch
            size="24"
            className="search-icon"
            color="mediumpurple"
          />
        </div>
      </div>
    </>
  );
};

export default MovieSearch;
