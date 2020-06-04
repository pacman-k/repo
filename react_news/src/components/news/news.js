import React from 'react';
import i18next from 'i18next';
import './news.css';
import API from '../API';

class News extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isUpdating: false,
            user: props.user,
            news: props.param.news,
            author: ' ' + props.param.news.authorDto.name + ' ' + props.param.news.authorDto.surname
        }
        this.updatingBox = { border: 'groove', borderRadius: '5px' }
        this.nonUpdatingBox = { border: 'none' }
    }

    handleUpdate = () => {
        this.setState({ isUpdating: true })
    }
    handleCancel = () => {
        document.getElementById('title').value = this.state.news.title
        document.getElementById('shortText').value = this.state.news.shortText
        document.getElementById('fullText').value = this.state.news.fullText
        this.setState({ isUpdating: false })
    }

    updateSubmit = async (event) => {
        event.preventDefault()
        const elements = event.target.elements;

        const updateResp = await API.put('news', {
            id: this.state.news.id,
            title: elements.title.value,
            shortText: elements.shortText.value,
            fullText: elements.fullText.value
        })
        this.setState({ news: updateResp.data, isUpdating: false })
    }

    render() {

        return (
            <main className="container" role="main">
                <form onSubmit={this.updateSubmit} style={{ marginBottom: '50px' }}>
                    <div className="row">
                        <div className="col-md-8 blog-main">
                            <div className="blog-post">
                                {(this.state.user.role === 'admin' || this.state.user.id == this.state.news.authorDto.id) && (
                                    !this.state.isUpdating ?
                                        <button type='button' onClick={this.handleUpdate} className='btn btn-primary' style={{ right: 0, position: 'absolute' }}>update</button>
                                        :
                                        <div>
                                            <button type='button' className='btn btn-danger' onClick={this.handleCancel} style={{ right: 0, position: 'absolute' }}>cancel</button>
                                            <button type='submit' className='btn btn-success' style={{ right: 80, position: 'absolute' }}>submit</button>
                                        </div>
                                )}
                                <br />
                                <br />
                                <h2 className="blog-post-title">
                                    <input id='title' required maxLength={30} className='inputField title' ref='shortText' disabled={!this.state.isUpdating} style={this.state.isUpdating ? { border: 'groove', borderRadius: '5px' } : { border: 'none' }} defaultValue={this.state.news.title} />
                                </h2>
                                <p className="blog-post-meta font-italic">
                                    {i18next.t('last_mod')} {this.state.news.creationDate}, {this.state.author}
                                </p>
                                {this.state.news.tagList.map(tag => (
                                    <div key={tag.id} style={{ display: 'inline', marginLeft: '10px', textDecoration: 'underline' }}>{tag.name}</div>
                                ))}
                                <hr />
                                <textarea id='shortText' required maxLength={100} rows={5} className='inputField shortText' disabled={!this.state.isUpdating} style={this.state.isUpdating ? this.updatingBox : this.nonUpdatingBox} defaultValue={this.state.news.shortText} />
                                <hr />
                                <textarea id='fullText' required maxLength={2000} rows={15} className='inputField fullText' disabled={!this.state.isUpdating} style={this.state.isUpdating ? this.updatingBox : this.nonUpdatingBox} defaultValue={this.state.news.fullText} />
                            </div>
                        </div>
                    </div>
                </form>
            </main>
        );
    }
}
export default News;