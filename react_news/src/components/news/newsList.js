import React from 'react';
import News from './news';
import './news.css';
import i18next from 'i18next';
import PerPage from './perPage';
import Pagination from './pagination';

class NewsList extends React.Component {
    constructor(props) {
        super(props),
            this.state = {
                news: [],
                newsPerPage: 5,
                currentPage: 1
            }
    }



    static getDerivedStateFromProps(props, state) {
        const news = [];
        props.news.forEach(function (item, index, array) {
            let page = Math.floor(index / state.newsPerPage);
            !news[page] && (news[page] = new Array())
            news[page].push(item);
        })
        state.news = news;
        return state
    }

    paddingHandle = (event) => {
        this.setState({ currentPage: parseInt(event.target.value) })
        this.scrollTop()
    }

    changePerPage = (event) => {
        this.setState({ newsPerPage: parseInt(event.target.value), currentPage: 1 });
        this.scrollTop();
    }

    scrollTop = () => {
        if (document.documentElement.scrollTop > 0) {
            window.scrollBy(0, -100)
            setTimeout(this.scrollTop, 15)
        }
    }

    render() {
        return (
            <div style={{ marginBottom: '50px' }}>
                {this.state.news[this.state.currentPage - 1] && this.state.news[this.state.currentPage - 1].map(news =>
                    <div key={news.id} className="container">
                        <div className="nav-scroller py-1 mb-2 ">
                            <div className=" jumbotron p-3 p-md-5 text-white rounded bg-dark">
                                <div className="col-md-6 px-0">
                                    <h1 className="display-4 font-italic">{news.title}</h1>
                                    <p className="lead my-3">{news.shortText}</p>
                                    <p className="lead mb-0">
                                        <button onClick={() => {this.props.setPage(News, { news: news }) }} className="goto-button">{i18next.t('cont_read')}</button>
                                    </p>
                                </div>
                                <div className="date ">
                                    <div>{i18next.t('create_date')} {news.creationDate}</div>
                                    <div>{i18next.t('modific_date')} {news.modificationDate}</div>
                                </div>
                            </div>
                        </div>
                    </div>

                )
                }
                {this.state.news.length > 1 &&
                <Pagination news={this.state.news} currentPage={this.state.currentPage} paddingHandle={this.paddingHandle}/>
                }
                <PerPage current={this.state.newsPerPage} change={this.changePerPage} />
            </div>
        );
    }
}

export default NewsList;

