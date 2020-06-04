import React from 'react';
import API from '../API';
import Tag from './tag';
import './tags.css';
import TagForm from './tagForm';
class Tags extends React.Component {
    state = {
        tags: []
    }

    async  componentDidMount() {
        const resp = await API.get('tags');
        this.setState({ tags: resp.data })

    }

    updateTag = async (tag) => {
        const tagResp = await API.put('tags', tag); 
        if (tagResp.status === 200) {
            const tagsResp = await API.get('tags');
            this.setState({ tags: tagsResp.data })
        }
       return tagResp
    }

    createTag = async (event) => {
        event.preventDefault();
        const tagResp = await API.post('tags', {
            name: event.target.elements.name.value
        });
        if (tagResp.status === 200) {
            const tagsResp = await API.get('tags');
            this.setState({ tags: tagsResp.data })
        }
    }


    render() {
        return (
            <div style={{marginBottom: '50px'}}>
                <TagForm save={this.createTag} />
                {this.state.tags.map(tag =>
                    <div key={tag.id} className='container'>
                        <Tag tag={tag} update={this.updateTag} />
                    </div>)
                }
            </div>
        );
    }
}

export default Tags;