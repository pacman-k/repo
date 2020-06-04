import React from "react";
import './header.css';
import i18next from "i18next";
import EN_F from '../img/flag_velikobritanii.jpg';
import RU_F from '../img/russian_flag.jpg';
import Login from './login';
import Register from './register';
import NewsMain from "./news/newsMain"



const Header = (props) => (
    <header className="header">
        <div className="layout navbar">
            <div>
                <button className="title-btn" onClick={props.setPage.bind(this, NewsMain)}>News Portal</button>
            </div>
            <div className="fixed-top">
                <button className="lang-button" onClick={() => {props.changeLanguage('en')}}><img className="image" src={EN_F} alt="EN" /></button>
                <button className="lang-button" onClick={()=>{props.changeLanguage('ru')}}><img className="image" src={RU_F} alt="RU" /></button>
            </div>
            {!props.user.isloged ?
                <div>
                    <button className="action-button" onClick={() =>{props.setPage(Login, {login: props.login})}}>{i18next.t('login')}</button>
                    <button className="action-button" onClick={()=>{props.setPage(Register, {login: props.login})}}>{i18next.t('register')}</button>
                </div>
                : <div>
                    <div className="font-italic">{props.user.login}</div>
                    <button className="action-button" onClick={() => {
                        props.logout();
                        props.setPage(Login, {login: props.login});
                    }}>
                        {i18next.t('logout')}
                    </button>
                </div>
            }
        </div>
    </header>
);

export default Header;