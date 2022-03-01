const MoveTrailerPopUp = (props) => {
  const trLocation = props.trLocation;
  const handleClosePop = props.handleClosePop;
  return (
    <>
      <div className="modal" onClick={() => handleClosePop()}>
        <div className="modal-content">
          <div className="modal-head">
            <label className="modal-head-item" onClick={() => handleClosePop()}>
              ✖
            </label>
          </div>
          <iframe className="modal-item" src={trLocation}></iframe>
        </div>
      </div>
    </>
  );
};

export default MoveTrailerPopUp;
