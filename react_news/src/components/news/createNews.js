import React from 'react';
import i18next from 'i18next';
import Select from 'react-select';
import API from "../API";
import News from './news';

class CreateNews extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            authors: [],
            selectedAuthorId: undefined
        }
    }

    async componentDidMount() {
        const authorsResp = await API.get('authors');
        const authors = authorsResp.data.map(author => ({ value: author.id, label: author.name + ' ' + author.surname }));
        this.setState({ authors: authors })
    }

    handleSubmit = async (event) => {
        event.preventDefault();
        const elements = event.target.elements;
        if (this.state.selectedAuthorId) {
            const news = {
                authorDto: {
                    id: this.state.selectedAuthorId
                },
                tagList: elements.tags.value ? elements.tags.value.split(' ').map(name => ({ name: name })) : [],
                title: elements.title.value,
                shortText: elements.shortText.value,
                fullText: elements.fullText.value
            }
            const newsSaveResp = await API.post('news', news);
            const savedNews = await newsSaveResp.data;
            this.props.setPage(News, { news: savedNews })
        }

    }

    handleOnChange = (option) => {
        if (!option) {
            this.state.selectedAuthorId = undefined;
            return;
        }
        this.state.selectedAuthorId = option.value;
    }

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <div className="form-group">
                    <label htmlFor="title">Title</label>
                    <input maxLength={30} required type="text" className="form-control" id="title" placeholder="Enter Title" />
                </div>
                <div className="form-group">
                    <label htmlFor="authorSelector">Author</label>
                    <Select onChange={this.handleOnChange} isClearable id='authorSelector' options={this.state.authors} placeholder='Select Author' />
                </div>
                <div className="form-group">
                    <label htmlFor="tags">Tags</label>
                    <input type="text" className="form-control" id="tags" placeholder="Enter Tags" />
                </div>
                <div className="form-group">
                    <label htmlFor="shortText">Short text</label>
                    <textarea required maxLength={100} rows={5} style={{ resize: "none" }} type="text" className="form-control" id="shortText" placeholder="Enter Short Text" />
                </div>
                <div className="form-group">
                    <label htmlFor="fullText">FullText</label>
                    <textarea required maxLength={2000} rows={15} style={{ resize: "none" }} type="text" className="form-control" id="fullText" placeholder="Enter Full Text" />
                </div>
                <div className="form-group" style={{ marginBottom: "50px" }}>
                    <button type="submit" className="btn btn-primary">{i18next.t('submit')}</button>
                </div>
            </form>
        );
    }
}


export default CreateNews