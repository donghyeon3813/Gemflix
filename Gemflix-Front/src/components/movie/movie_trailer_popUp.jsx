const MoveTrailerPopUp = (props) => {
  const trLocation = props.trLocation;
  return (
    <>
      <div>
        <iframe width="100%" height="100%" src={trLocation}></iframe>
      </div>
    </>
  );
};

export default MoveTrailerPopUp;
