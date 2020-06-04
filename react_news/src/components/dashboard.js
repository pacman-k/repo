import React from 'react';
import './dashboard.css';
import i18news from 'i18next';
import Tags from './tags/tags';
import Authors from './authors/authors';
import CreateNews from './news/createNews';

const Dashboard = (props) => (
    <div>
        {props.user.isloged &&
            <div className="dashboard">
                <header>dashboard</header>
                <br />
                <div >
                    {props.user.role === 'admin' ?
                        <div className="btn-group-vertical">
                            <button type="button" onClick={() => { props.setPage(Authors) }} className="btn btn-primary">{i18news.t('add_edit_authors')}</button>
                            <br />
                            <button type="button" onClick={() => { props.setPage(CreateNews) }} className="btn btn-primary">{i18news.t('add_news')}</button>
                            <br />
                            <button type="button" onClick={() => { props.setPage(Tags) }} className="btn btn-primary">{i18news.t('add_edit_tags')}</button>
                        </div>
                        : <div className="btn-group-vertical">
                            <button type="button" onClick={() => { props.setPage(CreateNews) }} className="btn btn-primary">{i18news.t('add_news')}</button>
                        </div>
                    }
                </div>
            </div>}
    </div>
);


export default Dashboard;
