import React from 'react';
import API from '../API';
import Author from './author';
import AuthorForm from './authorForm';

class Authors extends React.Component {
    state = {
        authors: []
    }

    async  componentDidMount() {
        const resp = await API.get('authors');
        this.setState({ authors: resp.data })

    }

    updateAuthor = async (author) => {
        const authorResp = await API.put('authors', author);
        if (authorResp.status === 200) {
            const authorsResp = await API.get('authors');
            this.setState({ authors: authorsResp.data })
        }
        return authorResp
    }

    createAuthor = async (event) => {
        event.preventDefault();
        const authorResp = await API.post('authors', {
            name: event.target.elements.name.value,
            surname: event.target.elements.surname.value
        });
        if (authorResp.status === 200) {
            const authorsResp = await API.get('authors');
            this.setState({ authors: authorsResp.data })
        }
    }


    render() {

        return (
            <div style={{marginBottom: '50px'}}>
                <AuthorForm save={this.createAuthor} />
                {this.state.authors.map(author =>
                    <div key={author.id} className='container'>
                        <Author author={author} update={this.updateAuthor} />
                    </div>)
                }
            </div>
        );
    }
}

export default Authors;