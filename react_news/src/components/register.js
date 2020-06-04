import React from 'react';
import Login from './login';
import './register.css';
import i18next from "i18next";

class Register extends React.Component {

    handleSubmit = async (event) => {
        event.preventDefault();
    }

    render() {
        return (
            <div className="card bg-light not-bordered">
                <article className="card-body mx-auto">
                    <h4 className="card-title mt-3 text-center">{i18next.t('create_account')}</h4>
                    <form onSubmit={this.handleSubmit}>
                        <div className="form-group input-group">
                            <input required={true} name="first_name" className="form-control" placeholder={i18next.t('name')} type="text" />
                        </div>
                        <div className="form-group input-group">
                            <input required={true} name="last_name" className="form-control" placeholder={i18next.t('surname')} type="text" />
                        </div>
                        <div className="form-group input-group">
                            <input required={true} name="login" className="form-control" placeholder={i18next.t('username')} type="text" />
                        </div>
                        <div className="form-group input-group">
                            <input required={true} name="password" className="form-control" placeholder={i18next.t('password')} type="password" />
                        </div>

                        <div className="form-group input-group">
                            <input required={true} name="repassword" className="form-control" placeholder={i18next.t('repeat_password')} type="password" />
                        </div>

                        <div className="form-group">
                            <button className="btn btn-primary btn-block">{i18next.t('submit')}</button>
                        </div>
                        <p className="text-center">{i18next.t('have_account')}<button className="nav-btn" onClick={() => { this.props.setPage(Login, { login: this.props.param.login }) }}>{i18next.t('login')}</button> </p>
                    </form>
                </article>
            </div>
        );
    }

}

export default Register;