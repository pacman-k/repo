import React from 'react';
import Main, { setPage } from './components/main';
import './App.css';
import i18next from 'i18next';
import NewsMain from './components/news/newsMain';
import Footer from './components/footer';


class App extends React.Component {

  constructor() {
    super();
    this.state = {
      _isMounted: false,
      initialPage: NewsMain,
      user: {
        isloged: true,
        id: undefined,
        login: 'DanyBoy',
        role: 'admin'
      }
    }
    this.setLanguage('en');
  }
  isMounted() {
    return this.state._isMounted;
  }

  componentDidMount() {
    this.state._isMounted = true;
  }

  setLanguage = (lang) => {
    i18next.init({
      lng: lang,
      resources: require(`./localization/${lang}.json`)
    })
    this.isMounted() && this.forceUpdate();
  }

  login = (user) => {
    this.setState({ user: user })
  }

  logout = () => {
    this.setState({
      user: {
        isloged: false,
        id: undefined,
        login: "",
        role: ""
      }
    })
  }

  render() {
    return (
      <div>
        <Main user={this.state.user} page={this.state.initialPage} setLanguage={this.setLanguage}
         login={this.login} logout={this.logout}/>
        <Footer />
      </div>
    );
  }
}

export default App;
