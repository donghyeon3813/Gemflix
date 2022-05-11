import React, { useEffect, useState } from "react";
import "./pagenation.css";

const Pagenation = (props) => {
  const total = props.total;
  const limit = props.limit;
  const page = props.page;
  const setPage = props.setPage;
  const totalPageNumbers = [];
  for (let i = 0; i < Math.ceil(total / limit); i++) {
    totalPageNumbers.push(i);
  }
  const pageArr = [];
  for (let i = 0; i < totalPageNumbers.length; i += 5) {
    pageArr.push(totalPageNumbers.slice(i, i + 5));
  }
  let arrNumber = Math.floor(page / 5);

  const numPages = Math.ceil(total / limit);

  return (
    <>
      <nav className="pagenation-group">
        <button
          className="pagenation-btn"
          onClick={() => setPage(page - 1)}
          disabled={page === 0}
        >
          &lt;
        </button>
        {pageArr.length > 0
          ? pageArr[arrNumber].map((pageNum) => (
              <button
                key={pageNum}
                className={page === pageNum ? "active-page" : "pagenation-btn"}
                onClick={() => setPage(pageNum)}
                aria-current={page === pageNum + 1 ? "page" : null}
              >
                {pageNum + 1}
              </button>
            ))
          : null}
        <button
          onClick={() => setPage(page + 1)}
          className={page + 1 === numPages ? "disable-page" : "pagenation-btn"}
          disabled={page + 1 === numPages}
        >
          &gt;
        </button>
      </nav>
    </>
  );
};

export default Pagenation;
