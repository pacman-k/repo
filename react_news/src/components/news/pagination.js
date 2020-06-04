import React from 'react';

const Pagination = (props) => (
    <nav>
        <ul className="pagination">
            {props.news[props.currentPage - 2] &&
                <li className="page-item">
                    <button value={props.currentPage - 1} onClick={props.paddingHandle} className="page-link" href="#" aria-label="Previous">
                        &laquo;
                                    </button>
                </li>
            }
            {props.news.map((item, index, array) => {
                let current = (index + 1 == props.currentPage)
                return <li key={index} style={current ? { textDecoration: 'underline' } : {}} className='page-item'><button value={index + 1} onClick={props.paddingHandle} className="page-link">{index + 1}</button></li>
            })

            }

            {props.news[props.currentPage] &&
                <li className="page-item">
                    <button value={props.currentPage + 1} onClick={props.paddingHandle} className="page-link" aria-label="Next">
                        &raquo;
                                    </button>
                </li>
            }
        </ul>
    </nav>
);

export default Pagination;