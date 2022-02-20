const MovieSearch = (props) => {
  const setTitle = props.setTitle;
  return (
    <>
      <div>
        <input
          type={"text"}
          onChange={(e) => {
            setTitle(e.target.value);
          }}
        ></input>
      </div>
    </>
  );
};

export default MovieSearch;
