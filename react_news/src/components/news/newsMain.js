import React from 'react';
import NewsList from './newsList';
import API from '../API';
import qs from 'qs';
import SelectSection from './selectSection';



class NewsMain extends React.Component {

    constructor() {
        super();
        this.state = {
            isMounted: false,
            authors: [],
            tags: [],
            news: [],
        }

    }


    async componentDidMount() {
        this.state.isMounted = true;
        const respNews = await API.get('news');
        const respAuthors = await API.get('authors');
        const respTags = await API.get('tags');
        this.state.isMounted && this.setState({ news: respNews.data, authors: respAuthors.data, tags: respTags.data })
    }

    componentWillUnmount() {
        this.state.isMounted = false;
    }



    getByCriteria = async (searchCriteria) => {
        const respNews = await API.get('news/searchByCriteria', {
            params: searchCriteria,
            paramsSerializer: params => {
                return qs.stringify(params)
            }
        });
        this.setState({ news: respNews.data })
    }


    render() {
        return (
            <div>
                <br />
                <SelectSection authors={this.state.authors} tags={this.state.tags} submit={this.getByCriteria} />
                <br />
                <NewsList news={this.state.news} setPage={this.props.setPage}/>
            </div>
        );
    }
}

export default NewsMain;