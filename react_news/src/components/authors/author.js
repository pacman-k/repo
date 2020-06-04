import React from 'react';

class Author extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            isActive: false,
            update: props.update
        }
    }

    activeClick = () => {
        this.setState({ isActive: true })
    }

    cancelClick = () => {
        this.refs.name.value = this.props.author.name;
        this.refs.surname.value = this.props.author.surname;
        this.setState({ isActive: false })
    }

    submitClick = async (event) => {
        event.preventDefault();
        var resp = await this.state.update({
            id: this.props.author.id,
            name: this.refs.name.value,
            surname: this.refs.surname.value
        })
        this.refs.name.value = resp.data.name
        this.refs.surname.value = resp.data.surname
        this.setState({ isActive: false })
    }


    render() {

        return (
            <div className="nav-scroller" style={{ marginTop: '20px' }}>
                <div>
                    <input ref='name' disabled={!this.state.isActive} defaultValue={this.props.author.name} />
                    <input ref='surname' disabled={!this.state.isActive} defaultValue={this.props.author.surname} />
                    {this.state.isActive ?
                        <div className='btn-group'>
                            <button className='btn btn-primary fun-btn' onClick={this.submitClick} type="submit">submit</button>
                            <button className='btn btn-danger fun-btn' type="button" onClick={this.cancelClick}>cancel</button>
                        </div>
                        :
                        <button className='btn btn-primary fun-btn' type="button" onClick={this.activeClick}>update</button>
                    }
                </div>
            </div>
        );
    }
}

export default Author;