import React from "react";
import Dashboard from './dashboard';
import Header from './header';
import './main.css';

class Main extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            CurrentPage: props.page,
            param: undefined,
            setLanguage: props.setLanguage,
            login: props.login,
            logout: props.logout
        }       
    }

    setPage = (page, metParam = undefined) => {
        this.setState({ CurrentPage: page, param: metParam })
        
    }

    render() {
        return (
            <div>
                <Header changeLanguage={this.state.setLanguage} setPage={this.setPage} user={this.props.user}
                 login={this.state.login} logout={this.state.logout}/>
                <div className="container left-non ">
                    <Dashboard user={this.props.user} setPage={this.setPage} />
                    <div className="page">
                        <this.state.CurrentPage param={this.state.param}
                            user={this.props.user} setPage={this.setPage} />
                    </div>
                </div>
            </div>
        );
    }
}


export default Main;
