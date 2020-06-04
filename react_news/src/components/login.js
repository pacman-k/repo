import React from "react";
import i18next from "i18next";
import Register from './register';
import NewsMain from './news/newsMain';
import './login.css';


const Login = (props) => {

    const handleSubmit = async (event) => {
        event.preventDefault();
        var user = {
            isloged: true,
            id: 4,
            login: event.target.elements.login.value,
            role: "admin"
        }
        props.param.login(user)
        props.setPage(NewsMain);
    }

    return (
        <div className="card bg-light not-bordered">
            <article className="card-body mx-auto">
                <h4 className="card-title mt-3 text-center">{i18next.t('login')}</h4>
                <form onSubmit={handleSubmit}>
                    <div className="form-group input-group">
                        <input required={true} name="login" className="form-control" placeholder={i18next.t('username')} type="text" />
                    </div>
                    <div className="form-group input-group">
                        <input required={true} name="password" className="form-control" placeholder={i18next.t('password')} type="password" />
                    </div>
                    <div className="form-group">
                        <button className="btn btn-primary btn-block">{i18next.t('signin')}</button>
                    </div>
                    <p className="text-center">{i18next.t('have_account')}<button className="nav-btn" onClick={() => { props.setPage(Register, { login: props.param.login }) }}>{i18next.t('signup')}</button> </p>
                </form>
            </article>
        </div>
    );
}


export default Login;