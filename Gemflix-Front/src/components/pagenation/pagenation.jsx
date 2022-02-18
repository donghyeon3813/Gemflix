import React, { useEffect, useState } from "react";

const Pagenation = (props) => {
  const total = props.total;
  const limit = props.limit;
  const page = props.page;
  const setPage = props.setPage;
  const numPages = Math.ceil(total / limit);
  console.log(numPages);

  return (
    <>
      <nav>
        <button onClick={() => setPage(page - 1)} disabled={page === 0}>
          &lt;
        </button>
        {Array(numPages)
          .fill()
          .map((_, i) => (
            <button
              key={i}
              onClick={() => setPage(i)}
              aria-current={page === i + 1 ? "page" : null}
            >
              {i + 1}
            </button>
          ))}
        <button
          onClick={() => setPage(page + 1)}
          disabled={page + 1 === numPages}
        >
          &gt;
        </button>
      </nav>
    </>
  );
};

export default Pagenation;
